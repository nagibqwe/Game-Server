package com.game.map.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.MapMessage.ResStopMove;
import org.apache.mina.core.session.IoSession;

/**
 * makehandler v1.5 移动
 */
public class ResStopMoveHandler extends Handler {

    private final Logger log = LogManager.getLogger(ResStopMoveHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResStopMove messInfo = (ResStopMove) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }

            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
//            log.error(player.getInfo() + " 坐标（" + player.getCurPos().toPosition() + ")");
            if (messInfo.getObjectId() == player.getId()) {
                player.stopMove(messInfo);
            } else {
                //player.setOtherStop(messInfo);
            }
//            log.info(player.getUserId() + " 坐标（" + player.getPosX() + "," + player.getPosY() + ")");
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResStopMoveHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
