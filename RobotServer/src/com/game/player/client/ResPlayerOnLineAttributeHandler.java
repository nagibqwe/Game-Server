package com.game.player.client;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ResPlayerOnLineAttribute;
import org.apache.mina.core.session.IoSession;


/**
* makehandler  v1.5
* 玩家上线服务端计算玩家个属性返回给客户端
*/
public class ResPlayerOnLineAttributeHandler extends Handler{

    private final Logger log = LogManager.getLogger(ResPlayerOnLineAttributeHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResPlayerOnLineAttribute messInfo = (ResPlayerOnLineAttribute) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            player.playerAttributeInfo(messInfo);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResPlayerOnLineAttributeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}