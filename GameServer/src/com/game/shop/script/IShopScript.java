package com.game.shop.script;

import com.game.db.bean.ShopBean;
import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.shopMessage.ReqBuyItem;
import game.message.shopMessage.ReqRefreshShop;
import game.message.shopMessage.ReqShopList;
import game.message.shopMessage.ReqShopSubList;

import java.util.List;

/**
 * @author Administrator
 */
public interface IShopScript extends IScript {
    /**
     * 返回所有商城数据
     * @return
     */
    List<ShopBean> allMalls();

    /**
     * 查询某个商品
     * @param sellId
     * @return
     */
    ShopBean mall(int sellId);

    /**
     * GM后台更新某商城数据
     * @param bean
     * @return
     */
    boolean updateShop(ShopBean bean);

    /**
     * GM后台删除某商城数据
     * @param sellId
     * @return
     */
    boolean deleteShop(int sellId);

    // 请求所有标签列表
    void onReqShopSubList(Player player, ReqShopSubList messInfo);

    // 请求商品列表
    void OnReqShopList(Player player, ReqShopList messInfo);

    // 购买物品
    void OnReqBuyItem(Player player, ReqBuyItem messInfo);

    // 刷新商城
    void OnReqRefreshShop(Player player, ReqRefreshShop messInfo);
}
