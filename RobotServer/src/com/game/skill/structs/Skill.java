package com.game.skill.structs;

import com.game.player.structs.Player;
import com.game.skill.config.SkillEvent;

import java.util.ArrayList;
import java.util.List;

public class Skill {

    //技能配置表ID
    private int id;

    //技能等级
    private int level = 1;

    //序列号
    private transient int serial = 1;

    //开始时间
    private transient long start = 0;

    //下次攻击时间
    private transient long nextUseTime;

    //延时技能事件
    private transient List<SkillEvent> slows = new ArrayList<>();

    //技能位置主要是玩家技能 用于反查
    private int pos;

    //是否是普通攻击技能
    private transient boolean isNormal;

    /////////////////////////////////getter and setter///////////////////////////////////////////////////////

    public long getNextUseTime() {
        return nextUseTime;
    }

    public void setNextUseTime(long nextUseTime) {
        this.nextUseTime = nextUseTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setSlows(List<SkillEvent> slows) {
        this.slows = slows;
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
