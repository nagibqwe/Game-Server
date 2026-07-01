package com.game.backgrand.script;

import game.core.script.IScript;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Desc TODO
 * @Date 2021/6/8 16:36
 * @Auth ZUncle
 */
public interface IBackGrand extends IScript {

    /**
     * 处理消息
     * @param session
     * @param httpRequest
     */
    void cmd(ChannelHandlerContext session, HttpRequest httpRequest);

}
