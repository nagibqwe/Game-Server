package com.kits.project.gmtool.server.controller;

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
import com.kits.project.gmtool.server.domain.TServer;
import com.kits.project.gmtool.server.service.ITServerService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 服务器列Controller
 * 
 * @author gm
 * @date 2021-04-28
 */
@Controller
@RequestMapping("/gmtool/server")
public class TServerController extends BaseController
{
    private String prefix = "gmtool/server";

    @Autowired
    private ITServerService tServerService;

    @RequiresPermissions("gmtool:server:view")
    @GetMapping()
    public String server()
    {
        return prefix + "/server";
    }

    /**
     * 查询服务器列列表
     */
    @RequiresPermissions("gmtool:server:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TServer tServer)
    {
        startPage();
        List<TServer> list = tServerService.selectTServerList(tServer);
        return getDataTable(list);
    }

    /**
     * 导出服务器列列表
     */
    @RequiresPermissions("gmtool:server:export")
    @Log(title = "服务器列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TServer tServer)
    {
        List<TServer> list = tServerService.selectTServerList(tServer);
        ExcelUtil<TServer> util = new ExcelUtil<TServer>(TServer.class);
        return util.exportExcel(list, "服务器列数据");
    }

    /**
     * 新增服务器列
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存服务器列
     */
    @RequiresPermissions("gmtool:server:add")
    @Log(title = "服务器列", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TServer tServer)
    {
        return toAjax(tServerService.insertTServer(tServer));
    }

    /**
     * 修改服务器列
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TServer tServer = tServerService.selectTServerById(id);
        mmap.put("tServer", tServer);
        return prefix + "/edit";
    }

    /**
     * 修改保存服务器列
     */
    @RequiresPermissions("gmtool:server:edit")
    @Log(title = "服务器列", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TServer tServer)
    {
        return toAjax(tServerService.updateTServer(tServer));
    }

    /**
     * 删除服务器列
     */
    @RequiresPermissions("gmtool:server:remove")
    @Log(title = "服务器列", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tServerService.deleteTServerByIds(ids));
    }
}
