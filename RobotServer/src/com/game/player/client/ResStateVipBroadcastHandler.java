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
 * makehandler v1.5 更新境界等级
 */
public class ResStateVipBroadcastHandler extends Handler {

    private final Logger log = LogManager.getLogger(ResStateVipBroadcastHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            StateVipMessage.ResStateVipBroadcast messInfo = (StateVipMessage.ResStateVipBroadcast) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (messInfo.getRoleID() != player.getId()) {
                return;
            }
            player.setStateLevel(messInfo.getStateVip());
            log.info("ResStateVipBroadcast>" + player.getInfo() + "更新境界等级:"+messInfo.getStateVip());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResStateVipBroadcastHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
