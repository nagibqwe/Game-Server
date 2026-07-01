package com.game.equip.struct;

import java.util.ArrayList;
import java.util.List;

public class GemInfo {

    /**
     * 部位镶嵌宝石的颜色，用于宝石智能精炼判断
     */
    private int color;

    /**
     * 精炼等级，从1开始
     */
    private int level;

    /**
     * 精炼经验
     */
    private int exp;

    /**
     * 镶嵌的宝石id列表，空位置为0，未解锁为空列表
     */
    private List<Integer> gemIds = new ArrayList<>();

    /**
     * 镶嵌的仙玉id列表，空位置为0，未解锁为空列表
     */
    private List<Integer> jadeIds = new ArrayList<>();

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }


    public List<Integer> getGemIds() {
        return gemIds;
    }

    public void setGemIds(List<Integer> gemIds) {
        this.gemIds = gemIds;
    }

    public List<Integer> getJadeIds() {
        return jadeIds;
    }

    public void setJadeIds(List<Integer> jadeIds) {
        this.jadeIds = jadeIds;
    }

    @Override
    public String toString() {
        return "GemInfo{" +
                "color=" + color +
                ", level=" + level +
                ", exp=" + exp +
                ", gemIds=" + gemIds +
                ", jadeIds=" + jadeIds +
                '}';
    }
}
