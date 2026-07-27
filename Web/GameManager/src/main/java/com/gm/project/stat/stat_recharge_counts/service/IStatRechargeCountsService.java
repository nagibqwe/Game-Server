package com.gm.project.stat.stat_recharge_counts.service;



import com.gm.project.stat.stat_recharge_counts.domain.RechargeCountsBean;

import java.util.List;

/**
 * 付费充值统计Service接口
 * 
 * @author gm
 * @date 2021-09-13
 */
public interface IStatRechargeCountsService
{


    /**
     * 付费次数统计
     * @param selectGroupName
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isBlack
     */
    public List<RechargeCountsBean> rechargeCountsStat(String selectGroupName, String selectServerIds, String channelNames, String startDate, String endDate, Boolean isBlack);
}
