package com.game.gameserver.structs;

import io.netty.channel.ChannelHandlerContext;
import java.util.HashSet;
import java.util.Set;

public class ServerInfo {

    //服务器编号
    private int serverId;
    //服务器类型: 游戏服， 战斗服
    private int serverType;
    //服务器IP
    private String serverIp;
    //服务器区域名称
    private String platName;
    //服务器端口号
    private int port;
    //服务器名字
    private String serverName;
    //版本号
    private String version;
    //当前在服务器的人数支持
    private int haveNum = 0;
    //管理的服务器列表
    private Set<Integer> sids = new HashSet<>();
    //管理的世界地图类型
    private Set<Integer> mapIds = new HashSet<>();
    //游戏服的开服时间
    private String openTime;
    //当前服务器连接
    private ChannelHandlerContext session;

    private int serverWorldLv = 0;//服务器世界等级

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getPlatName() {
        return platName;
    }

    public void setPlatName(String platName) {
        this.platName = platName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ChannelHandlerContext getSession() {
        return session;
    }

    public void setSession(ChannelHandlerContext session) {
        this.session = session;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getHaveNum() {
        return haveNum;
    }

    public void setHaveNum(int haveNum) {
        this.haveNum = haveNum;
    }

    public Set<Integer> getSids() {
        return sids;
    }

    public void setSids(Set<Integer> sids) {
        this.sids = sids;
    }

    public Set<Integer> getMapIds() {
        return mapIds;
    }

    public void setMapIds(Set<Integer> mapIds) {
        this.mapIds = mapIds;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public void setServerWorldLv(int serverWorldLv){this.serverWorldLv = serverWorldLv;}

    public  int getServerWorldLv(){return serverWorldLv;}
    

}
