package com.game.couplefight.scripts;

import com.game.player.structs.Player;
import game.core.script.IScript;

public interface ICoupleShop extends IScript {


    /**
     * 上线初始化
     * @param player
     */
    void onLieInitShopData(Player player);


    /**
     * 购买商品
     * @param player
     */
    void onReqBuyCoupleItem(Player player,int id);


    /**
     * 打开仙女商城
     * @param player
     */
    void onReqOpenCoupleShop(Player player);



}
