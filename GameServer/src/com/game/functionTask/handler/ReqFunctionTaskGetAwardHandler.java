package com.game.functionTask.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.FunctionTaskMessage.ReqFunctionTaskGetAward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqFunctionTaskGetAward.MsgID.eMsgID_VALUE, clazz = ReqFunctionTaskGetAward.class)

public class ReqFunctionTaskGetAwardHandler extends Handler<ReqFunctionTaskGetAward> {

    static final Logger log = LogManager.getLogger(ReqFunctionTaskGetAwardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqFunctionTaskGetAward messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.functionTaskManager.getScript().getAward((Player)mess.getExecutor(), messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFunctionTaskGetAwardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
