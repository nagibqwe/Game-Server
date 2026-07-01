/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.equip.struct;

/**
 * @author Administrator
 */
public class EquipDefine {

    //--------------------装备品质------------------------------//
    public static final int EquipQuality_White = 1;//白装
    public static final int EquipQuality_Green = 2;//绿装
    public static final int EquipQuality_Blue = 3;//蓝装
    public static final int EquipQuality_Purple = 4;//紫装
    public static final int EquipQuality_Orange = 5;//橙装
    public static final int EquipQuality_Golden = 6;//金装
    public static final int EquipQuality_Red = 7;//红装

    public static final String[] QualityDesc = {"白","绿","蓝","紫","橙","金","红","粉","暗金","幻彩"};

    //--------------------人物装备部位----------------------------//
    public static final int EquipPart_Head = 0;//头盔
    public static final int EquipPart_Weapon = 1;//武器
    public static final int EquipPart_Breastplate = 2;//胸甲
    public static final int EquipPart_Cuff = 3;//项链
    public static final int EquipPart_Belt = 4;//腰带
    public static final int EquipPart_Cuish = 5;//腿甲
    public static final int EquipPart_Shoe = 6;//鞋子
    public static final int EquipPart_Ring = 7;//戒指
    public static final int EquipPart_Bracelet = 8;//手镯
    public static final int EquipPart_Earring = 9;//耳环
    public static final int EquipPart_spiritMedal = 10;//灵章

    public static final int EquipMax_Part = 10;//装备最大部位

    public static final int EquipPart_Num = 11;//装备部位数量

    //圣装部位
    public static final int HolyEquip_DouXin = 111;//圣装-斗心


    //强化装备失败原因
    public static final int Equip_Failed_NoMoney = -1;//金币不足
    public static final int Equip_Failed_NoIron = -2;//代币不足
    public static final int Equip_Failed_MaxLv = -3;//最高级了
    public static final int Equip_Failed_Null = -4;//装备不存在
    public static final int Equip_Failed_UnKonw = -5;//未知错误，比如配置表不存在等
    public static final int Equip_Failed_Part = -6;//装备部位不同
    public static final int Equip_Failed_Config = -7;//配置错误
    public static final int Equip_Failed_Material = -8;//材料不足
    public static final int Equip_Failed_Less_Level = -9;//装备等级不足
    public static final int Equip_Failed_ZZ_Num = -10;//融合两件装备的资质个数不一样
    public static final int Equip_Failed_False = -11;//结果失败
    public static final int Equip_Failed_Book = -12;//道具书不存在
    public static final int Equip_Failed_Skill_Sys = -13;//学习技能时 没取到配置 
    public static final int Equip_Failed_Param = -14;//参数错误
    public static final int Equip_Failed_Level = -15;//等级不一样
    public static final int Equip_Failed_Add = -16;//添加的时候没有一个格子
    public static final int Equip_Failed_MilitaryRank = -17;//军衔不同时存在
    public static final int Equip_Failed_Random = -18;//随机不成功
    public static final int Equip_Failed_Page = -19;//同一页不需要切
    public static final int Equip_Quality_NO_Same = -20;//装备品质不一样
    //卸下装备失败原因
    public static final int UnWearFailed_WrongEquip = -1;//错误的装备
    public static final int UnWearFaile_NoBagCell = -2;//背包不足
}
