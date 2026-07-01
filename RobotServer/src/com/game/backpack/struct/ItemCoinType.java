package com.game.backpack.struct;


/**
 * 货币类型   预留1~100为货币类型
 */
public class ItemCoinType {
    /**
     * 货币配置表id应大于的值
     */
    public static final int ItemCoinMinId = 0;

    public static final int GoldType = 1;//钻石
    public static final int BindGold = 2;//绑定钻石
    public static final int BindMoney = 3;//绑定金币
    public static final int ImmortalExp = 4;//仙魄经验
    public static final int GoodEvil = 7;//善恶值
    public static final int EXP = 8;//经验
    public static final int PhysicalStrength = 9;//声望
    public static final int Achievement = 10;//成就
    public static final int GuildContibute = 11;//会贡
    public static final int WorldStoreHouse = 12;//银元宝
    public static final int GuildScore = 13;//仙盟个人积分
    public static final int DeftCrystal = 14;//灵晶（回收炉产出）
    public static final int HolyEquipIntegral = 15;//圣装精碎
    public static final int VipExp = 16; //vip经验
    public static final int UniversePoint = 17; //混沌积分
    public static final int ActivePoint = 21;//活跃点

    /**
     * 货币配置表id应小于的值，每次新增货币需加1
     */
    public static final int ItemCoinMaxId = 22;

}
