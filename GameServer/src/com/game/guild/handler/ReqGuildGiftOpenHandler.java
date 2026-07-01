package com.game.guild.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildMessage.ReqGuildGiftOpen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领取仙盟宝箱奖励
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGuildGiftOpen.MsgID.eMsgID_VALUE, clazz = ReqGuildGiftOpen.class)

public class ReqGuildGiftOpenHandler extends Handler<ReqGuildGiftOpen> {

    static final Logger log = LogManager.getLogger(ReqGuildGiftOpenHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGuildGiftOpen messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            Manager.guildsManager.manager().reqGuildGiftOpen(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGuildGiftOpenHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
