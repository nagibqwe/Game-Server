package com.kits.project.serverListConfig.serverExtra.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kits.common.utils.StringUtils;
import com.kits.common.utils.text.Convert;
import com.kits.framework.aspectj.lang.annotation.DataSource;
import com.kits.framework.aspectj.lang.enums.DataSourceType;
import com.kits.project.serverListConfig.serverList.service.ISdkServerListService;
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
import com.kits.project.serverListConfig.serverExtra.domain.SdkServerExtra;
import com.kits.project.serverListConfig.serverExtra.service.ISdkServerExtraService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 服务器额外信息Controller
 *
 * @author gm
 * @date 2021-05-07
 */
@Controller
@RequestMapping("/serverListConfig/serverExtra")
public class SdkServerExtraController extends BaseController
{
    private String prefix = "serverListConfig/serverExtra";

    @Autowired
    private ISdkServerExtraService sdkServerExtraService;

    @Autowired
    private ISdkServerListService sdkServerListService;

    @RequiresPermissions("serverListConfig:serverExtra:view")
    @GetMapping()
    public String serverExtra()
    {
        return prefix + "/serverExtra";
    }

//    /**
//     * 查询服务器额外信息列表
//     */
//    @RequiresPermissions("serverListConfig:serverExtra:list")
//    @PostMapping("/list")
//    @ResponseBody
//    public TableDataInfo list(SdkServerExtra sdkServerExtra)
//    {
//        startPage();
//        List<SdkServerExtra> list = sdkServerExtraService.selectSdkServerExtraList(sdkServerExtra);
//        return getDataTable(list);
//    }

    /**
     * 导出服务器额外信息列表
     */
    @RequiresPermissions("serverListConfig:serverExtra:export")
    @Log(title = "服务器额外信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SdkServerExtra sdkServerExtra)
    {
        List<SdkServerExtra> list = sdkServerExtraService.selectSdkServerExtraList(sdkServerExtra);
        ExcelUtil<SdkServerExtra> util = new ExcelUtil<SdkServerExtra>(SdkServerExtra.class);
        return util.exportExcel(list, "服务器额外信息数据");
    }

    /**
     * 新增服务器额外信息
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存服务器额外信息
     */
    @RequiresPermissions("serverListConfig:serverExtra:add")
    @Log(title = "服务器额外信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SdkServerExtra sdkServerExtra)
    {
        return toAjax(sdkServerExtraService.insertSdkServerExtra(sdkServerExtra));
    }

//    /**
//     * 修改服务器额外信息
//     */
//    @GetMapping("/edit/{serverListId}")
//    public String edit(@PathVariable("serverListId") Long serverListId, ModelMap mmap)
//    {
//        SdkServerExtra sdkServerExtra = sdkServerExtraService.selectSdkServerExtraById(serverListId);
//        mmap.put("sdkServerExtra", sdkServerExtra);
//        return prefix + "/edit";
//    }

    /**
     * 修改保存服务器额外信息
     */
    @RequiresPermissions("serverListConfig:serverExtra:edit")
    @Log(title = "服务器额外信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SdkServerExtra sdkServerExtra)
    {
        return toAjax(sdkServerExtraService.updateSdkServerExtra(sdkServerExtra));
    }

    /**
     * 删除服务器额外信息
     */
    @RequiresPermissions("serverListConfig:serverExtra:remove")
    @Log(title = "服务器额外信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sdkServerExtraService.deleteSdkServerExtraByIds(ids));
    }

    /**
     * 获取服务器列表下对应的服务器的额外信息
     * @param serverListId
     * @return
     */
    @PostMapping("/selectServerExtraList")
    @ResponseBody
    public List<SdkServerExtra> selectServerExtraList(long serverListId)
    {
        String serverIds = sdkServerListService.selectServerIdsById(serverListId);
        HashMap map = new HashMap();

        map.put("serverIds", Convert.toStrArray(StringUtils.nvl(serverIds,"")));
        map.put("serverListId",serverListId);
        List<SdkServerExtra> sdkServerExtraList = sdkServerExtraService.selectServerExtraByServerIds(map);
        return sdkServerExtraList;
    }
}
