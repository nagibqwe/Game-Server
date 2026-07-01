package com.gm.project.gmtool.activityBossType.controller;

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
import com.gm.project.gmtool.activityBossType.domain.ActivityBossType;
import com.gm.project.gmtool.activityBossType.service.IActivityBossTypeService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 运营活动boss类型Controller
 * 
 * @author gm
 * @date 2021-09-14
 */
@Controller
@RequestMapping("/gmtool/activityBossType")
public class ActivityBossTypeController extends BaseController
{
    private String prefix = "gmtool/activityBossType";

    @Autowired
    private IActivityBossTypeService activityBossTypeService;

    @RequiresPermissions("gmtool:activityBossType:view")
    @GetMapping()
    public String activityBossType()
    {
        return prefix + "/activityBossType";
    }

    /**
     * 查询运营活动boss类型列表
     */
//    @RequiresPermissions("gmtool:activityBossType:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActivityBossType activityBossType)
    {
        startPage();
        List<ActivityBossType> list = activityBossTypeService.selectActivityBossTypeList(activityBossType);
        return getDataTable(list);
    }

    /**
     * 导出运营活动boss类型列表
     */
    @RequiresPermissions("gmtool:activityBossType:export")
    @Log(title = "运营活动boss类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ActivityBossType activityBossType)
    {
        List<ActivityBossType> list = activityBossTypeService.selectActivityBossTypeList(activityBossType);
        ExcelUtil<ActivityBossType> util = new ExcelUtil<ActivityBossType>(ActivityBossType.class);
        return util.exportExcel(list, "运营活动boss类型数据");
    }

    /**
     * 新增运营活动boss类型
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存运营活动boss类型
     */
    @RequiresPermissions("gmtool:activityBossType:add")
    @Log(title = "运营活动boss类型", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ActivityBossType activityBossType)
    {
        return toAjax(activityBossTypeService.insertActivityBossType(activityBossType));
    }

    /**
     * 修改运营活动boss类型
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        ActivityBossType activityBossType = activityBossTypeService.selectActivityBossTypeById(id);
        mmap.put("activityBossType", activityBossType);
        return prefix + "/edit";
    }

    /**
     * 修改保存运营活动boss类型
     */
    @RequiresPermissions("gmtool:activityBossType:edit")
    @Log(title = "运营活动boss类型", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ActivityBossType activityBossType)
    {
        return toAjax(activityBossTypeService.updateActivityBossType(activityBossType));
    }

    /**
     * 删除运营活动boss类型
     */
    @RequiresPermissions("gmtool:activityBossType:remove")
    @Log(title = "运营活动boss类型", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(activityBossTypeService.deleteActivityBossTypeByIds(ids));
    }
}
