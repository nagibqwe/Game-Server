package com.game.soulbeast.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulBeastMessage.ReqSoulBeastEquipUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求魂兽装备升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSoulBeastEquipUp.MsgID.eMsgID_VALUE, clazz = ReqSoulBeastEquipUp.class)

public class ReqSoulBeastEquipUpHandler extends Handler<ReqSoulBeastEquipUp> {

    static final Logger log = LogManager.getLogger(ReqSoulBeastEquipUpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSoulBeastEquipUp messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulBeastManager.deal().reqSoulBeastEquipUp(mess.getExecutor(),messInfo.getSoulId(), messInfo.getFixEquipId(), messInfo.getCostsList(), messInfo.getNeedDouble());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSoulBeastEquipUpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
