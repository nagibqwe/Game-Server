package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GSendPersonalNotice;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服发送公告到游戏服游戏服
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2GSendPersonalNotice.MsgID.eMsgID_VALUE, clazz = F2GSendPersonalNotice.class)

public class F2GSendPersonalNoticeHandler extends Handler<F2GSendPersonalNotice> {

    static final Logger log = LogManager.getLogger(F2GSendPersonalNoticeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2GSendPersonalNotice messInfo) {
        try {
            long start = TimeUtils.Time();
            ChannelHandlerContext context = mess.getContext();
            Manager.crossServerManager.getCrossServer().onF2GSendPersonalNotice(context, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GSendPersonalNoticeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
