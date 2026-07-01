/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.skill.config.event;

import com.game.skill.config.SkillEvent;
import com.game.skill.config.SkillReadConfigUtil;
import com.game.skill.structs.SkillDefine;
import com.game.utils.Shape;
import com.game.utils.Symbol;

/**
 *召唤物事件
 * @author zenghai
 * v1.0.1 by soko
 */
public class PlaySkillObjectEvent extends SkillEvent {
    //召唤物名字
    public String skillName = "";
    //召唤物出现的位置，0自身位置，1目标位置
    public int posType = 0;
    //召唤物离主角的最大距离
    public float maxDis = 0f;
    //召唤物移动类型
    public int moveType = 0;
    //移动速度
    public float moveSpeed = 5f;
    //移动加速度
    public float moveAddSpeed = 0f;
    //固定方向移动偏移角度
    public float fixDirOffsetAngle = 0f;


    @Override
    public void split(String param) {
        String[] params = param.split(Symbol.FENHAO);
        skillName = SkillReadConfigUtil.getStringValue(params, 0, "");
        posType = SkillReadConfigUtil.getIntValue(params, 1,0);
        maxDis = SkillReadConfigUtil.getFloatValue(params, 2,10f);
        moveType = SkillReadConfigUtil.getIntValue(params, 3,0);
        moveSpeed = SkillReadConfigUtil.getFloatValue(params, 4,5f);
        moveAddSpeed = SkillReadConfigUtil.getFloatValue(params, 5,0f);
        fixDirOffsetAngle = SkillReadConfigUtil.getFloatValue(params, 6,0f);
    }

    public String getSkillName() {
        return skillName;
    }

    public int getPosType() {
        return posType;
    }

    public float getMaxDis() {
        return maxDis;
    }

    public int getMoveType() {
        return moveType;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public float getMoveAddSpeed() {
        return moveAddSpeed;
    }

    public float getFixDirOffsetAngle() {
        return fixDirOffsetAngle;
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

}
