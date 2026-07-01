package com.game.eightdiagrams.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.message.EightDiagramsMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.G2FReqRankPanel;
import io.netty.channel.ChannelHandlerContext;


/**
 * makehandler  v1.9 for netty
 * 游戏服像战斗服请求排行
 */
@Message(id = EightDiagramsMessage.G2FReqRankPanel.MsgID.eMsgID_VALUE, clazz = EightDiagramsMessage.G2FReqRankPanel.class)
public class G2FReqRankPanelHandler extends Handler<G2FReqRankPanel> {

    private static final Logger log = LogManager.getLogger(G2FReqRankPanelHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");


    @Override
    public void action(RMessage session, G2FReqRankPanel message) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = session.getContext();
            Manager.eightDiagramsManager.deal().G2FReqRankPanel(context, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2FReqRankPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}