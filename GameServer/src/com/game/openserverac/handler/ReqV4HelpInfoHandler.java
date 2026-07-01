package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqV4HelpInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求v4助力列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqV4HelpInfo.MsgID.eMsgID_VALUE, clazz = ReqV4HelpInfo.class)

public class ReqV4HelpInfoHandler extends Handler<ReqV4HelpInfo> {

    static final Logger log = LogManager.getLogger(ReqV4HelpInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqV4HelpInfo messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.v4HelpManager.deal().ReqV4HelpInfo(player,messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqV4HelpInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
