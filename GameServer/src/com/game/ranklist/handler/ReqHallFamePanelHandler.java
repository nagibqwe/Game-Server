package com.game.ranklist.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RankListMessage.ReqHallFamePanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 请求名人堂
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqHallFamePanel.MsgID.eMsgID_VALUE, clazz = ReqHallFamePanel.class)

public class ReqHallFamePanelHandler extends Handler<ReqHallFamePanel> {

    static final Logger log = LogManager.getLogger(ReqHallFamePanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqHallFamePanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.rankListManager.getTopHallRankScript().onReqTopHallRankPanel(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqHallFamePanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
