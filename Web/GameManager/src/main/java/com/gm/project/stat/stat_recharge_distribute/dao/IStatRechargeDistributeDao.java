package com.gm.project.stat.stat_recharge_distribute.dao;


import java.util.List;
import java.util.Map;

/**
 * Пополнение统计dao
 * 
 * @author gm
 * @date 2021-08-06
 */
public interface IStatRechargeDistributeDao
{
    /**
     * ПополнениеУровень统计 Данные
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> payLevelStat(String selectServerIds,String channelNames,String startDate, String endDate);

    /**
     * 每ДеньПополнение统计
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
     * ПополнениеКоличество
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> payCountStat(String selectServerIds, String channelNames, String startDate, String endDate);


}
