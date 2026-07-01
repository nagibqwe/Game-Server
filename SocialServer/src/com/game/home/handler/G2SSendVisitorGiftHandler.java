package com.game.home.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.G2SSendVisitorGift;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取访客送礼
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SSendVisitorGift.MsgID.eMsgID_VALUE, clazz = G2SSendVisitorGift.class)

public class G2SSendVisitorGiftHandler extends Handler<G2SSendVisitorGift> {

    static final Logger log = LogManager.getLogger(G2SSendVisitorGiftHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SSendVisitorGift messInfo) {
        try {
            long start = TimeUtils.Time();

            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.homeManager.deal().G2SSendVisitorGift(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SSendVisitorGiftHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
