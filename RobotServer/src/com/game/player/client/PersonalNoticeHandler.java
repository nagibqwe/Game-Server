/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.player.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.ChatMessage.PersonalNotice;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import org.apache.mina.core.session.IoSession;

/**
 *
 * 系统消息提示 用于处理后续事件
 * @author zhaibiao
 */
public class PersonalNoticeHandler extends Handler{
    private static final Logger log = LogManager.getLogger(PersonalNoticeHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            PersonalNotice messInfo = (PersonalNotice) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            player.systemMessages(messInfo.getType(),messInfo.getValueList());
//            log.info("PersonalNotice>"+player.getInfo());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("PersonalNoticeHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }
}
