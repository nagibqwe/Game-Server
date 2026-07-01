package com.game.jjc.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.JJCMessage.ReqAddChance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //添加挑战次数
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqAddChance.MsgID.eMsgID_VALUE, clazz = ReqAddChance.class)

public class ReqAddChanceHandler extends Handler<ReqAddChance> {

    static final Logger log = LogManager.getLogger(ReqAddChanceHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqAddChance message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.jjcManager.deal().OnReqAddChance(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
