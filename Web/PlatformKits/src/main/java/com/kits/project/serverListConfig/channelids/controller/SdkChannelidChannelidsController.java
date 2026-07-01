package com.kits.project.serverListConfig.channelids.controller;

import java.util.List;

import com.kits.framework.aspectj.lang.annotation.DataSource;
import com.kits.framework.aspectj.lang.enums.DataSourceType;
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
import com.kits.project.serverListConfig.channelids.domain.SdkChannelidChannelids;
import com.kits.project.serverListConfig.channelids.service.ISdkChannelidChannelidsService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 服务器列中的渠道ID和渠道ID列关系对应Controller
 * 
 * @author gm
 * @date 2021-05-10
 */
@Controller
@RequestMapping("/serverListConfig/channelids")
public class SdkChannelidChannelidsController extends BaseController
{
    private String prefix = "serverListConfig/channelids";

    @Autowired
    private ISdkChannelidChannelidsService sdkChannelidChannelidsService;

    @RequiresPermissions("serverListConfig:channelids:view")
    @GetMapping()
    public String channelids()
    {
        return prefix + "/channelids";
    }

    /**
     * 查询服务器列中的渠道ID和渠道ID列关系对应列表
     */
    @RequiresPermissions("serverListConfig:channelids:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SdkChannelidChannelids sdkChannelidChannelids)
    {
        startPage();
        List<SdkChannelidChannelids> list = sdkChannelidChannelidsService.selectSdkChannelidChannelidsList(sdkChannelidChannelids);
        return getDataTable(list);
    }

    /**
     * 导出服务器列中的渠道ID和渠道ID列关系对应列表
     */
    @RequiresPermissions("serverListConfig:channelids:export")
    @Log(title = "服务器列中的渠道ID和渠道ID列关系对应", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SdkChannelidChannelids sdkChannelidChannelids)
    {
        List<SdkChannelidChannelids> list = sdkChannelidChannelidsService.selectSdkChannelidChannelidsList(sdkChannelidChannelids);
        ExcelUtil<SdkChannelidChannelids> util = new ExcelUtil<SdkChannelidChannelids>(SdkChannelidChannelids.class);
        return util.exportExcel(list, "服务器列中的渠道ID和渠道ID列关系对应数据");
    }

    /**
     * 新增服务器列中的渠道ID和渠道ID列关系对应
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存服务器列中的渠道ID和渠道ID列关系对应
     */
    @RequiresPermissions("serverListConfig:channelids:add")
    @Log(title = "服务器列中的渠道ID和渠道ID列关系对应", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SdkChannelidChannelids sdkChannelidChannelids)
    {
        return toAjax(sdkChannelidChannelidsService.insertSdkChannelidChannelids(sdkChannelidChannelids));
    }

    /**
     * 修改服务器列中的渠道ID和渠道ID列关系对应
     */
    @GetMapping("/edit/{channelId}")
    public String edit(@PathVariable("channelId") Long channelId, ModelMap mmap)
    {
        SdkChannelidChannelids sdkChannelidChannelids = sdkChannelidChannelidsService.selectSdkChannelidChannelidsById(channelId);
        mmap.put("sdkChannelidChannelids", sdkChannelidChannelids);
        return prefix + "/edit";
    }

    /**
     * 修改保存服务器列中的渠道ID和渠道ID列关系对应
     */
    @RequiresPermissions("serverListConfig:channelids:edit")
    @Log(title = "服务器列中的渠道ID和渠道ID列关系对应", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SdkChannelidChannelids sdkChannelidChannelids)
    {
        return toAjax(sdkChannelidChannelidsService.updateSdkChannelidChannelids(sdkChannelidChannelids));
    }

    /**
     * 删除服务器列中的渠道ID和渠道ID列关系对应
     */
    @RequiresPermissions("serverListConfig:channelids:remove")
    @Log(title = "服务器列中的渠道ID和渠道ID列关系对应", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sdkChannelidChannelidsService.deleteSdkChannelidChannelidsByIds(ids));
    }
}
