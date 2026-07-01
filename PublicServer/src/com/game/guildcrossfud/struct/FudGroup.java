package com.game.guildcrossfud.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO  福地分组
 * @Date 2021/2/2 15:56
 * @Auth ZUncle
 */
public class FudGroup extends AlienGroup {

    //组ID
    @JsonIgnore
    transient int groupId;
    //数据
    @JsonIgnore
    transient String data;
    //跨服阶段
    int stage;
    //开服天数
    int openDay;
    //世界等级
    int worldLevel;
    //福地列表
    ConcurrentHashMap<Integer, FudCity> city = new ConcurrentHashMap<>();
    //阵营分组
    ConcurrentHashMap<Integer, FudCamp> camp = new ConcurrentHashMap<>();
    //服务器索引阵营 serverId->campId
    ConcurrentHashMap<Integer, Integer> serverCamp = new ConcurrentHashMap<>();
    //击杀排名
    @JsonIgnore
    transient List<FudRole> killRank = new ArrayList<>();
    //积分排名
    @JsonIgnore
    transient List<FudRole> scoreRank = new ArrayList<>();

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getOpenDay() {
        return openDay;
    }

    public void setOpenDay(int openDay) {
        this.openDay = openDay;
    }

    public int getWorldLevel() {
        return worldLevel;
    }

    public void setWorldLevel(int worldLevel) {
        this.worldLevel = worldLevel;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public ConcurrentHashMap<Integer, FudCity> getCity() {
        return city;
    }

    public void setCity(ConcurrentHashMap<Integer, FudCity> city) {
        this.city = city;
    }

    public ConcurrentHashMap<Integer, FudCamp> getCamp() {
        return camp;
    }

    public void setCamp(ConcurrentHashMap<Integer, FudCamp> camp) {
        this.camp = camp;
    }

    public ConcurrentHashMap<Integer, Integer> getServerCamp() {
        return serverCamp;
    }

    public void setServerCamp(ConcurrentHashMap<Integer, Integer> serverCamp) {
        this.serverCamp = serverCamp;
    }

    public List<FudRole> getKillRank() {
        return killRank;
    }

    public void setKillRank(List<FudRole> killRank) {
        this.killRank = killRank;
    }

    public List<FudRole> getScoreRank() {
        return scoreRank;
    }

    public void setScoreRank(List<FudRole> scoreRank) {
        this.scoreRank = scoreRank;
    }

    public FudCamp find(int serverId) {
        Integer camp = serverCamp.get(serverId);
        if (camp == null) {
            return null;
        }
        return this.camp.get(camp);
    }

    @Override
    public String toString() {
        List<Integer> servers = new ArrayList<>();
        serverCamp.forEach((k, v) -> servers.add(k));
        return "FudGroup{" +
                "groupId=" + groupId +
                ", stage=" + stage +
                ", openDay=" + openDay +
                ", worldLevel=" + worldLevel +
                ", servers=" + servers +
                '}';
    }
}
