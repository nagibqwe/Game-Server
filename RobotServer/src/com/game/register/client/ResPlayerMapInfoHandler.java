package com.game.register.client;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RegisterMessage.ResPlayerMapInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;


/**
* makehandler  v1.5
*服务器回复当前角色的所在地图
*/
public class ResPlayerMapInfoHandler extends Handler{

    private final Logger log = LogManager.getLogger(ResPlayerMapInfoHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResPlayerMapInfo messInfo = (ResPlayerMapInfo) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            player.setMapInfo(messInfo);
            player.waitDoTime(5000);
            Manager.registerManager.deal().reqLoadFinish(player);
            log.info(player.getInfo() + " 收到进入场景消息,角色ID= " + player.getId()+",地图ID:"+messInfo.getMapId()+",线路："+messInfo.getLineId());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResPlayerMapInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}