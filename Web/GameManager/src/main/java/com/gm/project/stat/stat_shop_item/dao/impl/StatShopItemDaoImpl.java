package com.gm.project.stat.stat_shop_item.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.stat_shop_item.dao.IStatShopItemDao;
import com.gm.project.stat.stat_shop_item.domain.ShopItemBean;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class StatShopItemDaoImpl implements IStatShopItemDao {

    public  List<ShopItemBean> statShopItem(DBClient dbClient,String channelNames, List<String> tableList, int FromSrc, int moneyType, String blackUserStr, int serverId, long stime , long etime){
        StringBuilder sqlStr = new StringBuilder("select itemModelId,moneyType,count(distinct(userId)) as users,count(distinct(roleId)) as roles,sum(realNum) as totalnum ,sum(moneyNum) as totalgold,sum(buyTimes) as totaltimes from (");
        //聚合查询所以表数据汇总到一起
        StringBuilder shopbuylogSql = new StringBuilder("select itemModelId,moneyType,userId,roleId,realnum,moneynum,buyTimes from ");
        StringBuilder allShopbuylogSql = new StringBuilder("");
        for  (String table : tableList) {
            allShopbuylogSql.append(shopbuylogSql);
            allShopbuylogSql.append(table);
            allShopbuylogSql.append(" where sid="+serverId);
            allShopbuylogSql.append(" and time>="+stime+" and time<="+etime);
            if (!StringUtils.isBlank(channelNames)) {
                allShopbuylogSql.append(" and platformName in (" + channelNames + ")");
            }
            if(moneyType != 0){
                allShopbuylogSql.append(" and moneyType="+moneyType);
            }

            if(FromSrc != 0){
                allShopbuylogSql.append(" and FromSrc="+FromSrc);
            }

            if (!StringUtils.isBlank(blackUserStr)) {
                allShopbuylogSql.append(" and t.userId NOT IN (" + blackUserStr + ")");
            }
            allShopbuylogSql.append(" union all ");
        }
        //去除最后拼接的  union all
        allShopbuylogSql.delete(allShopbuylogSql.length() - 11, allShopbuylogSql.length());
        sqlStr.append(allShopbuylogSql.toString()).append(") t");
        sqlStr.append(" group by itemModelId,moneyType");
        if(dbClient == null){
            return null;
        }
        List<ShopItemBean> mapList = dbClient.selectList(sqlStr.toString(), ShopItemBean.class);
        return mapList;
    }

}
