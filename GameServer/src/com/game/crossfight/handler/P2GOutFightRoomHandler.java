package com.game.crossfight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage.P2GOutFightRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //通知游戏服务器， 玩家不参加活动了
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = P2GOutFightRoom.MsgID.eMsgID_VALUE, clazz = P2GOutFightRoom.class)

public class P2GOutFightRoomHandler extends Handler<P2GOutFightRoom> {

    static final Logger log = LogManager.getLogger(P2GOutFightRoomHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, P2GOutFightRoom message) {
        try {
            long start = TimeUtils.Time();

            Manager.crossServerManager.crossFightdeal().OnP2GOutFightRoom(message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("P2GOutFightRoomHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
