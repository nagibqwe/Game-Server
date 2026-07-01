package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.utils.MessageUtils;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage;
import game.message.CouplefightMessage.ReqGroupPrepareMapEnter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入准备地图
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGroupPrepareMapEnter.MsgID.eMsgID_VALUE, clazz = ReqGroupPrepareMapEnter.class)

public class ReqGroupPrepareMapEnterHandler extends Handler<ReqGroupPrepareMapEnter> {

    static final Logger log = LogManager.getLogger(ReqGroupPrepareMapEnterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGroupPrepareMapEnter messInfo) {
        try {
            long start = TimeUtils.Time();

            Player p = mess.getExecutor();
            Manager.couplefightManager.getScript().reqGroupPrepareMapEnter(p);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGroupPrepareMapEnterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
