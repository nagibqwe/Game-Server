package com.game.couplefight.struct;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/7/7 17:47
 */
public class CouplefightPlayer {
    /**玩家的id*/
    private long id;
    /**阵营*/
    private int camp;
    /**是否机器人*/
    private boolean robot;
    /**是否死亡*/
    private boolean dead;
    /**输赢*/
    private boolean win;
    /**剩余生命百分比*/
    private double hpPercent;
    /**战斗力*/
    private long fightPower;
    /**等级*/
    private int level;

    public CouplefightPlayer(long id, int camp, boolean robot){
        this.id = id;
        this.camp = camp;
        this.robot = robot;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCamp() {
        return camp;
    }

    public void setCamp(int camp) {
        this.camp = camp;
    }

    public boolean getDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isRobot() {
        return robot;
    }

    public void setRobot(boolean robot) {
        this.robot = robot;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public double getHpPercent() {
        return hpPercent;
    }

    public void setHpPercent(double hpPercent) {
        this.hpPercent = hpPercent;
    }

    public long getFightPower() {
        return fightPower;
    }

    public void setFightPower(long fightPower) {
        this.fightPower = fightPower;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "CouplefightPlayer{" +
                "id=" + id +
                ", camp=" + camp +
                ", robot=" + robot +
                ", dead=" + dead +
                '}';
    }
}
