package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage.ReqRetrieveRes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求找回
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRetrieveRes.MsgID.eMsgID_VALUE, clazz = ReqRetrieveRes.class)

public class ReqRetrieveResHandler extends Handler<ReqRetrieveRes> {

    static final Logger log = LogManager.getLogger(ReqRetrieveResHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRetrieveRes messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.retrieveResManager.getScript().onReqRetrieveRes((Player) mess.getExecutor(),
                    messInfo.getType(), messInfo.getRrType(), messInfo.getCount(), false);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRetrieveResHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
