package com.game.unrealEquip.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.UnrealEquipMessage.ReqUseUnreal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //使用幻魂
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUseUnreal.MsgID.eMsgID_VALUE, clazz = ReqUseUnreal.class)

public class ReqUseUnrealHandler extends Handler<ReqUseUnreal> {

    static final Logger log = LogManager.getLogger(ReqUseUnrealHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUseUnreal messInfo) {
        try {
            long start = TimeUtils.Time();
            Manager.unrealEquipManager.deal().useUnrealSoul(mess.getExecutor(),messInfo.getItemId(),messInfo.getUseNum());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUseUnrealHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
