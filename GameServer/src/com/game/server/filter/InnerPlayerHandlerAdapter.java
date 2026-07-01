/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.filter;

import com.game.server.GameServer;
import game.core.message.OtherServerToPlayerMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class InnerPlayerHandlerAdapter extends SimpleChannelInboundHandler<OtherServerToPlayerMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext chc, OtherServerToPlayerMessage i) throws Exception {
        GameServer.worldExec(chc, i);
    }
}
