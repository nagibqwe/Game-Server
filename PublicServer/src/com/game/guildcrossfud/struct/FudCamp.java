package com.game.guildcrossfud.struct;

import java.util.HashSet;

/**
 * @Desc TODO
 * @Date 2021/2/2 14:59
 * @Auth ZUncle
 */
public class FudCamp {

    int camp;                       //阵营ID
    HashSet<Integer> serverList = new HashSet<>();    //服务器

    public int getCamp() {
        return camp;
    }

    public void setCamp(int camp) {
        this.camp = camp;
    }

    public HashSet<Integer> getServerList() {
        return serverList;
    }

    public void setServerList(HashSet<Integer> serverList) {
        this.serverList = serverList;
    }

    @Override
    public String toString() {
        return "FudCamp{" +
                "camp=" + camp +
                ", serverList=" + serverList +
                '}';
    }
}
