package com.game.backpack.client;
import com.game.manager.Manager;
import game.core.command.Handler;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.backpackMessage.ResItemAdd;
import org.apache.mina.core.session.IoSession;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.message.RMessage;


/**
* makehandler  v1.7 for netty
*物品新增
*/
public class ResItemAddHandler extends Handler{

    private static final Logger log = LogManager.getLogger(ResItemAddHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResItemAdd messInfo = (ResItemAdd) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }

            Manager.equipManager.deal().setBagItem(player,messInfo.getItemInfo());
            player.waitDoTime(1000);
            Manager.equipManager.deal().checkEquipWear(player,messInfo.getItemInfo());

            log.info("ResItemAdd>" + player.getInfo() + "获得道具,modelId=" + messInfo.getItemInfo().getItemModelId() + "，num="+messInfo.getItemInfo().getNum());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResItemAddHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}