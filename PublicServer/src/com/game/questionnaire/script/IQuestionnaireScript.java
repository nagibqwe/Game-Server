package com.game.questionnaire.script;

import game.core.script.IScript;
import game.message.QuestionnaireMessage;
import io.netty.channel.ChannelHandlerContext;

public interface IQuestionnaireScript extends IScript {

    /**
     * 检查活动开启结束
     */
    void checkActivityOpen(long now, long lastCheckTime);

    /**
     * 开启活动
     */
    void openActivity();

    /**
     * 活动关闭
     */
    void closeActivity();

    /**
     * 请求面板信息
     */
    void onG2PGetPanelInfo(ChannelHandlerContext session, long userId, long roleId);

    /**
     * 提交问卷
     */
    void onG2PSubmitAnswer(ChannelHandlerContext session, QuestionnaireMessage.G2PSubmitAnswer messInfo);

    /**
     * 领取奖励
     */
    void onG2PGetReward(ChannelHandlerContext session, QuestionnaireMessage.G2PGetReward messInfo);

}
