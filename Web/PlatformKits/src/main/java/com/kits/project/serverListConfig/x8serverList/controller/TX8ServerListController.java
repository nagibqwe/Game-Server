package com.kits.project.serverListConfig.x8serverList.controller;

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
import com.kits.project.serverListConfig.x8serverList.domain.TX8ServerList;
import com.kits.project.serverListConfig.x8serverList.service.ITX8ServerListService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 渠道区服策略列Controller
 * 
 * @author gm
 * @date 2021-07-08
 */
@Controller
@RequestMapping("/serverListConfig/x8serverList")
public class TX8ServerListController extends BaseController
{
    private String prefix = "serverListConfig/x8serverList";

    @Autowired
    private ITX8ServerListService tX8ServerListService;

    @RequiresPermissions("serverListConfig:x8serverList:view")
    @GetMapping()
    public String x8serverList()
    {
        return prefix + "/x8serverList";
    }

    /**
     * 查询渠道区服策略列列表
     */
    @RequiresPermissions("serverListConfig:x8serverList:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TX8ServerList tX8ServerList)
    {
        startPage();
        List<TX8ServerList> list = tX8ServerListService.selectTX8ServerListList(tX8ServerList);
        return getDataTable(list);
    }

    /**
     * 导出渠道区服策略列列表
     */
    @RequiresPermissions("serverListConfig:x8serverList:export")
    @Log(title = "渠道区服策略列", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TX8ServerList tX8ServerList)
    {
        List<TX8ServerList> list = tX8ServerListService.selectTX8ServerListList(tX8ServerList);
        ExcelUtil<TX8ServerList> util = new ExcelUtil<TX8ServerList>(TX8ServerList.class);
        return util.exportExcel(list, "渠道区服策略列数据");
    }

    /**
     * 新增渠道区服策略列
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存渠道区服策略列
     */
    @RequiresPermissions("serverListConfig:x8serverList:add")
    @Log(title = "渠道区服策略列", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TX8ServerList tX8ServerList)
    {
        return toAjax(tX8ServerListService.insertTX8ServerList(tX8ServerList));
    }

    /**
     * 修改渠道区服策略列
     */
    @GetMapping("/edit/{policyId}")
    public String edit(@PathVariable("policyId") Long policyId, ModelMap mmap)
    {
        TX8ServerList tX8ServerList = tX8ServerListService.selectTX8ServerListById(policyId);
        mmap.put("tX8ServerList", tX8ServerList);
        return prefix + "/edit";
    }

    /**
     * 修改保存渠道区服策略列
     */
    @RequiresPermissions("serverListConfig:x8serverList:edit")
    @Log(title = "渠道区服策略列", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TX8ServerList tX8ServerList)
    {
        return toAjax(tX8ServerListService.updateTX8ServerList(tX8ServerList));
    }

    /**
     * 删除渠道区服策略列
     */
    @RequiresPermissions("serverListConfig:x8serverList:remove")
    @Log(title = "渠道区服策略列", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tX8ServerListService.deleteTX8ServerListByIds(ids));
    }
}
