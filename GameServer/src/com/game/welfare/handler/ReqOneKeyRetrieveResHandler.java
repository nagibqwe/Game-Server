package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage.ReqOneKeyRetrieveRes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求一键找回
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOneKeyRetrieveRes.MsgID.eMsgID_VALUE, clazz = ReqOneKeyRetrieveRes.class)

public class ReqOneKeyRetrieveResHandler extends Handler<ReqOneKeyRetrieveRes> {

    static final Logger log = LogManager.getLogger(ReqOneKeyRetrieveResHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOneKeyRetrieveRes messInfo) {
        try {
            long start = TimeUtils.Time();
            Manager.retrieveResManager.getScript().onReqOneKeyRetrieveRes((Player) mess.getExecutor(),
                    messInfo.getRrType(), messInfo.getBaseTpe());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOneKeyRetrieveResHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
