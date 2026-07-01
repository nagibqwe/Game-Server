package com.game.commercialize.inter;

import com.game.commercialize.struct.DailyRechargeActivity;
import com.game.player.structs.Player;

public interface IDailyRecharge extends ICommercialize {
    /**
     * 请求领取每日累充奖励
     *
     * @param player  玩家
     * @param awardId 奖励id
     */
    void onReqDailyAccRechargeAward(Player player, int awardId);

    /**
     * 请求每日累充配置文件
     * @param player
     */
    void onReqDailyRechargeCfg(Player player);

    /**
     * 每日累充数据存库
     *
     * @param activity 统计数据
     */
    void saveDailyAccRecharge(DailyRechargeActivity activity);
}
