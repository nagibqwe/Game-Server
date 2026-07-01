package com.gm.project.gmtool.activityModel.controller;

import java.util.List;

import com.gm.project.gmtool.activityModel.domain.ActivityModel;
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
import com.gm.project.gmtool.activityModel.service.IActivityModelService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 运营活动模型库Controller
 * 
 * @author gm
 * @date 2021-09-14
 */
@Controller
@RequestMapping("/gmtool/activityModel")
public class ActivityModelController extends BaseController
{
    private String prefix = "gmtool/activityModel";

    @Autowired
    private IActivityModelService modelService;

    @RequiresPermissions("gmtool:activityModel:view")
    @GetMapping()
    public String activityModel()
    {
        return prefix + "/activityModel";
    }

    /**
     * 查询运营活动模型库列表
     */
//    @RequiresPermissions("gmtool:activityModel:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActivityModel activityModel)
    {
        startPage();
        List<ActivityModel> list = modelService.selectModelList(activityModel);
        return getDataTable(list);
    }

    /**
     * 导出运营活动模型库列表
     */
    @RequiresPermissions("gmtool:activityModel:export")
    @Log(title = "运营活动模型库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ActivityModel activityModel)
    {
        List<ActivityModel> list = modelService.selectModelList(activityModel);
        ExcelUtil<ActivityModel> util = new ExcelUtil<ActivityModel>(ActivityModel.class);
        return util.exportExcel(list, "运营活动模型库数据");
    }

    /**
     * 新增运营活动模型库
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存运营活动模型库
     */
    @RequiresPermissions("gmtool:activityModel:add")
    @Log(title = "运营活动模型库", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ActivityModel activityModel)
    {
        return toAjax(modelService.insertModel(activityModel));
    }

    /**
     * 修改运营活动模型库
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        ActivityModel activityModel = modelService.selectModelById(id);
        mmap.put("activityModel", activityModel);
        return prefix + "/edit";
    }

    /**
     * 修改保存运营活动模型库
     */
    @RequiresPermissions("gmtool:activityModel:edit")
    @Log(title = "运营活动模型库", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ActivityModel activityModel)
    {
        return toAjax(modelService.updateModel(activityModel));
    }

    /**
     * 删除运营活动模型库
     */
    @RequiresPermissions("gmtool:activityModel:remove")
    @Log(title = "运营活动模型库", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(modelService.deleteModelByIds(ids));
    }
}
