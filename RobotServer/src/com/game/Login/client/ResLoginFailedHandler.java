package com.game.Login.client;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import game.core.util.TimeUtils;
import game.message.LoginMessage.ResLoginFailed;
import org.apache.mina.core.session.IoSession;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;


/**
* makehandler  v1.9 for netty
* 服务端回复登录失败
*/
public class ResLoginFailedHandler extends Handler{

    private static final Logger log = LogManager.getLogger(ResLoginFailedHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResLoginFailed messInfo = (ResLoginFailed) mess.getData();
            //if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
            //    return;
            //}
            //Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            //if (player == null) {
            //    return;
            //}

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResLoginFailedHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e,e);
        }
    }

}