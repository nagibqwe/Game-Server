package com.game.chat.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChatMessage.ReqRefuseShare;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //玩家拒绝了你的分享
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqRefuseShare.MsgID.eMsgID_VALUE, clazz = ReqRefuseShare.class)

public class ReqRefuseShareHandler extends Handler<ReqRefuseShare> {

    static final Logger log = LogManager.getLogger(ReqRefuseShareHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqRefuseShare message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            if (player == null || message.getShareId() < 1) {
                logger.error("ReqRefuseShareHandler parameter is null!");
                return;
            }
            Manager.shareManager.deal().onReqRefuseShare(player, message.getShareId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRefuseShareHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
