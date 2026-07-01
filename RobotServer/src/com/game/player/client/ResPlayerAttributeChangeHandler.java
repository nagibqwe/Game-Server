package com.game.player.client;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ResPlayerAttributeChange;
import org.apache.mina.core.session.IoSession;


/**
* makehandler  v1.5
* 玩家属性变化返回给客户端
*/
public class ResPlayerAttributeChangeHandler extends Handler{

    private final Logger log = LogManager.getLogger(ResPlayerAttributeChangeHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResPlayerAttributeChange messInfo = (ResPlayerAttributeChange) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            player.playerAttributeChange(messInfo);
            log.debug("ResPlayerAttributeChange>" + player.getInfo() + "属性改变");
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResPlayerAttributeChangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}