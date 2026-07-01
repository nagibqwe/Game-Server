package com.game.kaoshangling.handler;

import com.game.kaoshangling.manager.KaoShangLingManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.KaoShangLingMessage.ReqOpenKaoShangLingPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //打开犒赏令
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqOpenKaoShangLingPanel.MsgID.eMsgID_VALUE, clazz = ReqOpenKaoShangLingPanel.class)

public class ReqOpenKaoShangLingPanelHandler extends Handler<ReqOpenKaoShangLingPanel> {

    static final Logger log = LogManager.getLogger(ReqOpenKaoShangLingPanelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqOpenKaoShangLingPanel message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            KaoShangLingManager.getInstance().deal().reqOpenKaoShangLingPanelHandler(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
