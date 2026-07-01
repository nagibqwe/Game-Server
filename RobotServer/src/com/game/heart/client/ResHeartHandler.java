/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.heart.client;

import com.game.fight.client.ResObjDeadHandler;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.heartMessage.ResHeart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author hewei
 */
public class ResHeartHandler extends Handler {

    private final Logger log = LogManager.getLogger(ResObjDeadHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            IoSession iosession = mess.getSession();
            ResHeart messInfo = (ResHeart) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                log.error("心跳返回：" + messInfo.getServerTime() + " 获取不到containsAttribute(SessionAttribute.PLAYER");
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            player.setlastHeartSendTime(TimeUtils.Time());
            
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
