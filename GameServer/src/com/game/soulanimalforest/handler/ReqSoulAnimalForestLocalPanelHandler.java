package com.game.soulanimalforest.handler;

import com.game.player.structs.Player;
import com.game.soulanimalforest.manager.SoulAnimalForestCrossManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.ReqSoulAnimalForestLocalPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //本地魂兽森林的界面
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSoulAnimalForestLocalPanel.MsgID.eMsgID_VALUE, clazz = ReqSoulAnimalForestLocalPanel.class)

public class ReqSoulAnimalForestLocalPanelHandler extends Handler<ReqSoulAnimalForestLocalPanel> {

    static final Logger log = LogManager.getLogger(ReqSoulAnimalForestLocalPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSoulAnimalForestLocalPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            SoulAnimalForestCrossManager.getInstance().manager().onReqSoulAnimalForestLocalPanel(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSoulAnimalForestLocalPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
