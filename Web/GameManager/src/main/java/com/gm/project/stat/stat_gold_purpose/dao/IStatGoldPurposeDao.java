package com.gm.project.stat.stat_gold_purpose.dao;


import com.gm.common.dbclient.DBClient;
import com.gm.project.stat.stat_gold_purpose.domain.GoldPurposeBean;

import java.util.List;
import java.util.Map;

/**
 * 元宝用途统计
 * 
 * @author gm
 * @date 2021-08-06
 */
public interface IStatGoldPurposeDao
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
    public List<GoldPurposeBean> getGoldConsumeList(DBClient dbClient, String table, String serverId, String channelNames, String startDate, String endDate, String blackUsers);
}
