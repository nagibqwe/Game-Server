package com.game.map.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MapMessage.ReqFabaoJumpBlock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //出战法宝跳出阻挡
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqFabaoJumpBlock.MsgID.eMsgID_VALUE, clazz = ReqFabaoJumpBlock.class)

public class ReqFabaoJumpBlockHandler extends Handler<ReqFabaoJumpBlock> {

    static final Logger log = LogManager.getLogger(ReqFabaoJumpBlockHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqFabaoJumpBlock message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
