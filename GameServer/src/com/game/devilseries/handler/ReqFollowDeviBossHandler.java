package com.game.devilseries.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DevilSeriesMessage.ReqFollowDeviBoss;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求关注除魔团boss
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqFollowDeviBoss.MsgID.eMsgID_VALUE, clazz = ReqFollowDeviBoss.class)

public class ReqFollowDeviBossHandler extends Handler<ReqFollowDeviBoss> {

    static final Logger log = LogManager.getLogger(ReqFollowDeviBossHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqFollowDeviBoss messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.devilSeriesManager.getScript().onReqFollowDeviBoss(player,messInfo.getCloneId(),messInfo.getFollowValue());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFollowDeviBossHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
