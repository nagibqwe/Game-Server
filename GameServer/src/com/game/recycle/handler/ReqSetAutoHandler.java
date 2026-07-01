package com.game.recycle.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RecycleMessage.ReqSetAuto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client -> Server 请求设置自动熔炼垃圾装备
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqSetAuto.MsgID.eMsgID_VALUE, clazz = ReqSetAuto.class)

public class ReqSetAutoHandler extends Handler<ReqSetAuto> {

    static final Logger log = LogManager.getLogger(ReqSetAutoHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqSetAuto messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.recycleManager.deal().onReqSetAuto(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqSetAutoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
