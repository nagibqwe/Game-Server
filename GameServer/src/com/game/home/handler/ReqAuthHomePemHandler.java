package com.game.home.handler;

import com.game.home.manager.HomeManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HomeMessage.ReqAuthHomePem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //设置房屋权限
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqAuthHomePem.MsgID.eMsgID_VALUE, clazz = ReqAuthHomePem.class)

public class ReqAuthHomePemHandler extends Handler<ReqAuthHomePem> {

    static final Logger log = LogManager.getLogger(ReqAuthHomePemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqAuthHomePem messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            HomeManager.getInstance().deal().reqAuthHomePemHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAuthHomePemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
