package com.kits.project.serverListConfig.x8serverExtra.controller;

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
import com.kits.project.serverListConfig.x8serverExtra.domain.TX8ServerPolicy;
import com.kits.project.serverListConfig.x8serverExtra.service.ITX8ServerPolicyService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;


/**
 * 渠道区服明细Controller
 * 
 * @author gm
 * @date 2021-07-08
 */
@Controller
@RequestMapping("/serverListConfig/x8serverExtra")
public class TX8ServerPolicyController extends BaseController
{
    private String prefix = "serverListConfig/x8serverExtra";

    @Autowired
    private ITX8ServerPolicyService tX8ServerPolicyService;

    @RequiresPermissions("serverListConfig:x8serverExtra:view")
    @GetMapping()
    public String x8serverExtra()
    {
        return prefix + "/x8serverExtra";
    }

    /**
     * 查询渠道区服明细列表
     */
    @RequiresPermissions("serverListConfig:x8serverExtra:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TX8ServerPolicy tX8ServerPolicy)
    {
        startPage();
        List<TX8ServerPolicy> list = tX8ServerPolicyService.selectTX8ServerPolicyList(tX8ServerPolicy);
        return getDataTable(list);
    }

    /**
     * 导出渠道区服明细列表
     */
    @RequiresPermissions("serverListConfig:x8serverExtra:export")
    @Log(title = "渠道区服明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TX8ServerPolicy tX8ServerPolicy)
    {
        List<TX8ServerPolicy> list = tX8ServerPolicyService.selectTX8ServerPolicyList(tX8ServerPolicy);
        ExcelUtil<TX8ServerPolicy> util = new ExcelUtil<TX8ServerPolicy>(TX8ServerPolicy.class);
        return util.exportExcel(list, "渠道区服明细数据");
    }

    /**
     * 新增渠道区服明细
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存渠道区服明细
     */
    @RequiresPermissions("serverListConfig:x8serverExtra:add")
    @Log(title = "渠道区服明细", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(TX8ServerPolicy tX8ServerPolicy)
    {
        return toAjax(tX8ServerPolicyService.insertTX8ServerPolicy(tX8ServerPolicy));
    }

    /**
     * 修改渠道区服明细
     */
    @GetMapping("/edit/{policyId}")
    public String edit(@PathVariable("policyId") Long policyId, ModelMap mmap)
    {
        TX8ServerPolicy tX8ServerPolicy = tX8ServerPolicyService.selectTX8ServerPolicyById(policyId);
        mmap.put("tX8ServerPolicy", tX8ServerPolicy);
        return prefix + "/edit";
    }

    /**
     * 修改保存渠道区服明细
     */
    @RequiresPermissions("serverListConfig:x8serverExtra:edit")
    @Log(title = "渠道区服明细", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TX8ServerPolicy tX8ServerPolicy)
    {
        return toAjax(tX8ServerPolicyService.updateTX8ServerPolicy(tX8ServerPolicy));
    }

    /**
     * 删除渠道区服明细
     */
    @RequiresPermissions("serverListConfig:x8serverExtra:remove")
    @Log(title = "渠道区服明细", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(tX8ServerPolicyService.deleteTX8ServerPolicyByIds(ids));
    }
}
