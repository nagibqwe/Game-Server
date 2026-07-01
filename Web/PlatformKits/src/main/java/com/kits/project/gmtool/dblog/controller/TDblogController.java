package com.kits.project.gmtool.dblog.controller;

import java.util.ArrayList;
import java.util.List;


import com.kits.common.utils.StringUtils;
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
import com.kits.project.gmtool.dblog.domain.TDblog;
import com.kits.project.gmtool.dblog.service.ITDblogService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;

/**
 * 日志库列Controller
 * 
 * @author gm
 * @date 2021-04-23
 */
@Controller
@RequestMapping("/gmtool/dblog")
public class TDblogController extends BaseController
{
    private String prefix = "gmtool/dblog";

    @Autowired
    private ITDblogService tDblogService;

    @RequiresPermissions("gmtool:dblog:view")
    @GetMapping()
    public String dblog()
    {
        return prefix + "/dblog";
    }

    @GetMapping("/selectdblog")
    public String selectdblog(String selectServerIdList,ModelMap mmap)
    {
        mmap.put("selectServerIdList",selectServerIdList);
        return prefix + "/selectdblog";
    }

    /**
     * 查询日志库列列表
     */
    @RequiresPermissions("gmtool:dblog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TDblog tDblog)
    {
        startPage();
        List<TDblog> list = tDblogService.selectTDblogList(tDblog);
        return getDataTable(list);
    }

    /**
     * 导出日志库列列表
     */
    @RequiresPermissions("gmtool:dblog:export")
    @Log(title = "日志库列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TDblog tDblog)
    {
        List<TDblog> list = tDblogService.selectTDblogList(tDblog);
        ExcelUtil<TDblog> util = new ExcelUtil<TDblog>(TDblog.class);
        return util.exportExcel(list, "日志库列数据");
    }

    /**
     * 新增日志库列
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存日志库列
     */
    @RequiresPermissions("gmtool:dblog:add")
    @Log(title = "日志库列", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TDblog tDblog)
    {
        return toAjax(tDblogService.insertTDblog(tDblog));
    }

    /**
     * 修改日志库列
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TDblog tDblog = tDblogService.selectTDblogById(id);
        mmap.put("tDblog", tDblog);
        return prefix + "/edit";
    }

    /**
     * 修改保存日志库列
     */
    @RequiresPermissions("gmtool:dblog:edit")
    @Log(title = "日志库列", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TDblog tDblog)
    {
        return toAjax(tDblogService.updateTDblog(tDblog));
    }

    /**
     * 删除日志库列
     */
    @RequiresPermissions("gmtool:dblog:remove")
    @Log(title = "日志库列", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tDblogService.deleteTDblogByIds(ids));
    }
}
