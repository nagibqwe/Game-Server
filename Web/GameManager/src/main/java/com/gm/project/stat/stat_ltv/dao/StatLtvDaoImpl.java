package com.gm.project.stat.stat_ltv.dao;

import com.gm.common.utils.DateUtils;
import com.gm.project.stat.common.dao.BaseDao;
import org.springframework.stereotype.Service;

/**
 *
 * 数据库操作相关
 * @author ruoyi
 */
@Service
public class StatLtvDaoImpl extends BaseDao
{


    /**
     * 获取某一个时间段 某部分用户 登录个数
     * @param serverList 服务器列表
     * @param userIdRegAddListStr 需要筛选的用户id
     * @return 返回 日期对应登录用户数 执行的sql
     */
    public String getUserStatLtvSql(String caclStartDay,String timeLoginBeinDay ,String timeLoginEndDay ,String serverList,String userIdRegAddListStr){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT SUM(b.totalFee) AS recharge_sum,count(distinct b.userId) as num,DATE_FORMAT(b.time,'%Y-%m-%d') AS charge_date FROM stat_role a");
        sql.append(" INNER JOIN stat_recharge b ON a.roleId = b.roleId and a.createTime <= b.time  WHERE b.statusReason = 5");
        sql.append(" AND b.sid IN ("+serverList+")");
        sql.append(" AND a.createTime between '" + caclStartDay + "' and '" + DateUtils.getNewDateForMinute2(caclStartDay,1440) + "' ");
        sql.append(" AND b.time BETWEEN '" + timeLoginBeinDay +"' AND '"+timeLoginEndDay+"' ");
        sql.append(" AND a.userId In (" + userIdRegAddListStr +")");
        sql.append(" GROUP BY charge_date");
        return sql.toString();
    }



}
