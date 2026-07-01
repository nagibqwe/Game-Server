package com.game.soulArmor.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SoulArmorMessage.ReqOpenSoulArmorLottery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //获取魂甲抽奖等级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqOpenSoulArmorLottery.MsgID.eMsgID_VALUE, clazz = ReqOpenSoulArmorLottery.class)

public class ReqOpenSoulArmorLotteryHandler extends Handler<ReqOpenSoulArmorLottery> {

    static final Logger log = LogManager.getLogger(ReqOpenSoulArmorLotteryHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqOpenSoulArmorLottery messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.soulArmorManager.script().reqOpenSoulArmorLottery(mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqOpenSoulArmorLotteryHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
