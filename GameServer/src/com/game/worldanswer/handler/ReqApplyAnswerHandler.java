package com.game.worldanswer.handler;

import com.game.player.structs.Player;
import com.game.worldanswer.manager.WorldAnswerManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.worldAnswerMessage.ReqApplyAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求报名 其实就是打开 答题界面
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqApplyAnswer.MsgID.eMsgID_VALUE, clazz = ReqApplyAnswer.class)

public class ReqApplyAnswerHandler extends Handler<ReqApplyAnswer> {

    static final Logger log = LogManager.getLogger(ReqApplyAnswerHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqApplyAnswer messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            WorldAnswerManager.getInstance().getIWorldAnswer().applyAnswer(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqApplyAnswerHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
