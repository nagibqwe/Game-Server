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
import game.message.ZoneMessage;
import game.message.ZoneMessage.ResEnterZoneTeamInfo;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author zhaibiao
 */
public class ResEnterZoneTeamInfoHandler extends Handler{
    private static final Logger log = LogManager.getLogger(ResEnterZoneTeamInfoHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");
    @Override
    public void action(RMessage mess) {
        IoSession iosession = mess.getSession();
        ResEnterZoneTeamInfo messInfo = (ResEnterZoneTeamInfo) mess.getData();
        if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
            return;
        }
        Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
        if(player.isIsRead()){
            return;
        }
        ZoneMessage.ReqReadyZone.Builder msg = ZoneMessage.ReqReadyZone.newBuilder();
        msg.setReady(true);
        player.setIsRead(true);
        msg.setTeamId(messInfo.getTeamId());
        player.sendMsg(ZoneMessage.ReqReadyZone.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        log.error("玩家同意进入副本 name : " + player.getUserId());
        player.setEventType(9);
    }
    
}
