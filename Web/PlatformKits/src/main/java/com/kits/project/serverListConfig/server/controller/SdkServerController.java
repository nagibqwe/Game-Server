package com.kits.project.serverListConfig.server.controller;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kits.common.utils.MessageUtils;
import com.kits.common.utils.StringUtils;
import com.kits.common.utils.text.Convert;
import com.kits.framework.aspectj.lang.annotation.DataSource;
import com.kits.framework.aspectj.lang.enums.DataSourceType;
import com.kits.framework.config.PlatformKitsConfig;
import com.kits.project.monitor.server.domain.Sys;
import com.kits.project.serverListConfig.serverExtra.service.ISdkServerExtraService;
import com.kits.project.serverListConfig.serverList.domain.SdkServerList;
import com.kits.project.serverListConfig.serverList.service.ISdkServerListService;
import com.kits.project.serverListConfig.serverUpdate.domain.SdkServerUpdate;
import com.kits.project.serverListConfig.serverUpdate.service.ISdkServerUpdateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.kits.framework.aspectj.lang.annotation.Log;
import com.kits.framework.aspectj.lang.enums.BusinessType;
import com.kits.project.serverListConfig.server.domain.SdkServer;
import com.kits.project.serverListConfig.server.service.ISdkServerService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 服务器配置信息Controller
 * 
 * @author gm
 * @date 2021-04-25
 */
@Controller
@RequestMapping("/serverListConfig/server")
public class SdkServerController extends BaseController
{
    private String prefix = "serverListConfig/server";

    @Autowired
    private ISdkServerService sdkServerService;

    @Autowired
    private ISdkServerListService sdkServerListService;

    @Autowired
    private ISdkServerExtraService sdkServerExtraService;

    @Autowired
    private PlatformKitsConfig PlatformKitsConfig;

    @RequiresPermissions("serverListConfig:server:view")
    @GetMapping()
    public String server()
    {
        return prefix + "/server";
    }

