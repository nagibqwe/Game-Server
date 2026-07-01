package com.game.devilseries.handler;

import com.game.devilseries.manager.DevilSeriesManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DevilSeriesMessage.ReqDevilEquipWear;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 穿戴装备
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDevilEquipWear.MsgID.eMsgID_VALUE, clazz = ReqDevilEquipWear.class)

public class ReqDevilEquipWearHandler extends Handler<ReqDevilEquipWear> {

    static final Logger log = LogManager.getLogger(ReqDevilEquipWearHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDevilEquipWear messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            DevilSeriesManager.instance.getScript().devilEquipWear(player, messInfo.getCampId(), messInfo.getCardId(), messInfo.getCellId(), messInfo.getEquipId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDevilEquipWearHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
