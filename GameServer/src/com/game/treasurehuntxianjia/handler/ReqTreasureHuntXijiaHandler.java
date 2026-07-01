package com.game.treasurehuntxianjia.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TreasureHuntXianjiaMessage.ReqTreasureHuntXijia;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求仙甲寻宝
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqTreasureHuntXijia.MsgID.eMsgID_VALUE, clazz = ReqTreasureHuntXijia.class)

public class ReqTreasureHuntXijiaHandler extends Handler<ReqTreasureHuntXijia> {

    static final Logger log = LogManager.getLogger(ReqTreasureHuntXijiaHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqTreasureHuntXijia messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.treasureHuntXianjiaManager.deal().onReqTreasureHuntXijia(player,messInfo.getType(),messInfo.getTimes());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqTreasureHuntXijiaHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
