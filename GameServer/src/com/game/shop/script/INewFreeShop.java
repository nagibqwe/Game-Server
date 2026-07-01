package com.game.shop.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

public interface INewFreeShop extends IScript {

    /**
     * 上线初始化
     */
    void onlineInit(Player player);

    /**
     * 请求购买或者领奖
     */
    void  onReqFreeShop(Player player,int id,int type);

    /**
     * 在线刷新
     * @param player
     */
    void refresh(Player player);

    /**
     * 打开购买信息面板
     * @param player
     */
    void onReqOpenNewFreeShop(Player player);


}
