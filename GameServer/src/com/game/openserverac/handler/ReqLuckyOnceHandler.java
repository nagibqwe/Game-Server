package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqLuckyOnce;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //幸运翻牌翻一次
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqLuckyOnce.MsgID.eMsgID_VALUE, clazz = ReqLuckyOnce.class)

public class ReqLuckyOnceHandler extends Handler<ReqLuckyOnce> {

    static final Logger log = LogManager.getLogger(ReqLuckyOnceHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqLuckyOnce messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.openServerAcManager.deal().luckyOnce(player, messInfo.getCellId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqLuckyOnceHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
