/**
 * Auto generated, do not edit it
 *
 * Global表
 */
package com.data;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadStringArrayEs; 
import com.data.struct.ReadLongArray; 
import com.data.struct.ReadLongArrayEs; 
import com.data.struct.ReadStringArray; 
import com.data.struct.ReadFloatArray; 

public final class Global{

    /**
     * 日常一键全部完成消耗元宝:10
     */
    public static int DailyRingRonsumption = 10;
    /**
     * 日常一键完成消耗元宝:10
     */
    public static int DailyOneConsume = 10;
    /**
     * 邮件自动删除时间（天）:15
     */
    public static int MailDelete = 15;
    /**
     * 收件箱最大存储邮件数量:50
     */
    public static int MailMaxNum = 50;
    /**
     * 创建宗派所需货币数量:"1,10"
     */
    public static ReadIntegerArray GuildCreateMoney;
    /**
     * 宗派名字最大和最小长度:"7,2"
     */
    public static ReadIntegerArray GuildNameLimit;
    /**
     * 角色名字最大和最小长度:"14,2"
     */
    public static ReadIntegerArray PlayerNameLimit;
    /**
     * 集市上架元宝底价(废弃):2
     */
    public static int AuctionYBmin = 2;
    /**
     * 集市税收（百分比）:10
     */
    public static int AuctionTax = 10;
    /**
     * 职业性别配置（职业_性别；0女1男）:"0,1}1,0}2,1}3,0"
     */
    public static ReadIntegerArrayEs JobSex;
    /**
     * 神秘商店刷新基数元宝:10
     */
    public static int MysteryShopRefreshBase = 10;
    /**
     * 神秘商店刷新封顶元宝:100
     */
    public static int MysteryShopRefreshMax = 100;
    /**
     * 装备栏默认图标：0.头盔、1.武器、2.胸甲、3.项链、4.腰带、5.腿甲、6.鞋子、7.戒指:"0,267}1,266}2,268}3,272}4,269}5,270}6,271}7,273}8,2063}9,2062}10,2064}30,266}31,268}32,899}33,901}34,905}35,905}36,267}37,902}38,904}39,904}40,903}41,269}42,270}43,271}201,1228}202,1227}203,1226}204,1225}44,266}45,268}46,899}47,901}48,905}49,905}50,267}51,902}52,904}53,904}54,903}55,269}56,270}57,271}58,266}59,268}60,899}61,901}62,905}63,905}64,267}65,902}66,904}67,904}68,903}69,269}70,270}71,271}72,266}73,268}74,899}75,901}76,905}77,905}78,267}79,902}80,904}81,904}82,903}83,269}84,270}85,271}401,1712}402,1714}403,1669}404,1670}405,1671}406,1672}407,1673}408,1674}409,1675}410,1676}411,1712}412,1714}413,1669}414,1670}415,1671}416,1672}417,1673}418,1674}419,1675}420,1676}421,1712}422,1714}423,1669}424,1670}425,1671}426,1672}427,1673}428,1674}429,1675}430,1676}431,1712}432,1714}433,1669}434,1670}435,1671}436,1672}437,1673}438,1674}439,1675}440,1676"
     */
    public static ReadIntegerArrayEs EquipDefaultIcon;
    /**
     * 自动穿戴装备的时间，单位秒:5
     */
    public static int Equip_auto_time = 5;
    /**
     * 基础移动速度:6
     */
    public static int BaseMoveSpeed = 6;
    /**
     * 移动速度千分比:2000
     */
    public static int MoveSpeed = 2000;
    /**
     * 攻击速度千分比:2000
     */
    public static int AttackSpeed = 2000;
    /**
     * 伤害公式-防御参数1.1:11000
     */
    public static int Damage_Defense = 11000;
    /**
     * 伤害公式-伤害下限参数1--85%:8500
     */
    public static int Damage_Min1 = 8500;
    /**
     * 伤害公式-伤害下限参数2--幸运分母:100
     */
    public static int Damage_Min2 = 100;
    /**
     * 伤害公式-伤害下限参数3--35%:3500
     */
    public static int Damage_Min3 = 3500;
    /**
     * 伤害公式-伤害上限 120%:12000
     */
    public static int Damage_Max = 12000;
    /**
     * 原地复活道具:60019
     */
    public static int ReviveItem = 60019;
    /**
     * 原地复活元宝:10
     */
    public static int ReviveYB = 10;
    /**
     * 伤害公式-暴击率下限 5%:500
     */
    public static int Damage_CritMin = 500;
    /**
     * 伤害公式-暴击率上限 45%:4500
     */
    public static int Damage_CritMax = 4500;
    /**
     * 邮件信封图标 未读已读:"11,12"
     */
    public static ReadIntegerArray MailIcon;
    /**
     * 日常任务完成20环奖励（职业_道具ID_数量）客户端显示用的:"0,50064,1}1,50064,1}2,50064,1}3,50064,1}4,50064,1}5,50064,1"
     */
    public static ReadIntegerArrayEs Daily20Reward;
    /**
     * 宗派篝火默认buffID:900003
     */
    public static int GuildExpBuff = 900003;
    /**
     * 仓库开启VIP等级限制:"1.0"
     */
    public static ReadIntegerArrayEs StoreOpenConditionl;
    /**
     * 宠物战斗跟随距离(厘米):1000
     */
    public static int Pet_fight_rice = 1000;
    /**
     * 宠物非战斗跟随距离(厘米):250
     */
    public static int Pet_rest_rice = 250;
    /**
     * 精魄存在最长时间（分钟）:60
     */
    public static int Pet_live_time = 60;
    /**
     * 新手村初始化主线任务:99001
     */
    public static int FristTask = 99001;
    /**
     * 竞技场每日调整次数(废弃):10
     */
    public static int JJCDailyNum = 10;
    /**
     * 竞技场单场战斗时间（毫秒） = 5分钟:120000
     */
    public static int JJCOnceTime = 120000;
    /**
     * 竞技场离线失败时间（毫秒） = 10秒:10000
     */
    public static int JJCOutlineTime = 10000;
    /**
     * 竞技场购买挑战次数花费元宝:2
     */
    public static int JJCBuyNum = 2;
    /**
     * 玩家角色能到达的最高等级:800
     */
    public static int PlayerMaxLevel = 800;
    /**
     * 宠物复活倒计时 (毫秒）:10000
     */
    public static int Pet_res_time = 10000;
    /**
     * 篝火半径:20
     */
    public static int GuildExpRadius = 20;
    /**
     * 职业的默认身体和武器模型 (格式: 职业_身体模型ID_武器模型ID):"0,3000100,2000100}1,3100100,2100100}2,3200100,2200100}3,3300100,2300100}4,3400100,2400100}5,3500100,2500100}"
     */
    public static ReadIntegerArrayEs DefaultBody;
    /**
     * 职业的默认空模型,只用来播放动作 (格式: 职业_身体模型ID):"0,3099999}1,3199999}2,3299999}3,3399999}4,3599999}5,3699999}"
     */
    public static ReadIntegerArrayEs DefaultEmptyBody;
    /**
     * 宗派任务全部完成奖励:"0,50033,1}1,50033,1}2,50033,1}3,50033,1}4,50033,1}5,50033,1"
     */
    public static ReadIntegerArrayEs GuildTaskAllReward;
    /**
     * 宗派任务完成上限(0 宗派周常；1 宗派日常；2仙盟日常）:"0,70}1,10}2,5"
     */
    public static ReadIntegerArrayEs GuildTaskMax;
    /**
     * 弹劾会长消耗:"4,100"
     */
    public static ReadIntegerArray GuildLeaderAccuse1;
    /**
     * 宗派解散天数:30
     */
    public static int GuildDismissDay = 30;
    /**
     * 宗派日志最大条数:200
     */
    public static int GuildLogMax = 200;
    /**
     * 宗派保留20条申请记录:20
     */
    public static int GuildApply = 20;
    /**
     * 日常任务每日上限（0 赏金日常；1 晶甲日常；2 活跃日常）:"0,20}1,1}2,2"
     */
    public static ReadIntegerArrayEs DailyTaskMax;
    /**
     * 宗派答题开启时间（年-月-日-星期-时-分）:"*-*-*-1-20-35}*-*-*-3-20-35}*-*-*-5-20-35}*-*-*-7-20-35"
     */
    public static ReadStringArrayEs GuildAnswerOpenTime;
    /**
     * 宗派答题间隔时间（秒）:10
     */
    public static int GuildAnswerCoolingTime = 10;
    /**
     * 宗派答题数量:20
     */
    public static int GuildAnswerNum = 20;
    /**
     * 宗派答题时间:20
     */
    public static int GuildAnswerTime = 20;
    /**
     * 宗派地图Id:20201
     */
    public static int GuildMapID = 20201;
    /**
     * 宗派答题正确奖励:"11,50"
     */
    public static ReadIntegerArray GuildAnswerCorrectReward;
    /**
     * 宗派答题错误奖励:"11,25"
     */
    public static ReadIntegerArray GuildAnswerErrorReward;
    /**
     * 宗派答题前3名额外奖励:"9,5"
     */
    public static ReadIntegerArray GuildAnswerRank1_3Reward;
    /**
     * 宗派战开启时间:"*-*-*-7-21-00"
     */
    public static String GuildBattleOpenTime = "*-*-*-7-21-00";
    /**
     * 宗派战持续时间（分钟）:15
     */
    public static int GuildBattleTime = 15;
    /**
     * 宗派资金的ICON:897
     */
    public static int GuildMoneyIcon = 897;
    /**
     * 开启宗派试炼需要消耗道具数量:"50041,1667"
     */
    public static ReadIntegerArray GuildBossNum;
    /**
     * 宗派试炼奖励道具显示:"100009,100010"
     */
    public static ReadIntegerArray GuildBossReward;
    /**
     * 每日出题数量上限:10
     */
    public static int DailyIssueNumMax = 10;
    /**
     * 每日答题数量上限:10
     */
    public static int DailyAnswerNumMax = 10;
    /**
     * 发题间隔时间（分钟）:2
     */
    public static int IssueCoolingTime = 2;
    /**
     * 答题间隔时间（分钟）:2
     */
    public static int AnswerCoolingTime = 2;
    /**
     * 每个试炼水晶对应获得奖励:"11,100"
     */
    public static ReadIntegerArray GuildBossItemReward;
    /**
     * 完成所有宗派任务奖励的道具（只用于客户端显示）:"50041,30"
     */
    public static ReadIntegerArray GuildTaskAllCompeteReward;
    /**
     * 宠物召唤冷却 （毫秒）:20000
     */
    public static int Pet_out_time = 20000;
    /**
     * 服务器初始世界等级:110
     */
    public static int WorldLevelServerFirst = 110;
    /**
     * pve减伤下限:50
     */
    public static int Damage_PveMin = 50;
    /**
     * pve增伤上限:150
     */
    public static int Damage_PveMax = 150;
    /**
     * pvp减伤上限:50
     */
    public static int Damage_PvpMin = 50;
    /**
     * pvp增伤上限:150
     */
    public static int Damage_PvpMax = 150;
    /**
     * 战斗力压制比下限:100
     */
    public static int Damage_FightRepressMin1 = 100;
    /**
     * 战斗力压制比下限生效值:100
     */
    public static int Damage_FightRepressMin2 = 100;
    /**
     * 战斗力压制比上限:100
     */
    public static int Damage_FightRepressMax1 = 100;
    /**
     * 战斗力压制比上限生效值:100
     */
    public static int Damage_FightRepressMax2 = 100;
    /**
     * 宗派入侵开启和结束时间:"3020-*-*-6-20-00}3020-*-*-6-20-15"
     */
    public static ReadStringArrayEs GuildPveActiviteOpenTime;
    /**
     * 每日免费刷新次数:3
     */
    public static int IDontKnow1 = 3;
    /**
     * 战魂公告：大于等于此品质的进行世界公告（1白色，2绿色，3蓝色，4紫色，5橙色，6金色，7红色）:4
     */
    public static int FightNoticeColour = 4;
    /**
     * 宗派篝火刷新时间:"*-*-*-1-20-30}*-*-*-3-20-30}*-*-*-5-20-30}*-*-*-7-20-30"
     */
    public static ReadStringArrayEs GuildExpRefreshTime;
    /**
     * 宗派篝火刷新位置（X_Z）:"154,81"
     */
    public static ReadIntegerArray GuildExpRefreshPosition;
    /**
     * 宗派战匹配时间:"*-*-*-7-0-00"
     */
    public static String GuildBattleMatchingTime = "*-*-*-7-0-00";
    /**
     * 第二次宗派战:"*-*-*-7-21-20"
     */
    public static String SecondGuildBattleMatchingTime = "*-*-*-7-21-20";
    /**
     * 改名卡道具ID_消耗的元宝数量:"1006,300"
     */
    public static ReadIntegerArray ChangeNameItem;
    /**
     * 宗派篝火ICON:336
     */
    public static int GuildExpIcon = 336;
    /**
     * 翻滚引导的提示等级，小于等于此变量就会有提示:220
     */
    public static int RollLeadLevel = 220;
    /**
     * 宗派公告冷却时间 单位小时:24
     */
    public static int GuildNoticeCoolingTime = 24;
    /**
     * 宗派公告内容长度:30
     */
    public static int GuildNoticeLength = 30;
    /**
     * 在市集中密码交易上架的单价底价值:20
     */
    public static int AuctionPasswordYBmin = 20;
    /**
     * 聊天框系统频道经验提示值下限:2000000000
     */
    public static int ChatExpMin = 2000000000;
    /**
     * 狂轰乱炸副本技能配置，id_范围_伤害最小值_伤害最大值_CD:"1,300,90,99,500}2,1000,450,500,5000"
     */
    public static ReadIntegerArrayEs FlyCloneSkill;
    /**
     * 月卡用户身份标志:936
     */
    public static int MouthCardIcon = 936;
    /**
     * 终身卡用户身份标志:937
     */
    public static int ForeverCardIcon = 937;
    /**
     * 对话气泡显示距离_时间_CD:"10,3,6"
     */
    public static ReadIntegerArray ChatBubbleDistance;
    /**
     * 月卡加终身卡用户身份标志:937
     */
    public static int DoubleCardIcon = 937;
    /**
     * 宗派设置加入限制默认等级:53
     */
    public static int GuildJoinMinLevel = 53;
    /**
     * 罪大恶极BUFFID:22
     */
    public static int Killbuff1 = 22;
    /**
     * 十恶不赦BUFF:23
     */
    public static int Killbuff2 = 23;
    /**
     * 寻路任务范围（玩家所在坐标点正负取值）:4
     */
    public static int TaskFindRaner = 4;
    /**
     * 成长基金购买所需蓝钻:1000
     */
    public static int GrowthFundCost = 1000;
    /**
     * 红包的存在时间，单位（秒）此处为48小时:172800
     */
    public static int RedBagTime = 172800;
    /**
     * 乱斗BUFF配置(GroundBuffID_坐标X_坐标Y):"1,29,29}2,29,36}3,29,22"
     */
    public static ReadIntegerArrayEs GroundBuff;
    /**
     * 进入BOSS之家正常层消耗的元宝:"0,1000,1000,1200,1200,1600,2000,3000"
     */
    public static ReadIntegerArray EnterBossHomeYB;
    /**
     * BOSS之家正常层免费进入VIP等级:"4,5,6,7,8,9,10,11"
     */
    public static ReadIntegerArray EnterBossHomeVipLevel;
    /**
     * 乱斗之王报名开始时间:"*-*-*-6-20-30"
     */
    public static String LuanDouKingOpenTime = "*-*-*-6-20-30";
    /**
     * 乱斗之王匹配时间:"*-*-*-6-20-35"
     */
    public static String LuanDouKingTime = "*-*-*-6-20-35";
    /**
     * 世界答题开启时间（年-月-日-星期-时-分）:"*-*-*-2-12-00}*-*-*-4-12-00}*-*-*-6-12-00}"
     */
    public static ReadStringArrayEs WorldAnswerOpenTime;
    /**
     * 世界答题间隔时间（秒）:10
     */
    public static int WorldAnswerCoolingTime = 10;
    /**
     * 世界答题数量:10
     */
    public static int WorldAnswerNum = 10;
    /**
     * 世界答题时间（每题初选时间_改选时间_间隔时间）（秒）:"20,6,4"
     */
    public static ReadIntegerArray WorldAnswerTime;
    /**
     * 世界答题奖励（枚举（对或者错）_道具(Item)ID_数量_是否绑定:"1,9,6,1}2,9,3,1"
     */
    public static ReadIntegerArrayEs WorldAnswerReward;
    /**
     * 世界答题前几名玩家获得额外奖励:"5,2,100000,1"
     */
    public static ReadIntegerArray WorldAnswerRankReward;
    /**
     * 登录奖励:"3,70008}7,55127"
     */
    public static ReadIntegerArrayEs IDontKonw2;
    /**
     * 多人坐骑可邀请距离:15
     */
    public static int MuitMountDistance = 15;
    /**
     * 宗派屏蔽显示功能ID:"470000,150000,770000,280000,1000000"
     */
    public static ReadIntegerArray IDontKnow3;
    /**
     * 宗派解散检测时间:"*-*-*-5-00-00"
     */
    public static String GuildDismissCheckTime = "*-*-*-5-00-00";
    /**
     * 宗派解散积分:100
     */
    public static int GuildDismissNum = 100;
    /**
     * 世界聊天的开启要求（格式：等级_VIP等级），满足任意一个条件开启:"195,3"
     */
    public static ReadIntegerArray ChatWorldLevel;
    /**
     * 退出宗派以后，多长时间可以加入（分钟）:120
     */
    public static int GuildQuitCoolingTime = 120;
    /**
     * 队伍邀请查找附近的人的时候最多找多少个:50
     */
    public static int TeamInvitationMaxNum = 50;
    /**
     * 宗派篝火存在的时间:15
     */
    public static int GuildExpLiveTime = 15;
    /**
     * 全民福利开服多少时间后结束:14
     */
    public static int IDontKown4 = 14;
    /**
     * 全民福利每多少分钟模拟加一次:10
     */
    public static int IDontKown5 = 10;
    /**
     * 达到VIP几可进入BOSS之家:4
     */
    public static int EnterBossHomeVipLimit = 4;
    /**
     * 弹劾会长火钻不足消耗:"3,100"
     */
    public static ReadIntegerArray GuildLeaderAccuse2;
    /**
     * 首冲提示开启条件:"510022}16000}0,9000011,200,0,0}1,9000012,200,0,0}2,9000013,100,0,0}3,9000014,100,0,0}4,9000015,400,0,0}5,9000016,150,0,0"
     */
    public static ReadIntegerArrayEs FristRechargeOpenCondition;
    /**
     * 七日活动上榜名次:140
     */
    public static int SevenDayActiviteLevelNeed = 140;
    /**
     * 玩家pve战斗压制比大于等于值（放大100倍）：100:100
     */
    public static int Damage_PlayerPveNum1 = 100;
    /**
     * 玩家pve战斗压制比上限（放大100倍）：100 :100
     */
    public static int Damage_PlayerPveNum2 = 100;
    /**
     * 玩家pve战斗压制比小于值（放大100倍）：100 :50
     */
    public static int Damage_PlayerPveNum3 = 50;
    /**
     * 玩家pve战斗压制比下限（放大100倍）：10 :50
     */
    public static int Damage_PlayerPveNum4 = 50;
    /**
     * 怪物战斗压制比大于值（放大100倍）：300:100
     */
    public static int Damage_MonsterPveNum1 = 100;
    /**
     * 怪物战斗压制比上限（放大100倍）：300 :400
     */
    public static int Damage_MonsterPveNum2 = 400;
    /**
     * 怪物战斗压制比小于等于值（放大100倍）：100 :75
     */
    public static int Damage_MonsterPveNum3 = 75;
    /**
     * 怪物战斗压制比下限（放大100倍）：100:75
     */
    public static int Damage_MonsterPveNum4 = 75;
    /**
     * 宗派仓库固定的道具:"10001,20000,1"
     */
    public static ReadIntegerArray GuildStoreFixedItem;
    /**
     * 仓库初始固定格子:200
     */
    public static int StoreCreateNum = 200;
    /**
     * 世界等级经验生效等级:110
     */
    public static int WorldLevelNeed = 110;
    /**
     * 小飞鞋功能使用物品ID:60007
     */
    public static int FlyShoeID = 60007;
    /**
     * 宗派每天领取道具:1004
     */
    public static int GuildDailyFreeItem = 1004;
    /**
     * 宗派资金ICON:865
     */
    public static int GuildMoneyIcon1 = 865;
    /**
     * 过短距离不使用小飞鞋:15
     */
    public static int FlyShoeMinDistance = 15;
    /**
     * 世界等级经验加成参数：等级A:10
     */
    public static int WorldLevelPram1 = 10;
    /**
     * 世界等级经验加成参数：等级B:13
     */
    public static int WorldLevelPram2 = 13;
    /**
     * 世界等级经验加成参数：每级经验，万分比:660
     */
    public static int WorldLevelPram3 = 660;
    /**
     * 世界等级经验加成上限：百分比:200
     */
    public static int WorldLevelPram4 = 200;
    /**
     * 尊享卡商品ID:"50065,202001"
     */
    public static ReadIntegerArray ForeverCardItemID;
    /**
     * 宗派仓库捐献装备品阶（阶数）:3
     */
    public static int GuildStoreDonateEquipLevel = 3;
    /**
     * 宗派仓库捐献装备品质（品质_是否带星）:"4,1"
     */
    public static ReadIntegerArray GuildStoreDonateEquipStar;
    /**
     * 最大角色等级:450
     */
    public static int PlayerMaxLevel1 = 450;
    /**
     * 弹劾会长所需会长离线天数:3
     */
    public static int GuildLeaderAccuseDay = 3;
    /**
     * BOSS重置的检查玩家的时间间隔时间值 单位：毫秒:10000
     */
    public static int IDontKown9 = 10000;
    /**
     * 世界答题对错积分（对:"400,200"
     */
    public static ReadIntegerArray WorldAnswerCorrectNum;
    /**
     * 世界答题排名积分（名称_积分:"1,3}2,2}3,1}4,1}5,1"
     */
    public static ReadIntegerArrayEs WorldAnswerErrorNum;
    /**
     * 进入个人boss消耗的火钻或者蓝钻:120
     */
    public static int BossNew_PersonalEnterCost1 = 120;
    /**
     * 转职任务直接完成消耗蓝钻:"0,0,500,0,0,0,0"
     */
    public static ReadIntegerArray ChangeJobTaskCostYBNum;
    /**
     * 在世界BOSS地图中触发回城复活限制的回城复活次数:5
     */
    public static int BossNew_WorldRevive1Times = 5;
    /**
     * 在世界BOSS地图中回城复活限制的等待时间（秒）:60
     */
    public static int BossNew_WorldRevive1CoolingTime = 60;
    /**
     * 在世界BOSS地图中触发原地复活限制的原地复活次数:10
     */
    public static int BossNew_WorldRevive2Times = 10;
    /**
     * 在世界BOSS地图中原地复活限制的等待时间（秒）:60
     */
    public static int BossNew_WorldRevive2CoolingTime = 60;
    /**
     * 宗派怪物攻城刷新间隔时间:120000
     */
    public static int GuildMonsterFightRefreshCoolingTime = 120000;
    /**
     * 宗派攻城最大波次:8
     */
    public static int GuildMonsterFightBatch = 8;
    /**
     * 宗派怪物攻城开启时间:"*-*-*-2-20-30}*-*-*-4-20-30}*-*-*-6-20-30"
     */
    public static ReadStringArrayEs GuildMonsterFightOpenTime;
    /**
     * 宗派怪物攻城存在时间:20
     */
    public static int GuildMonsterFightAliveTime = 20;
    /**
     * 每日累充界面展示模型ID(模型ID_缩放_旋转):"50001,220,0"
     */
    public static ReadIntegerArray DailyRechargeModleID;
    /**
     * 神魔战场阵营图标:"0,1163}1,1164}2,1165"
     */
    public static ReadIntegerArrayEs DailyActivite1CampIcon;
    /**
     * 王者对决可购买次数:5
     */
    public static int DailyPKKingBuyTimes = 5;
    /**
     * 王者对决购买消耗:"10}20}30}40}50"
     */
    public static ReadIntegerArrayEs DailyPKKingBuyCost;
    /**
     * 怪物攻城提前提示消息时间（分钟）:5
     */
    public static int GuildMonsterFightAdvancePrompt = 5;
    /**
     * 天之禁地每层成功之后所加buff id:80064
     */
    public static int DailyActivite2SuccessBuffID = 80064;
    /**
     * 宗派答题提前提示时间（分钟）:5
     */
    public static int GuildAnswerAdvancePrompt = 5;
    /**
     * 开服第N天世界等级开始生效(大于等于天数):1
     */
    public static int WorldLevelDatNeed = 1;
    /**
     * 转职4转完成之前等级最大370，经验最高累计11430613440000:"4,370,11430613440000"
     */
    public static ReadLongArray ChangeJobLimitLevelAndExp;
    /**
     * 灵魂格子开启的等级条件:"320}320}325}330}335}340}350"
     */
    public static ReadIntegerArrayEs SoulBoxOpenLevel;
    /**
     * 塔防副本建造塔的位置(地图位置标注【写死】_x坐标_y坐标_塔ID；）:"1,19,42,1}2,19,37}3,19,32}4,19,28}5,25,28}6,30,28}7,35,28}8,41,28}9,46,28}10,51,28}11,56,28}12,56,33}13,51,33}14,56,39}15,51,39}16,56,44}17,51,44}18,39,44}19,31,44,2}20,39,49}21,31,49"
     */
    public static ReadIntegerArrayEs TowerCloneTowerPosition;
    /**
     * 塔防副本灵魂背包数量:200
     */
    public static int TowerCloneSoulBagNum = 200;
    /**
     * 巅峰四转开启等级:350
     */
    public static int ChangeJob_4_OpenLevel = 350;
    /**
     * 塔防副本副本单次扫荡所需材料_数量:"50113,3"
     */
    public static ReadIntegerArray TowerCloneOnceWipeNeedItem;
    /**
     * 巅峰等级开启元级:370
     */
    public static int PeakLevel_NeedLevel = 370;
    /**
     * 神兽岛兽灵水晶和兽神水晶每日可采集次数:2
     */
    public static int BossOld2_DailyGather1OpenTimes = 2;
    /**
     * 神兽岛兽灵宝箱和兽神宝箱每日可开启次数:2
     */
    public static int BossOld2_DailyBox1OpenTimes = 2;
    /**
     * 神兽岛兽血水晶每日可采集次数:5
     */
    public static int BossOld2_DailyGather2OpenTimes = 5;
    /**
     * 神兽岛兽血宝箱每日可开启次数:10
     */
    public static int BossOld2_DailyBox2OpenTimes = 10;
    /**
     * 神兽岛采集掉兽血水晶掉血间隔（秒）_:1
     */
    public static int BossOld2_DailyGather2LoseBloodTime = 1;
    /**
     * 神兽岛采集掉兽血水晶掉血比例：损失当前血量百分比:15
     */
    public static int BossOld2_DailyGather2LoseBloodProportion = 15;
    /**
     * 神兽岛采集掉兽灵水晶和兽神水晶掉血间隔（秒）_:3
     */
    public static int BossOld2_DailyGather1LoseBloodTime = 3;
    /**
     * 神兽岛采集掉兽灵水晶和兽神水晶掉血比例：损失当前血量百分比:15
     */
    public static int BossOld2_DailyGather1LoseBloodProportion = 15;
    /**
     * 魂兽装备双倍强化时每X点经验提升1蓝钻消耗，不足100也算100:100
     */
    public static int BossOld2_EquipDoubleNeedYB = 100;
    /**
     * 魂兽默认可合体位置数量:1
     */
    public static int BossOld2_PossessionNum = 1;
    /**
     * 魂兽额外合体位置扩展消耗:所需等级_道具ID_数量:"250,60016,1}250,60016,1}350,60016,2}450,60016,4"
     */
    public static ReadIntegerArrayEs BossOld2_PossessionOtherItem;
    /**
     * 王者联盟战队名字长度字符数限制:"2,14"
     */
    public static ReadIntegerArray KingAlliance_TeamNameLimit;
    /**
     * 王者联盟赛季结束前X天无法踢人或解散战队:7
     */
    public static int KingAlliance_EndingTime = 7;
    /**
     * 王者联盟创建战队消耗，优先火钻:100
     */
    public static int KingAlliance_TeamCreatNeed = 100;
    /**
     * 王者联盟战队改名消耗，优先火钻:50
     */
    public static int KingAlliance_TeamChangeNameCost = 50;
    /**
     * 王者联盟战队最大成员数限制:3
     */
    public static int KingAlliance_TeamMaxNum = 3;
    /**
     * 王者联盟每天可参与次数:10
     */
    public static int KingAlliance_DailyTimes = 10;
    /**
     * 王者联盟可购买次数:5
     */
    public static int KingAlliance_BuyTimes = 5;
    /**
     * 王者联盟购买消耗:"10,20,30,40,50"
     */
    public static ReadIntegerArray KingAlliance_BuyCost;
    /**
     * 魂兽森林采集水晶扣血BUFFID:80200
     */
    public static int BossOld2_DailyGatherBuff = 80200;
    /**
     * 魂兽背包格子总数:100
     */
    public static int BossOld2_BagNum = 100;
    /**
     * 切换职业所消耗的物品_数量:"50148,1"
     */
    public static ReadIntegerArray JobChangeNeedItem;
    /**
     * 魂兽合成底图:"1495,1496,1493,1494,1497"
     */
    public static ReadIntegerArray BossOld2_BaseMap;
    /**
     * 仙缘系统是否需要异性才能结婚 0否 1是:1
     */
    public static int MarryNeedSex = 1;
    /**
     * 装备铸灵要求装备最低要求:品质_阶数:"5,9"
     */
    public static ReadIntegerArray EquipZhuLingNeedMin;
    /**
     * 进入洪荒神迹以消耗的道具Id:50154
     */
    public static int BossOld3_EnterNeedItem = 50154;
    /**
     * 进入洪荒神迹消耗的道具初始数量和增幅:"1}1"
     */
    public static ReadIntegerArrayEs BossOld3_EnterNeedItemNum;
    /**
     * 炼丹需要寻路的NPCID:"1000000,1000001,1000002,1000003,1000004,1000005,1000006,1000007"
     */
    public static ReadIntegerArray IDontKown7;
    /**
     * 洪荒神迹刷新时间:"11,15,18,22"
     */
    public static ReadIntegerArray BossOld3_RefreshTime;
    /**
     * 洪荒神迹天谴值上限:100
     */
    public static int BossOld3_LimitNum = 100;
    /**
     * 进入洪荒神殿的道具的单价:20
     */
    public static int BossOld3_EnterNeedItemPrice = 20;
    /**
     * 圣天城战报名基础积分:100
     */
    public static int HolySkyCity_BasePointNum = 100;
    /**
     * 圣天城战报名每次增加积分:10
     */
    public static int HolySkyCity_AddPointNum = 10;
    /**
     * 城主专属奖励，用于展示:"80048,1}60074,1"
     */
    public static ReadIntegerArrayEs HolySkyCity_KingReward;
    /**
     * 元素神殿死亡扣除精力值:5
     */
    public static int BossOld4_DeathCostLimitNum = 5;
    /**
     * 元素神殿每X秒扣除1点精力值:6
     */
    public static int BossOld4_LoseLimitNumSecond = 6;
    /**
     * 元素神殿精力值满值:100
     */
    public static int BossOld4_MaxLimitNum = 100;
    /**
     * 元素神殿进入所需道具ID:50149
     */
    public static int BossOld4_EnterNeedItem = 50149;
    /**
     * 元素神殿进入所需道具价值，优先消耗火钻:10
     */
    public static int BossOld4_EnterNeedItemPrice = 10;
    /**
     * 元素神殿每天第一次进入消耗道具数量:10
     */
    public static int BossOld4_FristEnterNeedItemNum = 10;
    /**
     * 元素神殿每天后续进入增加消耗数量:5
     */
    public static int BossOld4_AddEnterNeedItemNum = 5;
    /**
     * 元素神殿精力值耗尽踢出副本倒计时 秒:9
     */
    public static int BossOld4_CountDownSecond = 9;
    /**
     * 宗派联赛报名时间:"*-*-*-5-3-0}*-*-*-6-3-0"
     */
    public static ReadStringArrayEs GuildMatchesSignUpTime;
    /**
     * 宗派联赛第一次战斗时间:"*-*-*-6-21-00}*-*-*-6-21-15"
     */
    public static ReadStringArrayEs GuildMatchesFristFightTime;
    /**
     * 宗派联赛第二次战斗时间:"*-*-*-6-21-20}*-*-*-6-21-35"
     */
    public static ReadStringArrayEs GuildMatchesSecondFightTime;
    /**
     * 宗派联赛每月最后一周周六结算的时间:"*-*-*-6-22-00"
     */
    public static String GuildMatchesMonthSettlement = "*-*-*-6-22-00";
    /**
     * 圣天城战报名时间:"*-*-*-7-00-00}*-*-*-7-20-00"
     */
    public static ReadStringArrayEs HolySkyCity_signUpTime;
    /**
     * 圣天城战战斗时间:"*-*-*-7-20-00}*-*-*-7-20-20"
     */
    public static ReadStringArrayEs HolySkyCity_FightTime;
    /**
     * 元素神殿每天旗帜出现时间点:"0,55}2,25}3,55}5,25}6,55}8,25}9,55}11,25}12,55}14,25}15,55}17,25}18,55}20,25}21,55}23,25"
     */
    public static ReadIntegerArrayEs BossOld4_FlagAppearTime;
    /**
     * 元素神殿每天旗帜消失时间点:"0,10}1,40}3,10}4,40}6,10}7,40}9,10}10,40}12,10}13,40}15,10}16,40}18,10}19,40}21,10}22,40"
     */
    public static ReadIntegerArrayEs BossOld4_FlagDisappearTime;
    /**
     * 元素神殿扣血BUFFID:900005
     */
    public static int BossOld4_LoseBloodBuff = 900005;
    /**
     * 宗派联赛匹配队伍时间:"*-*-*-7-00-00"
     */
    public static String GuildMatchesTeamTime = "*-*-*-7-00-00";
    /**
     * 元素神殿离开旗帜X米范围后取消占领状态:5
     */
    public static int BossOld4_FlagCancelHold = 5;
    /**
     * 元素神殿旗帜刷新间隔时间 S:5400
     */
    public static int BossOld4_FlagCancelHold1 = 5400;
    /**
     * 巅峰五转等级:490
     */
    public static int ChangeJob_5_OpenLevel = 490;
    /**
     * 符文位置开放配置:"520,1}520,2}520,3}520,4}520,5}520,6}530,7}530,8}530,9}530,10}540,11"
     */
    public static ReadIntegerArrayEs RunePositionLevel;
    /**
     * 符文背包最大格子数:120
     */
    public static int RuneBagBox = 120;
    /**
     * 元素之尘icon:1234
     */
    public static int RuneDustIcon = 1234;
    /**
     * 光明符文位置:"1,2,3,4,5,6"
     */
    public static ReadIntegerArray RuneSunPosition;
    /**
     * 黑暗符文位置:"7,8,9,10"
     */
    public static ReadIntegerArray RuneDarkPosition;
    /**
     * 虚空符文位置:11
     */
    public static int RuneSkyPosition = 11;
    /**
     * 荣誉之战1血:10
     */
    public static int LOLFirstBlood = 10;
    /**
     * 荣誉之战击杀:5
     */
    public static int LOLKill = 5;
    /**
     * 荣誉之战助攻:1
     */
    public static int LOLAssists = 1;
    /**
     * 荣誉之战小兵1小兵死亡时在仇恨列表中的都会增加积分。:1
     */
    public static int LOLLittleMonstePointNum = 1;
    /**
     * 荣誉之战攻城傀儡，傀儡死亡时在仇恨列表中的都会增加积分。:10
     */
    public static int LOLBigMonstePointNum = 10;
    /**
     * 荣誉之战1血塔，御塔被摧毁时在仇恨列表会都增加积分。:200
     */
    public static int LOLFirstTowerPointNum = 200;
    /**
     * 荣誉之战防御塔，御塔被摧毁时在仇恨列表会都增加积分。:100
     */
    public static int LOLCommonTowerPointNum = 100;
    /**
     * 推塔团队分：防御塔被摧毁后，摧毁防御塔的阵营所有玩家获得:50
     */
    public static int LOLFirstTowerTeamPointNum = 50;
    /**
     * 荣耀战场阵营水晶icon:"1487,1488"
     */
    public static ReadIntegerArray LOLHomeIcon;
    /**
     * 最近好友显示数量上限:50
     */
    public static int NearlyFriendMax = 50;
    /**
     * 好友显示及加取数量上限:50
     */
    public static int FriendMax = 50;
    /**
     * 仇人显示及加取数量上限:30
     */
    public static int EnemyMax = 30;
    /**
     * 屏蔽显示及加取数量上限:30
     */
    public static int BanMax = 30;
    /**
     * 邮件显示数量:50
     */
    public static int MailDisplayMax = 50;
    /**
     * 每天可加亲密度的次数:2
     */
    public static int DailyLoveTimes = 2;
    /**
     * 每次私聊单个好友增加的亲密度:5
     */
    public static int PrivatelyChatLove = 5;
    /**
     * 推荐好友显示数量:4
     */
    public static int RecommendFriendMax = 4;
    /**
     * 宗派公示榜显示列表量:20
     */
    public static int GuildRankDisplayNum = 20;
    /**
     * 精准加成的下限 90%:9000
     */
    public static int Damage_Parm1 = 9000;
    /**
     * 精准加成上限  110%:11000
     */
    public static int Damage_Parm2 = 11000;
    /**
     * 破甲加成下限 50%:5000
     */
    public static int Damage_Parm3 = 5000;
    /**
     * 破甲加成上限 100%:10000
     */
    public static int Damage_Parm4 = 10000;
    /**
     * 会心机率上限 50%:5000
     */
    public static int Damage_Parm5 = 5000;
    /**
     * 会心暴击倍数 170%:17000
     */
    public static int Damage_Parm6 = 17000;
    /**
     * 连击机率上限 50%:5000
     */
    public static int Damage_Parm7 = 5000;
    /**
     * 连击暴击倍数 190%:19000
     */
    public static int Damage_Parm8 = 19000;
    /**
     * 追击机率上限 50%:5000
     */
    public static int Damage_Parm9 = 5000;
    /**
     * 追击暴击倍数 210%:21000
     */
    public static int Damage_Parm10 = 21000;
    /**
     * 破击下限0%:0
     */
    public static int Damage_Parm11 = 0;
    /**
     * 破击上限99%:9999
     */
    public static int Damage_Parm12 = 9999;
    /**
     * 御破下限0%:0
     */
    public static int Damage_Parm13 = 0;
    /**
     * 御破上限99%:9999
     */
    public static int Damage_Parm14 = 9999;
    /**
     * 普通暴击倍数 150%:15000
     */
    public static int Damage_Parm15 = 15000;
    /**
     * 在线离线挂机掉落生效间隔时间（秒）(废弃):60
     */
    public static int OnHookCoolingTime = 60;
    /**
     * 最大粗存离线收益时间点数的上限，单位分钟:1200
     */
    public static int OnHookMaxNum = 1200;
    /**
     * 单次离线收益最大时间上限，单位分钟:1200
     */
    public static int OnHookOnceMaxRewardTime = 1200;
    /**
     * 模糊查询最大显示数量:10
     */
    public static int FuzzyQueryMaxNum = 10;
    /**
     * 每天的总共举报数量:5
     */
    public static int MaxReportNum = 5;
    /**
     * 最短离线增加经验时间(分):5
     */
    public static int OnHookMinAddTime = 5;
    /**
     * 位面退出的延迟等待时间，单位毫米:4000
     */
    public static int PlanesQuitWaitTime = 4000;
    /**
     * 好友匹配推荐在线好友等级差:20
     */
    public static int FriendMatchLevelNum = 20;
    /**
     * 是否非好友能进行私聊 0 可以私聊；1 不可以:0
     */
    public static int IfFriendPrivatelyChat = 0;
    /**
     * 举报内容字数限制：最小字数_最大字数:"1,100"
     */
    public static ReadIntegerArray ReportNumLimit;
    /**
     * 客户端打坐飘字间隔时间（秒）:2
     */
    public static int OnHookClientTimes = 2;
    /**
     * 客户端经验地图飘字间隔时间（秒）:2
     */
    public static int OnHookClientMapExpTimes = 2;
    /**
     * 组队得人数上限:2
     */
    public static int TeamNum = 2;
    /**
     * 组队自动匹配倒计时:30
     */
    public static int TeamMatchTime = 30;
    /**
     * 日常任务扫荡界面奖励显(subtype_物品ID1_物品ID2）(0经验日常，1银币日常）:"0,3,100000}1,18005,1017"
     */
    public static ReadIntegerArrayEs DailyTaskSweepRewardDisplay;
    /**
     * 宗派任务扫荡界面奖励显(subtype_物品ID1_物品ID2）(0宗派周常，1宗派日常）:"0,2,60004}1,2,60004"
     */
    public static ReadIntegerArrayEs GuildTaskSweepRewardDisplay;
    /**
     * 日常任务环奖励(subtype_物品ID_物品数量）(0经验日常，1银币日常）:"0,10,1017,200}0,20,1017,200"
     */
    public static ReadIntegerArrayEs DailyTaskRingReward;
    /**
     * 宗派任务环奖励(subtype_物品ID_物品数量）(0宗派周常，1宗派日常）:"0,60004,1}1,60004,1"
     */
    public static ReadLongArrayEs GuildTaskRingReward;
    /**
     * 宗派福地，每个地图刷新的最大怪物数量，用于服务器遍历用:10
     */
    public static int GuildBattleBoss_MapMaxMonster = 10;
    /**
     * 宗派活跃宝贝，文字聊天和语音聊天增加的活跃点:"2,4"
     */
    public static ReadIntegerArray GuildBabyChatActivePoint;
    /**
     * 宗派活跃宝贝，给与玩家玩家的排名奖励。排名_奖励；:"1,19001}2,19002}3,19003"
     */
    public static ReadIntegerArrayEs GuildBabyRankReward;
    /**
     * 宗宗派福地，关联的dialyID，客户端用于读取道具展示:207
     */
    public static int GuildBattleBoss_DailyId = 207;
    /**
     * 宗宗派福地，每日的怒气值上限，每日零点清除:200
     */
    public static int GuildBattleBoss_LimtiNumMax = 200;
    /**
     * 宗宗派福地，超出多少范围清除仇恨，超出范围也不会有收益，格子数:10
     */
    public static int GuildBattleBoss_RewardRange = 10;
    /**
     * 宗宗派福地，死亡多少秒后清除伤害，单位时间秒:15
     */
    public static int GuildBattleBoss_DeathTime = 15;
    /**
     * 宗派中，大于等于多少级的宗派可编辑公告:3
     */
    public static int GuildNoticeLevelNeed = 3;
    /**
     * 宗派夺宝活动中，副本等待时间，单位秒:50
     */
    public static int GuildCloneWaitTime = 50;
    /**
     * 境界中，第1档礼包的充值金额，小于等于该金额推送这个，单位元:100
     */
    public static int VipFirstGift1Cost = 100;
    /**
     * 境界中，第2档礼包的充值金额，大于1档小于等于第2档，单位元，最后1个大于2档:300
     */
    public static int VipFirstGift2Cost = 300;
    /**
     * 宗派宝藏中，各种积分奖励的倍率【无领地，1档，2档，3档】:"100,150,200,250"
     */
    public static ReadIntegerArray GuildClonePointNum;
    /**
     * 宗派宝藏中，各种积分的分段,无领地表示0，0-999为1档，1000-1799为2档，大于1800为3档:"0,0}1,999}1000,1799}1800,9999"
     */
    public static ReadIntegerArrayEs GuildClonePointNumRange;
    /**
     * 大能遗府获得星数的奖励配置:"4,10001,5}8,10001,5}12,10002,5}15,10002,5"
     */
    public static ReadIntegerArrayEs StarCloneStarReward;
    /**
     * 宗派夺宝中，进入的clone副本ID，用于服务器读取:20101
     */
    public static int Guild_treaMap = 20101;
    /**
     * 仙缘系统-求婚是否全服公告的消耗  货币类型_数量(废弃）:"1,20"
     */
    public static ReadIntegerArray Marry_notice_cost;
    /**
     * 仙缘系统-当天可购买次数_请求购买次数_免费祈福次数:"1,1,1"
     */
    public static ReadIntegerArray Marry_belss_time;
    /**
     * 仙缘系统-对应三种不同婚礼邀请到的人数上限:"20,25,30"
     */
    public static ReadIntegerArray Marry_wedding_num;
    /**
     * 仙缘系统-开启时间h_最后开启时间h_持续时间m(废弃，挪至marry_order表）:"900,2300,30"
     */
    public static ReadIntegerArray Marry_wedding_last;
    /**
     * 仙缘系统-可预约好多天内的婚宴:1
     */
    public static int Marry_wedding_orde = 1;
    /**
     * 购买星级副本所需要的货币_数量:"1,20"
     */
    public static ReadIntegerArray StarCloneCost;
    /**
     * 境界中，多长时间没打开面板或者关闭面板，判断为进入挂机状态，用于前端判断:8
     */
    public static int Vip_afk_time = 8;
    /**
     * 境界中，礼包充值档位的，1表示小于100，2表示100-499，3表示大于等于500:"100,500"
     */
    public static ReadIntegerArray Vip_package_rech;
    /**
     * 个人BOSS中，切割道具ID_持续的时间（秒）_花费的元宝:"1013,3600,200"
     */
    public static ReadIntegerArray SingleBoss_item;
    /**
     * 个人BOSS中，增加刷新时长的道具ID:1015
     */
    public static int SingleBoss_item_refr = 1015;
    /**
     * 个人BOSS中，多倍掉落卡的ID_使用一次获得X次额外掉落:"1014,1"
     */
    public static ReadIntegerArray SingleBoss_special_drop;
    /**
     * BOSS刷新卡的ID:1009
     */
    public static int SingleBoss_refreBoss = 1009;
    /**
     * 仙缘系统-副本保留时间（分）:20
     */
    public static int Marry_Clone_item = 20;
    /**
     * 掉落奖励次数:掉落类型_击杀归属掉落次数_排名掉落次数_阳光掉落次数:"0,0,5,5}1,0,5,5}2,0,5,5}5,0,3,3}7,0,5,5}9,0,3,3"
     */
    public static ReadIntegerArrayEs Boss_kill_drop;
    /**
     * 个人BOSS中，刷新额外的BOSS的卡ID:1009
     */
    public static int SingleBoss_refreSpecBoss = 1009;
    /**
     * boss关注逻辑中，提示面板，提前多少秒钟弹出:60
     */
    public static int Boss_attent_notice = 60;
    /**
     * 离线与在线打坐收益掉落ID(废弃):100001
     */
    public static int Afk_speical_drop = 100001;
    /**
     * 仙缘系统-仙娃改名消耗：货币类型_货币价格，以后都是最大值:"1,100"
     */
    public static ReadIntegerArray Marry_child_name;
    /**
     * 仙缘系统-爱情宝匣购买消耗货币类型和数量:"1,520"
     */
    public static ReadIntegerArray Marry_box_cost;
    /**
     * 仙缘系统-爱情宝匣购买立返货币类型和数量:"1058,1"
     */
    public static ReadIntegerArray Marry_box_rew;
    /**
     * 仙缘系统-爱情宝匣购买后每天领取的奖励:"16101,16}1052,10}50001,5"
     */
    public static ReadIntegerArrayEs Marry_box_sepcialrew;
    /**
     * 仙缘系统-爱情宝匣购买后可领取的天数:30
     */
    public static int Marry_box_day = 30;
    /**
     * 仙缘系统-对诗失败奖励的itemID及其数量:"16201,5"
     */
    public static ReadIntegerArrayEs Marry_Ques_lose;
    /**
     * 仙缘系统-对诗成功奖励的itemID及其数量:"16201,10"
     */
    public static ReadIntegerArrayEs Marry_Ques_suces;
    /**
     * 仙缘系统-三倍收取消耗的货币类型和数量:"2,100"
     */
    public static ReadIntegerArray Marry_Ques_rew;
    /**
     * 仙缘系统-祈福一次获得的祈福成长点 失败经验_成功经验:"5,10"
     */
    public static ReadIntegerArray Marry_belss_up;
    /**
     * 法宝系统中，转盘的角度和对应的数字，用于转盘使用（起始角度_终点角度_数字）:"0,29,6}30,89,8}90,119,10}120,179,6}180,239,8}240,269,30}270,329,6}330,359,10"
     */
    public static ReadIntegerArrayEs Magic_angle;
    /**
     * 法宝系统中，每日免费转盘次数，每日零点的时候重置次数:3
     */
    public static int Magic_time = 3;
    /**
     * 竞技场中，秒杀的时候战力判断，高于地方战力多少显示秒杀，高的百分比:20
     */
    public static int JJc_fighting_kill = 20;
    /**
     * 赠送记录保存的条数:50
     */
    public static int Gift_time = 50;
    /**
     * 竞技场中，每日奖励发放的时间点，单位小时:21
     */
    public static int JJc_reward_time = 21;
    /**
     * 日常任务（经验，银币），宗派任务（日常，周常）扫荡券不足时的绑元/元宝单次消耗:4
     */
    public static int Daily_task_cost = 4;
    /**
     * 洗练消耗-道具,锁数,数量:"60003,0,2}60003,1,4}60003,2,8}60003,3,16}60003,4,32"
     */
    public static ReadIntegerArrayEs Clear_time;
    /**
     * 元宝洗练消耗-锁定空位_货币单位_消耗数量:"1,1,25}2,1,80}3,1,180}4,1,280"
     */
    public static ReadIntegerArrayEs Clear_cost;
    /**
     * 排行榜中，每日崇拜的上限次数:10
     */
    public static int Rank_time = 10;
    /**
     * 装备洗炼，每个条目计算分数的权重:10000
     */
    public static int Clear_weight = 10000;
    /**
     * 仙缘系统-购买一次祈福次数的货币类型和花费:"2,30"
     */
    public static ReadIntegerArray Marry_belss_cost;
    /**
     * 竞技场中，刷新的CD，单位时间秒，倒计时内按钮设置灰色:3
     */
    public static int JJC_refresh_time = 3;
    /**
     * 神兵激活是赠送的神兵材质ID(职业_模具1_模具2_模具3）:"0,3001003,3000003,3002003}1,3011002,3010002,3012002"
     */
    public static ReadIntegerArrayEs Frist_God_Weapon;
    /**
     * 仙缘系统-祈福倍率奖励:3
     */
    public static int Marry_belss_double = 3;
    /**
     * 福利-经验祈福的免费次数（鸿蒙悟道）:0
     */
    public static int Welfare_Blessing_Times = 0;
    /**
     * 福利-基金购买全服增加的随机次数:"1,3"
     */
    public static ReadIntegerArray Welfare_Pray_Times;
    /**
     * 经验副本的经验药水购买（物品ID_商会ID（填0不能买））:"1003,0}1002,0}1001,0"
     */
    public static ReadIntegerArrayEs Exp_Clone_Buy_Item;
    /**
     * 经验副本的鼓舞设置（金币消耗_BUFF；绑元消耗_BUFF）:"5000,80031}5,80032"
     */
    public static ReadIntegerArrayEs Exp_Clone_Power_Up;
    /**
     * 世界答题每题奖励（最大等级1_道具(Item)ID_数量_是否绑定）:"400,1017,2,1}800,1017,2,1"
     */
    public static ReadIntegerArrayEs World_answer_reward_new;
    /**
     * 世界答题最终奖励（最小排名_最大排名_道具(Item)ID_数量_是否绑定）(废弃):"3200,4000,60004,20,1}2400,3000,60004,18,1}1400,2200,60004,16,1}200,1200,60004,14,1"
     */
    public static ReadIntegerArrayEs World_answer_reward_final;
    /**
     * 1 ->查看  2 ->私聊  3 ->赠送 4 ->组队 5 ->邀请加入公会 6 -> 加为仇人 7->删除仇人 8 -> 屏蔽 9 -> 删除屏蔽 10 -> 加为好友 11 ->删除好友 12 ->举报 13 -> 踢出队伍 13 -> 提升队长:"1,2,3,4,13,14,7,8,9,10,11,12,14"
     */
    public static ReadIntegerArray Social_List;
    /**
     * 开服活动中，每日结算的时间点，小时，距离0点的小时:23
     */
    public static int New_server_rewtime = 23;
    /**
     * 开服活动中，成长之路的最终购买奖励的道具ID，和直购的最高价格单位元宝:"10026,10000"
     */
    public static ReadIntegerArray New_server_rew;
    /**
     * 开服狂欢中，虚假名次给与的排名名次假名次，结算点时在线的非前面的玩家都可获得奖励:100
     */
    public static int New_server_fake = 100;
    /**
     * 两人队伍，用第一列，三人组队用前二列，四人组队用三列；分别代表几人队伍的经验加成:"2,0}3,10}4,20"
     */
    public static ReadIntegerArrayEs New_team_exp;
    /**
     * 经验副本的杀怪数量_星数:"50,1}100,2}150,3}200,4}250,5"
     */
    public static ReadIntegerArrayEs Exp_Clone_Kill_num;
    /**
     * 宗派中，弹劾帮助需要的元宝:200
     */
    public static int IMPEACHMENTMATERIAL = 200;
    /**
     * 宗派中，会长几天不上线时间弹劾:2
     */
    public static int GUILDIMPEACHMENT = 2;
    /**
     * 宗派中，公会宣言最大长度:240
     */
    public static int GuildNoticeMaxLength_G = 240;
    /**
     * 宗派中，帮会名字最大长度_最小长度:"14,2"
     */
    public static ReadIntegerArray GuildNameLength_G;
    /**
     * 宗派中，解散公会时间:3
     */
    public static int DISSOLVEGUILD = 3;
    /**
     * 宗派中，公会log事件保留最大条数:50
     */
    public static int GUILDLOGCOUNT = 50;
    /**
     * 宗派中，帮会篝火刷新坐标:"50,50"
     */
    public static ReadIntegerArray GuildBonfireFreshPosition;
    /**
     * 宗派中，发送公告的间隔时间:2
     */
    public static int GuildEmailNotice = 2;
    /**
     * 宗派中，帮会仓库中，固定放入的道具:10001
     */
    public static int GUILDSTOREHOUSEITEM = 10001;
    /**
     * 宗派中，帮会仓库中固定领取道具:10001
     */
    public static int GuildReceiveItem = 10001;
    /**
     * 初始背包格子_最大背包格子:"120,180"
     */
    public static ReadIntegerArray Born_Bag_Num;
    /**
     * 开服活动成长之路中，活动存在的时间，开服时间的第几天，时间到之后就不再判断条件更新进度:8
     */
    public static int New_server_growup_time = 8;
    /**
     * 开服活动中，成长之路和开服活动的多余领奖时间，开服第几天开始就不再显示开服活动相关界面:8
     */
    public static int New_server_growup_timeoff = 8;
    /**
     * 排行榜中，每日崇拜给与的道具奖励道具_数量:"12,100"
     */
    public static ReadIntegerArrayEs Rank_praise;
    /**
     * 开服活动中，展示模型的属性值，需要和皮肤表中一致，需要同步修改2个地方:"1,15440}2,413643}3,7720}4,7720"
     */
    public static ReadIntegerArrayEs New_server_modle_att;
    /**
     * 开服活动中，非兑换和7日红包活动持续的时间到多少天，配置第8天零点:8
     */
    public static int New_server_active_time = 8;
    /**
     * 开服活动中，兑换活动和7日红包持续的时间[开服第多少天,配置第9天零点:9
     */
    public static int New_server_active_exchange = 9;
    /**
     * 开服活动中，消耗元宝兑换成绑元的比列，【第一天，比列】；【第二天，比列】,比列=配置/10000:"1,1000}2,1500}3,2000}4,2500}5,1000}6,1500},7,2000"
     */
    public static ReadIntegerArrayEs New_server_redbag;
    /**
     * 队员掉线多长时间后会被系统踢出队伍:5
     */
    public static int Team_leave_time = 5;
    /**
     * 心魔副本每波怪物之间的刷新间隔（豪秒）:3000
     */
    public static int Xinmo_CD = 3000;
    /**
     * 五行副本每波怪物之间的刷新间隔（豪秒）:3000
     */
    public static int Wuxing_CD = 3000;
    /**
     * 大能遗府的星级判断标准，剩余时间在【0-60】1星,【60-90】2星，【90以上】3星:"60,90"
     */
    public static ReadIntegerArray StarCloneNum;
    /**
     * 大能遗府的星级判断标准，剩余时间在【0-60】1星,【60-90】2星，【90以上】3星:"60,90"
     */
    public static ReadIntegerArray XinmoCloneNum;
    /**
     * 大能遗府的星级判断标准，剩余时间在【0-60】1星,【60-90】2星，【90以上】3星:"60,90"
     */
    public static ReadIntegerArray WuxingCloneNum;
    /**
     * 宗派中，初始的宗派仓库格子数量，注必须是6的倍数。:240
     */
    public static int Guild_store_cell = 240;
    /**
     * 仙魄背包格子数量最大上限:500
     */
    public static int Immortal_soul_backpack_max = 500;
    /**
     * 宗派战力计算的人数限制:10
     */
    public static int GuildFightLimit = 10;
    /**
     * 初始的主题、头像、气泡ID:"100,200,400"
     */
    public static ReadIntegerArray First_Fashion1;
    /**
     * 宗派宗主几天不上线，自动向成员发送邮件提示即将开启自动弹劾:4
     */
    public static int GuildAutoImpeachTime = 4;
    /**
     * 宗派改名道具id:900
     */
    public static int GuildChangeNameItem = 900;
    /**
     * 寻宝每日购买上限:10000
     */
    public static int TreasureTimes = 10000;
    /**
     * 达到指定等级弹出首充引导（如不需要这个条件，配0即可）:"0.0"
     */
    public static ReadIntegerArray RechargeAwardLevel;
    /**
     * 完成某个主线任务弹出首充引导（如不需要这个条件，配0即可）第一个弹出小tips，第二个弹出首充主界面:"990141,1}990287,2"
     */
    public static ReadIntegerArrayEs RechargeAwardTaskid;
    /**
     * 寻宝服务器记录:50
     */
    public static int TreasureServer = 50;
    /**
     * 寻宝个人记录:30
     */
    public static int TreasurePepole = 30;
    /**
     * 交易中，最大的上架位置，最好是双数,拍卖行上架数量上限:20
     */
    public static int Trade_maxitem = 20;
    /**
     * 交易中，密码的最大位数，密码位数必须等于它:4
     */
    public static int Trade_cypher_maxnum = 4;
    /**
     * 战斗力压制比下限，1血怪物中，玩家对怪物的伤害压制下限，除以10000:10000
     */
    public static int Damage_FightFixpressMin = 10000;
    /**
     * 战斗力压制比下限，1血怪物中，玩家对怪物的伤害压制上限，除以10000:10000
     */
    public static int Damage_FightFixpressMax = 10000;
    /**
     * 服务器仓库容量:50
     */
    public static int ServerStoreCell = 50;
    /**
     * 服务器仓库中固定放入的道具:"10001,20000,1"
     */
    public static ReadIntegerArray ServerStoreItem;
    /**
     * 服务器仓库捐献装备品质（品质_是否带星）:"4,1"
     */
    public static ReadIntegerArray ServerStoreDonateEquipStar;
    /**
     * 服务器仓库捐献装备品阶（阶数）:3
     */
    public static int ServerStoreDonateEquipLevel = 3;
    /**
     * 境界相关的经验掉落频率（单位毫秒）:5000
     */
    public static int StateVipExpDropTime = 5000;
    /**
     * 新手村，玩家进入游戏后默认的地图ID，mapid:102599
     */
    public static int NewPlayerFirstMap = 102599;
    /**
     * 特殊版本中给战斗力前10玩家的特殊称号，每个小时给1次，限时10分钟对应排名1-10:"70001,70011,70021,70002,70012,70022,70003,70013,70023,70003"
     */
    public static ReadIntegerArray Special_Title_Fight;
    /**
     * 交易中，上架1次的时间，单位小时:24
     */
    public static int Trade_maxtime = 24;
    /**
     * 新手村，玩家进入游戏后默认的地图坐标:"161,20"
     */
    public static ReadIntegerArray NewPlayerFirstCoordinate;
    /**
     * 拍卖行上架数量上限:20
     */
    public static int Trade_maxrecord = 20;
    /**
     * 背包小于等于该数量时弹出背包不足的提示:10
     */
    public static int Bag_Not_Enough = 10;
    /**
     * boss之家的起始地图:5100
     */
    public static int BossHomeFirstMap = 5100;
    /**
     * 勇者之巅每一层需要达到的积分:"1,2,3,4,5,7,9,11,12"
     */
    public static ReadIntegerArray BravePeakScoreLimit;
    /**
     * 服务器改名字符长度:"2,14"
     */
    public static ReadIntegerArray ChangeServerNameLong;
    /**
     * itemID:1016
     */
    public static int ChangeServerNameCost = 1016;
    /**
     * 境界经验地图回城的地图id:1003
     */
    public static int Realm_Exp_Map_CityID = 1003;
    /**
     * 境界压制中，攻击方-防守方境界获得的值，每大于1提升多少伤害，单位万分比，10000表示万分万:20000
     */
    public static int Damage_StatepressHigh = 20000;
    /**
     * 境界压制中，攻击方-防守方境界获得的值，每小于1减少多少伤害，单位万分比，2000表示万分2000:2000
     */
    public static int Damage_Statepresslow = 2000;
    /**
     * 境界压制中，攻击方-防守方境界获得的值，大于1时最高的伤害提升万分比，增加的极限值:60000
     */
    public static int Damage_StatepressMax = 60000;
    /**
     * 境界压制中，攻击方-防守方境界获得的值，小于1时最高的伤害降低万分比，减少的极限值:8000
     */
    public static int Damage_StatepressMin = 8000;
    /**
     * 世界BOSS增加收益次数的道具:1011
     */
    public static int World_Boss_Count_Item = 1011;
    /**
     * 神兽岛增加收益次数的道具:1012
     */
    public static int Soul_Beasts_Count_Item = 1012;
    /**
     * 副本结算面板，倒计时离开副本，单位秒:10
     */
    public static int Fb_leave_auto = 10;
    /**
     * 神兵引导界面点击跳转的界面:1215000
     */
    public static int Godweapon_panel = 1215000;
    /**
     * 初始自带的被动技能的主动效果:"71001,71002,71003,71004,71005,71006"
     */
    public static ReadIntegerArray Create_Use_Skill;
    /**
     * 服务器阶段_当前跨服最低开服时间； 注：1服，2服，4服，8服（废弃）:"1,1}2,6}4,11}8,21"
     */
    public static ReadIntegerArrayEs Cross_Match_WroldLv;
    /**
     * 熔炼的时候，默认的选中效果，品质，阶数和星级，表示当前及以下，比如2阶表示2阶及以下:"7,6,2"
     */
    public static ReadIntegerArray Smelt_equip_order;
    /**
     * 熔炼的时候，不同数量的道具给与的表现，3种强度的表现:"5,10,20"
     */
    public static ReadIntegerArray Smelt_equip_num;
    /**
     * 初始赠送的法宝化形ID:"6100001.0"
     */
    public static ReadIntegerArray Create_fabao;
    /**
     * 邮件缓存数量:999
     */
    public static int Mail_Caches_Num = 999;
    /**
     * 熔炼中，开启远熔炼的境界等级，到达该境界后，可进行远程熔炼:3
     */
    public static int Smelt_equip_distance_open = 3;
    /**
     * 熔炼中，找的npcID:30047
     */
    public static int Smelt_equip_npc = 30047;
    /**
     * 特殊境界无限层副本，击杀boss后，倒计时踢出玩家的时间,单位秒:15
     */
    public static int Special_fb_time = 15;
    /**
     * 世界boss出生前多少秒提示玩家:5
     */
    public static int World_Boss_Tips_Time = 5;
    /**
     * 世界BOSS玩法点击退出之后的目标地图:102599
     */
    public static int Boss_Exit_map = 102599;
    /**
     * 熔炼中，自动熔炼需要的VIP等级:2
     */
    public static int Smelt_equip_auto_level = 2;
    /**
     * 世界BOSS的排名次数的默认恢复一点的时间间隔【秒】:"1200,1"
     */
    public static ReadIntegerArray World_Boss_Count_Time;
    /**
     * boss之家需要的最低排名:30
     */
    public static int BossHomeNeedRank = 30;
    /**
     * 跨服世界boss场景中，踢人的时间点，在下一场开始前多少分钟踢人:10
     */
    public static int Manor_boss_clean = 10;
    /**
     * 跨服世界boss，每日的怒气最大值:250
     */
    public static int Manor_boss_maxangr = 250;
    /**
     * 仙缘系统-像同一玩家求婚的CD时间，CD时间内给提示，单位【秒】:30
     */
    public static int Marry_other_cd = 30;
    /**
     * 仙缘系统-被求婚方收到msg后，不处理自动拒绝的CD时间，单位【秒】:30
     */
    public static int Marry_refuse_cd = 30;
    /**
     * 跨服世界boss，每增加一个服击杀结界boss，给未击杀的服增加对结界boss的输出buff效果:71
     */
    public static int Manor_boss_buff = 71;
    /**
     * 八极阵图中每造成X点伤害给予玩家Y点积分:"50000,1"
     */
    public static ReadIntegerArray Eight_City_Count_Boss;
    /**
     * 八极阵图中每击杀其他服务的玩家获得的积分:10
     */
    public static int Eight_City_Count_PK = 10;
    /**
     * 跨服世界boss，结算的时间点，从进入跨服活动开启计时，无论当前是否是boss还是采集阶段，分钟:40
     */
    public static int Manor_boss_result = 40;
    /**
     * 跨服世界boss，每一轮结算给的奖励，itemid:81001
     */
    public static int Manor_boss_rew = 81001;
    /**
     * 大能遗府的怪物的特殊BUFF:"80093,80094,80095,80096"
     */
    public static ReadIntegerArray Star_Clone_monster_buff;
    /**
     * 拍卖行上架后的剩余时间,倒计时结束下架(秒):28800
     */
    public static int Auction_Shelf_time = 28800;
    /**
     * 拍卖行个人开拍的倒计时,结束开始拍卖(秒):120
     */
    public static int Auction_countdown = 120;
    /**
     * 套装BOSS小怪天谴值:3
     */
    public static int World_Boss1_parm1 = 3;
    /**
     * 套装BOSS每60秒获得天谴值:1
     */
    public static int World_Boss1_parm2 = 1;
    /**
     * 套装BOSS天谴值满值:100
     */
    public static int World_Boss1_parm3 = 100;
    /**
     * 套装BOSS进入所需道具ID:1018
     */
    public static int World_Boss1_parm4 = 1018;
    /**
     * 套装BOSS进入所需道具蓝钻价值:80
     */
    public static int World_Boss1_parm5 = 80;
    /**
     * 套装BOSS每天第一次进入消耗道具数量:1
     */
    public static int World_Boss1_parm6 = 1;
    /**
     * 套装BOSS每天后续进入增加消耗数量:1
     */
    public static int World_Boss1_parm7 = 1;
    /**
     * 宝石BOSS小怪天谴值:3
     */
    public static int World_Boss2_parm1 = 3;
    /**
     * 宝石BOSS每60秒获得天谴值:1
     */
    public static int World_Boss2_parm2 = 1;
    /**
     * 宝石BOSS天谴值满值:100
     */
    public static int World_Boss2_parm3 = 100;
    /**
     * 宝石BOSS进入所需道具ID:1019
     */
    public static int World_Boss2_parm4 = 1019;
    /**
     * 宝石BOSS进入所需道具蓝钻价值:80
     */
    public static int World_Boss2_parm5 = 80;
    /**
     * 宝石BOSS每天第一次进入消耗道具数量:1
     */
    public static int World_Boss2_parm6 = 1;
    /**
     * 宝石BOSS每天后续进入增加消耗数量:1
     */
    public static int World_Boss2_parm7 = 1;
    /**
     * 拍卖行个人,公会,世界交易记录的数量上限:20
     */
    public static int  Auctioneer_Records_Number = 20;
    /**
     * 离线挂机道具掉落时间间隔,作用于普通掉落和任务掉落,单位[秒]:1800
     */
    public static int OnHook_drop_time_interval = 1800;
    /**
     * 世界篝火添柴消耗：道具ID_数量_篝火经验增加(废弃):"1,500,50"
     */
    public static ReadIntegerArray World_Bonfire_Wood_cost;
    /**
     * 世界篝火等级：篝火等级_升级经验_加成百分比(废弃):"0,50,100}1,50,110}2,50,120}3,50,130}4,50,140}5,50,150}6,50,160}7,50,170}8,50,180}9,50,190}10,50,200"
     */
    public static ReadIntegerArrayEs World_Bonfire_Fire_level;
    /**
     * 世界篝火最大等级:10
     */
    public static int World_Bonfire_Fire_level_max = 10;
    /**
     * 世界篝火添柴阶段时间:300
     */
    public static int World_Bonfire_Wood_time = 300;
    /**
     * 划拳阶段时间：准备阶段_秒数;选择阶段_秒数;最终结算离开阶段_秒数:"1,5}2,10}3,5"
     */
    public static ReadIntegerArrayEs World_Bonfire_Stage;
    /**
     * 划拳匹配冷却时间：秒:10
     */
    public static int World_Bonfire_Game_Match_wait = 10;
    /**
     * 划拳酒量:3
     */
    public static int World_Bonfire_Game_Hp = 3;
    /**
     * 划拳奖励：道具ID_数量_绑定:"9,100,1"
     */
    public static ReadIntegerArray World_Bonfire_Game_Reward;
    /**
     * 划拳奖励目标：划拳胜利_数量;参与划拳_数量:"1,3"
     */
    public static ReadIntegerArray World_Bonfire_Game_Aim;
    /**
     * 经验x秒一跳:3
     */
    public static int World_Bonfire_Exp_Interval = 3;
    /**
     * 划拳机器人模型：职业_modelID:"0,11"
     */
    public static ReadIntegerArray World_Bonfire_Game_Robot_model;
    /**
     * 头像参数:0
     */
    public static int World_Bonfire_Game_Robot_head = 0;
    /**
     * 世界篝火划拳阶段总时间:600
     */
    public static int World_Bonfire_Game_Time = 600;
    /**
     * 世界篝火划拳每局最大时间（5+10+5）*10+10:210
     */
    public static int World_Bonfire_Game_Round_Time = 210;
    /**
     * 宠物升级需要的物品ID_对应的经验:"16001,500}16002,1500}16003,5000"
     */
    public static ReadIntegerArrayEs Pet_Levelup_Item_Num;
    /**
     * 世界篝火划拳等待x秒后匹配机器人:10
     */
    public static int World_Bonfire_Game_Robot_Time = 10;
    /**
     * BOSS之家固定的8个刷怪坐标:"32,106}92,106}62,126}20,76}38,34}62,15}88,52}115,78"
     */
    public static ReadIntegerArrayEs Boss_Home_parm1;
    /**
     * BOSS之家刷怪的个数:8
     */
    public static int Boss_Home_parm2 = 8;
    /**
     * BOSS之家刷新怪物的初始秒（距离凌晨）_间隔:"0,7200"
     */
    public static ReadIntegerArray Boss_Home_parm3;
    /**
     * 挚友组织改名道具_消耗:"1007,1"
     */
    public static ReadIntegerArray Newguild_change_name_item;
    /**
     * 挚友组织邀请的时间间隔（秒）:60
     */
    public static int Newguild_inveterate_CD = 60;
    /**
     * 挚友助战助战机器人的BUFF:"80017.0"
     */
    public static ReadIntegerArray Newguild_BUFF;
    /**
     * 挚友组织邀请的自动拒绝时间（秒）:"30.0"
     */
    public static ReadIntegerArray Newguild_inveterate_Time;
    /**
     * 挚友组织的4个图标资源:"tex,changejob,bg3}tex,changejob,bg4}tex,changejob,bg5}tex,changejob,bg2"
     */
    public static ReadStringArrayEs Newguild_pic;
    /**
     * 每日进入境界boss的最大次数:3
     */
    public static int StateVipBossEnterCount = 3;
    /**
     * 购买一次境界boss次数消耗的道具:"1011,3"
     */
    public static ReadIntegerArray BuyStateVipBossEnterCount;
    /**
     * 挚友公告修改需要的挚友等级:3
     */
    public static int NewGuild_NoticeLimit = 3;
    /**
     * 圣魂的对应物品_数量；:"1025,300}1026,300}1027,300}1028,300"
     */
    public static ReadIntegerArrayEs Equip_Holy_att_item;
    /**
     * 神兵主面板的职业_移动_旋转_缩放；:"0,-0.45,0.15,0,-45,90,90,320,320,320}1,-0.45,0.15,0,-45,90,0,320,320,320}"
     */
    public static ReadIntegerArrayEs Godweapon_main_camera;
    /**
     * 圣装的对应位置的开启等级（废弃）:"60,60,60,60,60,60,60,60,60,60,60"
     */
    public static ReadIntegerArray Equip_Holy_Open_Level;
    /**
     * 挚友助战存在的时间:30
     */
    public static int Newguild_BOSS_Clone_Times = 30;
    /**
     * 挚友助战的机器人数量:3
     */
    public static int Newguild_BOSS_Clone_Num = 3;
    /**
     * 圣装背包的数量:200
     */
    public static int Equip_Holy_bag = 200;
    /**
     * 活跃度和掌门传道时间的换算；1活跃点对应的秒数:3
     */
    public static int Leader_Preach_change = 3;
    /**
     * 掌门传道中，X秒获得一次经验。:1
     */
    public static int Leader_Preach_time = 1;
    /**
     * 圣装衣物套装的需求的装备位置:"11,12,13,14,15,16"
     */
    public static ReadIntegerArray Equip_Holy_suit_type1;
    /**
     * 圣装首饰套装的需求的装备位置:"17,18,19,20,21"
     */
    public static ReadIntegerArray Equip_Holy_suit_type2;
    /**
     * 装备融炼引导检测 引导ID_任务ID:"8,99009"
     */
    public static ReadIntegerArray Equip_GuideSmelt;
    /**
     * 装备洗练前4条的开启条件, 1条_自身装备星级;2条_自身装备星级;3条_自身装备星级;4条_自身装备星级:"1,0}2,0}3,0}4,0"
     */
    public static ReadIntegerArrayEs Equip_washing_conditions_Num_4;
    /**
     * 装备洗练第5条的开启条件, 5条_VIP等级:"5,5"
     */
    public static ReadIntegerArray Equip_washing_conditions_Num_5;
    /**
     * 离线挂机后背包内有的话自动使用道具的id:"1031,1004"
     */
    public static ReadIntegerArray OnHook_automatic_addtime;
    /**
     * 活跃度上线：等级_上限；等级_上限；:"1,380"
     */
    public static ReadIntegerArrayEs EveryDay_Activity_Point_Max;
    /**
     * 天之禁地的每层的人数上限:10
     */
    public static int TZZD_max_limit = 10;
    /**
     * 神魔战场的人数上限:9
     */
    public static int SMZC_max_limit = 9;
    /**
     * 世界篝火划拳加载双方模型的旋转参数：左边x_y_z;右边x_y_z:"0,0.11,0,0,-80,0,150}0,0.11,0,0,60,0,150"
     */
    public static ReadIntegerArrayEs World_Bonfire_Game_Model;
    /**
     * 世界篝火划拳：待机动作;出拳动作;喝酒动作:"huaquan1}huaquan2}drink"
     */
    public static ReadStringArrayEs World_Bonfire_Game_Animation;
    /**
     * 世界聊天的间隔时间（毫秒）:15000
     */
    public static int World_Chat_Interval = 15000;
    /**
     * 私聊的间隔时间（毫秒）:3000
     */
    public static int Private_Chat_Interval = 3000;
    /**
     * 有奖问卷奖励；物品类型_职业_物品ID_数量_绑定状态:"1,-1,1017,50,1}1,-1,60002,20,1"
     */
    public static ReadIntegerArrayEs Question_Reward;
    /**
     * 有奖问卷开启时间，年-月-日_时:分:秒（是公共服时间，不是个人服时间）yyyy-MM-ddHH:mm:ss:"2020-06-0100:00:00,2020-06-3023:59:59"
     */
    public static ReadStringArray Question_Start_Time;
    /**
     * 掌门传道中的客户端移动的坐标:"14,15}16,17}19,17}22,14}22,11}20,9}16,9}15,12}11,12}11,16}12,19}15,20}19,21:22,20}25,17}26,13}24,9}21,7}15,7}12,10"
     */
    public static ReadIntegerArrayEs Leader_Preach_X_Y;
    /**
     * 仇人上限数量:30
     */
    public static int Enemy_Limit = 30;
    /**
     * 挚友助战的机器人数量:80060
     */
    public static int Newguild_BOSS_BUFF = 80060;
    /**
     * 福利-银元宝祈福免费次数（鸿蒙悟道）:1
     */
    public static int Spirit_Stones_Pray_Free = 1;
    /**
     * 挚友公告的最大字数限制:50
     */
    public static int Newguild_Max_notice = 50;
    /**
     * 挚友名字的最大字数限制:16
     */
    public static int Newguild_Max_name = 16;
    /**
     * 心法最高等级:800
     */
    public static int Mental_MaxLevel = 800;
    /**
     * 开服狂欢装备星级阶数限制，大于等级:6
     */
    public static int Sever_Crazy_Limit_Equp = 6;
    /**
     * 心法切换时间:30000
     */
    public static int Mental_Switch_CD = 30000;
    /**
     * 心法持续时间:30000
     */
    public static int Mental_Hold_Time = 30000;
    /**
     * 天墟战场（混沌之境）怒气上限:170
     */
    public static int Universe_Anger_Limit = 170;
    /**
     * VIP周常货币Icon:13
     */
    public static int VIPWeekMoneyIcon = 13;
    /**
     * VIP货币Icon:274
     */
    public static int VIPMoneyIcon = 274;
    /**
     * 自动关注世界BOSS的等级:"180.0"
     */
    public static ReadIntegerArray Boss_attent_notice_level;
    /**
     * 资源找回：活跃值找回比例（完美找回_部分找回）:"1,0.8"
     */
    public static ReadFloatArray RetrieveRes_Activity_Scale;
    /**
     * 仙盟开启提示自动关闭时间（秒）:20
     */
    public static int GuildTips_AutoCloseTime = 20;
    /**
     * VIP等级免费扫荡开启条件（VIP等级_角色等级）:"4,300"
     */
    public static ReadIntegerArray VIP_Wipe_Out_Free;
    /**
     * 仙盟旗帜图标:"1,2,3,4,5,6"
     */
    public static ReadIntegerArray Guild_Icon;
    /**
     * 拍卖行展示关注间隔时间（单位秒）:60
     */
    public static int Auction_ShowCare_IntervalTime = 60;
    /**
     * 仙盟等级buff:"80501_1,80501_2,80501_3,80501_4,80501_5,80501_6,80501_7,80501_8,80501_9,80501_10"
     */
    public static ReadIntegerArray Guild_level_buff;
    /**
     * 仙盟资金扣费失败第4次后，8点的时候解散仙盟:"4,8"
     */
    public static ReadIntegerArray Guild_disband_notice;
    /**
     * 仙盟工资，2小时挂机卡1个:"1031,1"
     */
    public static ReadIntegerArray Guild_wage;
    /**
     * 仙盟创建时获取的第一次资金:3000
     */
    public static int Guild_funds = 3000;
    /**
     * 仙盟一键招人cd5分钟:300
     */
    public static int Guild_Recruit_BbSub_cd = 300;
    /**
     * 首次仙盟战评级时间（星期_时_分_秒）废弃:"3,19,0,0"
     */
    public static ReadIntegerArray Guild_War_First_Rank;
    /**
     * 常规仙盟战评级时间（星期_时_分_秒）废弃:"7,19,0,0"
     */
    public static ReadIntegerArray Guild_War_Rank;
    /**
     * 进入战场时间（时_分_秒）；进入战场时间必定和仙盟评级时间在同一天废弃:"20,0,0"
     */
    public static ReadIntegerArray Guild_War_Enter;
    /**
     * 仙盟战开始几分钟后不允许进入（0就是不限制，随时进入）:5
     */
    public static int Is_Halfway_Join = 5;
    /**
     * 仙盟战开始前的准备时间（秒）:180
     */
    public static int Guild_War_Ready = 180;
    /**
     * 从入场开始计时，活动整体时长（秒）（包含准备时间在内的总时长）:1800
     */
    public static int Guild_War_All_Time = 1800;
    /**
     * 第一场攻守方判定最长时间（秒）:480
     */
    public static int Guild_War_First_Time = 480;
    /**
     * 仙盟申请上限:50
     */
    public static int Guild_Application_cap = 50;
    /**
     * 一键申请的个数:50
     */
    public static int Guild_One_click_application = 50;
    /**
     * 仙盟日常免费刷新次数:4
     */
    public static int Guild_task_refresh_free = 4;
    /**
     * 仙盟日常刷新消耗(第一次道具_数量；第二次道具_数量；达到最后一次后则取最后一次的值）:"1,10}2,10"
     */
    public static ReadIntegerArrayEs Guild_task_refresh_cost;
    /**
     * 仙盟日常每次显示个数:6
     */
    public static int Guild_task_daily_display = 6;
    /**
     * 仙盟日常的品级几率:"1,5840}2,3500}3,500}4,160"
     */
    public static ReadIntegerArrayEs Guild_task_quality_chance;
    /**
     * 仙盟日常协助奖励:"9,100}3,10000"
     */
    public static ReadIntegerArrayEs Guild_task_assist_reward;
    /**
     * 仙盟福地关闭时间天数（正式数值5天）:2
     */
    public static int Guild_blessing_time = 2;
    /**
     * 仙盟贡献和资金的比例（获取贡献的时候同时获得仙盟资金）:1
     */
    public static int Guild_contribution_funds = 1;
    /**
     * 仙盟战点赞奖励（双方都增加）:"11,15,1,9"
     */
    public static ReadIntegerArray Guild_War_Praise_Reward;
    /**
     * 仙盟战仙盟奖励预览（废弃）:"19007,1,1,9}19008,1,1,9"
     */
    public static ReadIntegerArrayEs Guild_War_Guild_Preview;
    /**
     * 仙盟战个人奖励预览（废弃）:"11,1,1,9"
     */
    public static ReadIntegerArrayEs Guild_War_Personal_Preview;
    /**
     * 仙盟战出生点空气墙ID（用于控制开关空气墙）:"DynamicBlocker1,DynamicBlocker2,DynamicBlocker3,DynamicBlocker7"
     */
    public static ReadStringArray Guild_War_Born_Air_Wall;
    /**
     * 仙盟日常刷新cd（秒）不显示给玩家:1
     */
    public static int Guild_task_refresh_cd = 1;
    /**
     * 仙盟战击杀玩家获得积分数量:50
     */
    public static int Guild_War_Kill_Point = 50;
    /**
     * 仙盟战守方每X秒增加Y积分（0表示不会自然增加）:"10,50"
     */
    public static ReadIntegerArray Guild_War_Defense_Point_Add;
    /**
     * 福地boss刷新时间，00:30、09:00、12:30、16:00，19:00，22:00点刷新（单位分钟）【废弃】:"30,540,750,960,1140,1320"
     */
    public static ReadIntegerArray GuildBattleBoss_refresh_cd;
    /**
     * 仙盟拍卖收益最大的上限：物品10%的价值:10
     */
    public static int Guild_auction_earnings_cap = 10;
    /**
     * 仙盟拍卖税率：5%:10
     */
    public static int Guild_auction_earnings_tariff = 10;
    /**
     * 仙盟福地重新分配时间（分钟）【距0点得分钟数，可配置多次刷新】:"0,480,960"
     */
    public static ReadIntegerArray GuildBattleBoss_Openingtime;
    /**
     * 无限层关闭的时间02:00-10:00:"120,600,1510,1546,1547,1548,1549"
     */
    public static ReadIntegerArray Boss_Unlimit_Open_Time;
    /**
     * boss特殊展示宝箱ID:"81014,81015,81016,81017,81018,81019,81020,81021,81022,81023,81024,81025,81026,81027,81028,81029,81030,81031,81032,82001,82002,82003,82004,82005,82006,82007,82008,82009,82010,82011,82012,82013,82014,82015,82016,82017,82018,82019,82020,82021,82022,82023,82024,82025,82026,82027,82028,82029,82030,83001,83002,83003,83004,83005,83006,83007,83008,83009,83010,83011,83012,83013,83014,83015,83016,83017,83018,83019,83020,83021,83022,83023,83024,83025,83026,83027,83028,83029,83030,83031,83032,83033,83034,83035,83036,83037,83038,83039,83040,83041,83042,83043,83044,83045,83046,83047,83048,81041,81042,81043,81044,81045,81046,81047,81048,81049,81050,2125,1119,82031,82032,82033,82034,82035,82036,82037,82038,82039,82040,82041,82042,82043,82044,82045,82046,82047,82048,82049,82050,82051,82052,82053,82054,82055,82056,82057,82058,82059,82060,82061,82062,82063,82064,82065,82066,82067,82068,82069,82070,82071,82072,82073,82074,82075,82076,82077,82078,82079,82080,82081,82082,82083,82084,82085,82086,82087,82088,82089,82090,82091,82092,82093,82094,82095,82096,82097,82098,82099,82100,82101,82102,82103,82104,82105,82106,82107,82108,82109,82110,82111,82112,82113,82114,82115,82116,82117,82118,82119,82120,82121,82122,82123,82124,82125,82126,82127,82128,82129,82130,82131,82132,82133,82134,82135,82136,82137,82138,82139,82140,82141,82142,82143,82144,82145,82146,82147,82148,82149,82150,82151,82152,82153,82154,82155,82156,82157,82158,82159,82160,82161,82162,82163,82164,82165,82166,82167,82168,82169,82170,82171,82172,82173,82174,82175,82176,82177,82178,82179,82180,82181,82182,82183,82184,82185,82186,82187,82188,82189,82190,82191,82192,82193,82194,82195,82196,82197,82198,82199,82200,82201,82202,82203,82204,82205,82206,82207,82208,82209,82210,82211,82212,82213,82214,82215,82216,82217,82218,82219,82220,82221,82222,82223,82224,82225,82226,82227,82228,82229,82230,82231,82232,82233,82234,82235,82236,82237,82238,82239,82240,82241,82242,82243,82244,82245,82246,82247,82248,82249,82250,82251,82252,82253,82254,82255,82256,82257,82258,82259,82260,82261,82262,82263,82264,82265,82266,82267,82268,82269,82270,82271,82272,82273,82274,82275,82276,82277,82278,82279,82280,82281,82282,82283,82284,82285,82286,82287,82288,82289,82290,82291,82292,82293,82294,82295,82296,82297,82298,82299,82300,82301,82302,82303,82304,82305,82306,82307,82308,82309,82310,82311,82312,82313,82314,82315,82316,82317,82318,82319,82320,82321,82322,82323,82324,82325,82326,82327,82328,82329,82330,82331,82332,82333,82334,82335,82336,82337,82338,82339,82340,82341,82342,82343,82344,82345,82346,82347,82348,82349,82350,82351,82352,82353,82354,82355,82356,82357,82358,82359,82360,82361,82362,82363,82364,82365,82366,82367,82368,82369,82370,82371,82372,82373,82374,82375,81100,81101,81102,81103,1305,82384,82385,82386,82387,82388,82389,82390,82391,82392,83105,83106,83111,83112,83113,83115,83116,83117,83118,83121,83122,83123,83124,83127,83128,83129,83130"
     */
    public static ReadIntegerArray Boss_Special_Gift;
    /**
     * 仙盟BOSS的鼓舞次数：金币鼓舞_元宝鼓舞:"3,5"
     */
    public static ReadIntegerArray Guild_Growup_Time;
    /**
     * 仙盟金币鼓舞的消耗:"20000,30000,50000"
     */
    public static ReadIntegerArray Guild_Gold_Growup_Cost;
    /**
     * 仙盟元宝鼓舞的消耗:"5,5,10,10,20"
     */
    public static ReadIntegerArray Guild_YB_Growup_Cost;
    /**
     * 每一次鼓舞增加的总伤害的万分比:500
     */
    public static int Guild_Growup_Add = 500;
    /**
     * 仙盟BOSS用的怪物ID_x坐标_y坐标:"25001,23,20"
     */
    public static ReadIntegerArray Guild_Boss_ID;
    /**
     * 仙甲的获取途径:2015
     */
    public static int Xinajia_get = 2015;
    /**
     * 仙佩的获取途径:2016
     */
    public static int Xianpei_get = 2016;
    /**
     * 副装的获取途径:2017
     */
    public static int Fuzhuang_get = 2017;
    /**
     * 仙甲碎片的获取途径:2018
     */
    public static int Xianjia_Item_get = 2018;
    /**
     * 2个假无限层中增加的BUFFID:1000036
     */
    public static int Boss_Unlimit_Buff = 1000036;
    /**
     * 仙盟BOSS的开启时间:"720,730}1200,1210"
     */
    public static ReadIntegerArrayEs Guild_Boss_Time;
    /**
     * 仙盟BOSS用的怪物ID_x坐标_y坐标:"25002,24,15}25002,29,21}25002,23,27}25002,18,21"
     */
    public static ReadIntegerArrayEs Guild_monster_ID;
    /**
     * 仙甲寻宝寻宝次数上限；单轮时间（小时）；轮数上限:"1600,168,8"
     */
    public static ReadIntegerArray Xinajia_Hunt_pra;
    /**
     * 一血怪怪物战斗压制比大于值（放大100倍）：300:200
     */
    public static int Damage_MonsterPveNum5 = 200;
    /**
     * 一血怪怪物战斗压制比上限（放大100倍）：300 :200
     */
    public static int Damage_MonsterPveNum6 = 200;
    /**
     * 一血怪怪物战斗压制比小于等于值（放大100倍）：100 :50
     */
    public static int Damage_MonsterPveNum7 = 50;
    /**
     * 一血怪怪物战斗压制比下限（放大100倍）：100:50
     */
    public static int Damage_MonsterPveNum8 = 50;
    /**
     * 寻宝N次增加秘宝抽奖次数:200
     */
    public static int HuntNumAddSecret = 200;
    /**
     * 秘宝可抽奖个数上限:8
     */
    public static int SecretHuntLimit = 8;
    /**
     * 每一次鼓舞获得的物品:"9,20"
     */
    public static ReadIntegerArray Guild_Growup_Add_item;
    /**
     * 基础灵力值:"61,0}59,0}60,0"
     */
    public static ReadIntegerArrayEs Base_LingLi_att;
    /**
     * 福地boss求援cd（秒）:10
     */
    public static int GuildBattlebosshelp = 10;
    /**
     * 等级上限1_提示1;等级上限2_提示2;:"160,等级已达上限，需突破境界才能继续升级(超出的经验可以存储)}220,等级已达上限，需突破境界才能继续升级(超出的经验可以存储)}300,等级已达上限，需突破境界才能继续升级(超出的经验可以存储)}370,等级已达上限，需突破境界才能继续升级(超出的经验可以存储)}480,等级已达上限，需突破境界才能继续升级(超出的经验可以存储)}600,等级已达上限，需突破境界才能继续升级(超出的经验可以存储)"
     */
    public static ReadStringArrayEs Level_Limit_Notice;
    /**
     * 福地击杀玩家获得的个人积分_个人积分上限:"10,200"
     */
    public static ReadIntegerArray GuildBattlebosspvp;
    /**
     * 灵气脱战后每秒回复的万分比:500
     */
    public static int Lingli_nofighting_recovery_num = 500;
    /**
     * 灵气战斗中回满的时间（秒）:50
     */
    public static int Lingli_fighting_recovery_time = 50;
    /**
     * 初始的仙甲的icon配置（职业_部位_装备）:"0,30,5000005}0,31,5000015}0,32,5000025}0,33,5000035}0,34,5000041}0,35,5000051}0,36,5000061}0,37,5000071}0,38,5000081}0,39,5000091}0,40,5000101}0,41,5000111}0,42,5000121}0,43,5000131}1,30,5100005}1,31,5100015}1,32,5100025}1,33,5100035}1,34,5100041}1,35,5100051}1,36,5100061}1,37,5100071}1,38,5100081}1,39,5100091}1,40,5100101}1,41,5100111}1,42,5100121}1,43,5100131}0,44,5010005}0,45,5010015}0,46,5010025}0,47,5010035}0,48,5010041}0,49,5010051}0,50,5010061}0,51,5010071}0,52,5010081}0,53,5010091}0,54,5010101}0,55,5010111}0,56,5010121}0,57,5010131}1,44,5110005}1,45,5110015}1,46,5110025}1,47,5110035}1,48,5110041}1,49,5110051}1,50,5110061}1,51,5110071}1,52,5110081}1,53,5110091}1,54,5110101}1,55,5110111}1,56,5110121}1,57,5110131}0,401,5000281}0,402,5000291}0,403,5000301}0,404,5000311}0,405,5000321}0,406,5000331}0,407,5000341}0,408,5000351}0,409,5000361}0,410,5000371}1,401,5100281}1,402,5100291}1,403,5100301}1,404,5100311}1,405,5100321}1,406,5100331}1,407,5100341}1,408,5100351}1,409,5100361}1,410,5100371}0,411,5010281}0,412,5010291}0,413,5010301}0,414,5010311}0,415,5010321}0,416,5010331}0,417,5010341}0,418,5010351}0,419,5010361}0,420,5010371}1,411,5110281}1,412,5110291}1,413,5110301}1,414,5110311}1,415,5110321}1,416,5110331}1,417,5110341}1,418,5110351}1,419,5110361}1,420,5110371}0,421,5020281}0,422,5020291}0,423,5020301}0,424,5020311}0,425,5020321}0,426,5020331}0,427,5020341}0,428,5020351}0,429,5020361}0,430,5020371}1,421,5120281}1,422,5120291}1,423,5120301}1,424,5120311}1,425,5120321}1,426,5120331}1,427,5120341}1,428,5120351}1,429,5120361}1,430,5120371}0,431,5030281}0,432,5030291}0,433,5030301}0,434,5030311}0,435,5030321}0,436,5030331}0,437,5030341}0,438,5030351}0,439,5030361}0,440,5030371}1,431,5130281}1,432,5130291}1,433,5130301}1,434,5130311}1,435,5130321}1,436,5320331}1,437,5130341}1,438,5130351}1,439,5130361}1,440,5130371}2,30,5200005}2,31,5200015}2,32,5200025}2,33,5200035}2,34,5200041}2,35,5200051}2,36,5200061}2,37,5200071}2,38,5200081}2,39,5200091}2,40,5200101}2,41,5200111}2,42,5200121}2,43,5200131}2,44,5210005}2,45,5210015}2,46,5210025}2,47,5210035}2,48,5210041}2,49,5210051}2,50,5210061}2,51,5210071}2,52,5210081}2,53,5210091}2,54,5210101}2,55,5210111}2,56,5210121}2,57,5210131}2,401,5200281}2,402,5200291}2,403,5200301}2,404,5200311}2,405,5200321}2,406,5200331}2,407,5200341}2,408,5200351}2,409,5200361}2,410,5200371}2,411,5210281}2,412,5210291}2,413,5210301}2,414,5210311}2,415,5210321}2,416,5210331}2,417,5210341}2,418,5210351}2,419,5210361}2,420,5210371}2,421,5220281}2,422,5220291}2,423,5220301}2,424,5220311}2,425,5220321}2,426,5220331}2,427,5220341}2,428,5220351}2,429,5220361}2,430,5220371}2,431,5230281}2,432,5230291}2,433,5230301}2,434,5230311}2,435,5230321}2,436,5230331}2,437,5230341}2,438,5230351}2,439,5230361}2,440,5230371"
     */
    public static ReadIntegerArrayEs Xinajia_frist_model;
    /**
     * 灵力减伤上限:10000
     */
    public static int Lingli_LingLiJianShang_Max = 10000;
    /**
     * 仙盟拍卖行上架后的剩余时间,倒计时结束下架(秒):1800
     */
    public static int Guild_auction_Shelf_time = 1800;
    /**
     * 仙盟拍卖行开拍的倒计时,结束开始拍卖(秒):120
     */
    public static int Guild_auction_countdown = 120;
    /**
     * 仙盟战召集CD（秒）:60
     */
    public static int Guild_War_CallUp_CD = 60;
    /**
     * 仙盟战首次进入积分数量:10
     */
    public static int Guild_War_Enter_Point = 10;
    /**
     * 占领福地的仙盟，会获得成员人数+20的特权:20
     */
    public static int Guild_Member_Number = 20;
    /**
     * 静默更新功能开启的等级:63
     */
    public static int Update_Start_Level = 63;
    /**
     * 问卷调查的网页连接:"https://www.wjx.cn/jq/81544336.aspx"
     */
    public static ReadStringArrayEs QuestionNaireUrl;
    /**
     * 天墟战场开启时间（以大于等于开服时间为准）:8
     */
    public static int UniverseSeverOpenTime = 8;
    /**
     * 装备合成的装备获取物品ID:2019
     */
    public static int EquipSynthGet = 2019;
    /**
     * 第一个假无限层的人物走上去开始刷怪的坐标:"60,101"
     */
    public static ReadIntegerArrayEs BossFightPoint1;
    /**
     * 第二个假无限层的人物走上去开始刷怪的坐标:"60,101"
     */
    public static ReadIntegerArrayEs BossFightPoint2;
    /**
     * 天墟战场怒气ICON:275
     */
    public static int UniverseNuqiIcon = 275;
    /**
     * 背饰在额外展示中的大小,Y轴旋转,Y轴偏移:"200,0,0"
     */
    public static ReadIntegerArray BeishiPosition;
    /**
     * 天墟战场怒气重置时间（距离0点的分钟数）:"0,1020"
     */
    public static ReadIntegerArray UniverseNuqReset;
    /**
     * 角色延迟删除等级:110
     */
    public static int RoleDelayDeleteLevel = 110;
    /**
     * 服务器阶段_最低开服时间； 注：1服，2服，4服，8服，16服，32服【不可通过配置更改服务器数量】:"1,1}2,3}4,8}8,15"
     */
    public static ReadIntegerArrayEs Cross_Match_OpneTime;
    /**
     * 功能预告每日奖励:"12,100}10001,1}16001,1"
     */
    public static ReadIntegerArrayEs Function_Notice_Daily_Reward;
    /**
     * 是否每日首次登录都弹出玩法预告界面（0否1是）:1
     */
    public static int Is_Show_Function_Notice = 1;
    /**
     * 人数掉血总上限_人数掉血总比例(除100):"1,10"
     */
    public static ReadIntegerArray Number_blood_Max;
    /**
     * 脱战倒计时:6000
     */
    public static int Off_War_Time = 6000;
    /**
     * 下载有礼的奖励,itemid_num_bind:"3,100000,1}12,500,1"
     */
    public static ReadIntegerArrayEs Update_Gift_Reward;
    /**
     * VIP0升级到VIP1的对应人物等级:15
     */
    public static int VIP_Guide_Reward_Level = 15;
    /**
     * 奖池银元宝上限:100000
     */
    public static int Magic_Bowl_Reward_TopLimit = 100000;
    /**
     * 可领取的值达到配置值才显示小红点:500
     */
    public static int Magic_Bowl_Receive_Limit = 500;
    /**
     * 功能预告功能开启天数（奖励领取也共用这个天数），从开服时间开始算起（小于等于配置天数开启并领奖）:30
     */
    public static int Function_Notice_Open_Time = 30;
    /**
     * 掌门传道的BUFF:"80112,80113,80114,80111"
     */
    public static ReadIntegerArray Leader_Preach_Buff;
    /**
     * 首充特权增加的经验（万分比）:500
     */
    public static int First_Rechar_Exp_Add = 500;
    /**
     * 万妖卷：普通通关时间（秒）_完美同过时间（秒）:"45,25"
     */
    public static ReadIntegerArray Wanyaojuan_Finish_Time;
    /**
     * 竞技场开启秒杀次数限制（必须手动X次，才能开启秒杀）:3
     */
    public static int JJC_Auto_Time_Limit = 3;
    /**
     * 天禁之门的通关时间匹配奖励SSS、SS、S、A、B：普通通关时间（秒:"30,45,60,90,180"
     */
    public static ReadIntegerArray Skydoor_Finish_Time;
    /**
     * 天禁之门的SSS、SS、S、A、B匹配的掉率显示:"600,500,300,200,100"
     */
    public static ReadIntegerArray Skydoor_Drop_Num;
    /**
     * 竞拍还剩最后 60 秒时候 增加60秒时间:"60,60"
     */
    public static ReadIntegerArray Auction_AddTime;
    /**
     * 经验祈福次数上限_元宝祈福次数上限（不包含免费次数）:"5,10"
     */
    public static ReadIntegerArray Pray_Time_Limit;
    /**
     * 新手阶段，红色装备特殊提示:"2000250,2001914"
     */
    public static ReadIntegerArray Special_Red_Equip_title;
    /**
     * 新手阶段的假拍引导卖的物品；职业_装备ID:"0,2001913}1,2000249}2,2001913}3,2000249"
     */
    public static ReadIntegerArrayEs Trade_First_Sell_Item;
    /**
     * 新手阶段的假拍引导买的物品；职业_装备ID:"0,2000249}1,2001913}2,2000249}3,2000249"
     */
    public static ReadIntegerArrayEs Trade_First_Buy_Item;
    /**
     * 大能遗府一阶段星级需求的时间 321:"30,45,60"
     */
    public static ReadIntegerArray Daneng_clone_frist_time;
    /**
     * 大能遗府一阶段的BUFF:999999
     */
    public static int Daneng_clone_frist_Buff = 999999;
    /**
     * 大能遗府一阶段的BUFF2:999998
     */
    public static int Daneng_clone_frist_Buff2 = 999998;
    /**
     * 银元宝免费祈福获得的奖励:"12,500"
     */
    public static ReadIntegerArray Pray_Free_Time_Reward;
    /**
     * 神兽岛开启的最少开服天数:5
     */
    public static int Soul_Beasts_Open_Time = 5;
    /**
     * 场景加载异常后返回的场景ID:102599
     */
    public static int Back_Scene_MapId = 102599;
    /**
     * 0元购商城购买权限时间（开服时间天数）超过时间则不可购买，但是可以继续领取奖励:3
     */
    public static int Free_Shop_End_Time = 3;
    /**
     * 拍卖行上架，购买圣装的VIP等级限制（配置格式：上架限制_购买限制）；0代表无限制:"0,0"
     */
    public static ReadIntegerArray Holy_Trade_VIP_Limit;
    /**
     * X点人民币（单位：分）对应Y点VIP经验:"100,10"
     */
    public static ReadIntegerArray VIP_Exp_Source;
    /**
     * 实名认证成功奖励:"12,1000}3,100000"
     */
    public static ReadIntegerArrayEs Name_Certification_Award;
    /**
     * 仙缘系统-结婚所需亲密度:0
     */
    public static int Marry_Need_Love_Point = 0;
    /**
     * 仙缘系统-消费宣言配置（配置格式：货币类型_货币数量_宣言文字数量）:"12,1000,120"
     */
    public static ReadIntegerArray Marry_Information_Chat;
    /**
     * 仙缘系统-婚姻美食采集物列表(副本开始默认刷出的）:"100001,16"
     */
    public static ReadIntegerArrayEs Marry_Food_Gathers;
    /**
     * 仙缘系统-婚姻美食刷新坐标列表（废弃，修改为直接种在场景里面）:""
     */
    public static ReadIntegerArrayEs Marry_Food_Pos;
    /**
     * 仙缘系统-婚姻喜从天降刷新坐标:"81,35}78,32}84,38}82,38}80,40}83,40}85,40}85,42}83,44}80,42}80,45}84,45}81,47}81,50}78,50}75,50}76,47}76,52}72,52}72,48}69,51}69,48}65,49}64,52}72,54}78,53}67,53}62,47}62,44}63,42}65,40}60,40}63,35}63,37}66,31}55,41}56,43}57,41}42,42}44,41}46,43}34,51}36,43}36,47}28,50}33,46}20,42"
     */
    public static ReadIntegerArrayEs Marry_Souger_Pos;
    /**
     * 仙缘系统-婚姻热度进度和操作，0表示刷新喜从天降的id和数量，非0表示刷新的bossid和坐标:"520,0,100004,10}1314,0,100004,10}2234,0,100004,10}3344,50001,32,51"
     */
    public static ReadIntegerArrayEs Marry_Hot_Progress;
    /**
     * 仙缘系统-婚姻喜从天降采集最大次数，根据热度值改变:"520,3}1314,6}3344,9"
     */
    public static ReadIntegerArrayEs Marry_Souger_MaxCount;
    /**
     * 仙缘系统-婚姻美食采集最大次数:10
     */
    public static int Marry_Food_MaxCount = 10;
    /**
     * 仙缘系统-婚姻玩家进入热度值:10
     */
    public static int Marry_Player_EnterHot = 10;
    /**
     * 仙缘系统-婚姻赠送物品配置，物品ID_物品数量_增加热度:"1,9,9}50001,1,1}1,58,58}50002,1,99}1,99,99}50003,1,999"
     */
    public static ReadIntegerArrayEs Marry_Send_Items;
    /**
     * 仙缘系统-强制离婚消耗（货币类型_货币数量）:"1,200"
     */
    public static ReadIntegerArray Force_Divorce_Cost;
    /**
     * 仙缘系统-免费协商离婚时间（小时）:48
     */
    public static int Divorce_free_Time = 48;
    /**
     * 仙缘系统-人数上限，婚宴人数上限（普通婚姻没有婚宴）:30
     */
    public static int Marry_Guest_Limit = 30;
    /**
     * 仙缘系统-购买婚宴人数上限消耗_可购买上限（不包含原有人数；货币类型_货币数量_可购买上限）:"12,1000,10"
     */
    public static ReadIntegerArray Marry_Buy_Guest_Cost;
    /**
     * 仙缘系统-婚宴BOSS被击杀后的奖励:"1017,200,1,9}10001,10,1,9"
     */
    public static ReadIntegerArrayEs Marry_Boss_Reward;
    /**
     * 等级达到后自动勾选设置中的自动释放XP技能:35
     */
    public static int GameSetting_AutoXP_Level = 35;
    /**
     * 玩家初始称号，根据前3把剑灵ID设置:2331
     */
    public static int Player_Initial_Title = 2331;
    /**
     * 机器人(竞技场)战斗压制比大于值（放大100倍）：300:100
     */
    public static int Damage_Robot_PveNum9 = 100;
    /**
     * 机器人(竞技场)战斗压制比上限（放大100倍）：300 :200
     */
    public static int Damage_Robot_PveNum10 = 200;
    /**
     * 机器人(竞技场)战斗压制比小于等于值（放大100倍）：100 :75
     */
    public static int Damage_Robot_PveNum11 = 75;
    /**
     * 机器人(竞技场)战斗压制比下限（放大100倍）：100:75
     */
    public static int Damage_Robot_PveNum12 = 75;
    /**
     * 【VIP免费送】在线送VIP的时间  时间单位：分钟:25
     */
    public static int Free_VIP_Level_Up_Time = 25;
    /**
     * 怪物攻击玩家时，锁定玩家血量百分比，不会死亡（怪物ID_锁血万分比）:"10503,3000"
     */
    public static ReadIntegerArrayEs Monster_Att_player_Hp_Lock;
    /**
     * 成长之路主界面模型展示（废弃）:43400
     */
    public static int New_Sever_Growup_Show_Model = 43400;
    /**
     * 成长之路主界面模型展开启条件:"2,990643"
     */
    public static ReadIntegerArray New_Sever_Growup_Show_Model_Open;
    /**
     * 成长之路主界面模型显示（模型ID_模型倍数_Y轴位置_Y轴旋转）:"6000004,100,0,180}6000004,100,0,180"
     */
    public static ReadIntegerArrayEs New_Sever_Growup_Show_Model_Value;
    /**
     * 每日次数刷新的时间：时刻:5
     */
    public static int Daily_times_reset_time = 5;
    /**
     * 累计充值在无线层第五层弹出（只弹一次），填写clone_map的ID:1510
     */
    public static int Recharge_Convenient_Show = 1510;
    /**
     * 混沌之境指挥官称号ID（对应title表的ID）:40001
     */
    public static int Universe_Command_Title = 40001;
    /**
     * 剑灵阁最大挂机时间 单位秒:86400
     */
    public static int Sword_Soul_MaxMandateTime = 86400;
    /**
     * 剑灵阁单次发奖时间:300
     */
    public static int Sword_Soul_SingleRewordTime = 300;
    /**
     * 转职位面位置信息：地图_出入坐标点_位面坐标点_位面地图:"102599,125,168,120,170,41001"
     */
    public static ReadIntegerArray Change_Job_Finish;
    /**
     * 剑灵阁快速发奖次数:24
     */
    public static int Sword_Soul_quicktimes = 24;
    /**
     * 剑灵阁通关时间（秒）:45
     */
    public static int Sword_Soul_Cross_Time = 45;
    /**
     * 新服活动开启关闭时间（标签ID_开启天数_结束天数，开启天数从0点计算，关闭天数到当天24点截至）:"1,1,3}2,1,3"
     */
    public static ReadIntegerArrayEs New_Sever_Activity_Time;
    /**
     * 灵魄抽奖的特殊次数（单抽的特殊次数_十连抽的特殊次数）:"10,30"
     */
    public static ReadIntegerArray Sword_Soul_chouqu_times;
    /**
     * 初始的灵魄类型最大值:4
     */
    public static int Lingpo_initial = 4;
    /**
     * 副本失败的提升途径:"1210000.0"
     */
    public static ReadIntegerArray Clone_fail_up_way;
    /**
     * 实名认证提示年龄段:"8,16,18"
     */
    public static ReadIntegerArray Certification_Age;
    /**
     * 充值功能，新手礼包时间限制，从功能开启时计时（小时）:168
     */
    public static int Recharge_New_Bie_Gift_Limit = 168;
    /**
     * 一周福利触发幸运抽奖的次数显示:10
     */
    public static int Week_Welfare_Luck_Probability_Limit = 10;
    /**
     * BOSS整层刷新卷：无极墟域物品ID_晶甲和域物品ID_年兽封域物品ID:"1043,1044,1045"
     */
    public static ReadIntegerArray Boss_all_Refresh;
    /**
     * BOSS单个刷新卷：无极墟域物品ID_晶甲和域物品ID_年兽封域物品ID:"1049,1050,1051"
     */
    public static ReadIntegerArray Boss_single_Refresh;
    /**
     * BOSS单个召唤卷：无极墟域物品ID_晶甲和域物品ID_年兽封域物品ID:"1046,1047,1048"
     */
    public static ReadIntegerArray Boss_single_call;
    /**
     * BOSS单个召唤卷的单个BOSS的召唤上限:5
     */
    public static int Boss_single_call_limit = 5;
    /**
     * 仙缘系统-解锁心锁消耗的物品ID（对应Item表）:1053
     */
    public static int Marry_Lock_Unlock_Item = 1053;
    /**
     * 团购自增长天数设定（大于等级配置值，系统才会自增长）:2
     */
    public static int Group_Buy_System_Grow_Limit = 2;
    /**
     * 仙缘系统-情缘副本最终结算的缘分大考验图片配置:"1,100,101,102}2,103,104,105}3,106,107,108"
     */
    public static ReadIntegerArrayEs Marry_Copy_Fate_Test;
    /**
     * 仙甲默认模型（职业_武器_身体_光环_阵道）:"0_6200005_6200006_6200004_6200003,1_6200001_6200002_6200004_6200003,2_6520007_6510013_6200004_6200003,3_6300030_6300031_6300033_6300032"
     */
    public static ReadIntegerArray Xianjia_AllModel;
    /**
     * 仙缘系统-离婚请求CD时间:30
     */
    public static int Divorce_Beg_CD = 30;
    /**
     * 佳偶天成-相亲墙开启时间，按照开服时间计算  开始天数_结束天数:"1,7"
     */
    public static ReadIntegerArray Marry_Wall_OpenTime;
    /**
     * 佳偶天成-相亲墙奖励领取等级:178
     */
    public static int Marry_Wall_Award_Level = 178;
    /**
     * 佳偶天成-相亲墙等级奖励列表 物品ID_物品数量_是否绑定_职业（9通用）:"50001,10,1,9}12,888,1,9"
     */
    public static ReadIntegerArrayEs Marry_Wall_Award_Items;
    /**
     * 佳偶天成-相亲墙发送宣言奖励 物品ID_物品数量_是否绑定:"2125,1,1"
     */
    public static ReadIntegerArray Marry_Wall_Send_Award;
    /**
     * 佳偶天成-相亲墙发送宣言CD时间，单位为分钟:360
     */
    public static int Marry_Wall_Send_CDTime = 360;
    /**
     * 仙甲背包的数量：200:200
     */
    public static int Xianjia_bag_num = 200;
    /**
     * 一周福利抽奖天数（3_7.代表每周三和每周日可抽奖）:"1.0"
     */
    public static ReadIntegerArray Week_Welfare_Draw_Limit;
    /**
     * 藏珍阁抽奖用的物品_数量:"1059,1"
     */
    public static ReadIntegerArray CangZhenGe_need_item;
    /**
     * (藏珍阁的刷新时间：天)_最大轮数:"45,1"
     */
    public static ReadIntegerArray CangZhenGe_Refresh_time;
    /**
     * 晶甲BOSS增加收益次数的道具:1020
     */
    public static int JinJia_Boss_Count_Item = 1020;
    /**
     * 刷新的需要的抽奖次数_终极大奖的最大轮数:"250,1"
     */
    public static ReadIntegerArray Superreward_Refresh_time;
    /**
     * 狂欢周的关闭和开启时间:"0,5"
     */
    public static ReadIntegerArray HappyWeek_OpenTime;
    /**
     * 巅峰竞技活动开启等级:240
     */
    public static int PeakBattle_OpenLevel = 240;
    /**
     * 巅峰竞技活动开启时间:"*-*-*-1-21-00;*-*-*-2-21-00;*-*-*-3-21-00;*-*-*-4-21-00;*-*-*-5-21-00;*-*-*-6-21-00;*-*-*-7-21-00"
     */
    public static String PeakBattle_OpenTime = "*-*-*-1-21-00;*-*-*-2-21-00;*-*-*-3-21-00;*-*-*-4-21-00;*-*-*-5-21-00;*-*-*-6-21-00;*-*-*-7-21-00";
    /**
     * 巅峰竞技活动持续时长分钟(30分钟）:30
     */
    public static int PeakBattle_LastTime = 30;
    /**
     * 匹配等待时间s:120
     */
    public static int PeakBattle_MatchTime = 120;
    /**
     * 进入副本等待战斗倒计时s:3
     */
    public static int PeakBattle_WaitTime = 3;
    /**
     * 副本战斗时间s:180
     */
    public static int PeakBattle_BattleTime = 180;
    /**
     * 副本血量增大百分比n*100%:0.3f
     */
    public static float PeakBattle_BloodPlus = 0.3f;
    /**
     * 三个参与宝箱奖励  参与场次_道具ID_数量【废弃】，配置在peakBattleJoinReward:"1,12,1000}5,3,10000}10,1006,1"
     */
    public static ReadIntegerArrayEs PeakBattle_ThreeBox;
    /**
     * 领排名奖励至少需要参加的场次:20
     */
    public static int PeakBattle_RankJionTime = 20;
    /**
     * 排行榜上榜人数:50
     */
    public static int PeakBattle_RankCount = 50;
    /**
     * 连胜N场为“连胜”:3
     */
    public static int PeakBattle_ContinueWin = 3;
    /**
     * 连负N场为“连负”:3
     */
    public static int PeakBattle_ContinueLost = 3;
    /**
     * 赛季持续时间(天）:7
     */
    public static int PeakBattle_LastDay = 7;
    /**
     * 【泰文版】分享奖励:"1017,50}19021,5}1011,1"
     */
    public static ReadIntegerArrayEs Thai_ShareRewards;
    /**
     * 【泰文版】点赞奖励:"1017,50}19021,5}1011,1"
     */
    public static ReadIntegerArrayEs Thai_LikeRewards;
    /**
     * 幸运翻牌-对应消耗货币类型1个单位可以获得XX点幸运值(可配置多种货币同时消耗换成幸运值）:"1,1"
     */
    public static ReadIntegerArrayEs New_Sever_Luck_Value;
    /**
     * 幸运翻牌-翻牌一次消耗得幸运值:1800
     */
    public static int New_Sever_Luck_Limit = 1800;
    /**
     * 幸运翻牌-奖励配置（item_num_bind_occ_probability,最后个参数为权重），数量必须为9个:"1017,200,1,9,1000}10001,1,1,9,1000}20004,1,1,9,3000}21004,1,1,9,3000}60015,10,1,9,1000}10002,10,1,9,550}60071,150,1,9,200}19007,1,1,9,150}5000051,1,1,0,100}5100051,1,1,1,100}5200051,1,1,2,100}5100051,1,1,3,100"
     */
    public static ReadIntegerArrayEs New_Sever_Luck_reward;
    /**
     * 在该等级以下时，不清除活跃点:145
     */
    public static int Activities_count_Noclean_Level = 145;
    /**
     * 【泰文版】分享 （内容）链接:"https://www.facebook.com/FallingSkyTH"
     */
    public static ReadStringArrayEs Thai_ShareLinke;
    /**
     * 【泰文版】点赞 打开网页（fb主页） 链接:"https://www.facebook.com/FallingSkyTH"
     */
    public static ReadStringArrayEs Thai_LikeLinke;
    /**
     * 排行榜等级、战力、装备、洗练、宝石、星级界面人物模型大小位置以及旋转:"246,0,0,0,0,0"
     */
    public static ReadIntegerArray Rank_base_mainTransfom;
    /**
     * 首充界面跳转到充值界面的功能ID（第一个参数对应首充弹窗的功能ID，第二个参数对应首充主界面跳转）:"2580000,2752000"
     */
    public static ReadIntegerArray Frist_Recharge_Open_Function;
    /**
     * 在背包整理时不会自动使用的经验丹道具:"2068,2069"
     */
    public static ReadIntegerArray No_auto_use_item;
    /**
     * 自动打开免费VIP界面的任务id，当完成此任务时打开:"99020.0"
     */
    public static ReadIntegerArray FreeVIP_AutoOpen_Task;
    /**
     * 天禁令-对应RechargeItem的主键ID:3342
     */
    public static int FallingSky_RechargeId = 3342;
    /**
     * 天禁令-单轮天数_轮数上限（必须配合天禁令对应配置表修改）（天数对应的是开服时间）:"28,4"
     */
    public static ReadIntegerArray FallingSky_Round_Limit;
    /**
     * 宠物助战弹窗宠物统一获取途径【废弃】【废弃】【废弃】:"2580000_2_首充获得,1241300_17003_商城购买,0_1_活动产出"
     */
    public static ReadStringArray Pet_Get_Ways;
    /**
     * 巅峰竞技跨服玩法在开服第几天后开启:6
     */
    public static int PeakBattle_OpenDay = 6;
    /**
     * 天禁令-功能开启天数，天数对应的是开服时间（配1代表从第二天开始计时）:1
     */
    public static int FallingSky_Round_Open_Day = 1;
    /**
     * 福利每日礼包对应开启的开服天数:1
     */
    public static int Daily_Gift_Open_Time = 1;
    /**
     * 经脉重置消耗:"1,600"
     */
    public static ReadIntegerArray Meridian_Rest_Cost;
    /**
     * 巅峰竞技服务器拉匹配池里的人匹配的等待时间范围，单位秒（玩家不可见的）:"10,30"
     */
    public static ReadIntegerArray PeakBattle_MatchServerTime;
    /**
     * 魂甲抽奖钥匙id_增加抽奖阁的经验值:"1098,10}1099,20}1100,30"
     */
    public static ReadIntegerArrayEs SoulArmor_Keys;
    /**
     * 初始的头像ID_头像框ID_气泡ID:"1100000001,1200000001,1300000001"
     */
    public static ReadIntegerArray Initial_Decorate;
    /**
     * 选择心法时职业对应的经脉ID:"0,3,4}1,5,6}2,8,9"
     */
    public static ReadIntegerArrayEs Meridian_OCC_ID;
    /**
     * 选择心法后需要其他心法经脉的等级:30
     */
    public static int Need_meridian_level = 30;
    /**
     * 合并次数需要的道具ID，一次消耗1个:60008
     */
    public static int Wweep_Need_Item = 60008;
    /**
     * 重置心法需要的物品:"1,1000"
     */
    public static ReadIntegerArray Reset_meridian_Item;
    /**
     * 魂印分解获得材料ID:83090
     */
    public static int SoulPeral_SplitItemid = 83090;
    /**
     * 魂甲--魂珠套装界面的 统一获取途径【废弃】【废弃】【废弃，配置在item中】:"1272000,1,八极阵图}0,1,活动产出"
     */
    public static ReadIntegerArrayEs Hunyin_Get_Ways;
    /**
     * 职业_心法_对应的普攻:"0,0,100,101,102,103}0,1,2000,2010,2020}0,2,3000,3010,3020}1,0,200,201,202,203}1,2,7000,7010,7020}1,1,8000,8010,8020}2,0,8301,8302,8303,8304}2,1,8401,8402,8403}2,2,8501,8502,8503}3,0,8701,8702,8703,8704}3,1,8801,8802,8803}3,2,8901,8902,8903"
     */
    public static ReadIntegerArrayEs Meridian_special_skill;
    /**
     * 装备融炼选中时需要二次提示（部位_品质_星级）(装备部位(0头盔.1武器.2胸甲.3项链.4腰带.5腿甲.6鞋子.7戒指):"3,6,1}7,6,1"
     */
    public static ReadIntegerArrayEs Equipsmelt_confirmset;
    /**
     * 不显示初始称号底图的等级:800
     */
    public static int Enitial_Title_hide = 800;
    /**
     * 仙甲寻宝幸运值保底【幸运值上限_单次抽取所得幸运值】:"120000,50"
     */
    public static ReadIntegerArray Treasure_xianjia_Limit;
    /**
     * 荒古神坛增加收益次数的道具:1315
     */
    public static int HorseBoss_Count_Item = 1315;
    /**
     * 荒古令的每日受益上限:450
     */
    public static int HorseBoss_score_limit = 450;
    /**
     * 犒赏令购买高级特权:"1,600"
     */
    public static ReadIntegerArray KaoShangLing_BuyItem;
    /**
     * 谷歌翻译开关（0关，1开）:0
     */
    public static int Google_Translate_Switch = 0;
    /**
     * 八卦碎片的获取途径:2018
     */
    public static int Bagua_Item_get = 2018;
    /**
     * 魔魂装备合成品质顺序（合成装备品质按照配置顺序进行提升合成）:"3,4,6,7,8,9,10"
     */
    public static ReadIntegerArray Cross_devil_Equip_Compose_Sort;
    /**
     * 除魔团默认关注开启等级条件:200
     */
    public static int Cross_devil_Group_Attention_Open_Level = 200;
    /**
     * 魔魂装备部位对应名字:"305,魔眼}306,魔甲}307,魔心}308,魔丹}309,魔眼}310,魔甲}311,魔心}312,魔丹}313,魔眼}314,魔甲}315,魔心}316,魔丹}317,魔眼}318,魔甲}319,魔心}320,魔丹"
     */
    public static ReadIntegerArrayEs Cross_devil_card_equip_part_name;
    /**
     * 开服天数范围_在线分钟数（配置开服天数范围内，每天首次累计在线时间到达5分钟弹出首充小提示框）:"2,7,5"
     */
    public static ReadIntegerArray First_recharge_notice_daily;
    /**
     * 资源找回可找回的最大天数:1
     */
    public static int RetrieveRes_day_limit = 1;
    /**
     * 灵魄开启需要的前一个的等级:"1,0}2,2}3,6}4,12}5,14}6,16"
     */
    public static ReadIntegerArrayEs Immortal_soul_core_limit;
    /**
     * 完美情缘功能关闭时间（大于指定开服天数关闭，大于，没有等于）:7
     */
    public static int Marry_activity_close_time = 7;
    /**
     * 完美情缘亲密度排行榜功能跳转（跳转功能数量；功能ID_功能名字；功能图标）:"4}1241200,灵玉商城}3103000,仙缘豪礼}1350000,佳偶天成}60900,爱情宝匣}n,shangcheng}n,shangcheng}n,wenjuan}n,icon,zjm,huodong"
     */
    public static ReadIntegerArrayEs Marry_activity_rank_open_function;
    /**
     * 婚宴副本购买热度配置，购买礼包消耗的道具id_道具数量_增加的热度值_客户端展示的假道具id_推送热度礼包的副本剩余时间（单位秒）_推送礼包的热度值:"1,198,1500,1370,840,300"
     */
    public static ReadIntegerArray Marry_copy_buy_hot;
    /**
     * 仙缘系统-夫妻组队传道增加经验（百分比）:0
     */
    public static int Marry_team_exp_add = 0;
    /**
     * 婚宴签到奖励 物品ID_数量_是否绑定:"50001,1,1}16318,5,1"
     */
    public static ReadIntegerArrayEs Marry_copy_sign_award;
    /**
     * 仙缘系统-完美情缘击杀BOSS活动（掉落物品ID_系统回收消耗物品ID_回收所得物品数量）:"1369,12,888"
     */
    public static ReadIntegerArray Marry_activity_bless_goods;
    /**
     * 仙缘系统-完美情缘击杀BOSS活动掉落预览（物品id_物品数量_职业）:"50001,1,9}1052,1,9}16101,1,9}20001,1,9}21001,1,9"
     */
    public static ReadIntegerArrayEs Marry_activity_bless_showreward;
    /**
     * 每次赠送获得的对应的情义点_每日获得的最大次数:"50,10"
     */
    public static ReadIntegerArray Qingyi_send_goods_max;
    /**
     * 每次收取获得的对应的情义点_每日获得的最大次数:"50,10"
     */
    public static ReadIntegerArray Qingyi_recive_goods_max;
    /**
     * 每次情义点寻宝需要的情义点_每日寻宝的最大次数:"100,3"
     */
    public static ReadIntegerArray Qingyi_xunbao_pay_max;
    /**
     * 角色当前最大战斗力(小于次战力)_显示的增加最小战力(单次增加的战力,大于此值显示新战力展示):"1000000,20000}2000000,30000}4000000,50000"
     */
    public static ReadIntegerArrayEs BigPowerShowValue;
    /**
     * 仙缘系统-玩家大于等于配置等级，才会收到祝福界面提示:100
     */
    public static int Marry_pray_level_limit = 100;
    /**
     * 仙缘系统-玩家祝福时可领取到的奖励:"50001,1,1,9}12,888,1,9"
     */
    public static ReadIntegerArrayEs Marry_pray_reward;
    /**
     * 每日累充宝箱奖励:"10001,2}3,50000"
     */
    public static ReadIntegerArrayEs Daily_Recharge_Reward;
    /**
     * 第一次领取剑灵阁时的额外奖励:"25,1500"
     */
    public static ReadIntegerArrayEs Sword_soul_copy_frist_reward;
    /**
     * 月卡tips显示参数  倍数_灵玉返还值:"21,23380"
     */
    public static ReadIntegerArray Month_Card_Tips_Param;
    /**
     * 仙侣对决海选赛的可参与次数:15
     */
    public static int Marry_battle_1_times = 15;
    /**
     * 仙侣对决海选赛胜利_失败获得积分:"20,5"
     */
    public static ReadIntegerArray Marry_battle_1_count;
    /**
     * 仙侣对决小组赛胜利_失败获得积分:"20,5"
     */
    public static ReadIntegerArray Marry_battle_2_count;
    /**
     * 仙侣对决竞猜消耗物品:"37,100"
     */
    public static ReadIntegerArray Marry_battle_guess_use;
    /**
     * 仙侣对决竞猜失败奖励物品:"37,110"
     */
    public static ReadIntegerArray Marry_battle_guess_failed_reward;
    /**
     * 仙侣对决竞猜成功奖励物品（支持率区间【前闭后开】_物品ID_数量）:"0,50,37,200}50,60,37,180}60,70,37,170}70,90,37,160}90,100,37,150"
     */
    public static ReadIntegerArrayEs Marry_battle_guess_success_reward;
    /**
     * 家装大赛每人每天可以投的指定票数:3
     */
    public static int Social_house_vote_num = 3;
    /**
     * 家装大赛每人每天可以投的随机票数:10
     */
    public static int Social_house_random_vote_num = 10;
    /**
     * 家装大赛投随机时的奖励:"10001,1}10002,2"
     */
    public static ReadIntegerArrayEs Social_house_random_vote_reward;
    /**
     * 家装大赛要求的最低舒适度:120
     */
    public static int Social_house_limit_num = 120;
    /**
     * 无忧宝库每日赠送道具：道具ID_数量_绑定:"1371,30,1"
     */
    public static ReadIntegerArray Worry_free_treasure_item;
    /**
     * 无忧宝库持续时间，开服天数:30
     */
    public static int Worry_free_treasure_time = 30;
    /**
     * 台湾的Facebook分享:"https://www.facebook.com/XM.DXZ"
     */
    public static ReadStringArrayEs TW_ShareLink;
    /**
     * 台湾的每日Facebook分享:"https://www.facebook.com/FallingSkyTH"
     */
    public static ReadStringArrayEs TW_DailyShareLink;
    /**
     * 台湾的每日Facebook分享奖励:"1017,50}19021,5}1011,1"
     */
    public static ReadIntegerArrayEs TW_DailyShareLinkRewards;
    /**
     * 任务推荐中传道的提示配置（等级_活跃点）在XXX等级以上，当前活跃点超过XX就提示传道:"175,50"
     */
    public static ReadIntegerArray Task_chuandao_recommend_tips;
    /**
     * 默认的家具配置:"20001,1"
     */
    public static ReadIntegerArrayEs Normal_social_house_furniture;
    /**
     * 高级VIP和高级至尊VIP所需充值金额（元）:"20000,600000"
     */
    public static ReadIntegerArray Special_vip_recharge;
    /**
     * 高级VIP跳转到客服的链接:"www.baidu.com"
     */
    public static ReadStringArrayEs Special_vip_goto_line;
    /**
     * 高级至尊VIP跳转到客服的链接:"www.baidu.com"
     */
    public static ReadStringArrayEs Special_high_vip_goto_line;
    /**
     * 高级VIP认证链接:"www.baidu.com"
     */
    public static ReadStringArrayEs Special_normal_vip_goto_authentication;
    /**
     * 高级至尊VIP认证链接:"www.baidu.com"
     */
    public static ReadStringArrayEs Special_high_vip_goto_authentication;
    /**
     * 新手层3阶装备收集任务的默认装备ICON（职业_位置_装备ID）:"0,0,2000225}0,1,2000226}0,2,2000227}0,4,2000229}0,5,2000230}0,6,2000231}1,0,2001889}1,1,2001890}1,2,2001891}1,4,2001893}1,5,2001894}1,6,2001895}2,0,2000225}2,1,2000226}2,2,2000227}2,4,2000229}2,5,2000230}2,6,2000231}3,0,2001889}3,1,2001890}3,2,2001891}3,4,2001893}3,5,2001894}3,6,2001895"
     */
    public static ReadIntegerArrayEs Freeboss_equip_task_3;
    /**
     * 新手层4阶装备收集任务的默认装备ICON:"0,0,2000329}0,1,2000330}0,2,2000331}0,4,2000333}0,5,2000334}0,6,2000335}1,0,2001993}1,1,2001994}1,2,2001995}1,4,2001997}1,5,2001998}1,6,2001999}2,0,2000329}2,1,2000330}2,2,2000331}2,4,2000333}2,5,2000334}2,6,2000335}3,0,2001993}3,1,2001994}3,2,2001995}3,4,2001997}3,5,2001998}3,6,2001999"
     */
    public static ReadIntegerArrayEs Freeboss_equip_task_4;
    /**
     * 新服活动入口 开启时间_关闭时间:"1,14"
     */
    public static ReadIntegerArray New_sever_activity;
    /**
     * 设置有掉血上限的怪物，每次触发上限后的掉血的CD时间（毫秒）:1000
     */
    public static int Monster_blood_max_cd = 1000;
    /**
     * 新手层3阶掉落的顺序:"0,20001,20002,20003,20004}1,20009,20010,20011,20012}2,20001,20002,20003,20004}3,20009,20010,20011,20012"
     */
    public static ReadIntegerArrayEs Freeboss_drop_3;
    /**
     * 新手层4阶掉落的顺序:"0,20005,20006,20007,20008}1,20013,20014,20015,20016}2,20005,20006,20007,20008}3,20013,20014,20015,20016"
     */
    public static ReadIntegerArrayEs Freeboss_drop_4;
    /**
     * 赏金之道的快速扫荡1次的灵玉消耗:5
     */
    public static int DailyTask_saodang_num = 5;
    /**
     * 赏金之道的快速扫荡需要的等级:300
     */
    public static int DailyTask_saodang_level = 300;
    /**
     * VIP宝珠增加的属性:"1,311,62}2,8356,1671}3,155,31}4,155,31"
     */
    public static ReadIntegerArrayEs VIP_POWER_ATT;
    /**
     * BOSS之家正常层免费进入VIP等级:1021
     */
    public static int BossHome_Count_Item = 1021;
    /**
     * 拍卖行上架，购买魔魂的VIP等级限制（配置格式：上架限制_购买限制）；0代表无限制:"4,4"
     */
    public static ReadIntegerArray DevilSoul_Trade_VIP_Limit;
    /**
     * 物品id_物品数量;物品id_物品数量;物品id_物品数量:"1017,50}19021,5}1011,1"
     */
    public static ReadIntegerArrayEs TW_ShopCommentRewards;
    /**
     * 开启类型_类型id；:"1,100}1,100}1,100}2,9999}2,9999；2,9999}2,9999"
     */
    public static ReadIntegerArrayEs TW_ShopCommentOpen;
    /**
     * 手镯、耳环、灵章的默认装备:"8,2310055}9,2310263}10,2310471"
     */
    public static ReadIntegerArrayEs New_Equip_ID;
    /**
     * 【VIP免费送】在线送VIP的时间的奖励:"1372,1,1,9}16,30,1,9"
     */
    public static ReadIntegerArrayEs Free_VIP_Level_Up_Reward;
    /**
     * 充值界面显示的tips:""
     */
    public static ReadStringArrayEs PlayFormTipsLabel;
    /**
     * 充值增加经验的额度（灵玉）:360
     */
    public static int First_Recharge_Exp_Add = 360;
    /**
     * 诸界远征积分宝箱的物品ID:83104
     */
    public static int Cross_Fudi_Point_Gift = 83104;
    /**
     * 超值折扣（灵玉版）免费礼包时间限制，单位：分:240
     */
    public static int Limit_gold_shop_time_limit = 240;
    /**
     * 超值折扣（灵玉版）免费礼包奖励内容:"10001,2,1,9}12,100,1,9"
     */
    public static ReadIntegerArrayEs Limit_gold_shop_time_reward;
    /**
     * 超值折扣免费礼包时间限制，单位：分:240
     */
    public static int Limit_direct_shop_time_limit = 240;
    /**
     * 超值折扣免费礼包奖励内容:"11001,2,1,9}12,100,1,9"
     */
    public static ReadIntegerArrayEs Limit_direct_shop_time_reward;
    /**
     * 超值功能免费礼包时间限制，单位：分:240
     */
    public static int Recharge_total_function_time_limit = 240;
    /**
     * 超值功能免费礼包奖励内容:"16001,2,1,9}12,100,1,9"
     */
    public static ReadIntegerArrayEs Recharge_total_function_time_reward;
    /**
     * 神女巡游双倍经验时间段:"960,990}1290,1320"
     */
    public static ReadIntegerArrayEs ConvoyDoubleExpTime;
    /**
     * 仙侣对决海选赛，小组赛，冠军赛地榜，冠军赛天榜时间（星期几_开始分_结束分）:"6,1210,1235}6,1235,1258}7,1210,1232}7,1235,1257"
     */
    public static ReadIntegerArrayEs Marry_battle_time;
    /**
     * 每日礼包充值ID;一键购买的商品ID（对应rechargeitem表主键）:"11,12,13,14,15}16"
     */
    public static ReadIntegerArrayEs DailyRechargeGift_Num;
    /**
     * 副本扫荡卷轴快捷购买配置，绑玉商品id_灵玉商品ID_价格:"20003006,101002,10"
     */
    public static ReadIntegerArray CopySweepBuyItemCfg;
    /**
     * 圣魂的对应物品_数量；:"1383,300}1384,300}1385,300}1386,300}1387,300"
     */
    public static ReadIntegerArrayEs Equip_Magic_att_item;
    /**
     * 幻装背包的数量:200
     */
    public static int Equip_Magic_bag = 200;
    /**
     * 仙缘系统-情缘副本界面物品【成功】奖励展示（纯客户端展示，服务器读取clone_map表）:"16101,5,0,9}1052,5,0,9}2209,1,0,9"
     */
    public static ReadIntegerArrayEs Marry_Copy_Suc_Show_Item;
    /**
     * 仙缘系统-情缘副本界面物品【失败】奖励展示（纯客户端展示，服务器读取clone_map表）:"16101,3,0,9}1052,3,0,9}2209,1,0,9"
     */
    public static ReadIntegerArrayEs Marry_Copy_Fail_Show_Item;
    /**
     * 传闻默认头像显示：ID_头像ID_头像框ID_气泡ID:"0,1100000009,1200000009,1300000009"
     */
    public static ReadIntegerArray Rumors_Head;
    /**
     * 寻宝免费次数间隔（领取后重新计时，单位：小时）:8
     */
    public static int Hunt_Free_Time = 8;
    /**
     * 最大红包个数_货币类型_领取货币类型:"50,1,2}50,2,2}50,12,12"
     */
    public static ReadIntegerArrayEs RedPacket;
    /**
     * 创建仙盟所需VIP等级（需激活升仙令）:4
     */
    public static int Create_Guild_Vip_Limit = 4;
    /**
     * 所需要达到VIP等级（需激活升仙令）才可手动发红包:6
     */
    public static int RedPacket_Vip_Limit = 6;
    /**
     * v4助力需要的货币:"1,711"
     */
    public static ReadIntegerArray V4_Help_Need_Item;
    /**
     * v4助力开启的天数:7
     */
    public static int V4_Help_Day_Count = 7;
    /**
     * v4助力投资需要的vip等级:4
     */
    public static int V4_Help_TouZi_NeedVipLevel = 4;
    /**
     * v4助力申请需要的vip等级:"2,3"
     */
    public static ReadIntegerArray V4_Help_ShenQing_NeedVipLevel;
    /**
     * v4返利:阶段_物品_物品数量:"1,240}1,240}1,250}1,250"
     */
    public static ReadIntegerArrayEs VipRebate_Reward;
    /**
     * v4返利可完成任务所需条件:4
     */
    public static int VipRebate_NeedVipLevel = 4;
    /**
     * V4返利开启时间:"1,7"
     */
    public static ReadIntegerArray VipRebate_Day_Count;
    /**
     * V4返利最大任务轮数:4
     */
    public static int VipRebate_Max_Round = 4;
    /**
     * 返利宝箱返利百分比:10
     */
    public static int RebateBox_Percent = 10;
    /**
     * 红包描述最大字符数:8
     */
    public static int Redpacket_Miaoshu = 8;
    /**
     * 篝火吃肉次数:"9000109,1"
     */
    public static ReadIntegerArray World_Bonfire_Fire_meat;
    /**
     * 仙盟争霸开启时间:"1,7"
     */
    public static ReadIntegerArray XMZB_Day_Count;
    /**
     * v4返利界面购买：类型_数量_点击链接FunctionStart内id:"1,980,2842000"
     */
    public static ReadIntegerArray VipRebate_Buy;
    /**
     * 房屋规模对应的进入坐标；分别为低、中、高:"11_5,17_8,21_10"
     */
    public static ReadIntegerArray Home_First_Pos;
    /**
     * 经验多倍找回：所需物品_物品持续时间_物品价格:"1001,120,28"
     */
    public static ReadIntegerArray EXP_GET_Multiple_BACK;
    /**
     * QQ大厅加群链接:"https://jq.qq.com/?_wv=1027&k=36btnYlH"
     */
    public static String QQDaTingGroupLink = "https://jq.qq.com/?_wv=1027&k=36btnYlH";
    /**
     * 当前版本开启的职业信息:"0,1,2"
     */
    public static ReadIntegerArray Open_Occupatio_List;

}
