package com.game.vip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.VipMessage.ReqVip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求vip
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqVip.MsgID.eMsgID_VALUE, clazz = ReqVip.class)

public class ReqVipHandler extends Handler<ReqVip> {

    static final Logger log = LogManager.getLogger(ReqVipHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqVip messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.vipManager.deal().getVipInfo(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqVipHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
