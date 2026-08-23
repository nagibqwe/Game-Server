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
 * Контроллер логов операций горячего обновления сервера
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
     * Получение списка логов операций горячего обновления сервера
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
     * Страница команд публичного сервера
     * @return
     */
    @RequiresPermissions("gmtool:cmd:psCmd")
    @GetMapping("/psCmd")
    public String psCmd()
    {
        return prefix + "/psCmd";
    }

    /**
     * Получение списка логов операций публичного сервера
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
     * Страница установки времени открытия сервера
     * @return
     */
    @RequiresPermissions("gmtool:cmd:opstime")
    @GetMapping("/opstime")
    public String opstime()
    {
        return prefix + "/opstime";
    }

    /**
     * Экспорт списка логов операций горячего обновления сервера
     */
    @RequiresPermissions("gmtool:cmd:export")
    @Log(title = "Логи операций горячего обновления сервера", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CmdLog cmdLog)
    {
        List<CmdLog> list = cmdLogService.selectCmdLogList(cmdLog);
        ExcelUtil<CmdLog> util = new ExcelUtil<CmdLog>(CmdLog.class);
        return util.exportExcel(list, "Данные логов операций горячего обновления");
    }

    /**
     * Добавление лога операций горячего обновления сервера
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Сохранение нового лога операций горячего обновления сервера
     */
    @RequiresPermissions("gmtool:cmd:add")
    @Log(title = "Логи операций горячего обновления сервера", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CmdLog cmdLog)
    {
        return toAjax(cmdLogService.insertCmdLog(cmdLog));
    }

    /**
     * Редактирование лога операций горячего обновления сервера
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        CmdLog cmdLog = cmdLogService.selectCmdLogById(id);
        mmap.put("cmdLog", cmdLog);
        return prefix + "/edit";
    }

    /**
     * Сохранение изменений лога операций горячего обновления сервера
     */
    @RequiresPermissions("gmtool:cmd:edit")
    @Log(title = "Логи операций горячего обновления сервера", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CmdLog cmdLog)
    {
        return toAjax(cmdLogService.updateCmdLog(cmdLog));
    }

    /**
     * Удаление лога операций горячего обновления сервера
     */
    @RequiresPermissions("gmtool:cmd:remove")
    @Log(title = "Логи операций горячего обновления сервера", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(cmdLogService.deleteCmdLogByIds(ids));
    }
}