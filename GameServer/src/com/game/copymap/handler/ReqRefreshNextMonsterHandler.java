package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqRefreshNextMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //心魔副本请求刷下一波怪
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqRefreshNextMonster.MsgID.eMsgID_VALUE, clazz = ReqRefreshNextMonster.class)

public class ReqRefreshNextMonsterHandler extends Handler<ReqRefreshNextMonster> {

    static final Logger log = LogManager.getLogger(ReqRefreshNextMonsterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqRefreshNextMonster message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();

            Manager.scriptManager.GetScriptClass(ScriptEnum.EquipCopyActivityScript).call(player, "refreshMonster");

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRefreshNextMonsterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
