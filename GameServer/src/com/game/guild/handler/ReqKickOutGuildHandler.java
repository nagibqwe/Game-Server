package com.game.guild.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildMessage.ReqKickOutGuild;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //踢出公会
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqKickOutGuild.MsgID.eMsgID_VALUE, clazz = ReqKickOutGuild.class)

public class ReqKickOutGuildHandler extends Handler<ReqKickOutGuild> {

    static final Logger log = LogManager.getLogger(ReqKickOutGuildHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqKickOutGuild message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.guildsManager.manager().leaveGuild(player, message.getRoleId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
