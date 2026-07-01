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
import com.game.skill.structs.SkillDefine;
import com.game.utils.Shape;
import com.game.utils.Symbol;

/**
 * 一个直接的伤害数据处理
 * @author soko
 * @date 2018-9-10
 */
public class PlayHitEvent extends SkillEvent {
    //查找目标数据
    private FindTargetInfo findInfo = null;
    //产生伤害数据
    private SkillHitEffectInfo hitInfo = null;

    @Override
    public void split(String param) {
        String[] params = param.split(Symbol.FENHAO);
        findInfo = new FindTargetInfo();
        int end = findInfo.split( params, 0);
        hitInfo = new SkillHitEffectInfo();
        hitInfo.parseData( params, end);
    }
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
    public String getHitEffect() {
        return hitInfo.getHitEffect();
    }

    public FindTargetInfo getFindInfo() {
        return findInfo;
    }

    public SkillHitEffectInfo getHitInfo() {
        return hitInfo;
    }

    @Override
    public String toString() {
        return "PlayHitEvent{" +
                "findInfo=" + findInfo +
                ", hitInfo=" + hitInfo +
                '}';
    }
}
