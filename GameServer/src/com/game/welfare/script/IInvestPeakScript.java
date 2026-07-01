package com.game.welfare.script;

import com.game.player.structs.Player;

public interface IInvestPeakScript extends IWelfareScript {
    /**
     * 请求购买成长基金
     * @param player
     * @param gear
     */
    void onReqInvestPeakBuy(Player player, int gear);

    /**
     * 请求领取成长基金奖励
     * @param player
     * @param cfgID
     */
    void onReqInvestPeakGetAward(Player player, int cfgID);

    /**
     * 请求领取成长基金全服奖励
     * @param player
     * @param cfgID
     */
    void onReqInvestPeakServer(Player player, int cfgID);

    /**
     * 添加全服购买次数
     */
    void add();
}
