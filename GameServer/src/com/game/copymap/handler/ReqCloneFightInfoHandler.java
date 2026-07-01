package com.game.copymap.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqCloneFightInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求跨服副本的战报
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqCloneFightInfo.MsgID.eMsgID_VALUE, clazz = ReqCloneFightInfo.class)

public class ReqCloneFightInfoHandler extends Handler<ReqCloneFightInfo> {

    static final Logger log = LogManager.getLogger(ReqCloneFightInfoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqCloneFightInfo message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.copyMapManager.logic().OnReqCloneFightInfo(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCloneFightInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
