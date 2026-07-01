package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.utils.MessageUtils;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage;
import game.message.CouplefightMessage.P2GResMatchSuccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //公共服到游戏服-匹配成功
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GResMatchSuccess.MsgID.eMsgID_VALUE, clazz = P2GResMatchSuccess.class)

public class P2GResMatchSuccessHandler extends Handler<P2GResMatchSuccess> {

    static final Logger log = LogManager.getLogger(P2GResMatchSuccessHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GResMatchSuccess messInfo) {
        try {
            long start = TimeUtils.Time();

            Player p1 = Manager.playerManager.getPlayerCache(messInfo.getMId());
            Player p2 = Manager.playerManager.getPlayerCache(messInfo.getWId());

            CouplefightMessage.ResMatchSuccess.Builder res = CouplefightMessage.ResMatchSuccess.newBuilder();

            MessageUtils.send_to_player(p1, CouplefightMessage.ResMatchSuccess.MsgID.eMsgID_VALUE, res.build().toByteArray());
            MessageUtils.send_to_player(p2, CouplefightMessage.ResMatchSuccess.MsgID.eMsgID_VALUE, res.build().toByteArray());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResMatchSuccessHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
