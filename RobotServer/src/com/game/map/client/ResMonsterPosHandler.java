package com.game.map.client;
import game.core.command.Handler;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.MapMessage.ResMonsterPos;
import org.apache.mina.core.session.IoSession;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.message.RMessage;


/**
* makehandler  v1.7 for netty
*返回地图中刷新的怪物坐标
*/
public class ResMonsterPosHandler extends Handler{

    private static final Logger log = LogManager.getLogger(ResMonsterPosHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResMonsterPos messInfo = (ResMonsterPos) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }

            player.moveTo(messInfo.getX(), messInfo.getY());
            log.info(player.getInfo() + "收到目标位置信息:x="+messInfo.getX()+" y="+messInfo.getY());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResMonsterPosHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}