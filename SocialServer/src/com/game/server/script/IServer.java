package com.game.server.script;

import com.game.server.struct.ServerParams;
import game.core.message.RMessage;
import game.core.script.IScript;
import game.message.serverMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Desc TODO
 * @Date 2021/6/8 17:49
 * @Auth ZUncle
 */
public interface IServer extends IScript {

    /**
     * 服务器心跳
     */
    void doHeart();

    /**
     * 消息派发
     * @param msg
     */
    void dispatch(RMessage msg);

    /**
     * 连接注册
     * @param context
     */
    void register(ChannelHandlerContext context, serverMessage.gameServerInfo mess);

    /**
     * 连接关闭九零  一起玩 www.901  75.com
     * @param context
     * @param type
     */
    void close(ChannelHandlerContext context, int type);

    /**
     * 注册到公共服
     */
    void register2Public();

    /**
     * 加载服务器参数
     */
    void loadServerParams();

    /**
     * 保存服务器参数
     * @param key
     */
    void saveServerParams(ServerParams key);

}
