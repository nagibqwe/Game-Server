package com.kits.project.serverListConfig.x8server.controller;

import java.util.List;
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
import com.kits.project.serverListConfig.x8server.domain.TX8GameServer;
import com.kits.project.serverListConfig.x8server.service.ITX8GameServerService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 游戏区服Controller
 * 
 * @author gm
 * @date 2021-07-08
 */
@Controller
@RequestMapping("/serverListConfig/x8server")
public class TX8GameServerController extends BaseController
{
    private String prefix = "serverListConfig/x8server";

    @Autowired
    private ITX8GameServerService tX8GameServerService;

    @RequiresPermissions("serverListConfig:x8server:view")
    @GetMapping()
    public String x8server()
    {
        return prefix + "/x8server";
    }

    /**
     * 查询游戏区服列表
     */
    @RequiresPermissions("serverListConfig:x8server:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TX8GameServer tX8GameServer)
    {
        startPage();
        List<TX8GameServer> list = tX8GameServerService.selectTX8GameServerList(tX8GameServer);
        return getDataTable(list);
    }

    /**
     * 导出游戏区服列表
     */
    @RequiresPermissions("serverListConfig:x8server:export")
    @Log(title = "游戏区服", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TX8GameServer tX8GameServer)
    {
        List<TX8GameServer> list = tX8GameServerService.selectTX8GameServerList(tX8GameServer);
        ExcelUtil<TX8GameServer> util = new ExcelUtil<TX8GameServer>(TX8GameServer.class);
        return util.exportExcel(list, "游戏区服数据");
    }

    /**
     * 新增游戏区服
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存游戏区服
     */
    @RequiresPermissions("serverListConfig:x8server:add")
    @Log(title = "游戏区服", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TX8GameServer tX8GameServer)
    {
        return toAjax(tX8GameServerService.insertTX8GameServer(tX8GameServer));
    }

    /**
     * 修改游戏区服
     */
    @GetMapping("/edit/{svrId}")
    public String edit(@PathVariable("svrId") Long svrId, ModelMap mmap)
    {
        TX8GameServer tX8GameServer = tX8GameServerService.selectTX8GameServerById(svrId);
        mmap.put("tX8GameServer", tX8GameServer);
        return prefix + "/edit";
    }

    /**
     * 修改保存游戏区服
     */
    @RequiresPermissions("serverListConfig:x8server:edit")
    @Log(title = "游戏区服", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TX8GameServer tX8GameServer)
    {
        return toAjax(tX8GameServerService.updateTX8GameServer(tX8GameServer));
    }

    /**
     * 删除游戏区服
     */
    @RequiresPermissions("serverListConfig:x8server:remove")
    @Log(title = "游戏区服", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tX8GameServerService.deleteTX8GameServerByIds(ids));
    }
}
