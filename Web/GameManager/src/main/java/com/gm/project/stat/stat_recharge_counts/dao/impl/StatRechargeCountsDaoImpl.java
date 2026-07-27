package com.gm.project.stat.stat_recharge_counts.dao.impl;


import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.common.dao.BaseDao;
import com.gm.project.stat.stat_recharge_counts.dao.IStatRechargeCountsDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 充值统计dao
 * 
 * @author gm
 * @date 2021-08-06
 */

@Service
public class StatRechargeCountsDaoImpl extends BaseDao implements IStatRechargeCountsDao
{
    private static final long serialVersionUID = 1L;
    /**
     * 付费次数统计
     * @param table
     * @param serverId
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param blackUsers
     * @return
     */
    public List<Map<String, Object>> getRechargeCountsDataList(String table, String serverId, String channelNames, String startDate, String endDate, String blackUsers) {
        String sqlStr = " SELECT count(*) as counts,sum(totalFee) as amount,userId";
        sqlStr += " FROM " + table;
        sqlStr += " WHERE TIME between '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59' ";
        sqlStr += " AND status=1 AND statusReason=7 and totalFee>0 and src=0 AND sid in (" + serverId + ")";
        if (!StringUtils.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        if (!StringUtils.isBlank(blackUsers)) {
            sqlStr += " AND userId NOT IN (" + blackUsers + ")";
        }
        sqlStr += " GROUP BY userId";

        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlStr.toString());
        return mapList;
    }
}
