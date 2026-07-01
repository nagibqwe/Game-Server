package com.game.player.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.StateVipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * makehandler v1.5 境界等级信息
 */
public class ResStateVipHandler extends Handler {

    private final Logger log = LogManager.getLogger(ResStateVipHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            StateVipMessage.ResStateVip messInfo = (StateVipMessage.ResStateVip) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            log.info("ResStateVip>" + player.getInfo() + "获取境界信息");
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResStateVipHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
