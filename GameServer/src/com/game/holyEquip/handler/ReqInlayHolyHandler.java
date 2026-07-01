package com.game.holyEquip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HolyEquipMessage.ReqInlayHoly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @Desc //镶嵌圣装
 * @Desc TODO Auto Create, Do not Edit
 * @Auth Tool
 */

@Message(id = ReqInlayHoly.MsgID.eMsgID_VALUE, clazz = ReqInlayHoly.class)

public class ReqInlayHolyHandler extends Handler<ReqInlayHoly> {

    static final Logger log = LogManager.getLogger(ReqInlayHolyHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqInlayHoly message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.holyEquipManager.deal().holyEquipInlay(player, message.getUID());


            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
