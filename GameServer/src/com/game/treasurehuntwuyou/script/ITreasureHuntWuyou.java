package com.game.treasurehuntwuyou.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * 无忧宝库脚本
 */
public interface ITreasureHuntWuyou extends IScript {

    /**
     * 玩家上线处理
     * @param player
     */
    void online(Player player);

    /**
     * 请求打开主界面
     * @param player
     */
    void reqOpenPanel(Player player);

    /**
     * 请求获得抽奖道具
     * @param player
     */
    void reqGetItem(Player player);

    /**
     * 请求抽奖
     * @param player
     * @param num
     */
    void reqHunt(Player player, int num);

    /**
     * 请求提取仓库中的物品
     * @param player
     */
    void reqExtract(Player player, long uid);
}
