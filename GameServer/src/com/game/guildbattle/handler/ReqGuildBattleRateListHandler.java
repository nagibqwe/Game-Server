package com.game.guildbattle.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildBattleMessage.ReqGuildBattleRateList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求仙盟争霸评级列表
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGuildBattleRateList.MsgID.eMsgID_VALUE, clazz = ReqGuildBattleRateList.class)

public class ReqGuildBattleRateListHandler extends Handler<ReqGuildBattleRateList> {

    static final Logger log = LogManager.getLogger(ReqGuildBattleRateListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGuildBattleRateList message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.guildBattleManager.manager().reqGuildBattleRate(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
