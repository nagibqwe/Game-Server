package com.game.couplefight.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.fightroom.structs.FightRoom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 冠军赛数据
 * @Auther: gouzhongliang
 * @Date: 2021/7/23 14:03
 */
public class ChampionData implements Comparator<CoupleTeam>{
    /**1天榜 2地榜*/
    private int type;
    /**轮次数据*/
    private List<ChampionGroup> rounds;
    /**当前轮次*/
    private int ground;
    /**是否结束*/
    private boolean over;
    /**玩家排名*/
    @JsonIgnore
    private transient List<CoupleTeam> teams;
    /**所在的准备房间*/
    @JsonIgnore
    private transient FightRoom preRoom;
    @JsonIgnore
    private transient CoupleData coupleData;

    public ChampionData(){}

    public ChampionData(List<CoupleTeam> teams){
        this.teams = teams;
        List<ChampionGroup> championGroups = new ArrayList<>(4);
        championGroups.add(new ChampionGroup(1, this.teams));
        this.rounds = championGroups;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public FightRoom getPreRoom() {
        return preRoom;
    }

    public void setPreRoom(FightRoom preRoom) {
        this.preRoom = preRoom;
    }

    public List<ChampionGroup> getRounds() {
        return rounds;
    }

    public void setRounds(List<ChampionGroup> rounds) {
        this.rounds = rounds;
    }

    public int getGround() {
        return ground;
    }

    public void setGround(int ground) {
        this.ground = ground;
    }

    public List<CoupleTeam> getTeams() {
        return teams;
    }

    public void setTeams(List<CoupleTeam> teams) {
        this.teams = teams;
    }

    public void sort() {
        this.teams.sort(this);
    }

    public CoupleData getCoupleData() {
        return coupleData;
    }

    public void setCoupleData(CoupleData coupleData) {
        this.coupleData = coupleData;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    @Override
    public int compare(CoupleTeam o1, CoupleTeam o2) {
        int s1 = o1.getChampionsInfo().getScore();
        int s2 = o2.getChampionsInfo().getScore();
        if(s1 < s2){
            return 1;
        }else if(s1 > s2){
            return -1;
        }
        if(o1.getNumber() > o2.getNumber()){
            return 1;
        }else{
            return -1;
        }
    }

    /**
     * 加载一些数据
     * @param d
     */
    public void load(CoupleData d) {
        this.coupleData = d;
        for(int i = 0; i < rounds.size(); i++){
            ChampionGroup r = rounds.get(i);
            r.setChampionType(this.type);
            if(i == 0){
                teams = new ArrayList<>();
                for(Long id : r.getTeamIds()){
                    teams.add(d.getTeams().get(id));
                }
            }
        }
    }
}
