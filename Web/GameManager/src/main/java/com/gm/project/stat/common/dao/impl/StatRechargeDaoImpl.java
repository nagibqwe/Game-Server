package com.gm.project.stat.common.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.common.dao.IStatRechargeDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 充值相关数据库 通用操作
 */
@Service
public class StatRechargeDaoImpl implements IStatRechargeDao {

    /**
     * 得到充值充值数据
     * @param channelNames
     * @param selectServerIds
     * @param blackUserStr
     * @param stime
     * @param etime
     * @param isBlack
     * @return
     */
    public List<Map<String,Object>> getRechargeMapDataList(String channelNames, String selectServerIds, String blackUserStr, long stime, long etime, boolean isBlack){
        String table = "stat_recharge";
        String sqlStr = "SELECT userId,sum(totalFee) as totalmoney,sum(gameMoney) as totalgold,DATE_FORMAT(from_unixtime(timesec), '%Y-%m-%d') as day,count(distinct(orderNo)) as totaltimes from "+table+"";
        sqlStr += " where sid in (" + selectServerIds + ") and status=1 and statusReason=7 and totalFee>0 and src=0 and timesec>="+stime+" and timesec<="+etime+"";

        if (!StringUtils.isBlank(channelNames)) {
            sqlStr += " and platformName in (" + channelNames + ")";
        }
        if (isBlack) {
            sqlStr += " and userId not in(" + blackUserStr + ")";
        }
        sqlStr += " group by userId,day";
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlStr);
        return mapList;
    }
}
