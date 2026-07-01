package com.game.team.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TeamMessage.ReqCleanApplyList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //清空申请列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCleanApplyList.MsgID.eMsgID_VALUE, clazz = ReqCleanApplyList.class)

public class ReqCleanApplyListHandler extends Handler<ReqCleanApplyList> {

    static final Logger log = LogManager.getLogger(ReqCleanApplyListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCleanApplyList messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.teamManager.deal().reqCleanApplyListHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCleanApplyListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
