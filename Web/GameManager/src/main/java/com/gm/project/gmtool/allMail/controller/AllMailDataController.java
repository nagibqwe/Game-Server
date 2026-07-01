package com.gm.project.gmtool.allMail.controller;

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
import com.gm.project.gmtool.allMail.domain.AllMailData;
import com.gm.project.gmtool.allMail.service.IAllMailDataService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 全服邮件Controller
 * 
 * @author gm
 * @date 2021-08-30
 */
@Controller
@RequestMapping("/gmtool/allMail")
public class AllMailDataController extends BaseController
{
    private String prefix = "gmtool/allMail";

    @Autowired
    private IAllMailDataService allMailDataService;

    @RequiresPermissions("gmtool:allMail:view")
    @GetMapping()
    public String allMail()
    {
        return prefix + "/allMail";
    }

    /**
     * 查询全服邮件列表
     */
//    @RequiresPermissions("gmtool:allMail:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AllMailData allMailData)
    {
        startPage();
        List<AllMailData> list = allMailDataService.selectAllMailDataList(allMailData);
        return getDataTable(list);
    }

    /**
     * 导出全服邮件列表
     */
    @RequiresPermissions("gmtool:allMail:export")
    @Log(title = "全服邮件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AllMailData allMailData)
    {
        List<AllMailData> list = allMailDataService.selectAllMailDataList(allMailData);
        ExcelUtil<AllMailData> util = new ExcelUtil<AllMailData>(AllMailData.class);
        return util.exportExcel(list, "全服邮件数据");
    }

    /**
     * 新增全服邮件
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存全服邮件
     */
    @RequiresPermissions("gmtool:allMail:add")
    @Log(title = "全服邮件", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AllMailData allMailData)
    {
        return toAjax(allMailDataService.insertAllMailData(allMailData));
    }

    /**
     * 修改全服邮件
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        AllMailData allMailData = allMailDataService.selectAllMailDataById(id);
        mmap.put("allMailData", allMailData);
        return prefix + "/edit";
    }

    /**
     * 修改保存全服邮件
     */
    @RequiresPermissions("gmtool:allMail:edit")
    @Log(title = "全服邮件", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AllMailData allMailData)
    {
        return toAjax(allMailDataService.updateAllMailData(allMailData));
    }

    /**
     * 删除全服邮件
     */
    @RequiresPermissions("gmtool:allMail:remove")
    @Log(title = "全服邮件", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(allMailDataService.deleteAllMailDataByIds(ids));
    }
}
