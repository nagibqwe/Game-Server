package com.game.guild.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildMessage.ReqCreateGuild;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求创建公会
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCreateGuild.MsgID.eMsgID_VALUE, clazz = ReqCreateGuild.class)

public class ReqCreateGuildHandler extends Handler<ReqCreateGuild> {

    static final Logger log = LogManager.getLogger(ReqCreateGuildHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCreateGuild message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.guildsManager.createGuild(player, message.getName(), message.getIcon(), message.getNotice());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
