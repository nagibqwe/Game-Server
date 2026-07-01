package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.F2PFightRoomState;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //同步房间的战斗进程结果
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PFightRoomState.MsgID.eMsgID_VALUE, clazz = F2PFightRoomState.class)

public class F2PFightRoomStateHandler extends Handler<F2PFightRoomState> {

    static final Logger log = LogManager.getLogger(F2PFightRoomStateHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PFightRoomState messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fightManager.deal().OnF2PFightRoomState(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PFightRoomStateHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
