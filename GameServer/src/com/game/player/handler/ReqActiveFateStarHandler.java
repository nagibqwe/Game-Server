package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqActiveFateStar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //激活命星
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqActiveFateStar.MsgID.eMsgID_VALUE, clazz = ReqActiveFateStar.class)

public class ReqActiveFateStarHandler extends Handler<ReqActiveFateStar> {

    static final Logger log = LogManager.getLogger(ReqActiveFateStarHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqActiveFateStar messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.deal(ScriptEnum.ChangeJobBaseScript).activeFateStar(player, messInfo.getId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActiveFateStarHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
