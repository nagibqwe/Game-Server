package com.game.command.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CommandMessage.ReqCommandBulletScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //发送弹幕
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCommandBulletScreen.MsgID.eMsgID_VALUE, clazz = ReqCommandBulletScreen.class)

public class ReqCommandBulletScreenHandler extends Handler<ReqCommandBulletScreen> {

    static final Logger log = LogManager.getLogger(ReqCommandBulletScreenHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCommandBulletScreen message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.commandManager.deal().onReqCommandBulletScreen(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCommandBulletScreenHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
