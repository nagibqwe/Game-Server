package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqMatchStart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求开始匹配
* @Desc gzl
* @Auth Tool
*/

@Message(id = ReqMatchStart.MsgID.eMsgID_VALUE, clazz = ReqMatchStart.class)

public class ReqMatchStartHandler extends Handler<ReqMatchStart> {

    static final Logger log = LogManager.getLogger(ReqMatchStartHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMatchStart messInfo) {
        try {
            long start = TimeUtils.Time();
            Manager.couplefightManager.getScript().matchStart((Player)mess.getExecutor());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMatchStartHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
