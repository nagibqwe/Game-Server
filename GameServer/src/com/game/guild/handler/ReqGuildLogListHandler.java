package com.game.guild.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildMessage.ReqGuildLogList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求公会log信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqGuildLogList.MsgID.eMsgID_VALUE, clazz = ReqGuildLogList.class)

public class ReqGuildLogListHandler extends Handler<ReqGuildLogList> {

    static final Logger log = LogManager.getLogger(ReqGuildLogListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqGuildLogList message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.guildsManager.manager().reqGuildLogList(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
