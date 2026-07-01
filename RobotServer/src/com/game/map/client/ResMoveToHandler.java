package com.game.map.client;

import com.game.map.structs.BaseNpc;
import com.game.player.structs.OtherMoveTo;
import game.core.command.Handler;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.MapMessage.ResMoveTo;
import org.apache.mina.core.session.IoSession;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import com.game.utils.MapUtils;
import game.core.message.RMessage;
import game.message.CommonMessage.Position;

/**
 * makehandler v1.7 for netty 广播移动
 */
public class ResMoveToHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResMoveToHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResMoveTo messInfo = (ResMoveTo) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }
            //主角不需要这个事件
            if (player.getId() == messInfo.getObjectId()) {
                return;
            }

            BaseNpc mo = player.getMapObject(messInfo.getObjectId());

            if (mo == null) {
                return;
            }

            if ( mo.getSpeed() < 0) {
                return;
            }

            OtherMoveTo omt = new OtherMoveTo();
            omt.setInstanceId(messInfo.getObjectId());
            omt.getPos().setX(mo.getCurPos().getX());
            omt.getPos().setY(mo.getCurPos().getY());
            int time = 0;
            com.game.structs.Position pt = new com.game.structs.Position();
            for (Position pos : messInfo.getPosListList()) {
                pt.setX(pos.getX());
                pt.setY(pos.getY());
                time += (int) (MapUtils.getDistance(omt.getPos(), pt) * 1000 / mo.getSpeed());
                omt.getPos().setX(pos.getX());
                omt.getPos().setY(pos.getY());
            }
            omt.setEndTime(TimeUtils.Time() + time);
            player.addMoveToEvent(omt);
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResMoveToHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
