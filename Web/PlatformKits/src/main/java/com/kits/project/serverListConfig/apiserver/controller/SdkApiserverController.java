package com.kits.project.serverListConfig.apiserver.controller;

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
import com.kits.project.serverListConfig.apiserver.domain.SdkApiserver;
import com.kits.project.serverListConfig.apiserver.service.ISdkApiserverService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * APIServer地址管理Controller
 * 
 * @author gm
 * @date 2021-06-24
 */
@Controller
@RequestMapping("/serverListConfig/apiserver")
public class SdkApiserverController extends BaseController
{
    private String prefix = "serverListConfig/apiserver";

    @Autowired
    private ISdkApiserverService sdkApiserverService;

    @RequiresPermissions("serverListConfig:apiserver:view")
    @GetMapping()
    public String apiserver()
    {
        return prefix + "/apiserver";
    }

    /**
     * 查询APIServer地址管理列表
     */
    @RequiresPermissions("serverListConfig:apiserver:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SdkApiserver sdkApiserver)
    {
        startPage();
        List<SdkApiserver> list = sdkApiserverService.selectSdkApiserverList(sdkApiserver);
        return getDataTable(list);
    }

    /**
     * 导出APIServer地址管理列表
     */
    @RequiresPermissions("serverListConfig:apiserver:export")
    @Log(title = "APIServer地址管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SdkApiserver sdkApiserver)
    {
        List<SdkApiserver> list = sdkApiserverService.selectSdkApiserverList(sdkApiserver);
        ExcelUtil<SdkApiserver> util = new ExcelUtil<SdkApiserver>(SdkApiserver.class);
        return util.exportExcel(list, "APIServer地址管理数据");
    }

    /**
     * 新增APIServer地址管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存APIServer地址管理
     */
    @RequiresPermissions("serverListConfig:apiserver:add")
    @Log(title = "APIServer地址管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SdkApiserver sdkApiserver)
    {
        return toAjax(sdkApiserverService.insertSdkApiserver(sdkApiserver));
    }

    /**
     * 修改APIServer地址管理
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SdkApiserver sdkApiserver = sdkApiserverService.selectSdkApiserverById(id);
        mmap.put("sdkApiserver", sdkApiserver);
        return prefix + "/edit";
    }

    /**
     * 修改保存APIServer地址管理
     */
    @RequiresPermissions("serverListConfig:apiserver:edit")
    @Log(title = "APIServer地址管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SdkApiserver sdkApiserver)
    {
        return toAjax(sdkApiserverService.updateSdkApiserver(sdkApiserver));
    }

    /**
     * 删除APIServer地址管理
     */
    @RequiresPermissions("serverListConfig:apiserver:remove")
    @Log(title = "APIServer地址管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sdkApiserverService.deleteSdkApiserverByIds(ids));
    }
}
