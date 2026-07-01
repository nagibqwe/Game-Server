/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.script;

import game.core.script.IScript;
import game.message.ChatMessage;
import game.message.CrossFightMessage.G2PCheckCrossInfo;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.G2PConnectHeart;
import game.message.DailyactiveMessage;
import game.message.serverMessage;
import game.message.serverMessage.G2PReqRegister;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IGSManagerScript extends IScript {

    public void OnG2PReqRegister(ChannelHandlerContext context, G2PReqRegister mess);

    public void OnG2PCheckCrossInfo(ChannelHandlerContext context, G2PCheckCrossInfo mess);
    
    public void OnG2PConnectHeart(ChannelHandlerContext context, G2PConnectHeart mess);

    public void OnG2PServerNameChange(ChannelHandlerContext context, CrossServerMessage.G2PServerNameChange messInfo);

    public void OnG2PServerOpentimeChange(ChannelHandlerContext context, CrossServerMessage.G2PServerOpentimeChange messInfo);

    public void OnG2PServerWorldLvChange(ChannelHandlerContext context, CrossServerMessage.G2PServerWorldLvChange messInfo);

    public void OnG2PReqCrossServerMatch(ChannelHandlerContext context,DailyactiveMessage.G2PReqCrossServerMatch mess);

    /**
     * boss刷新提示
     * @param groupID
     * @param bossID
     * @param type
     */
    void sendBossTipsToGame(int groupID,int bossID,int type);

    void onG2PDailyData(ChannelHandlerContext context, CrossServerMessage.G2PDailyData mess);

    void onG2PReqChatMess(ChannelHandlerContext context, CrossServerMessage.G2PReqChatMess messInfo);

    /**
     * 服务器广播notice
     * @param messInfo
     */
    void F2PServerNotice(ChatMessage.F2PServerNotice messInfo);

    /**
     * 社交服务器注册
     * @param context
     * @param messInfo
     */
    void S2PRegisterServerHandler(ChannelHandlerContext context, serverMessage.S2PRegisterServer messInfo);

    /**
     * 请求服务器列表
     * @param context
     * @param mess
     */
    void OnG2PReqFightServerList(ChannelHandlerContext context, serverMessage.G2PReqFightServerList mess);
}
