package com.game.shihai.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ShiHaiMessage.ReqShiHaiData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //打开面板，返回ResShiHaiData
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqShiHaiData.MsgID.eMsgID_VALUE, clazz = ReqShiHaiData.class)

public class ReqShiHaiDataHandler extends Handler<ReqShiHaiData> {

    static final Logger log = LogManager.getLogger(ReqShiHaiDataHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqShiHaiData messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.shiHaiManager.deal().shiHaiData(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqShiHaiDataHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
