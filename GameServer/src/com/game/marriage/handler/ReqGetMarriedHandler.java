package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqGetMarried;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求结婚
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqGetMarried.MsgID.eMsgID_VALUE, clazz = ReqGetMarried.class)

public class ReqGetMarriedHandler extends Handler<ReqGetMarried> {

    static final Logger log = LogManager.getLogger(ReqGetMarriedHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqGetMarried messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.manager().reqMarriage(player, messInfo.getBeMarrayId(), messInfo.getType(), messInfo.getIsNotice(), messInfo.getNotice());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetMarriedHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
