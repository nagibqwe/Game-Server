package com.game.pet.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PetMessage.ResSyncPet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * makehandler v1.6 for netty Game->Client 同步宠物信息(激活或强化后)
 */
public class ResSyncPetHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResSyncPetHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResSyncPet messInfo = (ResSyncPet) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());

            player.getPet().setModelId(messInfo.getPet().getModelId());

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResSyncPetHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
