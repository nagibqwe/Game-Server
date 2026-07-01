package com.kits.project.serverListConfig.x8app.controller;

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
import com.kits.project.serverListConfig.x8app.domain.TX8App;
import com.kits.project.serverListConfig.x8app.service.ITX8AppService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 游戏应用Controller
 * 
 * @author gm
 * @date 2021-07-08
 */
@Controller
@RequestMapping("/serverListConfig/x8app")
public class TX8AppController extends BaseController
{
    private String prefix = "serverListConfig/x8app";

    @Autowired
    private ITX8AppService tX8AppService;

    @RequiresPermissions("serverListConfig:x8app:view")
    @GetMapping()
    public String x8app()
    {
        return prefix + "/x8app";
    }

    /**
     * 查询游戏应用列表
     */
    @RequiresPermissions("serverListConfig:x8app:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TX8App tX8App)
    {
        startPage();
        List<TX8App> list = tX8AppService.selectTX8AppList(tX8App);
        return getDataTable(list);
    }

    /**
     * 导出游戏应用列表
     */
    @RequiresPermissions("serverListConfig:x8app:export")
    @Log(title = "游戏应用", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TX8App tX8App)
    {
        List<TX8App> list = tX8AppService.selectTX8AppList(tX8App);
        ExcelUtil<TX8App> util = new ExcelUtil<TX8App>(TX8App.class);
        return util.exportExcel(list, "游戏应用数据");
    }

    /**
     * 新增游戏应用
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存游戏应用
     */
    @RequiresPermissions("serverListConfig:x8app:add")
    @Log(title = "游戏应用", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TX8App tX8App)
    {
        return toAjax(tX8AppService.insertTX8App(tX8App));
    }

    /**
     * 修改游戏应用
     */
    @GetMapping("/edit/{appId}")
    public String edit(@PathVariable("appId") Long appId, ModelMap mmap)
    {
        TX8App tX8App = tX8AppService.selectTX8AppById(appId);
        mmap.put("tX8App", tX8App);
        return prefix + "/edit";
    }

    /**
     * 修改保存游戏应用
     */
    @RequiresPermissions("serverListConfig:x8app:edit")
    @Log(title = "游戏应用", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TX8App tX8App)
    {
        return toAjax(tX8AppService.updateTX8App(tX8App));
    }

    /**
     * 删除游戏应用
     */
    @RequiresPermissions("serverListConfig:x8app:remove")
    @Log(title = "游戏应用", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tX8AppService.deleteTX8AppByIds(ids));
    }
}
