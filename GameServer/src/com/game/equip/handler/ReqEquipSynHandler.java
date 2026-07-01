package com.game.equip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EquipMessage.ReqEquipSyn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //装备合成
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqEquipSyn.MsgID.eMsgID_VALUE, clazz = ReqEquipSyn.class)

public class ReqEquipSynHandler extends Handler<ReqEquipSyn> {

    static final Logger log = LogManager.getLogger(ReqEquipSynHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqEquipSyn message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.auctionManager.auctionFastPur(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
