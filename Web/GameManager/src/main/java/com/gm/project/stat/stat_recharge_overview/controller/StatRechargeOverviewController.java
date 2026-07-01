package com.gm.project.stat.stat_recharge_overview.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_recharge_overview.domain.StatRechargeOverviewBean;
import com.gm.project.stat.stat_recharge_overview.service.IStatRechargeOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 付费总览 Controller
 * 
 * @author gm
 * @date 2021-10-28
 */
@Controller
@RequestMapping("/stat/stat_recharge_overview")
public class StatRechargeOverviewController extends BaseController
{
    private String prefix = "stat/stat_recharge_overview";
    @Autowired
    private IStatRechargeOverviewService statRechargeOverview;
    @GetMapping("to_stat_recharge_overview")
    public String to_stat_recharge_overview()
    {
        return prefix + "/stat_recharge_overview";
    }

    /**
     * dau 统计
     */
    @PostMapping("/stat_recharge_overview")
    @ResponseBody
    public TableDataInfo stat_recharge_overview(String selectGroupName,String selectServerIdList, String channelNames, String startDate, String endDate, Boolean isBlack)
    {
        if(StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        startPage();
        List<StatRechargeOverviewBean> dataList = this.statRechargeOverview.StatRechargeOverview(selectGroupName,selectServerIdList,channelNames,startDate,endDate,isBlack);
        return getDataTable(dataList);
    }

}
