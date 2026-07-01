package com.game.player.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ResUpdataPkValue;
import org.apache.mina.core.session.IoSession;

/**
 * makehandler v1.6 for netty 更新pk值
 */
public class ResUpdataPkValueHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResUpdataPkValueHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResUpdataPkValue messInfo = (ResUpdataPkValue) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            player.setPkState(messInfo.getPkValue());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResUpdataPkValueHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
