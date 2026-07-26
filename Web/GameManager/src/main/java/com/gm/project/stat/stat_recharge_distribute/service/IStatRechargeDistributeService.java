package com.gm.project.stat.stat_recharge_distribute.service;



import java.util.List;
import java.util.Map;

/**
 * Пополнение分布Service接口
 * 
 * @author gm
 * @date 2021-09-13
 */
public interface IStatRechargeDistributeService
{
    /**
     * Пополнение挡位分布
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> payLevelStat(String selectServerIds,String channelNames,String startDate, String endDate);

    /**
     * Пополнение每日Время段分布
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @return
     */
    public List<Map<String, Object>> payDaylStat(String selectServerIds,String channelNames,String startDate);

    /**
     * Пополнение礼包分布
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @return
     */
    public List<Map<String, Object>> payGoodIdslStat(String selectServerIds,String channelNames,String startDate, String endDate);
}
