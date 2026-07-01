/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.zone.script;

import com.game.zone.structs.ZoneTeam;
import game.core.script.IScript;
import game.message.BravePeakMessage;
import game.message.CrossServerMessage;
import game.message.ZoneMessage;
import game.message.ZoneMessage.G2PReqCrossZoneReadyZone;
import game.message.ZoneMessage.G2PReqEnterZone;
import io.netty.channel.ChannelHandlerContext;

/**
 * 跨服进入的协议处理函数
 *
 * @author xuchangming <xysoko@qq.com>
 */
public interface IZoneHandlerScript extends IScript {

    void OnG2PReqCrossZoneReadyZone(ChannelHandlerContext context, G2PReqCrossZoneReadyZone mess);

    void OnG2PReqEnterZone(ChannelHandlerContext context, G2PReqEnterZone mess);
    
    void OnG2PReqCancelMatch(ChannelHandlerContext context, ZoneMessage.G2PReqCancelMatch mess);
    
    void cancelMatch(long roleId ,String name,ZoneTeam zt,int type);

    /**
     * 游戏服过来获取玩家勇者巅峰信息
     * @param context
     * @param mess
     */
    void onG2PGetPlayerBravePeakInfo(ChannelHandlerContext context, BravePeakMessage.G2PGetPlayerBravePeakInfo mess);
    
}
