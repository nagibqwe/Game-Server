package com.game.peak.script;

import com.game.db.bean.PeakBean;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.PeakMessage;

/**
 * @Desc TODO
 * @Date 2020/10/13 16:56
 * @Auth ZUncle
 */
public interface IPeakPk extends IScript {

    /**
     * 取消匹配
     * @param player
     */
    void reqCancelPeakMatch(Player player);

    /**
     * 匹配
     * @param player
     */
    void reqEnterPeakMatch(Player player);

    /**
     * 获取巅峰数据
     * @param player
     */
    void reqPeakInfo(Player player);

    /**
     * 获取排名信息
     * @param player
     */
    void reqPeakRankInfo(Player player);

    /**
     * 获取段位信息
     * @param player
     */
    void reqPeakStageInfo(Player player);

    /**
     * 领取巅峰竞技段位奖励
     * @param player
     * @param stageId
     */
    void reqPeakStageReward(Player player, int stageId);

    /**
     * 领取巅峰竞技场次奖励
     * @param player
     * @param times
     */
    void reqPeakTimesReward(Player player, int times);

    /**
     * 跨服巅峰竞技挑战结果
     * @param roleId
     * @param isWin
     * @param exp
     */
    void P2GPeakCloneResult(long roleId, int isWin, long exp);

    /**
     * 跨服领取段位奖励
     * @param messInfo
     */
    void P2GPeakStageReward(PeakMessage.P2GPeakStageReward messInfo);

    /**
     * 跨服领取场次奖励
     * @param messInfo
     */
    void P2GPeakTimesReward(PeakMessage.P2GPeakTimesReward messInfo);

    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 创建匹配等待副本
     */
    MapObject getWaitMap();

    /**
     * 活动开启
     */
    void start();
    /**
     * 活动结束
     */
    void close();

    /**
     * 开始匹配
     */
    void match();

    /**
     * 新增积分
     */
    void cloneOver(PeakBean win, int winScore, PeakBean loser, int loserScore);

    /**
     * 添加积分
     * @param bean
     * @param score
     */
    void cloneOver(PeakBean bean, int score);

    /**
     * 启动服务器加载本服 巅峰竞技
      */
    void loadAll();

    /**
     * 0点刷新
     */
    void zeroClockDeal();

    /**
     * 进入等待地图
     * @param player
     */
    void reqEnterWaitScene(Player player);

    int getZoneModelId();

}
