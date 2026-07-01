package com.game.eightdiagrams.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EightDiagramsMessage.ReqEightDiagramsPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //打开八卦阵页面
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqEightDiagramsPanel.MsgID.eMsgID_VALUE, clazz = ReqEightDiagramsPanel.class)

public class ReqEightDiagramsPanelHandler extends Handler<ReqEightDiagramsPanel> {

    static final Logger log = LogManager.getLogger(ReqEightDiagramsPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqEightDiagramsPanel message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.eightDiagramsManager.deal().ReqEightDiagramsPanel(player,message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
