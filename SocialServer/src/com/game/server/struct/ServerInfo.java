package com.game.server.struct;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Desc TODO
 * @Date 2021/6/9 16:00
 * @Auth ZUncle
 */
public class ServerInfo {

    String plat;
    int serverId;
    int serverType;
    ChannelHandlerContext session; //连接

    public String uniqueKey() {
        return plat + "_" + serverId;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public ChannelHandlerContext getSession() {
        return session;
    }

    public void setSession(ChannelHandlerContext session) {
        this.session = session;
    }
}
