package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqDealMarryPropose;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //处理求婚消息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDealMarryPropose.MsgID.eMsgID_VALUE, clazz = ReqDealMarryPropose.class)

public class ReqDealMarryProposeHandler extends Handler<ReqDealMarryPropose> {

    static final Logger log = LogManager.getLogger(ReqDealMarryProposeHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDealMarryPropose messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.manager().reqBeMarriage(player, messInfo.getMarrayId(), messInfo.getIsAgree());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDealMarryProposeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
