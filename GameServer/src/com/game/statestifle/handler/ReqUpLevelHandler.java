package com.game.statestifle.handler;

import com.game.player.structs.Player;
import com.game.statestifle.manager.StateStifleManager;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateStifleMessage.ReqUpLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqUpLevel.MsgID.eMsgID_VALUE, clazz = ReqUpLevel.class)

public class ReqUpLevelHandler extends Handler<ReqUpLevel> {

    static final Logger log = LogManager.getLogger(ReqUpLevelHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqUpLevel messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            StateStifleManager.getInstance().deal().levelUp(player, messInfo.getOneKey());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqUpLevelHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
