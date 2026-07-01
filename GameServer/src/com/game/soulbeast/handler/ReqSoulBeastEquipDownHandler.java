package com.game.soulbeast.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulBeastMessage.ReqSoulBeastEquipDown;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求魂兽装备脱
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSoulBeastEquipDown.MsgID.eMsgID_VALUE, clazz = ReqSoulBeastEquipDown.class)

public class ReqSoulBeastEquipDownHandler extends Handler<ReqSoulBeastEquipDown> {

    static final Logger log = LogManager.getLogger(ReqSoulBeastEquipDownHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSoulBeastEquipDown messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulBeastManager.deal().reqSoulBeastEquipDown(mess.getExecutor(), messInfo.getSoulBeastId(), messInfo.getEquipIdsList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSoulBeastEquipDownHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
