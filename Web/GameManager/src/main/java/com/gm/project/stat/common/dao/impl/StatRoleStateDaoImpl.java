package com.gm.project.stat.common.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.common.dao.IStatRoleStateDao;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 角色相关查询
 */
@Service
public class StatRoleStateDaoImpl implements IStatRoleStateDao {



    public List<Map<String, Object>> getUserRegisterDataList(String channelNames, String selectServerIdList, String startDate, String endDate) {
        String table = "stat_role";
        StringBuilder str = new StringBuilder();
        str.append("SELECT t2.userId,t2.createTime");
        str.append(" FROM (SELECT userId,createTime,createsid FROM " + table + " AS t1 where 1=1");
        if (!StringUtils.isBlank(channelNames)) {
            str.append(" AND platformName in (" + channelNames + ")");
        }
        str.append(" AND createsid in (" + selectServerIdList+")");

        str.append(" GROUP BY userId,createTime HAVING createTime=( SELECT MIN(createTime)");
        str.append(" FROM " + table + " WHERE  createsid in ("+selectServerIdList+") and userId=t1.userId)) t2");
        str.append(" WHERE UNIX_TIMESTAMP(t2.createTime) BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00')  AND UNIX_TIMESTAMP('" + endDate + " 23:59:59')");
        str.append(" AND t2.createsid in (" + selectServerIdList+")");
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(str.toString());
        return mapList;
    }
    /**
     * 获取用户id列表
     * @param dbClientGM
     * @param caclStartDay
     * @param serverList
     * @return
     */
    public Set<String> getUserIdRegAddSet(DBClient dbClientGM, String caclStartDay, String serverList){
        // String userRegisterSql = this.getUserRegisterSql(null,"stat_role",serverList,caclStartDay,caclStartDay);
        //每日新增用户列表 排除之前登录过的
        List<Map<String,Object>> userRegAddList = this.getUserRegisterDataList(null,serverList,caclStartDay,caclStartDay);// dbClientGM.selectList(userRegisterSql);
        Set<String> userIdRegAddList = new HashSet<>();
        if(userRegAddList!=null){
            for (Map<String, Object> userRegAddMap : userRegAddList) {
                String userId = userRegAddMap.get("userId").toString();
                userIdRegAddList.add(userId);
            }
        }
        return userIdRegAddList;
    }


    /**
     * 新增设备
     * @param channelNames
     * @param selectServerIds
     * @param blackUserStr
     * @param startDate
     * @param endDate
     * @param isBlack
     * @return
     */
    public List<Map<String, Object>> getNewDeviceDataList( String channelNames, String selectServerIds, String blackUserStr, String startDate, String endDate,boolean isBlack) {
        //活跃设备SQL
        String table = "stat_role";//rolestate不用判断合服
        //新增设备SQL
        String  sqlDeviceStr = "select t2.machineCode,DATE_FORMAT(t2.createTime, '%Y-%m-%d') as day from (select machineCode,createTime from "+table+" as t1 where createsid in("+selectServerIds+")";
        if (isBlack) {
            sqlDeviceStr += " and userId not in(" + blackUserStr + ")";
        }
        if (!StringUtils.isBlank(channelNames)) {
            sqlDeviceStr += " and platformName in (" + channelNames + ")";
        }
        sqlDeviceStr += " group by machineCode,createTime having createTime=(select min(createTime) from "+table+" where machineCode=t1.machineCode )) t2 where t2.createTime between '"+startDate+"' AND '"+endDate+"' group by t2.machineCode,DATE_FORMAT(t2.createTime, '%Y-%m-%d')";
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlDeviceStr.toString());
        return mapList;
    }


}
