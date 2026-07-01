package com.game.count.structs;

import com.data.Global;

/**
 * @author admin
 */
public enum VariantType {
    /**
     * 每日boolean值
     */
    BooleanDayValue(0, Count.RefreshType.CountType_Day),
    /**
     * 每周boolean值
     */
    BooleanWeekValue(1, Count.RefreshType.CountType_Week),
    /**
     * 每月boolean值
     */
    BooleanMonthValue(2, Count.RefreshType.CountType_Month),
    /**
     * 每年boolean值
     */
    BooleanYearValue(3, Count.RefreshType.CountType_Year),
    /**
     * 永久boolean值
     */
    BooleanForeverValue(4, Count.RefreshType.CountType_Forever),
    /**
     * 举报次数
     */
    TodayReportCount(5, Count.RefreshType.CountType_Day),

    /**
     * 巅峰竞技总场次
     */
    PeakTotal(6, Count.RefreshType.CountType_Forever),

    /**
     * 仙缘.情缘副本
     */
    MarryCloneBuy(7, Count.RefreshType.CountType_Day),
    /**
     * 巅峰竞技总胜利场次
     */
    PeakWin(8, Count.RefreshType.CountType_Forever),
    /**
     * 巅峰竞技 场次奖励邮件状态
     */
    PeakTimesRewardMail(9, Count.RefreshType.CountType_Day),
    /**
     * 上传头像
     */
    UP_CUSTOM_AVATAR(10, Count.RefreshType.CountType_Forever),
    /**
     * 巅峰竞技场今日获得经验
     */
    Peak_DayPkExp(11, Count.RefreshType.CountType_Day),
    /**
     * 和好友聊天增加亲密度次数
     */
    ChAT_WITH_FRIEND_ADD_INTIMACY(12, Count.RefreshType.CountType_Day),
    /**
     * 巅峰竞技 今日总挑战次数奖励
     */
    Peak_DayBox(13, Count.RefreshType.CountType_Day),
    /**
     * 巅峰竞技 段位奖励
     */
    Peak_StageBox(14, Count.RefreshType.CountType_Week),
    /**
     * 当日首次登陆时等级
     */
    Today_First_Login_Level(15, Count.RefreshType.CountType_Day),
    /**
     * 0点刷新
     */
    ZeroClock(16, Count.RefreshType.CountType_Day, 0),
    /**
     * 竞技场领奖CD
     */
    JJCrewardCd(17, Count.RefreshType.CountType_Day),
    /**
     * 5点刷新
     */
    FiveClock(18, Count.RefreshType.CountType_Day, Global.Daily_times_reset_time),
    /**
     * 战斗任务免费次数
     */
    BATTLE_TASK_FREE_FRESH_COUNT(19, Count.RefreshType.CountType_Day),
    /**
     * 今日在线时间秒
     */
    Daily_Online_Time(20, Count.RefreshType.CountType_Day),
    /**
     * 今日元宝仙甲寻宝X次
     */
    Daily_XianJiaXunBao_Times(21, Count.RefreshType.CountType_Day),
    /**
     * 今日掌门传道X 秒
     */
    Daily_LeaderPreach_Time(22, Count.RefreshType.CountType_Day),
    /**
     * 今日打坐X 秒
     */
    Daily_Meditation_Time(23, Count.RefreshType.CountType_Day),
    /**
     * 今日领取X次剑灵阁收益
     */
    Daily_SwordSoul_Times(24, Count.RefreshType.CountType_Day),
    /**
     * 今日挑战X次竞技场
     */
    Daily_JJC_Times(25, Count.RefreshType.CountType_Day),
    /**
     * 今日击杀个人首领X次
     */
    Daily_Kill_Self_Boss_Times(26, Count.RefreshType.CountType_Day),
    /**
     * 今日击杀无限首领X次
     */
    Daily_Kill_UnLimit_Boss_Times(27, Count.RefreshType.CountType_Day),
    /**
     * 今日击杀无极虚域首领x次
     */
    Daily_Kill_WuJIArea_Boss_Times(28, Count.RefreshType.CountType_Day),
    /**
     * 今日击杀晶甲首领x次
     */
    Daily_Kill_JingJia_Boss_Times(29, Count.RefreshType.CountType_Day),
    /**
     * 今日参与天禁之门X次
     */
    Daily_Enter_TJZM_Times(30, Count.RefreshType.CountType_Day),
    /**
     * 今日完成赏金之道x次
     */
    Daily_ShangJingFunc_Times(31, Count.RefreshType.CountType_Day),

    /**
     * 今日活跃度达到x点
     */
    Daily_Active_Value(32, Count.RefreshType.CountType_Day,0),
    /**
     * 巅峰竞技场。赛季
     */
    PeakRankCalc(33, Count.RefreshType.CountType_Week,0),
    /**
     * 初级寻宝累计总次数(包括免费与非免费)
     */
    PRIMARY_SEEK_TOTAL_NUM(34, Count.RefreshType.CountType_Day),
    /**
     * 今日高级非免费已寻宝次数
     */
    TODAY_SENIOR_SEEK_NUM(35, Count.RefreshType.CountType_Day),
    /**
     * 高级寻宝累计总次数(包括免费与非免费)
     */
    SENIOR_SEEK_TOTAL_NUM(36, Count.RefreshType.CountType_Forever),
    /**
     * 免费祈祷金币时刻
     */
    FREE_PRAY_MONEY_MOMENT(37, Count.RefreshType.CountType_Forever),

