package com.game.couplefight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.F2PResFightResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //战斗服到公共服-海选战斗结果
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PResFightResult.MsgID.eMsgID_VALUE, clazz = F2PResFightResult.class)

public class F2PResFightResultHandler extends Handler<F2PResFightResult> {

    static final Logger log = LogManager.getLogger(F2PResFightResultHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PResFightResult messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().fightResult(messInfo.getType(), messInfo.getFid(), messInfo.getWinList(), messInfo.getLoseList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PResFightResultHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
