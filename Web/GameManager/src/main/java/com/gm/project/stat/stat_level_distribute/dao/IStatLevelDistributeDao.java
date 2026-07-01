package com.gm.project.stat.stat_level_distribute.dao;


import com.gm.common.dbclient.DBClient;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.stat_level_distribute.domain.StatLevelDistribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色等级分布Dao接口
 * 
 * @author gm
 * @date 2021-08-06
 */
public interface IStatLevelDistributeDao
{
    /**
     * 离线时候的等级分布
     * @param channelNames
     * @param serverId
     * @param level
     * @param startDate
     * @param endDate
     * @return
     */
     List<StatLevelDistribute> getLevelDistributeListByCondition0(DBClient logDBClient,String tableName, String channelNames, String serverId, String level, String startDate, String endDate);

    /**
     * 离线后加上离线挂机的等级分布
     * @param channelNames
     * @param serverId
     * @param level
     * @param startDate
     * @param endDate
     * @return
     */
     List<StatLevelDistribute> getLevelDistributeListByCondition1Sql(DBClient logDBClient,String channelNames, String serverId, String level, String startDate, String endDate);
}
