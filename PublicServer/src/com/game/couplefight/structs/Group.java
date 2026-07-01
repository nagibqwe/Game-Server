package com.game.couplefight.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.fightroom.structs.FightRoom;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 小组赛分组信息
 * @Auther: gouzhongliang
 * @Date: 2021/7/13 10:14
 */
public class Group implements Comparator<CoupleTeam> {
    /**ID*/
    private int id;
    /**战斗轮次信息*/
    private List<GroupRound> rounds;
    /**是否结束所有轮次*/
    private boolean over = false;
    /**队伍id*/
    private List<Long> teamIds;
    /**所在的准备房间*/
    @JsonIgnore
    private transient FightRoom preRoom;
    /**十只队伍*/
    @JsonIgnore
    private transient List<CoupleTeam> teams = new ArrayList<>();

    public List<CoupleTeam> getTeams() {
        return teams;
    }

    public void setTeams(List<CoupleTeam> teams) {
        this.teams = teams;
    }

    public List<GroupRound> getRounds() {
        return rounds;
    }

    public void setRounds(List<GroupRound> rounds) {
        this.rounds = rounds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FightRoom getPreRoom() {
        return preRoom;
    }

    public void setPreRoom(FightRoom preRoom) {
        this.preRoom = preRoom;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public List<Long> getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(List<Long> teamIds) {
        this.teamIds = teamIds;
    }

    public void init() {
        this.teamIds = new ArrayList<>();
        for(CoupleTeam team : teams){
            teamIds.add(team.getId());
        }
        CoupleTeam[] arr = new CoupleTeam[10];
        this.teams.toArray(arr);
        List<GroupRound> rounds = new ArrayList<>();
        rounds.add(new GroupRound(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8], arr[9]));
        rounds.add(new GroupRound(arr[0], arr[2], arr[1], arr[3], arr[4], arr[6], arr[7], arr[9], arr[8], arr[5]));
        rounds.add(new GroupRound(arr[0], arr[3], arr[2], arr[4], arr[1], arr[7], arr[6], arr[8], arr[5], arr[9]));
        rounds.add(new GroupRound(arr[0], arr[4], arr[2], arr[5], arr[3], arr[7], arr[6], arr[9], arr[8], arr[1]));
        rounds.add(new GroupRound(arr[0], arr[5], arr[2], arr[6], arr[4], arr[7], arr[1], arr[9], arr[8], arr[3]));
        rounds.add(new GroupRound(arr[0], arr[6], arr[2], arr[7], arr[4], arr[8], arr[1], arr[5], arr[3], arr[9]));
        rounds.add(new GroupRound(arr[0], arr[7], arr[2], arr[8], arr[4], arr[9], arr[6], arr[1], arr[3], arr[5]));
        rounds.add(new GroupRound(arr[0], arr[8], arr[2], arr[9], arr[4], arr[1], arr[6], arr[3], arr[5], arr[7]));
        rounds.add(new GroupRound(arr[0], arr[9], arr[2], arr[1], arr[4], arr[3], arr[6], arr[5], arr[8], arr[7]));
        this.rounds = rounds;
//        check();
    }

    private void check() {
        Set<String> keys = new HashSet<>();
        for(GroupRound round : rounds){
            for(CoupleFightRoom room : round.getRooms()){
                int index1 = teamIds.indexOf(room.getT1());
                int index2 = teamIds.indexOf(room.getT2());
                if(index1 < index2){
                    keys.add(index1 + "_" + index2);
                }else{
                    keys.add(index2 + "_" + index1);
                }
            }
        }
        System.out.println(keys);
        System.out.println(keys.size());
    }

    /**
     * 更新排名信息
     */
    public void updateRank() {
        teams.sort(this);
    }

    @Override
    public int compare(CoupleTeam t1, CoupleTeam t2) {
        int s1 = t1.getGroupsInfo().getScore();
        int s2 = t2.getGroupsInfo().getScore();
        if(s1 > s2){
            return -1;
        }else if(s1 < s2){
            return 1;
        }
        return 0;
    }

//    public static void main(String[] args) {
//        Group group = new Group();
//        List<CoupleTeam> ls = new ArrayList<>();
//        ls.add(new CoupleTeam());
//        ls.add(new CoupleTeam());
//        ls.add(new CoupleTeam());
//        ls.add(new CoupleTeam());
//        ls.add(new CoupleTeam());
//        ls.add(new CoupleTeam());
//        ls.add(new CoupleTeam());
//        ls.add(new CoupleTeam());
//        ls.add(new CoupleTeam());
//        ls.add(new CoupleTeam());
//        group.setTeams(ls);
//        group.init();
//    }

}
