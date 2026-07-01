package com.game.marriage.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.MarriageMessage.ReqAffirmDivorce;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //是否确认离婚
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqAffirmDivorce.MsgID.eMsgID_VALUE, clazz = ReqAffirmDivorce.class)

public class ReqAffirmDivorceHandler extends Handler<ReqAffirmDivorce> {

    static final Logger log = LogManager.getLogger(ReqAffirmDivorceHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqAffirmDivorce messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.marriageManager.manager().reqAffirmDivorce(player, messInfo.getOpt());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqAffirmDivorceHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
