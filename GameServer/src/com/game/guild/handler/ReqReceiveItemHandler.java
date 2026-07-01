package com.game.guild.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildMessage.ReqReceiveItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //领工资
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqReceiveItem.MsgID.eMsgID_VALUE, clazz = ReqReceiveItem.class)

public class ReqReceiveItemHandler extends Handler<ReqReceiveItem> {

    static final Logger log = LogManager.getLogger(ReqReceiveItemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqReceiveItem message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.guildsManager.manager().reqGetItem(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
