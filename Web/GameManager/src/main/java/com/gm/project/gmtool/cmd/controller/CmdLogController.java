package com.gm.project.gmtool.cmd.controller;

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
import com.gm.project.gmtool.cmd.domain.CmdLog;
import com.gm.project.gmtool.cmd.service.ICmdLogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 热更服务器操作日志Controller
 * 
 * @author gm
 * @date 2021-07-30
 */
@Controller
@RequestMapping("/gmtool/cmd")
public class CmdLogController extends BaseController
{
    private String prefix = "gmtool/cmd";

    @Autowired
    private ICmdLogService cmdLogService;

    @RequiresPermissions("gmtool:cmd:view")
    @GetMapping()
    public String cmd()
    {
        return prefix + "/cmd";
    }

    /**
     * 查询热更服务器操作日志列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CmdLog cmdLog)
    {
        startPage();
        cmdLog.setGmType(0);
        List<CmdLog> list = cmdLogService.selectCmdLogList(cmdLog);
        return getDataTable(list);
    }

    /**
     * 公共服指令页面
     * @return
     */
    @RequiresPermissions("gmtool:cmd:psCmd")
    @GetMapping("/psCmd")
    public String psCmd()
    {
        return prefix + "/psCmd";
    }

    /**
     * 查询热更公共服操作日志列表
     */
    @PostMapping("/pcList")
    @ResponseBody
    public TableDataInfo pcList(CmdLog cmdLog)
    {
        startPage();
        cmdLog.setGmType(1);
        List<CmdLog> list = cmdLogService.selectCmdLogList(cmdLog);
        return getDataTable(list);
    }

    /**
     * 设置开服时间页面
     * @return
     */
    @RequiresPermissions("gmtool:cmd:opstime")
    @GetMapping("/opstime")
    public String opstime()
    {
        return prefix + "/opstime";
    }

    /**
     * 导出热更服务器操作日志列表
     */
    @RequiresPermissions("gmtool:cmd:export")
    @Log(title = "热更服务器操作日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CmdLog cmdLog)
    {
        List<CmdLog> list = cmdLogService.selectCmdLogList(cmdLog);
        ExcelUtil<CmdLog> util = new ExcelUtil<CmdLog>(CmdLog.class);
        return util.exportExcel(list, "热更服务器操作日志数据");
    }

    /**
     * 新增热更服务器操作日志
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存热更服务器操作日志
     */
    @RequiresPermissions("gmtool:cmd:add")
    @Log(title = "热更服务器操作日志", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CmdLog cmdLog)
    {
        return toAjax(cmdLogService.insertCmdLog(cmdLog));
    }

    /**
     * 修改热更服务器操作日志
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        CmdLog cmdLog = cmdLogService.selectCmdLogById(id);
        mmap.put("cmdLog", cmdLog);
        return prefix + "/edit";
    }

    /**
     * 修改保存热更服务器操作日志
     */
    @RequiresPermissions("gmtool:cmd:edit")
    @Log(title = "热更服务器操作日志", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CmdLog cmdLog)
    {
        return toAjax(cmdLogService.updateCmdLog(cmdLog));
    }

    /**
     * 删除热更服务器操作日志
     */
    @RequiresPermissions("gmtool:cmd:remove")
    @Log(title = "热更服务器操作日志", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(cmdLogService.deleteCmdLogByIds(ids));
    }
}
