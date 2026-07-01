package com.game.soulbeast.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulBeastMessage.ReqSoulBeastFight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求魂兽出战
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSoulBeastFight.MsgID.eMsgID_VALUE, clazz = ReqSoulBeastFight.class)

public class ReqSoulBeastFightHandler extends Handler<ReqSoulBeastFight> {

    static final Logger log = LogManager.getLogger(ReqSoulBeastFightHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSoulBeastFight messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulBeastManager.deal().reqSoulBeastFight(mess.getExecutor(), messInfo.getSoulId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSoulBeastFightHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
