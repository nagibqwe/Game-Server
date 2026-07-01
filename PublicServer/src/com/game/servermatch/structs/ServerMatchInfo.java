package com.game.servermatch.structs;

/**
 * Created by cxl on 2019/8/29.
 */
public class ServerMatchInfo {

    //服务器编号
    private int serverId;

    //服务器名字
    private String serverName;

    //服务器世界等级
    private int serverWorldLv = 0;

    public void setServerId(int serverId){this.serverId = serverId;}

    public int getServerId(){return serverId;}

    public void setServerName(String serverName){this.serverName = serverName;}

    public String getServerName(){return serverName;}

    public void setServerWorldLv(int serverWorldLv){this.serverWorldLv = serverWorldLv;}

    public int getServerWorldLv(){return serverWorldLv;}


}
