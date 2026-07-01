package com.game.unrealEquip.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.UnrealEquipMessage.ReqResolveUnreal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //分解幻装
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqResolveUnreal.MsgID.eMsgID_VALUE, clazz = ReqResolveUnreal.class)

public class ReqResolveUnrealHandler extends Handler<ReqResolveUnreal> {

    static final Logger log = LogManager.getLogger(ReqResolveUnrealHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqResolveUnreal messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.unrealEquipManager.deal().resolveUnrealEquip(mess.getExecutor(),messInfo.getUid());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqResolveUnrealHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
