package com.gm.project.stat.stat_shop_item.service;

import com.gm.project.stat.stat_shop_item.domain.ShopItemBean;

import java.util.List;

public interface IStatShopItemService {
    /**
     * 商城购买统计
     * @param groupName
     * @param selectServerId
     * @param channelNames
     * @param FromSrc
     * @param moneyType
     * @param startDate
     * @param endDate
     * @param isBlack
     * @return
     */
    public List<ShopItemBean> statShopItem(String groupName, Integer selectServerId, String channelNames, Integer FromSrc, Integer moneyType, String startDate, String endDate, Boolean isBlack);
}
