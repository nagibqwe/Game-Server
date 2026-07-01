package com.game.heart.script;

import game.core.timer.TimerEvent;
import game.message.heartMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author admin
 */
public interface IHeartScript {
    
     void OnHeartReceive(ChannelHandlerContext session, heartMessage.ReqHeart heartInfo);

    /**
     * 玩家心跳脚本
     * @param event
     */
     void PlayerHeartTimer(TimerEvent event);
}
