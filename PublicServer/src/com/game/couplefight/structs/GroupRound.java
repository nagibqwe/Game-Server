package com.game.couplefight.structs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 小组轮次
 * @Auther: gouzhongliang
 * @Date: 2021/7/29 11:55
 */
public class GroupRound {
    private List<CoupleFightRoom> rooms = new ArrayList<>();

    public GroupRound(){}

    public GroupRound(CoupleTeam... teams){
        int size = teams.length;
        for(int i=0; i<size; i++){
            rooms.add(new CoupleFightRoom(teams[i], teams[i+1]));
            i++;
        }
    }

    public List<CoupleFightRoom> getRooms() {
        return rooms;
    }

    public void setRooms(List<CoupleFightRoom> rooms) {
        this.rooms = rooms;
    }
}
