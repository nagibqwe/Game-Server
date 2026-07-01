package com.game.bi.struct;

import com.data.FunctionStart;

/**需要加BI数据的内置活动的类型枚举*/
public enum BIActiityTypeEnum {
    //运营活动
    GetActive(0,"",1,"活跃兑换",2),
    DailyRecharge(0,"",2,"每日充值",2),
    LimitTimeLogin(0,"",3,"每日登录",2),
    LimitGiftBag(0,"",4,"限购礼包",2),
    DailyDraw(0,"",5,"天帝宝库",2),
    RechargeTotal(0,"",6,"累计充值",2),
    TotalConsume(0,"",7,"限时消耗",2),
    CollectGoodsExchange(0,"",8,"集物兑换",2),
    GroupBuy(0,"",9,"团购",2),
    LuckyCat(0,"",10,"招财猫",2),
    HolidayBoss(0,"",11,"首领狂欢",2),
    HolidayTask(0,"",12,"庆典任务",2),
    HolidayWords(0,"",13,"集字活动",2),
    FestivalPreference(0,"",14,"节日特惠(直购礼包)",2),
    ContinuouRecharge(0,"",15,"连续充值",2),
    LimitShopActivity(0,"",16,"限购商城(元旦活动)",2),
    LimitTimeGift(0,"",17,"限时礼包",2),
    HolidayScore(0,"",18,"积分排名",2),
    FestvialWish(0,"",19,"节日许愿",2),
    FBShare(0,"",20,"FB分享",2),
    ContinuouRecharge2(0,"",21,"连续充值2",2),
    NewYearWish(0,"",22,"新年祝福",2),
    Dice(0,"",23,"掷骰子",2),
    LuckyCat2(0,"",24,"幸运宝玉",2),

    //内置活动
    VIP_DailyGift(101, "VIP",1,"每日礼包", 1),
    VIP_PurGift(101, "VIP",2,"等级礼包", 1),
    DOWNLOWD(0,"",102, "资源下载", 1),
    CHARGE_WELFARE(0,"",103, "充值返利", 1),
    CRAZY_WEEKEND(0,"",104, "狂欢周（周六狂欢）", 1),
    DISCOUNT(0,"",105, "超值折扣", 1),
    MYSTERY_SHOP(0,"",106, "神秘商店", 1),
    FIRST_CHARGE(0,"",107,"首冲奖励领取", 1),
    FREE_BUY(0,"",108, "0元购", 1),
    LOVE_SHIP(0,"",109, "完美情缘", 1),
    QUESTIONNAIRE(0,"",110, "有奖问卷", 1),
    WorldBonfire(0,"",111,"日暮篝火", 1),
    WorldAnswer(0,"",112,"心境博弈（世界答题）", 1),
    LimitShop(0,"",113, "神秘限购", 1),
    JJC(0,"",114, "竞技场", 1),
    RetrieveRes(0,"",115, "资源找回", 1),
    SoulAnimalIsland(0,"",116, "年兽封域", 1),
    NewFREE_BUY(0,"",117, "新0元购", 1),

    PeakStageWin(117,"巅峰竞技",1,"赢",1),
    PeakStageLose(117,"巅峰竞技",2,"输",1),
    PeakStage(117,"巅峰竞技", 3, "段位奖励", 1),

    GuildBoss_Inspire(118,"仙盟首领", 1 ,"鼓舞",1),

    FallingSky_Buy(119,"天禁令",1, "高级天禁令购买",1),
    FallingSky_TaskAward(119,"天禁令",2, "天禁令任务奖励领取",1),
    FallingSky_LevelAward(119,"天禁令",3, "天禁令等级奖励领取",1),

    SoulArmorLottery(120,"祈灵（魂甲抽奖）", 3),

    Marriage(121, "结婚", 1),

    ActivityRank(122,"修仙宝鉴", 1),
    CoupleEscort(123,"神女巡游", 0, "任务完成", 1),
    FunctionTask(124,"核心任务", 1, "直购礼包购买", 1),

    OPEN_SERVER_CRAZY(201, "开服狂欢"),
    LUCKY_CARD(201, "开服狂欢", 11, "幸运翻牌",1),
    KILL_BOSS(201, "开服狂欢", 12, "BOSS首杀",1),

    GrowthWay(202, "成长之路"),
    GrowthWay_one(202, "成长之路", 1, "历练之道",1),
    GrowthWay_two(202, "成长之路", 2, "神装之道",1),
    GrowthWay_three(202, "成长之路", 3, "聚宝之道",1),
    GrowthWay_four(202, "成长之路", 4, "仙盟之道",1),

    XFHD_JOTC(203, "新服活动", 1, "佳偶天成",1),
    XFHD_XFHD(203, "新服活动", 2, "新服活动",1),

    ZFL_privilege(204, "周福利", 1, "特权放送",1),
    ZFL_DRAW(204, "周福利", 2, "转盘",1),

    RANK(205, "排行榜"),

    Pay_Newbie(206, "充值", 1, "新手充值",1),
    Pay_Day(206, "充值", 2, "每日充值",1),
    Pay_Week(206, "充值", 3, "每周充值",1),
    Pay_Base(206, "充值", 4, "基础充值",1),

    WELFARE_LoginGift(207, "福利", 1, "登录礼包",1),
    WELFARE_DailyCheck(207, "福利", 2, "每日签到",1),
    WELFARE_Investment(207, "福利", 3, "成长基金",1),
    WELFARE_WuDao(207, "福利", 4, "鸿蒙悟道",1),
    WELFARE_Card(207, "福利", 5, "特权卡",1),
    //    WELFARE_DailyGift(207, "福利", 6, "每日礼包"),
    WELFARE_ExchangeGift(207, "福利", 7, "兑换礼包",1),
    WELFARE_LevelGift(207, "福利", 8, "等级礼包",1),
    WELFARE_InvestmentPeak(207, "福利", 9, "巅峰基金",1),

    EVERYDAY_CHARGE(208, "每日累充", 1, "每日累充",1),
    EVERYDAY_CONSUME(208, "每日累充", 2, "每日消耗",1),
    EVERYDAY_CANGBAOGE(208, "每日累充", 3, "藏珍阁",1),
    EVERYDAY_DUIBAODIAN(208, "每日累充", 4, "兑宝殿",1),

    TreasureXianjia(209, "寻宝", 1,"仙甲寻宝",1),
    TreasureFind(209, "寻宝", 2,"机缘寻宝",1),
    TreasureZaoHua(209, "寻宝", 3,"造化寻宝",1),
    TreasureXianPo(209, "寻宝", 4,"灵魂抽取",1),

    //-------------------完成分割线（上面做好了）---------------------


    //-------------------开发分割线（上面开发中）---------------------

    ;

    private int id; //活动id(functionStart的id（运营活动id+一千万）)
    private String name;
    private int subId;//二级活动id
    private String subName;//二级活动名字
    private int type = 1;// 1内置活动 2运营活动

    BIActiityTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    BIActiityTypeEnum(int id, String name, int subId, String subName, int type) {
        this.id = id;
        this.name = name;
        this.subId = subId;
        this.subName = subName;
        this.type = type;
    }

    BIActiityTypeEnum(int id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getSubId() {
        return subId;
    }

    public String getSubName() {
        return subName;
    }
}
