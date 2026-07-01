package com.gm.project.stat.stat_recharge_second.service;



import com.gm.project.stat.stat_recharge_second.domain.RechargeSecondItemBean;
import com.gm.project.stat.stat_recharge_second.domain.RechargeSecondTimeIntervaBean;

import java.util.List;

/**
 * 二次付费统计Service接口
 * 
 * @author gm
 * @date 2021-09-13
 */
public interface IStatRechargeSecondService
{


    /**
     * 付费次数统计
     * @param selectGroupName
     * @param selectServerIdList
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isBlack
     */
    public List<RechargeSecondTimeIntervaBean> statRechargeSecondTimeInterval(String selectGroupName,String selectServerIdList, String channelNames,String startDate, String endDate, Boolean isBlack);

    /**
     * 二次付费项目统计
     * @param selectGroupName
     * @param selectServerIdList
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isBlack
     * @return
     */
    public List<RechargeSecondItemBean> statRechargeSecondItem(String selectGroupName,String selectServerIdList, String channelNames,String startDate, String endDate, Boolean isBlack);
}
