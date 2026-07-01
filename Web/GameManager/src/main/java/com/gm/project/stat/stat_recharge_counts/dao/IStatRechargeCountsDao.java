package com.gm.project.stat.stat_recharge_counts.dao;


import java.util.List;
import java.util.Map;

/**
 * 充值统计dao
 * 
 * @author gm
 * @date 2021-08-06
 */
public interface IStatRechargeCountsDao
{

    /**
     * 付费次数统计 获取数据
     * @param table
     * @param serverId
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param blackUsers
     * @return
     */
    public List<Map<String, Object>> getRechargeCountsDataList(String table, String serverId, String channelNames, String startDate, String endDate, String blackUsers);
}
