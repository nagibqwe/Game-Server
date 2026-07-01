package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqPeakLevelPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //打开界面
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPeakLevelPanel.MsgID.eMsgID_VALUE, clazz = ReqPeakLevelPanel.class)

public class ReqPeakLevelPanelHandler extends Handler<ReqPeakLevelPanel> {

    static final Logger log = LogManager.getLogger(ReqPeakLevelPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPeakLevelPanel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.deal(ScriptEnum.ChangeJobBaseScript).sendPlayerFateStar(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPeakLevelPanelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
