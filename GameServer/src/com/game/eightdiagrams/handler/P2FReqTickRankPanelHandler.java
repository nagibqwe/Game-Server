package com.game.eightdiagrams.handler;
import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.message.EightDiagramsMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.P2FReqTickRankPanel;


/**
* makehandler  v1.9 for netty
*向战斗服请求实时排行数据
*/
@Message(id = EightDiagramsMessage.P2FReqTickRankPanel.MsgID.eMsgID_VALUE, clazz = EightDiagramsMessage.P2FReqTickRankPanel.class)
public class P2FReqTickRankPanelHandler extends Handler<P2FReqTickRankPanel>{

    private static final Logger log = LogManager.getLogger(P2FReqTickRankPanelHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2FReqTickRankPanel message) {
        try {
            long start = TimeUtils.Time();

            Manager.eightDiagramsManager.deal().P2FReqTickRankPanel(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FReqTickRankPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e,e);
        }
    }
}