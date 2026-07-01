package com.game.worldbonfire.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldBonfireMessage.G2PWorldBonfireAddWoodCheck;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求跨服检查篝火升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PWorldBonfireAddWoodCheck.MsgID.eMsgID_VALUE, clazz = G2PWorldBonfireAddWoodCheck.class)

public class G2PWorldBonfireAddWoodCheckHandler extends Handler<G2PWorldBonfireAddWoodCheck> {

    static final Logger log = LogManager.getLogger(G2PWorldBonfireAddWoodCheckHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PWorldBonfireAddWoodCheck messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.worldBonfireManager.manager().onWorldBonfireCheckLevel(context, messInfo.getRoleId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PWorldBonfireAddWoodCheckHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
