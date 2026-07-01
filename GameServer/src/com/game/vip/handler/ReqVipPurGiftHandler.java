package com.game.vip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.VipMessage.ReqVipPurGift;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求购买等级礼包
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqVipPurGift.MsgID.eMsgID_VALUE, clazz = ReqVipPurGift.class)

public class ReqVipPurGiftHandler extends Handler<ReqVipPurGift> {

    static final Logger log = LogManager.getLogger(ReqVipPurGiftHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqVipPurGift messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.vipManager.deal().purVipReward(player, messInfo.getLv());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqVipPurGiftHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
