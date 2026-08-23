package com.gm.project.gmtool.activeCodebatch.controller;

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
import com.gm.project.gmtool.activeCodebatch.domain.CodeBatch;
import com.gm.project.gmtool.activeCodebatch.service.ICodeBatchService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * Контроллер пакетов кодов активации
 * 
 * @author gm
 * @date 2021-09-22
 */
@Controller
@RequestMapping("/gmtool/activeCodebatch")
public class CodeBatchController extends BaseController
{
    private String prefix = "gmtool/activeCodebatch";

    @Autowired
    private ICodeBatchService codeBatchService;

    @RequiresPermissions("gmtool:activeCodebatch:view")
    @GetMapping()
    public String activeCodebatch()
    {
        return prefix + "/activeCodebatch";
    }

    /**
     * Получение списка пакетов кодов активации
     */
//    @RequiresPermissions("gmtool:activeCodebatch:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(CodeBatch codeBatch)
    {
        startPage();
        List<CodeBatch> list = codeBatchService.selectCodeBatchList(codeBatch);
        return getDataTable(list);
    }

    /**
     * Экспорт списка пакетов кодов активации
     */
    @RequiresPermissions("gmtool:activeCodebatch:export")
    @Log(title = "Пакеты кодов активации", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CodeBatch codeBatch)
    {
        List<CodeBatch> list = codeBatchService.selectCodeBatchList(codeBatch);
        ExcelUtil<CodeBatch> util = new ExcelUtil<CodeBatch>(CodeBatch.class);
        return util.exportExcel(list, "Данные пакетов кодов активации");
    }

    /**
     * Добавление пакета кодов активации
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * Сохранение нового пакета кодов активации
     */
    @RequiresPermissions("gmtool:activeCodebatch:add")
    @Log(title = "Пакеты кодов активации", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CodeBatch codeBatch)
    {
        return toAjax(codeBatchService.insertCodeBatch(codeBatch));
    }

    /**
     * Редактирование пакета кодов активации
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        CodeBatch codeBatch = codeBatchService.selectCodeBatchById(id);
        mmap.put("codeBatch", codeBatch);
        return prefix + "/edit";
    }

    /**
     * Сохранение изменений пакета кодов активации
     */
    @RequiresPermissions("gmtool:activeCodebatch:edit")
    @Log(title = "Пакеты кодов активации", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CodeBatch codeBatch)
    {
        return toAjax(codeBatchService.updateCodeBatch(codeBatch));
    }

    /**
     * Удаление пакета кодов активации
     */
    @RequiresPermissions("gmtool:activeCodebatch:remove")
    @Log(title = "Пакеты кодов активации", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(codeBatchService.deleteCodeBatchByIds(ids));
    }
}