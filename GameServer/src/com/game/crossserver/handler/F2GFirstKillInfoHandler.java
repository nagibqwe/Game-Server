package com.game.crossserver.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GFirstKillInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服boss击杀数据
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GFirstKillInfo.MsgID.eMsgID_VALUE, clazz = F2GFirstKillInfo.class)

public class F2GFirstKillInfoHandler extends Handler<F2GFirstKillInfo> {

    static final Logger log = LogManager.getLogger(F2GFirstKillInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GFirstKillInfo message) {
        try {
            long start = TimeUtils.Time();

            Manager.openServerAcManager.deal().onKillMonster(message.getModelId(), message.getRoleIdList(), message.getRoleNameList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GFirstKillInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
