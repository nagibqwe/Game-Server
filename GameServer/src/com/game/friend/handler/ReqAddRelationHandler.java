package com.game.friend.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.friendMessage.ReqAddRelation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //添加关系
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqAddRelation.MsgID.eMsgID_VALUE, clazz = ReqAddRelation.class)

public class ReqAddRelationHandler extends Handler<ReqAddRelation> {

    static final Logger log = LogManager.getLogger(ReqAddRelationHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqAddRelation message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.friendManager.deal().addRelation(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
