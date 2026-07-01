package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqOpenFirstKillPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求面板
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenFirstKillPanel.MsgID.eMsgID_VALUE, clazz = ReqOpenFirstKillPanel.class)

public class ReqOpenFirstKillPanelHandler extends Handler<ReqOpenFirstKillPanel> {

    static final Logger log = LogManager.getLogger(ReqOpenFirstKillPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenFirstKillPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.openServerAcManager.deal().onReqFirstKillPanel(player, false);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenFirstKillPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
