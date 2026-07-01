package com.gm.project.stat.stat_churn_rate.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.stat_churn_rate.dao.IStatChurnRateDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatChurnRateDaoImpl implements IStatChurnRateDao {

    /**
     * 获取选择时间内所有登录用户
     * @param dbClient
     * @param channelNames
     * @param table
     * @param startDate
     * @param endDate
     * @return
     */
    public  List<Map<String, Object>> getAllUserDataList(DBClient dbClient, String channelNames, String table, String startDate, String endDate){
        StringBuilder str = new StringBuilder();
        str.append("SELECT userId, DATE_FORMAT(FROM_UNIXTIME(time),'%Y-%m-%d') date");
        str.append(" FROM " + table);
        str.append(" where time between UNIX_TIMESTAMP('" + startDate + " 00:00:00') AND UNIX_TIMESTAMP('" + endDate + " 23:59:59') ");
        if (!StringUtils.isBlank(channelNames)) {
            str.append(" and platformName in (" + channelNames + ")");
        }
        str.append(" GROUP BY userId,date");


        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> allUserDataList = dbClientGM.selectList(str.toString());
        return allUserDataList;
    }


    public List<Map<String, Object>> getRechargeDataMap(DBClient dbClient, String channelNames,  String selectServerIdList,String table,String minPay, String maxPay,List<Map<String, Object>> allUserDataList){
        StringBuilder str = new StringBuilder();
        str.append("SELECT userId ");
        str.append(" FROM " + table);
        str.append(" where status = 1 and statusReason = 7");
        str.append(" and sid in (" + selectServerIdList + ")");
        if (minPay != null && !maxPay.equals("") && !maxPay.equals("") && maxPay != null) {
            str.append(" and (totalFee between  " + minPay + " and " + maxPay + ")");
        }
        String userId = "";
        if (allUserDataList != null) {
            for (Map<String, Object> map : allUserDataList) {
                userId += map.get("userId").toString() + ",";
            }
            if (userId != null && !"".equals(userId)) {
                userId = userId.substring(0, userId.length() - 1);
                str.append(" and userId in (" + userId + ")");
            }
        }
        if (!StringUtils.isBlank(channelNames)) {
            str.append(" and platformName in (" + channelNames + ")");
        }
        str.append(" GROUP BY userId");
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> rechargeDataMap = dbClientGM.selectList(str.toString());
        return rechargeDataMap;
    }
    /**
     * 获取用户累充金额
     */
    public List<Map<String, Object>> getAmountDataList(DBClient dbClient,String table, String channelNames, String vipUserId, int amountSum) {
        StringBuilder str = new StringBuilder();
        str.append("select sum(t.totalFee) as amount,count(t.userId) as counts");
        str.append(" FROM (" + "select userId,sum(totalFee) as totalFee from " + table);
        if (vipUserId != null && !"".equals(vipUserId)) {
            str.append(" where status = 1 and statusReason = 7 and userId in (" + vipUserId + ") group by userId) t");
        } else {
            str.append(" where status = 1 and statusReason = 7 and userId=0 group by userId) t");
        }
        if (amountSum == 0) {
            str.append(" where t.totalFee>0 and t.totalFee<=10");
        } else if (amountSum == 10) {
            str.append(" where t.totalFee>10 and t.totalFee<=20");
        } else if (amountSum == 20) {
            str.append(" where t.totalFee>20 and t.totalFee<=30");
        } else if (amountSum == 30) {
            str.append(" where t.totalFee>30 and t.totalFee<=70");
        } else if (amountSum == 70) {
            str.append(" where t.totalFee>70 and t.totalFee<=150");
        } else if (amountSum == 150) {
            str.append(" where t.totalFee>150 and t.totalFee<=300");
        } else if (amountSum == 300) {
            str.append(" where t.totalFee>300 and t.totalFee<=500");
        } else if (amountSum == 500) {
            str.append(" where t.totalFee>500 and t.totalFee<=1000");
        } else if (amountSum == 1000) {
            str.append(" where t.totalFee>1000 and t.totalFee<=1500");
        } else if (amountSum == 1500) {
            str.append(" where t.totalFee>1500 and t.totalFee<=2000");
        } else if (amountSum == 2000) {
            str.append(" where t.totalFee>2000 and t.totalFee<=5000");
        } else if (amountSum == 5000) {
            str.append(" where t.totalFee>5000 and t.totalFee<=10000");
        } else if (amountSum == 10000) {
            str.append(" where t.totalFee>=10000 ");
        }
        if (!StringUtils.isBlank(channelNames)) {
            str.append(" and t.platformName in (" + channelNames + ")");
        }
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> dataList = dbClientGM.selectList(str.toString());
        return dataList;
    }



    /**
     * 获取用户等级的SQL
     */
    public List<Map<String, Object>> getUserLevelDataList(DBClient dbClient,String serverIdList, String channelNames, String users) {
        StringBuilder str = new StringBuilder();
        str.append("select userId,max(level) as level");
        str.append(" FROM stat_role");
        str.append(" where userId in (" + users + ")");
        str.append(" and createsid in (" + serverIdList + ")");
        if (!StringUtils.isBlank(channelNames)) {
            str.append(" and platformName in (" + channelNames + ")");
        }
        str.append(" group by userId");
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> dataList = dbClientGM.selectList(str.toString());
        return dataList;
    }
}
