package com.gm.project.stat.stat_recharge_distribute.dao;


import java.util.List;
import java.util.Map;

/**
 * 充值统计dao
 * 
 * @author gm
 * @date 2021-08-06
 */
public interface IStatRechargeDistributeDao
{
    /**
     * 充值等级统计 数据
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> payLevelStat(String selectServerIds,String channelNames,String startDate, String endDate);

    /**
     * 每天充值统计
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @return
     */
    public List<Map<String, Object>> payDaylStat( String selectServerIds,String channelNames,String startDate);

    /**
     * 礼包id分布
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @return
     */
    public List<Map<String, Object>> payGoodIdslStat(String selectServerIds,String channelNames,String startDate, String endDate);


    /**
     * 充值人数
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> payCountStat(String selectServerIds, String channelNames, String startDate, String endDate);


}
