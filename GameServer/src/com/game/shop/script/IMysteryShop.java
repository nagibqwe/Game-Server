package com.game.shop.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * Created by 542 on 2020/5/26.
 */
public interface IMysteryShop  extends IScript {


    /**
     * 购买限购商品
     * @param player
     * @param id
     */
    void onReqMysteryShopBuy(Player player, int id);

    /**
     * 刷新商品
     * @param player
     */
    void refresh(Player player);

    /**
     * online
     * @param player
     */
    void online(Player player);
}
