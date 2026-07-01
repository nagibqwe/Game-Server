package com.game.friend.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.friendMessage.ReqGetRelationList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求好友列表
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGetRelationList.MsgID.eMsgID_VALUE, clazz = ReqGetRelationList.class)

public class ReqGetRelationListHandler extends Handler<ReqGetRelationList> {

    static final Logger log = LogManager.getLogger(ReqGetRelationListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGetRelationList message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.friendManager.deal().getRelationList(player, message.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
