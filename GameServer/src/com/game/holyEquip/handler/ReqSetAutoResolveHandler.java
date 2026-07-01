package com.game.holyEquip.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HolyEquipMessage.ReqSetAutoResolve;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //自动分解设置
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqSetAutoResolve.MsgID.eMsgID_VALUE, clazz = ReqSetAutoResolve.class)

public class ReqSetAutoResolveHandler extends Handler<ReqSetAutoResolve> {

    static final Logger log = LogManager.getLogger(ReqSetAutoResolveHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqSetAutoResolve message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)session.getExecutor();
            Manager.holyEquipManager.deal(). ReqSetAutoResolve( player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
