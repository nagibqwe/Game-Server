package com.gm.project.gmtool.activityLuckyValue.controller;

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
import com.gm.project.gmtool.activityLuckyValue.domain.ActivityLuckyValue;
import com.gm.project.gmtool.activityLuckyValue.service.IActivityLuckyValueService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 抽奖幸运值Controller
 * 
 * @author gm
 * @date 2021-09-16
 */
@Controller
@RequestMapping("/gmtool/activityLuckyValue")
public class ActivityLuckyValueController extends BaseController
{
    private String prefix = "gmtool/activityLuckyValue";

    @Autowired
    private IActivityLuckyValueService activityLuckyValueService;

    @RequiresPermissions("gmtool:activityLuckyValue:view")
    @GetMapping()
    public String activityLuckyValue()
    {
        return prefix + "/activityLuckyValue";
    }

    /**
     * 查询抽奖幸运值列表
     */
//    @RequiresPermissions("gmtool:activityLuckyValue:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ActivityLuckyValue activityLuckyValue)
    {
        startPage();
        List<ActivityLuckyValue> list = activityLuckyValueService.selectActivityLuckyValueList(activityLuckyValue);
        return getDataTable(list);
    }

    /**
     * 导出抽奖幸运值列表
     */
    @RequiresPermissions("gmtool:activityLuckyValue:export")
    @Log(title = "抽奖幸运值", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ActivityLuckyValue activityLuckyValue)
    {
        List<ActivityLuckyValue> list = activityLuckyValueService.selectActivityLuckyValueList(activityLuckyValue);
        ExcelUtil<ActivityLuckyValue> util = new ExcelUtil<ActivityLuckyValue>(ActivityLuckyValue.class);
        return util.exportExcel(list, "抽奖幸运值数据");
    }

    /**
     * 新增抽奖幸运值
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存抽奖幸运值
     */
    @RequiresPermissions("gmtool:activityLuckyValue:add")
    @Log(title = "抽奖幸运值", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ActivityLuckyValue activityLuckyValue)
    {
        return toAjax(activityLuckyValueService.insertActivityLuckyValue(activityLuckyValue));
    }

    /**
     * 修改抽奖幸运值
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap)
    {
        ActivityLuckyValue activityLuckyValue = activityLuckyValueService.selectActivityLuckyValueById(id);
        mmap.put("activityLuckyValue", activityLuckyValue);
        return prefix + "/edit";
    }

    /**
     * 修改保存抽奖幸运值
     */
    @RequiresPermissions("gmtool:activityLuckyValue:edit")
    @Log(title = "抽奖幸运值", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(ActivityLuckyValue activityLuckyValue)
    {
        return toAjax(activityLuckyValueService.updateActivityLuckyValue(activityLuckyValue));
    }

    /**
     * 删除抽奖幸运值
     */
    @RequiresPermissions("gmtool:activityLuckyValue:remove")
    @Log(title = "抽奖幸运值", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(activityLuckyValueService.deleteActivityLuckyValueByIds(ids));
    }
}
