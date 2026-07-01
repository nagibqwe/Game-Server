/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.http;

import game.core.script.IScript;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IHttpScript extends IScript {

    public void OnHttp(ChannelHandlerContext session, HttpRequest httpRequest);
}
