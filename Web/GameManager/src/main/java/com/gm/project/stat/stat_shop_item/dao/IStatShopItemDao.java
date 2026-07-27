package com.gm.project.stat.stat_shop_item.dao;

import com.gm.common.dbclient.DBClient;
import com.gm.project.stat.stat_shop_item.domain.ShopItemBean;

import java.util.List;


public interface IStatShopItemDao {
    public List<ShopItemBean> statShopItem(DBClient dbClient, String channelNames, List<String> tableList, int FromSrc, int moneyType, String blackUserStr, int serverId, long stime , long etime);
}
