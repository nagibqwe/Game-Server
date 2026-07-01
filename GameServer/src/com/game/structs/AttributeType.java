package com.game.structs;

/**
 * @author lw
 */
public class AttributeType {

    /**
     * 攻击
     */
    public static final int ATTR_Atk = 1;

    /**
     * 生命
     */
    public static final int ATTR_MaxHp = 2;

    /**
     * 破甲
     */
    public static final int ATTR_DefBreak = 3;

    /**
     * 防御
     */
    public static final int ATTR_Def = 4;

    /**
     * 精准
     */
    public static final int ATTR_Hit = 5;

    /**
     * 偏斜
     */
    public static final int ATTR_HitBreak = 6;

    /**
     * 攻速
     */
    public static final int ATTR_AtkSpeed = 7;

    /**
     * 移速
     */
    public static final int ATTR_Speed = 8;

    /**
     * 幸运
     */
    public static final int ATTR_Luck = 9;

    /**
     * 无视防御
     */
    public static final int ATTR_DisDef = 10;

    /**
     * 暴击
     */
    public static final int ATTR_Critical = 11;

    /**
     * 韧性
     */
    public static final int ATTR_Resilience = 12;

    /**
     * 增伤               百分比
     */
    public static final int ATTR_AddHarm = 13;

    /**
     * 减伤               百分比
     */
    public static final int ATTR_DecHarm = 14;


    /**
     * 破击率              百分比
     */
    public static final int ATTR_AtkPer = 15;

    /**
     * 御破率              百分比
     */
    public static final int ATTR_AtkBreakPer = 16;

    /**
     * 精准率              百分比
     */
    public static final int ATTR_HitPer = 17;

    /**
     * 偏斜率              百分比
     */
    public static final int ATTR_HtBreakPer = 18;

    /**
     * 暴击率              百分比
     */
    public static final int ATTR_CriticalPer = 19;

    /**
     * 抗暴率              百分比
     */
    public static final int ATTR_CriticalBreakPer = 20;

    /**
     * 控制延长             百分比
     */
    public static final int ATTR_ControlAdd = 21;

    /**
     * 控制缩短             百分比
     */
    public static final int ATTR_ControlDec = 22;

    /**
     * 会心几率             百分比
     */
    public static final int ATTR_HitOdds = 23;

    /**
     * 会心抵抗             百分比
     */
    public static final int ATTR_HitDefOdds = 24;

    /**
     * 连击几率             百分比
     */
    public static final int ATTR_DHitOdds = 25;

    /**
     * 格挡几率             百分比
     */
    public static final int ATTR_GuardOdds = 26;

    /**
     * 追击几率             百分比
     */
    public static final int ATTR_PursueOdds = 27;

    /**
     * 识破几率             百分比
     */
    public static final int ATTR_SeeResilienceOdds = 28;

    /**
     * 攻击百分比           百分比
     */
    public static final int ATTR_AtkOdds = 29;

    /**
     * 生命百分比           百分比
     */
    public static final int ATTR_MaxHpOdds = 30;

    /**
     * 破甲百分比           百分比
     */
    public static final int ATTR_DefBreakOdds = 31;

    /**
     * 防御百分比           百分比
     */
    public static final int ATTR_DefOdds = 32;

    /**
     * 攻速加成             百分比
     */
    public static final int ATTR_AtkSpeedOdds = 33;
    /**
     * 移速加成             百分比
     */
    public static final int ATTR_SpeedOdds = 34;

    /**
     * 对怪物攻击伤害提升     百分比
     */
    public static final int ATTR_AtkMonsterHarmAdd = 35;

    /**
     * 被怪物攻击伤害降低     百分比
     */
    public static final int ATTR_AtkMonsterHarmDec = 36;

    /**
     * 对玩家攻击伤害提升     百分比
     */
    public static final int ATTR_AtkRoleHarmAdd = 37;

    /**
     * 被玩家攻击伤害降低     百分比
     */
    public static final int ATTR_AtkRoleHarmDec = 38;

