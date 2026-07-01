package com.game.equip.client;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.EquipMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * 客户端收到服务器的装备部位消息
 */
public class ResEquipPartInfoHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResEquipPartInfoHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            EquipMessage.ResEquipPartInfo messInfo = (EquipMessage.ResEquipPartInfo) mess.getData();
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }
            Manager.equipManager.deal().initBodyEquip(player,messInfo);
            log.info("ResEquipPartInfo>" + player.getInfo() + "获得身上装备信息，个数：" + messInfo.getInfosList().size());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResEquipPartInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
