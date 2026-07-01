package com.kits.project.serverListConfig.x8channelMaster.controller;

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
import com.kits.project.serverListConfig.x8channelMaster.domain.TX8ChannelMaster;
import com.kits.project.serverListConfig.x8channelMaster.service.ITX8ChannelMasterService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 渠道商-实际的发行渠道商,360,小米等Controller
 * 
 * @author gm
 * @date 2021-07-08
 */
@Controller
@RequestMapping("/serverListConfig/x8channelMaster")
public class TX8ChannelMasterController extends BaseController
{
    private String prefix = "serverListConfig/x8channelMaster";

    @Autowired
    private ITX8ChannelMasterService tX8ChannelMasterService;

    @RequiresPermissions("serverListConfig:x8channelMaster:view")
    @GetMapping()
    public String x8channelMaster()
    {
        return prefix + "/x8channelMaster";
    }

    /**
     * 查询渠道商-实际的发行渠道商,360,小米等列表
     */
    @RequiresPermissions("serverListConfig:x8channelMaster:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TX8ChannelMaster tX8ChannelMaster)
    {
        startPage();
        List<TX8ChannelMaster> list = tX8ChannelMasterService.selectTX8ChannelMasterList(tX8ChannelMaster);
        return getDataTable(list);
    }

    /**
     * 导出渠道商-实际的发行渠道商,360,小米等列表
     */
    @RequiresPermissions("serverListConfig:x8channelMaster:export")
    @Log(title = "渠道商-实际的发行渠道商,360,小米等", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TX8ChannelMaster tX8ChannelMaster)
    {
        List<TX8ChannelMaster> list = tX8ChannelMasterService.selectTX8ChannelMasterList(tX8ChannelMaster);
        ExcelUtil<TX8ChannelMaster> util = new ExcelUtil<TX8ChannelMaster>(TX8ChannelMaster.class);
        return util.exportExcel(list, "渠道商-实际的发行渠道商,360,小米等数据");
    }

    /**
     * 新增渠道商-实际的发行渠道商,360,小米等
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存渠道商-实际的发行渠道商,360,小米等
     */
    @RequiresPermissions("serverListConfig:x8channelMaster:add")
    @Log(title = "渠道商-实际的发行渠道商,360,小米等", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TX8ChannelMaster tX8ChannelMaster)
    {
        return toAjax(tX8ChannelMasterService.insertTX8ChannelMaster(tX8ChannelMaster));
    }

    /**
     * 修改渠道商-实际的发行渠道商,360,小米等
     */
    @GetMapping("/edit/{chnmId}")
    public String edit(@PathVariable("chnmId") Long chnmId, ModelMap mmap)
    {
        TX8ChannelMaster tX8ChannelMaster = tX8ChannelMasterService.selectTX8ChannelMasterById(chnmId);
        mmap.put("tX8ChannelMaster", tX8ChannelMaster);
        return prefix + "/edit";
    }

    /**
     * 修改保存渠道商-实际的发行渠道商,360,小米等
     */
    @RequiresPermissions("serverListConfig:x8channelMaster:edit")
    @Log(title = "渠道商-实际的发行渠道商,360,小米等", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TX8ChannelMaster tX8ChannelMaster)
    {
        return toAjax(tX8ChannelMasterService.updateTX8ChannelMaster(tX8ChannelMaster));
    }

    /**
     * 删除渠道商-实际的发行渠道商,360,小米等
     */
    @RequiresPermissions("serverListConfig:x8channelMaster:remove")
    @Log(title = "渠道商-实际的发行渠道商,360,小米等", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tX8ChannelMasterService.deleteTX8ChannelMasterByIds(ids));
    }
}
