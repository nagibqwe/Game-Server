package com.game.boss.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BossMessage.ReqCallBigR;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求召唤大R
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCallBigR.MsgID.eMsgID_VALUE, clazz = ReqCallBigR.class)

public class ReqCallBigRHandler extends Handler<ReqCallBigR> {

    static final Logger log = LogManager.getLogger(ReqCallBigRHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCallBigR message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.scriptManager.GetScriptClass(ScriptEnum.NoodBossMapScript).call(player, "onReqCallRobot");

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCallBigRHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
