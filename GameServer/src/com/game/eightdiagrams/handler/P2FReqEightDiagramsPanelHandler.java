package com.game.eightdiagrams.handler;
import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.message.EightDiagramsMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.P2FReqEightDiagramsPanel;


/**
* makehandler  v1.9 for netty
*向战斗服请求主面板数据
*/
@Message(id = EightDiagramsMessage.P2FReqEightDiagramsPanel.MsgID.eMsgID_VALUE, clazz = EightDiagramsMessage.P2FReqEightDiagramsPanel.class)
public class P2FReqEightDiagramsPanelHandler extends Handler<P2FReqEightDiagramsPanel>{

    private static final Logger log = LogManager.getLogger(P2FReqEightDiagramsPanelHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");


    @Override
    public void action(RMessage session, P2FReqEightDiagramsPanel message) {
        try {
            long start = TimeUtils.Time();

            Manager.eightDiagramsManager.deal().P2FReqEightDiagramsPanel(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FReqEightDiagramsPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e,e);
        }

    }
}