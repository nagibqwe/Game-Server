package com.game.soulanimalforest.handler;

import com.game.soulanimalforest.manager.SoulAnimalForestCrossManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.P2GResSoulAnimalForestCrossBossRefreshTip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //魂兽森林boss刷新提前一分钟提示
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2GResSoulAnimalForestCrossBossRefreshTip.MsgID.eMsgID_VALUE, clazz = P2GResSoulAnimalForestCrossBossRefreshTip.class)

public class P2GResSoulAnimalForestCrossBossRefreshTipHandler extends Handler<P2GResSoulAnimalForestCrossBossRefreshTip> {

    static final Logger log = LogManager.getLogger(P2GResSoulAnimalForestCrossBossRefreshTipHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2GResSoulAnimalForestCrossBossRefreshTip messInfo) {
        try {
            long start = TimeUtils.Time();

            SoulAnimalForestCrossManager.getInstance().manager().onP2GResSoulAnimalForestCrossBossRefreshTip(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GResSoulAnimalForestCrossBossRefreshTipHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
