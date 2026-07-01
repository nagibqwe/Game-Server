package com.game.dailyactive.handler;

import com.game.player.structs.Player;
import com.game.utils.MessageUtils;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DailyactiveMessage;
import game.message.DailyactiveMessage.ReqCrossServerMatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求跨服 服务器分组数据
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCrossServerMatch.MsgID.eMsgID_VALUE, clazz = ReqCrossServerMatch.class)

public class ReqCrossServerMatchHandler extends Handler<ReqCrossServerMatch> {

    static final Logger log = LogManager.getLogger(ReqCrossServerMatchHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCrossServerMatch message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            DailyactiveMessage.G2PReqCrossServerMatch.Builder msg = DailyactiveMessage.G2PReqCrossServerMatch.newBuilder();
            msg.setRoleid(player.getId());
            MessageUtils.send_to_public(DailyactiveMessage.G2PReqCrossServerMatch.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
