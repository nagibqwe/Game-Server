package com.gm.project.stat.common.dao;

import com.gm.common.dbclient.DBClient;
import com.gm.common.utils.StringUtils;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseDao {

    /**
     * 获取
     * @param channelNames
     * @param table
     * @param serverList
     * @param serverList
     * @param startDate
     * @param endDate
     * @return
     */
    public String getUserRegisterSql(String channelNames, String table, String serverList, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t2.userId");
        sql.append(" FROM (SELECT userId,createTime,createsid FROM " + table + " AS t1");
        if (!StringUtils.isEmpty(channelNames)) {
            //sql.append(" where platformName in (" + channelNames + ")");
        }
        sql.append(" where createsid IN ( "+serverList+")");
        sql.append(" GROUP BY userId,createTime HAVING createTime=( SELECT MIN(createTime)");
        sql.append(" FROM " + table + " WHERE createsid IN ("+ serverList+") and userId=t1.userId)) t2");//@todo汇总数据库 排除其他服务器的影响 （以前单个不影响）
        sql.append(" WHERE UNIX_TIMESTAMP(t2.createTime) BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00')  AND UNIX_TIMESTAMP('" + endDate + " 23:59:59')");
        sql.append(" AND t2.createsid IN ( "+serverList+")");
        return sql.toString();
    }


}
