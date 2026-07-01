package com.game.bi.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BIMessage.ReqBi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 具体的BI数据，内部数据格式由对接人协商负责
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqBi.MsgID.eMsgID_VALUE, clazz = ReqBi.class)

public class ReqBiHandler extends Handler<ReqBi> {

    static final Logger log = LogManager.getLogger(ReqBiHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqBi message) {
        try {
            long start = TimeUtils.Time();

            Manager.biManager.getScript().onReqBi((Player) session.getExecutor(), message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBiHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
