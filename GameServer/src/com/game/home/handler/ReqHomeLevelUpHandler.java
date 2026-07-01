package com.game.home.handler;

import com.game.home.manager.HomeManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.ReqHomeLevelUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //房屋升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqHomeLevelUp.MsgID.eMsgID_VALUE, clazz = ReqHomeLevelUp.class)

public class ReqHomeLevelUpHandler extends Handler<ReqHomeLevelUp> {

    static final Logger log = LogManager.getLogger(ReqHomeLevelUpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqHomeLevelUp messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            HomeManager.getInstance().deal().reqHomeLevelUp(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqHomeLevelUpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
