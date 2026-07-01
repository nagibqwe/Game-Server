package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqGetLuckyLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //查看幸运翻牌抽奖记录请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetLuckyLog.MsgID.eMsgID_VALUE, clazz = ReqGetLuckyLog.class)

public class ReqGetLuckyLogHandler extends Handler<ReqGetLuckyLog> {

    static final Logger log = LogManager.getLogger(ReqGetLuckyLogHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetLuckyLog messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.openServerAcManager.deal().getLuckyHistroy(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetLuckyLogHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
