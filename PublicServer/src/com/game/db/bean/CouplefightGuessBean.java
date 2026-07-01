package com.game.db.bean;

import com.game.couplefight.structs.CoupleData;
import game.core.util.JsonUtils;

/**
 * 仙侣对决竞猜信息
 * @Auther: gouzhongliang
 * @Date: 2021/7/27 18:12
 */
public class CouplefightGuessBean {

    private int id;
    /**活动id*/
    private int activityId;
    /**服务器组*/
    private int group;
    /**1天 2地*/
    private int type;
    /**轮次*/
    private int round;
    /**战斗id*/
    private int fightId;
    /**队伍id*/
    private long teamId;
    /**玩家id*/
    private long rid;
    /**名称*/
    private String name;
    /**等级*/
    private int level;
    /**战力*/
    private long power;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getFightId() {
        return fightId;
    }

    public void setFightId(int fightId) {
        this.fightId = fightId;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getPower() {
        return power;
    }

    public void setPower(long power) {
        this.power = power;
    }
}
