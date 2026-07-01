package com.game.soulArmor.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulArmorMessage.ReqSoulArmorBag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求魂甲背包
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSoulArmorBag.MsgID.eMsgID_VALUE, clazz = ReqSoulArmorBag.class)

public class ReqSoulArmorBagHandler extends Handler<ReqSoulArmorBag> {

    static final Logger log = LogManager.getLogger(ReqSoulArmorBagHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSoulArmorBag messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulArmorManager.script().reqSoulArmorBag(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSoulArmorBagHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
