package com.game.commercialize.inter;

import com.game.player.structs.Player;

public interface IFCCharge extends ICommercialize {
    /**
     * 请求领取首充、续充奖励
     * @param player
     * @param cfgID
     */
    void onReqFCChargeReward(Player player, int cfgID);

    /**
     * 首充续充功能是否已经打开
     * @param player
     * @param function
     * @return
     */
    boolean checkFCChargeFunc(Player player, int function);
}
