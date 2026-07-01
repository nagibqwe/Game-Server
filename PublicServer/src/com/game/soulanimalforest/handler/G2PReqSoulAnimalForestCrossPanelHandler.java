package com.game.soulanimalforest.handler;

import com.game.soulanimalforest.manager.SoulAnimalForestManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.G2PReqSoulAnimalForestCrossPanel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服魂兽森林的界面 游戏服--》公共服
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqSoulAnimalForestCrossPanel.MsgID.eMsgID_VALUE, clazz = G2PReqSoulAnimalForestCrossPanel.class)

public class G2PReqSoulAnimalForestCrossPanelHandler extends Handler<G2PReqSoulAnimalForestCrossPanel> {

    static final Logger log = LogManager.getLogger(G2PReqSoulAnimalForestCrossPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqSoulAnimalForestCrossPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            SoulAnimalForestManager.getInstance().manager().onG2PReqSoulAnimalForestCrossPanel(context, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqSoulAnimalForestCrossPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
