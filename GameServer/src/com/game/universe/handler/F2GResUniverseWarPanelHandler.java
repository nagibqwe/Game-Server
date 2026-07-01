package com.game.universe.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MSG_UniverseMessage.F2GResUniverseWarPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //返回太虚战场数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = F2GResUniverseWarPanel.MsgID.eMsgID_VALUE, clazz = F2GResUniverseWarPanel.class)

public class F2GResUniverseWarPanelHandler extends Handler<F2GResUniverseWarPanel> {

    static final Logger log = LogManager.getLogger(F2GResUniverseWarPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, F2GResUniverseWarPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.universeManager.deal().F2GResUniverseWarPanel(messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GResUniverseWarPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
