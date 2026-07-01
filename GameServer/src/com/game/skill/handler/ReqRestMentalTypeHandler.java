package com.game.skill.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SkillMessage.ReqRestMentalType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //重置心法
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRestMentalType.MsgID.eMsgID_VALUE, clazz = ReqRestMentalType.class)

public class ReqRestMentalTypeHandler extends Handler<ReqRestMentalType> {

    static final Logger log = LogManager.getLogger(ReqRestMentalTypeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRestMentalType messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.skillManager.deal().onReqResetSelectMental(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRestMentalTypeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
