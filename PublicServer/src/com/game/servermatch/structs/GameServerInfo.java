/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.servermatch.structs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 只需要存游戏服server
 * @author zhaibiao
 */
public class GameServerInfo {
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
    //管理的服务器列表
    private Set<Integer> sids = new HashSet<>();
        //游戏服的开服时间
    private String openTime;

    //服务器世界等级
    private int serverWorldLv = 0;

    private boolean isMerge = false;
    //大组ID，32个服务器为一个大组
    private int bigGroupID = 0;
    //6个阶段，对应的组ID索引
    public HashMap<Integer,Integer> stageWithGroupIndex  = new HashMap<>();
    //第一次连进公共服的时间
    public long firstConnectTime = 0l;

    public boolean isGMbackgroundSet = false;
    
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
    public String serverKey(){
        return platName +"_" +serverId;
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

    public Set<Integer> getSids() {
        return sids;
    }

    public void setSids(Set<Integer> sids) {
        this.sids = sids;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public void setServerWorldLv(int worldLv){this.serverWorldLv = worldLv;}

    public int getServerWorldLv(){return serverWorldLv;}

    public void setIsMerge(boolean isMerge){this.isMerge = isMerge;}

    public boolean getIsMerge(){return isMerge;}

    public void setbigGroupID(int bigGroupID){this.bigGroupID = bigGroupID;}

    public int getbigGroupID(){return bigGroupID;}

    public long getFirstConnectTime() {
        return firstConnectTime;
    }

    public void setFirstConnectTime(long firstConnectTime) {
        this.firstConnectTime = firstConnectTime;
    }

    public boolean isGMbackgroundSet() {
        return isGMbackgroundSet;
    }

    public void setGMbackgroundSet(boolean GMbackgroundSet) {
        isGMbackgroundSet = GMbackgroundSet;
    }
}
