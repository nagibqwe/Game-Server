package com.game.holyEquip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HolyEquipMessage.ReqResolveHoly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //分解圣装
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqResolveHoly.MsgID.eMsgID_VALUE, clazz = ReqResolveHoly.class)

public class ReqResolveHolyHandler extends Handler<ReqResolveHoly> {

    static final Logger log = LogManager.getLogger(ReqResolveHolyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqResolveHoly message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.holyEquipManager.deal().resolveHolyEquip(player,message.getUidsList());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
