package com.kits.project.serverListConfig.notice.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.kits.common.utils.DateUtils;
import com.kits.project.serverListConfig.server.service.ISdkServerService;
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
import com.kits.project.serverListConfig.notice.domain.SdkNotice;
import com.kits.project.serverListConfig.notice.service.ISdkNoticeService;
import com.kits.framework.web.controller.BaseController;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.common.utils.poi.ExcelUtil;
import com.kits.framework.web.page.TableDataInfo;

import javax.servlet.http.HttpServletRequest;

import static com.kits.common.utils.DateUtils.YYYY_MM_DD_HH_MM_SS;
import static com.kits.common.utils.DateUtils.dateDiff;


/**
 * 公告管理Controller
 * 
 * @author gm
 * @date 2021-06-22
 */
@Controller
@RequestMapping("/serverListConfig/notice")
public class SdkNoticeController extends BaseController
{
    private String prefix = "serverListConfig/notice";

    @Autowired
    private ISdkNoticeService sdkNoticeService;

    @Autowired
    private ISdkServerService sdkServerService;

    @RequiresPermissions("serverListConfig:notice:view")
    @GetMapping()
    public String notice()
    {
        return prefix + "/notice";
    }

    /**
     * 查询公告管理列表
     */
    @RequiresPermissions("serverListConfig:notice:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(HttpServletRequest request) {
        startPage();
        Map<String, String[]> stringMap = request.getParameterMap();
        HashMap map = new HashMap();
        map.put("noticeName",stringMap.get("noticeName")[0]);
        map.put("noticeType",stringMap.get("noticeType")[0]);
        map.put("startTime",stringMap.get("startTime")[0]);
        map.put("endTime",stringMap.get("endTime")[0]);
        map.put("status",stringMap.get("status")[0]);

        List<SdkNotice> list = sdkNoticeService.selectSdkNoticeListBy(map);
        return getDataTable(list);
    }

    /**
     * 导出公告管理列表
     */
    @RequiresPermissions("serverListConfig:notice:export")
    @Log(title = "公告管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SdkNotice sdkNotice)
    {
        List<SdkNotice> list = sdkNoticeService.selectSdkNoticeList(sdkNotice);
        ExcelUtil<SdkNotice> util = new ExcelUtil<SdkNotice>(SdkNotice.class);
        return util.exportExcel(list, "公告管理数据");
    }

    /**
     * 新增公告管理
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存公告管理
     */
    @RequiresPermissions("serverListConfig:notice:add")
    @Log(title = "公告管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SdkNotice sdkNotice)
    {
        if (null == sdkNotice.getStartTime() || null == sdkNotice.getEndTime()){
            return AjaxResult.error("公告开始时间或结束时间不能为空");
        }
        long dateDiff = dateDiff(sdkNotice.getStartTime(),sdkNotice.getEndTime());
        if (dateDiff > 0){
            return toAjax(sdkNoticeService.insertSdkNotice(sdkNotice));
        }else {
            return AjaxResult.error("公告开始时间结束时间设置错误");
        }
    }

    /**
     * 修改公告管理
     */
    @GetMapping("/edit/{noticeId}")
    public String edit(@PathVariable("noticeId") Long noticeId, ModelMap mmap)
    {
        SdkNotice sdkNotice = sdkNoticeService.selectSdkNoticeById(noticeId);
        mmap.put("sdkNotice", sdkNotice);
        return prefix + "/edit";
    }

    /**
     * 修改保存公告管理
     */
    @RequiresPermissions("serverListConfig:notice:edit")
    @Log(title = "公告管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SdkNotice sdkNotice)
    {
        return toAjax(sdkNoticeService.updateSdkNotice(sdkNotice));
    }

    /**
     * 删除公告管理
     */
    @RequiresPermissions("serverListConfig:notice:remove")
    @Log(title = "公告管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sdkNoticeService.deleteSdkNoticeByIds(ids));
    }
}
