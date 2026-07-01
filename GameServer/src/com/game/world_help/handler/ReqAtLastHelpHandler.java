package com.game.world_help.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldHelpMessage.ReqAtLastHelp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 获取上一次被支援的信息
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqAtLastHelp.MsgID.eMsgID_VALUE, clazz = ReqAtLastHelp.class)

public class ReqAtLastHelpHandler extends Handler<ReqAtLastHelp> {

    static final Logger log = LogManager.getLogger(ReqAtLastHelpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqAtLastHelp messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldHelpManager.getScript().onReqAtLastHelp((Player) mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAtLastHelpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