    /**
     * 当前等级角色生命提升比  百分比
     */
    public static final int ATTR_MaxHpAdd = 39;

    /**
     * 当前等级角色攻击提升比  百分比
     */
    public static final int ATTR_AtkAdd = 40;

    /**
     * 当前等级角色防御提升比  百分比
     */
    public static final int ATTR_DefAdd = 41;

    /**
     * 当前等级角色破甲提升比  百分比
     */
    public static final int ATTR_DefBreakAdd = 42;

    /**
     * 杀怪物经验提升百分比    百分比
     */
    public static final int ATTR_MonserExp = 43;

    /**
     * 主角回血              百分比
     */
    public static final int ATTR_RoleHp = 44;

    /**
     * 风元素
     */
    public static final int ATTR_Wind = 45;

    /**
     * 火元素
     */
    public static final int ATTR_Fire = 46;

    /**
     * 水元素
     */
    public static final int ATTR_Water = 47;

    /**
     * 雷元素
     */
    public static final int ATTR_Thunder = 48;

    /**
     * 土元素
     */
    public static final int ATTR_Soil = 49;

    /**
     * 风元素抗性值
     */
    public static final int ATTR_WindDef = 50;

    /**
     * 火元素抗性值
     */
    public static final int ATTR_FireDef = 51;

    /**
     * 水元素抗性值
     */
    public static final int ATTR_WaterDef = 52;

    /**
     * 雷元素抗性值
     */
    public static final int ATTR_ThunderDef = 53;

    /**
     * 土元素抗性值
     */
    public static final int ATTR_SoilDef = 54;

    /**
     * 暴击伤害倍数
     */
    public static final int ATTR_CritRate = 55;

    /**
     * 会心伤害倍数
     */
    public static final int ATTR_Crit_HitDefRate = 56;

    /**
     * 连击伤害倍数
     */
    public static final int ATTR_Crit_DHitRate = 57;

    /**
     * 追击伤害倍数
     */
    public static final int ATTR_Crit_PursueRate = 58;

    /**
     * 灵力最大值
     */
    public static final int ATTR_Wakan = 59;

    /**
     * 灵力攻击
     */
    public static final int ATTR_Wakan_Atk = 60;

    /**
     * 灵力减伤
     */
    public static final int ATTR_Wakan_Dec = 61;

    /**
     * 怪物免伤百分比
     */
    public static final int ATTR_Monster_Dec = 62;

    /**
     *     基础属性最大值
     */
    public static final int ATTR_MAX = 62;



    public static final int AttackSpeedFinal = 105;

    public static final int MoveSpeedFinal = 106;


