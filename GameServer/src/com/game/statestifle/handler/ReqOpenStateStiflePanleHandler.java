package com.game.statestifle.handler;

import com.game.player.structs.Player;
import com.game.statestifle.manager.StateStifleManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateStifleMessage.ReqOpenStateStiflePanle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求打开灵压升级面板
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenStateStiflePanle.MsgID.eMsgID_VALUE, clazz = ReqOpenStateStiflePanle.class)

public class ReqOpenStateStiflePanleHandler extends Handler<ReqOpenStateStiflePanle> {

    static final Logger log = LogManager.getLogger(ReqOpenStateStiflePanleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenStateStiflePanle messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            StateStifleManager.getInstance().deal().openPanel(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenStateStiflePanleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
