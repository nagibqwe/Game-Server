/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.http.script;

import game.core.script.IScript;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * 执行HTTP消息
 */
public interface IHttpScript extends IScript {

    void onHttp(ChannelHandlerContext session, HttpRequest httpRequest);

    /**
     * 加载屏蔽账号数据和白名单数据
     */
    void loadDatas();
}
