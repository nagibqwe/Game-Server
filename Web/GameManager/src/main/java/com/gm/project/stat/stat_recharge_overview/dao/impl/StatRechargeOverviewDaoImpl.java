package com.gm.project.stat.stat_recharge_overview.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.stat_recharge_overview.dao.IStatRechargeOverviewDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * DAU 数据查询
 * 每日登陆过游戏的用户数
 */
@Service
public class StatRechargeOverviewDaoImpl implements IStatRechargeOverviewDao {


    /*
     * 获取当天登录过的账号(DAU)
     */
    public List<Map<String, Object>> getLoginUsersDataList(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT userId";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE TIME BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59') AND sid in (" + serverId + ")";
        if (channelNames != null){
            if (!StringUtils.isBlank(channelNames)) {
                sqlStr += " AND platformName in (" + channelNames + ")";
            }
        }
        if (!blackUserStr.equals("")) {
            sqlStr += " AND userId NOT IN (" + blackUserStr + ")";
        }
        sqlStr += " GROUP BY userId";
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlStr.toString());
        return mapList;
    }

    /*
     * 获取当天付费人数、付费总额
     */
    public List<Map<String, Object>> getRechargeUsersDataList(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT userId,sum(totalFee) as amount";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE TIME BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59')";
        sqlStr += " AND statusReason=7 AND status=1 and totalFee>0 and src=0 AND sid in (" + serverId + ")";
        if (channelNames != null)
            if (!StringUtils.isBlank(channelNames)) {
                sqlStr += " AND platformName in (" + channelNames + ")";
            }
        if (!blackUserStr.equals("")) {
            sqlStr += " AND userId NOT IN (" + blackUserStr + ")";
        }
        sqlStr += " GROUP BY userId";
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlStr.toString());
        return mapList;
    }

    /*
     * 获取当天新增账号
     */
    public List<Map<String, Object>> getNewUsersDataList(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT r.userId";
        sqlStr += " FROM (SELECT userId, DATE_FORMAT(MIN(createTime), '%Y-%m-%d') createTime,platformName,createsid FROM " + table + " GROUP BY userId) r";
        sqlStr += " WHERE UNIX_TIMESTAMP(r.createTime) BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59') AND r.createsid in (" + serverId + ")";
        if (channelNames != null)
            if (!StringUtils.isBlank(channelNames)) {
                sqlStr += " AND r.platformName in (" + channelNames + ")";
            }
        if (!blackUserStr.equals("")) {
            sqlStr += " AND r.userId NOT IN (" + blackUserStr + ")";
        }
        sqlStr += " GROUP BY r.userId";
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlStr.toString());
        return mapList;
    }

    /*
     * 获取当天新增付费账号
     */
    public List<Map<String, Object>> getNewRechargeUsersDataList(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT t.userId FROM (SELECT userId, DATE_FORMAT(FROM_UNIXTIME(time), '%Y-%m-%d') dateTime";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE time BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59')";
        sqlStr += " AND statusReason=7 AND status=1 and totalFee>0 and src=0 AND sid in (" + serverId + ")";
        sqlStr += " GROUP BY userId ) t";
        sqlStr += " LEFT JOIN (SELECT userId, DATE_FORMAT(MIN(createTime), '%Y-%m-%d') createTime,platformName";
        sqlStr += " FROM rolestate";
        sqlStr += " GROUP BY userId) r";
        sqlStr += " ON t.userId=r.userId";
        sqlStr += " WHERE UNIX_TIMESTAMP(createTime) BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59')";
        if (!StringUtils.isBlank(channelNames)) {
            sqlStr += " AND r.platformName in (" + channelNames + ")";
        }
        if (!blackUserStr.equals("")) {
            sqlStr += " AND t.userId NOT IN (" + blackUserStr + ")";
        }
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlStr.toString());
        return mapList;
    }

    /*
     * 老玩家付费账号
     */
    public List<Map<String, Object>> getOldRechargeUsersDataList(String table, String serverId, String channelNames, String start, String end, String blackUserStr) {
        String sqlStr = " SELECT t.userId FROM (SELECT userId, DATE_FORMAT(FROM_UNIXTIME(time), '%Y-%m-%d') dateTime";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE time BETWEEN UNIX_TIMESTAMP('" + start + " 00:00:00')  AND UNIX_TIMESTAMP('" + end + " 23:59:59')";
        sqlStr += " AND statusReason=7 AND status=1 and totalFee>0 and src=0 AND sid in (" + serverId + ")";
        sqlStr += " GROUP BY userId ) t";
        sqlStr += " LEFT JOIN (SELECT userId, DATE_FORMAT(MIN(createTime), '%Y-%m-%d') createTime,platformName";
        sqlStr += " FROM stat_role";
        sqlStr += " GROUP BY userId) r";
        sqlStr += " ON t.userId=r.userId";
        sqlStr += " WHERE UNIX_TIMESTAMP(createTime) < UNIX_TIMESTAMP('" + start + " 00:00:00') ";
        if (!StringUtils.isBlank(channelNames)) {
            sqlStr += " AND r.platformName in (" + channelNames + ")";
        }
        if (!blackUserStr.equals("")) {
            sqlStr += " AND t.userId NOT IN (" + blackUserStr + ")";
        }

        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlStr.toString());
        return mapList;
    }
}
