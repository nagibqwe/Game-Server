package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqTrialsInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求海选赛信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqTrialsInfo.MsgID.eMsgID_VALUE, clazz = ReqTrialsInfo.class)

public class ReqTrialsInfoHandler extends Handler<ReqTrialsInfo> {

    static final Logger log = LogManager.getLogger(ReqTrialsInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqTrialsInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqCouplefightInfo((Player) mess.getExecutor(), 1);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqTrialsInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
