package com.game.huaxinflysword.script;

import com.game.player.structs.Player;
import game.core.script.IScript;

/**
 * @author gsj
 * @create 2020/7/15 16:50
 */
public interface ISwordSoulScript extends IScript {

    /**
     * 功能开启初始数据
     */
    void initHookInfo(Player player);

    /**
     * 打开剑灵阁面板
     */
    void onReqSwordSoulPanel(Player player);

    /**
     * 请求领取挂机奖励
     */
    void onReqGetHookReward(Player player);

    /**
     * 快速收益
     */
    void onReqQuickEarn(Player player);

    /**
     * 完成剑灵阁副本挑战
     */
    void finishChallengeTower(Player player, int layer);

    /**
     * 请求剑冢面板
     */
    void onReqSwordTombPanel(Player player);

    /**
     * 请求剑冢觉醒
     */
    void onReqSwordTombWakeUp(Player player, int id);


    /**
     *跳关请求
     * @param player
     */
    void onReqSkipSoulCopy(Player player);
}
