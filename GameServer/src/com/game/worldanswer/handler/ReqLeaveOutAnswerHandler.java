package com.game.worldanswer.handler;

import com.game.player.structs.Player;
import com.game.worldanswer.manager.WorldAnswerManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.worldAnswerMessage.ReqLeaveOutAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求离开答题界面
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqLeaveOutAnswer.MsgID.eMsgID_VALUE, clazz = ReqLeaveOutAnswer.class)

public class ReqLeaveOutAnswerHandler extends Handler<ReqLeaveOutAnswer> {

    static final Logger log = LogManager.getLogger(ReqLeaveOutAnswerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqLeaveOutAnswer messInfo) {
        try {
            long start = TimeUtils.Time();

            WorldAnswerManager.getInstance().getIWorldAnswer().playerLeaveAnswer((Player)mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqLeaveOutAnswerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
