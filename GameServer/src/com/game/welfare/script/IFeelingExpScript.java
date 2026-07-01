package com.game.welfare.script;

import com.game.player.structs.Player;

public interface IFeelingExpScript extends IWelfareScript {
    /**
     * 感悟
     * @param player
     * @param typ
     * @param times
     */
    void onReqFeelingExp(Player player, int typ, int times);
}
