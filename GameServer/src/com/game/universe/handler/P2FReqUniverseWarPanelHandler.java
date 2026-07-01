package com.game.universe.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MSG_UniverseMessage.P2FReqUniverseWarPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //打开太虚战场的面板 公共服--》战斗服
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = P2FReqUniverseWarPanel.MsgID.eMsgID_VALUE, clazz = P2FReqUniverseWarPanel.class)

public class P2FReqUniverseWarPanelHandler extends Handler<P2FReqUniverseWarPanel> {

    static final Logger log = LogManager.getLogger(P2FReqUniverseWarPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, P2FReqUniverseWarPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.universeManager.deal().P2FReqUniverseWarPanel(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2FReqUniverseWarPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
