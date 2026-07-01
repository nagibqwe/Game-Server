package com.game.commercialize.handler;

import com.game.commercialize.inter.ICommercialize;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommercializeMessage;
import game.message.CommercializeMessage.ReqCommercialize;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求商业化数据
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCommercialize.MsgID.eMsgID_VALUE, clazz = ReqCommercialize.class)

public class ReqCommercializeHandler extends Handler<ReqCommercialize> {

    static final Logger log = LogManager.getLogger(ReqCommercializeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCommercialize message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            ICommercialize script = Manager.commercializeManager.getScript(CommercializeMessage.Commercialize.valueOf(message.getTyp()));
            if (script != null)
                script.onReqCommercialize(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCommercializeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
