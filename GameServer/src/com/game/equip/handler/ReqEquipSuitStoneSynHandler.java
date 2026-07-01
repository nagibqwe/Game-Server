package com.game.equip.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EquipMessage.ReqEquipSuitStoneSyn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //装备套装石合成
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqEquipSuitStoneSyn.MsgID.eMsgID_VALUE, clazz = ReqEquipSuitStoneSyn.class)

public class ReqEquipSuitStoneSynHandler extends Handler<ReqEquipSuitStoneSyn> {

    static final Logger log = LogManager.getLogger(ReqEquipSuitStoneSynHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqEquipSuitStoneSyn message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
