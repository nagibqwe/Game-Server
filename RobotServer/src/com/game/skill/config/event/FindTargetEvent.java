/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.skill.config.event;

import com.game.skill.config.SkillEvent;
import com.game.skill.structs.SkillDefine;
import com.game.utils.Shape;
import com.game.utils.Symbol;

/**
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public class FindTargetEvent extends SkillEvent {

    private int AreaType = SkillDefine.SkillShape_Rectangle;
    private float RectWidth = 1f; //宽
    private float RectHeight = 1f; //高

    private float SectorAngle = 100f; //扇形角度
    private float SectorRadius = 1f; //扇形半径
    private float RoundRadius = 1f;  //圆半径
    private int MaxTargetCount = 10; //攻击个数

    private float attackDis = 1f; //攻击范围
    
    private final Shape shape = new Shape();

    @Override
    public void split(String param) {
        String[] params = param.split(Symbol.FENHAO_REG);
        AreaType = Integer.parseInt(params[0]);
        RectWidth = Float.parseFloat(params[1]);
        RectHeight = Float.parseFloat(params[2]);
        SectorAngle = Float.parseFloat(params[3]);
        SectorRadius = Float.parseFloat(params[4]);
        RoundRadius = Float.parseFloat(params[5]);
        MaxTargetCount = Integer.parseInt(params[6]);

        shape.setType(AreaType);
        switch (AreaType) {
            case SkillDefine.SkillShape_Rectangle:
                attackDis = RectHeight > RectWidth ? RectHeight : RectWidth;
                shape.setHeight(RectHeight);
                shape.setWidth(RectWidth);
                break;
            case SkillDefine.SkillShape_Sector:
                attackDis = SectorRadius;
                shape.setAngle(SectorAngle);
                shape.setR(SectorRadius);
                break;
            case SkillDefine.SkillShape_Circle:
                attackDis = RoundRadius;
                shape.setR(RoundRadius);
                break;
            default:
        }

    }

    public float getAttackDis() {
        return attackDis;
    }

    public void setAttackDis(float attackDis) {
        this.attackDis = attackDis;
    }

    public int getAreaType() {
        return AreaType;
    }

    public float getRectWidth() {
        return RectWidth;
    }

    public float getRectHeight() {
        return RectHeight;
    }

    public float getSectorAngle() {
        return SectorAngle;
    }

    public float getSectorRadius() {
        return SectorRadius;
    }

    public float getRoundRadius() {
        return RoundRadius;
    }

    public int getMaxTargetCount() {
        return MaxTargetCount;
    }

    @Override
    public float getHitDis(int type) {
        return attackDis;
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
        return shape;
    }

    @Override
    public int getUniqueID() {
        return 0;
    }
}
