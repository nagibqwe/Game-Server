package com.game.gm.script;

import game.core.script.IScript;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Desc TODO
 * @Date 2021/6/24 15:01
 * @Auth ZUncle
 */
public interface IGmScript extends IScript {

    /**
     * 处理命令
     *
     * @param cmd
     */
    void cmd(String cmd);

    /**
     * 处理命令
     * @param context
     * @param roleId
     * @param cmd
     */
    void cmd(ChannelHandlerContext context, long roleId, String cmd);

}
