package com.gm.project.gamelog.feedbacklog.controller;

import com.gm.common.utils.StringUtils;
import java.util.List;
import java.util.Map;

import com.gm.project.common.utils.GameLogUtil;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.gm.framework.aspectj.lang.annotation.Log;
import com.gm.framework.aspectj.lang.enums.BusinessType;
import com.gm.project.gamelog.feedbacklog.domain.Feedbacklog;
import com.gm.project.gamelog.feedbacklog.service.IFeedbacklogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 反馈日志Controller
 * 
 * @author gm
 * @date 2021-09-10
 */
@Controller
@RequestMapping("/gamelog/feedbacklog")
public class FeedbacklogController extends BaseController
{
    private String prefix = "gamelog/feedbacklog";

    @Autowired
    private IFeedbacklogService feedbacklogService;

    @RequiresPermissions("gamelog:feedbacklog:view")
    @GetMapping()
    public String feedbacklog()
    {
        return prefix + "/feedbacklog";
    }
    /**
     * 查询反馈日志列表
     */
    @RequiresPermissions("gamelog:feedbacklog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Feedbacklog feedbacklog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Feedbacklog> list = feedbacklogService.selectFeedbacklogList(feedbacklog,param);
        return getDataTable(list);
    }
    /**
     * 导出反馈日志列表
     */
    @RequiresPermissions("gamelog:feedbacklog:export")
    @Log(title = "反馈日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Feedbacklog feedbacklog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        List<Feedbacklog> list = feedbacklogService.selectFeedbacklogList(feedbacklog,param);
        ExcelUtil<Feedbacklog> util = new ExcelUtil<Feedbacklog>(Feedbacklog.class);
        return util.exportExcel(list, "反馈日志数据");
    }
}
