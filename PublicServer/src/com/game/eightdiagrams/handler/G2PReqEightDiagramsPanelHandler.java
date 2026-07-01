package com.game.eightdiagrams.handler;

import com.game.eightdiagrams.manager.EightDiagramsManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.G2PReqEightDiagramsPanel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //向共公共服请求数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqEightDiagramsPanel.MsgID.eMsgID_VALUE, clazz = G2PReqEightDiagramsPanel.class)

public class G2PReqEightDiagramsPanelHandler extends Handler<G2PReqEightDiagramsPanel> {

    static final Logger log = LogManager.getLogger(G2PReqEightDiagramsPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqEightDiagramsPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            ChannelHandlerContext context = mess.getContext();
            EightDiagramsManager.getInstance().deal().G2PReqEightDiagramsPanel(context,messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqEightDiagramsPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
