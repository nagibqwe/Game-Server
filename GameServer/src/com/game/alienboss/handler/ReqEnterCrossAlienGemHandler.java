package com.game.alienboss.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.AlienBossMessage.ReqEnterCrossAlienGem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入须弥宝库
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqEnterCrossAlienGem.MsgID.eMsgID_VALUE, clazz = ReqEnterCrossAlienGem.class)

public class ReqEnterCrossAlienGemHandler extends Handler<ReqEnterCrossAlienGem> {

    static final Logger log = LogManager.getLogger(ReqEnterCrossAlienGemHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqEnterCrossAlienGem messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = mess.getExecutor();
            Manager.crossFudManager.deal().reqEnterCrossAlienGemHandler(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqEnterCrossAlienGemHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
