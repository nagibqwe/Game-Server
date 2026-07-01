package com.gm.project.gamelog.gmcommandlog.controller;

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
import com.gm.project.gamelog.gmcommandlog.domain.Gmcommandlog;
import com.gm.project.gamelog.gmcommandlog.service.IGmcommandlogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * gm命令日志Controller
 * 
 * @author gm
 * @date 2021-09-08
 */
@Controller
@RequestMapping("/gamelog/gmcommandlog")
public class GmcommandlogController extends BaseController
{
    private String prefix = "gamelog/gmcommandlog";

    @Autowired
    private IGmcommandlogService gmcommandlogService;

    @RequiresPermissions("gamelog:gmcommandlog:view")
    @GetMapping()
    public String gmcommandlog()
    {
        return prefix + "/gmcommandlog";
    }
    /**
     * 查询gm命令日志列表
     */
    @RequiresPermissions("gamelog:gmcommandlog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Gmcommandlog gmcommandlog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Gmcommandlog> list = gmcommandlogService.selectGmcommandlogList(gmcommandlog,param);
        return getDataTable(list);
    }
    /**
     * 导出gm命令日志列表
     */
    @RequiresPermissions("gamelog:gmcommandlog:export")
    @Log(title = "gm命令日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Gmcommandlog gmcommandlog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        List<Gmcommandlog> list = gmcommandlogService.selectGmcommandlogList(gmcommandlog,param);
        ExcelUtil<Gmcommandlog> util = new ExcelUtil<Gmcommandlog>(Gmcommandlog.class);
        return util.exportExcel(list, "gm命令日志数据");
    }
}
