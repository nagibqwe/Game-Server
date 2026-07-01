package com.game.couplefight.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.db.bean.CouplefightBean;
import com.game.fightroom.structs.FightRoom;
import com.game.manager.Manager;
import com.game.utils.MessageUtils;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.CouplefightMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 预选赛数据
 * @Auther: gouzhongliang
 * @Date: 2021/7/3 11:04
 */
public class CoupleData {
    /**活动id*/
    private int activityId;
    /**服务器组id*/
    private int serverGroupId;
    /**服务器列表*/
    private List<String> serverIds = new ArrayList<>();
    /**创建时间*/
    private Date createDate;

    /**-------------海选赛信息------------*/
    //成功报名的队伍
    @JsonIgnore
    private transient Map<Long, CoupleTeam> teams = new ConcurrentHashMap<>();
    //玩家对应的队伍玩家id-队伍id
    @JsonIgnore
    private transient Map<Long, CoupleTeam> players = new ConcurrentHashMap<>();
    //队伍名称对应的战队信息
    @JsonIgnore
    private transient Map<String, CoupleTeam> names = new ConcurrentHashMap<>();
    //海选赛匹配队列
    @JsonIgnore
    private transient LinkedList<CoupleTeam> matchs = new LinkedList<>();
    /**海选赛排名信息*/
    private List<RankInfo> ranks = new ArrayList<>();

    /**-------------小组赛信息------------*/
    //分组情况
    private List<Group> groups = new ArrayList<>();
    //轮次计数
    private int groupsRound;
    //是否结束并结算
    private boolean groupOver = false;

    /**-------------冠军赛信息------------*/
    //地榜
    private ChampionData championDi;
    //天榜
    private ChampionData championTian;
    //粉丝数据
    @JsonIgnore
    private transient ChampionFansData championFansData = new ChampionFansData();
    /**-------------end------------*/

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public int getGroupsRound() {
        return groupsRound;
    }

    public void setGroupsRound(int groupsRound) {
        this.groupsRound = groupsRound;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(int serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public List<String> getServerIds() {
        return serverIds;
    }

    public void setServerIds(List<String> serverIds) {
        this.serverIds = serverIds;
    }

    public Map<Long, CoupleTeam> getTeams() {
        return teams;
    }

    public Map<Long, CoupleTeam> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Long, CoupleTeam> players) {
        this.players = players;
    }

    public Map<String, CoupleTeam> getNames() {
        return names;
    }

    public void setNames(Map<String, CoupleTeam> names) {
        this.names = names;
    }

    public LinkedList getMatchs() {
        return matchs;
    }

    public boolean isGroupOver() {
        return groupOver;
    }

    public void setGroupOver(boolean groupOver) {
        this.groupOver = groupOver;
    }

    public ChampionData getChampionDi() {
        return championDi;
    }

    public void setChampionDi(ChampionData championDi) {
        this.championDi = championDi;
    }

    public ChampionData getChampionTian() {
        return championTian;
    }

    public void setChampionTian(ChampionData championTian) {
        this.championTian = championTian;
    }

    public ChampionFansData getChampionFansData() {
        return championFansData;
    }

    public void setChampionFansData(ChampionFansData championFansData) {
        this.championFansData = championFansData;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 添加报名队伍
     * @param team
     */
    public synchronized void addTeam(CoupleTeam team) {
        teams.put(team.getId(),team);
        players.put(team.getMen().getId(), team);
        players.put(team.getWomen().getId(), team);
        names.put(team.getName(),team);
    }

    /**
     * 添加匹配队伍
     * @param team
     */
    public synchronized void addMatch(CoupleTeam team){
        team.getTrialsInfo().setTime(TimeUtils.Time());
        team.setRoom(null);
        team.getMen().setConfirm(null);
        team.getWomen().setConfirm(null);
        if(!matchs.contains(team)){
            matchs.add(team);
        }
    }

    /**
     * 添加匹配队伍
     * @param team
     */
    public synchronized void removeMatch(CoupleTeam team){
        team.getTrialsInfo().setTime(0);
        matchs.remove(team);
    }

    public synchronized void matchOver(CoupleTeam win, CoupleTeam lose){

    }

    public List<RankInfo> getRanks() {
        return ranks;
    }

    public void setRanks(List<RankInfo> ranks) {
        this.ranks = ranks;
    }

    public synchronized void initGroupInfo(List<CoupleTeam> teams){
        Manager.couplefightManager.getScript().initGroupInfo(this, teams);
    }

    public void initChampionInfo(List<CoupleTeam> dis, List<CoupleTeam> tians) {
        Manager.couplefightManager.getScript().initChampionInfo(this, dis, tians);
    }

    /**
     * 获取数据
     * @param type 1天榜 2地榜
     * @return
     */
    public ChampionData getChampionInfoByType(int type) {
        if(type == 1){
            return getChampionTian();
        }else{
            return getChampionDi();
        }
    }

    public CouplefightMessage.ResChampionInfo.Builder toProtoChampions(Integer type) {
        CouplefightMessage.ResChampionInfo.Builder res = CouplefightMessage.ResChampionInfo.newBuilder();
        res.setType(type);
        ChampionData data = getChampionInfoByType(type);
        if(data == null){
            return null;
        }
        ChampionGroup g = data.getRounds().get(data.getRounds().size() - 1);
        res.setRound(g.getRound());
        res.setRounds(g.toProro(this));
        return res;
    }

}
