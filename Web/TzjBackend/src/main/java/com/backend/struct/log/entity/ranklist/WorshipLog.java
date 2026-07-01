package com.backend.struct.log.entity.ranklist;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 崇拜日志
 */
@Table(name = "worshiplog", tableType = TableType.Month)
public class WorshipLog implements IConvertor {

    @FieldDesc
    private long playerId;                  //崇拜玩家Id

    @FieldDesc
    private long worshipPlayerId;           //被崇拜玩家Id

    @FieldDesc
    private int worshipDay;                 //崇拜时间（1970至今的天数）

    @FieldDesc
    private String worshippedPlayerIdSet;   //今日所崇拜的玩家Id列表(player.getWorshipRoleIdSet() JSON化String后的存储)

    @FieldDesc
    private int todayWorshipNum;            //今日已崇拜次数

    @FieldDesc(desc = "logentity.commonlogbean.time")
    private long time;                      //时间

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getWorshipPlayerId() {
        return worshipPlayerId;
    }

    public void setWorshipPlayerId(long worshipPlayerId) {
        this.worshipPlayerId = worshipPlayerId;
    }

    public int getWorshipDay() {
        return worshipDay;
    }

    public void setWorshipDay(int worshipDay) {
        this.worshipDay = worshipDay;
    }

    public String getWorshippedPlayerIdSet() {
        return worshippedPlayerIdSet;
    }

    public void setWorshippedPlayerIdSet(String worshippedPlayerIdSet) {
        this.worshippedPlayerIdSet = worshippedPlayerIdSet;
    }

    public int getTodayWorshipNum() {
        return todayWorshipNum;
    }

    public void setTodayWorshipNum(int todayWorshipNum) {
        this.todayWorshipNum = todayWorshipNum;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
