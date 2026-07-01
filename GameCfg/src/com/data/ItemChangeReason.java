/**
 * Auto generated, do not edit it
 *
 * ItemChangeReason表
 */
package com.data;

import com.data.struct.ReadStringArray; 

public final class ItemChangeReason{

    /**
     * 移动:1001
     */
    public static int     Move = 1001;
    /**
     * 拆分:1002
     */
    public static int     Slip = 1002;
    /**
     * 合并:1003
     */
    public static int     ComBin = 1003;
    /**
     * 出库:1004
     */
    public static int     StoreToBag = 1004;
    /**
     * 入库:1005
     */
    public static int     BagToStore = 1005;
    /**
     * 背包整理:1006
     */
    public static int     BagClearUp = 1006;
    /**
     * 仓库整理:1007
     */
    public static int     StoreClearUp = 1007;
    /**
     * 仓库移动:1008
     */
    public static int     StoreMove = 1008;
    /**
     * 手动删除消耗:1009
     */
    public static int     OwnDeleteDec = 1009;
    /**
     * 开启背包格子消耗:1013
     */
    public static int     OpenBagCellDec = 1013;
    /**
     * 开启仓库格子消耗:1014
     */
    public static int     OpenStoreCellDec = 1014;
    /**
     * 达成成就获得:1015
     */
    public static int     AchievementGet = 1015;
    /**
     * 好友物品培养增加亲密度消耗:1101
     */
    public static int     FriendSendItemAddValueDec = 1101;
    /**
     * 背包到服务器仓库消耗:1102
     */
    public static int BagServerStoreHouseDec = 1102;
    /**
     * 挚友改名消耗:1103
     */
    public static int ChumChangeNameDec = 1103;
    /**
     * 玩家赠送:1104
     */
    public static int FreindGiftGet = 1104;
    /**
     * 玩家领取获得情义点奖励:1105
     */
    public static int QingyiReciveGoodsGet = 1105;
    /**
     * 玩家赠送获得情义点奖励:1106
     */
    public static int QingyiSendGoodsGet = 1106;
    /**
     * 创建帮会消耗:1201
     */
    public static int     CreateGuildDec = 1201;
    /**
     * 帮会改名消耗:1202
     */
    public static int     ChangeGuildNameDec = 1202;
    /**
     * 弹劾会长消耗:1203
     */
    public static int     GuildImpeachDec = 1203;
    /**
     * 帮会捐献消耗:1204
     */
    public static int     GuildDonateDec = 1204;
    /**
     * 玩家学习公会技能消耗:1205
     */
    public static int     GuildLearnSkillDec = 1205;
    /**
     * 参加公会物品竞拍获得:1206
     */
    public static int     AuctionGet = 1206;
    /**
     * 背包到公会仓库消耗:1207
     */
    public static int     BagGuildStoreHouseDec = 1207;
    /**
     * 公会退出消耗:1208
     */
    public static int     GuildQuitDec = 1208;
    /**
     * 加入公会消耗:1209
     */
    public static int     GuildJoinDec = 1209;
    /**
     * 公会每日领取道具获得:1210
     */
    public static int     GuildReceiveItemGet = 1210;
    /**
     * 帮会日常、周常一键完成单个任务消耗:1211
     */
    public static int GuildTaskOneKeyDec = 1211;
    /**
     * 公会建筑升级消耗:1212
     */
    public static int GuildBuildExpDec = 1212;
    /**
     * 公会维修基金消耗:1213
     */
    public static int GuildFoundExpDec = 1213;
    /**
     * 公会领取工资:1214
     */
    public static int GuildGetItemGet = 1214;
    /**
     * 仙盟任务刷新消耗:1215
     */
    public static int GuildTaskRefreshDec = 1215;
    /**
     * 仙盟战点赞:1216
     */
    public static int GuildBattlePraiseGet = 1216;
    /**
     * 仙盟boss奖励:1218
     */
    public static int GuildBossRewardGet = 1218;
    /**
     * 公会日常奖励获取:1219
     */
    public static int GuildTaskGet = 1219;
    /**
     * 公会周常奖励获取:1220
     */
    public static int GuildTaskGet1 = 1220;
    /**
     * 仙盟日常奖励获取:1221
     */
    public static int GuildTaskGet2 = 1221;
    /**
     * 公会日常奖励消耗:1222
     */
    public static int GuildTaskDec = 1222;
    /**
     * 公会周常奖励消耗:1223
     */
    public static int GuildTaskDec1 = 1223;
    /**
     * 仙盟日常奖励消耗:1224
     */
    public static int GuildTaskDec2 = 1224;
    /**
     * 仙盟战个人奖励:1225
     */
    public static int GuildWarPersonalRewardGet = 1225;
    /**
     * 仙盟boss鼓舞获得:1230
     */
    public static int GuildBossInspireGet = 1230;
    /**
     * 仙盟boss鼓舞消耗:1231
     */
    public static int GuildBossInspireDec = 1231;
    /**
     * 仙盟宝箱获得:1232
     */
    public static int GuildGiftGet = 1232;
    /**
     * 翅膀升级消耗:1301
     */
    public static int     WingUpDec = 1301;
    /**
     * 坐骑进阶消耗:1401
     */
    public static int     HorseUpDec = 1401;
    /**
     * 坐骑化形消耗:1402
     */
    public static int     HorseHuaxingDec = 1402;
    /**
     * 坐骑吃药消耗:1403
     */
    public static int     HorseDrugDec = 1403;
    /**
     * 坐骑装备穿戴消耗:1420
     */
    public static int HorseEquipWearDec = 1420;
    /**
     * 坐骑装备穿戴获得:1421
     */
    public static int HorseEquipWearGet = 1421;
    /**
     * 坐骑装备强化消耗:1422
     */
    public static int HorseEquipIntenDec = 1422;
    /**
     * 坐骑装备附魂消耗:1423
     */
    public static int HorseEquipSoulDec = 1423;
    /**
     * 坐骑装备升级消耗:1424
     */
    public static int HorseEquipComposeDec = 1424;
    /**
     * 坐骑装备升级获得:1425
     */
    public static int HorseEquipComposeGet = 1425;
    /**
     * 坐骑装备分解消耗:1426
     */
    public static int HorseEquipDecomposeDec = 1426;
    /**
     * 坐骑装备分解获得:1427
     */
    public static int HorseEquipDecomposeGet = 1427;
    /**
     * 坐骑装备激活消耗:1428
     */
    public static int HorseEquipActiveDec = 1428;
    /**
     * 坐骑装备自动分解获得:1429
     */
    public static int HorseEquipAutoDecomposeGet = 1429;
    /**
     *  魂甲抽奖获得:1501
     */
    public static int SoulArmorLotteryGet = 1501;
    /**
     *  魂甲抽奖消耗:1502
     */
    public static int SoulArmorLotteryDec = 1502;
    /**
     *  魂甲.魂印分解获得:1503
     */
    public static int SoulArmorBallSplitGet = 1503;
    /**
     *  魂甲.魂印分解消耗:1504
     */
    public static int SoulArmorBallSplitDec = 1504;
    /**
     *  魂甲.魂印卸下获得:1505
     */
    public static int SoulArmorUnWearGet = 1505;
    /**
     *  魂甲淬炼消耗:1506
     */
    public static int SoulArmorUpDec = 1506;
    /**
     *  魂甲突破消耗:1507
     */
    public static int SoulArmorUpQualityDec = 1507;
    /**
     *  魂甲觉醒消耗:1508
     */
    public static int SoulArmorUpSkillLevelDec = 1508;
    /**
     *  魂甲觉醒技能升级消耗:1509
     */
    public static int SoulArmorUpSkillDec = 1509;
    /**
     *  魂甲魂印孔位升级消耗:1510
     */
    public static int SoulArmorUpSlotDec = 1510;
    /**
     *  魂甲魂印穿戴消耗:1511
     */
    public static int SoulArmorWearDec = 1511;
    /**
     *  魂甲.魂印合成消耗:1512
     */
    public static int SoulArmorBallMergeDec = 1512;
    /**
     * 魂甲.魂印合成获得:1513
     */
    public static int SoulArmorBallMergeGet = 1513;
    /**
     *  魂甲双倍抽奖获得:1514
     */
    public static int SoulArmorGoldLotteryGet = 1514;
    /**
     * 仙甲兑换获得:1601
     */
    public static int ExchangeImmortalEquipGet = 1601;
    /**
     * 仙甲兑换消耗:1602
     */
    public static int ExchangeImmortalEquipDec = 1602;
    /**
     * 仙甲分解获得:1603
     */
    public static int ResolveImmortalEquipGet = 1603;
    /**
     * 仙甲分解消耗:1604
     */
    public static int ResolveImmortalEquipDec = 1604;
    /**
     * 仙甲穿戴镶嵌:1605
     */
    public static int InlayImmortalEquip = 1605;
    /**
     * 仙甲合成:1606
     */
    public static int CompoundImmortalEquip = 1606;
    /**
     * 仙甲寻宝消耗:1607
     */
    public static int TreasureXianjiaHuntDec = 1607;
    /**
     * 仙魄合成系统获得:1701
     */
    public static int ImmortalcompoundGet = 1701;
    /**
     * 仙魄合成系消耗:1702
     */
    public static int ImmortalcompoundgetDec = 1702;
    /**
     * 仙魄升级系统消耗:1703
     */
    public static int ImmortalLevelUpDec = 1703;
    /**
     * 仙魄分解获得:1704
     */
    public static int ImmortalResolveGet = 1704;
    /**
     * 仙魄分解消耗:1705
     */
    public static int ImmortalResolveDec = 1705;
    /**
     * 仙魄兑换获得:1706
     */
    public static int ImmortalexchangeGet = 1706;
    /**
     * 仙魄分解消耗:1707
     */
    public static int ImmortalexchangeDec = 1707;
    /**
     * 仙魄拆解获得:1708
     */
    public static int ImmortalDismountingGet = 1708;
    /**
     * 仙魄拆解消耗:1709
     */
    public static int ImmortalDismountingDec = 1709;
    /**
     * 神兵突破消耗:1801
     */
    public static int     WeaponUpDec = 1801;
    /**
     * 神兵化形消耗:1802
     */
    public static int     WeaponHuaxingDec = 1802;
    /**
     * 神兵升级消耗:1803
     */
    public static int GodWeaponUplevelDec = 1803;
    /**
     * 神兵磨具激活消耗:1804
     */
    public static int GodWeaponActModleDec = 1804;
    /**
     * 神兵升品消耗:1805
     */
    public static int GodWeaponUpQualityDec = 1805;
    /**
     * 神兵日常奖励获取:1806
     */
    public static int DailyTaskGet1 = 1806;
    /**
     * 神兵日常奖励消耗:1807
     */
    public static int DailyTaskDec1 = 1807;
    /**
     * 宠物激活消耗:1901
     */
    public static int PetActiveDec = 1901;
    /**
     * 宠物升阶消耗:1902
     */
    public static int PetStrengthDec = 1902;
    /**
     * 宠物御魂消耗:1903
     */
    public static int PetSoulDec = 1903;
    /**
     * 宠物吞噬装备消耗:1904
     */
    public static int PetEatEquipDec = 1904;
    /**
     * 替换宠物装备获得:1905
     */
    public static int ReplacePetEquipGet = 1905;
    /**
     * 熔炼宠物装备扣除:1907
     */
    public static int MeltPetEquipDec = 1907;
    /**
     * 强化宠物装备扣除:1908
     */
    public static int IntenPetEquipDec = 1908;
    /**
     * 附魂宠物装备扣除:1909
     */
    public static int SoulPetEquipDec = 1909;
    /**
     * 合成宠物装备扣除:1910
     */
    public static int ComposePetEquipDec = 1910;
    /**
     * 主动分解宠物装备扣除:1911
     */
    public static int DecomposePetEquipDec = 1911;
    /**
     * 主动分解宠物装备获得:1912
     */
    public static int DecomposePetEquipGet = 1912;
    /**
     * 自动分解宠物装备扣除:1913
     */
    public static int AutoDecomposePetEquipDec = 1913;
    /**
     * 自动分解宠物装备获得:1914
     */
    public static int AutoDecomposePetEquipGet = 1914;
    /**
     * 激活宠物装备槽:1915
     */
    public static int ActivePetEquipSlotDec = 1915;
    /**
     * 穿戴宠物装备旧装备背包获得:1920
     */
    public static int DressPetEquipGet = 1920;
    /**
     * 穿戴宠物装备背包扣除:1921
     */
    public static int DressPetEquipDec = 1921;
    /**
     * 合成宠物装备获得:1922
     */
    public static int ComposePetEquipGet = 1922;
    /**
     * 阵法升级消耗:2001
     */
    public static int     MagicUpDec = 2001;
    /**
     * 阵法化形消耗:2002
     */
    public static int     MagicHuaxingDec = 2002;
    /**
     * 阵法吃药消耗:2003
     */
    public static int     MagicDrugDec = 2003;
    /**
     * 阵法附灵消耗:2101
     */
    public static int     WeaponAffiliatedSpiritDec = 2101;
    /**
     * 市集求购消耗:2102
     */
    public static int MarketWantBuyDec = 2102;
    /**
     * 市集求购商品下架获得:2103
     */
    public static int MarketWantBuyDownBackGet = 2103;
    /**
     * 市集交易下架获得:2104
     */
    public static int MarketDownBackGet = 2104;
    /**
     * 市集求购售卖消耗:2105
     */
    public static int MarketWantBuySellDec = 2105;
    /**
     * 市集求购获得:2106
     */
    public static int MarketWantBuySellGet = 2106;
    /**
     * 交易消耗:2107
     */
    public static int     TradeDec = 2107;
    /**
     * 交易获得:2108
     */
    public static int     TradeGet = 2108;
    /**
     * 交易卸下获得:2109
     */
    public static int     TradeUnloadGet = 2109;
    /**
     * 交易取消获得:2110
     */
    public static int     TradeCancelGet = 2110;
    /**
     * 交易失败获得:2111
     */
    public static int     TradeFailGet = 2111;
    /**
     * 交易异常获得:2112
     */
    public static int     TradeExceptionGet = 2112;
    /**
     * 拍卖行合成购买获得:2113
     */
    public static int AuctionFastPurGet = 2113;
    /**
     * 竞拍失败退回获得:2114
     */
    public static int AuctionFailureGet = 2114;
    /**
     * 拍卖成功获得:2115
     */
    public static int AuctionSuccessfulGet = 2115;
    /**
     * 出售成功获得:2116
     */
    public static int SaleSuccessfulGet = 2116;
    /**
     * 无人购买退回物品获得:2117
     */
    public static int SaleFailureGet = 2117;
    /**
     * 拍卖行下架获得:2118
     */
    public static int AuctionOutGet = 2118;
    /**
     * 拍卖行合成购买消耗:2119
     */
    public static int AuctionFastPurDec = 2119;
    /**
     * 购买市集的物品消耗:2120
     */
    public static int     MarketBuyItemDec = 2120;
    /**
     * 贩卖市集的物品获得:2121
     */
    public static int     MarketSellItemGet = 2121;
    /**
     * 市集中上架物品消耗:2122
     */
    public static int     MarketUpItemDec = 2122;
    /**
     * 交易失败货币返回获得:2123
     */
    public static int     MarketBuyFailureGet = 2123;
    /**
     * 集市邮件的附件领取获得:2124
     */
    public static int     MarketMailReceiveGet = 2124;
    /**
     * 拍卖行竞价消耗:2125
     */
    public static int AuctionPriceDec = 2125;
    /**
     * 拍卖行上架消耗:2126
     */
    public static int AuctionPutDec = 2126;
    /**
     * 拍卖行一口价消耗:2127
     */
    public static int AuctionPurDec = 2127;
    /**
     * 神器升级消耗:2301
     */
    public static int     TalismanUpDec = 2301;
    /**
     * 神器化形消耗:2302
     */
    public static int     TalismanHuaxingDec = 2302;
    /**
     * 神器吃药消耗:2303
     */
    public static int     TalismanDrugDec = 2303;
    /**
     * 穿上装备消耗:2401
     */
    public static int     WearEquipDec = 2401;
    /**
     * 卸下装备获得:2402
     */
    public static int     UnWearEquipGet = 2402;
    /**
     * 分解装备消耗:2403
     */
    public static int     ResolveEquipDec = 2403;
    /**
     * 合成消耗:2404
     */
    public static int     CompoundDec = 2404;
    /**
     * 装备出售消耗:2405
     */
    public static int     EquipSellDec = 2405;
    /**
     * 装备神炼消耗:2406
     */
    public static int     EquipGodTriedDec = 2406;
    /**
     * 装备合成消耗:2407
     */
    public static int     EquipSyntheticDec = 2407;
    /**
     * 装备合成消耗:2408
     */
    public static int     EquipSyntheticSellDec = 2408;
    /**
     * 装备套装消耗:2409
     */
    public static int     EquipSuitDec = 2409;
    /**
     * 装备套装石合成消耗:2410
     */
    public static int     EquipSuitSynDec = 2410;
    /**
     * 合成的装备拆解获得:2411
     */
    public static int     EquipSynSplitGet = 2411;
    /**
     * 转换职业时删除装备消耗:2412
     */
    public static int     ChangeJobDeleteEquipDec = 2412;
    /**
     * 合成新物品:2413
     */
    public static int     CompoundAdd = 2413;
    /**
     * 装备部位强化消耗:2414
     */
    public static int     EquipPartStrengthenDec = 2414;
    /**
     * 宝石镶嵌or卸下:2415
     */
    public static int     GemInlayDown = 2415;
    /**
     * 仙玉镶嵌or卸下:2416
     */
    public static int     JadeInlayDown = 2416;
    /**
     * 宝石精炼消耗:2417
     */
    public static int GemRefineDec = 2417;
    /**
     * 洗练消耗:2418
     */
    public static int EquipWashDec = 2418;
    /**
     * 洗髓消耗:2419
     */
    public static int XiSuiDec = 2419;
    /**
     * 合成获得:2420
     */
    public static int CompoundGet = 2420;
    /**
     * 回收炉删除装备获得:2421
     */
    public static int RecycleGet = 2421;
    /**
     * 回收炉删除装备消耗:2422
     */
    public static int RecycleDec = 2422;
    /**
     * 神品装备升星消耗:2423
     */
    public static int ShenpinEquipUpStarDec = 2423;
    /**
     * 神品装备升星获得:2424
     */
    public static int ShenpinEquipUpStarGet = 2424;
    /**
     * 神品装备升阶消耗:2425
     */
    public static int ShenpinEquipUpStageDec = 2425;
    /**
     * 神品装备升阶获得:2426
     */
    public static int ShenpinEquipUpStageGet = 2426;
    /**
     * 装备拆解消耗:2427
     */
    public static int EquipSplitDec = 2427;
    /**
     * 装备拆解获得:2428
     */
    public static int EquipSplitGet = 2428;
    /**
     * 手动使用物品消耗:2501
     */
    public static int     OwnUseDec = 2501;
    /**
     * 卖出物品消耗:2502
     */
    public static int     SellItemDec = 2502;
    /**
     * 图鉴吞噬消耗:2503
     */
    public static int     CardSmeltDec = 2503;
    /**
     * 开礼包获得:2504
     */
    public static int     OpenGiftGet = 2504;
    /**
     * 角色改名卡道具消耗:2505
     */
    public static int     ChangeNameDec = 2505;
    /**
     * 使用道具增加魅力消耗:2506
     */
    public static int     ItemUseAddIntimacyDec = 2506;
    /**
     * 吃药消耗:2507
     */
    public static int     DrugDec = 2507;
    /**
     * 经验丹经验获得:2508
     */
    public static int ExpAddItemGet = 2508;
    /**
     * 消费元宝卡元宝值获得:2520
     */
    public static int     UserItemCardGetGoldGet = 2520;
    /**
     * 使用圣魂消耗:2521
     */
    public static int UseHolySoulDec = 2521;
    /**
     * 商城元宝消费获得:2601
     */
    public static int     ShopBuyGoldGet = 2601;
    /**
     * 商城元宝消费消耗:2602
     */
    public static int     ShopBuyGoldDec = 2602;
    /**
     * 商城消费获得:2603
     */
    public static int     ShopBuyCostGet = 2603;
    /**
     * 商城消费消耗:2604
     */
    public static int     ShopBuyCostDec = 2604;
    /**
     * 神秘限购商品:2610
     */
    public static int LimitShop = 2610;
    /**
     * 神秘限购商品获得:2611
     */
    public static int LimitShopGet = 2611;
    /**
     * 神秘限购商品消耗:2612
     */
    public static int LimitShopDec = 2612;
    /**
     * 神秘商店:2620
     */
    public static int MyShopReward = 2620;
    /**
     * 神秘商店获得:2621
     */
    public static int MyShopRewardGet = 2621;
    /**
     * 神秘商店消耗:2622
     */
    public static int MyShopRewardDec = 2622;
    /**
     * 任务完成奖励获得:2721
     */
    public static int     TaskRewardsGet = 2721;
    /**
     * 任务完成奖励消耗:2722
     */
    public static int     TaskRewardsDec = 2722;
    /**
     * 日常任务扣除消耗:2702
     */
    public static int     DailyTaskDec = 2702;
    /**
     * 任务加倍完成消耗:2703
     */
    public static int     TaskFinishFanBeiDec = 2703;
    /**
     * 主线任务奖励获得:2704
     */
    public static int     MainTaskRewardGet = 2704;
    /**
     * 直线任务奖励获得:2705
     */
    public static int     BranchTaskRewardGet = 2705;
    /**
     * 任务提交道具消耗:2706
     */
    public static int     TaskSubmitItemDec = 2706;
    /**
     * 任务收集道具消耗:2707
     */
    public static int     CollectTaskSubmitItemDec = 2707;
    /**
     * 领取护送任务消耗:2708
     */
    public static int     ReceiveEscortTaskCostDec = 2708;
    /**
     * 日常任务一键完成单个任务消耗:2709
     */
    public static int DailyTaskOneKeyDec = 2709;
    /**
     * 领取任务目标奖励消耗:2710
     */
    public static int TaskTargetRewardDec = 2710;
    /**
     * 任务目标奖励获得:2711
     */
    public static int TaskTargetRewardGet = 2711;
    /**
     * 完成日常次数获得:2715
     */
    public static int CompleteDailyGet = 2715;
    /**
     * 圣装合成获得:2801
     */
    public static int HolyEquipCompoundGet = 2801;
    /**
     * 圣装分解获得:2802
     */
    public static int HolyEquipResolveGet = 2802;
    /**
     * 圣装合成消耗:2803
     */
    public static int HolyEquipCompoundDec = 2803;
    /**
     * 圣装镶嵌获得:2804
     */
    public static int HolyInlayReasonGet = 2804;
    /**
     * 圣装分解扣除消耗:2805
     */
    public static int HolyEquipResolveDec = 2805;
    /**
     * 结婚消耗:2901
     */
    public static int     GetMarriageDec = 2901;
    /**
     * 强制离婚消耗:2902
     */
    public static int     ForceDivorceDec = 2902;
    /**
     * 参加婚宴送礼金消耗:2903
     */
    public static int CashGiftDec = 2903;
    /**
     * 仙缘心锁升级消耗:2904
     */
    public static int MarryLocalUpLevelDec = 2904;
    /**
     * 婚姻仙居突破消耗:2905
     */
    public static int MarryHouseBreakDec = 2905;
    /**
     * 仙缘.情缘副本获得:2906
     */
    public static int MarryCopyMapGet = 2906;
    /**
     * 结婚--仙娃激活消耗:2907
     */
    public static int MarryChildActiveDec = 2907;
    /**
     * 结婚--仙娃升级消耗:2908
     */
    public static int MarryChildLevelDec = 2908;
    /**
     * 婚姻领取每日宝匣奖励获得:2909
     */
    public static int MarryDailyBoxRewardGet = 2909;
    /**
     * 婚姻领取宝匣返利奖励获得:2910
     */
    public static int MarryRebateBoxRewardGet = 2910;
    /**
     * 婚姻购买宝匣消耗:2911
     */
    public static int MarryBoxBuyDec = 2911;
    /**
     * 婚姻系统--对诗获得:2912
     */
    public static int MarryPrayPoemGet = 2912;
    /**
     * 婚姻系统--祈福收取果实获得:2913
     */
    public static int MarryPrayGetAppleGet = 2913;
    /**
     * 婚姻系统--领取亲密度奖励获得:2914
     */
    public static int MarryIntimacyGet = 2914;
    /**
     * 婚姻系统--满足条件称号获得:2915
     */
    public static int MarryTitleGet = 2915;
    /**
     * 婚宴操作--购买喜糖消耗:2916
     */
    public static int WeddingBuyCandiesDec = 2916;
    /**
     * 婚宴操作--购买礼炮消耗:2917
     */
    public static int WeddingBuySaluteDec = 2917;
    /**
     * 婚宴操作--购买烟花消耗:2918
     */
    public static int WeddingBuyFireDec = 2918;
    /**
     * 婚姻--三倍领取祈福果实获得:2919
     */
    public static int MarryTripleGet = 2919;
    /**
     * 婚姻-仙娃改名消耗:2920
     */
    public static int MarryChildChangeNameDec = 2920;
    /**
     * 结婚消耗:2921
     */
    public static int MarryDec = 2921;
    /**
     * 结婚宣言消耗:2922
     */
    public static int MarryNoticeDec = 2922;
    /**
     * 强制离婚消耗:2923
     */
    public static int Force_Divorce_Dec = 2923;
    /**
     * 婚礼赠送消耗:2924
     */
    public static int WeddingSendDec = 2924;
    /**
     * 婚礼购买消耗:2925
     */
    public static int WeddingBuyDec = 2925;
    /**
     * 婚礼使用消耗:2926
     */
    public static int WeddingUseDec = 2926;
    /**
     * 婚礼购买获得:2927
     */
    public static int WeddingBuyGet = 2927;
    /**
     *  仙缘任务获得:2928
     */
    public static int MarryTaskGet = 2928;
    /**
     * 仙缘奖励获得:2929
     */
    public static int MarrySuccessGet = 2929;
    /**
     * 婚宴获得:2930
     */
    public static int MarryBossRewardGet = 2930;
    /**
     * 仙缘-缘定三生:2931
     */
    public static int MarryWall = 2931;
    /**
     * 仙缘-缘定三生消耗:2932
     */
    public static int MarryWallDec = 2932;
    /**
     * 仙缘-缘定三生获得:2933
     */
    public static int MarryWallGet = 2933;
    /**
     * 发布爱情宣言获得:2934
     */
    public static int PushMarryDeclarationGet = 2934;
    /**
     * 发布爱情宣言消耗:2935
     */
    public static int PushMarryDeclarationCost = 2935;
    /**
     * 普通结婚消耗:2936
     */
    public static int MarryGeneralDec = 2936;
    /**
     * 高级结婚消耗:2937
     */
    public static int MarryHigherDec = 2937;
    /**
     * 豪华结婚消耗:2938
     */
    public static int MarryLuxuryDec = 2938;
    /**
     * 普通结婚获得:2939
     */
    public static int MarryGeneralGet = 2939;
    /**
     * 高级结婚获得:2940
     */
    public static int MarryHigherGet = 2940;
    /**
     * 豪华结婚获得:2941
     */
    public static int MarryLuxuryGet = 2941;
    /**
     * 完美情缘排名奖励获得:2942
     */
    public static int MarryActivityRankGet = 2942;
    /**
     * 完美情缘商店购买获得:2943
     */
    public static int MarryActivityShopBuyGet = 2943;
    /**
     * 完美情缘任务奖励获得:2944
     */
    public static int MarryActivityTaskRewardGet = 2944;
    /**
     * 婚姻副本购买热度消耗:2945
     */
    public static int MarryCopyBuyHotDes = 2945;
    /**
     * 完美情缘任务奖励获得:2946
     */
    public static int MarryCopySigRewardGet = 2946;
    /**
     * 情缘福袋回收:2947
     */
    public static int MarryActivityGiftCost = 2947;
    /**
     * 情缘福袋回收:2948
     */
    public static int MarryActivityGiftGet = 2948;
    /**
     *  婚礼祝福获得:2949
     */
    public static int MarryBlessGiftGet = 2949;
    /**
     * 激活称号消耗:3001
     */
    public static int ActiveTitleDec = 3001;
    /**
     * 邮件附件领取获得:3101
     */
    public static int     MailAttachReceiveGet = 3101;
    /**
     * 邮件发送使用:3102
     */
    public static int MailSentUse = 3102;
    /**
     * 灵魄寻宝购买扣除:3301
     */
    public static int SoulBuyDec = 3301;
    /**
     * 灵魄寻宝购买赠送获得:3302
     */
    public static int SoulBuySendGet = 3302;
    /**
     * 灵魄寻宝抽奖扣除:3303
     */
    public static int SoulHuntDec = 3303;
    /**
     * 灵魄寻宝抽奖获得:3304
     */
    public static int SoulHuntGet = 3304;
    /**
     * 寻宝回收道具获得:3305
     */
    public static int TreasureRecoveryGet = 3305;
    /**
     * 寻宝回收道具扣除:3306
     */
    public static int TreasureRecoveryCost = 3306;
    /**
     * 剑灵阁领取收益:3401
     */
    public static int SwordSoulTowerGet = 3401;
    /**
     * 剑灵阁快速收益获得:3402
     */
    public static int SwordSoulQuickRewardGet = 3402;
    /**
     * vip周奖励礼包奖励获得:3502
     */
    public static int VipWeekGiftGet = 3502;
    /**
     * vip周奖励自动领取礼包奖励获得:3503
     */
    public static int VipWeekAutoGiftGet = 3503;
    /**
     * vip充值奖励获得:3504
     */
    public static int VipRechargeGiftGet = 3504;
    /**
     * VIP经验道具获得:3505
     */
    public static int VipExpItemGet = 3505;
    /**
     * Vip目标奖励获得:3506
     */
    public static int VipTargetRewardGet = 3506;
    /**
     * 充值vip经验获得:3507
     */
    public static int RechargeVipExpGet = 3507;
    /**
     * 在线送vip经验获得:3508
     */
    public static int OnlineVipExpGet = 3508;
    /**
     *  VIP经验版本修正获得:3509
     */
    public static int VIPExpFixedGet = 3509;
    /**
     * 购买vip礼包奖励:3510
     */
    public static int VipPurGift = 3510;
    /**
     * 购买vip礼包奖励获得:3511
     */
    public static int VipPurGiftGet = 3511;
    /**
     * 购买vip礼包奖励消耗:3512
     */
    public static int VipPurGiftDec = 3512;
    /**
     * vip升级获得:3513
     */
    public static int VipLevelUpGet = 3513;
    /**
     * vip珠宝使用获得:3514
     */
    public static int VipPearlGet = 3514;
    /**
     * 魂兽增加额外格子消耗:3601
     */
    public static int     SoulBeastAddExtendGridDec = 3601;
    /**
     * 魂兽售卖道具和装备获得:3602
     */
    public static int SoulbestSellItemGet = 3602;
    /**
     * 魂兽装备强化消耗:3603
     */
    public static int SoulBeastStrengthenDec = 3603;
    /**
     * 魂兽装备合成消耗:3604
     */
    public static int SoulBeastMergeDec = 3604;
    /**
     * 魂兽装备合成获得:3605
     */
    public static int SoulBeastMergeGet = 3605;
    /**
     * 魂兽售卖道具消耗:3606
     */
    public static int SoulbestSellItemDec = 3606;
    /**
     * 竞技场每日奖励获得:3701
     */
    public static int     JJCRewardGet = 3701;
    /**
     * 首席竞技场获得:3702
     */
    public static int     JJCBattleGet = 3702;
    /**
     * 竞技场首次达到排名奖励获得:3703
     */
    public static int JJCFirstRewardGet = 3703;
    /**
     * 竞技场购买次数消耗:3704
     */
    public static int JJCBuyCountGetDec = 3704;
    /**
     * 巅峰竞技场段位奖励获得:3705
     */
    public static int PeekStageRewardGet = 3705;
    /**
     * 巅峰竞技场场次奖励获得:3706
     */
    public static int PeekTimesRewardGet = 3706;
    /**
     * 巅峰竞技场挑战奖励获得:3707
     */
    public static int PeekPkRewardGet = 3707;
    /**
     * 竞技场排行奖励:3708
     */
    public static int JJCRankGet = 3708;
    /**
     * 福利：每日签到消耗:3801
     */
    public static int WelfareDayCheckInDec = 3801;
    /**
     * 福利：每日签到:3802
     */
    public static int WelfareDayCheckIn = 3802;
    /**
     * 福利：月卡尊享卡:3803
     */
    public static int WelfareExclusiveCard = 3803;
    /**
     * 福利：月卡尊享卡获得:3804
     */
    public static int WelfareExclusiveCardGet = 3804;
    /**
     * 福利：月卡尊享卡消耗:3805
     */
    public static int WelfareExclusiveCardDec = 3805;
    /**
     * 福利：感悟经验获得:3806
     */
    public static int WelfareFeelingExpGet = 3806;
    /**
     * 福利：感悟经验消耗:3807
     */
    public static int WelfareFeelingExpDec = 3807;
    /**
     * 福利：累计签到获得:3808
     */
    public static int WelfareDayTotalCheckInGet = 3808;
    /**
     * 福利：每日签到获得:3809
     */
    public static int WelfareDayCheckInGet = 3809;
    /**
     * 福利：感悟经验:3810
     */
    public static int WelfareFeelingExp = 3810;
    /**
     * 等级礼包获得奖励:3811
     */
    public static int LevelGiftAdd = 3811;
    /**
     * vip每日礼包奖励:3812
     */
    public static int VipDailyGift = 3812;
    /**
     * 福利：感悟银币获得:3813
     */
    public static int WelfareFeelingCoinGet = 3813;
    /**
     * 福利：感悟银币消耗:3814
     */
    public static int WelfareFeelingCoinDec = 3814;
    /**
     * 福利：周卡:3815
     */
    public static int WelfareCardWeek = 3815;
    /**
     * 福利：周卡获得:3816
     */
    public static int WelfareCardWeekGet = 3816;
    /**
     * 成长基金:3817
     */
    public static int WelfareGrowthFund = 3817;
    /**
     * 福利：登陆礼包:3820
     */
    public static int WelfareLoginGift = 3820;
    /**
     * 福利：登陆礼包获得:3821
     */
    public static int WelfareLoginGiftGet = 3821;
    /**
     * 周福利幸运抽奖消耗:3822
     */
    public static int LuckyDrawWeekDec = 3822;
    /**
     * 周福利幸运抽奖获得:3823
     */
    public static int LuckyDrawWeekGet = 3823;
    /**
     * 等级礼包vip额外获得奖励:3824
     */
    public static int LevelGiftVipAdd = 3824;
    /**
     * 免费礼包 获得奖励:3825
     */
    public static int WelfareFreeGiftGet = 3825;
    /**
     * 转职消耗:3901
     */
    public static int     ChangeJobDec = 3901;
    /**
     * 转职任务奖励获得:3902
     */
    public static int     GenderTaskRewardGet = 3902;
    /**
     * 转职任务一键完成的时候扣除消耗:3903
     */
    public static int     GenderTaskOneKeyFinishDec = 3903;
    /**
     * 转职任务一键完成:3904
     */
    public static int GenderTaskOneKeyFinish = 3904;
    /**
     * 转职阶段完成获得:3905
     */
    public static int GenderStageFinishGet = 3905;
    /**
     * 崇拜排行榜玩家奖励获得:4001
     */
    public static int     WorshipRewardGet = 4001;
    /**
     * 掉落获得:4101
     */
    public static int     DropGet = 4101;
    /**
     * 复活消耗:4102
     */
    public static int     ReliveDec = 4102;
    /**
     * 小地图传送消耗:4103
     */
    public static int     MiniMapTransDec = 4103;
    /**
     * Pk消耗:4104
     */
    public static int     PkDec = 4104;
    /**
     * 杀怪掉落获得:4105
     */
    public static int     DropByKillMonsterGet = 4105;
    /**
     * 离线挂机获得:4106
     */
    public static int     HookOfflineGet = 4106;
    /**
     * 在线打坐获得:4107
     */
    public static int     HookOnlineGet = 4107;
    /**
     * 地图经验获得:4108
     */
    public static int     HookMapGet = 4108;
    /**
     * 职业掉落获得:4109
     */
    public static int ProDropGet = 4109;
    /**
     * 采集掉落获得:4110
     */
    public static int     DropByGatherGet = 4110;
    /**
     * 首杀红包奖励:4112
     */
    public static int FirstKillRedPacket = 4112;
    /**
     * 服务器首杀奖励:4113
     */
    public static int ServerFirstKillReward = 4113;
    /**
     * 共享掉落:4114
     */
    public static int ShareDrop = 4114;
    /**
     * 神兽岛采集获得:4115
     */
    public static int SoulAnimalGatherDropGet = 4115;
    /**
     * 恭喜获得服务器首杀奖励:4116
     */
    public static int FirstKillBossKillRewardGain = 4116;
    /**
     * 挂机经验找回获得:4117
     */
    public static int     HookFindTimeGet = 4117;
    /**
     * 挂机经验找回扣除:4118
     */
    public static int     HookFindTimeDec = 4118;
    /**
     * 通关副本获得:4201
     */
    public static int     CopyMapGet = 4201;
    /**
     * 鼓舞消耗:4202
     */
    public static int     UpMoraleDec = 4202;
    /**
     * 掉落获得:4203
     */
    public static int     DropByFightServerGet = 4203;
    /**
     * 扫荡副本道具消耗:4204
     */
    public static int     SweepCloneUseItemDec = 4204;
    /**
     * 星级副本星数奖励获得:4205
     */
    public static int StarCopyRewardGet = 4205;
    /**
     * 星级副本扫荡消耗:4206
     */
    public static int StarCopySweepDec = 4206;
    /**
     * 进入星级副本消耗:4207
     */
    public static int StarCopyEnterDec = 4207;
    /**
     * vip副本次数购买消耗:4208
     */
    public static int VipCopyMapBuyDec = 4208;
    /**
     * 经验副本获得:4209
     */
    public static int ExpCopyGet = 4209;
    /**
     * 婚礼副本经验获得:4210
     */
    public static int MarrigeCopyExpGet = 4210;
    /**
     *  集字活动副本掉落:4211
     */
    public static int HolidayActivityWordsCloneDrop = 4211;
    /**
     * 副本合并消耗:4212
     */
    public static int ZoneMergeDec = 4212;
    /**
     * 仙缘.情缘副本:4213
     */
    public static int MarryCopyMap = 4213;
    /**
     * boss之家进入消耗:4301
     */
    public static int     EnterBossHomeDec = 4301;
    /**
     * 击杀Boss活动获得:4302
     */
    public static int     ActivityKillBossGet = 4302;
    /**
     * 个人boss消耗:4303
     */
    public static int PersonalBossDec = 4303;
    /**
     * boss击杀归属奖励获得:4304
     */
    public static int BossOrdinaryGet = 4304;
    /**
     * boss次数特殊掉落获得:4305
     */
    public static int BossRelationGet = 4305;
    /**
     * boss排名掉落获得:4306
     */
    public static int BossRankGet = 4306;
    /**
     * boss阳光普照奖励获得:4307
     */
    public static int BossCapitaGet = 4307;
    /**
     * 购买boss排名奖励次数消耗:4308
     */
    public static int BuyBossRankDec = 4308;
    /**
     * 套装boss进入消耗:4309
     */
    public static int SuitBossDec = 4309;
    /**
     * 宝石boss进入消耗:4310
     */
    public static int GemBossDec = 4310;
    /**
     * 首杀boss个人击杀奖励获得:4311
     */
    public static int PersonFirstKillRewardGet = 4311;
    /**
     * 首领活动掉落获得:4312
     */
    public static int HolidayBossDropGet = 4312;
    /**
     * 首领活动礼包开启掉落获得:4313
     */
    public static int HolidayBossGiftDropGet = 4313;
    /**
     * 首领活动购买获得:4314
     */
    public static int HolidayBossShopGet = 4314;
    /**
     * 荒古神坛活动掉落:4315
     */
    public static int HorseBossDropGet = 4315;
    /**
     * 重置经脉获得:4401
     */
    public static int MeridianRestGet = 4401;
    /**
     * 重置经脉扣除消耗:4402
     */
    public static int MeridianRestDec = 4402;
    /**
     * 激活经脉扣除消耗:4403
     */
    public static int MeridianActiveDec = 4403;
    /**
     * 感谢支援玩家时被支援者的奖励获得:4501
     */
    public static int WorldHelpThkAddGet = 4501;
    /**
     * 感谢支援玩家时支援者的奖励获得:4502
     */
    public static int WorldHelpThkAdd2Get = 4502;
    /**
     * BOSS死亡时被支援者的奖励获得:4503
     */
    public static int WorldHelpBossDieAddGet = 4503;
    /**
     * BOSS死亡时支援者的奖励获得:4504
     */
    public static int WorldHelpBossDieAdd2Get = 4504;
    /**
     * 感谢支援玩家时扣除道具消耗:4506
     */
    public static int WorldHelpThkDec = 4506;
    /**
     * GM后台移除物品消耗:4601
     */
    public static int     GMDeductItemDec = 4601;
    /**
     * 后台功能扣除元宝值消耗:4602
     */
    public static int BackServerGoldDec = 4602;
    /**
     * gm获得:4603
     */
    public static int GM = 4603;
    /**
     * gm扣除:4605
     */
    public static int GMDec = 4605;
    /**
     * gm获得:4606
     */
    public static int GMGet = 4606;
    /**
     * GM来增加元宝获得:4609
     */
    public static int     GMToGetGoldGet = 4609;
    /**
     * 后台过来增加元宝获得:4668
     */
    public static int     BackServerToRechargeGet = 4668;
    /**
     * 识海升级消耗:4701
     */
    public static int ShiHaiDec = 4701;
    /**
     * 境界任务领奖获得:4802
     */
    public static int StateVipRewardGet = 4802;
    /**
     * 境界突破奖励获得:4803
     */
    public static int StateVipUpRewardGet = 4803;
    /**
     * 境界礼包购买获得:4804
     */
    public static int StateGiftPurGet = 4804;
    /**
     * 境界经验掉落获得:4805
     */
    public static int StateVipExpDropGet = 4805;
    /**
     * 境界副本挑战获取:4806
     */
    public static int BossStateChanllageGet = 4806;
    /**
     * 境界副本扫荡提取:4807
     */
    public static int BossStateSawapGet = 4807;
    /**
     * 升级境界灵压消耗:4808
     */
    public static int UpLevelStateStifleDec = 4808;
    /**
     * 购买境界boss进入次数消耗:4809
     */
    public static int BuyBossStateVipCountDec = 4809;
    /**
     * 蜕变消耗:4901
     */
    public static int MentalSkillTuibianDec = 4901;
    /**
     * 心法升级消耗:5001
     */
    public static int MentalUpDec = 5001;
    /**
     * 掌门传道经验获得:5101
     */
    public static int LeaderPreachAddExpGet = 5101;
    /**
     * 掌门传道道具奖励获得:5102
     */
    public static int LeaderPreachRewardGet = 5102;
    /**
     * 掌门传道扣除:5103
     */
    public static int LeaderPreachDec = 5103;
    /**
     * 寻宝消耗:5204
     */
    public static int TreasureHuntDec = 5204;
    /**
     * 寻宝获得:5205
     */
    public static int TreasureHuntGet = 5205;
    /**
     * 寻宝提取获得:5206
     */
    public static int TreasureHuntExtractGet = 5206;
    /**
     * 寻宝处购买消耗:5207
     */
    public static int TreasureHuntBuyDec = 5207;
    /**
     * 寻宝处购买获得:5208
     */
    public static int TreasureHuntBuyGet = 5208;
    /**
     * 寻宝仓库一键提取获得:5209
     */
    public static int TreasureStoreOnekeyExtractGet = 5209;
    /**
     * 秘宝获得:5210
     */
    public static int TreasureHuntMibaoGet = 5210;
    /**
     * 仙甲寻宝获得:5211
     */
    public static int TreasureHuntXJGet = 5211;
    /**
     * 仙甲寻宝奖励获得:5212
     */
    public static int TreasureHuntXJAwardGet = 5212;
    /**
     * 聚宝盆领取获得:5301
     */
    public static int AgateGet = 5301;
    /**
     * 聚宝盆奖池领取获得:5302
     */
    public static int AgatePoolGet = 5302;
    /**
     * 灵压法宝升级消耗:5403
     */
    public static int StiflefFaBaoUpDec = 5403;
    /**
     * 法宝器灵激活升级消耗:5404
     */
    public static int SoulSpiritDec = 5404;
    /**
     * 法宝日常奖励获取:5405
     */
    public static int DailyTaskGet = 5405;
    /**
     * 时装升星消耗:5606
     */
    public static int     FashionStarUpDec = 5606;
    /**
     * 时装激活消耗:5607
     */
    public static int ActiveFashionDec = 5607;
    /**
     * 时装发型该表消耗:5608
     */
    public static int FashionHairChangeDec = 5608;
    /**
     * 激活神兽消耗:5701
     */
    public static int     ActiveMythicalDec = 5701;
    /**
     * 技能等级升级消耗:5801
     */
    public static int     SkillLevelUpDec = 5801;
    /**
     * 心法被动技能升级消耗:5802
     */
    public static int MentalSkillUp = 5802;
    /**
     * 新技能格子升级:5803
     */
    public static int NewSkillUpCellDec = 5803;
    /**
     * 新技能升星:5804
     */
    public static int NewSkillUpstarDec = 5804;
    /**
     * 升级血脉系统消耗:5901
     */
    public static int     UpBloodDec = 5901;
    /**
     * 灵体放入或替换装备获得:6001
     */
    public static int SpiritGet = 6001;
    /**
     * 灵体放入或替换装备消耗:6002
     */
    public static int SpiritDec = 6002;
    /**
     * 灵体解封消耗:6003
     */
    public static int SpiritStartUseDec = 6003;
    /**
     * 服务器改名消耗:6101
     */
    public static int ChangeServerNameDec = 6101;
    /**
     * 新服活动奖励:6103
     */
    public static int NewActive = 6103;
    /**
     * 平台评价点赞奖励获得:6105
     */
    public static int PlatformEvaluateLike = 6105;
    /**
     * 平台评价分享奖励获得:6106
     */
    public static int PlatformEvaluateShare = 6106;
    /**
     * 平台评价每日分享奖励获得:6107
     */
    public static int PlatformEvaluateEveryDayShare = 6107;
    /**
     * 商店评价引导奖励获得:6108
     */
    public static int ShopCommentRewardsGet = 6108;
    /**
     * 犒赏令-荒古令获得:6201
     */
    public static int KaoShangLingHorseGet = 6201;
    /**
     * 犒赏令-荒古令消耗:6202
     */
    public static int KaoShangLingHorseDec = 6202;
    /**
     * 犒赏令-荒古令购买高级消耗:6203
     */
    public static int KaoShangLingHorseBuySpecailDec = 6203;
    /**
     * 魔魂升级消耗:6301
     */
    public static int DevilCardLevelUpCost = 6301;
    /**
     * 魔魂升阶消耗:6302
     */
    public static int DevilCardRankUpCost = 6302;
    /**
     * 魔魂突破消耗:6303
     */
    public static int DevilCardBreakCost = 6303;
    /**
     * 魔魂解锁消耗:6304
     */
    public static int DevilCardUnlockCost = 6304;
    /**
     * 魔魂装备合成消耗:6305
     */
    public static int DevilEquipSynthesisCost = 6305;
    /**
     * 魔魂装备合成获得:6306
     */
    public static int DevilEquipSynthesisGet = 6306;
    /**
     * 魔魂装备穿戴获得:6307
     */
    public static int DevilEquipWearGet = 6307;
    /**
     * 魔魂装备穿戴消耗:6308
     */
    public static int DevilEquipWearCost = 6308;
    /**
     * 封魔台高级抽奖获得:6310
     */
    public static int DevilHunt1Get = 6310;
    /**
     * 封魔台中级抽奖获得:6311
     */
    public static int DevilHunt2Get = 6311;
    /**
     * 封魔台低级抽奖获得:6312
     */
    public static int DevilHunt3Get = 6312;
    /**
     * 除魔团副本开启扣除:6313
     */
    public static int DevilCopyMapCost = 6313;
    /**
     * 除魔团副本获得:6314
     */
    public static int DevilCopyMapGet = 6314;
    /**
     * 挚友等级提升获得:6401
     */
    public static int IntimateLevelUpGet = 6401;
    /**
     * 仙府送礼:6501
     */
    public static int HouseGiftCost = 6501;
    /**
     * 仙府商店购买:6502
     */
    public static int HouseShopCost = 6502;
    /**
     * 仙府商店获得:6503
     */
    public static int HouseShopGet = 6503;
    /**
     * 家装大赛获得:6504
     */
    public static int HouseMatchGet = 6504;
    /**
     * 任务获得:6505
     */
    public static int HouseTaskGet = 6505;
    /**
     * 投票获得:6506
     */
    public static int HouseVoteGet = 6506;
    /**
     * 房屋升级消耗:6507
     */
    public static int HouseLevelUpCost = 6507;
    /**
     * 房屋聚宝盆获得:6508
     */
    public static int HouseTupGet = 6508;
    /**
     * 幻装合成消耗:6601
     */
    public static int UnrealEquipCompoundDec = 6601;
    /**
     * 幻装合成获得:6602
     */
    public static int UnrealEquipCompoundGet = 6602;
    /**
     * 幻装镶嵌获得:6603
     */
    public static int UnrealInlayReasonGet = 6603;
    /**
     * 幻装拆解扣除消耗:6604
     */
    public static int UnrealEquipResolveDec = 6604;
    /**
     * 幻装拆解获得:6605
     */
    public static int UnrealEquipResolveGet = 6605;
    /**
     * 使用幻魂消耗:6606
     */
    public static int UseUnrealSoulDec = 6606;
    /**
     * 激活命星消耗:9901
     */
    public static int     ActiveFateStarDec = 9901;
    /**
     * 篝火升级消耗:50001
     */
    public static int WorldBonfireLevelDec = 50001;
    /**
     * 篝火猜拳领奖获得:50002
     */
    public static int WorldBonfireRewardGet = 50002;
    /**
     * 篝火经验获得:50003
     */
    public static int WorldBonfireExpGet = 50003;
    /**
     * 篝火获取经验获得:50004
     */
    public static int     BonfireExpGet = 50004;
    /**
     * 资源找回消耗:50101
     */
    public static int RetrieveResDec = 50101;
    /**
     * 资源完美找回奖励:50102
     */
    public static int RetrieveResAdd = 50102;
    /**
     * 资源部分找回奖励:50103
     */
    public static int RetrieveResPartAdd = 50103;
    /**
     * 太虚战场奖励:50201
     */
    public static int UniverseReward = 50201;
    /**
     * 联赛每月奖励获得:50301
     */
    public static int     LeagueMonthlyRewardGet = 50301;
    /**
     * 联赛每周奖励获得:50302
     */
    public static int     LeagueWeeklyRewardGet = 50302;
    /**
     * 世界答题获得:50401
     */
    public static int WorldAnswerGet = 50401;
    /**
     * 世界答题结束获得:50402
     */
    public static int WorldAnswerOverGet = 50402;
    /**
     * 位面奖励获得:50501
     */
    public static int PlaneRewardGet = 50501;
    /**
     * 藏宝阁抽奖获得:50601
     */
    public static int CangbaogeLotteryGet = 50601;
    /**
     * 藏宝阁兑换获得:50602
     */
    public static int CangbaogeExchangeGet = 50602;
    /**
     * 藏宝阁领奖获得:50603
     */
    public static int CangbaogeRewardGet = 50603;
    /**
     * 藏宝抽奖消耗:50604
     */
    public static int CangbaogeLotteryDel = 50604;
    /**
     * 藏宝兑换消耗:50605
     */
    public static int CangbaogeExchangeDel = 50605;
    /**
     *  跨服福地解锁消耗:50701
     */
    public static int CrossFudUnLockCost = 50701;
    /**
     * 跨服福地积分获得:50702
     */
    public static int CrossFudScoreBoxGain = 50702;
    /**
     * 跨服福地城市占领获得:50703
     */
    public static int CrossFudCityBoxGain = 50703;
    /**
     * 跨服福地Boss归属获得:50704
     */
    public static int CrossFudBossOwnGain = 50704;
    /**
     * 跨服魔王Boss归属获得:50705
     */
    public static int CrossDevilBossOwnGain = 50705;
    /**
     * 八级阵图城市占领奖励:50801
     */
    public static int EightCityParticipantReward = 50801;
    /**
     * 八级阵图积分奖励:50802
     */
    public static int EightCityIntegralReward = 50802;
    /**
     * 实名认证奖励:50901
     */
    public static int NameCertificationAward = 50901;
    /**
     * 使用激活码获得物品:51001
     */
    public static int ActiveCodeGetContentGain = 51001;
    /**
     * 混沌名人堂第一阶段结束:51101
     */
    public static int UniveresRankEnd = 51101;
    /**
     * 分享奖励:51201
     */
    public static int ShareRewardGain = 51201;
    /**
     *  福地论剑获得:51301
     */
    public static int GuildFudGain = 51301;
    /**
     * 更新有礼获得:51401
     */
    public static int     UpdateRewardGet = 51401;
    /**
     * 荣耀之战领奖获得:51501
     */
    public static int     BraveGloryRewardGet = 51501;
    /**
     * 充值元宝获得:51601
     */
    public static int     RechargeAddGoldGet = 51601;
    /**
     * 充值活动获得:51602
     */
    public static int     ActivityRechargeGet = 51602;
    /**
     * 充值赠送获得:51603
     */
    public static int     RechargeRewardGet = 51603;
    /**
     * 充值源代码:51605
     */
    public static int RechargeSourceCode = 51605;
    /**
     * 周末狂欢充值获得（BI统计使用）:51606
     */
    public static int CrazyWeekend = 51606;
    /**
     * 充值获得（BI统计使用）:51607
     */
    public static int Charge = 51607;
    /**
     * 充值新手礼包获得（BI统计使用）:51608
     */
    public static int ChargeNewPlayer = 51608;
    /**
     * 充值每日礼包获得（BI统计使用）:51609
     */
    public static int ChargeEverydayGift = 51609;
    /**
     * 充值每周礼包获得（BI统计使用）:51610
     */
    public static int ChargeEveryWeekFGift = 51610;
    /**
     * 充值超值折扣获得（BI统计用）:51611
     */
    public static int Chaozhizhekou = 51611;
    /**
     * 跨服领地战上一轮奖励获得:51701
     */
    public static int ManorLastRewardGet = 51701;
    /**
     * 首充赠送获得:51801
     */
    public static int     FirstRechargeGet = 51801;
    /**
     * 百元首充获得（BI统计使用）:51802
     */
    public static int HundredFirstRecharge = 51802;
    /**
     * 领取活跃度奖励获得:51901
     */
    public static int     ActiveRewardGet = 51901;
    /**
     * 活跃度每日清零:51903
     */
    public static int ActivePointDailyClear = 51903;
    /**
     * 活跃日常奖励获取:51904
     */
    public static int DailyTaskGet2 = 51904;
    /**
     * 活跃日常奖励消耗:51905
     */
    public static int DailyTaskDec2 = 51905;
    /**
     * 点击红包获得的金币值获得:52001
     */
    public static int RedPacketClickGet = 52001;
    /**
     * 提交红包扣除的货币值消耗:52002
     */
    public static int RedPacketSubmitDelDec = 52002;
    /**
     * 退还红包获得:52003
     */
    public static int RedPacketRebateGet = 52003;
    /**
     * 万人之上奖励获得:52201
     */
    public static int AllMenUpAwardGet = 52201;
    /**
     * 宗派玩法--福地日常奖励获得:52301
     */
    public static int GuildActivityDayRewardGet = 52301;
    /**
     * 宗派玩法--福地boss掉落获得:52302
     */
    public static int GuildActivityBossDropGet = 52302;
    /**
     * 宗派玩法--福地排名获得:52303
     */
    public static int GuildActivityRankGet = 52303;
    /**
     * 功能领奖获得:52401
     */
    public static int FunctionRewardGet = 52401;
    /**
     * 成长基金获得:52501
     */
    public static int WelfareGrowthFundGet = 52501;
    /**
     * 成长基金消耗:52502
     */
    public static int WelfareGrowthFundCost = 52502;
    /**
     * 开服狂欢获得:52601
     */
    public static int OpenServerAcGet = 52601;
    /**
     * 开服成长之路领取获得:52602
     */
    public static int OpenServerGrowPointGet = 52602;
    /**
     * 开服成长之路积分领取获得:52603
     */
    public static int OpenServerGrowPointRewardGet = 52603;
    /**
     * 开服成长之路购买获得:52604
     */
    public static int OpenServerGrowupPurGet = 52604;
    /**
     * 开服特殊活动:52605
     */
    public static int OpenServerSpecAc = 52605;
    /**
     * 开服特殊活动兑换:52606
     */
    public static int OpenServerSpecAcExchange = 52606;
    /**
     * 开服幸运翻牌活动获得:52607
     */
    public static int LuckyCardGet = 52607;
    /**
     * 新服优势奖励:52608
     */
    public static int NewServerAdvantage = 52608;
    /**
     * V4返利获得:52609
     */
    public static int V4RebateGet = 52609;
    /**
     * v4助力投资领取奖励:52610
     */
    public static int V4GetAwardGet = 52610;
    /**
     * v4助力被投资领取奖励:52611
     */
    public static int V4GetAward1Get = 52611;
    /**
     * v4助力消耗:52612
     */
    public static int V4HelpOtherCost = 52612;
    /**
     * 开服返利宝箱获得:52613
     */
    public static int RebateBoxGet = 52613;
    /**
     * 开服仙盟争霸获得:52614
     */
    public static int XMZBGet = 52614;
    /**
     * 神魔战场奖励获得:52701
     */
    public static int GodDevilRewardGet = 52701;
    /**
     * 有奖问卷获得:52801
     */
    public static int QuestionaireReward = 52801;
    /**
     * 开启特殊宝箱:52901
     */
    public static int OpenSpecialGift = 52901;
    /**
     * 开服活动预告奖励:52907
     */
    public static int OpenServerAcNoticeReward = 52907;
    /**
     * 修神断体奖励:53001
     */
    public static int VipRechargeReward = 53001;
    /**
     * 下载奖励:53101
     */
    public static int DownloadReward = 53101;
    /**
     * 0元购:53201
     */
    public static int FreeShopReward = 53201;
    /**
     * 购买人数上限消耗:53202
     */
    public static int PurInviteNum = 53202;
    /**
     * 0元购:53203
     */
    public static int FreeShopCost = 53203;
    /**
     * 新零元购:53204
     */
    public static int NewFreeShowCost = 53204;
    /**
     * 新零元购:53205
     */
    public static int NewFreeShopReward = 53205;
    /**
     * 元宝超值折扣:53206
     */
    public static int GoldDiscountCost = 53206;
    /**
     * 元宝超值折扣:53207
     */
    public static int GoldDiscountReward = 53207;
    /**
     * 免费超值折扣:53208
     */
    public static int FreeDiscountReward = 53208;
    /**
     * 免费元宝超值折扣:53209
     */
    public static int FreeGoldDiscountReward = 53209;
    /**
     * 更新公告奖励的领取:53301
     */
    public static int UpdateNoticeAwardGet = 53301;
    /**
     * 每日充值获得:53401
     */
    public static int DailyRechargeget = 53401;
    /**
     * 每日消耗获得:53402
     */
    public static int DailyConsumeget = 53402;
    /**
     * 首领活动购买消耗:53501
     */
    public static int HolidayBossShopCost = 53501;
    /**
     * 节日许愿活动临时仓库提取:53601
     */
    public static int FestvialWishExtract = 53601;
    /**
     * 天禁令获得:53701
     */
    public static int FallingSkyGet = 53701;
    /**
     * 天禁令扣除:53702
     */
    public static int FallingSkyDec = 53702;
    /**
     * 天禁令奖励:53703
     */
    public static int FallingSkyRewardGain = 53703;
    /**
     * 天禁令任务奖励获得:53704
     */
    public static int FallingSkyTaskRewardGain = 53704;
    /**
     * 天禁令等级奖励获得:53705
     */
    public static int FallingSkyLevelRewardGain = 53705;
    /**
     * 等级升级获得:53801
     */
    public static int LevelChangeGet = 53801;
    /**
     * 限时活动排行榜领取获得【修仙宝鉴】:53901
     */
    public static int ActivityRankGet = 53901;
    /**
     * 全服狂欢排行奖励:54001
     */
    public static int SeverCrazyRankRewardGain = 54001;
    /**
     * 全服狂欢个人奖励:54002
     */
    public static int SeverCrazyPersonalReward = 54002;
    /**
     * 天虚战场获得:54101
     */
    public static int UniverseGet = 54101;
    /**
     * 巅峰基金获得:54201
     */
    public static int InvestPeakGet = 54201;
    /**
     * 巅峰基金消耗:54202
     */
    public static int InvestPeakCost = 54202;
    /**
     * 仙侣对决海选赛战斗获得:54301
     */
    public static int CoupleFightTrialsFightGet = 54301;
    /**
     * 仙侣对决小组赛战斗获得:54302
     */
    public static int CoupleFightGroupsFightGet = 54302;
    /**
     * 仙侣对决冠军赛地榜战斗获得:54303
     */
    public static int CoupleFightDiFightGet = 54303;
    /**
     * 仙侣对决冠军赛天榜战斗获得:54304
     */
    public static int CoupleFightTianFightGet = 54304;
    /**
     * 仙侣对决小组赛排名获得:54305
     */
    public static int CoupleFightGroupsRankGet = 54305;
    /**
     * 仙侣对决冠军赛地榜排名获得:54306
     */
    public static int CoupleFightDiRankGet = 54306;
    /**
     * 仙侣对决冠军赛天榜排名获得:54307
     */
    public static int CoupleFightTianRankGet = 54307;
    /**
     * 仙侣对决竞猜消耗:54308
     */
    public static int CoupleFightGuessCost = 54308;
    /**
     * 仙侣对决竞猜获得:54309
     */
    public static int CoupleFightGuessGet = 54309;
    /**
     * 仙侣对决海选赛奖励获得:54310
     */
    public static int CoupleFightTrialsAwardGet = 54310;
    /**
     * 仙女护送扣除:54311
     */
    public static int CoupleEscortCost = 54311;
    /**
     * 仙女护送获得:54312
     */
    public static int CoupleEscortGet = 54312;
    /**
     * 仙女护送获得:54313
     */
    public static int CoupleShopGet = 54313;
    /**
     * 仙女护送扣除:54314
     */
    public static int CoupleShopCost = 54314;
    /**
     * 无忧宝库寻宝消耗:54401
     */
    public static int WuyouHuntCost = 54401;
    /**
     * 无忧宝库寻宝获得:54402
     */
    public static int WuyouHuntGet = 54402;
    /**
     * 无忧宝库免费获得:54403
     */
    public static int WuyouHuntFreeGet = 54403;
    /**
     * 无忧宝库提取获得:54404
     */
    public static int WuyouHuntExtractGet = 54404;
    /**
     * 功能任务获得:54501
     */
    public static int FunctionTaskGet = 54501;
    /**
     * 功能任务充值获得:54502
     */
    public static int FunctionTaskRechargeGet = 54502;
    /**
     * 混沌虚空宝库获得:54601
     */
    public static int AlienGemGet = 54601;
    /**
     * 充值返利获得:54701
     */
    public static int RechargeRebateGet = 54701;
    /**
     * 化形消耗:990075
     */
    public static int     HuaxingDec = 990075;
    /**
     * 活跃兑换获得:100100000
     */
    public static int GetActiveActivityGet = 100100000;
    /**
     * 活跃币的获得:100100001
     */
    public static int GetActiveCoinActiveActivity = 100100001;
    /**
     * 我要变强的每日充值专属礼包获得:100200001
     */
    public static int     DailyRechargeRewardGet = 100200001;
    /**
     * 运营活动限时登陆获得:100300001
     */
    public static int LimitTimeLoginActivityGet = 100300001;
    /**
     * 运营活动限时登陆高级获得:100300002
     */
    public static int LimitTimeLoginActivityHighGradeGet = 100300002;
    /**
     * 限购礼包获得:100400001
     */
    public static int LimitGiftBagAcitvityGet = 100400001;
    /**
     * 限购礼包消耗:100400002
     */
    public static int LimitGiftBagAcitvityDec = 100400002;
    /**
     * 天帝宝库普通:100500000
     */
    public static int DailyDraw = 100500000;
    /**
     * 天帝宝库轮次奖励活动获得:100500002
     */
    public static int DailyDrawRollGet = 100500002;
    /**
     * 天帝宝库进度奖励获得:100500003
     */
    public static int DailyDrawPrcGet = 100500003;
    /**
     * 天帝宝库扣除:100500004
     */
    public static int DailyDrawCost = 100500004;
    /**
     * 天帝宝库翻卡牌:100500005
     */
    public static int DailyDrawOpenCard = 100500005;
    /**
     * 天帝宝库翻卡牌获得:100500006
     */
    public static int DailyDrawOpenCardGet = 100500006;
    /**
     * 天帝宝库翻卡牌扣除:100500007
     */
    public static int DailyDrawOpenCardDec = 100500007;
    /**
     * 运营活动限时累充获得:100600000
     */
    public static int RechargeTotalActivityGet = 100600000;
    /**
     * 运营活动限时消耗获得:100700001
     */
    public static int TotalConsumeActivityGet = 100700001;
    /**
     * 集物兑换获得:100800001
     */
    public static int CollectGoodsExchangeGet = 100800001;
    /**
     * 集物兑换扣除:100800002
     */
    public static int CollectGoodsExchangeDel = 100800002;
    /**
     * 运营活动团购活动获得:100900001
     */
    public static int GroupBuyActivityGet = 100900001;
    /**
     * 运营活动团购活动消耗:100900002
     */
    public static int GroupBuyActivityDec = 100900002;
    /**
     * 运营活动团购活动返还获得:100900003
     */
    public static int GroupBuyActivityReturnGet = 100900003;
    /**
     * 运营活动招财猫获得:101000001
     */
    public static int ActivityLuckyCatGet = 101000001;
    /**
     * 运营活动招财猫消耗:101000002
     */
    public static int ActivityLuckyCatDec = 101000002;
    /**
     * 运营活动幸运宝玉获得:101000003
     */
    public static int ActivityLuckyCat2Get = 101000003;
    /**
     * 运营活动幸运宝玉消耗:101000004
     */
    public static int ActivityLuckyCat2Dec = 101000004;
    /**
     *  庆典任务获得:101200001
     */
    public static int HolidayActivityTaskGet = 101200001;
    /**
     *  集字活动扣除:101300001
     */
    public static int HolidayActivityWordsCost = 101300001;
    /**
     *  集字活动兑换:101300002
     */
    public static int HolidayActivityWordsGet = 101300002;
    /**
     * 节日特惠活动获得:101400000
     */
    public static int FestivalPreferenceGet = 101400000;
    /**
     * 连续充值获得:101500000
     */
    public static int ContinuousRechargeGet = 101500000;
    /**
     * 每日充值活动获得:101500200
     */
    public static int DailyRechargeAcitvityGet = 101500200;
    /**
     * 每日充值累计奖励获得:101500201
     */
    public static int DailyRechargeAcitvityTotalGet = 101500201;
    /**
     * 限购商城获得:101600001
     */
    public static int LimitShopActivetyGet = 101600001;
    /**
     * 限购商城扣除消耗:101600002
     */
    public static int LimitShopActivetyDec = 101600002;
    /**
     * 限时礼包活动购买消耗:101700001
     */
    public static int LimitTimeGiftActivityBuyDec = 101700001;
    /**
     * 限时礼包活动购买获得:101700002
     */
    public static int LimitTimeGiftActivityGet = 101700002;
    /**
     *  庆典积分获得:101800001
     */
    public static int HolidayActivityScoreRankReachGet = 101800001;
    /**
     * 节日许愿活动消耗:101900000
     */
    public static int FestvialWishCost = 101900000;
    /**
     * 节日许愿活动获得:101900001
     */
    public static int FestvialWishGet = 101900001;
    /**
     * 节日许愿积分奖励获得:101900002
     */
    public static int FestvialWishScoreAwardGet = 101900002;
    /**
     * 圣诞分享活动获得:102000001
     */
    public static int FBShareChristmasActivityGet = 102000001;
    /**
     * 元旦分享活动获得:102000002
     */
    public static int FBShareNewYearActivityGet = 102000002;
    /**
     * 连续充值2充值获得:102100000
     */
    public static int ContinuousRechargeGet2 = 102100000;
    /**
     * 连续充值2累计天数获得:102100001
     */
    public static int ContinuousRechargeDaysGet2 = 102100001;
    /**
     * 新年祝福活动签到获得:102200001
     */
    public static int NewYearWishSignGet = 102200001;
    /**
     * 新年祝福活动补签消耗:102200002
     */
    public static int NewYearWishSignCost = 102200002;
    /**
     * 掷骰子:102300000
     */
    public static int DiceRewardGain = 102300000;
    /**
     * 掷骰子通关次数奖励:102300001
     */
    public static int DicePlayerTimesGet = 102300001;
    /**
     * 掷骰子跳格子奖励:102300002
     */
    public static int DiceGridGet = 102300002;
    /**
     * 掷骰子通关奖励:102300003
     */
    public static int DiceCrossGet = 102300003;
    /**
     * 掷骰子消耗:102300004
     */
    public static int DiceCost = 102300004;
    /**
     * 聚宝盆抽奖获得:102600000
     */
    public static int CornucopiaGet = 102600000;
    /**
     * 聚宝盆抽奖消耗:102600001
     */
    public static int CornucopiaCost = 102600001;
    /**
     * 聚宝盆元宝池抽奖获得:102600002
     */
    public static int CornucopiaGoldGet = 102600002;
    /**
     * 聚宝盆次数奖励获得:102600003
     */
    public static int CornucopiaCountGet = 102600003;
    /**
     * 聚宝盆活跃奖励获得:102600004
     */
    public static int CornucopiaActiveGet = 102600004;
    /**
     * 砸金蛋抽奖获得:102700000
     */
    public static int SmashEggGet = 102700000;
    /**
     * 砸金蛋抽奖消耗:102700001
     */
    public static int SmashEggCost = 102700001;
    /**
     * 砸金蛋刷新消耗:102700002
     */
    public static int SmashEggRefreshCost = 102700002;
    /**
     * 砸金蛋次数奖励获得:102700003
     */
    public static int SmashEggCountGet = 102700003;
    /**
     * 砸金蛋在线时长奖励获得:102700004
     */
    public static int SmashEggOnlineGet = 102700004;
    /**
     * 方泽探宝抽奖获得:102900001
     */
    public static int FZTBActivityGet = 102900001;
    /**
     * 方泽探宝抽奖消耗:102900002
     */
    public static int FZTBActivityDec = 102900002;
    /**
     * 方泽探宝抽9次奖励获得:102900003
     */
    public static int FZTBActivityDraw9Get = 102900003;
    /**
     * 方泽探宝重置抽奖消耗:102900004
     */
    public static int FZTBActivityResetDrawMapDec = 102900004;
    /**
     * :"移动,拆分,合并,出库,入库,背包整理,仓库整理,仓库移动,手动删除消耗,开启背包格子消耗,开启仓库格子消耗,达成成就获得,好友物品培养增加亲密度消耗,背包到服务器仓库消耗,挚友改名消耗,玩家赠送,玩家领取获得情义点奖励,玩家赠送获得情义点奖励,创建帮会消耗,帮会改名消耗,弹劾会长消耗,帮会捐献消耗,玩家学习公会技能消耗,参加公会物品竞拍获得,背包到公会仓库消耗,公会退出消耗,加入公会消耗,公会每日领取道具获得,帮会日常、周常一键完成单个任务消耗,公会建筑升级消耗,公会维修基金消耗,公会领取工资,仙盟任务刷新消耗,仙盟战点赞,仙盟boss奖励,公会日常奖励获取,公会周常奖励获取,仙盟日常奖励获取,公会日常奖励消耗,公会周常奖励消耗,仙盟日常奖励消耗,仙盟战个人奖励,仙盟boss鼓舞获得,仙盟boss鼓舞消耗,仙盟宝箱获得,翅膀升级消耗,坐骑进阶消耗,坐骑化形消耗,坐骑吃药消耗,坐骑装备穿戴消耗,坐骑装备穿戴获得,坐骑装备强化消耗,坐骑装备附魂消耗,坐骑装备升级消耗,坐骑装备升级获得,坐骑装备分解消耗,坐骑装备分解获得,坐骑装备激活消耗,坐骑装备自动分解获得, 魂甲抽奖获得, 魂甲抽奖消耗, 魂甲.魂印分解获得, 魂甲.魂印分解消耗, 魂甲.魂印卸下获得, 魂甲淬炼消耗, 魂甲突破消耗, 魂甲觉醒消耗, 魂甲觉醒技能升级消耗, 魂甲魂印孔位升级消耗, 魂甲魂印穿戴消耗, 魂甲.魂印合成消耗,魂甲.魂印合成获得, 魂甲双倍抽奖获得,仙甲兑换获得,仙甲兑换消耗,仙甲分解获得,仙甲分解消耗,仙甲穿戴镶嵌,仙甲合成,仙甲寻宝消耗,仙魄合成系统获得,仙魄合成系消耗,仙魄升级系统消耗,仙魄分解获得,仙魄分解消耗,仙魄兑换获得,仙魄分解消耗,仙魄拆解获得,仙魄拆解消耗,神兵突破消耗,神兵化形消耗,神兵升级消耗,神兵磨具激活消耗,神兵升品消耗,神兵日常奖励获取,神兵日常奖励消耗,宠物激活消耗,宠物升阶消耗,宠物御魂消耗,宠物吞噬装备消耗,替换宠物装备获得,熔炼宠物装备扣除,强化宠物装备扣除,附魂宠物装备扣除,合成宠物装备扣除,主动分解宠物装备扣除,主动分解宠物装备获得,自动分解宠物装备扣除,自动分解宠物装备获得,激活宠物装备槽,穿戴宠物装备旧装备背包获得,穿戴宠物装备背包扣除,合成宠物装备获得,阵法升级消耗,阵法化形消耗,阵法吃药消耗,阵法附灵消耗,市集求购消耗,市集求购商品下架获得,市集交易下架获得,市集求购售卖消耗,市集求购获得,交易消耗,交易获得,交易卸下获得,交易取消获得,交易失败获得,交易异常获得,拍卖行合成购买获得,竞拍失败退回获得,拍卖成功获得,出售成功获得,无人购买退回物品获得,拍卖行下架获得,拍卖行合成购买消耗,购买市集的物品消耗,贩卖市集的物品获得,市集中上架物品消耗,交易失败货币返回获得,集市邮件的附件领取获得,拍卖行竞价消耗,拍卖行上架消耗,拍卖行一口价消耗,神器升级消耗,神器化形消耗,神器吃药消耗,穿上装备消耗,卸下装备获得,分解装备消耗,合成消耗,装备出售消耗,装备神炼消耗,装备合成消耗,装备合成消耗,装备套装消耗,装备套装石合成消耗,合成的装备拆解获得,转换职业时删除装备消耗,合成新物品,装备部位强化消耗,宝石镶嵌or卸下,仙玉镶嵌or卸下,宝石精炼消耗,洗练消耗,洗髓消耗,合成获得,回收炉删除装备获得,回收炉删除装备消耗,神品装备升星消耗,神品装备升星获得,神品装备升阶消耗,神品装备升阶获得,装备拆解消耗,装备拆解获得,手动使用物品消耗,卖出物品消耗,图鉴吞噬消耗,开礼包获得,角色改名卡道具消耗,使用道具增加魅力消耗,吃药消耗,经验丹经验获得,消费元宝卡元宝值获得,使用圣魂消耗,商城元宝消费获得,商城元宝消费消耗,商城消费获得,商城消费消耗,神秘限购商品,神秘限购商品获得,神秘限购商品消耗,神秘商店,神秘商店获得,神秘商店消耗,任务完成奖励获得,任务完成奖励消耗,日常任务扣除消耗,任务加倍完成消耗,主线任务奖励获得,直线任务奖励获得,任务提交道具消耗,任务收集道具消耗,领取护送任务消耗,日常任务一键完成单个任务消耗,领取任务目标奖励消耗,任务目标奖励获得,完成日常次数获得,圣装合成获得,圣装分解获得,圣装合成消耗,圣装镶嵌获得,圣装分解扣除消耗,结婚消耗,强制离婚消耗,参加婚宴送礼金消耗,仙缘心锁升级消耗,婚姻仙居突破消耗,仙缘.情缘副本获得,结婚--仙娃激活消耗,结婚--仙娃升级消耗,婚姻领取每日宝匣奖励获得,婚姻领取宝匣返利奖励获得,婚姻购买宝匣消耗,婚姻系统--对诗获得,婚姻系统--祈福收取果实获得,婚姻系统--领取亲密度奖励获得,婚姻系统--满足条件称号获得,婚宴操作--购买喜糖消耗,婚宴操作--购买礼炮消耗,婚宴操作--购买烟花消耗,婚姻--三倍领取祈福果实获得,婚姻-仙娃改名消耗,结婚消耗,结婚宣言消耗,强制离婚消耗,婚礼赠送消耗,婚礼购买消耗,婚礼使用消耗,婚礼购买获得, 仙缘任务获得,仙缘奖励获得,婚宴获得,仙缘-缘定三生,仙缘-缘定三生消耗,仙缘-缘定三生获得,发布爱情宣言获得,发布爱情宣言消耗,普通结婚消耗,高级结婚消耗,豪华结婚消耗,普通结婚获得,高级结婚获得,豪华结婚获得,完美情缘排名奖励获得,完美情缘商店购买获得,完美情缘任务奖励获得,婚姻副本购买热度消耗,完美情缘任务奖励获得,情缘福袋回收,情缘福袋回收, 婚礼祝福获得,激活称号消耗,邮件附件领取获得,邮件发送使用,灵魄寻宝购买扣除,灵魄寻宝购买赠送获得,灵魄寻宝抽奖扣除,灵魄寻宝抽奖获得,寻宝回收道具获得,寻宝回收道具扣除,剑灵阁领取收益,剑灵阁快速收益获得,vip周奖励礼包奖励获得,vip周奖励自动领取礼包奖励获得,vip充值奖励获得,VIP经验道具获得,Vip目标奖励获得,充值vip经验获得,在线送vip经验获得, VIP经验版本修正获得,购买vip礼包奖励,购买vip礼包奖励获得,购买vip礼包奖励消耗,vip升级获得,vip珠宝使用获得,魂兽增加额外格子消耗,魂兽售卖道具和装备获得,魂兽装备强化消耗,魂兽装备合成消耗,魂兽装备合成获得,魂兽售卖道具消耗,竞技场每日奖励获得,首席竞技场获得,竞技场首次达到排名奖励获得,竞技场购买次数消耗,巅峰竞技场段位奖励获得,巅峰竞技场场次奖励获得,巅峰竞技场挑战奖励获得,竞技场排行奖励,福利：每日签到消耗,福利：每日签到,福利：月卡尊享卡,福利：月卡尊享卡获得,福利：月卡尊享卡消耗,福利：感悟经验获得,福利：感悟经验消耗,福利：累计签到获得,福利：每日签到获得,福利：感悟经验,等级礼包获得奖励,vip每日礼包奖励,福利：感悟银币获得,福利：感悟银币消耗,福利：周卡,福利：周卡获得,成长基金,福利：登陆礼包,福利：登陆礼包获得,周福利幸运抽奖消耗,周福利幸运抽奖获得,等级礼包vip额外获得奖励,免费礼包 获得奖励,转职消耗,转职任务奖励获得,转职任务一键完成的时候扣除消耗,转职任务一键完成,转职阶段完成获得,崇拜排行榜玩家奖励获得,掉落获得,复活消耗,小地图传送消耗,Pk消耗,杀怪掉落获得,离线挂机获得,在线打坐获得,地图经验获得,职业掉落获得,采集掉落获得,首杀红包奖励,服务器首杀奖励,共享掉落,神兽岛采集获得,恭喜获得服务器首杀奖励,挂机经验找回获得,挂机经验找回扣除,通关副本获得,鼓舞消耗,掉落获得,扫荡副本道具消耗,星级副本星数奖励获得,星级副本扫荡消耗,进入星级副本消耗,vip副本次数购买消耗,经验副本获得,婚礼副本经验获得, 集字活动副本掉落,副本合并消耗,仙缘.情缘副本,boss之家进入消耗,击杀Boss活动获得,个人boss消耗,boss击杀归属奖励获得,boss次数特殊掉落获得,boss排名掉落获得,boss阳光普照奖励获得,购买boss排名奖励次数消耗,套装boss进入消耗,宝石boss进入消耗,首杀boss个人击杀奖励获得,首领活动掉落获得,首领活动礼包开启掉落获得,首领活动购买获得,荒古神坛活动掉落,重置经脉获得,重置经脉扣除消耗,激活经脉扣除消耗,感谢支援玩家时被支援者的奖励获得,感谢支援玩家时支援者的奖励获得,BOSS死亡时被支援者的奖励获得,BOSS死亡时支援者的奖励获得,感谢支援玩家时扣除道具消耗,GM后台移除物品消耗,后台功能扣除元宝值消耗,gm获得,gm扣除,gm获得,GM来增加元宝获得,后台过来增加元宝获得,识海升级消耗,境界任务领奖获得,境界突破奖励获得,境界礼包购买获得,境界经验掉落获得,境界副本挑战获取,境界副本扫荡提取,升级境界灵压消耗,购买境界boss进入次数消耗,蜕变消耗,心法升级消耗,掌门传道经验获得,掌门传道道具奖励获得,掌门传道扣除,寻宝消耗,寻宝获得,寻宝提取获得,寻宝处购买消耗,寻宝处购买获得,寻宝仓库一键提取获得,秘宝获得,仙甲寻宝获得,仙甲寻宝奖励获得,聚宝盆领取获得,聚宝盆奖池领取获得,灵压法宝升级消耗,法宝器灵激活升级消耗,法宝日常奖励获取,时装升星消耗,时装激活消耗,时装发型该表消耗,激活神兽消耗,技能等级升级消耗,心法被动技能升级消耗,新技能格子升级,新技能升星,升级血脉系统消耗,灵体放入或替换装备获得,灵体放入或替换装备消耗,灵体解封消耗,服务器改名消耗,新服活动奖励,平台评价点赞奖励获得,平台评价分享奖励获得,平台评价每日分享奖励获得,商店评价引导奖励获得,犒赏令-荒古令获得,犒赏令-荒古令消耗,犒赏令-荒古令购买高级消耗,魔魂升级消耗,魔魂升阶消耗,魔魂突破消耗,魔魂解锁消耗,魔魂装备合成消耗,魔魂装备合成获得,魔魂装备穿戴获得,魔魂装备穿戴消耗,封魔台高级抽奖获得,封魔台中级抽奖获得,封魔台低级抽奖获得,除魔团副本开启扣除,除魔团副本获得,挚友等级提升获得,仙府送礼,仙府商店购买,仙府商店获得,家装大赛获得,任务获得,投票获得,房屋升级消耗,房屋聚宝盆获得,幻装合成消耗,幻装合成获得,幻装镶嵌获得,幻装拆解扣除消耗,幻装拆解获得,使用幻魂消耗,激活命星消耗,篝火升级消耗,篝火猜拳领奖获得,篝火经验获得,篝火获取经验获得,资源找回消耗,资源完美找回奖励,资源部分找回奖励,太虚战场奖励,联赛每月奖励获得,联赛每周奖励获得,世界答题获得,世界答题结束获得,位面奖励获得,藏宝阁抽奖获得,藏宝阁兑换获得,藏宝阁领奖获得,藏宝抽奖消耗,藏宝兑换消耗, 跨服福地解锁消耗,跨服福地积分获得,跨服福地城市占领获得,跨服福地Boss归属获得,跨服魔王Boss归属获得,八级阵图城市占领奖励,八级阵图积分奖励,实名认证奖励,使用激活码获得物品,混沌名人堂第一阶段结束,分享奖励, 福地论剑获得,更新有礼获得,荣耀之战领奖获得,充值元宝获得,充值活动获得,充值赠送获得,充值源代码,周末狂欢充值获得（BI统计使用）,充值获得（BI统计使用）,充值新手礼包获得（BI统计使用）,充值每日礼包获得（BI统计使用）,充值每周礼包获得（BI统计使用）,充值超值折扣获得（BI统计用）,跨服领地战上一轮奖励获得,首充赠送获得,百元首充获得（BI统计使用）,领取活跃度奖励获得,活跃度每日清零,活跃日常奖励获取,活跃日常奖励消耗,点击红包获得的金币值获得,提交红包扣除的货币值消耗,退还红包获得,万人之上奖励获得,宗派玩法--福地日常奖励获得,宗派玩法--福地boss掉落获得,宗派玩法--福地排名获得,功能领奖获得,成长基金获得,成长基金消耗,开服狂欢获得,开服成长之路领取获得,开服成长之路积分领取获得,开服成长之路购买获得,开服特殊活动,开服特殊活动兑换,开服幸运翻牌活动获得,新服优势奖励,V4返利获得,v4助力投资领取奖励,v4助力被投资领取奖励,v4助力消耗,开服返利宝箱获得,开服仙盟争霸获得,神魔战场奖励获得,有奖问卷获得,开启特殊宝箱,开服活动预告奖励,修神断体奖励,下载奖励,0元购,购买人数上限消耗,0元购,新零元购,新零元购,元宝超值折扣,元宝超值折扣,免费超值折扣,免费元宝超值折扣,更新公告奖励的领取,每日充值获得,每日消耗获得,首领活动购买消耗,节日许愿活动临时仓库提取,天禁令获得,天禁令扣除,天禁令奖励,天禁令任务奖励获得,天禁令等级奖励获得,等级升级获得,限时活动排行榜领取获得【修仙宝鉴】,全服狂欢排行奖励,全服狂欢个人奖励,天虚战场获得,巅峰基金获得,巅峰基金消耗,仙侣对决海选赛战斗获得,仙侣对决小组赛战斗获得,仙侣对决冠军赛地榜战斗获得,仙侣对决冠军赛天榜战斗获得,仙侣对决小组赛排名获得,仙侣对决冠军赛地榜排名获得,仙侣对决冠军赛天榜排名获得,仙侣对决竞猜消耗,仙侣对决竞猜获得,仙侣对决海选赛奖励获得,仙女护送扣除,仙女护送获得,仙女护送获得,仙女护送扣除,无忧宝库寻宝消耗,无忧宝库寻宝获得,无忧宝库免费获得,无忧宝库提取获得,功能任务获得,功能任务充值获得,混沌虚空宝库获得,充值返利获得,化形消耗,活跃兑换获得,活跃币的获得,我要变强的每日充值专属礼包获得,运营活动限时登陆获得,运营活动限时登陆高级获得,限购礼包获得,限购礼包消耗,天帝宝库普通,天帝宝库轮次奖励活动获得,天帝宝库进度奖励获得,天帝宝库扣除,天帝宝库翻卡牌,天帝宝库翻卡牌获得,天帝宝库翻卡牌扣除,运营活动限时累充获得,运营活动限时消耗获得,集物兑换获得,集物兑换扣除,运营活动团购活动获得,运营活动团购活动消耗,运营活动团购活动返还获得,运营活动招财猫获得,运营活动招财猫消耗,运营活动幸运宝玉获得,运营活动幸运宝玉消耗, 庆典任务获得, 集字活动扣除, 集字活动兑换,节日特惠活动获得,连续充值获得,每日充值活动获得,每日充值累计奖励获得,限购商城获得,限购商城扣除消耗,限时礼包活动购买消耗,限时礼包活动购买获得, 庆典积分获得,节日许愿活动消耗,节日许愿活动获得,节日许愿积分奖励获得,圣诞分享活动获得,元旦分享活动获得,连续充值2充值获得,连续充值2累计天数获得,新年祝福活动签到获得,新年祝福活动补签消耗,掷骰子,掷骰子通关次数奖励,掷骰子跳格子奖励,掷骰子通关奖励,掷骰子消耗,聚宝盆抽奖获得,聚宝盆抽奖消耗,聚宝盆元宝池抽奖获得,聚宝盆次数奖励获得,聚宝盆活跃奖励获得,砸金蛋抽奖获得,砸金蛋抽奖消耗,砸金蛋刷新消耗,砸金蛋次数奖励获得,砸金蛋在线时长奖励获得,方泽探宝抽奖获得,方泽探宝抽奖消耗,方泽探宝抽9次奖励获得,方泽探宝重置抽奖消耗"
     */
    public static ReadStringArray AllDescString;

}
