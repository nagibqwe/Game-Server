package com.game.home.handler;

import com.game.home.manager.HomeManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.ReqSendVisitorGift;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //访客送礼
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSendVisitorGift.MsgID.eMsgID_VALUE, clazz = ReqSendVisitorGift.class)

public class ReqSendVisitorGiftHandler extends Handler<ReqSendVisitorGift> {

    static final Logger log = LogManager.getLogger(ReqSendVisitorGiftHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSendVisitorGift messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            HomeManager.getInstance().deal().reqSendVisitorGiftHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSendVisitorGiftHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
