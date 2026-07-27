package com.gm.project.stat.stat_recharge_second.dao.impl;


import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.common.dao.BaseDao;
import com.gm.project.stat.stat_recharge_second.dao.IStatRechargeSecondDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 二次付费统计dao
 * 
 * @author gm
 * @date 2021-08-06
 */

@Service
public class StatRechargeSecondDaoImpl extends BaseDao implements IStatRechargeSecondDao
{
    private static final long serialVersionUID = 1L;

    /**
     * 获取首充的userId
     * @param table
     * @param serverId
     * @param channelNames
     * @param endDate
     * @param blackUserStr
     * @return
     */
    private String getFirstRechargeUserSql(String table, String serverId, String channelNames, String endDate, String blackUserStr) {
        String sqlStr = "SELECT s.userId";
        sqlStr += " FROM (SELECT userId,min(t.timesec) timesec,sid FROM (SELECT * FROM " + table + " WHERE status=1 AND statusReason=7 and totalFee>0 and src=0 ORDER BY timesec ASC) t";
        sqlStr += " GROUP BY t.userId) s";
        sqlStr += " WHERE s.timesec  <= UNIX_TIMESTAMP('" + endDate + " 23:59:59') AND s.sid IN (" + serverId + ")";
        if (!StringUtils.isBlank(blackUserStr)) {
            sqlStr += " AND s.userId NOT IN (" + blackUserStr + ")";
        }
        if (!StringUtils.isBlank(channelNames)) {
            sqlStr += " AND s.platformName IN (" + channelNames + ")";
        }
        return sqlStr;
    }


    public List<Map<String, Object>> getFirstAndSecondRechargeList(String table, String serverId, String channelNames, String startDate, String endDate, String blackUserStr) {
        //获取首充和二次充值的列表
        String sqlStr = "SELECT r1.userId, r1.timesec,r1.totalFee,r1.itemId,r1.goodsId";
        sqlStr += " FROM " + table + " r1";
        sqlStr += " WHERE r1.status=1 AND r1.statusReason AND 2 > ( ";
        sqlStr += " SELECT COUNT(*) FROM " + table + " r2 WHERE r2.userId=r1.userId AND r2.timesec < r1.timesec)";
        sqlStr += " AND r1.userId IN (" + getFirstRechargeUserSql(table, serverId, channelNames, endDate, blackUserStr) + ")";
        sqlStr += " AND r1.sid IN (" + serverId + ")";
        sqlStr += " ORDER BY r1.userId,r1.timesec ASC;";

        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlStr.toString());
        return mapList;
    }
}
