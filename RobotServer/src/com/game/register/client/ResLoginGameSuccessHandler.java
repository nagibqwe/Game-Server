package com.game.register.client;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RegisterMessage.ResLoginGameSuccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * makehandler v1.5 服务端回复登录成功
 */
public class ResLoginGameSuccessHandler extends Handler {

    private final Logger log = LogManager.getLogger(ResLoginGameSuccessHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResLoginGameSuccess messInfo = (ResLoginGameSuccess) mess.getData();

            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            Manager.registerManager.deal().loginGameSuccess(player, messInfo.getInfoListList());
            
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResLoginGameSuccessHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
