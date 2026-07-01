package com.game.commercialize.inter;

import com.game.player.structs.Player;

/**
 * Created by cxl on 2020/9/7.
 */
public interface IDailyRechargeTotal extends ICommercialize {


    /**
     * 请求领奖
     * @param player
     * @param id
     */
    void onReqGetRechargeReward(Player player,int id);

    /**
     * 累计消耗
     * @param player
     * @param gold
     */
    void totalConsume(Player player,int gold);


    /**
     *领取宝箱奖励
     * @param player
     */
    void onReqGetBoxReward(Player player);

}
