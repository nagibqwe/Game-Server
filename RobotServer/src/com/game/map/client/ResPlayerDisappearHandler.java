package com.game.map.client;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.MapMessage.ResPlayerDisappear;
import org.apache.mina.core.session.IoSession;


/**
* makehandler  v1.5
*玩家消失
*/
public class ResPlayerDisappearHandler extends Handler{

    private final Logger log = LogManager.getLogger(ResPlayerDisappearHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResPlayerDisappear messInfo = (ResPlayerDisappear) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            for(Long id : messInfo.getPlayerIdsList()){
                player.removeNpc(id);
            }
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResPlayerDisappearHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}