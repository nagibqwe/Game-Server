package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GCloneCDRecordAdd;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //设置玩家的一个指定CD次数+1  ---逻辑代码已经被删
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GCloneCDRecordAdd.MsgID.eMsgID_VALUE, clazz = F2GCloneCDRecordAdd.class)

public class F2GCloneCDRecordAddHandler extends Handler<F2GCloneCDRecordAdd> {

    static final Logger log = LogManager.getLogger(F2GCloneCDRecordAddHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GCloneCDRecordAdd message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.crossFightdeal().OnF2GCloneCDRecordAdd(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GCloneCDRecordAddHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
