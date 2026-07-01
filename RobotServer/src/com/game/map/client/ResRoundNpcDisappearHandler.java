package com.game.map.client;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.MapMessage.ResRoundNpcDisappear;
import org.apache.mina.core.session.IoSession;


/**
* makehandler  v1.5
*向周围玩家发送消失的NPC信息
*/
public class ResRoundNpcDisappearHandler extends Handler{

    private final Logger log = LogManager.getLogger(ResRoundNpcDisappearHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResRoundNpcDisappear messInfo = (ResRoundNpcDisappear) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            for(long id : messInfo.getNpcIdsList()){
                player.removeNpc(id);
            }
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResRoundNpcDisappearHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}