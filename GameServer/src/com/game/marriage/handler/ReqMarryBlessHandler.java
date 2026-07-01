package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryBless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //送祝福
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryBless.MsgID.eMsgID_VALUE, clazz = ReqMarryBless.class)

public class ReqMarryBlessHandler extends Handler<ReqMarryBless> {

    static final Logger log = LogManager.getLogger(ReqMarryBlessHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryBless messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.manager().reqMarryBless(player, messInfo.getMarryId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryBlessHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
