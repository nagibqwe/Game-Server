package com.game.ninedaysfocused.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.NineDaysFocusedMessage.ReqOpenTasKPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求打开任务列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenTasKPanel.MsgID.eMsgID_VALUE, clazz = ReqOpenTasKPanel.class)

public class ReqOpenTasKPanelHandler extends Handler<ReqOpenTasKPanel> {

    static final Logger log = LogManager.getLogger(ReqOpenTasKPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenTasKPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenTasKPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
