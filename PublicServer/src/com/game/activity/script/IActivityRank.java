package com.game.activity.script;

import game.core.script.IScript;
import game.message.BackendMessage.G2PReqCrossRank;
import game.message.BackendMessage.G2PReqCrossRankData;
import game.message.BackendMessage.G2PReqCrossRankDataIsReceive;

/**
 * 活动排行接口
 * @author zhaibiao
 */
public interface IActivityRank extends IScript{
    //发送数据到跨服处理排行
    void OnG2PReqCrossRank(G2PReqCrossRank mess);
    //请求玩家自己的排行数据
    void OnG2PReqCrossRankData(G2PReqCrossRankData mess);
    //自己是否可以领取
    void OnG2PReqCrossRankDataIsReceive(G2PReqCrossRankDataIsReceive mess);
}
