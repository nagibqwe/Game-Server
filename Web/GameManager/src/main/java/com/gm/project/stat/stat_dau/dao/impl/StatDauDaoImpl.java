package com.gm.project.stat.stat_dau.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.stat_dau.dao.IStatDauDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * DAU 数据查询
 * 每日登陆过游戏的用户数
 */
@Service
public class StatDauDaoImpl implements IStatDauDao {

    /**
     * 获取DAU的数据
     * @param table
     * @param channelNames
     * @param selectServerIds
     * @param blackUserStr
     * @param startDate
     * @param endDate
     * @param level
     * @return
     */
    public List<Map<String, Object>> getDayDAUDataList(String table, String channelNames, String selectServerIds, String blackUserStr, String startDate, String endDate, int level) {
        String sqlStr = "SELECT userId,level FROM (SELECT userId,max(level) level,platformName,serverId ";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE timeLogin  BETWEEN '" + startDate + " 00:00:00'  AND '" + endDate + " 23:59:59'";
        sqlStr += " group by userId) t";
        sqlStr += " WHERE t.serverId IN ('" + selectServerIds + "')";
        if (!StringUtils.isBlank(channelNames)) {
            sqlStr += " AND t.platformName IN (" + channelNames + ")";
        }
        if (!StringUtils.isBlank(blackUserStr)) {
            sqlStr += "AND t.userId NOT IN (" + blackUserStr + ")";
        }
        if (level != 0) {
            sqlStr += " AND t.level >= " + level;
        }

        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlStr.toString());
        return mapList;
    }
}
