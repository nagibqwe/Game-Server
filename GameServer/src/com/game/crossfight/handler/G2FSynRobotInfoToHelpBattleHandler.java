package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.G2FSynRobotInfoToHelpBattle;
import io.netty.channel.ChannelHandlerContext;


/**
 * makehandler  v1.9 for netty
 * 机器人跨服助战
 */
@Message(id = G2FSynRobotInfoToHelpBattle.MsgID.eMsgID_VALUE, clazz = G2FSynRobotInfoToHelpBattle.class)
public class G2FSynRobotInfoToHelpBattleHandler extends Handler<G2FSynRobotInfoToHelpBattle> {

    private static final Logger log = LogManager.getLogger(G2FSynRobotInfoToHelpBattleHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");


    @Override
    public void action(RMessage session, G2FSynRobotInfoToHelpBattle message) {

        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.crossServerManager.crossFightdeal().G2FSynRobotInfoToHelpBattle(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FSynRobotInfoToHelpBattleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}