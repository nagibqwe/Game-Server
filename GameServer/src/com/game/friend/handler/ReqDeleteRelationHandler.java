package com.game.friend.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.friendMessage.ReqDeleteRelation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //删除关系
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqDeleteRelation.MsgID.eMsgID_VALUE, clazz = ReqDeleteRelation.class)

public class ReqDeleteRelationHandler extends Handler<ReqDeleteRelation> {

    static final Logger log = LogManager.getLogger(ReqDeleteRelationHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqDeleteRelation message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.friendManager.deal().deleteRelation(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
