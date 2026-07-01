package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqGroupsInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求小组赛主界面信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGroupsInfo.MsgID.eMsgID_VALUE, clazz = ReqGroupsInfo.class)

public class ReqGroupsInfoHandler extends Handler<ReqGroupsInfo> {

    static final Logger log = LogManager.getLogger(ReqGroupsInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGroupsInfo messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.couplefightManager.getScript().reqCouplefightInfo((Player)mess.getExecutor(), 20);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGroupsInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
