package com.gm.project.stat.stat_remain.dao;

import com.gm.common.dbclient.DBClient;
import com.gm.common.utils.DateUtils;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.common.dao.BaseDao;
import com.gm.project.stat.stat_daily_data.dao.IStatDailyDataDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * 数据库操作相关
 * @author ruoyi
 */
@Service
public class StatRemainDaoImpl extends BaseDao
{
    /**
     * 获取某一个时间段 某部分用户 登录个数
     * @param serverList 服务器列表
     * @param userIdRegAddListStr 需要筛选的用户id
     * @return 返回 日期对应登录用户数 执行的sql
     */
//    public String getUserStatRemainSqlSql(String caclStartDay,String timeLoginBeinDay ,String timeLoginEndDay ,String serverList,String userIdRegAddListStr){
//        StringBuilder statRemainSql = new StringBuilder("SELECT COUNT(DISTINCT B.userId) AS count,DATE_FORMAT(B.timeLogin,'%Y-%m-%d') AS date FROM stat_role A");
//        statRemainSql.append(" INNER JOIN stat_login as B on A.userId = B.userId AND A.createsid = B.serverId");
//        statRemainSql.append(" WHERE A.createTime between '" + caclStartDay + "' and '" + DateUtils.getNewDateForMinute2(caclStartDay,1440) + "' ");
//        statRemainSql.append(" AND A.createsid IN ( "+serverList+")");
//
//        // if(!StringUtils.isEmpty(userIdRegAddListStr)){
//        statRemainSql.append(" AND A.userId IN ( "+userIdRegAddListStr+")");
//        //}
//
//        statRemainSql.append(" AND B.timeLogin BETWEEN '" + timeLoginBeinDay +"' AND '"+timeLoginEndDay+"' ");
//        statRemainSql.append("GROUP BY  date");
//        return statRemainSql.toString();
//    }

    public String getUserStatRemainSqlSql(String caclStartDay,String timeLoginBeinDay ,String timeLoginEndDay ,String serverList,String userIdRegAddListStr){
        StringBuilder statRemainSql = new StringBuilder("SELECT COUNT(DISTINCT A.userId) AS count,DATE_FORMAT(A.timeLogin,'%Y-%m-%d') AS date FROM stat_login A");
        // statRemainSql.append(" INNER JOIN stat_login as B on A.userId = B.userId AND A.createsid = B.serverId");
        //  statRemainSql.append(" WHERE A.createTime between '" + caclStartDay + "' and '" + DateUtils.getNewDateForMinute2(caclStartDay,1440) + "' ");
        // if(!StringUtils.isEmpty(userIdRegAddListStr)){

        //}
        statRemainSql.append(" WHERE A.serverId IN ( "+serverList+")");
        statRemainSql.append(" AND A.timeLogin BETWEEN '" + timeLoginBeinDay +"' AND '"+timeLoginEndDay+"'");

        statRemainSql.append(" AND A.userId IN ( "+userIdRegAddListStr+")");

        statRemainSql.append(" GROUP BY  date");
        return statRemainSql.toString();
    }
    /**
     * 获取某一个时间段 某部分用户 登录个数
     * @param caclStartTime 需要计算的某一天
     * @param serverList 服务器列表

     * @return 返回 日期对应登录用户数 执行的sql
     */
    public String getNewRoleStatRemainSqlSql(String caclStartTime,String timeLoginBeinDay ,String timeLoginEndDay ,String serverList){
        StringBuilder statRemainSql = new StringBuilder("SELECT COUNT(DISTINCT B.roleId) AS count,DATE_FORMAT(B.timeLogin,'%Y-%m-%d') AS date FROM stat_role A");
        statRemainSql.append(" INNER JOIN stat_login as B on A.roleId = B.roleId AND A.createsid = B.serverId");
        statRemainSql.append(" WHERE A.createTime between '" + caclStartTime + "' and '" + DateUtils.getNewDateForMinute2(caclStartTime,1440) + "' ");
        statRemainSql.append(" AND A.createsid IN ( "+serverList+")");
        statRemainSql.append(" AND B.timeLogin BETWEEN '" + timeLoginBeinDay +"' AND '"+timeLoginEndDay+"' ");
        //按照时间排序
        statRemainSql.append("GROUP BY  date");
        return statRemainSql.toString();
    }
    /**
     * 获取
     * @param channelNames
     * @param serverList
     * @param serverList
     * @param startDate
     * @param endDate
     * @return
     */
    public String getNewRoleSql(String channelNames, String serverList, String startDate, String endDate) {
        StringBuilder newRoleSql = new StringBuilder();
        newRoleSql.append("SELECT COUNT(*) AS count FROM `stat_role`");
        newRoleSql.append(" WHERE `createsid` IN ("+serverList+")");
        newRoleSql.append(" AND UNIX_TIMESTAMP(createTime) BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00')  AND UNIX_TIMESTAMP('" + endDate + " 23:59:59')");
        newRoleSql.append(" LIMIT 1");
        return newRoleSql.toString();
    }
//    private String getRechargeUserSql(String channelNames, String table, String serverIdList, String firstLogUserIdStr) {
//        StringBuilder str = new StringBuilder();
//        str.append("select userId from  " + table +" where 1 = 1");
//        str.append(" and userId in ('" + firstLogUserIdStr + "')");
//        str.append(" and sid in ('" + serverIdList + "')");
//        if (!StringUtils.isBlank(channelNames)) {
//            str.append(" and platformName in (" + channelNames + ")");
//        }
//        str.append(" and status = 1 and statusReason = 7 group by userId ");
//        return str.toString();
//    }

