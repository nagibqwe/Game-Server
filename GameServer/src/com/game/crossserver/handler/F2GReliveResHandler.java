package com.game.crossserver.handler;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossServerMessage.F2GReliveRes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服通知玩家复活情况
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = F2GReliveRes.MsgID.eMsgID_VALUE, clazz = F2GReliveRes.class)

public class F2GReliveResHandler extends Handler<F2GReliveRes> {

    static final Logger log = LogManager.getLogger(F2GReliveResHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, F2GReliveRes message) {
        try {
            long start = TimeUtils.Time();

            Manager.playerManager.deal(ScriptEnum.PlayerReliveBaseScript).F2GReliveResHandler(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("F2GReliveResHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
