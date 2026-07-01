package com.game.skill.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.skill.config.SkillEvent;
import java.util.ArrayList;
import java.util.List;

public class Skill {

    //技能唯一ID
    private int skillId;
    //技能等级
    private int level = 1;
    @JsonIgnore
    private transient int serial = 1;  //序列号
    @JsonIgnore
    private transient long start = 0; //开始时间
    @JsonIgnore
    private transient List<SkillEvent> slows = new ArrayList<>(); //延时技能事件
    //技能位置主要是玩家技能 用于反查
    private int pos;
    @JsonIgnore
    private transient boolean isNormal; //是否是普通攻击技能

    public Skill() {
    }
    
    public int getSkillId() {
        return skillId;
    }
    
    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }
    
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public List<SkillEvent> getSlows() {
        return slows;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public boolean isNormal() {
        return isNormal;
    }

    public void setNormal(boolean normal) {
        isNormal = normal;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isIsNormal() {
        return isNormal;
    }

    public void setIsNormal(boolean isNormal) {
        this.isNormal = isNormal;
    }
    
    
    
}
