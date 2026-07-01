package com.game.soulArmor.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulArmorMessage.ReqSoulArmorMerge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 魂印合成
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSoulArmorMerge.MsgID.eMsgID_VALUE, clazz = ReqSoulArmorMerge.class)

public class ReqSoulArmorMergeHandler extends Handler<ReqSoulArmorMerge> {

    static final Logger log = LogManager.getLogger(ReqSoulArmorMergeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSoulArmorMerge messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulArmorManager.script().reqSoulArmorMerge(mess.getExecutor(), messInfo.getSlotId(), messInfo.getEquipsList());


            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSoulArmorMergeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
