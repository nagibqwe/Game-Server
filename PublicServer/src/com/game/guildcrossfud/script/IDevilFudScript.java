package com.game.guildcrossfud.script;

import game.core.script.IScript;
import game.message.GuildCrossFudMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Desc TODO
 * @Date 2021/4/27 20:52
 * @Auth ZUncle
 */
public interface IDevilFudScript extends IScript {


    /**
     * 战斗服 同步福地数据到公共服
     * @param context
     * @param mess
     */
    void F2PCrossFudInfo(ChannelHandlerContext context, GuildCrossFudMessage.F2PCrossFudInfo mess);


    /**
     * 活动开启
     */
    void activeBegin();

    /**
     * 活动关闭
     */
    void activeEnd( );

}
