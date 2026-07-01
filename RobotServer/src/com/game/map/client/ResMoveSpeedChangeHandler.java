package com.game.map.client;

import com.game.map.structs.BaseNpc;
import game.core.command.Handler;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.MapMessage.ResMoveSpeedChange;
import org.apache.mina.core.session.IoSession;
import com.game.player.structs.Player;
import com.game.structs.AttributeType;
import com.game.structs.SessionAttribute;
import game.core.message.RMessage;

/**
 * makehandler v1.7 for netty 移动速度变化
 */
public class ResMoveSpeedChangeHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResMoveSpeedChangeHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResMoveSpeedChange messInfo = (ResMoveSpeedChange) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }

            if (player.getId() == messInfo.getObjectId()) {
                player.setAttributeValue(AttributeType.MoveSpeedFinal, messInfo.getValue());
            } else {
                BaseNpc mo = player.getMapObject(messInfo.getObjectId());
                if (mo == null) {
                    return;
                }
                mo.setSpeed(messInfo.getValue()/100f);
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResMoveSpeedChangeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
