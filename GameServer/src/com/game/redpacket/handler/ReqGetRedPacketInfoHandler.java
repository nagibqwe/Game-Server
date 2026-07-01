package com.game.redpacket.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RedPacketMessage.ReqGetRedPacketInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //查看红包详细
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetRedPacketInfo.MsgID.eMsgID_VALUE, clazz = ReqGetRedPacketInfo.class)

public class ReqGetRedPacketInfoHandler extends Handler<ReqGetRedPacketInfo> {

    static final Logger log = LogManager.getLogger(ReqGetRedPacketInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetRedPacketInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player == null) {
                return;
            }
            Manager.redPacketManager.getScript().OnReqGetRedPacketInfo(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetRedPacketInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
