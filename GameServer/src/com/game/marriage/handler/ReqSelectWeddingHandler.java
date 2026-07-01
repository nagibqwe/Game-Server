package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqSelectWedding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //预约婚宴
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSelectWedding.MsgID.eMsgID_VALUE, clazz = ReqSelectWedding.class)

public class ReqSelectWeddingHandler extends Handler<ReqSelectWedding> {

    static final Logger log = LogManager.getLogger(ReqSelectWeddingHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSelectWedding messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.reqSelectMarriage(player, messInfo.getTimeStart());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSelectWeddingHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
