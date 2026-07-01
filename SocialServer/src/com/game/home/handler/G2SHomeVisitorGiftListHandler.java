package com.game.home.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.G2SHomeVisitorGiftList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取访客送礼清单
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SHomeVisitorGiftList.MsgID.eMsgID_VALUE, clazz = G2SHomeVisitorGiftList.class)

public class G2SHomeVisitorGiftListHandler extends Handler<G2SHomeVisitorGiftList> {

    static final Logger log = LogManager.getLogger(G2SHomeVisitorGiftListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SHomeVisitorGiftList messInfo) {
        try {
            long start = TimeUtils.Time();

            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.homeManager.deal().G2SHomeVisitorGiftList(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SHomeVisitorGiftListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
