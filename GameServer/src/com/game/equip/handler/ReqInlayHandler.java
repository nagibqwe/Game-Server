package com.game.equip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EquipMessage.ReqInlay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc ////////////////////////////装备宝石协议//////////////////////////
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqInlay.MsgID.eMsgID_VALUE, clazz = ReqInlay.class)

public class ReqInlayHandler extends Handler<ReqInlay> {

    static final Logger log = LogManager.getLogger(ReqInlayHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqInlay message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.gemManager.deal().onReqInlay(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
