package com.gm.project.stat.stat_churn_rate.dao;

import com.gm.common.dbclient.DBClient;

import java.util.List;
import java.util.Map;


public interface IStatChurnRateDao {
    /**
     * 获取选择时间内所有登录用户
     * @param dbClient
     * @param channelNames
     * @param table
     * @param startDate
     * @param endDate
     * @return
     */
    public  List<Map<String, Object>> getAllUserDataList(DBClient dbClient, String channelNames, String table, String startDate, String endDate);

    /**
     * 得到充值数据
     * @param dbClient
     * @param channelNames
     * @param selectServerIdList
     * @param table
     * @param minPay
     * @param maxPay
     * @param allUserDataList
     * @return
     */
    public List<Map<String, Object>> getRechargeDataMap(DBClient dbClient, String channelNames,  String selectServerIdList,String table,String minPay, String maxPay,List<Map<String, Object>> allUserDataList);

    public List<Map<String, Object>> getAmountDataList(DBClient dbClient,String table, String channelNames, String vipUserId, int amountSum);

    public List<Map<String, Object>> getUserLevelDataList(DBClient dbClient,String serverIdList, String channelNames, String users);
}
