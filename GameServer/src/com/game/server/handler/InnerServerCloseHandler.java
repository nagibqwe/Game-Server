/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.handler;

import com.game.server.GameServer;
import game.core.command.ICommand;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author Administrator
 */
public class InnerServerCloseHandler implements ICommand {

    private final ChannelHandlerContext session;
    private final int type;

    public InnerServerCloseHandler(ChannelHandlerContext session, int type) {
        this.session = session;
        this.type = type;
    }

    @Override
    public void action() {

        if (session == null) {
            return;
        }

        //断开连接，置空数据
        GameServer.getInstance().ConnectionOut(type, session);

    }

}
