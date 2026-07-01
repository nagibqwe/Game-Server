package com.game.fight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.FightMessage.ReqChangeAttackDir;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //攻击转方向
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqChangeAttackDir.MsgID.eMsgID_VALUE, clazz = ReqChangeAttackDir.class)

public class ReqChangeAttackDirHandler extends Handler<ReqChangeAttackDir> {

    static final Logger log = LogManager.getLogger(ReqChangeAttackDirHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqChangeAttackDir message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.fightManager.deal().OnReqChangeAttackDir(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
