package com.game.welfare.script;

import com.game.player.structs.Player;

public interface IExclusiveCardScript extends IWelfareScript {
    /**
     * 购买
     * @param player
     * @param cardId
     */
    void onReqExclusiveCard(Player player, int cardId);

    /**
     * 领取奖励
     * @param player
     * @param cardId
     */
    void onReqExclusiveCardReward(Player player, int cardId);

    /**
     * 是否有周卡
     * @param player
     * @return
     */
    boolean haveWeekCard(Player player);

    /**
     * 是否有月卡
     * @param player
     * @return
     */
    boolean haveMonthCard(Player player);

    /**
     * 是否有尊享卡
     * @param player
     * @return
     */
    boolean haveExclusiveCard(Player player);

    /**
     * 获取聚宝盆周卡池倍率，没有周卡才有
     */
    int getWeekCardRate(Player player);

    /**
     * 获取聚宝盆月卡池倍率，没有月卡才有
     */
    int getMonthCardRate(Player player);


    /**
     * 获取剩余时间
     * @param player
     * @param cfgGoodId
     * @return
     */
    long getEndTime(Player player,int cfgGoodId);

    /**
     * 发送之前的奖励
     * @param player
     */
    public void sendOldAward(Player player);
}
