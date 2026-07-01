package com.kits.project.serverListConfig.x8channel.controller;

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
import com.kits.project.serverListConfig.x8channel.domain.TX8Channel;
import com.kits.project.serverListConfig.x8channel.service.ITX8ChannelService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 渠道-包含游戏各个渠道商的配置Controller
 * 
 * @author gm
 * @date 2021-07-08
 */
@Controller
@RequestMapping("/serverListConfig/x8channel")
public class TX8ChannelController extends BaseController
{
    private String prefix = "serverListConfig/x8channel";

    @Autowired
    private ITX8ChannelService tX8ChannelService;

    @RequiresPermissions("serverListConfig:x8channel:view")
    @GetMapping()
    public String x8channel()
    {
        return prefix + "/x8channel";
    }

    /**
     * 查询渠道-包含游戏各个渠道商的配置列表
     */
    @RequiresPermissions("serverListConfig:x8channel:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TX8Channel tX8Channel)
    {
        startPage();
        List<TX8Channel> list = tX8ChannelService.selectTX8ChannelList(tX8Channel);
        return getDataTable(list);
    }

    /**
     * 导出渠道-包含游戏各个渠道商的配置列表
     */
    @RequiresPermissions("serverListConfig:x8channel:export")
    @Log(title = "渠道-包含游戏各个渠道商的配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TX8Channel tX8Channel)
    {
        List<TX8Channel> list = tX8ChannelService.selectTX8ChannelList(tX8Channel);
        ExcelUtil<TX8Channel> util = new ExcelUtil<TX8Channel>(TX8Channel.class);
        return util.exportExcel(list, "渠道-包含游戏各个渠道商的配置数据");
    }

    /**
     * 新增渠道-包含游戏各个渠道商的配置
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存渠道-包含游戏各个渠道商的配置
     */
    @RequiresPermissions("serverListConfig:x8channel:add")
    @Log(title = "渠道-包含游戏各个渠道商的配置", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TX8Channel tX8Channel)
    {
        return toAjax(tX8ChannelService.insertTX8Channel(tX8Channel));
    }

    /**
     * 修改渠道-包含游戏各个渠道商的配置
     */
    @GetMapping("/edit/{chnId}")
    public String edit(@PathVariable("chnId") Long chnId, ModelMap mmap)
    {
        TX8Channel tX8Channel = tX8ChannelService.selectTX8ChannelById(chnId);
        mmap.put("tX8Channel", tX8Channel);
        return prefix + "/edit";
    }

    /**
     * 修改保存渠道-包含游戏各个渠道商的配置
     */
    @RequiresPermissions("serverListConfig:x8channel:edit")
    @Log(title = "渠道-包含游戏各个渠道商的配置", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TX8Channel tX8Channel)
    {
        return toAjax(tX8ChannelService.updateTX8Channel(tX8Channel));
    }

    /**
     * 删除渠道-包含游戏各个渠道商的配置
     */
    @RequiresPermissions("serverListConfig:x8channel:remove")
    @Log(title = "渠道-包含游戏各个渠道商的配置", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tX8ChannelService.deleteTX8ChannelByIds(ids));
    }
}