    private String getRechargeUserSql(String channelNames, String table, String serverId, String firstLogUserIdStr) {
        StringBuilder str = new StringBuilder();
        str.append("select userId from  " + table);
        str.append(" WHERE userId IN (" + firstLogUserIdStr + ") and sid in (" + serverId + ")");
        if (!StringUtils.isBlank(channelNames)) {
            str.append(" and platformName in (" + channelNames + ")");
        }
        str.append(" and status = 1 and statusReason = 7  and totalFee>0 group by userId");
        return str.toString();
    }

    @Autowired
    public IStatDailyDataDao statDailyDataDao;


    /**
     * 获取新用户 充值
     * @param dbClientGM
     * @param caclStartDay
     * @param serverList
     * @param userIdRegAddList
     * @return
     */
    public  Set<String> gePayNewUserIdRegAddSet(DBClient dbClientGM,String caclStartDay, String serverList,Set<String> userIdRegAddList){
        String userIdRegAddListStr = StringUtils.join(userIdRegAddList,",");
        String userPaySqlStr = this.getRechargeUserSql(null, "stat_recharge", serverList, userIdRegAddListStr);
        List<Map<String,Object>>   userPayAddList = dbClientGM.selectList(userPaySqlStr);

        Set<String> newUserPayAddList = new HashSet<>();
        if(userPayAddList!=null){
            for (Map<String, Object> userRegAddMap : userPayAddList) {
                String userId = userRegAddMap.get("userId").toString();
                newUserPayAddList.add(userId);
            }
        }
        return newUserPayAddList;
    }

    /**
     *  获取老用户sql
     * @param channelNames
     * @param table
     * @param serverList
     * @param startDate
     * @param endDate
     * @return
     */
    public String getUserOldRegisterSql(String channelNames, String table, String serverList, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t2.userId as oldUserId");
        sql.append(" FROM (SELECT userId,createTime,createsid FROM " + table + " AS t1");
        if (!StringUtils.isEmpty(channelNames)) {
            //sql.append(" where platformName in (" + channelNames + ")");
        }
        sql.append(" where createsid IN ( "+serverList+")");
        sql.append(" GROUP BY userId,createTime HAVING createTime!=( SELECT MIN(createTime)");
        sql.append(" FROM " + table + " WHERE createsid IN ("+ serverList+") and userId=t1.userId)) t2");//@todo汇总数据库 排除其他服务器的影响 （以前单个不影响）
        sql.append(" WHERE UNIX_TIMESTAMP(t2.createTime) BETWEEN UNIX_TIMESTAMP('" + startDate + " 00:00:00')  AND UNIX_TIMESTAMP('" + endDate + " 23:59:59')");
        sql.append(" AND t2.createsid IN ( "+serverList+")");
        return sql.toString();
    }
    /**
     * 获取老用户
     * @param dbClientGM
     * @param caclStartDay
     * @param serverList
     * @return
     */
    public Set<String> getOldUserIdRegAddSet(DBClient dbClientGM,String caclStartDay, String serverList){
        String oldUserRegisterSql = this.getUserOldRegisterSql(null,"stat_role",serverList,caclStartDay,caclStartDay);
        //每日新增用户列表 排除之前登录过的
        List<Map<String,Object>>   oldUserAddList = dbClientGM.selectList(oldUserRegisterSql);

        //新增角色id列表
        Set<String> oldUserIdRegAddList = new HashSet<>();
        if(oldUserAddList!=null){
            for (Map<String, Object> userRegAddMap : oldUserAddList) {
                String userId = userRegAddMap.get("oldUserId").toString();
                oldUserIdRegAddList.add(userId);
            }
        }
        return oldUserIdRegAddList;
    }

    /**
     * 获取角色数量
     * @param dbClientGM
     * @param caclStartDay
     * @param serverList
     * @return
     */
    public int getNewRoleNum(DBClient dbClientGM,String caclStartDay, String serverList){
        String newRoleSql = this.getNewRoleSql(null,serverList,caclStartDay,caclStartDay);
        List<Map<String,Object>>  newRoleList = dbClientGM.selectList(newRoleSql);

        int caclNewCount = 0;
        if(newRoleList!=null && newRoleList.size()>0){
            caclNewCount = Integer.parseInt(newRoleList.get(0).get("count").toString());
        }
        return caclNewCount;
    }

}
