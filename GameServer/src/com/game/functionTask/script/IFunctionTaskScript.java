package com.game.functionTask.script;

import com.game.player.structs.Player;
import com.game.recharge.structs.RechargeItemInfo;
import game.core.script.IScript;

/**
 * 核心功能任务脚本
 */
public interface IFunctionTaskScript extends IScript {

    /**
     * 玩家上线操作
     */
    public void online(Player player);

    /**
     * 初始化
     */
    void init();

    /**
     * 领取奖励
     * @param player
     * @param id
     */
    void getAward(Player player, int id);

    /**
     * 刷新每日功能任务
     * @param player
     */
    void onRefreshUpProgress(Player player, int type, int... change);

    /**
     * 玩家充值
     * @param rechargeId
     */
    void onRecharge(Player player, int rechargeId);

    /**
     * 是否可获得奖励
     * @param goods_id
     * @return
     */
    boolean canReward(Player player, int goods_id);
}
