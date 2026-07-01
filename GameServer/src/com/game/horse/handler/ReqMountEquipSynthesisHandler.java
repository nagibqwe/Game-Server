package com.game.horse.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HorseMessage.ReqMountEquipSynthesis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 装备合成
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqMountEquipSynthesis.MsgID.eMsgID_VALUE, clazz = ReqMountEquipSynthesis.class)

public class ReqMountEquipSynthesisHandler extends Handler<ReqMountEquipSynthesis> {

    static final Logger log = LogManager.getLogger(ReqMountEquipSynthesisHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqMountEquipSynthesis message) {
        try {
            long start = TimeUtils.Time();

            Manager.horseManager.deal().horseEquipSynthetic(session.getExecutor(), message.getAssistantId(), message.getCellId(), message.getEquipsList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
