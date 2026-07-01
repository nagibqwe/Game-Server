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
 * 激活码批次号Controller
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
     * 查询激活码批次号列表
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
     * 导出激活码批次号列表
     */
    @RequiresPermissions("gmtool:activeCodebatch:export")
    @Log(title = "激活码批次号", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(CodeBatch codeBatch)
    {
        List<CodeBatch> list = codeBatchService.selectCodeBatchList(codeBatch);
        ExcelUtil<CodeBatch> util = new ExcelUtil<CodeBatch>(CodeBatch.class);
        return util.exportExcel(list, "激活码批次号数据");
    }

    /**
     * 新增激活码批次号
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存激活码批次号
     */
    @RequiresPermissions("gmtool:activeCodebatch:add")
    @Log(title = "激活码批次号", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(CodeBatch codeBatch)
    {
        return toAjax(codeBatchService.insertCodeBatch(codeBatch));
    }

    /**
     * 修改激活码批次号
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        CodeBatch codeBatch = codeBatchService.selectCodeBatchById(id);
        mmap.put("codeBatch", codeBatch);
        return prefix + "/edit";
    }

    /**
     * 修改保存激活码批次号
     */
    @RequiresPermissions("gmtool:activeCodebatch:edit")
    @Log(title = "激活码批次号", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(CodeBatch codeBatch)
    {
        return toAjax(codeBatchService.updateCodeBatch(codeBatch));
    }

    /**
     * 删除激活码批次号
     */
    @RequiresPermissions("gmtool:activeCodebatch:remove")
    @Log(title = "激活码批次号", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(codeBatchService.deleteCodeBatchByIds(ids));
    }
}