    /**
     * 运营活动幸运值
     */
    ACTIVITY_LUCKY_VALUE(38, Count.RefreshType.CountType_Forever),

    /**
     * 组队爬塔的扫荡次数
     */
    Copymap_Team_Tower(41, Count.RefreshType.CountType_Day),

    /**
     * 挑战副本的扫荡次数
     */
    Copymap_Challenge_RaidLevel(42, Count.RefreshType.CountType_Day),
    /**
     * 资源副本的采集数量
     */
    CopyMap_Res_Gather(43, Count.RefreshType.CountType_Day),

    /**
     * 组队爬塔进入次数
     */
    TEAM_TOWER_NUM(44, Count.RefreshType.CountType_Day),

    /**
     * 在爱丽丝仙境中使用鼓舞的次数
     */
    AILISI_UP_MORALE_NUM(45, Count.RefreshType.CountType_Day),

    /**
     * 每日首席任务完成次数
     */
    SHOUXI_TASK_FINISH_NUM(46, Count.RefreshType.CountType_Day),
    /**
     * 宗派活动--福地boss，玩家怒气值
     */
    GuildActivityAnger(48, Count.RefreshType.CountType_Day),
    /**
     * 仙缘.情缘副本已挑战次数
     */
    MarryCloneUseTimes(50, Count.RefreshType.CountType_Day),
    /**
     * 仙缘.仙匣购买奖励
     */
    MarryBoxBuyOnceReward(51, Count.RefreshType.CountType_Forever),
    /**
     * 仙缘.缘定三生奖励
     */
    MarryWallReward(52, Count.RefreshType.CountType_Forever),
    /**
     * 法宝神器解封转盘每日次数
     */
    TreasureUnlock(55, Count.RefreshType.CountType_Day),
    /**
     * 每日活跃值
     * TODO 废弃
     */
    DailyActivePoint(56, Count.RefreshType.CountType_Day),
    /**
     * 玩家累计消耗绑定元宝
     */
    ConsumeBindGold(57, Count.RefreshType.CountType_Forever),
    /**
     * 玩家累计消耗元宝
     */
    ConsumeGold(58, Count.RefreshType.CountType_Forever),
    /**
     * 玩家累计消耗金币
     */
    ConsumeBindCoin(59, Count.RefreshType.CountType_Forever),
    /**
     * 累计玩家打坐小时
     */
    AccuMeditation(60, Count.RefreshType.CountType_Forever),
    /**
     * 竞技场/首席大弟子累积参加次数
     */
    JJCAccumulationCount(61, Count.RefreshType.CountType_Forever),
    /**
     * 仙缘。爱情宣言CD
     */
    MarryDeclarationCD(62, Count.RefreshType.CountType_Forever),
    /**
     * 仙缘。情缘副本次数
     */
    MarryCloneBuyTimes(63, Count.RefreshType.CountType_Day),
    /**
     * 婚姻--免费购买次数
     */
    MarryPrayFree(64, Count.RefreshType.CountType_Day),
    /**
     * 仙盟福地刷新
     */
    GuildFudi(65, Count.RefreshType.CountType_Day),
    /**
     * 宠物升阶
     */
    PetUpDegree(66, Count.RefreshType.CountType_Forever),
    /**
     * 领取登录礼包
     */
    LoginGift(67, Count.RefreshType.CountType_Forever),
    /**
     * 在服务器仓库捐献X次
     */
    ServerStore(68, Count.RefreshType.CountType_Forever),

    /**
     * 宠物吃装备次数
     */
    PetEat(69, Count.RefreshType.CountType_Forever),

    /**
     * 仙盟福地，title刷新
     */
    GuildFudiTitleReset(70, Count.RefreshType.CountType_Hour),

    /**
     * 回收炉
     */
    RecycleNum(71, Count.RefreshType.CountType_Forever),

    /**
     * 结婚次数
     */
    MarryNum(72, Count.RefreshType.CountType_Forever),

    /**
     * 结婚购买宝匣次数
     */
    MarryBoxNum(73, Count.RefreshType.CountType_Forever),

    /**
     * 日常系统刷新
     */
    DailyWeekActive(74, Count.RefreshType.CountType_Week),

