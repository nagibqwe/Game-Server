package com.game.equip.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EquipMessage.ReqSoulBeastEquipSyn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //魂兽装备合成
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqSoulBeastEquipSyn.MsgID.eMsgID_VALUE, clazz = ReqSoulBeastEquipSyn.class)

public class ReqSoulBeastEquipSynHandler extends Handler<ReqSoulBeastEquipSyn> {

    static final Logger log = LogManager.getLogger(ReqSoulBeastEquipSynHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqSoulBeastEquipSyn message) {
        try {
            long start = TimeUtils.Time();

            Manager.soulBeastManager.deal().reqSoulBeastEquipSynthetic(session.getExecutor(), message.getPart(), message.getEquipIdsList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
