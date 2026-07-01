package com.game.heart.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.heartMessage.ReqSetReconnectSignSuccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //设置断线重联验证sign值成功
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqSetReconnectSignSuccess.MsgID.eMsgID_VALUE, clazz = ReqSetReconnectSignSuccess.class)

public class ReqSetReconnectSignSuccessHandler extends Handler<ReqSetReconnectSignSuccess> {

    static final Logger log = LogManager.getLogger(ReqSetReconnectSignSuccessHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqSetReconnectSignSuccess message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.heartManager.setReconnectSignSuccess(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
