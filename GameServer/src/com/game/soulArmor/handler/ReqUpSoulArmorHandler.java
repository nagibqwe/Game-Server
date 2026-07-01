package com.game.soulArmor.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulArmorMessage.ReqUpSoulArmor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //淬炼魂甲
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpSoulArmor.MsgID.eMsgID_VALUE, clazz = ReqUpSoulArmor.class)

public class ReqUpSoulArmorHandler extends Handler<ReqUpSoulArmor> {

    static final Logger log = LogManager.getLogger(ReqUpSoulArmorHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpSoulArmor messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulArmorManager.script().reqUpSoulArmor(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpSoulArmorHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
