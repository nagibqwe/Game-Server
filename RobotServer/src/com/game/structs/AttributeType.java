/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.structs;

/**
 *
 * @author Administrator
 */
public class AttributeType {

    //////面板显示值
    public static final int Attack = 0;//攻击
    public static final int Defense = 1;//防御
    public static final int MaxHP = 2;//生命
    public static final int Crit = 3;//暴击
    public static final int CritHarm = 4;//爆伤
    public static final int Attackspeed = 5; //攻速
    public static final int MoveSpeed = 6;//移速
    public static final int AppendHram = 7;//附加伤害
    public static final int WithstandHram = 8;//抵挡伤害
    public static final int HarmExtraDel = 9;//伤害减少
    public static final int HarmExtraAdd = 10;//伤害加成
    public static final int ReboundHarm = 11;//反弹伤害
    public static final int HitRecover = 12;//击中恢复
    public static final int ReboundHarmRes = 13;//反弹伤害抵抗
    public static final int HitRecoverRes = 14;//击中恢复抵抗

    public static final int AttributeTypeCount = 15;//属性个数,每加一个属性这个值需加1,且顺序依次加1不能跳跃
    
    /////////计算后的最终值
    public static final int AttackSpeedFinal = 105;
    public static final int MoveSpeedFinal = 106;
}
