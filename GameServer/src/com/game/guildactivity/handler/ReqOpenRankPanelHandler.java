package com.game.guildactivity.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildActivityMessage.ReqOpenRankPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //打开福地称号面板请求数据
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqOpenRankPanel.MsgID.eMsgID_VALUE, clazz = ReqOpenRankPanel.class)

public class ReqOpenRankPanelHandler extends Handler<ReqOpenRankPanel> {

    static final Logger log = LogManager.getLogger(ReqOpenRankPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqOpenRankPanel message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();

            Manager.guildActivityManager.deal().openRankPanel(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
