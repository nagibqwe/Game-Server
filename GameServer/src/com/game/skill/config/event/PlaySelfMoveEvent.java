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
 * @author zenghai <zenghai@haowan123.com>
 */
public class PlaySelfMoveEvent extends SkillEvent {
    //移动类型
    private int moveType = 0;
    //移动最大距离
    private float moveDis = 0f;
    //移动时间
    private float moveTime;
    //无目标时的移动距离
    private float noTargetDis = 0f;

    @Override
    public void split(String param) {
        String[] params = param.split(Symbol.FENHAO);
        moveType = SkillReadConfigUtil.getIntValue(params, 0, 0);
        moveDis = SkillReadConfigUtil.getFloatValue(params, 1, 10f);
        moveTime = SkillReadConfigUtil.getFloatValue(params,2, 0.5f);
        noTargetDis = SkillReadConfigUtil.getFloatValue(params, 3, 10f);
    }

    public int getMoveType() {
        return moveType;
    }

    public float getMoveDis() {
        return moveDis;
    }

    public float getMoveTime() {
        return moveTime;
    }

    public float getNoTargetDis() {
        return noTargetDis;
    }
    @Override
    public int getUniqueID() {
        return 0;
    }

    @Override
    public String getHitEffect() {
        return "";
    }

    @Override
    public String toString() {
        return "PlaySelfMoveEvent{" +
                "moveType=" + moveType +
                ", moveDis=" + moveDis +
                ", moveTime=" + moveTime +
                ", noTargetDis=" + noTargetDis +
                '}';
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
}
