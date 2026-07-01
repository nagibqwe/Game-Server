package com.gm.project.stat.common.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.common.dao.IStatLoginDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class StatLoginDaoImpl implements IStatLoginDao {

    /**
     * 活跃玩家

     * @param channelNames
     * @param selectServerIds
     * @param blackUserStr
     * @param stime
     * @param etime
     * @param isBlack
     * @return
     */
    public List<Map<String, Object>> getActiveUserDataList(String channelNames, String selectServerIds, String blackUserStr, long stime, long etime, boolean isBlack) {
        String table = "stat_login";
        String sqlStr = "select userId,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day from "+table;
        sqlStr+= " where sid in("+selectServerIds+") and time>="+stime+" and time<="+etime+"";
        if (isBlack) {
            sqlStr += " and userId not in(" + blackUserStr + ")";
        }
        if (!StringUtils.isBlank(channelNames)) {
            sqlStr += " and platformName in (" + channelNames + ")";
        }
        sqlStr += " group by userId,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')";

        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlStr);
        return mapList;
    }



    /**
     * 活跃设备

     * @param channelNames
     * @param selectServerIds
     * @param blackUserStr

     * @param isBlack
     * @return
     */
    public List<Map<String, Object>> getActiveDeviceDataList( String channelNames, String selectServerIds, String blackUserStr,long stime, long etime,boolean isBlack) {

        String table = "stat_login";
        //活跃设备SQL
        String sqlDeviceStr = "select machineCode,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day from "+table+" where sid in("+selectServerIds+") and time>="+stime+" and time<="+etime+"";
        if (isBlack) {
            sqlDeviceStr += " and userId not in(" + blackUserStr + ")";
        }
        if (!StringUtils.isBlank(channelNames)) {
            sqlDeviceStr += " and platformName in (" + channelNames + ")";
        }
        sqlDeviceStr += " group by machineCode,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')";
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlDeviceStr.toString());
        return mapList;
    }
}
