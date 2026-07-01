package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqXiSui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc // 请求洗髓，成功返回SyncXiSuiData
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqXiSui.MsgID.eMsgID_VALUE, clazz = ReqXiSui.class)

public class ReqXiSuiHandler extends Handler<ReqXiSui> {

    static final Logger log = LogManager.getLogger(ReqXiSuiHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqXiSui messInfo) {
        try {
            long start = TimeUtils.Time();

            Manager.playerManager.xiSuiScript().onReqXiSui((Player) mess.getExecutor(), messInfo.getFree());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqXiSuiHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