    /**
     * 查询服务器配置信息列表
     */
    @RequiresPermissions("serverListConfig:server:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SdkServer sdkServer)
    {
        startPage();
        List<SdkServer> list = sdkServerService.selectSdkServerList(sdkServer);
        return getDataTable(list);
    }

    @PostMapping("/reloadCache")
    @ResponseBody
    public AjaxResult reloadCache()
    {
        sdkServerService.reloadAllCache(System.currentTimeMillis());
        return AjaxResult.success();
    }

    /**
     * 导出服务器配置信息列表
     */
    @RequiresPermissions("serverListConfig:server:export")
    @Log(title = "服务器配置信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SdkServer sdkServer)
    {
        List<SdkServer> list = sdkServerService.selectSdkServerList(sdkServer);
        ExcelUtil<SdkServer> util = new ExcelUtil<SdkServer>(SdkServer.class);
        return util.exportExcel(list, "服务器配置信息数据");
    }

    /**
     * 新增服务器配置信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存服务器配置信息
     */
    @RequiresPermissions("serverListConfig:server:add")
    @Log(title = "服务器配置信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SdkServer sdkServer)
    {
        return toAjax(sdkServerService.insertSdkServer(sdkServer));
    }

    /**
     * 修改服务器配置信息
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SdkServer sdkServer = sdkServerService.selectSdkServerById(id);
        mmap.put("sdkServer", sdkServer);
        return prefix + "/edit";
    }

    /**
     * 修改保存服务器配置信息
     */
    @RequiresPermissions("serverListConfig:server:edit")
    @Log(title = "服务器配置信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SdkServer sdkServer)
    {
        return toAjax(sdkServerService.updateSdkServer(sdkServer));
    }

    /**
     * 删除服务器配置信息
     */
    @RequiresPermissions("serverListConfig:server:remove")
    @Log(title = "服务器配置信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        //根据页面传的ids查询需要删除的serverId列表
        List<Long> serverIdLongList = sdkServerService.selectServerIdsByIds(ids);
        String serverIdsDel = StringUtils.longListToString(serverIdLongList);
        List<String> serverIdStrListDel = Arrays.asList(Convert.toStrArray(serverIdsDel));
        //删除服务器额外信息
        HashMap map = new HashMap();
        map.put("serverIds", Convert.toStrArray(serverIdsDel));
        sdkServerExtraService.deleteServerExtraByServerId(map);

        //查询全部服务器列表数据
        SdkServerList sdkServerList = new SdkServerList();
        List<SdkServerList> serverLists = sdkServerListService.selectSdkServerListList(sdkServerList);
        if (serverLists != null && serverLists.size() > 0){
            for (int i = 0; i < serverLists.size(); i++){
                String serverIds = serverLists.get(i).getServerIds();
                Long id = serverLists.get(i).getId();
                List<String> serverIdsList = Arrays.asList(Convert.toStrArray(serverIds));
                List<String> serverIdsList2 = new ArrayList(serverIdsList);
                for (int j = 0; j < serverIdStrListDel.size(); j++){
                    if (serverIdsList2.contains(serverIdStrListDel.get(j))){
                        serverIdsList2.remove(serverIdStrListDel.get(j));//修改服务器列表中server_ids数据
                    }
                }
                SdkServerList sdkServerList1 = new SdkServerList();
                sdkServerList1.setId(id);
                sdkServerList1.setServerIds(StringUtils.stringListToString(serverIdsList2));
                sdkServerListService.updateSdkServerList(sdkServerList1,0);
            }
        }
        //删除服务器信息
        sdkServerService.deleteSdkServerByIds(ids);
        return AjaxResult.success();
    }

    /**
     * 获取可选服务器
     * @param serverListId
     * @return
     */
    @PostMapping("/selectServers")
    @ResponseBody
    public List<SdkServer> selectServers(long serverListId)
    {
        String serverIds = sdkServerListService.selectServerIdsById(serverListId);
        List<SdkServer> sdkServerList = sdkServerService.selectServerByServerIds(StringUtils.nvl(serverIds,""));
        return sdkServerList;
    }

    /**
     * 加载所有服务器信息
     * @return
     */
    @PostMapping("/selectServersInfo")
    @ResponseBody
    public List<SdkServer> selectServersInfo()
    {
        SdkServer sdkServer = new SdkServer();
        List<SdkServer> list = sdkServerService.selectSdkServerList(sdkServer);
        return list;
    }

    /**
     * 检测服务器ID是否存在
     * @param serverId
     * @return
     */
    @PostMapping("/checkServerId")
    @ResponseBody
    public AjaxResult checkServerId(String serverId)
    {
        int count = 0;
        SdkServer sdkServer = new SdkServer();
        sdkServer.setServerId(Long.valueOf(serverId));
        List<SdkServer> list = sdkServerService.selectSdkServerList(sdkServer);
        AjaxResult ajaxResult = new AjaxResult();
        if (null != list && list.size() > 0){
            count = list.size();
            ajaxResult.put("count",count);
            ajaxResult.put("msg", MessageUtils.message("serverList.serverIdHasExist"));
        }else {
            ajaxResult.put("count",count);
            ajaxResult.put("msg", MessageUtils.message("serverList.serverIdEnable"));
        }
        return ajaxResult;
    }

    /**
     * 刷新服务器配置的数据库和服务器配置的缓存
     * @return
     */
    @GetMapping("/refreshServer")
    @ResponseBody
    public AjaxResult refreshServer(HttpServletRequest request, HttpServletResponse response)
    {
        String targetId = request.getParameter("targetId");
        String fromIds = request.getParameter("fromIds");
        String secretKey = request.getParameter("secret_key");
        if (null == targetId || "".equals(targetId)){
            return AjaxResult.error("目标服务器ID为空");
        }
        if (null == fromIds || "".equals(fromIds)){
            return AjaxResult.error("被合服的服务器ID为空");
        }
        if (null == secretKey || "".equals(secretKey)){
            return AjaxResult.error("密钥为空");
        }
        if (PlatformKitsConfig.getSecretKey().equals(secretKey)){//校验密钥是否相同(相同时才可以操作)
            SdkServer sdkServer = new SdkServer();
            sdkServer.setServerId(Long.valueOf(targetId));
            List<SdkServer> servers = sdkServerService.selectSdkServerList(sdkServer);
            if (null != servers && servers.size() > 0){
                SdkServer server = servers.get(0);
                String serverIp = server.getServerIp();
                Long serverPort = server.getServerPort();
                Map<String,Object> paramMap = new HashMap();
                paramMap.put("serverIp",serverIp);
                paramMap.put("serverPort",serverPort);
                paramMap.put("serverIds",Convert.toStrArray(fromIds));
                sdkServerService.updateSdkServerByServerId(paramMap);//更新数据库的数据
                Long curTime = System.currentTimeMillis();
                sdkServerService.reloadAllCache(curTime);//更新缓存
                sdkServerService.setServerUpdateTime(curTime);
            }
            return AjaxResult.success();
        }else {
            return AjaxResult.error();
        }
    }
}
