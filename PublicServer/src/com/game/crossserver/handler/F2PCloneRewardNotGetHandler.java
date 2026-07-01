package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2PCloneRewardNotGet;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //缓存没有领到奖的人信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2PCloneRewardNotGet.MsgID.eMsgID_VALUE, clazz = F2PCloneRewardNotGet.class)

public class F2PCloneRewardNotGetHandler extends Handler<F2PCloneRewardNotGet> {

    static final Logger log = LogManager.getLogger(F2PCloneRewardNotGetHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2PCloneRewardNotGet messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fightManager.deal().OnF2PCloneRewardNotGet(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2PCloneRewardNotGetHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
