package com.game.store.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.storeMessage.ReqStoreClearUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //仓库整理
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqStoreClearUp.MsgID.eMsgID_VALUE, clazz = ReqStoreClearUp.class)

public class ReqStoreClearUpHandler extends Handler<ReqStoreClearUp> {

    static final Logger log = LogManager.getLogger(ReqStoreClearUpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqStoreClearUp messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.storeManager.deal().OnReqStoreClearUp(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqStoreClearUpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
