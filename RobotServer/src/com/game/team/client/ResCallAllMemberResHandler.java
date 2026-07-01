/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.team.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.TeamMessage;
import game.message.TeamMessage.ResCallAllMemberRes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author zhaibiao
 */
public class ResCallAllMemberResHandler extends Handler{
    private final Logger log = LogManager.getLogger(ResCallAllMemberResHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResCallAllMemberRes messInfo = (ResCallAllMemberRes) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            //收到招呼同意驻召唤
            TeamMessage.ReqAgreeCall.Builder msg = TeamMessage.ReqAgreeCall.newBuilder();
            msg.setCallId(messInfo.getCallId());
            player.sendMsg(TeamMessage.ReqAgreeCall.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResCallAllMemberResHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }
}
