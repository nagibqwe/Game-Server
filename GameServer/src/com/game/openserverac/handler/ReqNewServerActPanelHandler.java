package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqNewServerActPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求新服活动面板数据
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqNewServerActPanel.MsgID.eMsgID_VALUE, clazz = ReqNewServerActPanel.class)

public class ReqNewServerActPanelHandler extends Handler<ReqNewServerActPanel> {

    static final Logger log = LogManager.getLogger(ReqNewServerActPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqNewServerActPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.openServerAcManager.deal().sendNewServerActInfo(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqNewServerActPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
