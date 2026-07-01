package com.game.horse.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HorseMessage.ReqMountChangeAssi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 更换助阵坐骑
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqMountChangeAssi.MsgID.eMsgID_VALUE, clazz = ReqMountChangeAssi.class)

public class ReqMountChangeAssiHandler extends Handler<ReqMountChangeAssi> {

    static final Logger log = LogManager.getLogger(ReqMountChangeAssiHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqMountChangeAssi message) {
        try {
            long start = TimeUtils.Time();

            Manager.horseManager.deal().changeHorseAssiant(session.getExecutor(), message.getAssistantId(), message.getMountModelId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
