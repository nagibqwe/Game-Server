package com.kits.project.serverListConfig.loginServer.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kits.common.utils.StringUtils;
import com.kits.common.utils.text.Convert;
import com.kits.project.serverListConfig.serverList.domain.SdkServerList;
import com.kits.project.serverListConfig.serverList.service.ISdkServerListService;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.kits.framework.aspectj.lang.annotation.Log;
import com.kits.framework.aspectj.lang.enums.BusinessType;
import com.kits.project.serverListConfig.loginServer.domain.SdkLoginServer;
import com.kits.project.serverListConfig.loginServer.service.ISdkLoginServerService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 登录服信息Controller
 * 
 * @author gm
 * @date 2021-10-20
 */
@Controller
@RequestMapping("/serverListConfig/loginServer")
public class SdkLoginServerController extends BaseController
{
    private String prefix = "serverListConfig/loginServer";

    @Autowired
    private ISdkLoginServerService sdkLoginServerService;

    @Autowired
    private ISdkServerListService sdkServerListService;

    @RequiresPermissions("serverListConfig:loginServer:view")
    @GetMapping()
    public String loginServer()
    {
        return prefix + "/loginServer";
    }

    /**
     * 查询登录服信息列表
     */
    @RequiresPermissions("serverListConfig:loginServer:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SdkLoginServer sdkLoginServer)
    {
        startPage();
        List<SdkLoginServer> list = sdkLoginServerService.selectSdkLoginServerList(sdkLoginServer);
        return getDataTable(list);
    }

    /**
     * 导出登录服信息列表
     */
    @RequiresPermissions("serverListConfig:loginServer:export")
    @Log(title = "登录服信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SdkLoginServer sdkLoginServer)
    {
        List<SdkLoginServer> list = sdkLoginServerService.selectSdkLoginServerList(sdkLoginServer);
        ExcelUtil<SdkLoginServer> util = new ExcelUtil<SdkLoginServer>(SdkLoginServer.class);
        return util.exportExcel(list, "登录服信息数据");
    }

    /**
     * 新增登录服信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存登录服信息
     */
    @RequiresPermissions("serverListConfig:loginServer:add")
    @Log(title = "登录服信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SdkLoginServer sdkLoginServer)
    {
        return toAjax(sdkLoginServerService.insertSdkLoginServer(sdkLoginServer));
    }

    /**
     * 修改登录服信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SdkLoginServer sdkLoginServer = sdkLoginServerService.selectSdkLoginServerById(id);
        mmap.put("sdkLoginServer", sdkLoginServer);
        return prefix + "/edit";
    }

    /**
     * 修改保存登录服信息
     */
    @RequiresPermissions("serverListConfig:loginServer:edit")
    @Log(title = "登录服信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SdkLoginServer sdkLoginServer)
    {
        return toAjax(sdkLoginServerService.updateSdkLoginServer(sdkLoginServer));
    }

    /**
     * 删除登录服信息
     */
    @RequiresPermissions("serverListConfig:loginServer:remove")
    @Log(title = "登录服信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        List<String> loginServerIdsDel = Arrays.asList(Convert.toStrArray(ids));
        List<String> loginServerIdsStrListDel = new ArrayList(loginServerIdsDel);

        //查询全部服务器列表数据
        SdkServerList sdkServerList = new SdkServerList();
        List<SdkServerList> serverLists = sdkServerListService.selectSdkServerListList(sdkServerList);
        if (serverLists != null && serverLists.size() > 0){
            for (int i = 0; i < serverLists.size(); i++){
                String loginServerIds = serverLists.get(i).getLoginServerGroup();
                Long id = serverLists.get(i).getId();
                List<String> loginServerIdsList = Arrays.asList(Convert.toStrArray(loginServerIds));
                List<String> loginServerIdsList2 = new ArrayList(loginServerIdsList);
                for (int j = 0; j < loginServerIdsStrListDel.size(); j++){
                    if (loginServerIdsList2.contains(loginServerIdsStrListDel.get(j))){
                        loginServerIdsList2.remove(loginServerIdsStrListDel.get(j));//修改服务器列表中login_server_group数据
                    }
                }
                SdkServerList sdkServerList1 = new SdkServerList();
                sdkServerList1.setId(id);
                sdkServerList1.setLoginServerGroup(StringUtils.stringListToString(loginServerIdsList2));
                sdkServerListService.updateSdkServerList(sdkServerList1,3);
            }
        }
        //删除登录服信息
        sdkLoginServerService.deleteSdkLoginServerByIds(ids);
        return AjaxResult.success();
    }

    /**
     * 获取所有登录服信息
     * @return
     */
    @PostMapping("/selectLoginServers")
    @ResponseBody
    public List<SdkLoginServer> selectLoginServers()
    {
        SdkLoginServer sdkLoginServer = new SdkLoginServer();
        List<SdkLoginServer> sdkLoginServers = sdkLoginServerService.selectSdkLoginServerList(sdkLoginServer);
        return sdkLoginServers;
    }

    /**
     * 获取页面展示登录服组(已有的登录服数据)
     * @param serverListId
     * @return
     */
    @PostMapping("/selectShowLoginServers")
    @ResponseBody
    public List<SdkLoginServer> selectShowLoginServers(long serverListId)
    {
        SdkServerList sdkServerList = sdkServerListService.selectSdkServerListById(serverListId);
        String loginServerIds = sdkServerList.getLoginServerGroup();
        List<SdkLoginServer> sdkLoginServers = sdkLoginServerService.selectShowLoginServers(StringUtils.nvl(loginServerIds,""));
        return sdkLoginServers;
    }
}
