package com.gm.project.stat.stat_churn_rate.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_churn_rate.domain.PlayerLeaveAmountBean;
import com.gm.project.stat.stat_churn_rate.domain.PlayerLeaveCountBean;
import com.gm.project.stat.stat_churn_rate.domain.PlayerLeaveRankBean;
import com.gm.project.stat.stat_churn_rate.service.IStatChurnRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;


/**
 * 流失Controller
 * 
 * @author gm
 * @date 2021-05-25
 */
@Controller
@RequestMapping("/stat/stat_churn_rate")
public class StatChurnRateController extends BaseController
{
    private String prefix = "stat/stat_churn_rate";

    @Autowired
    private IStatChurnRateService statChurnRateService;

    @GetMapping("to_stat_churn_rate")
    public String to_stat_churn_rate()
    {

        return prefix + "/stat_churn_rate";
    }
    /**
     * 查询留存统计列表
     */
    @PostMapping("/stat_player_leave_count")
    @ResponseBody
    public TableDataInfo  stat_player_leave_count(String selectGroupName, String selectServerIdList, String channelNames, String startDate, String endDate, Boolean  isblack,
                                                                             String select_condition, String minPay, String maxPay) throws ParseException{
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        List<PlayerLeaveCountBean> playerLeaveCountBeanList = this.statChurnRateService.playerLeaveCount(selectGroupName,selectServerIdList,channelNames,startDate,endDate,isblack,select_condition,minPay,maxPay);
        return  getDataTable(playerLeaveCountBeanList);
    }
    /**
     * 查询留存统计列表
     */
    @PostMapping("/stat_player_leave_amount")
    @ResponseBody
    public TableDataInfo  stat_player_leave_amount(String selectGroupName, String selectServerIdList, String channelNames, String startDate, String endDate, Boolean  isblack,
                                                   String select_condition, String minPay, String maxPay) throws ParseException{
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        List<PlayerLeaveAmountBean> playerLeaveCountBeanList = this.statChurnRateService.playerLeaveAmount(selectGroupName,selectServerIdList,channelNames,startDate,endDate,isblack,select_condition,minPay,maxPay);
        return  getDataTable(playerLeaveCountBeanList);
    }
    /**
     * 用户等级分布流失
     */
    @PostMapping("/stat_player_leave_rank")
    @ResponseBody
    public TableDataInfo  stat_player_leave_rank(String selectGroupName, String selectServerIdList, String channelNames, String startDate, String endDate, Boolean  isblack,
                                                   String select_condition, String minPay, String maxPay) throws ParseException{
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        List<PlayerLeaveRankBean> dataList = this.statChurnRateService.playerLeaveRank(selectGroupName,selectServerIdList,channelNames,startDate,endDate,isblack,select_condition,minPay,maxPay);
        return  getDataTable(dataList);
    }

}
