package com.gm.project.stat.stat_recharge_overview.dao;

import java.util.List;
import java.util.Map;

public interface IStatRechargeOverviewDao {



    /**
     * 获取当天登录过的账号(DAU)
     */
    public List<Map<String, Object>> getLoginUsersDataList(String table, String serverId, String channelNames, String start, String end, String blackUserStr);
    /**
     * 获取当天付费人数、付费总额
     */
    public List<Map<String, Object>> getRechargeUsersDataList(String table, String serverId, String channelNames, String start, String end, String blackUserStr);

    /**
     * 获取当天新增账号
     */
    public List<Map<String, Object>> getNewUsersDataList(String table, String serverId, String channelNames, String start, String end, String blackUserStr);

    /**
     * 获取当天新增付费账号
     */
    public List<Map<String, Object>> getNewRechargeUsersDataList(String table, String serverId, String channelNames, String start, String end, String blackUserStr);

    /**
     * 老玩家付费账号
     */
    public List<Map<String, Object>> getOldRechargeUsersDataList(String table, String serverId, String channelNames, String start, String end, String blackUserStr);

}
