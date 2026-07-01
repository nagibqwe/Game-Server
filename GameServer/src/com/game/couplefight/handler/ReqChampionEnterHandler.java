package com.game.couplefight.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage.ReqChampionEnter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求进入冠军赛准备地图
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChampionEnter.MsgID.eMsgID_VALUE, clazz = ReqChampionEnter.class)

public class ReqChampionEnterHandler extends Handler<ReqChampionEnter> {

    static final Logger log = LogManager.getLogger(ReqChampionEnterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChampionEnter messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.couplefightManager.getScript().reqChampionEnter(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChampionEnterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
