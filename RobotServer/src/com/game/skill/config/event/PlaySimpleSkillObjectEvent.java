/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.skill.config.event;

import com.game.skill.config.FindTargetInfo;
import com.game.skill.config.SkillEvent;
import com.game.skill.config.SkillHitEffectInfo;
import com.game.skill.config.SkillReadConfigUtil;
import com.game.utils.Shape;
import com.game.utils.Symbol;

/**
 *简单技能召唤物事件
 * @author zenghai
 * v1.0.1 by soko
 */
public class PlaySimpleSkillObjectEvent extends SkillEvent {
    //召唤物查找目标配置
    public FindTargetInfo findInfo = null;
    //召唤物造成伤害配置
    public SkillHitEffectInfo hitInfo = null;
    //召唤物出现位置,0自身位置，1目标位置
    public int posType = 0;
    //召唤物离主角的最大距离
    public float maxDis = 0f;
    //召唤物特效ID
    public int vfxID = 0;
    //召唤物生存的帧数
    public int lifeFrame = 30;
    //召唤物产生后多少帧出现伤害
    public int startHitFrame = 0;
    //召唤物参数伤害次数
    public int hitCount = 1;
    //每次伤害间隔帧数
    public int hitInterval = 1;

    @Override
    public float getSpeed(int type) {
        return hitInfo.getSpeed(type);
    }

    @Override
    public int getRunTime(int type) {
        return hitInfo.getRunTime(type);
    }

    @Override
    public int getHitType() {
        return hitInfo.getHitType();
    }

    @Override
    public float getHitDis(int type) {
        return hitInfo.getHitDis(type);
    }

    @Override
    public Shape getShape() {
        return findInfo.getShape();
    }

    @Override
    public int getUniqueID() {
        return hitInfo.getUniqueID();
    }

    @Override
    public void split(String param) {
        String[] params = param.split(Symbol.FENHAO);
        findInfo = new FindTargetInfo();
        int begin = findInfo.split(params, 0);
        hitInfo = new SkillHitEffectInfo();
        int end = hitInfo.parseData(params, begin);
        posType = SkillReadConfigUtil.getIntValue(params, end,0);
        maxDis = SkillReadConfigUtil.getFloatValue(params, end + 1,10f);
        vfxID = SkillReadConfigUtil.getIntValue(params, end + 2,0);
        lifeFrame = SkillReadConfigUtil.getIntValue(params, end + 3,30);
        startHitFrame = SkillReadConfigUtil.getIntValue(params, end + 4,10);
        hitCount = SkillReadConfigUtil.getIntValue(params, end + 5,1);
        hitInterval = SkillReadConfigUtil.getIntValue(params, end + 6,10);
    }

    public FindTargetInfo getFindInfo() {
        return findInfo;
    }

    public SkillHitEffectInfo getHitInfo() {
        return hitInfo;
    }

    public int getPosType() {
        return posType;
    }

    public float getMaxDis() {
        return maxDis;
    }

    public int getVfxID() {
        return vfxID;
    }

    public int getLifeFrame() {
        return lifeFrame;
    }

    public int getStartHitFrame() {
        return startHitFrame;
    }

    public int getHitCount() {
        return hitCount;
    }

    public int getHitInterval() {
        return hitInterval;
    }

}
