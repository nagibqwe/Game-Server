package com.game.godbook.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GodBook.ReqActiveAmulet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求激活符咒
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqActiveAmulet.MsgID.eMsgID_VALUE, clazz = ReqActiveAmulet.class)

public class ReqActiveAmuletHandler extends Handler<ReqActiveAmulet> {

    static final Logger log = LogManager.getLogger(ReqActiveAmuletHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqActiveAmulet message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            Manager.godBookManager.deal().onReqActiveAmulet(player, message);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
