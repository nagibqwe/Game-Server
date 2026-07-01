package com.gm.project.stat.stat_recharge_second.controller;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.controller.BaseController;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_recharge_second.domain.RechargeSecondItemBean;
import com.gm.project.stat.stat_recharge_second.domain.RechargeSecondTimeIntervaBean;
import com.gm.project.stat.stat_recharge_second.service.IStatRechargeSecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 二次付费统计Controller
 * 
 * @author gm
 * @date 2021-09-13
 */
@Controller
@RequestMapping("/stat/stat_recharge_second")
public class StatRechargeSecondController extends BaseController
{
    private String prefix = "stat/stat_recharge_second";

    @Autowired
    private IStatRechargeSecondService statRechargeCountsService;
    /**
     * 跳转到付费次数统计界面
     * @return
     */
    @GetMapping("/to_stat_recharge_second")
    public String toStatRechargeCounts()
    {
        return prefix + "/stat_recharge_second";
    }
    /**
     * 付费次数统计
     * @param channelNames
     * @param selectServerIdList
     * @param startDate
     * @return
     */
    @PostMapping("/stat_recharge_second_time_interval")
    @ResponseBody
    public  TableDataInfo statRechargeSecondTimeInterval(String selectGroupName,String selectServerIdList, String channelNames,String startDate, String endDate, Boolean isBlack){
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        startPage();
       List<RechargeSecondTimeIntervaBean> dataList= this.statRechargeCountsService.statRechargeSecondTimeInterval(selectGroupName,selectServerIdList,channelNames,startDate,endDate,isBlack);
        return  getDataTable(dataList);
    }
    @PostMapping("/stat_recharge_second_item")
    @ResponseBody
    public  TableDataInfo statRechargeSecondItem(String groupName,String selectServerIdList, String channelNames,String startDate, String endDate, boolean isBlack){
        if(StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)){
            return getDataTableErrorMsg("请选择开始 和 结束时间");
        }
        if(StringUtils.isEmpty(selectServerIdList)){
            return getDataTableErrorMsg("请选择服务器列表");
        }
        startPage();
        List<RechargeSecondItemBean> dataList= this.statRechargeCountsService.statRechargeSecondItem(groupName,selectServerIdList,channelNames,startDate,endDate,isBlack);
        return  getDataTable(dataList);
    }
 }
