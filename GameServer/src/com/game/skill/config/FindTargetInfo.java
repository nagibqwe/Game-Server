/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.skill.config;

import com.game.skill.structs.SkillDefine;
import com.game.utils.Shape;

/**
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public class FindTargetInfo{

    private int areaType = SkillDefine.SkillShape_Rectangle;
//    private float rectWidth = 1f; //宽
//    private float RectHeight = 1f; //高
//
//    private float SectorAngle = 100f; //扇形角度
//    private float SectorRadius = 1f; //扇形半径
//    private float SectorRadius = 1f; //扇形半径
//    private float RoundRadius = 1f;  //圆半径
    private int maxTargetCount = 10; //攻击个数

    private float attackDis = 0f; //攻击范围

    private final Shape shape = new Shape();

    public int split(String[] param, int begin) {
        areaType = SkillReadConfigUtil.getIntValue(param, begin +0,0);
        float rectWidth = SkillReadConfigUtil.getFloatValue(param, begin +1,1f);
        float RectHeight = SkillReadConfigUtil.getFloatValue(param, begin +2,1f);
        float SectorAngle = SkillReadConfigUtil.getFloatValue(param, begin +3,100f);
        float SectorRadius = SkillReadConfigUtil.getFloatValue(param, begin +4,1f);
        float RoundRadius = SkillReadConfigUtil.getFloatValue(param, begin +5,1f);
        maxTargetCount = SkillReadConfigUtil.getIntValue(param, begin +6,10);

        shape.setType(areaType);
        switch (areaType) {
            case SkillDefine.SkillShape_Rectangle:
                attackDis = RectHeight > rectWidth ? RectHeight : rectWidth;
                shape.setHeight(RectHeight);
                shape.setWidth(rectWidth);
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
        return 8;
    }

    public float getAttackDis() {
        return attackDis;
    }

    public int getMaxTargetCount() {
        return maxTargetCount;
    }

    public Shape getShape() {
        return shape;
    }

    @Override
    public String toString() {
        return "FindTargetInfo{" +
                "areaType=" + areaType +
                ", maxTargetCount=" + maxTargetCount +
                ", attackDis=" + attackDis +
                ", shape=" + shape +
                '}';
    }
}
