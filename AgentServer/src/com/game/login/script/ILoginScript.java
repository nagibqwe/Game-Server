package com.game.login.script;

import game.message.LoginMessage;
import io.netty.channel.ChannelHandlerContext;

public interface ILoginScript {

    void check(ChannelHandlerContext iosession, LoginMessage.ReqLogin messInfo);
    
}
