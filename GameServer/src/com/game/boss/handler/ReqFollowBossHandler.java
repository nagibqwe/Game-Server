package com.game.boss.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BossMessage.ReqFollowBoss;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求关注or取消关注boss
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqFollowBoss.MsgID.eMsgID_VALUE, clazz = ReqFollowBoss.class)

public class ReqFollowBossHandler extends Handler<ReqFollowBoss> {

    static final Logger log = LogManager.getLogger(ReqFollowBossHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqFollowBoss message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.bossManager.manager().reqFollowBoss(player, message.getBossId(), message.getType(), message.getBossType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqFollowBossHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
