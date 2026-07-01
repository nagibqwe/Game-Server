package com.game.home.handler;

import com.game.home.manager.HomeManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.ReqHomeVisitorGiftList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取访客送礼清单
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqHomeVisitorGiftList.MsgID.eMsgID_VALUE, clazz = ReqHomeVisitorGiftList.class)

public class ReqHomeVisitorGiftListHandler extends Handler<ReqHomeVisitorGiftList> {

    static final Logger log = LogManager.getLogger(ReqHomeVisitorGiftListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqHomeVisitorGiftList messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            HomeManager.getInstance().deal().reqHomeVisitorGiftListHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqHomeVisitorGiftListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
