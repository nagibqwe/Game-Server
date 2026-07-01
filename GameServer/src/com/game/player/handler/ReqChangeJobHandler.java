package com.game.player.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ReqChangeJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //转职
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqChangeJob.MsgID.eMsgID_VALUE, clazz = ReqChangeJob.class)

public class ReqChangeJobHandler extends Handler<ReqChangeJob> {

    static final Logger log = LogManager.getLogger(ReqChangeJobHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqChangeJob messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.deal(ScriptEnum.ChangeJobBaseScript).changeJob(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqChangeJobHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
