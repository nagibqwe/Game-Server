package com.game.world_help.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.WorldHelpMessage.ReqWorldHelpList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求已有的世界支援列表
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqWorldHelpList.MsgID.eMsgID_VALUE, clazz = ReqWorldHelpList.class)

public class ReqWorldHelpListHandler extends Handler<ReqWorldHelpList> {

    static final Logger log = LogManager.getLogger(ReqWorldHelpListHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqWorldHelpList messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.worldHelpManager.getScript().onReqWorldHelpList((Player) mess.getExecutor());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqWorldHelpListHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
