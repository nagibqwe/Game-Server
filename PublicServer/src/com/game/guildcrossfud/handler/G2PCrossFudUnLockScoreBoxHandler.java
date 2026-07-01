package com.game.guildcrossfud.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildCrossFudMessage.G2PCrossFudUnLockScoreBox;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //解锁积分宝箱
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PCrossFudUnLockScoreBox.MsgID.eMsgID_VALUE, clazz = G2PCrossFudUnLockScoreBox.class)

public class G2PCrossFudUnLockScoreBoxHandler extends Handler<G2PCrossFudUnLockScoreBox> {

    static final Logger log = LogManager.getLogger(G2PCrossFudUnLockScoreBoxHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PCrossFudUnLockScoreBox messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.fudManager.deal().G2PCrossFudUnLockScoreBox(context,  messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PCrossFudUnLockScoreBoxHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
