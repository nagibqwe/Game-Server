package com.game.devilseries.handler;

import com.game.devilseries.manager.DevilSeriesManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DevilSeriesMessage.ReqDevilEquipSynthesis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //装备合成请求
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDevilEquipSynthesis.MsgID.eMsgID_VALUE, clazz = ReqDevilEquipSynthesis.class)

public class ReqDevilEquipSynthesisHandler extends Handler<ReqDevilEquipSynthesis> {

    static final Logger log = LogManager.getLogger(ReqDevilEquipSynthesisHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDevilEquipSynthesis messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            DevilSeriesManager.instance.getScript().devilEquipSynthesis(player, messInfo.getItemId(), messInfo.getEquipsList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDevilEquipSynthesisHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
