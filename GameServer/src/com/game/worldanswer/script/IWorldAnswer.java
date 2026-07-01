package com.game.worldanswer.script;

import com.game.player.structs.Player;
import game.message.worldAnswerMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by 542 on 2019/7/15.
 */
public interface IWorldAnswer {

    /**
     * 答题报名
     */
    public void  applyAnswer(Player player);
    /**
     * 玩家发过来的答题结果
     */
    public void playerAnswerResult(Player player,int index);

    /**
     * 玩家离开答题界面
     */
    public void playerLeaveAnswer(Player player);

    /**
     * 发答题奖励
     */
    public void sendAnswerReward( worldAnswerMessage.P2GResQuestionReward messInfo);

    /**
     * 发答题结束奖励
     */
    public void sendAnswerOverReward( worldAnswerMessage.P2GResWorldAnswerOver messInfo);


}
