package com.game.equip.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RecycleMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * 客户端收到服务器熔炼回复
 */
public class ResRecycleHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResRecycleHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            RecycleMessage.ResRecycle messInfo = (RecycleMessage.ResRecycle) mess.getData();
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }
            for (long rmId:player.getRmEquipList()) {
                player.getBagEquips().remove(rmId);
            }
            player.getRmEquipList().clear();
            log.info("ResRecycle>" + player.getInfo() + "收到：" + messInfo.getNum() + "个回收道具(灵晶)");

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResRecycleHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
