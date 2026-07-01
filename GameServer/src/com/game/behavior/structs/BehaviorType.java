/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.behavior.structs;

/**
 * 行为类枚举
 *
 * @author soko <xuchangming@haowan123.com>
 */
public enum BehaviorType {
    Move(1), //移动行为
    DirMove(2), //方向移动
    Run(3), //跑行为    
    Fly(4), //飞行为
    AttackMove(5), //攻击移动行为
    Revive(6), //复活行为
    Gather(7), //采集物行为
    RanBack(8), //回跑行为
    Jump(9), //跳跃
    SkillMove(10), //技能位移
    SkillEvent(11), //技能事件
    MagicMoveEvent(12),//召唤物定点移动事件
    MagicDirMoveEvent(13),//召唤物定向移动事件
    ;

    private final int value;

    private BehaviorType(int v) {
        value = v;
    }

    @Override
    public String toString() {
        return "BehaviorType:" + value;
    }

}
