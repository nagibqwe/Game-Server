package com.game.cangbaoge.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CangbaogeMessage.ReqCangbaogeLottery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //抽奖
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCangbaogeLottery.MsgID.eMsgID_VALUE, clazz = ReqCangbaogeLottery.class)

public class ReqCangbaogeLotteryHandler extends Handler<ReqCangbaogeLottery> {

    static final Logger log = LogManager.getLogger(ReqCangbaogeLotteryHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCangbaogeLottery message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.cangbaogeManager.deal().ReqCangbaogeLottery(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCangbaogeLotteryHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
