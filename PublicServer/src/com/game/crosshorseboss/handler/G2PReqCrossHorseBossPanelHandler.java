package com.game.crosshorseboss.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossHorseBossMessage.G2PReqCrossHorseBossPanel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 游戏服--》公共服
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqCrossHorseBossPanel.MsgID.eMsgID_VALUE, clazz = G2PReqCrossHorseBossPanel.class)

public class G2PReqCrossHorseBossPanelHandler extends Handler<G2PReqCrossHorseBossPanel> {

    static final Logger log = LogManager.getLogger(G2PReqCrossHorseBossPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqCrossHorseBossPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.crossHorseBossManager.deal().onG2PReqCrossHorseBossPanel(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqCrossHorseBossPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
