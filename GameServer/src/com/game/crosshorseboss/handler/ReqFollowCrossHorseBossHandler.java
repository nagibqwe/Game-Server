package com.game.crosshorseboss.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossHorseBossMessage.ReqFollowCrossHorseBoss;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求是否关注跨服BOSS的关注
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqFollowCrossHorseBoss.MsgID.eMsgID_VALUE, clazz = ReqFollowCrossHorseBoss.class)

public class ReqFollowCrossHorseBossHandler extends Handler<ReqFollowCrossHorseBoss> {

    static final Logger log = LogManager.getLogger(ReqFollowCrossHorseBossHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqFollowCrossHorseBoss message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.crossHorseBossManager.deal().onReqFollowCrossHorseBoss(player,message.getFollowValue(),message.getBossId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFollowCrossHorseBossHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
