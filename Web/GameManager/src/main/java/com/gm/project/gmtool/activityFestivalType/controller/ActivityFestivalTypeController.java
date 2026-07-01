package com.gm.project.gmtool.activityFestivalType.controller;

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
import com.gm.project.gmtool.activityFestivalType.domain.ActivityFestivalType;
import com.gm.project.gmtool.activityFestivalType.service.IActivityFestivalTypeService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 节日类型Controller
 * 
 * @author gm
 * @date 2021-09-09
 */
@Controller
@RequestMapping("/gmtool/activityFestivalType")
public class ActivityFestivalTypeController extends BaseController
{
    private String prefix = "gmtool/activityFestivalType";

    @Autowired
    private IActivityFestivalTypeService activityFestivalTypeService;

    @RequiresPermissions("gmtool:activityFestivalType:view")
    @GetMapping()
    public String activityFestivalType()
    {
        return prefix + "/activityFestivalType";
    }

    /**
     * 查询节日类型列表
     */
//    @RequiresPermissions("gmtool:activityFestivalType:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActivityFestivalType activityFestivalType)
    {
        startPage();
        List<ActivityFestivalType> list = activityFestivalTypeService.selectActivityFestivalTypeList(activityFestivalType);
        return getDataTable(list);
    }

    /**
     * 导出节日类型列表
     */
    @RequiresPermissions("gmtool:activityFestivalType:export")
    @Log(title = "节日类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ActivityFestivalType activityFestivalType)
    {
        List<ActivityFestivalType> list = activityFestivalTypeService.selectActivityFestivalTypeList(activityFestivalType);
        ExcelUtil<ActivityFestivalType> util = new ExcelUtil<ActivityFestivalType>(ActivityFestivalType.class);
        return util.exportExcel(list, "节日类型数据");
    }

    /**
     * 新增节日类型
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存节日类型
     */
    @RequiresPermissions("gmtool:activityFestivalType:add")
    @Log(title = "节日类型", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ActivityFestivalType activityFestivalType)
    {
        return toAjax(activityFestivalTypeService.insertActivityFestivalType(activityFestivalType));
    }

    /**
     * 修改节日类型
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        ActivityFestivalType activityFestivalType = activityFestivalTypeService.selectActivityFestivalTypeById(id);
        mmap.put("activityFestivalType", activityFestivalType);
        return prefix + "/edit";
    }

    /**
     * 修改保存节日类型
     */
    @RequiresPermissions("gmtool:activityFestivalType:edit")
    @Log(title = "节日类型", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ActivityFestivalType activityFestivalType)
    {
        return toAjax(activityFestivalTypeService.updateActivityFestivalType(activityFestivalType));
    }

    /**
     * 删除节日类型
     */
    @RequiresPermissions("gmtool:activityFestivalType:remove")
    @Log(title = "节日类型", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(activityFestivalTypeService.deleteActivityFestivalTypeByIds(ids));
    }
}
