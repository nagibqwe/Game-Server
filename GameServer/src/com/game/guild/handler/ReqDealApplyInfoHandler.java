package com.game.guild.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildMessage.ReqDealApplyInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //处理某玩家入会申请
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqDealApplyInfo.MsgID.eMsgID_VALUE, clazz = ReqDealApplyInfo.class)

public class ReqDealApplyInfoHandler extends Handler<ReqDealApplyInfo> {

    static final Logger log = LogManager.getLogger(ReqDealApplyInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqDealApplyInfo message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.guildsManager.reqApplyDelGuild(player, message.getRoleIdList(), message.getAgree());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
