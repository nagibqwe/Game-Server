package com.game.nature.struct;

import com.game.skill.structs.Skill;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 造化系统
 */
public class Nature {
    /**
     * 配置表中的id   NatureXXX表中的id
     * */
    private int currentId;
    /**
     * 当前的模型id，可以是NatureXXX.xlsx中的id，也可以是HuaxingXXX.xlsx中的id
     */
    private int curModelId;
    /**
     * 类型 1：坐骑，2：翅膀 ，3：法器 4:阵道 5:神兵 6:法宝
     */
    private int type;
    /**
     * 打架技能
     */
    private ConcurrentHashMap<Integer, Skill> skills = new ConcurrentHashMap<>();

    public int getCurrentId() {
        return currentId;
    }

    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }

    public int getCurModelId() {
        return curModelId;
    }

    public void setCurModelId(int curModelId) {
        this.curModelId = curModelId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ConcurrentHashMap<Integer, Skill> getSkills() {
        return skills;
    }

    public void setSkills(ConcurrentHashMap<Integer, Skill> skills) {
        this.skills = skills;
    }
}
