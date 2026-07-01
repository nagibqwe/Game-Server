package com.game.openserverac.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.OpenServerAcMessage.ReqV4HelpOther;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求投资玩家
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqV4HelpOther.MsgID.eMsgID_VALUE, clazz = ReqV4HelpOther.class)

public class ReqV4HelpOtherHandler extends Handler<ReqV4HelpOther> {

    static final Logger log = LogManager.getLogger(ReqV4HelpOtherHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqV4HelpOther messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player)mess.getExecutor();
            Manager.v4HelpManager.deal().ReqV4HelpOther(player,messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqV4HelpOtherHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
