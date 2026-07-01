package com.game.statevip.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateVipMessage.ReqOpenExpPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Clinet -> Server 打开经验面板
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenExpPanel.MsgID.eMsgID_VALUE, clazz = ReqOpenExpPanel.class)

public class ReqOpenExpPanelHandler extends Handler<ReqOpenExpPanel> {

    static final Logger log = LogManager.getLogger(ReqOpenExpPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenExpPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenExpPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
