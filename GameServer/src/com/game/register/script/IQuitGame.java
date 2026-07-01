package com.game.register.script;

import io.netty.channel.ChannelHandlerContext;

/**
 * 游戏退出的接口
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IQuitGame {

     void QuitGame(ChannelHandlerContext context, int reason, boolean isQuit,boolean isSendMsg);
    
}
