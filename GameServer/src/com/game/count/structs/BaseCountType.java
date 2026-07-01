package com.game.count.structs;

/**
 * @author admin
 */
public enum BaseCountType {

    /**
     * 玩家变量
     */
    Variant("0"),
    /**
     * 物品使用次数
     */
    ITEM_USE("1"),

    /**
     * 物品使用次数
     */
    ITEM_AllUSE("2"),
    /**
     * 巅峰竞技段位礼包领取次数
     */
    PeakWeekStageBox("3"),
    /**
     * 神秘商人日兑换次数
     */
    MysBu_Shop_Day_AlreadyBuy("4"),
    /**
     * 定时刷新物品的收益次数
     */
    Timing_Refresh_Earning("5"),
    /**
     * 活动刷新
     */
    Activity("6"),
    /**
     * 个人掉落
     */
    DROP_ITEM_LIMIT("7"),
    /**
     * 在魂兽森林的每日水晶采集
     */
    SOULANIMALFORESTGATHERNUM("8"),
    /**
     * 新手无限层副本#TODO 废弃
     */
    COPY_MAP_NoobBoss("9"),
    /**
     * 副本每日扫荡次数
     */
    COPY_MAP_SWEEP_TIMES("10"),
    /**
     * 各种事件次数
     */
    Event_TIMES("11"),
    /**
     * 击杀boss次数
     */
    KillBoss_Times("12"),
    /**
     * 副本合并次数 预设置
     */
    COPY_Merge_Count("14"),
    /**
     * 每日感悟经验
     */
    Feeling_Exp("15"),
    /**
     * 成长基金
     */
    GrowthFund("16"),
    /**
     * 每日礼包
     */
    DayGift("17"),
    /**
     * 单个玩家寻宝奖池单个物品产生次数
     */
    PTreasureHuntItem("18"),
    /**
     * 服务器单个物品出现的次数
     */
    STreasureHuntItem("19"),

    /**
     * 玩家各种类型单独记录总的寻宝次数
     */
    PTreasureTypeHuntTimes("20"),

    /**
     * 玩家免费寻宝使用次数
     */
    PTreasureHuntFreeUseTimes("21"),
    /**
     * 玩家等级邮件
     */
    PlayerLevelMail("22"),
    /**
     * 全服有多少对夫妻
     */
    MarryServerNumber("23"),

    /**
     * 开服特殊活动全服领取次数
     */
    OpenServerSpecGetCount("24"),
    /**
     * 商城
     */
    SHOP_COUNT("25"),
    /**
     * 开服活动每日兑换
     */
    OpenServerDailyExchange("26"),

    /**
     * 开服活动宗派领奖限制(按照位来处理)
     */
    OpenServerGuildReward("27"),

    /**
     * 玩家各种类型今日寻宝次数
     */
    PTodayTreasureTypeHuntTimes("28"),
    /**
     * 完成X个当前境界的任务
     */
    StateTask("29"),
    /**
     * 合成X件X阶及以上的X色X星装备
     */
    SyntheticEquipStrong("30"),
    /**
     * 充值计数
     */
    Recharge("31"),
    /**
     * 某地图击杀的boss数量
     */
    MapKillMonsterNum("32"),

    /**
     * 超值折扣
     */
    Direct_shop("33"),

    /**
     * 领地战怒气值
     */
    ManorWarAnger("34"),
    /**
     * 挚友召唤
     */
    ChumCall("35"),

    /**
     * 击杀子类型boss次数
     */
    KillSpecBoss_Times("36"),
    /**
     * 购买X物品X次数
     */
    PurShopItem_Times("37"),
    /**
     * 掌门传道领取道具奖励次数
     */
    LeaderPreach("38"),
    /**
     * 世界支援奖励计数
     */
    WorldHelp("39"),
    /**
     * 福利卡次数统计
     */
    WelfareCard("40"),
    /**
     * 福利卡每日礼包次数
     */
    WelfareDailyGiftNum("41"),
    /**
     * 太虚战场怒气值
     */
    UniverseAnger("42"),
    /**
     * 玩家各种类型累计寻宝次数
     */
    PTotalTreasureTypeHuntTimes("43"),
    /**
     * TODO 废弃
     * 服务器心跳 零点刷新
     */
    ServerClock("44"),
    /**
     * 声望商城购买道具数量
     */
    IntegralShopBuyItem("45"),
    /**
     * 拍卖行上架X阶X品质X部位装备次数
     */
    AuctionPut("46"),
    /**
     * 拍卖行购买X阶X品质X部位装备次数
     */
    AuctionBuy("47"),
    /**
     * 购买成长基金X档Y次
     */
    GrowthFundBuy("48"),
    /**
     * 请求一键领取邮件操作X次
     */
    OneKeyMailRecv("49"),
    /**
     * 组队完成组队副本
     */
    GroupCopyMap("50"),
    /**
     * 完成仙盟S级任务协助X次
     */
    GuildTaskMapSupport("51"),
    /**
     * 累计完成X次S级仙盟任务
     */
    GuildTaskTotal("52"),

    /**
     * 灵魄寻宝单次计数
     */
    TreasureSoulOneTimeCount("53"),
    /**
     * 灵魄寻宝10次计数
     */
    TreasureSoulTenTimesCount("54"),

    /**
     * 灵魄寻宝历史次数统计
     */
    TreasureSoulTotalCount("55"),

    /**
     * 完成x档婚礼
     */
    FinishWedding("56"),
    /**
     * 荣誉商城购买道具数量
     */
    GloryShopBuyItem("57"),
    /**
     * 宠物装备分解X次
     */
    Pet_Equip_Resolve("58"),
    /**
     * 玩家仙甲寻宝幸运值
     */
    PTreasureHuntLuckyvalue("59"),
    /**
     * 坐骑装备分解X次
     */
    Horse_Equip_Resolve("60"),
    /**
     * 犒赏令当天获得值
     */
    KaoShangLingScore_Horse_Day("61"),
    /**
     * 犒赏令 一轮总的值
     */
    KaoShangLingScore_Horse_Total("62"),
    /**
     * 巅峰基金
     */
    InvestPeak("63"),

    /**
     * 好友情义点赠送奖励次数
     */
    FriendShipPointGiveRewardCount("64"),

    /**
     * 好友情义点接受奖励次数
     */
    FriendShipPointReceiveRewardCount("65"),
    /**
     * 每天获得的情义点
     */
    DayFriendShipPoint("66"),

    /**
     * 是否赠送给npc情义点
     */
    NpcFriendGiveShipPoint("67"),

    /**
     * 是否接受npc 赠送的情义点
     */
    NpcFriendReceiveShipPoint("68"),

    /**
     * 是否接受npc 赠送的情义点
     */
    NpcFriendReceiveShipPointReward("69"),

    PlatformEvaluateEveryDayShare("70"),
    /**
     * 每日任务环数奖励
     */
    DailyTaskRingReward("71"),
    /**
     * 社区商城
     */
    HouseShop("72"),

    /**
     * 仙侣商城
     */
    CoupleShop("73"),

    /**
     * 福地积分奖励
     */
    FudScore("74"),

    /**
     * 篝火采集计数
     */
    WorldBonfireGather("75"),
    ;

    private final String value;

    BaseCountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BaseCountType convert(String value) {
        for (BaseCountType baseCountType : BaseCountType.values()) {
            if (value.equals(baseCountType.getValue())) {
                return baseCountType;
            }
        }
        return null;
    }
}
