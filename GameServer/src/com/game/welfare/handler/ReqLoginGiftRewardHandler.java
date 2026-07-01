package com.game.welfare.handler;

import com.game.player.structs.Player;
import com.game.welfare.manager.WelfareManager;
import com.game.welfare.script.ILoginGiftScript;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import game.message.WelfareMessage.ReqLoginGiftReward;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求领取登陆礼包
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqLoginGiftReward.MsgID.eMsgID_VALUE, clazz = ReqLoginGiftReward.class)

public class ReqLoginGiftRewardHandler extends Handler<ReqLoginGiftReward> {

    static final Logger log = LogManager.getLogger(ReqLoginGiftRewardHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqLoginGiftReward messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            ILoginGiftScript script = (ILoginGiftScript) WelfareManager.getInstance().getScript(WelfareMessage.WelfareType.LoginGift);
            if (script != null)
                script.onReqLoginGiftReward(player, messInfo.getDay());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqLoginGiftRewardHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
