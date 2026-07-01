package com.game.guild.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildMessage.ReqRecommendGuild;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求公会列表
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqRecommendGuild.MsgID.eMsgID_VALUE, clazz = ReqRecommendGuild.class)

public class ReqRecommendGuildHandler extends Handler<ReqRecommendGuild> {

    static final Logger log = LogManager.getLogger(ReqRecommendGuildHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqRecommendGuild message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.guildsManager.manager().reqGuildList(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
