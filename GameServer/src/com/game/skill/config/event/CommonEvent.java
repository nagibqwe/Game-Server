/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.skill.config.event;

import com.game.skill.config.SkillEvent;
import com.game.skill.structs.SkillDefine;
import com.game.utils.Shape;

/**
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public class CommonEvent extends SkillEvent {

    @Override
    public void split(String param) {

    }

    @Override
    public float getHitDis(int type) {
        return 0f;
    }

    @Override
    public float getSpeed(int type) {
        return 1f;
    }

    @Override
    public int getRunTime(int type) {
        return 1;
    }

    @Override
    public int getHitType() {
        return SkillDefine.SkillAttackMoveType_None;
    }

    @Override
    public Shape getShape() {
        return new Shape();
    }

    @Override
    public int getUniqueID() {
        return 0;
    }

    @Override
    public String getHitEffect() {
        return "";
    }
}
