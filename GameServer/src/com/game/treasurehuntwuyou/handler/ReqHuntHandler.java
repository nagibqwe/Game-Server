package com.game.treasurehuntwuyou.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TreasureHuntWuyouMessage.ReqHunt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求抽奖
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqHunt.MsgID.eMsgID_VALUE, clazz = ReqHunt.class)

public class ReqHuntHandler extends Handler<ReqHunt> {

    static final Logger log = LogManager.getLogger(ReqHuntHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqHunt messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.treasureHuntWuyouManager.getScript().reqHunt((Player) mess.getExecutor(), messInfo.getNum());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqHuntHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
