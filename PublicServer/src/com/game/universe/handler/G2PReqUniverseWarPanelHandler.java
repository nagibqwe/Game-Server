package com.game.universe.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MSG_UniverseMessage.G2PReqUniverseWarPanel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //打开太虚战场的面板 游戏服--》公共服
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqUniverseWarPanel.MsgID.eMsgID_VALUE, clazz = G2PReqUniverseWarPanel.class)

public class G2PReqUniverseWarPanelHandler extends Handler<G2PReqUniverseWarPanel> {

    static final Logger log = LogManager.getLogger(G2PReqUniverseWarPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqUniverseWarPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            Manager.universeManager.manager().openPanel(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqUniverseWarPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
