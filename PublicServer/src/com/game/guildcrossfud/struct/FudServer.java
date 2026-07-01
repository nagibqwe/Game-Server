package com.game.guildcrossfud.struct;

/**
 * @Desc TODO
 * @Date 2021/2/23 14:01
 * @Auth ZUncle
 */
public class FudServer {

    int serverId;   //服务器ID
    int stage;      //最大跨服阶段

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
