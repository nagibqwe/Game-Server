package com.game.copymap.structs;

import com.game.boss.struct.Boss;
import com.game.monster.structs.Monster;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @Desc TODO
 * @Date 2021/11/24 17:18
 * @Auth ZUncle
 */
public class AlienCopyData extends ZoneCache {

    int group;
    int city;
    HashMap<Long, Boss> monster = new HashMap<>();
    HashMap<Integer, Boss> boss = new HashMap<>();
    HashMap<Integer, Integer> bossKill = new HashMap<>();     //Boss击杀服务器
    HashMap<Integer, Integer> server = new HashMap<>();    //服务器列表
    int extraServerId;          //占领服务器
    int totalScore = 0;         //幻境总积分

    HashSet<Monster> beAttack = new HashSet<>();           //受攻击boss
    HashSet<Monster> state = new HashSet<>();              //boss状态刷新

    public HashMap<Integer, Integer> getBossKill() {
        return bossKill;
    }

    public void setBossKill(HashMap<Integer, Integer> bossKill) {
        this.bossKill = bossKill;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public HashSet<Monster> getState() {
        return state;
    }

    public void setState(HashSet<Monster> state) {
        this.state = state;
    }

    public HashSet<Monster> getBeAttack() {
        return beAttack;
    }

    public void setBeAttack(HashSet<Monster> beAttack) {
        this.beAttack = beAttack;
    }

    public int getExtraServerId() {
        return extraServerId;
    }

    public void setExtraServerId(int extraServerId) {
        this.extraServerId = extraServerId;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public HashMap<Long, Boss> getMonster() {
        return monster;
    }

    public void setMonster(HashMap<Long, Boss> monster) {
        this.monster = monster;
    }

    public HashMap<Integer, Boss> getBoss() {
        return boss;
    }

    public void setBoss(HashMap<Integer, Boss> boss) {
        this.boss = boss;
    }

    public HashMap<Integer, Integer> getServer() {
        return server;
    }

    public void setServer(HashMap<Integer, Integer> server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "AlienCopyData{" +
                "group=" + group +
                ", city=" + city +
                ", extraServerId=" + extraServerId +
                '}';
    }
}
