/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.utils;

import com.game.server.ClientHandlerAdapter;
import game.core.message.SMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author Administrator
 */
public class MessageUtil {
    public static void SendMess(ChannelHandlerContext ctx, SMessage msg)
    {
        ClientHandlerAdapter.SendMessage(ctx, msg);
    }
}
