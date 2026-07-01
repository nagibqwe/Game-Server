package com.game.crossrank.handler;

import com.game.player.structs.Player;
import com.game.utils.MessageUtils;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossRankMessage;
import game.message.CrossRankMessage.ReqCrossRankInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求跨服排行信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCrossRankInfo.MsgID.eMsgID_VALUE, clazz = ReqCrossRankInfo.class)

public class ReqCrossRankInfoHandler extends Handler<ReqCrossRankInfo> {

    static final Logger log = LogManager.getLogger(ReqCrossRankInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCrossRankInfo message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();

            CrossRankMessage.ReqG2PCrossRankInfo.Builder msg =  CrossRankMessage.ReqG2PCrossRankInfo.newBuilder();
            msg.setRoleId(player.getId());
            MessageUtils.send_to_public(CrossRankMessage.ReqG2PCrossRankInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCrossRankInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
