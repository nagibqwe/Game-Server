package com.gm.project.gmtool.rechargeItemLog.controller;

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
import com.gm.project.gmtool.rechargeItemLog.domain.RechargeItemLog;
import com.gm.project.gmtool.rechargeItemLog.service.IRechargeItemLogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * Журнал настроек пополненияController
 * 
 * @author gm
 * @date 2021-08-25
 */
@Controller
@RequestMapping("/gmtool/rechargeItemLog")
public class RechargeItemLogController extends BaseController
{
    private String prefix = "gmtool/rechargeItemLog";

    @Autowired
    private IRechargeItemLogService rechargeItemLogService;

    @RequiresPermissions("gmtool:rechargeItemLog:view")
    @GetMapping()
    public String rechargeItemLog()
    {
        return prefix + "/rechargeItemLog";
    }

    /**
     * 查询Журнал настроек пополнения列表
     */
//    @RequiresPermissions("gmtool:rechargeItemLog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(RechargeItemLog rechargeItemLog)
    {
        startPage();
        List<RechargeItemLog> list = rechargeItemLogService.selectRechargeItemLogList(rechargeItemLog);
        return getDataTable(list);
    }

    /**
     * ЭкспортЖурнал настроек пополнения列表
     */
    @RequiresPermissions("gmtool:rechargeItemLog:export")
    @Log(title = "Журнал настроек пополнения", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RechargeItemLog rechargeItemLog)
    {
        List<RechargeItemLog> list = rechargeItemLogService.selectRechargeItemLogList(rechargeItemLog);
        ExcelUtil<RechargeItemLog> util = new ExcelUtil<RechargeItemLog>(RechargeItemLog.class);
        return util.exportExcel(list, "Журнал настроек пополненияДанные");
    }

    /**
     * ДобавитьЖурнал настроек пополнения
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * ДобавитьСохранитьЖурнал настроек пополнения
     */
    @RequiresPermissions("gmtool:rechargeItemLog:add")
    @Log(title = "Журнал настроек пополнения", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(RechargeItemLog rechargeItemLog)
    {
        return toAjax(rechargeItemLogService.insertRechargeItemLog(rechargeItemLog));
    }

    /**
     * ИзменитьЖурнал настроек пополнения
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        RechargeItemLog rechargeItemLog = rechargeItemLogService.selectRechargeItemLogById(id);
        mmap.put("rechargeItemLog", rechargeItemLog);
        return prefix + "/edit";
    }

    /**
     * ИзменитьСохранитьЖурнал настроек пополнения
     */
    @RequiresPermissions("gmtool:rechargeItemLog:edit")
    @Log(title = "Журнал настроек пополнения", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(RechargeItemLog rechargeItemLog)
    {
        return toAjax(rechargeItemLogService.updateRechargeItemLog(rechargeItemLog));
    }

    /**
     * УдалитьЖурнал настроек пополнения
     */
    @RequiresPermissions("gmtool:rechargeItemLog:remove")
    @Log(title = "Журнал настроек пополнения", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(rechargeItemLogService.deleteRechargeItemLogByIds(ids));
    }
}
