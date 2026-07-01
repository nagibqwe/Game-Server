package com.game.redpacket.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RedPacketMessage.ReqRedpacketList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //查看红包列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqRedpacketList.MsgID.eMsgID_VALUE, clazz = ReqRedpacketList.class)

public class ReqRedpacketListHandler extends Handler<ReqRedpacketList> {

    static final Logger log = LogManager.getLogger(ReqRedpacketListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqRedpacketList messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player == null) {
                return;
            }
            Manager.redPacketManager.getScript().OnReqRedpacketList(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqRedpacketListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
