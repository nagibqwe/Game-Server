package com.game.unrealEquip.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.UnrealEquipMessage.ReqInlayUnreal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //镶嵌幻装
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqInlayUnreal.MsgID.eMsgID_VALUE, clazz = ReqInlayUnreal.class)

public class ReqInlayUnrealHandler extends Handler<ReqInlayUnreal> {

    static final Logger log = LogManager.getLogger(ReqInlayUnrealHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqInlayUnreal messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.unrealEquipManager.deal().soulEquipInlay(mess.getExecutor(),messInfo.getUID());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqInlayUnrealHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
