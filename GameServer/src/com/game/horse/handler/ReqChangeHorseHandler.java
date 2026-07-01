package com.game.horse.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.HorseMessage.ReqChangeHorse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 请求改乘坐骑
* @Desc TODO Auto Create, Do not Edit
* @Auth Tool
*/

@Message(id = ReqChangeHorse.MsgID.eMsgID_VALUE, clazz = ReqChangeHorse.class)

public class ReqChangeHorseHandler extends Handler<ReqChangeHorse> {

    static final Logger log = LogManager.getLogger(ReqChangeHorseHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage session, ReqChangeHorse message) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();
            if (player != null) {
                Manager.horseManager.deal().onReqChangeHorse(player, message.getHorseLayer());
            } else {
                log.error("未获取到玩家数据！");
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqGetAchievementHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
