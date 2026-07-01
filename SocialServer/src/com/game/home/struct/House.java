package com.game.home.struct;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * @Desc TODO 家园
 * @Date 2021/7/12 18:06
 * @Auth ZUncle
 */
public class House {

    /**
     * 房屋规模
     */
    int level;

    /**
     * 聚宝盆等级, 经验
     */
    int tupLevel;
    int tupExp;

    /**
     * 家装大赛票数
     */
    int vote;
    /**
     * 装饰度
     */
    int decorate;
    /**
     * 是否允许非好友拜访
     */
    boolean authUnFriendEnter = false;
    /**
     * 是否允许非好友送礼
     */
    boolean authUnFriendGift = true;
    /**
     * 辅助建设者
     */
    long helper;
    /**
     * 未装扮家具
     */
    HashMap<Integer, Furniture> store = new HashMap<>();
    /**
     * 以装扮家具
     */
    HashMap<Long, FurnitureFix> style = new HashMap<>();
    /**
     * 拜访记录
     */
    List<VisitorEvent> ve = new ArrayList<>();
    /**
     * 已随机投票对象
     */
    @JsonIgnore
    transient HashSet<Long> randomVote = new HashSet<>();

    /**
     * 家园场景玩家ID
     */
    @JsonIgnore
    transient HashSet<Long> sceneMember = new HashSet<>();


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTupLevel() {
        return tupLevel;
    }

    public void setTupLevel(int tupLevel) {
        this.tupLevel = tupLevel;
    }

    public int getTupExp() {
        return tupExp;
    }

    public void setTupExp(int tupExp) {
        this.tupExp = tupExp;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getDecorate() {
        return decorate;
    }

    public void setDecorate(int decorate) {
        this.decorate = decorate;
    }

    public boolean isAuthUnFriendEnter() {
        return authUnFriendEnter;
    }

    public void setAuthUnFriendEnter(boolean authUnFriendEnter) {
        this.authUnFriendEnter = authUnFriendEnter;
    }

    public boolean isAuthUnFriendGift() {
        return authUnFriendGift;
    }

    public void setAuthUnFriendGift(boolean authUnFriendGift) {
        this.authUnFriendGift = authUnFriendGift;
    }

    public long getHelper() {
        return helper;
    }

    public void setHelper(long helper) {
        this.helper = helper;
    }

    public HashMap<Integer, Furniture> getStore() {
        return store;
    }

    public void setStore(HashMap<Integer, Furniture> store) {
        this.store = store;
    }

    public HashMap<Long, FurnitureFix> getStyle() {
        return style;
    }

    public void setStyle(HashMap<Long, FurnitureFix> style) {
        this.style = style;
    }

    public List<VisitorEvent> getVe() {
        return ve;
    }

    public void setVe(List<VisitorEvent> ve) {
        this.ve = ve;
    }

    public HashSet<Long> getRandomVote() {
        return randomVote;
    }

    public void setRandomVote(HashSet<Long> randomVote) {
        this.randomVote = randomVote;
    }

    public HashSet<Long> getSceneMember() {
        return sceneMember;
    }

    public void setSceneMember(HashSet<Long> sceneMember) {
        this.sceneMember = sceneMember;
    }
}
