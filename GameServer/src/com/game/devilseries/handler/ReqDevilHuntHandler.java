package com.game.devilseries.handler;

import com.game.devilseries.manager.DevilSeriesManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DevilSeriesMessage.ReqDevilHunt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //魔魂抽奖请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDevilHunt.MsgID.eMsgID_VALUE, clazz = ReqDevilHunt.class)

public class ReqDevilHuntHandler extends Handler<ReqDevilHunt> {

    static final Logger log = LogManager.getLogger(ReqDevilHuntHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDevilHunt messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            DevilSeriesManager.instance.getScript().onReqDevilHuntHandler(player,messInfo.getHuntType(),messInfo.getConsecutiveType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDevilHuntHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
