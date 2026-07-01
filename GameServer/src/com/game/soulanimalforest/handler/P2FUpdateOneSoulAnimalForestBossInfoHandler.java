package com.game.soulanimalforest.handler;

import com.game.soulanimalforest.manager.SoulAnimalForestCrossManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.P2FUpdateOneSoulAnimalForestBossInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服通知所有的战斗服的单个BOSS更新
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2FUpdateOneSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, clazz = P2FUpdateOneSoulAnimalForestBossInfo.class)

public class P2FUpdateOneSoulAnimalForestBossInfoHandler extends Handler<P2FUpdateOneSoulAnimalForestBossInfo> {

    static final Logger log = LogManager.getLogger(P2FUpdateOneSoulAnimalForestBossInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2FUpdateOneSoulAnimalForestBossInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            SoulAnimalForestCrossManager.getInstance().manager().onP2FUpdateOneSoulAnimalForestBossInfo(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FUpdateOneSoulAnimalForestBossInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
