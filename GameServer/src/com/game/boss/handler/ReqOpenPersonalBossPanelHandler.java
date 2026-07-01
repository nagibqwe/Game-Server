package com.game.boss.handler;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BossMessage.ReqOpenPersonalBossPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求打开个人boss界面
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqOpenPersonalBossPanel.MsgID.eMsgID_VALUE, clazz = ReqOpenPersonalBossPanel.class)

public class ReqOpenPersonalBossPanelHandler extends Handler<ReqOpenPersonalBossPanel> {

    static final Logger log = LogManager.getLogger(ReqOpenPersonalBossPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqOpenPersonalBossPanel message) {
        try {
            long start = TimeUtils.Time();

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenPersonalBossPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
