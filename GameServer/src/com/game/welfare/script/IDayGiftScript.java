package com.game.welfare.script;

import com.game.player.structs.Player;

public interface IDayGiftScript extends IWelfareScript {
    /**
     * 临时每日礼包充值
     * @param player
     * @param id
     */
    void onReqDayGiftRecharge(Player player, int id);
}
