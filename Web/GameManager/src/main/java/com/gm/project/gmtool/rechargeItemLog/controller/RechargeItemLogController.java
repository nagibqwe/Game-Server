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
 * 充值配置日志Controller
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
     * 查询充值配置日志列表
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
     * 导出充值配置日志列表
     */
    @RequiresPermissions("gmtool:rechargeItemLog:export")
    @Log(title = "充值配置日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(RechargeItemLog rechargeItemLog)
    {
        List<RechargeItemLog> list = rechargeItemLogService.selectRechargeItemLogList(rechargeItemLog);
        ExcelUtil<RechargeItemLog> util = new ExcelUtil<RechargeItemLog>(RechargeItemLog.class);
        return util.exportExcel(list, "充值配置日志数据");
    }

    /**
     * 新增充值配置日志
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存充值配置日志
     */
    @RequiresPermissions("gmtool:rechargeItemLog:add")
    @Log(title = "充值配置日志", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(RechargeItemLog rechargeItemLog)
    {
        return toAjax(rechargeItemLogService.insertRechargeItemLog(rechargeItemLog));
    }

    /**
     * 修改充值配置日志
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        RechargeItemLog rechargeItemLog = rechargeItemLogService.selectRechargeItemLogById(id);
        mmap.put("rechargeItemLog", rechargeItemLog);
        return prefix + "/edit";
    }

    /**
     * 修改保存充值配置日志
     */
    @RequiresPermissions("gmtool:rechargeItemLog:edit")
    @Log(title = "充值配置日志", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(RechargeItemLog rechargeItemLog)
    {
        return toAjax(rechargeItemLogService.updateRechargeItemLog(rechargeItemLog));
    }

    /**
     * 删除充值配置日志
     */
    @RequiresPermissions("gmtool:rechargeItemLog:remove")
    @Log(title = "充值配置日志", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(rechargeItemLogService.deleteRechargeItemLogByIds(ids));
    }
}
