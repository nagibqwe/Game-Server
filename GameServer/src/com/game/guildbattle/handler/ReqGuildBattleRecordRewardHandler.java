package com.game.guildbattle.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildBattleMessage.ReqGuildBattleRecordReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取终结或者连胜奖励
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGuildBattleRecordReward.MsgID.eMsgID_VALUE, clazz = ReqGuildBattleRecordReward.class)

public class ReqGuildBattleRecordRewardHandler extends Handler<ReqGuildBattleRecordReward> {

    static final Logger log = LogManager.getLogger(ReqGuildBattleRecordRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGuildBattleRecordReward message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.guildBattleManager.manager().reqGuildBattleRecordReward(player, message.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
