package com.gm.project.stat.stat_churn_rate.service;

import com.gm.project.stat.stat_churn_rate.domain.PlayerLeaveAmountBean;
import com.gm.project.stat.stat_churn_rate.domain.PlayerLeaveCountBean;
import com.gm.project.stat.stat_churn_rate.domain.PlayerLeaveRankBean;

import java.text.ParseException;
import java.util.List;

/**
 * 玩家流失统计服务
 */
public interface IStatChurnRateService {
    /**
     * 流失用户统计
     * @param selectGroupName
     * @param selectServerIdList
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isBlack
     * @param select_condition
     * @param minPay
     * @param maxPay
     * @return
     * @throws ParseException
     */
    public List<PlayerLeaveCountBean> playerLeaveCount(String selectGroupName, String selectServerIdList, String channelNames, String startDate, String endDate, Boolean  isBlack,
                                                       String select_condition, String minPay, String maxPay) throws ParseException;

    /**
     * 流失用户付费金额与次数
     * @param selectGroupName
     * @param selectServerIdList
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isBlack
     * @param select_condition
     * @param minPay
     * @param maxPay
     * @return
     * @throws ParseException
     */
    public List<PlayerLeaveAmountBean> playerLeaveAmount(String selectGroupName, String selectServerIdList, String channelNames, String startDate, String endDate, Boolean  isBlack,
                                                         String select_condition, String minPay, String maxPay) throws ParseException;

    /**
     * 流失用户等级分布
     * @param selectGroupName
     * @param selectServerIdList
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isBlack
     * @param select_condition
     * @param minPay
     * @param maxPay
     * @return
     * @throws ParseException
     */
    public List<PlayerLeaveRankBean> playerLeaveRank(String selectGroupName, String selectServerIdList, String channelNames, String startDate, String endDate, Boolean  isBlack,
                                                     String select_condition, String minPay, String maxPay) throws ParseException;
}
