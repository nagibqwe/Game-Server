package com.game.guild.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildMessage.ReqQuitGuild;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //退出公会
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqQuitGuild.MsgID.eMsgID_VALUE, clazz = ReqQuitGuild.class)

public class ReqQuitGuildHandler extends Handler<ReqQuitGuild> {

    static final Logger log = LogManager.getLogger(ReqQuitGuildHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqQuitGuild message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.guildsManager.manager().leaveGuild(player, 0);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