    //系统属性
     /**
     * 坐骑升阶攻击百分比
     */
    public static final int ATTR_Horse_Attack = 1;
    /**
     * 坐骑升阶生命百分比
     */
    public static final int ATTR_Horse_Hp = 2;
    /**
     * 坐骑升阶破甲百分比
     */
    public static final int ATTR_Horse_DefBreak = 3;
    /**
     * 坐骑升阶防御百分比
     */
    public static final int ATTR_Horse_Defence = 4;
    /**
     * 翅膀升阶攻击百分比
     */
    public static final int ATTR_Wing_Attack = 5;
    /**
     * 翅膀升阶生命百分比
     */
    public static final int ATTR_Wing_Hp = 6;
    /**
     * 翅膀升阶破甲百分比
     */
    public static final int ATTR_Wing_DefBreak = 7;
    /**
     * 翅膀升阶防御百分比
     */
    public static final int ATTR_Wing_Defence = 8;
    /**
     * 头盔宝石比
     */
    public static final int ATTR_Helmet_Gem = 9;
    /**
     * 武器宝石比
     */
    public static final int ATTR_Weapon_Gem = 10;
    /**
     * 胸甲宝石比
     */
    public static final int ATTR_Breastplate_Gem = 11;
    /**
     * 项链宝石比
     */
    public static final int ATTR_Necklace_Gem = 12;
    /**
     * 腰带宝石比
     */
    public static final int ATTR_Belt_Gem = 13;
    /**
     * 腿甲宝石比
     */
    public static final int ATTR_Leg_Armor_Gem = 14;
    /**
     * 鞋子宝石比
     */
    public static final int ATTR_Shoe_Gem = 15;
    /**
     * 戒指宝石比
     */
    public static final int ATTR_Ring_Gem = 16;
    /**
     * 法器升级攻击
     * */
    public static final int ATTR_Talisman_Attack = 17;
    /**
     * 法器升级生命
     * */
    public static final int ATTR_Talisman_HP = 18;
    /**
     * 法器升级破甲
     * */
    public static final int ATTR_Talisman_DefBreak = 19;
    /**
     * 法器升级防御
     * */
    public static final int ATTR_Talisman_Defence = 20;
    /**
     * 灵阵升级精准
     * */
    public static final int ATTR_Magic_Hit = 21;
    /**
     * 灵阵升级偏斜
     * */
    public static final int ATTR_Magic_HitBreak = 22;
    /**
     * 灵阵升级暴击
     * */
    public static final int ATTR_Magic_Critical = 23;
    /**
     * 灵阵升级韧性
     * */
    public static final int ATTR_Magic_Resilience = 24;
    /**
     * 法宝1属性放大倍率
     */
    public static final int ATTR_Treasure_One = 25;
    /**
     * 法宝2属性放大倍率
     */
    public static final int ATTR_Treasure_Two = 26;
    /**
     * 法宝3属性放大倍率
     */
    public static final int ATTR_Treasure_Three = 27;
    /**
     * 法宝4属性放大倍率
     */
    public static final int ATTR_Treasure_Four = 28;
    /**
     * 法宝5属性放大倍率
     */
    public static final int ATTR_Treasure_Five = 29;
    /**
     * 法宝6属性放大倍率
     */
    public static final int ATTR_Treasure_Six = 30;
    /**
     * 法宝7属性放大倍率
     */
    public static final int ATTR_Treasure_Seven = 31;

    /**
     * 装备基础生命
     */
    public static final int ATTR_EquipBase_Hp = 32;

    /**
     * 装备基础防御
     */
    public static final int ATTR_EquipBase_Def = 33;

    /**
     * 装备基础破甲
     */
    public static final int ATTR_EquipBase_DefBreak = 34;

    /**
     * 装备基础攻击
     */
    public static final int ATTR_EquipBase_Atk = 35;

    /**
     * 当前选择神兵基础属性放大倍率
     */
    public static final int ATTR_EquipBase_Coefficient = 36;

    /**
     * 圣装基础生命
     */
    public static final int ATTR_HolyBase_Hp = 37;

    /**
     * 圣装基础防御
     */
    public static final int ATTR_HolyBase_Df = 38;

    /**
     * 圣装基础破甲
     */
    public static final int ATTR_HolyBase_Break= 39;

    /**
     * 圣装基础攻击
     */
    public static final int ATTR_HolyBase_Atk= 40;

    /**
     * 圣装总生命
     */
    public static final int ATTR_HolyAll_Hp= 41;

    /**
     * 圣装总防御
     */
    public static final int ATTR_HolyAll_Df= 42;

    /**
     * 圣装总破甲
     */
    public static final int ATTR_HolyAll_Break= 43;

    /**
     * 圣装总攻击
     */
    public static final int ATTR_HolyAll_Atk= 44;

    /**
     * 圣装总属性
     */
    public static final int ATTR_HolyAll_Attribute= 45;


    /**
     * 法宝升阶攻击百分比
     */
    public static final int ATTR_Fabao_Attack = 46;
    /**
     * 法宝升阶生命百分比
     */
    public static final int ATTR_Fabao_Hp = 47;
    /**
     * 法宝升阶破甲百分比
     */
    public static final int ATTR_Fabao_DefBreak = 48;
    /**
     * 法宝升阶防御百分比
     */
    public static final int ATTR_Fabao_Defence = 49;

