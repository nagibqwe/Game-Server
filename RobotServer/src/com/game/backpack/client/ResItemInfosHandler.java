package com.game.backpack.client;

import com.game.manager.Manager;
import game.core.command.Handler;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.backpackMessage.ResItemInfos;
import org.apache.mina.core.session.IoSession;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.message.RMessage;

/**
 * makehandler v1.7 for netty 玩家背包物品信息,上线和背包变化时返回给玩家
 */
public class ResItemInfosHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResItemInfosHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResItemInfos messInfo = (ResItemInfos) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }

            Manager.equipManager.deal().initBagItem(player,messInfo);
            player.waitDoTime(2000);
            Manager.equipManager.deal().checkAllEquipWear(player);
            if(player.getBagEquips().size()>40){
                Manager.taskManager.mainTask().doEquipSmelt(player);
            }
            log.info("ResItemInfos>"+ player.getInfo() +"道具个数：" + messInfo.getItemInfoListList().size());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 500) {
                logger.error("ResItemInfosHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
