package com.game.kaoshangling.script;

import com.game.player.structs.Player;

/**
 * 犒赏令脚本
 */
public interface IKaoShangLingScript {
    /**
     * 玩家上线
     * @param player
     */
    void playerOnline(Player player);

    /**
     * 领取奖励
     * @param player
     * @param isOneKey
     * @param key
     */
    void reqKaoShangLingRewardHandler(Player player,int type,int isOneKey,int key,int isSendMail);

    /**
     * 请求刷新犒赏令轮次
     * @param player
     */
    void reqKaoShangLingRefreshRankHandler(Player player,int type);

    /**
     * 打开面板请求
     * @param player
     */
    void reqOpenKaoShangLingPanelHandler(Player player);

    /**
     * 购买高级荒古令
     * @param player
     */
    void reqBuySpecailKaoShangLing(Player player,int type);
}
