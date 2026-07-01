package com.game.skill.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.SkillMessage.ReqActivateMeridian;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //激活经脉
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqActivateMeridian.MsgID.eMsgID_VALUE, clazz = ReqActivateMeridian.class)

public class ReqActivateMeridianHandler extends Handler<ReqActivateMeridian> {

    static final Logger log = LogManager.getLogger(ReqActivateMeridianHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqActivateMeridian messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.skillManager.deal().onReqActivateMeridian(player,messInfo.getMeridianID());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqActivateMeridianHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
