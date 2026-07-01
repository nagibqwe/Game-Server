package com.game.holyEquip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HolyEquipMessage.ReqUseHolySoul;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //使用圣魂
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqUseHolySoul.MsgID.eMsgID_VALUE, clazz = ReqUseHolySoul.class)

public class ReqUseHolySoulHandler extends Handler<ReqUseHolySoul> {

    static final Logger log = LogManager.getLogger(ReqUseHolySoulHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqUseHolySoul message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.holyEquipManager.deal().useHolySoul(player,message.getItemId(),message.getUseNum());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
