package com.game.welfare.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.welfare.script.IDayGiftScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqDayGiftRecharge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求充值购买每日礼包
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDayGiftRecharge.MsgID.eMsgID_VALUE, clazz = ReqDayGiftRecharge.class)

public class ReqDayGiftRechargeHandler extends Handler<ReqDayGiftRecharge> {

    static final Logger log = LogManager.getLogger(ReqDayGiftRechargeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDayGiftRecharge messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            IDayGiftScript script = (IDayGiftScript) Manager.welfareManager.getScript(WelfareMessage.WelfareType.DayGift);
            if (script != null)
                script.onReqDayGiftRecharge(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDayGiftRechargeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
