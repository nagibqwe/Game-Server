package com.game.friend.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.friendMessage.G2SReqGiveFriendShipPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //赠送跨服好友情义点请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SReqGiveFriendShipPoint.MsgID.eMsgID_VALUE, clazz = G2SReqGiveFriendShipPoint.class)

public class G2SReqGiveFriendShipPointHandler extends Handler<G2SReqGiveFriendShipPoint> {

    static final Logger log = LogManager.getLogger(G2SReqGiveFriendShipPointHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SReqGiveFriendShipPoint messInfo) {
        try {
            long start = TimeUtils.Time();
            Manager.friendManager.deal().G2SReqGiveFriendShipPoint( messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SReqGiveFriendShipPointHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
