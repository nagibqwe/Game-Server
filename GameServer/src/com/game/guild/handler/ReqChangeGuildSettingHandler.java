package com.game.guild.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildMessage.ReqChangeGuildSetting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //改变公会设置信息
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqChangeGuildSetting.MsgID.eMsgID_VALUE, clazz = ReqChangeGuildSetting.class)

public class ReqChangeGuildSettingHandler extends Handler<ReqChangeGuildSetting> {

    static final Logger log = LogManager.getLogger(ReqChangeGuildSettingHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqChangeGuildSetting message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.guildsManager.manager().reqChangeGuildSetting(player,
                    message.getIsAutoApply(), message.getLv(), message.getFightPoint(), message.getIcon(), message.getNotice());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
