package com.game.welfare.script;

import com.game.player.structs.Player;

public interface IGrowthFundScript extends IWelfareScript {
    /**
     * 请求购买成长基金
     * @param player
     * @param gear
     */
    void onReqGrowthFundBuy(Player player, int gear);

    /**
     * 请求领取成长基金奖励
     * @param player
     * @param cfgID
     */
    void onReqGrowthGetAward(Player player, int cfgID);

    /**
     * 请求领取成长基金全服奖励
     * @param player
     * @param cfgID
     */
    void onReqGrowthFundServer(Player player, int cfgID);

    /**
     * 添加全服购买次数
     */
    void add();
}
