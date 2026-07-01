/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.script;

import game.core.script.IScript;
import game.message.CrossServerMessage.G2PGMCMD;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IGMScript extends IScript {

    public void OnDeal(ChannelHandlerContext context, G2PGMCMD mess);
    
    public void dealGm(ChannelHandlerContext context, long roleId, String cmd);
}
