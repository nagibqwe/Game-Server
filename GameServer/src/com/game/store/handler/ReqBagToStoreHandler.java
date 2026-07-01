package com.game.store.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.storeMessage.ReqBagToStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求入库
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqBagToStore.MsgID.eMsgID_VALUE, clazz = ReqBagToStore.class)

public class ReqBagToStoreHandler extends Handler<ReqBagToStore> {

    static final Logger log = LogManager.getLogger(ReqBagToStoreHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqBagToStore messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.storeManager.deal().OnReqBagToStore(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqBagToStoreHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
