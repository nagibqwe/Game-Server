package com.game.guildcrossfud.struct;

import com.data.bean.Cfg_Cross_fudi_hold_reward_Bean;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/2/2 14:54
 * @Auth ZUncle
 */
public class FudCity extends DevilCity {

    int cityId;                 //福地ID
    int state;                  //城市状态 0=未占领 1=临时占领 2=已占领
    int camp;                   //占领福地的阵营
    HashSet<Integer> rootNode = new HashSet<>();                //根节点福地
    List<Long> rank = new ArrayList<>();                        //积分排名
    HashSet<Long>  openBoxSign = new HashSet<>();               //领奖记录
    @JsonIgnore
    transient long roomId;                                      //福地战场ID
    @JsonIgnore
    transient HashMap<Long, Integer>  score = new HashMap<>();        //玩家积分统计
    @JsonIgnore
    transient HashMap<Long, Integer>  kill = new HashMap<>();         //玩家击杀统计
    @JsonIgnore
    transient HashMap<Integer, FudBoss> boss = new HashMap<>();       //福地boss数据
    @JsonIgnore
    transient HashMap<Integer, Integer> enterRole = new HashMap<>();  //战场人数统计 serverId-> count
    @JsonIgnore
    transient HashMap<Integer, Integer> campScore = new HashMap<>();  //福地阵营积分 camp-> score
    @JsonIgnore
    transient Cfg_Cross_fudi_hold_reward_Bean cityBean;               //福地宝箱奖励
    @JsonIgnore
    transient long nextRefreshTime;                                   //福地Boss下一次刷新时间

    public long getNextRefreshTime() {
        return nextRefreshTime;
    }

    public void setNextRefreshTime(long nextRefreshTime) {
        this.nextRefreshTime = nextRefreshTime;
    }

    public HashMap<Long, Integer> getKill() {
        return kill;
    }

    public void setKill(HashMap<Long, Integer> kill) {
        this.kill = kill;
    }

    public HashSet<Integer> getRootNode() {
        return rootNode;
    }

    public void setRootNode(HashSet<Integer> rootNode) {
        this.rootNode = rootNode;
    }

    public Cfg_Cross_fudi_hold_reward_Bean getCityBean() {
        return cityBean;
    }

    public void setCityBean(Cfg_Cross_fudi_hold_reward_Bean cityBean) {
        this.cityBean = cityBean;
    }

    public HashMap<Integer, Integer> getCampScore() {
        return campScore;
    }

    public void setCampScore(HashMap<Integer, Integer> campScore) {
        this.campScore = campScore;
    }

    public HashMap<Integer, Integer> getEnterRole() {
        return enterRole;
    }

    public void setEnterRole(HashMap<Integer, Integer> enterRole) {
        this.enterRole = enterRole;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCamp() {
        return camp;
    }

    public void setCamp(int camp) {
        this.camp = camp;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public HashMap<Long, Integer> getScore() {
        return score;
    }

    public void setScore(HashMap<Long, Integer> score) {
        this.score = score;
    }

    public List<Long> getRank() {
        return rank;
    }

    public void setRank(List<Long> rank) {
        this.rank = rank;
    }

    public HashSet<Long> getOpenBoxSign() {
        return openBoxSign;
    }

    public void setOpenBoxSign(HashSet<Long> openBoxSign) {
        this.openBoxSign = openBoxSign;
    }

    public HashMap<Integer, FudBoss> getBoss() {
        return boss;
    }

    public void setBoss(HashMap<Integer, FudBoss> boss) {
        this.boss = boss;
    }

    @Override
    public String toString() {
        return "FudCity{" +
                "cityId=" + cityId +
                ", state=" + state +
                ", camp=" + camp +
                '}';
    }
}
