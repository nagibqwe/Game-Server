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
 * Резервные копии БДController
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
     * 查询Список резервных копий БД
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
     * ЭкспортСписок резервных копий БД
     */
    @RequiresPermissions("gmtool:dbbak:export")
    @Log(title = "Резервные копии БД", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Dbbak dbbak)
    {
        List<Dbbak> list = dbbakService.selectDbbakList(dbbak);
        ExcelUtil<Dbbak> util = new ExcelUtil<Dbbak>(Dbbak.class);
        return util.exportExcel(list, "Резервные копии БДДанные");
    }

    /**
     * ДобавитьРезервные копии БД
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * ДобавитьСохранитьРезервные копии БД
     */
    @RequiresPermissions("gmtool:dbbak:add")
    @Log(title = "Резервные копии БД", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Dbbak dbbak)
    {
        return toAjax(dbbakService.insertDbbak(dbbak));
    }

    /**
     * ИзменитьРезервные копии БД
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Dbbak dbbak = dbbakService.selectDbbakById(id);
        mmap.put("dbbak", dbbak);
        return prefix + "/edit";
    }

    /**
     * ИзменитьСохранитьРезервные копии БД
     */
    @RequiresPermissions("gmtool:dbbak:edit")
    @Log(title = "Резервные копии БД", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Dbbak dbbak)
    {
        return toAjax(dbbakService.updateDbbak(dbbak));
    }

    /**
     * УдалитьРезервные копии БД
     */
    @RequiresPermissions("gmtool:dbbak:remove")
    @Log(title = "Резервные копии БД", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(dbbakService.deleteDbbakByIds(ids));
    }
}
