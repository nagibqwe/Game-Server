package com.game.player.client;

import com.game.manager.Manager;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.PlayerMessage.ResPlayerBaseInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;


/**
 * makehandler  v1.5
 */
public class ResPlayerBaseInfoHandler extends Handler {

    private final Logger log = LogManager.getLogger(ResPlayerBaseInfoHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResPlayerBaseInfo messInfo = (ResPlayerBaseInfo) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            Manager.playerManager.deal().initPlayerBaseInfo(player, messInfo);
            log.info("ResPlayerBaseInfo>" + player.getInfo() + "上线同步基本信息");
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResPlayerBaseInfoHandler deal long time:" + dealtime);
            }
            log.trace(String.format("进入游戏 角色ID:%d 完成数量:%d", player.getId(), PlayerManager.getInstance().deal().getPlayerCount()));
            //System.out.println(String.format("收到角色详细消息 角色ID= %d 角色总数:%d", player.getId(), PlayerManager.getInstance().getPlayerCount()));
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}