package com.gm.project.stat.stat_level_distribute.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.utils.StringUtils;
import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;
import com.gm.project.stat.common.dao.BaseDao;
import com.gm.project.stat.stat_level_distribute.dao.IStatLevelDistributeDao;
import com.gm.project.stat.stat_level_distribute.domain.StatLevelDistribute;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 角色等级分布对象 stat_level_distribute
 * 
 * @author gm
 * @date 2021-08-06
 */

@Service
public class StatLevelDistributeDaoImpl extends BaseDao implements IStatLevelDistributeDao
{
    private static final long serialVersionUID = 1L;
    /**
     * 离线时候的等级分布
     * @param channelNames
     * @param serverId
     * @param level
     * @param startDate
     * @param endDate
     * @return
     */
    public List<StatLevelDistribute> getLevelDistributeListByCondition0(DBClient logDBClient,String tableName, String channelNames, String serverId, String level, String startDate, String endDate){
        if(logDBClient == null){
            return new ArrayList<>();
        }
        String sqlStr = "SELECT COUNT(t.roleId) as rolecount,t.level FROM (SELECT roleId, MAX(level) as level FROM "+tableName;
        sqlStr += " WHERE sid="+serverId;
        if (!StringUtils.isBlank(startDate)) {
            startDate += " 00:00:00";
            sqlStr += " and createTime>= UNIX_TIMESTAMP('"+startDate+"')";
        }
        if (!StringUtils.isBlank(endDate)) {
            endDate += " 23:59:59";
            sqlStr += " and createTime<= UNIX_TIMESTAMP('"+endDate+"')";
        }
        if (!StringUtils.isBlank(channelNames)) {
            sqlStr += " and platformName in (" + channelNames + ")";
        }
        sqlStr += " GROUP BY roleId) t";
        if (!level.equals("")) {
            sqlStr += " WHERE t.level >= "+level;
        }
        sqlStr += " GROUP BY t.level";

        List<StatLevelDistribute> list =  logDBClient.selectList(sqlStr,StatLevelDistribute.class);
        return list;
    }

    /**
     * 离线后加上离线挂机的等级分布
     * @param channelNames
     * @param serverId
     * @param level
     * @param startDate
     * @param endDate
     * @return
     */
    public List<StatLevelDistribute> getLevelDistributeListByCondition1Sql(DBClient logDBClient,String channelNames, String serverId, String level, String startDate, String endDate){
        if(logDBClient == null){
            return new ArrayList<>();
        }
        String sqlStr = "select count(roleId) as rolecount,level from rolestate where createsid="+serverId+" ";
        if (!level.equals("")) {
            sqlStr += " and level >= " + level;
        }
        if (!StringUtils.isBlank(startDate)) {
            startDate += " 00:00:00'";
            sqlStr += " and createTime>='"+startDate;
        }
        if (!StringUtils.isBlank(endDate)) {
            endDate += " 23:59:59'";
            sqlStr += " and createTime<='" + endDate;
        }
        if (!StringUtils.isBlank(channelNames)) {
            sqlStr += " and platformName in (" + channelNames + ")";
        }
        sqlStr += " group by level;";
        List<StatLevelDistribute> list =  logDBClient.selectList(sqlStr,StatLevelDistribute.class);
        return list;
    }
}
