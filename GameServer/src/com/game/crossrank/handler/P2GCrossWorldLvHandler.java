package com.game.crossrank.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossRankMessage.P2GCrossWorldLv;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //返回游戏服跨服世界等级
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GCrossWorldLv.MsgID.eMsgID_VALUE, clazz = P2GCrossWorldLv.class)

public class P2GCrossWorldLvHandler extends Handler<P2GCrossWorldLv> {

    static final Logger log = LogManager.getLogger(P2GCrossWorldLvHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GCrossWorldLv message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossRankManager.deal().onP2GCrossWorldLv(message.getCrossWorldLv());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GCrossWorldLvHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
