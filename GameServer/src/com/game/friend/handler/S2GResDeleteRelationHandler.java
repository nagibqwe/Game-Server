package com.game.friend.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.friendMessage.S2GResDeleteRelation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服 游戏服 到 社交服 删除关系
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = S2GResDeleteRelation.MsgID.eMsgID_VALUE, clazz = S2GResDeleteRelation.class)

public class S2GResDeleteRelationHandler extends Handler<S2GResDeleteRelation> {

    static final Logger log = LogManager.getLogger(S2GResDeleteRelationHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, S2GResDeleteRelation messInfo) {
        try {
            long start = TimeUtils.Time();
            Manager.friendManager.cross().S2GResDeleteRelation(messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("S2GResDeleteRelationHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
