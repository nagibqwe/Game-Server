package com.kits.project.serverListConfig.whiteList.controller;

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
import com.kits.project.serverListConfig.whiteList.domain.SdkWhite;
import com.kits.project.serverListConfig.whiteList.service.ISdkWhiteService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 白名单Controller
 * 
 * @author gm
 * @date 2021-04-26
 */
@Controller
@RequestMapping("/serverListConfig/whiteList")
public class SdkWhiteController extends BaseController
{
    private String prefix = "serverListConfig/whiteList";

    @Autowired
    private ISdkWhiteService sdkWhiteService;

    @RequiresPermissions("serverListConfig:whiteList:view")
    @GetMapping()
    public String whiteList()
    {
        return prefix + "/whiteList";
    }

    /**
     * 查询白名单列表
     */
    @RequiresPermissions("serverListConfig:whiteList:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SdkWhite sdkWhite)
    {
        startPage();
        List<SdkWhite> list = sdkWhiteService.selectSdkWhiteList(sdkWhite);
        return getDataTable(list);
    }

    /**
     * 导出白名单列表
     */
    @RequiresPermissions("serverListConfig:whiteList:export")
    @Log(title = "白名单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SdkWhite sdkWhite)
    {
        List<SdkWhite> list = sdkWhiteService.selectSdkWhiteList(sdkWhite);
        ExcelUtil<SdkWhite> util = new ExcelUtil<SdkWhite>(SdkWhite.class);
        return util.exportExcel(list, "白名单数据");
    }

    /**
     * 新增白名单
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存白名单
     */
    @RequiresPermissions("serverListConfig:whiteList:add")
    @Log(title = "白名单", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SdkWhite sdkWhite)
    {
        return toAjax(sdkWhiteService.insertSdkWhite(sdkWhite));
    }

    /**
     * 修改白名单
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SdkWhite sdkWhite = sdkWhiteService.selectSdkWhiteById(id);
        mmap.put("sdkWhite", sdkWhite);
        return prefix + "/edit";
    }

    /**
     * 修改保存白名单
     */
    @RequiresPermissions("serverListConfig:whiteList:edit")
    @Log(title = "白名单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SdkWhite sdkWhite)
    {
        return toAjax(sdkWhiteService.updateSdkWhite(sdkWhite));
    }

    /**
     * 删除白名单
     */
    @RequiresPermissions("serverListConfig:whiteList:remove")
    @Log(title = "白名单", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sdkWhiteService.deleteSdkWhiteByIds(ids));
    }
}
