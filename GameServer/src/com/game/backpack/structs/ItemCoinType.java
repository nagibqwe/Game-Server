package com.game.backpack.structs;


/**
 * 货币类型   预留1~100为货币类型
 */
public class ItemCoinType {
    /**
     * 货币配置表id应大于的值
     */
    public static final int Rmb = -1;//人民币
    public static final int ItemCoinMinId = 0;
    public static final int GemCoin = 1;//灵玉
    public static final int BindGemCoin = 2;//绑定灵玉
    public static final int BindMoney = 3;//绑定金币
    public static final int ImmortalExp = 4;//仙魄经验
    public static final int GoodEvil = 7;//善恶值
    public static final int EXP = 8;//经验
    public static final int PhysicalStrength = 9;//声望
    public static final int Achievement = 10;//成就
    public static final int GuildContibute = 11;//会贡
    public static final int GoldCoin = 12;//金元宝
    public static final int GuildScore = 13;//仙盟个人积分
    public static final int DeftCrystal = 14;//灵晶（回收炉产出）
    public static final int HolyEquipIntegral = 15;//圣装精碎
    public static final int VipExp = 16; //vip经验
    public static final int UniversePoint = 17; //混沌积分
    public static final int TaskTarget = 18; //任务目标点
    public static final int ActivePoint = 21;//活跃点
    public static final int SoulCoinType_1 = 22;//灵魄精碎用于升级，分解获得副本产出
    public static final int SoulCoinType_2 = 23;//灵魄碎片，可用于灵魄兑换
    public static final int SoulCoinType_3 = 24;//灵魄抽取产物，可用于灵魄合成
    public static final int SoulCoinType_4 = 25;//灵魄秘宝，可用于灵魄抽取
    public static final int LuckyDrawVolume = 26;//抽奖卷
    public static final int ActiveCoin = 27;//活跃货币(运营活动)
    public static final int FallingSkyCoin = 28;//天禁令碎片
    public static final int HonourCoin = 29;//荣誉值(巅峰竞技)
    public static final int MeridianCoin = 30;//经脉系统货币

    public static final int FriendShipPoint = 31;

    public static final int MeridianCoin_1 = 32;
    public static final int MeridianCoin_2 = 33;
    public static final int MeridianCoin_3 = 34;

    public static final int Popularity = 35;    //仙府人气
    public static final int HouseCoin = 36;     //仙府币
    public static final int XianNvCOin = 37;    //仙侣币

    /**
     * 货币配置表id应小于的值，每次新增货币需加1
     */
    public static final int ItemCoinMaxId = 38;

}
