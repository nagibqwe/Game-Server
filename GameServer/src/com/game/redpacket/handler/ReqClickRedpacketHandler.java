package com.game.redpacket.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RedPacketMessage.ReqClickRedpacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //抢红包
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqClickRedpacket.MsgID.eMsgID_VALUE, clazz = ReqClickRedpacket.class)

public class ReqClickRedpacketHandler extends Handler<ReqClickRedpacket> {

    static final Logger log = LogManager.getLogger(ReqClickRedpacketHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqClickRedpacket messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player == null) {
                return;
            }
            Manager.redPacketManager.getScript().OnReqClickRedpacket(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqClickRedpacketHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
