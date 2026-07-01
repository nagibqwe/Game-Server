package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.F2GSynPlayerInfoResult;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //同步的结果
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GSynPlayerInfoResult.MsgID.eMsgID_VALUE, clazz = F2GSynPlayerInfoResult.class)

public class F2GSynPlayerInfoResultHandler extends Handler<F2GSynPlayerInfoResult> {

    static final Logger log = LogManager.getLogger(F2GSynPlayerInfoResultHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GSynPlayerInfoResult message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.crossFightdeal().OnF2GSynPlayerInfoResult(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GSynPlayerInfoResultHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
