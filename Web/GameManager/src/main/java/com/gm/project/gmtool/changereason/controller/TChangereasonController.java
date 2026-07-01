package com.gm.project.gmtool.changereason.controller;

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
import com.gm.project.gmtool.changereason.domain.TChangereason;
import com.gm.project.gmtool.changereason.service.ITChangereasonService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 原因码Controller
 * 
 * @author gm
 * @date 2021-12-21
 */
@Controller
@RequestMapping("/gmtool/changereason")
public class TChangereasonController extends BaseController
{
    private String prefix = "gmtool/changereason";

    @Autowired
    private ITChangereasonService tChangereasonService;

    @RequiresPermissions("gmtool:changereason:view")
    @GetMapping()
    public String changereason()
    {
        return prefix + "/changereason";
    }

    /**
     * 查询原因码列表
     */
    @RequiresPermissions("gmtool:changereason:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TChangereason tChangereason)
    {
        startPage();
        List<TChangereason> list = tChangereasonService.selectTChangereasonList(tChangereason);
        return getDataTable(list);
    }

    /**
     * 导出原因码列表
     */
    @RequiresPermissions("gmtool:changereason:export")
    @Log(title = "原因码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TChangereason tChangereason)
    {
        List<TChangereason> list = tChangereasonService.selectTChangereasonList(tChangereason);
        ExcelUtil<TChangereason> util = new ExcelUtil<TChangereason>(TChangereason.class);
        return util.exportExcel(list, "原因码数据");
    }

    /**
     * 新增原因码
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存原因码
     */
    @RequiresPermissions("gmtool:changereason:add")
    @Log(title = "原因码", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TChangereason tChangereason)
    {
        return toAjax(tChangereasonService.insertTChangereason(tChangereason));
    }

    /**
     * 修改原因码
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        TChangereason tChangereason = tChangereasonService.selectTChangereasonById(id);
        mmap.put("tChangereason", tChangereason);
        return prefix + "/edit";
    }

    /**
     * 修改保存原因码
     */
    @RequiresPermissions("gmtool:changereason:edit")
    @Log(title = "原因码", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TChangereason tChangereason)
    {
        return toAjax(tChangereasonService.updateTChangereason(tChangereason));
    }

    /**
     * 删除原因码
     */
    @RequiresPermissions("gmtool:changereason:remove")
    @Log(title = "原因码", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tChangereasonService.deleteTChangereasonByIds(ids));
    }
}
