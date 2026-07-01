package com.game.couplefight.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.gameserver.manager.GameServerManager;
import game.message.CouplefightMessage;

/**
 * 仙侣队伍
 * @Auther: gouzhongliang
 * @Date: 2021/7/1 14:12
 */
public class CoupleTeam {
    /**队伍id*/
    private long id;
    /**战队名称*/
    private String name;
    /**服务器id*/
    private int serverId;
    /**平台名称*/
    private String platName;
    /**是否为机器人队伍*/
    private transient boolean robot = false;
    /**队伍编号*/
    private int number;

    /**男玩家*/
    private Player men;
    /**女玩家*/
    private Player women;
    /**预选赛信息*/
    private TeamTrialsInfo trialsInfo = new TeamTrialsInfo();
    /**小组赛信息*/
    private TeamGroupsInfo groupsInfo;
    /**冠军赛信息*/
    private TeamChampionsInfo championsInfo;

    /**战斗房间*/
    @JsonIgnore
    private transient CoupleFightRoom room;

    public CoupleTeam(){}

    public CoupleTeam(int id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getServerId() {
        return serverId;
    }

    public String getServerPlatId() {
        return GameServerManager.getInstance().makeKey(platName,serverId);
    }

    public String getPlatName() {
        return platName;
    }

    public void setPlatName(String platName) {
        this.platName = platName;
    }

    public boolean isRobot() {
        return robot;
    }

    public void setRobot(boolean robot) {
        this.robot = robot;
    }

    public Player getMen() {
        return men;
    }

    public void setMen(Player men) {
        this.men = men;
        men.setTeam(this);
    }

    public Player getWomen() {
        return women;
    }

    public void setWomen(Player women) {
        this.women = women;
        women.setTeam(this);
    }

    public CoupleFightRoom getRoom() {
        return room;
    }

    public void setRoom(CoupleFightRoom room) {
        this.room = room;
    }

    public TeamTrialsInfo getTrialsInfo() {
        return trialsInfo;
    }

    public void setTrialsInfo(TeamTrialsInfo trialsInfo) {
        this.trialsInfo = trialsInfo;
    }

    public TeamGroupsInfo getGroupsInfo() {
        return groupsInfo;
    }

    public void setGroupsInfo(TeamGroupsInfo groupsInfo) {
        this.groupsInfo = groupsInfo;
    }

    public TeamChampionsInfo getChampionsInfo() {
        return championsInfo;
    }

    public void setChampionsInfo(TeamChampionsInfo championsInfo) {
        this.championsInfo = championsInfo;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Player getPlayerById(long id){
        if(men != null && men.getId() == id){
            return men;
        }else if(women != null && women.getId() == id){
            return women;
        }
        return null;
    }

    public CouplefightMessage.TeamInfo.Builder toProto() {
        CouplefightMessage.TeamInfo.Builder obj = CouplefightMessage.TeamInfo.newBuilder();
        obj.setId(this.id);
        obj.setName(this.name);
        obj.addRoles(men.toProto());
        obj.addRoles(women.toProto());
        return obj;
    }

    public CouplefightMessage.TrialsInfo.Builder toTrialsProto() {
        CouplefightMessage.TrialsInfo.Builder obj = CouplefightMessage.TrialsInfo.newBuilder();
        obj.setCount(trialsInfo.getCount());
        obj.setScore(trialsInfo.getScore());
        if(trialsInfo.getCount() == 0){
            obj.setRate(0);
        }else {
            obj.setRate(trialsInfo.getWinCount() * 100 / trialsInfo.getCount());
        }
        obj.setRank(trialsInfo.getRank());
        return obj;
    }

    public CouplefightMessage.GroupTeam.Builder toGroupsProto() {
        CouplefightMessage.GroupTeam.Builder obj = CouplefightMessage.GroupTeam.newBuilder();
        obj.setScore(groupsInfo.getScore());
        if(groupsInfo.getCount() == 0){
            obj.setRate(0);
        }else{
            obj.setRate(groupsInfo.getWinCount() * 100 / groupsInfo.getCount());
        }
        obj.setTeamId(getId());
        return obj;
    }


    @Override
    public String toString() {
        return hashCode() + "-" + id;
    }

}
