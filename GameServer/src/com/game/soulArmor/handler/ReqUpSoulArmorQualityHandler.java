package com.game.soulArmor.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulArmorMessage.ReqUpSoulArmorQuality;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //魂甲突破
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpSoulArmorQuality.MsgID.eMsgID_VALUE, clazz = ReqUpSoulArmorQuality.class)

public class ReqUpSoulArmorQualityHandler extends Handler<ReqUpSoulArmorQuality> {

    static final Logger log = LogManager.getLogger(ReqUpSoulArmorQualityHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpSoulArmorQuality messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulArmorManager.script().reqUpSoulArmorQuality(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpSoulArmorQualityHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
