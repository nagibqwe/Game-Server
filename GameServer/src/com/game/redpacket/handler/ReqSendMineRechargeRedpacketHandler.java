package com.game.redpacket.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RedPacketMessage.ReqSendMineRechargeRedpacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //将自己充值获得的红包转发送到公会中
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSendMineRechargeRedpacket.MsgID.eMsgID_VALUE, clazz = ReqSendMineRechargeRedpacket.class)

public class ReqSendMineRechargeRedpacketHandler extends Handler<ReqSendMineRechargeRedpacket> {

    static final Logger log = LogManager.getLogger(ReqSendMineRechargeRedpacketHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSendMineRechargeRedpacket messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player == null) {
                return;
            }
            Manager.redPacketManager.getScript().OnReqSendMineRechargeRedpacket(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSendMineRechargeRedpacketHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
