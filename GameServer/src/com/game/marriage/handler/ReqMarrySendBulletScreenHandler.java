package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarrySendBulletScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //发送弹幕
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarrySendBulletScreen.MsgID.eMsgID_VALUE, clazz = ReqMarrySendBulletScreen.class)

public class ReqMarrySendBulletScreenHandler extends Handler<ReqMarrySendBulletScreen> {

    static final Logger log = LogManager.getLogger(ReqMarrySendBulletScreenHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarrySendBulletScreen messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.manager().reqMarrySendBulletScreen(player,messInfo.getContext());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarrySendBulletScreenHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
