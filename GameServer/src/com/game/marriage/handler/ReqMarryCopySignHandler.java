package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqMarryCopySign;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //婚宴副本签到请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqMarryCopySign.MsgID.eMsgID_VALUE, clazz = ReqMarryCopySign.class)

public class ReqMarryCopySignHandler extends Handler<ReqMarryCopySign> {

    static final Logger log = LogManager.getLogger(ReqMarryCopySignHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqMarryCopySign messInfo) {
        try {
            long start = TimeUtils.Time();
            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.manager().reqMarryCopySign(player);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqMarryCopySignHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
