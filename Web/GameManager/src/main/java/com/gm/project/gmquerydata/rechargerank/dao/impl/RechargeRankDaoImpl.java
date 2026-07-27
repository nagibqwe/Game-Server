package com.gm.project.gmquerydata.rechargerank.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.common.utils.StatUtil;
import com.gm.project.gmquerydata.rechargerank.dao.IRechargeRankDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *  充值排行榜
 *
 */
@Service
public class RechargeRankDaoImpl implements IRechargeRankDao {
    //分组查询记录总数的sql前缀
    private static String prefixSql = "select count(*) from (";
    //分组查询记录总数的sql后缀
    private static String suffixSql =")temp";
    public List<Map<String, Object>> rechargeRankByRoleIdDataList(long start, long end, String channelNames,String selectServerIds,Map<String,Object> param) {
//        String table = "stat_recharge";
//        String sql = "select  roleId,  totalFee, re.timesec from " +
//                table + " where re.totalFee > 10 AND re.status=1 AND re.statusReason=7 and re.src=0 and re.timesec >= " + start + " and re.timesec <= " + end;
//        StringBuilder builder = new StringBuilder(sql);
//        if (channelNames != null){
//            if (!StringUtils.isBlank(channelNames)) {
//                sql += " AND r.platformName in (" + channelNames + ")";
//            }
//        }
//        sql += " AND  re.sid IN (" + selectServerIds + ")";

        String sql = "SELECT MAX(time) as time, roleId,userId,sid,count(roleId) as rechargeCount,SUM(totalFee) as totalRecharge,MIN(totalFee) as  minRecharge  ,MAX(totalFee) as maxRecharge  ,AVG(totalFee) as avgRecharge FROM stat_recharge";
        String where = " where 1=1 AND  sid IN (" + selectServerIds + ")";
        where += " and totalFee>0 and status=1 AND statusReason=7";

        if(!StatUtil.isParam(param,"isStatTestOrder")){
            where += " and src = 0";
        }

        where += " and timesec >= " + start + " and timesec <= " + end;



        where += " GROUP BY roleId";


        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }

        int count = dbClientGM.qryTotalCount(prefixSql+ sql+where+suffixSql);
        param.put("count",count);
        where += " ORDER BY totalRecharge DESC";
        //分页数据
        if(param.containsKey("pageNum")){
            int pageNum = (int)param.get("pageNum");
            int pageSize = (int)param.get("pageSize");
            where += " limit "+(pageNum-1)*pageSize+","+pageSize+"";
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sql+where);
        return mapList;
    }


    public List<Map<String, Object>> rechargeRankByUserIdDataList(long start, long end, String channelNames,String selectServerIds,Map<String,Object> param) {
//        String table = "stat_recharge";
//        String sql = "select  roleId,  totalFee, re.timesec from " +
//                table + " where re.totalFee > 10 AND re.status=1 AND re.statusReason=7 and re.src=0 and re.timesec >= " + start + " and re.timesec <= " + end;
//        StringBuilder builder = new StringBuilder(sql);
//        if (channelNames != null){
//            if (!StringUtils.isBlank(channelNames)) {
//                sql += " AND r.platformName in (" + channelNames + ")";
//            }
//        }
//        sql += " AND  re.sid IN (" + selectServerIds + ")";

        String sql = "SELECT MAX(time) as time, MAX(roleId) as roleId,userId,sid,count(roleId) as rechargeCount,SUM(totalFee) as totalRecharge,MIN(totalFee) as  minRecharge  ,MAX(totalFee) as maxRecharge  ,AVG(totalFee) as avgRecharge FROM stat_recharge";
        String where = " where 1=1 AND  sid IN (" + selectServerIds + ")";
        where += " and totalFee>0 and status=1 AND statusReason=7";

        if(!StatUtil.isParam(param,"isStatTestOrder")){
            where += " and src = 0";
        }

        where += " and timesec >= " + start + " and timesec <= " + end;
        where += " GROUP BY userId";


        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientGM == null){
            return null;
        }

        int count = dbClientGM.qryTotalCount(prefixSql+ sql+where+suffixSql);
        param.put("count",count);
        where += " ORDER BY totalRecharge DESC";
        //分页数据
        if(param.containsKey("pageNum")){
            int pageNum = (int)param.get("pageNum");
            int pageSize = (int)param.get("pageSize");
            where += " limit "+(pageNum-1)*pageSize+","+pageSize+"";
        }
        List<Map<String, Object>> mapList = dbClientGM.selectList(sql+where);
        return mapList;
    }

}
