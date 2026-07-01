/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.equip.struct;

import com.data.struct.ReadIntegerArray;
import com.game.player.structs.CellItem;

/**
 * 客户端装备实体
 */
public class Equip extends CellItem {
    /**
     * 装备品质(：1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     */
    private int quality;
    /**
     * 品阶：1表示1阶，2表示2阶
     */
    private int grade;
    /**
     * 装备星级
     */
    private int star;
    /**
     *"职业限制
     * 0-男剑
     * 1-女枪
     * 2-待定
     * 3-待定
     * 4-待定
     * 5-待定
     * 9-通用"
     */
    private ReadIntegerArray gender;
    /**
     * 装备等级要求
     */
    private int level;
    /**
     * 装备评分(战斗力)
     */
    private int score;
    /**
     * 装备部位(0头盔.1武器.2胸甲.3项链.4腰带.5腿甲.6鞋子.7戒指)
     */
    private int part;
    /**
     * 套装ID
     */
    private int suitId;

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public ReadIntegerArray getGender() {
        return gender;
    }

    public void setGender(ReadIntegerArray gender) {
        this.gender = gender;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    public int getSuitId() {
        return suitId;
    }

    public void setSuitId(int suitId) {
        this.suitId = suitId;
    }

    @Override
    public Equip clone() {
        try {
            Equip eq = (Equip) super.clone();
            return eq;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Equip{" +
                "quality=" + quality +
                ", grade=" + grade +
                ", star=" + star +
                ", gender=" + gender +
                ", level=" + level +
                ", score=" + score +
                ", part=" + part +
                ", suitId=" + suitId +
                '}';
    }
}
