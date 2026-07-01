package com.game.activityRanklist.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ActivityRankListMessage.ReqActivityRankInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 请求排行榜数据
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqActivityRankInfo.MsgID.eMsgID_VALUE, clazz = ReqActivityRankInfo.class)

public class ReqActivityRankInfoHandler extends Handler<ReqActivityRankInfo> {

    static final Logger log = LogManager.getLogger(ReqActivityRankInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqActivityRankInfo message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.activityRankManager.deal().getActivityRankInfo(player, message.getRankKind());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActivityRankInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
