package com.game.huaxinflysword.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HuaxinFlySwordMessage.ReqSkipSoulCopy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //剑灵隔 跳关
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSkipSoulCopy.MsgID.eMsgID_VALUE, clazz = ReqSkipSoulCopy.class)

public class ReqSkipSoulCopyHandler extends Handler<ReqSkipSoulCopy> {

    static final Logger log = LogManager.getLogger(ReqSkipSoulCopyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSkipSoulCopy messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();

            Manager.huaxinFlySwordManager.swordSoulScript().onReqSkipSoulCopy(player);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSkipSoulCopyHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
