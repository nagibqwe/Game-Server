package com.game.shop.structs;

public class ShopDefine {
    // 成功
    public final static int Succ = 0;
    // 商品不存在
    public final static int ShopReasonNoSell = 1;
    // 物品不存在
    public final static int ShopReasonNoItem = 2;
    // 商品库存不足
    public final static int ShopReasonNoNum = 3;
    // 角色等级不足
    public final static int ShopReasonLimitLvl = 4;
    // 帮会等级不足
    public final static int ShopReasonLimitGuildLvl = 5;
    // 军衔等级不足
    public final static int ShopReasonLimitMrank = 6;
    // VIP等级不足
    public final static int ShopReasonLimitVip = 7;
    // 货币不足
    public final static int ShopReasonNoCoin = 8;
    // 不在该商品的销售时间段
    public final static int ShopReasonNotInTime = 9;
    // 背包空间不足
    public final static int ShopReasonNoBag = 10;
    // 商城已关闭
    public final static int ShopReasonFunction = 11;
    // 购买商品失败
    public final static int ShopReasonFailed = 12;
    // 职业限制
    public final static int ShopReasonLimitCareer = 13;
    // 仙盟商店等级限制
    public final static int ShopReasonGuildShopLvl = 14;
    // 世界等级限制
    public final static int ShopReasonWorldLvl = 15;

    public final static String[] ShopDesc = {
            "元宝商城",
            "兑换商城",
            "福地积分商店",
            "仙盟贡献商店",
            "混沌之境商店"
    };
}
