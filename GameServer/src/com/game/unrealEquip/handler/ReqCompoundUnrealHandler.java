package com.game.unrealEquip.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.UnrealEquipMessage.ReqCompoundUnreal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //合成
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCompoundUnreal.MsgID.eMsgID_VALUE, clazz = ReqCompoundUnreal.class)

public class ReqCompoundUnrealHandler extends Handler<ReqCompoundUnreal> {

    static final Logger log = LogManager.getLogger(ReqCompoundUnrealHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCompoundUnreal messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.unrealEquipManager.deal().compoundUnrealSoul(mess.getExecutor(),messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCompoundUnrealHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
