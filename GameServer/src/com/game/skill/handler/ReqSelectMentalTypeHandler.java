package com.game.skill.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SkillMessage.ReqSelectMentalType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //选择心法
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSelectMentalType.MsgID.eMsgID_VALUE, clazz = ReqSelectMentalType.class)

public class ReqSelectMentalTypeHandler extends Handler<ReqSelectMentalType> {

    static final Logger log = LogManager.getLogger(ReqSelectMentalTypeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSelectMentalType messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.skillManager.deal().onSelectMental(player,messInfo.getMentalType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSelectMentalTypeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
