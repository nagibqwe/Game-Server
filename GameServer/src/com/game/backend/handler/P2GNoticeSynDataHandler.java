package com.game.backend.handler;

import com.game.server.GameServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BackendMessage.P2GNoticeSynData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知有游戏服更新屏蔽或者白名单等数据
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GNoticeSynData.MsgID.eMsgID_VALUE, clazz = P2GNoticeSynData.class)

public class P2GNoticeSynDataHandler extends Handler<P2GNoticeSynData> {

    static final Logger log = LogManager.getLogger(P2GNoticeSynDataHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GNoticeSynData message) {
        try {
            long start = TimeUtils.Time();

            GameServer.getBackCommandScript().P2GNoticeSynData(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GNoticeSynDataHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
