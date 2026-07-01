package com.game.pet.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ReqEatSoul;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //Client->Game 御魂升级
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqEatSoul.MsgID.eMsgID_VALUE, clazz = ReqEatSoul.class)

public class ReqEatSoulHandler extends Handler<ReqEatSoul> {

    static final Logger log = LogManager.getLogger(ReqEatSoulHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqEatSoul messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player)mess.getExecutor();
            Manager.petManager.deal().eatSoul(player, messInfo);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqEatSoulHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
