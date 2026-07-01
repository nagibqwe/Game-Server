package com.game.openserverac.scripts;

import com.game.player.structs.Player;
import game.core.script.IScript;
import game.message.OpenServerAcMessage;

public interface IV4HelpScript extends IScript {
    /**
     * 活动结束
     */
    void doActivityEnd();

    /**
     * 零点刷新
     */
    void zeroClockDeal();
    /**
     * 心跳
     */
    void tick();
    /**
     * 玩家上线，推送数据
     * */
    void playerOnline(Player player);
    /**
     * 成为申请人
     * @param player
     * @param msg
     */
     void ReqV4HelpBeApply(Player player, OpenServerAcMessage.ReqV4HelpBeApply msg);

    /**
     * 请求v4助力列表
     * @param player
     * @param msg
     */
     void ReqV4HelpInfo(Player player, OpenServerAcMessage.ReqV4HelpInfo msg);

    /**
     * 请求投资玩家
     * @param player
     * @param msg
     */
     void ReqV4HelpOther(Player player, OpenServerAcMessage.ReqV4HelpOther msg);

    /**
     * 请求领取助力奖励
     * @param player
     * @param msg
     */
     void ReqV4GetAward(Player player, OpenServerAcMessage.ReqV4GetAward msg);
}
