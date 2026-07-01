package com.game.peak.script;

import com.game.db.bean.PeakBean;
import game.core.script.IScript;
import game.message.PeakMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Desc TODO
 * @Date 2020/10/13 16:56
 * @Auth ZUncle
 */
public interface IPeak extends IScript {

    /**
     * 取消匹配
     * @param mess
     */
    void G2PCancelPeakMatch(ChannelHandlerContext context,PeakMessage.G2PCancelPeakMatch mess);

    /**
     * 匹配
     * @param mess
     */
    void G2PEnterPeakMatch(ChannelHandlerContext context, PeakMessage.G2PEnterPeakMatch mess);

    /**
     * 获取巅峰数据
     * @param context
     * @param messInfo
     */
    void G2PPeakInfo(ChannelHandlerContext context, PeakMessage.G2PPeakInfo messInfo);

    /**
     * 获取排名信息
     * @param mess
     */
    void G2PPeakRankInfo(ChannelHandlerContext context, PeakMessage.G2PPeakRankInfo mess);

    /**
     * 获取段位信息
     * @param mess
     */
    void G2PPeakStageInfo(ChannelHandlerContext context, PeakMessage.G2PPeakStageInfo mess);

    /**
     * 领取巅峰竞技段位奖励
     * @param mess
     */
    void G2PPeakStageReward(ChannelHandlerContext context, PeakMessage.G2PPeakStageReward mess);

    /**
     * 领取巅峰竞技场次奖励
     * @param context
     * @param messInfo
     */
    void G2PPeakTimesReward(ChannelHandlerContext context, PeakMessage.G2PPeakTimesReward messInfo);

    /**
     * 巅峰竞技挑战结束
     * @param mess
     */
    void F2PPeakCloneResult(PeakMessage.F2PPeakCloneResult mess);

    ////////////////////////////////////////////////////////////////////////////////////////
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
     * 启动服务器加载本服 巅峰竞技
      */
    void loadAll();

    /**
     * 0点刷新
     */
    void zeroClockDeal();

    /**
     * 保存到数据库
     * @param roleId
     */
    void saveDB(long roleId);

    /**
     * 增加积分
     * @param bean
     * @param score
     */
    void addScore(PeakBean bean, int score);


}
