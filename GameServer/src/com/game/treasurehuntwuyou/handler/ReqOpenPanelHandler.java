package com.game.treasurehuntwuyou.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TreasureHuntWuyouMessage.ReqOpenPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求打开界面
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenPanel.MsgID.eMsgID_VALUE, clazz = ReqOpenPanel.class)

public class ReqOpenPanelHandler extends Handler<ReqOpenPanel> {

    static final Logger log = LogManager.getLogger(ReqOpenPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.treasureHuntWuyouManager.getScript().reqOpenPanel((Player) mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
