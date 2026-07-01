package com.game.devilseries.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DevilSeriesMessage.ReqOpenDeviBossPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求打开除魔团页面
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenDeviBossPanel.MsgID.eMsgID_VALUE, clazz = ReqOpenDeviBossPanel.class)

public class ReqOpenDeviBossPanelHandler extends Handler<ReqOpenDeviBossPanel> {

    static final Logger log = LogManager.getLogger(ReqOpenDeviBossPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenDeviBossPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.devilSeriesManager.getScript().onReqOpenDeviBossPanel(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenDeviBossPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
