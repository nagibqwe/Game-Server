package com.game.vip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.VipMessage.ReqActiveVipPearl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求激活VIP宝珠
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqActiveVipPearl.MsgID.eMsgID_VALUE, clazz = ReqActiveVipPearl.class)

public class ReqActiveVipPearlHandler extends Handler<ReqActiveVipPearl> {

    static final Logger log = LogManager.getLogger(ReqActiveVipPearlHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqActiveVipPearl messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.vipManager.pearl().activeVipPearl(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActiveVipPearlHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
