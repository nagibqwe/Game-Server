package com.game.team.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TeamMessage.ReqGetWaitList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取等待的队伍列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetWaitList.MsgID.eMsgID_VALUE, clazz = ReqGetWaitList.class)

public class ReqGetWaitListHandler extends Handler<ReqGetWaitList> {

    static final Logger log = LogManager.getLogger(ReqGetWaitListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetWaitList messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.teamManager.deal().reqGetWaitListHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetWaitListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
