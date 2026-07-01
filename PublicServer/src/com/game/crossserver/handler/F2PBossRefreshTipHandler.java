package com.game.crossserver.handler;

import com.game.server.MainServer;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2PBossRefreshTip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //刷新BOSS提示 -public
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PBossRefreshTip.MsgID.eMsgID_VALUE, clazz = F2PBossRefreshTip.class)

public class F2PBossRefreshTipHandler extends Handler<F2PBossRefreshTip> {

    static final Logger log = LogManager.getLogger(F2PBossRefreshTipHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PBossRefreshTip messInfo) {
        try {
            long start = TimeUtils.Time();

            MainServer.getInstance().gsmanager().sendBossTipsToGame(messInfo.getGroupID(),messInfo.getBossID(),messInfo.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PBossRefreshTipHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
