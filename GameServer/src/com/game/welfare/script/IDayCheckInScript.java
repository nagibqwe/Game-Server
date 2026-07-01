package com.game.welfare.script;

import com.game.player.structs.Player;

public interface IDayCheckInScript extends IWelfareScript {
    /**
     * 请求签到或者领奖
     * @param player
     * @param cfgID 签到配置ID或者领奖配置ID
     * @param typ 1签到，2领奖
     */
    void onReqDayCheckIn(Player player, int cfgID, int typ);
}
