package com.game.title.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TitleMessage.ReqDownTitle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求卸下称号
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDownTitle.MsgID.eMsgID_VALUE, clazz = ReqDownTitle.class)

public class ReqDownTitleHandler extends Handler<ReqDownTitle> {

    static final Logger log = LogManager.getLogger(ReqDownTitleHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDownTitle messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.titleManager.deal().onReqDownTitle(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDownTitleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
