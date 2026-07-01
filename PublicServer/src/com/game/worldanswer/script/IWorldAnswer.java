package com.game.worldanswer.script;

import game.message.worldAnswerMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by 542 on 2019/7/15.
 */
public interface IWorldAnswer {



    /**
     * 活动阶段
     */
     void worldAnswerStageChange(int stage);

    /**
     * 开始
     */
     void worldAnswerStart();

    /**
     * 结束
     */
     void worldAnswerOver();

    /**
     * 每题阶段
     */
     void quesiontRound(int round);

    /**
     * 答题阶段循环
     */
     void worldAnswerTimeLoop();

    /**
     * 报名
     */
      void onG2PReqApplyAnswer(ChannelHandlerContext context, worldAnswerMessage.G2PReqApplyAnswer messInfo);
    /**
     * 答题
     */
     void onG2PReqPlayerAnswerResult(ChannelHandlerContext context,worldAnswerMessage.G2PReqAnswerResult messInfo);

    /**
     * 玩家离开答题界面
     */
     void onG2PReqPlayerLeaveAnswer(ChannelHandlerContext context,Long roleId);



}
