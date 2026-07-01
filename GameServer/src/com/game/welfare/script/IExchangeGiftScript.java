package com.game.welfare.script;

import com.game.player.structs.Player;

public interface IExchangeGiftScript extends IWelfareScript {
    /**
     * 兑换礼包
     * @param player
     * @param strID
     */
    void onReqExchangeGift(Player player, String strID);
}
