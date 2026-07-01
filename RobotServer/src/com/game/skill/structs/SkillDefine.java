package com.game.skill.structs;


public class SkillDefine {

    //击退方向
    public static final int SkillHitAway0 = 0; //技能使用使用者方向
    public static final int SkillHitAway1 = 1; //同方向击退


    //技能类型

    public static final int SkillType_Active = 0;               //主动技能
    public static final int SkillType_UnActive = 1;             //被动技能
    public static final int SkillType_TeSha = 2;                //特杀技能

    //技能伤害类型
    // 伤害类型：0：没有伤害；1：物理伤害
    public static final int SkillDamageType_None = 0;       //
    public static final int SkillDamageType_Physics = 1;    //1：物理伤害


    //技能位移类型
    // 技能造成的位移效果，0不位移，1击退，2击飞 3抓取 4普通攻击(僵直)
    public static final int SkillAttackMoveType_None = 0;
    public static final int SkillAttackMoveType_AttackHit = 1;
    public static final int SkillAttackMoveType_Back = 2;
    public static final int SkillAttackMoveType_Fly = 3;
    public static final int SkillAttackMoveType_Catch = 4;

    //技能使用者类型
    //使用者（0-3，职业1,2,3,4技能；10：无限制；11怪物技能；12：宠物技能；13：婚姻技能
    public static final int SkillUserType_One = 1; //男性技能
    public static final int SkillUserType_T = 2; //女性技能
    public static final int SkillUserType_None = 10; //10，无限制；
    public static final int SkillUserType_Monster = 11; //怪物技能，
    public static final int SkillUserType_Pet = 12; //宠物技能

    //技能作用范围
    //作用范围形状（0矩形，1扇形，2圆形）
    public static final int SkillShape_Rectangle = 0; //矩形
    public static final int SkillShape_Sector = 1; //扇形
    public static final int SkillShape_Circle = 2;  //圆形


    //技能事件
    /**
     * 播放动作
     */
    public static final int PLAY_ANIMATION_EVENT = 0;
    /**
     * 播放特效
     */
    public static final int PLAY_VFX_EVENT = 1;
    /**
     * 播放音效
     */
    public static final int PLAY_SFX_EVENT = 2;
    /**
     * 播放摄像机震动
     */
    public static final int PLAY_CAMERA_SHAKE_EVENT = 3;
    /**
     * 播放辐射状模糊效果
     */
    public static final int PLAY_BLUR_EVENT = 4;
    /**
     * 播放减速效果
     */
    public static final int PLAY_SLOW_EVENT = 5;
    /**
     * 禁止角色移动事件
     */
    public static final int DISABLE_MOVE_EVENT = 6;
    /**
     * 禁止角色改变方向事件
     */
    public static final int DISABLE_CHANGE_DIR_EVENT = 7;
    /**
     * 播放锁定弹道
     */
    public static final int PLAY_LOCK_TRAJECTORY_EVENT = 8;
    /**
     * 播放简单召唤物
     */
    public static final int PLAY_SIMPLE_SKILL_OBJECT_EVENT = 9;
    /**
     * 播放技能召唤物
     */
    public static final int PLAY_SKILL_OBJECT_EVENT = 10;
    /**
     * 播放产生伤害事件
     */
    public static final int PLAY_HIT_EVENT = 11;
    /**
     * 播放自身移动事件
     */
    public static final int PLAY_SELF_MOVE_EVENT = 12;


    private final int CritType_Deep = 1;
    private final int CritType_Luck = 2;
    private final int CritType_Crit = 3;

    //玩家攻击状态类型
    public static final int NORMAL = 1;//常态
    public static final int BE_ATTACK = 2;//被攻击
    public static final int BE_CRIT = 3;//被暴击
    public static final int BE_DEEP = 4;//被致命一击
    public static final int BE_LUCK = 5;//被致命一击
    public static final int ATTACK = 6;//攻击
    public static final int CRIT = 7;//暴击
    public static final int DEEP = 8;//致命一击
    public static final int LUCK = 9;//致命一击

}