    /**
     * 日常系统刷新
     */
    DailyDayActive(75, Count.RefreshType.CountType_Day),
    /**
     * 增加好友
     */
    AddFriendNum(77, Count.RefreshType.CountType_Forever),
    /**
     * 永久使用道具增加活跃上限的次数
     */
    AddDailyActiveMaxCount(78, Count.RefreshType.CountType_Forever),
    /**
     * 永久使用道具增加活跃上限值
     */
    AddDailyActiveMaxNum(79, Count.RefreshType.CountType_Forever),
    /**
     * 每日活跃值消耗
     */
    DailyActivePointCost(80, Count.RefreshType.CountType_Day),
    /**
     * 玩家累计充值
     */
    RechargeMoney(81, Count.RefreshType.CountType_Forever),
    /**
     * 世界答题
     */
    WorldAnswerNum(82, Count.RefreshType.CountType_Forever),
    /**
     * 世界支援
     */
    WorldHelpNum(83, Count.RefreshType.CountType_Forever),
    /**
     * 世界被支援
     */
    WorldBeHelpNum(84, Count.RefreshType.CountType_Forever),
    /**
     * 技能升级次数统计
     */
    SkillUpNum(85, Count.RefreshType.CountType_Forever),
    /**
     * 法宝任务接受多少次
     */
    FaBaoTaskNum(86, Count.RefreshType.CountType_Forever),
    /**
     * 经验悟道累计次数
     */
    EXPPrayNum(87, Count.RefreshType.CountType_Forever),
    /**
     * 灵石悟道累计次数
     */
    MoneyPrayNum(88, Count.RefreshType.CountType_Forever),
    /**
     * 公会退出免费CD时间
     */
    GuildQuitFree(90, Count.RefreshType.CountType_Forever),

    /**
     * 击杀X个仙盟福地BOSS或者混沌之境BOSS（二者击杀总和达到X即可）
     */
    Kill_Guild_Territorial_Boss(91, Count.RefreshType.CountType_Day),

    /**
     * 法宝任务每周完成多少次
     */
    FaBaoTaskFinishNumWeek(92, Count.RefreshType.CountType_Week),
    /**
     * 仙盟福地pvp增加的个人积分
     */
    GuildActivityBossPvpScore(93, Count.RefreshType.CountType_Day),
    /**
     * 玩家累计充值
     */
    RechargeGold(94, Count.RefreshType.CountType_Forever),
    /**
     * 仙缘任务
     */
    MarryTask(95, Count.RefreshType.CountType_Forever),
    /**
     * 每日击杀精英怪次数
     */
    MarryTaskReward(96, Count.RefreshType.CountType_Forever),
    /**
     * 诸界远征boss击杀
     */
    CrossFudBossKill(97, Count.RefreshType.CountType_Forever),

    /**
     * 仙盟频道发言次数
     */
    GuildChat(98, Count.RefreshType.CountType_Forever),

    /**
     * 参与仙盟战次数
     */
    GuildWar(99, Count.RefreshType.CountType_Forever),

    /**
     * 在组队状态下参与传道X次
     */
    GroupLeaderpreach(100, Count.RefreshType.CountType_Forever),
    /**
     * 剑灵挂机快速收益次数
     */
    HookQuickEarn(101, Count.RefreshType.CountType_Day),

    /**
     * 完成x档婚礼
     */
    FinishWedding(102, Count.RefreshType.CountType_Forever),

    /**
     * 世界频道发言次数
     */
    WorldChat(103, Count.RefreshType.CountType_Forever),
    /**
     * 累计技能蜕变次数
     */
    UpSkillTuibianCount(104, Count.RefreshType.CountType_Forever),
    /**
     * 完美情缘活动
     */
    MarryActivity(105, Count.RefreshType.CountType_Forever),
    /**
     * 每日击杀诸界远征boss
     */
    Kill_crossfudi_Boss(106, Count.RefreshType.CountType_Day),
    /**
     * 每日充值
     */
    Daily_RechargeGold(107, Count.RefreshType.CountType_Day),
    /**
     * 聚宝盆领奖
     */
    HouseTup(108, Count.RefreshType.CountType_Day),

    /**
     * vip BOSS
     */
    Daily_Kill_VIP_Boss_Times(109, Count.RefreshType.CountType_Day),

    /**
     * 封魔台抽奖
     */
    Daily_DevilHunt_Times(110, Count.RefreshType.CountType_Day),
    /**
     * v4助力活动
     */
    V4HelpScript(111, Count.RefreshType.CountType_Forever),

    /**
     * 房屋任务刷新
     */
    Daily_Home_Task(111, Count.RefreshType.CountType_Day),

    ;



    /**
     * key值，从0一次增加
     */
    private final long key;
    /**
     * (0:天， 1:周，2:月，3:年， 4:永久)
     */
    private final Count.RefreshType type;

    /**
     * 每日刷新  小时
     */
    private int hour;

    VariantType(long key, Count.RefreshType type) {
        this.key = key;
        this.type = type;
    }

    VariantType(long key, Count.RefreshType type, int hour) {
        this.key = key;
        this.type = type;
        this.hour = hour;
    }

    /**
     * 获取key
     *
     * @return
     */
    public long getKey() {
        return key;
    }

    /**
     * 获取刷新类型(0:天， 1:周，2:月，3:年， 4:永久)
     *
     * @return
     */
    public Count.RefreshType getType() {
        return type;
    }

    /**
     * 每日刷新  小时
     */
    public int getHour() {
        return hour;
    }
}
