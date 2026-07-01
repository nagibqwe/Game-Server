package com.gm.project.stat.stat_role.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.stat_role.dao.IStatRoleDao;
import com.gm.project.stat.stat_role.domain.RoleInfoBean;
import com.gm.project.stat.stat_shop_item.domain.ShopItemBean;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StatRoleDaoImpl implements IStatRoleDao {
    public  List<RoleInfoBean> getRoleStateList(DBClient dbClient,String table, String channelNames, String sortType, int pageIndex, int pageSize) {
        String strSql = "select t2.platformName,t2.createsid,t2.funcellUUid,t2.userId,t2.roleId,t2.roleName,t2.`level`,t2.onlineTime,t2.rechargeGold,t2.gold,t2.createTime as createTimeStr,t2.lastLoginTime,t2.isDelete,t2.ts from " + table + " t2,(SELECT t0.userId,max(t0.ts) as maxtime FROM " + table + " t0 GROUP BY t0.userId) t1 where t2.userId=t1.userId and t2.ts=t1.maxtime ";
        if (!StringUtils.isBlank(channelNames)) {
            strSql += " and platformName in (" + channelNames + ")";
        }
        strSql += " order by t2." + sortType + " desc limit " + (pageIndex-1)*pageSize + "," + pageSize;

        if(dbClient == null){
            return null;
        }
        List<RoleInfoBean> mapList = dbClient.selectList(strSql, RoleInfoBean.class);
        return mapList;
    }
    public int getRoleStateCount(DBClient dbClient,String table, String channelNames) {
        String strSql = "select count(*) as counts from (select t2.platformName,t2.createsid,t2.funcellUUid,t2.userId,t2.roleId,t2.roleName,t2.`level`,t2.onlineTime,t2.rechargeGold,t2.gold,t2.createTime,t2.lastLoginTime,t2.isDelete,t2.ts from " + table + " t2,(SELECT t0.userId,max(t0.ts) as maxtime FROM " + table + " t0 GROUP BY t0.userId) t1 where t2.userId=t1.userId and t2.ts=t1.maxtime ";
        if (!StringUtils.isBlank(channelNames)) {
            strSql += " and platformName in (" + channelNames + ")";
        }
        strSql += ") t3";

        if(dbClient == null){
            return 0;
        }
        return  dbClient.qryTotalCount(strSql);
    }
}
