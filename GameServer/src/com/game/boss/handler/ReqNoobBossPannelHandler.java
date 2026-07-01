package com.game.boss.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.BossMessage.ReqNoobBossPannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求新手层boss
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqNoobBossPannel.MsgID.eMsgID_VALUE, clazz = ReqNoobBossPannel.class)

public class ReqNoobBossPannelHandler extends Handler<ReqNoobBossPannel> {

    static final Logger log = LogManager.getLogger(ReqNoobBossPannelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqNoobBossPannel message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.scriptManager.GetScriptClass(ScriptEnum.NoodBossMapScript).call(player, "onReqNoobBossPannel");

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqNoobBossPannelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
