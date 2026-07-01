/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.script;

import game.message.serverMessage.F2GResRegister;
import game.message.serverMessage.G2FReqRegister;
import game.message.serverMessage.P2GResFightServer;
import game.message.serverMessage.P2GResFightServerList;
import game.message.serverMessage.P2GResRegister;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IServerScript {

    public void OnF2GResRegister(ChannelHandlerContext context, F2GResRegister messInfo);

    public void OnG2FReqRegister(ChannelHandlerContext context, G2FReqRegister messInfo);

    public void OnP2GResFightServer(ChannelHandlerContext context, P2GResFightServer messInfo);

    public void OnP2GResFightServerList(ChannelHandlerContext context, P2GResFightServerList messInfo);

    public void OnP2GResRegister(ChannelHandlerContext context, P2GResRegister messInfo);

    public void OnFightSessionOut(ChannelHandlerContext session, int serverId);

    /**
     * 注册到社交服务器
     * @param context
     */
    void G2SRegisterServer(ChannelHandlerContext context);

    public String getStr(String tableName, int id);

    public String getDragoonName(int career, int dragoonId);

    public String getLanguage(String tableName, int id, long roleId);

    public String getLanguage(String tableName, String id);

    public String getNotHaveMarkStr(String tableName, int id);

    String getItemString(int itemModelId, String lang, String name);

    //加载服务器指定的多语言
    public void loadMutilLang();

    //请求apiServer,加载服务器id列表（包含本服id和合服id）
    public void loadServerIdList();
}
