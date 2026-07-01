package com.game.soulanimalforest.handler;

import com.game.soulanimalforest.manager.SoulAnimalForestCrossManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.P2FResCloneMonsterDie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //指定怪物或者水晶的消失需要同步公共服的数据返回结果
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2FResCloneMonsterDie.MsgID.eMsgID_VALUE, clazz = P2FResCloneMonsterDie.class)

public class P2FResCloneMonsterDieHandler extends Handler<P2FResCloneMonsterDie> {

    static final Logger log = LogManager.getLogger(P2FResCloneMonsterDieHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2FResCloneMonsterDie messInfo) {
        try {
            long start = TimeUtils.Time();

            SoulAnimalForestCrossManager.getInstance().manager().onP2FResCloneMonsterDie(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FResCloneMonsterDieHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
