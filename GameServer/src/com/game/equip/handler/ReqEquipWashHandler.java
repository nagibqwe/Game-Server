package com.game.equip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EquipMessage.ReqEquipWash;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //装备洗练
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqEquipWash.MsgID.eMsgID_VALUE, clazz = ReqEquipWash.class)

public class ReqEquipWashHandler extends Handler<ReqEquipWash> {

    static final Logger log = LogManager.getLogger(ReqEquipWashHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqEquipWash message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.equipManager.deal().onReqEquipWash(player, message.getId(), message.getType(), message.getIndexsList());


            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
