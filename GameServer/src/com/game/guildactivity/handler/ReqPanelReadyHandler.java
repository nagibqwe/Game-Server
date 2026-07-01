package com.game.guildactivity.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildActivityMessage.ReqPanelReady;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //面板就绪
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqPanelReady.MsgID.eMsgID_VALUE, clazz = ReqPanelReady.class)

public class ReqPanelReadyHandler extends Handler<ReqPanelReady> {

    static final Logger log = LogManager.getLogger(ReqPanelReadyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqPanelReady message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.guildActivityManager.deal().panelReady(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
