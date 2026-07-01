package com.gm.project.stat.stat_career_distribute.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.stat_career_distribute.dao.IStatCareerDistributeDao;
import com.gm.project.stat.stat_career_distribute.domain.StatCareerDistribute;
import com.gm.project.stat.stat_level_distribute.domain.StatLevelDistribute;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 职业分布Mapper接口
 * 
 * @author gm
 * @date 2021-09-07
 */
@Service
public class StatCareerDistributeDaoImpl implements IStatCareerDistributeDao
{
    public List<StatCareerDistribute> selectStatCareerDistributeList(StatCareerDistribute statCareerDistribute, String channelNames, Integer selectServerId){
        DBClient dbClient  = DBServerMgr.getInstance().getLogDBClient(selectServerId);
        if(dbClient == null){
            return null;
        }
        String sqlStr = "select count(roleId) as rolecount,career from rolestate where createsid="+selectServerId;
        if (!StringUtils.isBlank(channelNames)) {
            sqlStr += " and platformName in (" + channelNames + ")";
        }
        sqlStr += " group by career";
        List<StatCareerDistribute> list =  dbClient.selectList(sqlStr,StatCareerDistribute.class);
        return list;
    }
}
