/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.skill.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 技能可视化数据
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public class SkillVisual {
    //可视化效果ID
    private String id;
    //一帧时间
    public static final float OneFrameTime = 1f / 30f;
    //技能帧数
    private int fps;
    //技能可打段帧数
    private int combofps;
    //霸体时间
    private int SuperArmorCount;
    //是否可移动
    private boolean canMove;
    /**
     * 技能编号ID，技能事件
     */
    private final HashMap<Integer, SkillEvent> events = new HashMap<>();
    /**
     * 技能总的事件集合
     */
    private final List<SkillEvent> eventList = new ArrayList<>();
    private boolean lockTrajact = false;
    private boolean summon = false;
    //攻击距离
    private float attackDis = 0f;
    //攻击个数
    private int maxAttack = 0;
    //技能位移距离
    private float moveDis = 0f;
    //是否对玩家有效
    private boolean canPlayerValid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取霸体时间
     */
    public int getSuperTime() {
        return (int) (SuperArmorCount * OneFrameTime * 1000);
    }

    public int getCd() {
        return (int) (fps * OneFrameTime * 1000);
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getCombofps() {
        return combofps;
    }

    public void setCombofps(int combofps) {
        this.combofps = combofps;
    }

    public int getSuperArmorCount() {
        return SuperArmorCount;
    }

    public void setSuperArmorCount(int SuperArmorCount) {
        this.SuperArmorCount = SuperArmorCount;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    /**
     * 技能编号ID，技能事件 的集合
     * @return 返回当前技能效果集合的总的技能事件
     */
    public HashMap<Integer, SkillEvent> getEvents() {
        return events;
    }

    public List<SkillEvent> getEventList() {
        return eventList;
    }

    public boolean isLockTrajact() {
        return lockTrajact;
    }

    public void setLockTrajact(boolean lockTrajact) {
        this.lockTrajact = lockTrajact;
    }

    public boolean isSummon() {
        return summon;
    }

    public void setSummon(boolean isSummon) {
        this.summon = isSummon;
    }

    public float getAttackDis() {
        return attackDis;
    }

    public void setAttackDis(float attackDis) {
        this.attackDis = attackDis;
    }

    public int getMaxAttack() {
        return maxAttack;
    }

    public void setMaxAttack(int maxAttack) {
        this.maxAttack = maxAttack;
    }

    public float getMoveDis() {
        return moveDis;
    }

    public void setMoveDis(float moveDis) {
        this.moveDis = moveDis;
    }

    public boolean isCanPlayerValid() {
        return canPlayerValid;
    }

    public void setCanPlayerValid(boolean canPlayerValid) {
        this.canPlayerValid = canPlayerValid;
    }

}
