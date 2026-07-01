package com.game.map.client;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.MapMessage.ResMonsterDisappear;
import org.apache.mina.core.session.IoSession;


/**
* makehandler  v1.5
*给周围玩家发送Monster 消失
*/
public class ResMonsterDisappearHandler extends Handler{

    private final Logger log = LogManager.getLogger(ResMonsterDisappearHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResMonsterDisappear messInfo = (ResMonsterDisappear) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            player.removeNpc(messInfo.getMonsterId());
            log.info(player.getInfo()+"收到怪物移除消息："+messInfo.getMonsterId());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResMonsterDisappearHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}