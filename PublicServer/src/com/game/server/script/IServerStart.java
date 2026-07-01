package com.game.server.script;

import game.core.script.IScript;
import game.message.serverMessage.G2PReqFightServer;
import game.message.serverMessage.G2PReqFightServerList;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author
 */
public interface IServerStart extends IScript{
     void tick(long curtime);

     void OnSessionOut(ChannelHandlerContext context, int type);
    
     void OnG2PReqFightServer(ChannelHandlerContext context, G2PReqFightServer mess);
}
