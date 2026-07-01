package com.game.redpacket.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RedPacketMessage.ReqSendRedpacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //发红包
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSendRedpacket.MsgID.eMsgID_VALUE, clazz = ReqSendRedpacket.class)

public class ReqSendRedpacketHandler extends Handler<ReqSendRedpacket> {

    static final Logger log = LogManager.getLogger(ReqSendRedpacketHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSendRedpacket messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player == null) {
                return;
            }
            Manager.redPacketManager.getScript().OnReqSendRedpacket(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSendRedpacketHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
