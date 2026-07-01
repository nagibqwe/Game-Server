package com.gm.project.gmtool.activityFestivalRelation.controller;

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
import com.gm.project.gmtool.activityFestivalRelation.domain.ActivityFestivalRelation;
import com.gm.project.gmtool.activityFestivalRelation.service.IActivityFestivalRelationService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 运营活动节日关系Controller
 * 
 * @author gm
 * @date 2021-11-08
 */
@Controller
@RequestMapping("/gmtool/activityFestivalRelation")
public class ActivityFestivalRelationController extends BaseController
{
    private String prefix = "gmtool/activityFestivalRelation";

    @Autowired
    private IActivityFestivalRelationService activityFestivalRelationService;

    @RequiresPermissions("gmtool:activityFestivalRelation:view")
    @GetMapping()
    public String activityFestivalRelation()
    {
        return prefix + "/activityFestivalRelation";
    }

    /**
     * 查询运营活动节日关系列表
     */
//    @RequiresPermissions("gmtool:activityFestivalRelation:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActivityFestivalRelation activityFestivalRelation)
    {
        startPage();
        List<ActivityFestivalRelation> list = activityFestivalRelationService.selectActivityFestivalRelationList(activityFestivalRelation);
        return getDataTable(list);
    }

    /**
     * 导出运营活动节日关系列表
     */
    @RequiresPermissions("gmtool:activityFestivalRelation:export")
    @Log(title = "运营活动节日关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ActivityFestivalRelation activityFestivalRelation)
    {
        List<ActivityFestivalRelation> list = activityFestivalRelationService.selectActivityFestivalRelationList(activityFestivalRelation);
        ExcelUtil<ActivityFestivalRelation> util = new ExcelUtil<ActivityFestivalRelation>(ActivityFestivalRelation.class);
        return util.exportExcel(list, "运营活动节日关系数据");
    }

    /**
     * 新增运营活动节日关系
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存运营活动节日关系
     */
    @RequiresPermissions("gmtool:activityFestivalRelation:add")
    @Log(title = "运营活动节日关系", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ActivityFestivalRelation activityFestivalRelation)
    {
        return toAjax(activityFestivalRelationService.insertActivityFestivalRelation(activityFestivalRelation));
    }

    /**
     * 修改运营活动节日关系
     */
    @GetMapping("/edit/{logicId}")
    public String edit(@PathVariable("logicId") Integer logicId, ModelMap mmap)
    {
        ActivityFestivalRelation activityFestivalRelation = activityFestivalRelationService.selectActivityFestivalRelationById(logicId);
        mmap.put("activityFestivalRelation", activityFestivalRelation);
        return prefix + "/edit";
    }

    /**
     * 修改保存运营活动节日关系
     */
    @RequiresPermissions("gmtool:activityFestivalRelation:edit")
    @Log(title = "运营活动节日关系", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ActivityFestivalRelation activityFestivalRelation)
    {
        return toAjax(activityFestivalRelationService.updateActivityFestivalRelation(activityFestivalRelation));
    }

    /**
     * 删除运营活动节日关系
     */
    @RequiresPermissions("gmtool:activityFestivalRelation:remove")
    @Log(title = "运营活动节日关系", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(activityFestivalRelationService.deleteActivityFestivalRelationByIds(ids));
    }
}
