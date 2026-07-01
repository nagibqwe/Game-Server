package com.gm.project.gamelog.backgmcmdlog.controller;

import com.gm.common.utils.StringUtils;

import java.util.HashMap;
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
import com.gm.project.gamelog.backgmcmdlog.domain.Backgmcmdlog;
import com.gm.project.gamelog.backgmcmdlog.service.IBackgmcmdlogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 后台指令日志Controller
 * 
 * @author gm
 * @date 2021-09-10
 */
@Controller
@RequestMapping("/gamelog/backgmcmdlog")
public class BackgmcmdlogController extends BaseController
{
    private String prefix = "gamelog/backgmcmdlog";

    @Autowired
    private IBackgmcmdlogService backgmcmdlogService;

    @RequiresPermissions("gamelog:backgmcmdlog:view")
    @GetMapping()
    public String backgmcmdlog()
    {
        return prefix + "/backgmcmdlog";
    }
    /**
     * 查询后台指令日志列表
     */
    @RequiresPermissions("gamelog:backgmcmdlog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Backgmcmdlog backgmcmdlog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        startPage();
        List<Backgmcmdlog> list = backgmcmdlogService.selectBackgmcmdlogList(backgmcmdlog,param);
        return getDataTable(list);
    }
    /**
     * 导出后台指令日志列表
     */
    @RequiresPermissions("gamelog:backgmcmdlog:export")
    @Log(title = "后台指令日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Backgmcmdlog backgmcmdlog,String startDate,String endDate,Integer serverId,Integer pageSize)
    {

        Map<String,Object> param = GameLogUtil.getParamMap(startDate,endDate,serverId,pageSize);
        List<Backgmcmdlog> list = backgmcmdlogService.selectBackgmcmdlogList(backgmcmdlog,param);
        ExcelUtil<Backgmcmdlog> util = new ExcelUtil<Backgmcmdlog>(Backgmcmdlog.class);
        return util.exportExcel(list, "后台指令日志数据");
    }
}
