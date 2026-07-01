package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqPurInvitNum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //购买邀请人数
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqPurInvitNum.MsgID.eMsgID_VALUE, clazz = ReqPurInvitNum.class)

public class ReqPurInvitNumHandler extends Handler<ReqPurInvitNum> {

    static final Logger log = LogManager.getLogger(ReqPurInvitNumHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqPurInvitNum messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.reqPurInvitNum(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqPurInvitNumHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
