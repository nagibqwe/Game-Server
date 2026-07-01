package com.game.guildbattle.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GuildBattleMessage.ReqSendBulletScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //发送弹幕
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSendBulletScreen.MsgID.eMsgID_VALUE, clazz = ReqSendBulletScreen.class)

public class ReqSendBulletScreenHandler extends Handler<ReqSendBulletScreen> {

    static final Logger log = LogManager.getLogger(ReqSendBulletScreenHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSendBulletScreen messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            Manager.guildBattleManager.manager().reqSendBulletScreen(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSendBulletScreenHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
