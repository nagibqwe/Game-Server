package com.game.couplefight.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * 仙侣对决战斗队伍
 * @Auther: gouzhongliang
 * @Date: 2021/10/13 11:01
 */
public class FightTeam {
    //成员
    private List<CouplefightPlayer> players = new ArrayList<>(2);
    //阵营
    private int camp;
    //血量总百分比
    private double hpPercent = 0;
    //总战力
    private long fight = 0;
    //总等级
    private int level = 0;

    public FightTeam(int camp){
        this.camp = camp;
    }

    public void add(CouplefightPlayer player){
        this.players.add(player);
    }

    public List<CouplefightPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<CouplefightPlayer> players) {
        this.players = players;
    }

    public int getCamp() {
        return camp;
    }

    public void setCamp(int camp) {
        this.camp = camp;
    }

    public double getHpPercent() {
        return hpPercent;
    }

    public void setHpPercent(double hpPercent) {
        this.hpPercent = hpPercent;
    }

    public long getFight() {
        return fight;
    }

    public void setFight(long fight) {
        this.fight = fight;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
