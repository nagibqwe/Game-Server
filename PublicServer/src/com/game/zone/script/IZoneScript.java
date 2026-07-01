/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.zone.script;

import com.game.fightroom.structs.FightRoom;
import com.game.zone.structs.TeamPlayerInfo;
import game.core.script.IScript;
import game.message.ZoneMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * 处理副本类的接口合集
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IZoneScript extends IScript {

    void OnTick(long time);

    void OnNoticeTime(ChannelHandlerContext context);
    
    void sendTeamInfo(FightRoom fr);
    
    ZoneMessage.cloneTeamInfo.Builder buildTeamInfo(TeamPlayerInfo info);
}
