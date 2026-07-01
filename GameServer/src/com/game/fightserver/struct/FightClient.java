/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.fightserver.struct;

/**
 * 游戏服的信息
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class FightClient {

    private volatile int sid;//服务器ID
    private volatile String plat;//服务器平台标识
    private String ip;//服务器IP
    private int port;//服务器端口
    private String version;//服务器版本
    private volatile String serverNamePrefix = "";//服务器名字前缀
    private String serverName = "";

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServerNamePrefix() {
        return serverNamePrefix;
    }

    public void setServerNamePrefix(String serverNamePrefix) {
        this.serverNamePrefix = serverNamePrefix;
    }


    public void setServerName(String serverName){this.serverName = serverName;}

    public String getServerName(){return serverName;}

}
