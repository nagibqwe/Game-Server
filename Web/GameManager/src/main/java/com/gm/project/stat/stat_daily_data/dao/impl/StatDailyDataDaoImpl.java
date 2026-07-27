package com.gm.project.stat.stat_daily_data.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.stat_daily_data.dao.IStatDailyDataDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *  日常统计 数据查询
 * 每日登陆过游戏的用户数
 */
@Service
public class StatDailyDataDaoImpl implements IStatDailyDataDao {


    public List<Map<String, Object>> getOnlineList(String selectServerIdList,String startDate,String endDate){
        String table = "t_servernum";
        String sqlStr = "select max(num) as maxnum,avg(num) as avgnum,DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') as day from "+table+" where serverId in("+selectServerIdList+") and DATE_FORMAT(from_unixtime(time), '%Y-%m-%d') BETWEEN '"+startDate+"' AND '"+endDate+"' ";
        sqlStr += " group by DATE_FORMAT(from_unixtime(time), '%Y-%m-%d')";
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.GM);
        if(dbClientGM == null){
            return null;
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sqlStr);
        return mapList;
    }







}
