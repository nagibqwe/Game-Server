package com.gm.project.gmtool.gmlog.controller;

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
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.project.gmtool.gmlog.domain.GMLog;
import com.gm.project.gmtool.gmlog.service.IGMLogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * Журнал GM-панелиController
 * 
 * @author gm
 * @date 2021-09-01
 */
@Controller
@RequestMapping("/gmtool/gmlog")
public class GMLogController extends BaseController
{
    private String prefix = "gmtool/gmlog";

    @Autowired
    private IGMLogService gMLogService;

    @RequiresPermissions("gmtool:gmlog:view")
    @GetMapping()
    public String gmlog()
    {
        return prefix + "/gmlog";
    }

    /**
     * 查询Журнал GM-панели列表
     */
    @RequiresPermissions("gmtool:gmlog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(GMLog gMLog)
    {
        startPage();
        List<GMLog> list = gMLogService.selectGMLogList(gMLog);
        return getDataTable(list);
    }

    /**
     * ЭкспортЖурнал GM-панели列表
     */
    @RequiresPermissions("gmtool:gmlog:export")
    @Log(title = "Журнал GM-панели", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(GMLog gMLog)
    {
        List<GMLog> list = gMLogService.selectGMLogList(gMLog);
        ExcelUtil<GMLog> util = new ExcelUtil<GMLog>(GMLog.class);
        return util.exportExcel(list, "Журнал GM-панелиДанные");
    }

    /**
     * ДобавитьЖурнал GM-панели
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * ДобавитьСохранитьЖурнал GM-панели
     */
    @RequiresPermissions("gmtool:gmlog:add")
    @Log(title = "Журнал GM-панели", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(GMLog gMLog)
    {
        return toAjax(gMLogService.insertGMLog(gMLog));
    }

    /**
     * ИзменитьЖурнал GM-панели
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        GMLog gMLog = gMLogService.selectGMLogById(id);
        mmap.put("gMLog", gMLog);
        return prefix + "/edit";
    }

    /**
     * ИзменитьСохранитьЖурнал GM-панели
     */
    @RequiresPermissions("gmtool:gmlog:edit")
    @Log(title = "Журнал GM-панели", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(GMLog gMLog)
    {
        return toAjax(gMLogService.updateGMLog(gMLog));
    }

    /**
     * УдалитьЖурнал GM-панели
     */
    @RequiresPermissions("gmtool:gmlog:remove")
    @Log(title = "Журнал GM-панели", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(gMLogService.deleteGMLogByIds(ids));
    }
}
