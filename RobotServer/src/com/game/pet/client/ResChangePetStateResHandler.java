package com.game.pet.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import org.apache.mina.core.session.IoSession;


/**
* makehandler  v1.6 for netty
*Game->Client 返给宠物Ai状态
*/
public class ResChangePetStateResHandler extends Handler{

    private static final Logger log = LogManager.getLogger(ResChangePetStateResHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
//            ResChangePetStateRes messInfo = (ResChangePetStateRes) mess.getData();

            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            
            
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResChangePetStateResHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}