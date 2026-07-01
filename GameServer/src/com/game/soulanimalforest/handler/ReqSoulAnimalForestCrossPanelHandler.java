package com.game.soulanimalforest.handler;

import com.game.player.structs.Player;
import com.game.soulanimalforest.manager.SoulAnimalForestCrossManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.ReqSoulAnimalForestCrossPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服魂兽森林的界面
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSoulAnimalForestCrossPanel.MsgID.eMsgID_VALUE, clazz = ReqSoulAnimalForestCrossPanel.class)

public class ReqSoulAnimalForestCrossPanelHandler extends Handler<ReqSoulAnimalForestCrossPanel> {

    static final Logger log = LogManager.getLogger(ReqSoulAnimalForestCrossPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSoulAnimalForestCrossPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player == null) {
                return;
            }
            SoulAnimalForestCrossManager.getInstance().manager().onReqSoulAnimalForestCrossPanel(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSoulAnimalForestCrossPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
