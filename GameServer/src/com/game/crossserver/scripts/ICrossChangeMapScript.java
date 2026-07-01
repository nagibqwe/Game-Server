/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.crossserver.scripts;

import game.message.CrossFightMessage.F2GEnterCloneMapRes;
import game.message.CrossFightMessage.G2FEnterCloneMap;
import game.message.CrossFightMessage.G2FOnEnterMapAgain;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author soko <xuchangming@haowan123.com>
 */
public interface ICrossChangeMapScript {

    void OnG2FEnterCloneMap(ChannelHandlerContext context, G2FEnterCloneMap mess);

    void OnEnterMapAgain(ChannelHandlerContext context, G2FOnEnterMapAgain mess);

    void OnF2GEnterCloneMapRes(ChannelHandlerContext context, F2GEnterCloneMapRes mess);

}
