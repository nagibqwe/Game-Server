package com.gm.project.stat.stat_recharge_distribute.dao.impl;


import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.common.dao.BaseDao;
import com.gm.project.stat.stat_recharge_distribute.dao.IStatRechargeDistributeDao;
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
public class StatRechargeDistributeDaoImpl  implements IStatRechargeDistributeDao
{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public List<Map<String, Object>> payLevelStat(String selectServerIds,String channelNames,String startDate, String endDate){
        StringBuilder str = new StringBuilder();
        str.append("SELECT rcl.totalFee as totalFee, count(rcl.totalFee) as count FROM " + "stat_recharge" + " rcl ");
        str.append("WHERE  rcl.time BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59' AND sid in (" + selectServerIds + ")");
        if (!StringUtils.isBlank(channelNames)) {
            str.append(" and rcl.platformName in (" + channelNames + ") ");
        }

        str.append(" and rcl.totalFee>0 and rcl.status=1 AND rcl.statusReason=7 and rcl.src = 0");

        str.append(" GROUP BY rcl.totalFee ");
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(str.toString());
        return mapList;
    }

    public List<Map<String, Object>> payDaylStat(String selectServerIds,String channelNames,String startDate){
        StringBuilder str = new StringBuilder();
        str.append("SELECT rcl.totalFee,HOUR(rcl.time) timeHour,rcl.userId FROM " + "stat_recharge"  + " rcl ");
        str.append("WHERE rcl.time BETWEEN '" + startDate + " 00:00:00' AND '" + startDate + " 23:59:59' AND sid in (" + selectServerIds + ")");
        if (!StringUtils.isBlank(channelNames)) {
            str.append(" and rcl.platformName in (" + channelNames + ") ");
        }

        str.append(" and rcl.totalFee>0 and rcl.status=1 AND rcl.statusReason=7 and rcl.src = 0");

        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(str.toString());
        return mapList;
    }

    @Override
    public List<Map<String, Object>> payGoodIdslStat(String selectServerIds, String channelNames, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT rcl.goodsId as goodsId, count(rcl.goodsId) as count FROM " + "stat_recharge" + " rcl ");
        str.append("WHERE  rcl.time BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59' AND sid in (" + selectServerIds + ")");
        if (!StringUtils.isBlank(channelNames)) {
            str.append(" and rcl.platformName in (" + channelNames + ") ");
        }

        str.append(" and rcl.totalFee>0 and rcl.status=1 AND rcl.statusReason=7 and rcl.src = 0");

        str.append(" GROUP BY rcl.goodsId ");
        DBClient dbClientSTAT = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientSTAT == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientSTAT.selectList(str.toString());
        return mapList;
    }
    @Override
    public List<Map<String, Object>> payCountStat(String selectServerIds, String channelNames, String startDate, String endDate) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT rcl.roleId as roleId, count(rcl.roleId) as count FROM " + "stat_recharge" + " rcl ");
        str.append("WHERE  rcl.time BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59' AND sid in (" + selectServerIds + ")");
        if (!StringUtils.isBlank(channelNames)) {
            str.append(" and rcl.platformName in (" + channelNames + ") ");
        }

        str.append(" and rcl.totalFee>0 and rcl.status=1 AND rcl.statusReason=7 and rcl.src = 0");

        str.append(" GROUP BY rcl.roleId ");
        DBClient dbClientSTAT = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientSTAT == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientSTAT.selectList(str.toString());
        return mapList;
    }



}
