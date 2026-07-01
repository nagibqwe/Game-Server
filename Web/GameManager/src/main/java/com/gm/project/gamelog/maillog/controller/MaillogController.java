package com.gm.project.gamelog.maillog.controller;

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
import com.gm.project.gamelog.maillog.domain.Maillog;
import com.gm.project.gamelog.maillog.service.IMaillogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 邮件日志Controller
 * 
 * @author gm
 * @date 2021-09-08
 */
@Controller
@RequestMapping("/gamelog/maillog")
public class MaillogController extends BaseController
{
    private String prefix = "gamelog/maillog";

    @Autowired
    private IMaillogService maillogService;

    @RequiresPermissions("gamelog:maillog:view")
    @GetMapping()
    public String maillog()
    {
        return prefix + "/maillog";
    }
    /**
     * 查询邮件日志列表
     */
    @RequiresPermissions("gamelog:maillog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Maillog maillog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Maillog> list = maillogService.selectMaillogList(maillog,param);
        return getDataTable(list);
    }
    /**
     * 导出邮件日志列表
     */
    @RequiresPermissions("gamelog:maillog:export")
    @Log(title = "邮件日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Maillog maillog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        List<Maillog> list = maillogService.selectMaillogList(maillog,param);
        ExcelUtil<Maillog> util = new ExcelUtil<Maillog>(Maillog.class);
        return util.exportExcel(list, "邮件日志数据");
    }
}
