package com.game.couplefight.handler;

import com.game.manager.Manager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.G2PReqGroupPrepareMapEnter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //游戏服到公共服-请求进入准备地图
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = G2PReqGroupPrepareMapEnter.MsgID.eMsgID_VALUE, clazz = G2PReqGroupPrepareMapEnter.class)

public class G2PReqGroupPrepareMapEnterHandler extends Handler<G2PReqGroupPrepareMapEnter> {

    static final Logger log = LogManager.getLogger(G2PReqGroupPrepareMapEnterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, G2PReqGroupPrepareMapEnter messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqGroupPrepareMapEnter(mess.getContext(), messInfo.getRid());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("G2PReqGroupPrepareMapEnterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
