package com.gm.project.stat.stat_recharge_accumulate.dao;


import java.util.List;
import java.util.Map;

/**
 * 累积充值统计dao
 * 
 * @author gm
 * @date 2021-08-06
 */
public interface IStatRechargeAccumulateDao
{


    /**
     * 累积充值统计 获取数据
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param blackUsers
     * @return
     */
    public List<Map<String, Object>> getRechargeAccumulateList(String selectServerIds, String channelNames, String startDate, String endDate, String blackUsers);

}
