package com.game.eightdiagrams.structs;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 542 on 2019/9/23.
 */
public class EightDiageramSeverInfo {

    //服务器编号
    private int serverId = 0;

    private String serverName = "";

    //服务器区域名称
    private String platName = "";

    private int campColor  = 0;

    public void setServerId(int serverId){this.serverId = serverId;}

    public int getServerId(){return serverId;}

    public void setPlatName(String platName){this.platName = platName;}

    public String getPlatName(){return  platName;}

    public void setServerName(String serverName){this.serverName = serverName;}

    public String getServerName(){return serverName;}

    public void setCampColor(int campColor){this.campColor = campColor;}

    public int getCampColor(){return campColor;}

}
