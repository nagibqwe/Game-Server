package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqOpenChallengePanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //打开挑战副本面板请求数据
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqOpenChallengePanel.MsgID.eMsgID_VALUE, clazz = ReqOpenChallengePanel.class)

public class ReqOpenChallengePanelHandler extends Handler<ReqOpenChallengePanel> {

    static final Logger log = LogManager.getLogger(ReqOpenChallengePanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqOpenChallengePanel message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();

            Manager.copyMapManager.singleTower().sendSingleTowerInfo(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenChallengePanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
