package com.game.ranklist.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RankListMessage.ReqUniverseRankPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 请求天墟战场名人堂
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUniverseRankPanel.MsgID.eMsgID_VALUE, clazz = ReqUniverseRankPanel.class)

public class ReqUniverseRankPanelHandler extends Handler<ReqUniverseRankPanel> {

    static final Logger log = LogManager.getLogger(ReqUniverseRankPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUniverseRankPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.rankListManager.getUniverseRankScript().onReqUniverseRankPanel(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUniverseRankPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