    /**
     * 宠物升阶攻击百分比
     */
    public static final int ATTR_Pet_Attack = 50;
    /**
     * 宠物升阶生命百分比
     */
    public static final int ATTR_Pet_Hp = 51;
    /**
     * 宠物升阶破甲百分比
     */
    public static final int ATTR_Pet_DefBreak = 52;
    /**
     * 宠物升阶防御百分比
     */
    public static final int ATTR_Pet_Defence = 53;


    /**
     *   系统属性最大值1045
     */
    public static final int SystemAttr_Max = 54;




    /**
     * 基础属性修正
     */
    public static int[][] ATTR_Add_FIX = new int[][]{
            {ATTR_MaxHpAdd, ATTR_MaxHp},
            {ATTR_AtkAdd, ATTR_Atk},
            {ATTR_DefAdd, ATTR_Def},
            {ATTR_DefBreakAdd, ATTR_DefBreak}
    };

    /**
     * 属性修正
     */
    public static int[][] ATTR_FIX = new int[][]{
            {ATTR_Atk, ATTR_AtkOdds},
            {ATTR_MaxHp, ATTR_MaxHpOdds},
            {ATTR_DefBreak, ATTR_DefBreakOdds},
            {ATTR_Def, ATTR_DefOdds},
            {ATTR_AtkSpeed, ATTR_AtkSpeedOdds},
            {ATTR_Speed, ATTR_SpeedOdds}
    };

    public static int[] ATTR_NO_FIX = new int[]{
            ATTR_MaxHpAdd,
            ATTR_AtkAdd,
            ATTR_DefAdd,
            ATTR_DefBreakAdd,
            ATTR_Hit,
            ATTR_HitBreak,
            ATTR_Luck,
            ATTR_DisDef,
            ATTR_Critical,
            ATTR_Resilience,
            ATTR_AddHarm,
            ATTR_DecHarm,
            ATTR_AtkPer,
            ATTR_AtkBreakPer,
            ATTR_HitPer,
            ATTR_HtBreakPer,
            ATTR_CriticalPer,
            ATTR_CriticalBreakPer,
            ATTR_ControlAdd,
            ATTR_ControlDec,
            ATTR_HitOdds,
            ATTR_HitDefOdds,
            ATTR_DHitOdds,
            ATTR_GuardOdds,
            ATTR_PursueOdds,
            ATTR_SeeResilienceOdds,
            ATTR_AtkMonsterHarmAdd,
            ATTR_AtkMonsterHarmDec,
            ATTR_AtkRoleHarmAdd,
            ATTR_AtkRoleHarmDec,
            ATTR_MonserExp,
            ATTR_RoleHp,
            ATTR_Wind,
            ATTR_Fire,
            ATTR_Water,
            ATTR_Thunder,
            ATTR_Soil,
            ATTR_WindDef,
            ATTR_FireDef,
            ATTR_WaterDef,
            ATTR_ThunderDef,
            ATTR_SoilDef,
            ATTR_CritRate,
            ATTR_Crit_HitDefRate,
            ATTR_Crit_DHitRate,
            ATTR_Crit_PursueRate,
            ATTR_Wakan,
            ATTR_Wakan_Atk,
            ATTR_Wakan_Dec
    };


    //用来计算 战斗力的
    public static int[] FIGHT_POWER = new int[]{
            ATTR_Atk,
            ATTR_MaxHp,
            ATTR_DefBreak,
            ATTR_Def,
            ATTR_Hit,
            ATTR_HitBreak,
            ATTR_AtkSpeed,
            ATTR_Speed,
            ATTR_Luck,
            ATTR_DisDef,
            ATTR_Critical,
            ATTR_Resilience,
            ATTR_AddHarm,
            ATTR_DecHarm,
            ATTR_AtkPer,
            ATTR_AtkBreakPer,
            ATTR_HitPer,
            ATTR_HtBreakPer,
            ATTR_CriticalPer,
            ATTR_CriticalBreakPer,
            ATTR_ControlAdd,
            ATTR_ControlDec,
            ATTR_HitOdds,
            ATTR_HitDefOdds,
            ATTR_DHitOdds,
            ATTR_GuardOdds,
            ATTR_PursueOdds,
            ATTR_SeeResilienceOdds
    };
}
