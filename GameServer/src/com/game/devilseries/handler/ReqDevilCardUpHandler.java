package com.game.devilseries.handler;

import com.game.devilseries.manager.DevilSeriesManager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.DevilSeriesMessage.ReqDevilCardUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //请求魔魂解锁升级升阶
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqDevilCardUp.MsgID.eMsgID_VALUE, clazz = ReqDevilCardUp.class)

public class ReqDevilCardUpHandler extends Handler<ReqDevilCardUp> {

    static final Logger log = LogManager.getLogger(ReqDevilCardUpHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqDevilCardUp messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            DevilSeriesManager.instance.getScript().devilCardUp(player, messInfo.getCampId(), messInfo.getCardId(), messInfo.getType());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqDevilCardUpHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
