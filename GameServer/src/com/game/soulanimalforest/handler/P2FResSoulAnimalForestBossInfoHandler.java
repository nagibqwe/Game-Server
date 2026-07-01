package com.game.soulanimalforest.handler;

import com.game.soulanimalforest.manager.SoulAnimalForestCrossManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.P2FResSoulAnimalForestBossInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服BOSS信息的更新，会有多次
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2FResSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, clazz = P2FResSoulAnimalForestBossInfo.class)

public class P2FResSoulAnimalForestBossInfoHandler extends Handler<P2FResSoulAnimalForestBossInfo> {

    static final Logger log = LogManager.getLogger(P2FResSoulAnimalForestBossInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2FResSoulAnimalForestBossInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            SoulAnimalForestCrossManager.getInstance().manager().onP2FResSoulAnimalForestBossInfo(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FResSoulAnimalForestBossInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
