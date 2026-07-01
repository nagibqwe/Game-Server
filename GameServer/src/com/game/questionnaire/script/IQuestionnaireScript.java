package com.game.questionnaire.script;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.QuestionnaireMessage;

public interface IQuestionnaireScript extends IScript {

    /**
     * 检查活动开启状态
     */
    void checkOpenState(long lastCheckTime, long now);

    /**
     * 上线发送活动数据
     */
    void onlineDeal(Player player);

    /**
     * 请求领取奖励
     */
    void onReqGetReward(Player player);

    /**
     * 提交问卷
     */
    void onReqSubmitAnswer(Player player, QuestionnaireMessage.ReqSubmitAnswer messInfo);

    /**
     * 活动开启结束广播
     */
    void onP2GOpenState(QuestionnaireMessage.P2GOpenState messInfo);

    /**
     * 领取奖励返回
     */
    void onP2GGetRewardState(QuestionnaireMessage.P2GGetRewardState messInfo);


    /**
     * 下载完后领奖
     * @param player
     */
    void onDownLoadOver(Player player);

}
