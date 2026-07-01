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
import game.message.TeamMessage.ResInviteInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author zhaibiao
 */
public class ResInviteInfoHandler extends Handler{
    
    private final Logger log = LogManager.getLogger(ResInviteInfoHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResInviteInfo messInfo = (ResInviteInfo) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            //收到邀请信息回复同意
            if(player.getTeamId()==-1){
                TeamMessage.ReqInviteRes.Builder msg = TeamMessage.ReqInviteRes.newBuilder();
                msg.setRoleId(messInfo.getRoleId());
                msg.setTeamdId(messInfo.getTeamdId());
                msg.setType(0);
                player.sendMsg(TeamMessage.ReqInviteRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResInviteInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }
    
//            TeamMessage.ResInviteInfo.Builder msg = TeamMessage.ResInviteInfo.newBuilder();
//        msg.setTeamdId(team.getTeamId());
//        msg.setRoleId(player.getId());
//        msg.setName(player.getName());
//        msg.setType(team.getType());
//        msg.setGroupId(team.getType_group());
//        MessageUtils.send_to_player(target, TeamMessage.ResInviteInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    
}
