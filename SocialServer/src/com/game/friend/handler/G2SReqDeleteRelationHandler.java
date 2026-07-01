package com.game.friend.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.friendMessage.G2SReqDeleteRelation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服 游戏服 到 社交服 删除关系
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SReqDeleteRelation.MsgID.eMsgID_VALUE, clazz = G2SReqDeleteRelation.class)

public class G2SReqDeleteRelationHandler extends Handler<G2SReqDeleteRelation> {

    static final Logger log = LogManager.getLogger(G2SReqDeleteRelationHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SReqDeleteRelation messInfo) {
        try {
            long start = TimeUtils.Time();
            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.friendManager.deal().G2SReqDeleteRelationHandler(player, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SReqDeleteRelationHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
