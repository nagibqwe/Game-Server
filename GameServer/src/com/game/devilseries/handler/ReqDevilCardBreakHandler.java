package com.game.devilseries.handler;

import com.game.devilseries.manager.DevilSeriesManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DevilSeriesMessage.ReqDevilCardBreak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 魔魂突破
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDevilCardBreak.MsgID.eMsgID_VALUE, clazz = ReqDevilCardBreak.class)

public class ReqDevilCardBreakHandler extends Handler<ReqDevilCardBreak> {

    static final Logger log = LogManager.getLogger(ReqDevilCardBreakHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDevilCardBreak messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            DevilSeriesManager.instance.getScript().devilCardBreak(player, messInfo.getCampId(), messInfo.getCardId(), messInfo.getEquipIdList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDevilCardBreakHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
