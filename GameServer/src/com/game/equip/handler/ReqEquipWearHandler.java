package com.game.equip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EquipMessage.ReqEquipWear;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //穿戴装备
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqEquipWear.MsgID.eMsgID_VALUE, clazz = ReqEquipWear.class)

public class ReqEquipWearHandler extends Handler<ReqEquipWear> {

    static final Logger log = LogManager.getLogger(ReqEquipWearHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqEquipWear message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.equipManager.wear(player, message.getEquipId(), message.getInherit());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
