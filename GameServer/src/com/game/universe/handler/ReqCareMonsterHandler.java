package com.game.universe.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MSG_UniverseMessage.ReqCareMonster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求关注怪物
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqCareMonster.MsgID.eMsgID_VALUE, clazz = ReqCareMonster.class)

public class ReqCareMonsterHandler extends Handler<ReqCareMonster> {

    static final Logger log = LogManager.getLogger(ReqCareMonsterHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqCareMonster messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            if (player == null) {
                return;
            }
            Manager.universeManager.deal().onReqCareMonster(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqCareMonsterHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
