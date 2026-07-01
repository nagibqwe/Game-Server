package com.game.couplefight.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.fightroom.structs.FightRoom;

/**
 * 仙侣对决战斗房间
 * @Auther: gouzhongliang
 * @Date: 2021/7/6 10:51
 */
public class CoupleFightRoom {

    /**战斗房间id*/
    private long roomId;
    /**队伍1*/
    private long t1;
    /**队伍2*/
    private long t2;
    /**创建时间*/
    private long time;
    /**是否结束*/
    private boolean over = false;

    @JsonIgnore
    private transient CoupleTeam robot;

    public CoupleFightRoom(){}

    public CoupleFightRoom(CoupleTeam t1, CoupleTeam t2){
        this(null, t1,t2);
    }

    public CoupleFightRoom(FightRoom room, CoupleTeam t1, CoupleTeam t2){
        if(room != null){
            this.roomId = room.getFid();
        }
        if(t1 != null){
            this.t1 = t1.getId();
            if(t1.isRobot()){
                robot = t1;
            }
        }
        if(t2 != null){
            this.t2 = t2.getId();
            if(t2.isRobot()){
                robot = t2;
            }
        }
    }

    public long getT1() {
        return t1;
    }

    public void setT1(long t1) {
        this.t1 = t1;
    }

    public long getT2() {
        return t2;
    }

    public void setT2(long t2) {
        this.t2 = t2;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }
}
