package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqKillMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求飞剑击杀怪物
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqKillMonster.MsgID.eMsgID_VALUE, clazz = ReqKillMonster.class)

public class ReqKillMonsterHandler extends Handler<ReqKillMonster> {

    static final Logger log = LogManager.getLogger(ReqKillMonsterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqKillMonster message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();

            Manager.scriptManager.GetScriptClass(ScriptEnum.StarCopyActivityScript).call(player, "killMonster", message.getMonsterId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqKillMonsterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
