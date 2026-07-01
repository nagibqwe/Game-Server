package com.game.couplefight.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.fightroom.structs.FightRoom;
import game.message.CouplefightMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 冠军赛分组信息
 * @Auther: gouzhongliang
 * @Date: 2021/7/19 10:20
 */
public class ChampionGroup {

    /**第几轮 1开始 最多4轮*/
    private int round;
    /**队伍id*/
    private List<Long> teamIds;
    /**房间信息*/
    private List<ChampionRoom> rooms = new ArrayList<>();
    /**冠军赛类型 1天 2地*/
    private int championType;

    public ChampionGroup(){}

    public ChampionGroup(int round, List<CoupleTeam> teams){
        this.round = round;
        teamIds = new ArrayList<>();
        for(CoupleTeam t : teams){
            if(t != null){
                teamIds.add(t.getId());
            }
        }
        init(new ArrayList<>(teams));
    }

    public ChampionGroup(int round){
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public List<Long> getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(List<Long> teamIds) {
        this.teamIds = teamIds;
    }

    public List<ChampionRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<ChampionRoom> rooms) {
        this.rooms = rooms;
    }

    public int getChampionType() {
        return championType;
    }

    public void setChampionType(int championType) {
        this.championType = championType;
    }

    private void init(List<CoupleTeam> teams) {
        int size = 0;
        //战斗房间
        switch (round){
            case 1://八强
                size = 16;
                //补全队伍
                for(int i = teams.size(); i < size; i++){
                    teams.add(null);
                }
                //打乱排序
                Collections.shuffle(teams);
                //设置队伍序号
                int number = 0;
                for(CoupleTeam t : teams){
                    number++;
                    if(t != null){
                        t.setNumber(number);
                    }
                }
                break;
            case 2://四强
                size = 8;
                rooms = new ArrayList<>(4);

                break;
            case 3://半决赛
                size = 4;
                rooms = new ArrayList<>(2);
                break;
            case 4://决赛
                size = 2;
                rooms = new ArrayList<>(1);
                break;
        }

        int length = size / 2;
        rooms = new ArrayList<>(length);
        for(int i = 1; i <= length; i++){
            rooms.add(new ChampionRoom( round * 100 + i, null, teams.get(i*2-2), teams.get(i*2-1)));
        }

    }

    /**
     * 获取对战
     * @param fightId
     */
    public ChampionRoom getFight(int fightId) {
        for(ChampionRoom r : rooms){
            if(r.getId() == fightId){
                return r;
            }
        }
        return null;
    }

    public CouplefightMessage.ChampionRound.Builder toProro(CoupleData data) {
        CouplefightMessage.ChampionRound.Builder round = CouplefightMessage.ChampionRound.newBuilder();
        for(ChampionRoom group : rooms){
            round.addGroups(group.toProtoInfo(data));
        }
        return round;
    }

}
