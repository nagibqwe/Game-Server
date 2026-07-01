package com.game.friend.handler;

import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.friendMessage.G2SReqAddRelation;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //跨服 游戏服 到 社交服 添加关系
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2SReqAddRelation.MsgID.eMsgID_VALUE, clazz = G2SReqAddRelation.class)

public class G2SReqAddRelationHandler extends Handler<G2SReqAddRelation> {

    static final Logger log = LogManager.getLogger(G2SReqAddRelationHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2SReqAddRelation messInfo) {
        try {
            long start = TimeUtils.Time();
            ChannelHandlerContext channel = mess.getContext();
            GlobalPlayerWorldInfo player = mess.getExecutor();
            Manager.friendManager.deal().G2SReqAddRelationHandler(player, messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2SReqAddRelationHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
