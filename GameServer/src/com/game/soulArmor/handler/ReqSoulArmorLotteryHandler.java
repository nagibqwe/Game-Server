package com.game.soulArmor.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulArmorMessage.ReqSoulArmorLottery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //魂甲抽奖
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSoulArmorLottery.MsgID.eMsgID_VALUE, clazz = ReqSoulArmorLottery.class)

public class ReqSoulArmorLotteryHandler extends Handler<ReqSoulArmorLottery> {

    static final Logger log = LogManager.getLogger(ReqSoulArmorLotteryHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSoulArmorLottery messInfo) {
        try {
            long start = TimeUtils.Time();


            Manager.soulArmorManager.script().reqSoulArmorLottery(mess.getExecutor(), messInfo.getType(), messInfo.getCount(), messInfo.getGold());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSoulArmorLotteryHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
