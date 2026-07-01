package com.game.guildbattle.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildBattleMessage.ReqGuildBattleBack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求回城
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGuildBattleBack.MsgID.eMsgID_VALUE, clazz = ReqGuildBattleBack.class)

public class ReqGuildBattleBackHandler extends Handler<ReqGuildBattleBack> {

    static final Logger log = LogManager.getLogger(ReqGuildBattleBackHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGuildBattleBack message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.guildBattleManager.manager().reqGuildBattleBack(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
