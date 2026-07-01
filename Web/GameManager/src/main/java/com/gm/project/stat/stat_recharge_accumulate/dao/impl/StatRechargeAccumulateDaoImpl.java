package com.gm.project.stat.stat_recharge_accumulate.dao.impl;


import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.common.dao.BaseDao;
import com.gm.project.stat.stat_recharge_accumulate.dao.IStatRechargeAccumulateDao;
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
public class StatRechargeAccumulateDaoImpl extends BaseDao implements IStatRechargeAccumulateDao
{
    private static final long serialVersionUID = 1L;

    /**
     * 获取累计充值数据
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param blackUsers
     * @return
     */
    public List<Map<String, Object>> getRechargeAccumulateList(String selectServerIds, String channelNames, String startDate, String endDate, String blackUsers) {
        String sqlStr = "SELECT sum(totalFee) as amount,userId";
        sqlStr += " FROM " + "stat_recharge";
        sqlStr += " WHERE TIME between '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59' ";
        sqlStr += " AND status=1 AND statusReason=7  and totalFee>0 and src=0 AND sid IN (" + selectServerIds + ")";
        if (!StringUtils.isBlank(channelNames)) {
            sqlStr += " AND platformName IN (" + channelNames + ")";
        }
        if (!blackUsers.equals("")) {
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
