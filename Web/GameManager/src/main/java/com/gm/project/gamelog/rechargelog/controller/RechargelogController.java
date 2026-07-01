package com.gm.project.gamelog.rechargelog.controller;

import com.gm.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gm.common.utils.sql.SqlUtil;
import com.gm.framework.web.page.PageDomain;
import com.gm.framework.web.page.TableSupport;
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
import com.gm.project.gamelog.rechargelog.domain.Rechargelog;
import com.gm.project.gamelog.rechargelog.service.IRechargelogService;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.common.utils.poi.ExcelUtil;
import com.gm.framework.web.page.TableDataInfo;


/**
 * 充值日志Controller
 * 
 * @author gm
 * @date 2021-09-09
 */
@Controller
@RequestMapping("/gamelog/rechargelog")
public class RechargelogController extends BaseController
{
    private String prefix = "gamelog/rechargelog";

    @Autowired
    private IRechargelogService rechargelogService;

    @RequiresPermissions("gamelog:rechargelog:view")
    @GetMapping()
    public String rechargelog()
    {
        return prefix + "/rechargelog";
    }
    /**
     * 查询充值日志列表
     */
    @RequiresPermissions("gamelog:rechargelog:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Rechargelog rechargelog,String startDate,String endDate,Integer serverId)
    {
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(serverId == null || serverId == 0){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        Map<String,Object> param = new HashMap<>();
        param.put("startDate",startDate);
        param.put("endDate",endDate);
        param.put("serverId",serverId);

        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            param.put("pageNum",pageNum);
            param.put("pageSize",pageSize);
            param.put("orderBy",orderBy);
        }
        startPage();
        TableDataInfo tableDataInfo = getDataTable(new ArrayList<>());
        List<Rechargelog> list = rechargelogService.selectRechargelogList(rechargelog,param);
        if(param.containsKey("count")){
            tableDataInfo.setTotal((int)param.get("count"));
        }
        tableDataInfo.setRows(list);
        return tableDataInfo;
    }
    /**
     * 导出充值日志列表
     */
    @RequiresPermissions("gamelog:rechargelog:export")
    @Log(title = "充值日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Rechargelog rechargelog,String startDate,String endDate,Integer serverId)
    {

        Map<String,Object> param = new HashMap<>();
        param.put("startDate",startDate);
        param.put("endDate",endDate);
        param.put("serverId",serverId);
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            param.put("pageNum",pageNum);
            param.put("pageSize",pageSize);
            param.put("orderBy",orderBy);
        }
        List<Rechargelog> list = rechargelogService.selectRechargelogList(rechargelog,param);
        ExcelUtil<Rechargelog> util = new ExcelUtil<Rechargelog>(Rechargelog.class);
        return util.exportExcel(list, "充值日志数据");
    }
}
