package com.game.guildbattle.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildBattleMessage.ReqGuildBattleResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求战绩结果
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGuildBattleResult.MsgID.eMsgID_VALUE, clazz = ReqGuildBattleResult.class)

public class ReqGuildBattleResultHandler extends Handler<ReqGuildBattleResult> {

    static final Logger log = LogManager.getLogger(ReqGuildBattleResultHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGuildBattleResult message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.guildBattleManager.manager().reqGuildBattleResult(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
