package com.game.friend.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.friendMessage.ReqDimSelect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //模糊查询
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqDimSelect.MsgID.eMsgID_VALUE, clazz = ReqDimSelect.class)

public class ReqDimSelectHandler extends Handler<ReqDimSelect> {

    static final Logger log = LogManager.getLogger(ReqDimSelectHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqDimSelect message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.friendManager.deal().dimSelect(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
