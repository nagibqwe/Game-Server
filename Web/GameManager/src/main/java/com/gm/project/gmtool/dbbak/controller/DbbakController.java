package com.gm.project.gmtool.dbbak.controller;

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
import com.gm.project.gmtool.dbbak.domain.Dbbak;
import com.gm.project.gmtool.dbbak.service.IDbbakService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 数据库备份Controller
 * 
 * @author gm
 * @date 2021-09-13
 */
@Controller
@RequestMapping("/gmtool/dbbak")
public class DbbakController extends BaseController
{
    private String prefix = "gmtool/dbbak";

    @Autowired
    private IDbbakService dbbakService;

    @RequiresPermissions("gmtool:dbbak:view")
    @GetMapping()
    public String dbbak()
    {
        return prefix + "/dbbak";
    }

    /**
     * 查询数据库备份列表
     */
    @RequiresPermissions("gmtool:dbbak:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Dbbak dbbak)
    {
        startPage();
        List<Dbbak> list = dbbakService.selectDbbakList(dbbak);
        return getDataTable(list);
    }

    /**
     * 导出数据库备份列表
     */
    @RequiresPermissions("gmtool:dbbak:export")
    @Log(title = "数据库备份", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Dbbak dbbak)
    {
        List<Dbbak> list = dbbakService.selectDbbakList(dbbak);
        ExcelUtil<Dbbak> util = new ExcelUtil<Dbbak>(Dbbak.class);
        return util.exportExcel(list, "数据库备份数据");
    }

    /**
     * 新增数据库备份
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存数据库备份
     */
    @RequiresPermissions("gmtool:dbbak:add")
    @Log(title = "数据库备份", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Dbbak dbbak)
    {
        return toAjax(dbbakService.insertDbbak(dbbak));
    }

    /**
     * 修改数据库备份
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Dbbak dbbak = dbbakService.selectDbbakById(id);
        mmap.put("dbbak", dbbak);
        return prefix + "/edit";
    }

    /**
     * 修改保存数据库备份
     */
    @RequiresPermissions("gmtool:dbbak:edit")
    @Log(title = "数据库备份", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Dbbak dbbak)
    {
        return toAjax(dbbakService.updateDbbak(dbbak));
    }

    /**
     * 删除数据库备份
     */
    @RequiresPermissions("gmtool:dbbak:remove")
    @Log(title = "数据库备份", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(dbbakService.deleteDbbakByIds(ids));
    }
}
