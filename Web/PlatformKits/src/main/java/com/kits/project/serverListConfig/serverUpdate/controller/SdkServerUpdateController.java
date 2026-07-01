package com.kits.project.serverListConfig.serverUpdate.controller;

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
import com.kits.framework.aspectj.lang.annotation.Log;
import com.kits.framework.aspectj.lang.enums.BusinessType;
import com.kits.project.serverListConfig.serverUpdate.domain.SdkServerUpdate;
import com.kits.project.serverListConfig.serverUpdate.service.ISdkServerUpdateService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 服务器信息修改时间记录Controller
 * 
 * @author gm
 * @date 2021-12-08
 */
@Controller
@RequestMapping("/serverListConfig/serverUpdate")
public class SdkServerUpdateController extends BaseController
{
    private String prefix = "serverListConfig/serverUpdate";

    @Autowired
    private ISdkServerUpdateService sdkServerUpdateService;

    @RequiresPermissions("serverListConfig:serverUpdate:view")
    @GetMapping()
    public String serverUpdate()
    {
        return prefix + "/serverUpdate";
    }

    /**
     * 查询服务器信息修改时间记录列表
     */
    @RequiresPermissions("serverListConfig:serverUpdate:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SdkServerUpdate sdkServerUpdate)
    {
        startPage();
        List<SdkServerUpdate> list = sdkServerUpdateService.selectSdkServerUpdateList(sdkServerUpdate);
        return getDataTable(list);
    }

    /**
     * 导出服务器信息修改时间记录列表
     */
    @RequiresPermissions("serverListConfig:serverUpdate:export")
    @Log(title = "服务器信息修改时间记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SdkServerUpdate sdkServerUpdate)
    {
        List<SdkServerUpdate> list = sdkServerUpdateService.selectSdkServerUpdateList(sdkServerUpdate);
        ExcelUtil<SdkServerUpdate> util = new ExcelUtil<SdkServerUpdate>(SdkServerUpdate.class);
        return util.exportExcel(list, "服务器信息修改时间记录数据");
    }

    /**
     * 新增服务器信息修改时间记录
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存服务器信息修改时间记录
     */
    @RequiresPermissions("serverListConfig:serverUpdate:add")
    @Log(title = "服务器信息修改时间记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SdkServerUpdate sdkServerUpdate)
    {
        return toAjax(sdkServerUpdateService.insertSdkServerUpdate(sdkServerUpdate));
    }

    /**
     * 修改服务器信息修改时间记录
     */
    @GetMapping("/edit/{updateTime}")
    public String edit(@PathVariable("updateTime") Long updateTime, ModelMap mmap)
    {
        SdkServerUpdate sdkServerUpdate = sdkServerUpdateService.selectSdkServerUpdateById(updateTime);
        mmap.put("sdkServerUpdate", sdkServerUpdate);
        return prefix + "/edit";
    }

    /**
     * 修改保存服务器信息修改时间记录
     */
    @RequiresPermissions("serverListConfig:serverUpdate:edit")
    @Log(title = "服务器信息修改时间记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SdkServerUpdate sdkServerUpdate)
    {
        return toAjax(sdkServerUpdateService.updateSdkServerUpdate(sdkServerUpdate));
    }

    /**
     * 删除服务器信息修改时间记录
     */
    @RequiresPermissions("serverListConfig:serverUpdate:remove")
    @Log(title = "服务器信息修改时间记录", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sdkServerUpdateService.deleteSdkServerUpdateByIds(ids));
    }
}
