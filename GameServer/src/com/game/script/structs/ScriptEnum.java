package com.game.script.structs;

import com.game.activity.struct.ActivityType;
import com.game.buff.structs.BuffDefine;
import com.game.friend.script.ICrossFriendScript;
import com.game.player.structs.PlayerAttributeType;
import com.game.ranklist.structs.RankType;

/**
 * @author lw
 * <p>
 * 脚本定义类
 */
public class ScriptEnum {

    /**
     * 功能脚本左偏移, 用于表述子脚本
     */
    public final static int NodeLimit = 10000;
    ////////////////////////////////////1 - 1000 基础功能脚本(DemoBaseScript)////////////////////////////////////////////////////////
    /**
     * 地图创建脚本
     */
    public final static int MapDealBaseScript = 1;
    /**
     * 支线任务脚本
     */
    public final static int BranchTaskBaseScript = 2;
    /**
     * 组队脚本
     */
    public final static int TeamHandlerBaseScript = 3;
    /**
     * 进入游戏脚本,加载完玩家数据并未同步到客户端
     */
    public final static int LoadScript = 4;
    /**
     * 退出游戏脚本
     */
    public final static int QuitGameBaseScript = 5;
    /**
     * 消息过滤脚本
     */
    public final static int HandlerScript = 6;
    /**
     * 主线任务的脚本
     */
    public final static int MainTaskBaseScript = 7;
    /**
     * 红包脚本
     */
    public final static int RedPacketBaseScript = 8;
    /**
     * 普通日常任务的脚本
     */
    public final static int DailyTaskBaseScript = 9;
    /**
     * 帮会日常、周常脚本
     */
    public final static int ConquerTaskBaseScript = 10;
    /**
     * 玩家等级变化脚本
     */
    public final static int PlayerLevelChangeBaseScript = 11;
    /**
     * 竞技场逻辑类脚本
     */
    public final static int JJCManagerScriptBaseScript = 12;
    /**
     * GM命令脚本
     */
    public final static int GmComandBaseScript = 13;
    /**
     * 技能相关脚本
     */
    public final static int SkillBaseScript = 14;
    /**
     * 装备相关脚本
     */
    public final static int EquipManagerBaseScript = 15;
    /**
     * 掉落脚本
     */
    public final static int DropBaseScript = 16;

    /**
     * 任务的处理脚本
     */
    public final static int TaskManagerBaseScript = 17;
    /**
     * 怪物管理中心脚本
     */
    public final static int MonsterBaseScript = 18;
    /**
     * 坐骑系统脚本
     */
    public final static int HorseBaseScript = 19;
    /**
     * 玩家脚本
     */
    public final static int PlayerBattleBaseScript = 20;
    /**
     * 转职脚本
     * 已废弃
     */
    public final static int ChangeJobBaseScript = 21;
    /**
     * 地图清洁工脚本
     */
    public final static int MapCleanerBaseScript = 22;
    /**
     * 商店，庐山真面管理接口脚本
     */
    public final static int ShopScript = 23;
    /**
     * 完全进入游戏脚本,加载完玩家数据并同步到客户端之后
     */
    public final static int PlayerEnterGameFinishBaseScript = 24;
    /**
     * 玩家复活脚本
     */
    public final static int PlayerReliveBaseScript = 25;
    /**
     * 副本管理脚本类主管脚本
     */
    public final static int CopyMapManagerBaseScript = 26;
    /**
     * 成就系统脚本
     */
    public final static int AchievementBaseScript = 27;
    /**
     * 属性系统
     */
    public final static int AttributeScript = 28;
    /**
     * 称号脚本
     */
    public final static int TitleBaseScript = 29;
    /**
     * 活动脚本
     */
    public final static int ActivityScriptBaseScript = 30;
    /**
     * 后台命令处理脚本
     */
    public final static int BackCommandBaseScript = 31;
    /**
     * 错误分析报告
     */
    public final static int ErrorLogReportBaseScript = 32;
    /**
     * 传送阵脚本
     */
    public final static int TransportBaseScript = 33;
    /**
     * 首充、续充脚本
     */
    public final static int FCChargeBaseScript = 34;
    /**
     * 充值管理脚本
     */
    public final static int RechargeManagerBaseScript = 35;
    /**
     * 角色改名脚本
     */
    public final static int ChangeRoleNameBaseScript = 36;
    /**
     * 帮会系统脚本
     */
    public final static int GuildManagerBaseScript = 37;
    /**
     * 巅峰竞技
     */
    public final static int PeakPkScript = 38;
    /**
     * 服务脚本
     */
    public final static int ServerBaseScript = 39;
    /**
     * 游戏服与战斗服交互的脚本
     */
    public final static int CrossServerBaseScript = 40;
    /**
     * 战斗服场景建立的交互脚本
     */
    public final static int CrossFightBaseScript = 41;
    /**
     * 跨服场景切换
     */
    public final static int CrossChangeMapBaseScript = 42;
    /**
     * 魂甲系统
     */
    public final static int SoulArmorScript = 43;
    /**
     * 心跳类的处理
     */
    public final static int HeartBaseScript = 44;
    /**
     * 地图
     */
    public final static int MapManagerBaseScript = 45;
    /**
     * 战斗管理类
     */
    public final static int FightManagerBaseScript = 46;
    /**
     * 向手机推送消息
     */
    public final static int PushMessageBaseScript = 47;
    /**
     * 消息移动
     */
    public final static int MessageBaseScript = 48;
    /**
     * 平台评价
     */
    public final static int PlatformEvaluateBaseScript = 49;
    /**
     * 资源找回
     */
    public final static int RetrieveResScript = 50;
    /**
     * 聊天脚本
     */
    public final static int ChatBaseScript = 52;
    /**
     * 好友脚本
     */
    public final static int FriendBaseScript = 53;
    /**
     * 排行榜管理脚本
     */
    public final static int RankListBaseScript = 54;
    /**
     * 注册脚本
     */
    public final static int RegisterBaseScript = 55;
    /**
     * 背包的协议脚本处理
     */
    public final static int BackpackBaseScript = 56;
    /**
     * 背包的系统共用处理
     */
    public final static int BackpackManagerBaseScript = 57;
    /**
     * 仓库协议处理
     */
    public final static int StoreBaseScript = 58;
    /**
     * 仓库系统共用处理
     */
    public final static int StoreManagerBaseScript = 59;
    /**
     * 装备使用接口
     */
    public final static int EquipItemBaseScript = 60;
    /**
     * 玩家的管理类
     */
    public final static int PlayerManagerBaseScript = 61;
    /**
     * 开关控制脚本
     */
    public final static int ControlBaseScript = 62;
    /**
     * 转职任务协议
     */
    public final static int GenderTaskBaseScript = 63;
    /**
     * 宠物系统脚本
     */
    public final static int PetBaseScript = 64;
    /**
     * 玩家的管理类扩展脚本
     */
    public final static int PlayerManagerExtScript = 65;
    /**
     * buff系统
     */
    public final static int BuffScript = 66;
    /**
     * 机器人
     */
    public final static int RobotBaseScript = 67;
    /**
     * 服务器心跳脚本
     */
    public final static int ServerHeartBaseScript = 68;
    /**
     * 节日活动脚本
     */
    public static final int HolidayActivityBaseScript = 69;
    /**
     * 节日活动的外部操作脚本
     */
    public static final int HolidayActionBaseScript = 70;
    /**
     * 分享功能脚本
     */
    public static final int ShareBaseScript = 71;
    /**
     * 怪物的AI脚本处理实现
     */
    public static final int MonsterYedAiBaseScript = 72;
    /**
     * 世界boss处理脚本
     */
    public final static int BossManagerBaseScript = 75;
    /**
     * 引导任务处理脚本
     */
    public final static int GuideTaskBaseScript = 76;

    /**
     * 离线挂机逻辑脚本
     */
    public final static int PlayerHookBaseScript = 78;
    /**
     * 内测元宝充值卡
     */
    public final static int InnerGoldBaseScript = 79;
    /**
     * 造化脚本
     */
    public final static int NatureBaseScript = 80;
    /**
     * 天书符咒脚本
     */
    public final static int GodBookBaseScript = 81;
    /**
     * 宗派活动脚本
     */
    public final static int GuildActivityBaseScript = 82;
    /**
     * 宝石镶嵌脚本
     */
    public final static int GemBaseScript = 83;

    /**
     * 境界脚本
     */
    public final static int StateLevelBaseScript = 84;
    /**
     * 识海脚本
     */
    public final static int ShiHaiBaseScript = 85;
    /**
     * 超值折扣
     */
    public final static int DiscountRechargeScript = 86;
    /**
     * 设置脚本
     */
    public final static int SettingBaseScript = 88;
    /**
     * 任务可见处理脚本
     */
    public final static int NpcManagerBaseScript = 89;
    /**
     * 战场任务脚本
     */
    public final static int BattleFiledTaskBaseScript = 90;
    /**
     * 洗髓脚本
     */
    public final static int XiSuiScript = 92;

    /**
     * 采集物处理脚本
     */
    public static final int GatherManagerBaseScript = 93;

    /**
     * 每日可做
     */
    public final static int DailyActiveBaseScript = 95;

    /**
     * 答题脚本
     */
    public final static int AnswerQuestionsBaseScript = 96;

    /**
     * 兽魂脚本
     */
    public static final int SoulBeastBaseScript = 97;
    /**
     * 血脉系统
     */
    public static final int BloodBaseScript = 98;
    /**
     * 每日累充脚本
     */
    public final static int DailyRechargeBaseScript = 99;

    /**
     * 角色登录修复脚本
     */
    public final static int PlayerLoginFixBugScript = 100;

    /**
     * 仙魂系统脚本
     */
    public final static int ImmortalSoulBaseScript = 101;

    /**
     * 登陆礼包
     */
    public final static int LoginGiftBaseScript = 102;
    /**
     * 每日礼包
     */
    public final static int DayGiftBaseScript = 103;
    /**
     * 每日签到
     */
    public final static int DayCheckInBaseScript = 104;
    /**
     * 感悟经验
     */
    public final static int FeelingExpBaseScript = 105;
    /**
     * 成长基金
     */
    public final static int GrowthFundBaseScript = 106;
    /**
     * 兑换礼包（礼包码）
     */
    public final static int ExchangeGiftBaseScript = 107;
    /**
     * 月卡尊享卡
     */
    public final static int ExclusiveCardBaseScript = 108;

    /**
     * 寻宝系统
     */
    public final static int TreasureHuntBaseScript = 109;

    /**
     * 开服活动
     */
    public final static int OpenServerAcBaseScript = 110;

    /**
     * 九天争锋基础脚本ID
     */
    public final static int NineDaysFocusedBaseScript = 111;

    /**
     * 世界支援脚本
     */
    public final static int WorldHelpScript = 112;

    /**
     * 副本逻辑脚本
     */
    public static final int CopyLogicBaseScript = 113;

    /**
     * 地图销毁脚本
     */
    public static final int MapDelBaseScript = 114;

    /**
     * 灵压脚本
     */
    public static final int StateStifleBaseScript = 115;

    /**
     * 回收炉脚本
     */
    public static final int RecycleBaseScript = 116;

    /**
     * 万寿修为丹
     */
    public final static int WanShouXiuWeiDanBaseScript = 117;

    /**
     * 魂兽森林协议管理与处理的地方
     */
    public final static int SoulAnimalForestManagerBaseScript = 118;

    /**
     * 跨服福地脚本
     */
    public final static int CrossFudScript = 119;

    /**
     * 竞拍脚本
     */
    public static final int AuctionBaseScript = 120;

    /**
     * 八星阵图
     */
    public static final int EightDiagramsScript = 121;

    /**
     * 世界篝火
     */
    public static final int WorldBonfireBaseScript = 122;

    /**
     * 挚友
     */
    public static final int ChumScript = 123;
    /**
     * 圣装
     */
    public static final int HolyEquipScript = 124;
    /**
     * 掌门传道
     */
    public static final int LeaderPreachScript = 125;

    /**
     * 有奖问答
     */
    public static final int QuestionnaireScript = 127;
    /**
     * Vip基础功能
     */
    public static final int VipBaseScript = 128;
    /**
     * Vip权限功能
     */
    public static final int VipPowerScript = 129;
    /**
     * 货币系统共用处理
     */
    public final static int CurrencyManagerBaseScript = 130;
    /**
     * 被动技能
     */
    public final static int FightTriggerBaseScript = 131;
    /**
     * 等级礼包
     */
    public final static int LevelGiftBaseScript = 132;

    /**
     * bi
     */
    public final static int BIScript = 133;

    /**
     * 神秘限购
     */
    public final static int LimitShopScript = 134;

    /**
     * 仙盟争霸脚本
     */
    public final static int GuildBattleBaseScript = 135;

    /**
     * 仙甲系统脚本
     */
    public final static int ImmortalEquipBaseScript = 136;


    /**
     * 仙甲寻宝
     */
    public final static int TreasureHuntXianjiaScript = 137;

    /**
     * 新时装
     */
    public final static int NewFasionBaseScript = 138;

    /**
     * bi 4399
     */
    public final static int BI4399Script = 139;


    /**
     * 跨服排行榜
     */
    public final static int CrossRankScript = 140;


    /**
     * 新化形
     */
    public final static int HuaxinFlySwordScript = 141;


    /**
     * 神秘商店
     */
    public final static int MysteryShopScript = 142;
    /**
     * 实名认证
     */
    public final static int CertifyScript = 143;

    /**
     * 0元购买
     */
    public final static int FreeShopScript = 144;

    /**
     * 结婚脚本
     */
    public final static int MarriageScript = 145;

    /**
     * 更新公告脚本
     */
    public final static int UpdateNoticScript = 146;

    /**
     * 剑灵阁脚本
     */
    public final static int SwordSoulTowerScript = 147;

    /**
     * 藏宝阁脚本
     */
    public final static int CangbaogeScript = 148;

    /**
     * 新每日累充
     */
    public final static int DailyRechargeTotalScript = 149;

    /**
     * 天紧令
     */
    public final static int FallingSkyScript = 150;

    /**
     * 活动排行脚本
     */
    public final static int ActivityRankScript = 151;

    /**
     * 日志数据管理
     */
    public final static int LogDataManagerScript = 152;

    /**
     * 跨服坐骑BOSS
     */
    public final static int CrossHorseBossScript = 153;
    /**
     * 犒赏令
     */
    public final static int KaoShangLingScript = 154;
    /**
     * 魔魂系统
     */
    public final static int DevilSeriesScript = 155;
    /**
     * 巅峰基金
     */
    public final static int InvestPeak = 156;
    /**
     * 家园系统
     */
    public final static int HomeScript = 157;

    /**
     * 情缘活动相关脚本
     */
    public final static int MarryActivityScript = 158;

    /**
     * 仙侣对决相关脚本
     */
    public final static int CouplefightScript = 159;
    /**
     * 跨服好友相关脚本
     */
    public final static int CrossFriendScript = 160;
    /**
     * 仙侣对决商城
     */
    public final static int CoupleShopScript = 161;

    /**
     * 社区脚本
     */
    public final  static int CommunityScript = 162;

    /**
     * 角色修改自定义头像脚本
     */
    public final static int PlayerCustomHeadScript = 163;

    /**
     * 无忧宝库
     */
    public final static int TreasureHuntWuyouScript = 164;


    /**
     * 新0元购买
     */
    public final static int NewFreeShopScript = 165;

    /**
     * Vip宝珠功能
     */
    public static final int VipPearlScript = 166;


    /**
     *  免费礼包
     */
    public final static int WelfareFreeGiftScript = 167;

    /**
     * 核心功能任务
     */
    public final static int FunctionTaskScript = 168;

    /**
     * 仙女护送
     */
    public final static int CoupleEscortScript = 169;

    /**
     * 幻装
     */
    public final static int UnrealEquipScript = 170;

    /**
     * 混沌虚空第二阶段（须弥宝库）
     */
    public final static int AlienGemScript = 171;


    /**
     * v4 助力活动
     */
    public final static int V4HelpScript  = 172;

    /**
     * v4 返利
     */
    public final static int V4RebateScript  = 173;

    /**
     * 充值返利
     */
    public final static int RechargeRebateScript  = 174;

    /**
     * bi QQ
     */
    public final static int QQScript = 175;
    ////////////////////////////////////1001 - 2000 活动地图脚本 (DemoActivityScript)////////////////////////////////////////////////
    /**
     * 夫妻副本的脚本ID
     */
    public static final int MarryWeddingActivityScript = 1001;

    /**
     * 竞技场副本
     */
    public final static int JJCCloneActivityScript = 1002;

    /**
     * 世界boss副本脚本
     */
    public static final int WorldBossActivityScript = 1003;
    /**
     * 魂兽森林本地副本
     */
    public final static int SoulAnimalForestLocalCloneActivityScript = 1004;
    /**
     * 位面
     */
    public static final int PlaneActivityScript = 1005;
    /**
     * 假无限层
     */
    public static final int OnceCopyActivityScript = 1006;
    /**
     * boss之家地图脚本
     */
    public final static int BossHomeScript = 1007;

    /**
     * 福地boss地图脚本
     */
    public static final int GuildFudScript = 1008;

    /**
     * 仙缘.情缘副本
     */
    public static final int MarryHoneyScript = 1009;
    /**
     * 单人爬塔（万妖卷）
     */
    public static final int SingleTowerActivityScript = 1010;
    /**
     * 星级副本（大能遗府）
     */
    public static final int StarCopyActivityScript = 1011;
    /**
     * 个人boss
     */
    public static final int PersonalBossActivityScript = 1012;
    /**
     * 个人无限首领
     */
    public static final int PersonalUnLimitBossScript = 1013;

    /**
     * 天禁之门
     */
    public static final int FairyLandActivityScript = 1014;
    /**
     * 经验副本
     */
    public static final int ExpCopyActivityScript = 1015;

    /**
     * 跨服福地副本脚本
     */
    public static final int CrossFudCloneScript = 1016;
    /**
     * 装备副本(心魔副本)
     */
    public static final int EquipCopyActivityScript = 1017;
    /**
     * 强化副本（五行副本）
     */
    public static final int StrengthenCopyActivityScript = 1018;

    /**
     * 九天争锋战斗地图脚本
     */
    public final static int NineDaysFocusedWarCloneActivityScript = 1019;

    /**
     * 境界boss副本
     */
    public static final int BossStateActivityScript = 1020;

    /**
     * 仙盟论剑活动
     */
    public static final int GuildLastBattleScript = 1021;

    /**
     * 八星阵图
     */
    public static final int EightDiagramsWar = 1023;

    /**
     * 魔王缝隙副本
     */
    public static final int CrossDevilCloneScript = 1022;

    /**
     * 套装boss
     */
    public static final int SuitBossScript = 1024;

    /**
     * 宝石boss
     */
    public static final int GemBossScript = 1025;

    /**
     * 混沌虚空副本
     */
    public static final int CrossAlienScript = 1026;

    /**
     * 仙盟争霸
     */
    public static final int GuildBatleMapScript = 1027;
    /**
     * 公会任务副本
     */
    public static final int GuildTaskMapScript = 1028;

    /**
     * 仙盟驻地脚本
     */
    public static final int GuildBaseMapScript = 1029;

    /**
     * 新手层boss脚本
     */
    public static final int NoodBossMapScript = 1030;

    /**
     * 指挥系统脚本
     */
    public static final int CommandScript = 1031;

    /**
     * 剑灵阁副本
     */
    public static final int SwordSoulTowerMapScript = 1032;

    /**
     * 剑冢挑战副本
     */
    public static final int SwordSoulCopyMapScript = 1033;

    /**
     * 巅峰竞技场副本
     */
    public static final int PeakCloneScript = 1034;

    /**
     * NPC定时器地图
     */
    public static final int NpcTimerMapScript = 1035;
    /**
     * 除魔团副本-魔魂系列
     */
    public static final int DevilSeriesCopyMapScript = 1036;

    /**
     * 巅峰竞技场匹配等待地图
     */
    public static final int PeakWaitScript = 1037;


    ////////////////////////////////////2001 - 3000 跨服活动地图脚本 (DemoCrossActivityScript)////////////////////////////////////////
    /**
     * 跨服副本集中营
     */
    public final static int CopyMapTestCrossActivityScript = 2001;
    /**
     * 魂兽森林跨服副本
     */
    public final static int SoulAnimalForestCrossCloneCrossActivityScript = 2002;
    /**
     * 勇者巅峰副本
     */
    public static final int BravePeakCrossActivityScript = 2003;
    /**
     * 神魔战场副本（三界之门）
     */
    public static final int GodDevilWarCrossActivityScript = 2004;
    /**
     * 世界篝火副本
     */
    public static final int WorldBonfireActivityScript = 2005;
    /**
     * 太虚战场副本
     */
    public static final int UniverseWarActivityScript = 2006;

    /**
     * 跨服世界BOSS
     */
    public static final int CrossHorseBossActivityScript = 2007;
    /**
     * 仙侣对决 准备地图
     */
    public static final int CouplefightPreMapScript = 2008;
    /**
     * 仙侣对决 战斗地图
     */
    public static final int CouplefightMapScript = 2009;
    /**
     * 家园地图
     */
    public static final int HouseSceneScript =  2010;
    /**
     * 须弥宝库
     */
    public static final int AlienGemCopyMapScript =  2011;

    ////////////////////////////////////3001 - 4000 通用功能脚本 (DemoCommonScript)/////////////////////////////////////////////
    /**
     * 移动行为
     */
    public final static int MoveBehaviorCommonScirpt = 3001;
    /**
     * 方向移动行为
     */
    public final static int DirMoveBehaviorCommonScript = 3002;
    /**
     * 飞行行为
     */
    public final static int FlyBehaviorCommonScript = 3003;
    /**
     * 奔跑行为
     */
    public final static int RunBehaviorCommonScript = 3004;
    /**
     * 攻击移动行为
     */
    public final static int AttackMoveBehaviorCommonScript = 3005;
    /**
     * 攻击移动行为
     */
    public final static int ReviveBehaviorCommonScript = 3006;
    /**
     * 怪物AI脚本
     */
    public final static int MonsterAiCommonScript = 3007;
    /**
     * 玩家AI脚本
     */
    public final static int PlayerAiCommonScript = 3008;
    /**
     * 采集行为脚本
     */
    public final static int GatherBehaviorCommonScript = 3009;
    /**
     * 回跑行为
     */
    public final static int RunBackBehaviorCommonScript = 3010;
    /**
     * 跳跃行为
     */
    public final static int JumpBehaviorCommonScript = 3011;
    /**
     * 技能位移
     */
    public final static int SkillMoveBehaviorCommonScript = 3012;
    /**
     * 角色ai检查技能攻击对象的脚本
     */
    public final static int AiPlayerSkillTargetCheckerCommonScript = 3013;
    /**
     * 引导技能目标检查脚本
     */
    public final static int GuideSkillTargetCheckerCommonScript = 3014;
    /**
     * 怪物ai技能攻击时目标检查脚本
     */
    public final static int AiMonsterSkillTargetCheckerCommonScript = 3015;
    /**
     * 宠物ai技能攻击目标检查脚本
     */
    public final static int AiPetSkillTargetCheckerCommonScript = 3016;
    /**
     * 机器人ai脚本
     */
    public final static int RobotAiCommonScript = 3017;
    /**
     * 默认的目标识别脚本,除了自己,别人都能打
     */
    public final static int AiDefaultSkillTargetCheckerCommonScript = 3018;
    /**
     * entity的ai脚本
     */
    public final static int EntityAiCommonScript = 3019;
    /**
     * 召唤物行为脚本
     */
    public final static int MagicBehaviorCommonScript = 3020;
    /**
     * 召唤物ai脚本
     */
    public final static int SkillMagicAiCommonScript = 3021;
    /**
     * entity的脚本
     */
    public final static int EntityCommonScript = 3022;
    /**
     * 召唤物的AI脚本
     */
    public final static int MagicAiBehaviorCommonScript = 3023;

    /**
     * 默认Yedai脚本
     */
    public final static int YedAiCommonScript = 3024;

    /**
     * 一周福利幸运抽奖脚本
     */
    public final static int LuckyDrawWeekScript = 3025;

    /**
     * 狂欢周脚本
     */
    public final static int CrazyWeekScript = 3026;


    ////////////////////////////////////4001 - 5000 属性脚本 (DemoAttributeScript)/////////////////////////////////////////////

    /**
     * 基础属性脚本
     */
    public final static int BaseAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.BASE.getValue();

    /**
     * 装备属性脚本
     */
    public final static int EquipAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.EQUIP.getValue();

    /**
     * buff属性脚本
     */
    public final static int BuffAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.BUFF.getValue();

    /**
     * 药属性脚本
     */
    public final static int MedicineAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.MEDICINESATTRIBUTE.getValue();

    /**
     * 坐骑属性脚本
     */
    public final static int HorseAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.HORSE.getValue();

    /**
     * 婚姻属性脚本
     */
    public final static int MarriageAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.MARRIAGE.getValue();

    /**
     * 宠物属性脚本
     */
    public final static int PetAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.PET.getValue();
    /**
     * 称号属性脚本
     */
    public final static int TitleAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.TITLE.getValue();

    /**
     * 境界VIP属性脚本
     */
    public final static int StateVipAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.StateVip.getValue();

    /**
     * 技能属性脚本
     */
    public final static int SkillAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.Skill.getValue();

    /**
     * 翅膀属性脚本
     */
    public final static int WingAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.WING.getValue();

    /**
     * 任务属性脚本
     */
    public final static int TaskAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.Task.getValue();

    /**
     * 魂兽属性脚本
     */
    public final static int SoulBeastAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.SOUL_BEAST.getValue();

    /**
     * 符咒任务属性脚本
     */
    public final static int AmuletTaskAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.AmuletTask.getValue();

    /**
     * 法器属性脚本
     */
    public final static int TalismanAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.Talisman.getValue();

    /**
     * 阵法属性脚本
     */
    public final static int MagicAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.Magic.getValue();

    /**
     * 神兵属性脚本
     */
    public final static int WeaponAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.Weapon.getValue();

    /**
     * 宝石属性脚本
     */
    public final static int GemAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.GEM.getValue();

    /**
     * 识海属性脚本
     */
    public final static int ShiHaiAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.ShiHai.getValue();


    /**
     * 仙魄属性脚本
     */
    public final static int ImmortalSoulAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.Immortalsoul.getValue();

    /**
     * 灵压法宝属性
     */
    public final static int StifleFabaoAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.StifleFabao.getValue();

    /**
     * 装备收集属性
     */
    public final static int SpiritAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.Spirit.getValue();

    /**
     * 圣装属性
     */
    public static final int HolyEquipAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.HolyEquip.getValue();

    /**
     * 洗髓属性
     */
    public static final int XiSuiAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.XiSui.getValue();

    /**
     * 仙甲属性
     */
    public static final int ImmortalEquipAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.ImmortalEquip.getValue();

    /**
     * 新化形属性
     */
    public static final int HuaxinFlySwordAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.HuaxinFlySword.getValue();

    /**
     * 新时装属性
     */
    public static final int NewFashionAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.NewFashion.getValue();
    /**
     * 魂甲属性
     */
    public static final int SoulEquipAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.SoulEquip.getValue();
    /**
     * 魔魂属性
     */
    public static final int DevilSeriesAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.DevilSeries.getValue();
    /**
     * VIP属性脚本
     */
    public final static int VipAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.Vip.getValue();

    ////////////////////////////////////  排行榜脚本 每个排行榜的id由两部分组成，一个是排行榜管理脚本的id，一个是rank_base.xlsx中的id/////////////////////////////////////////////
    /**
     * 等级排行榜脚本
     */
    public final static int LevelRankScript = RankListBaseScript * NodeLimit + RankType.LEVEL_RANK;
    /**
     * 战力排行榜脚本
     */
    public final static int FightPowerRankScript = RankListBaseScript * NodeLimit + RankType.FIGHT_POWER_RANK;
    /**
     * 坐骑排行榜脚本
     */
    public final static int HorseRankScript = RankListBaseScript * NodeLimit + RankType.HORSE_RANK;
    /**
     * 翅膀排行榜脚本
     */
    public final static int WingRankScript = RankListBaseScript * NodeLimit + RankType.WING_RANK;
    /**
     * 装备排行榜脚本
     */
    public final static int EquipRankScript = RankListBaseScript * NodeLimit + RankType.EQUIP_RANK;
    /**
     * 法宝排行榜脚本
     */
    public final static int MagicWeaponRankScript = RankListBaseScript * NodeLimit + RankType.MAGIC_WEAPON_RANK;
    /**
     * 法器排行榜脚本
     */
    public final static int TalismanRankScript = RankListBaseScript * NodeLimit + RankType.TALISMAN_RANK;
    /**
     * 阵法排行榜脚本
     */
    public final static int MagicRankScript = RankListBaseScript * NodeLimit + RankType.MAGIC_RANK;
    /**
     * 神兵排行榜脚本
     */
    public final static int WeaponRankScript = RankListBaseScript * NodeLimit + RankType.WEAPON_RANK;
    /**
     * 宝石排行榜脚本
     */
    public final static int GemRankScript = RankListBaseScript * NodeLimit + RankType.GEM_RANK;
    /**
     * 离线效率排行榜脚本
     */
    public final static int OfflineEfficiencyRankScript = RankListBaseScript * NodeLimit + RankType.OFFLINE_EFFICIENCY_RANK;
    /**
     * 装备洗练排行榜脚本
     */
    public final static int EquipWashRankScript = RankListBaseScript * NodeLimit + RankType.EQUIPWASH_RANK;
    /**
     * 装备强化排行榜脚本
     */
    public final static int EquipStrengthenRankScript = RankListBaseScript * NodeLimit + RankType.EQUIPSTRENGTHEN_RANK;
    /**
     * 宝石等级排行榜脚本
     */
    public final static int GemLevelRankScript = RankListBaseScript * NodeLimit + RankType.GEMLEVEL_RANK;
    /**
     * 装备总星级排行榜
     */
    public final static int EquipStarRankScript = RankListBaseScript * NodeLimit + RankType.EQUIPSTAR_RANK;
    /**
     * 装备总星级排行榜
     */
    public final static int EquipAllStarRankScript = RankListBaseScript * NodeLimit + RankType.EQUIPALLSTAR_RANK;
    /**
     * 竞技场排行榜脚本
     */
    public final static int ArenaRankScript = RankListBaseScript * NodeLimit + RankType.ARENA_RANK;
    /**
     * 石海排行榜脚本
     */
    public final static int ShiHaiRankScript = RankListBaseScript * NodeLimit + RankType.SHIHAI_RANK;
    /**
     * 公会排行榜脚本
     */
    public final static int GuildRankScript = RankListBaseScript * NodeLimit + RankType.GUILD_RANK;
    /**
     * 魅力值排行榜脚本
     */
    public final static int CharmRankScript = RankListBaseScript * NodeLimit + RankType.CHARM_RANK;
    /**
     * 送花排行榜脚本
     */
    public final static int SendFlowerRankScript = RankListBaseScript * NodeLimit + RankType.SEND_FLOWER_RANK;
    /**
     * 名人堂脚本
     */
    public final static int TopHallRankScript = RankListBaseScript * NodeLimit + RankType.TOPHALL_RANK;
    /**
     * 天虚战场排名脚本
     */
    public final static int UniverseRankScript = RankListBaseScript * NodeLimit + RankType.UNIVERSE_RANK;
    /**
     * 宠物战力排行
     */
    public final static int PetRankScript = RankListBaseScript * NodeLimit + RankType.PETASTAR_RANK;
    /**
     * 灵体战力排行
     */
    public final static int SpiritRankScript = RankListBaseScript * NodeLimit + RankType.SPIRITSTAR_RANK;
    /**
     * 仙甲战力排行
     */
    public final static int ImmEquipRankScript = RankListBaseScript * NodeLimit + RankType.IMEQUIPSTAR_RANK;
    /**
     * 圣装战力排行
     */
    public final static int HolyEquipRankScript = RankListBaseScript * NodeLimit + RankType.HOLYEQUIP_RANK;
    /**
     * 神兽战力排行
     */
    public final static int MonstorRankScript = RankListBaseScript * NodeLimit + RankType.MONSTOR_RANK;
    /**
     * 宠物御魂等级排行
     */
    public final static int PetSoulLvRankScript = RankListBaseScript * NodeLimit + RankType.PET_SOUL_LV_RANK;
    /**
     * 宠物等级排行
     */
    public final static int PetLvRankScript = RankListBaseScript * NodeLimit + RankType.PET_LV_RANK;
    /**
     * 坐骑御魂等级排行
     */
    public final static int HorseSoulLvRankScript = RankListBaseScript * NodeLimit + RankType.HORSE_SOUL_LV_RANK;
    /**
     * 坐骑等级排行
     */
    public final static int HorseLvRankScript = RankListBaseScript * NodeLimit + RankType.HORSE_LV_RANK;
    /**
     * 元宝消耗排行
     */
    public final static int ConsumeGodRankScript = RankListBaseScript * NodeLimit + RankType.COMSUME_GOLD_RANK;

    /**
     * 魂甲战力排行
     */
    public final static int SoulFightRankScript = RankListBaseScript * NodeLimit + RankType.SOUL_FIGHT_RANK;

    /**
     * 八卦战力排行
     */
    public final static int BaguaRankScript = RankListBaseScript * NodeLimit + RankType.BAGUA_RANK;

    /**
     * 亲密度排行
     */
    public final static int IntimacyRankScript = RankListBaseScript * NodeLimit + RankType.Intimacy_Rank;

    /**
     * 灵魂战力排行
     */
    public final static int ImmortalsoulScript = RankListBaseScript * NodeLimit + RankType.Immortalsoul_RANK;

    /**
     * 魔魂战力排行
     */
    public final static int DevilSoulRankScript = RankListBaseScript * NodeLimit + RankType.DEVIL_SOUL_RANK;

    /**
     * 坐骑装备战力排行
     */
    public final static int HorseEquipRankScript = RankListBaseScript * NodeLimit + RankType.HORSE_EQUIP_RANK;

    /**
     * 幻装属性
     */
    public static final int UnrealEquipAttributeScript = AttributeScript * NodeLimit + PlayerAttributeType.UnrealEquip.getValue();

    ////////////////////////////////////  buff 效果脚本/////////////////////////////////////////////

    public final static int NoneBuff = BuffScript * NodeLimit + BuffDefine.Type_None;   //无效果
    public final static int AttributeBuff = BuffScript * NodeLimit + BuffDefine.Type_Attribute; //属性
    public final static int HpPoolBuff = BuffScript * NodeLimit + BuffDefine.Type_HpPool; //血池
    public final static int DecHpBuff = BuffScript * NodeLimit + BuffDefine.Type_DecHp; //掉血
    public final static int DecAllHpBuff = BuffScript * NodeLimit + BuffDefine.Type_DecAllHpRate; //掉血总值万分比
    public final static int DecCurHpRateBuff = BuffScript * NodeLimit + BuffDefine.Type_DecCurHpRate; //掉血当前值万分比
    public final static int AddHpBuff = BuffScript * NodeLimit + BuffDefine.Type_AddHp; //治疗
    public final static int AddAllHpRateBuff = BuffScript * NodeLimit + BuffDefine.Type_addAllHpRate; //治疗总值万分比
    public final static int AddCurHpRateBuff = BuffScript * NodeLimit + BuffDefine.Type_addCurHpRate; //治疗当前值万分比
    public final static int SuperManBuff = BuffScript * NodeLimit + BuffDefine.Type_SuperMan; //霸体状态
    public final static int MoneyRateBuff = BuffScript * NodeLimit + BuffDefine.Type_MoneyRate; //金币倍率 param1：倍率万分比
    public final static int ExpRateBuff = BuffScript * NodeLimit + BuffDefine.Type_ExpRate; //经验倍率 param1：倍率万分比
    public final static int GuiyingBuff = BuffScript * NodeLimit + BuffDefine.Type_Guiying; //鬼影buff
    public final static int SuperPveBuff = BuffScript * NodeLimit + BuffDefine.Type_SuperPveBuff; //击杀周围怪物
    public final static int RoleInvisibleBuff = BuffScript * NodeLimit + BuffDefine.Type_RoleInvisible; //角色隐身
    public final static int DINGBuff = BuffScript * NodeLimit + BuffDefine.Type_DING; //定身BUFF
    public final static int MiaoKangBuff = BuffScript * NodeLimit + BuffDefine.Type_MiaoKang; //免控BUFF
    public final static int ReDamageFromBossBuff = BuffScript * NodeLimit + BuffDefine.Type_ReDamageFromBoss; //boss收到的伤害按比例施加到玩家身上
    public final static int BigBoomBuff = BuffScript * NodeLimit + BuffDefine.Type_BigBoom; //给player或者monster挂一个,结束时候炸周围的人固定伤害
    public final static int PosTriggerBuff = BuffScript * NodeLimit + BuffDefine.Type_PosTriggerBuff; //位置触发事件的buff
    public final static int TriggerSummonBuff = BuffScript * NodeLimit + BuffDefine.Type_TriggerSummonBuff; //触发召唤物的buff
    public final static int DizzinessBuff = BuffScript * NodeLimit + BuffDefine.Type_Dizziness; //眩晕的BUFF
    public final static int BianshenBuff = BuffScript * NodeLimit + BuffDefine.Type_Bianshen; //变身buff
    public final static int ChuandaoBuff = BuffScript * NodeLimit + BuffDefine.Type_Chuandao; //传道buff
    public final static int FeijianBuff = BuffScript * NodeLimit + BuffDefine.Type_Feijian; //飞剑buff
    public final static int JinliaoBuff = BuffScript * NodeLimit + BuffDefine.Type_jinliao; //禁疗buff


    ////////////////////////////////////  运营活动 脚本/////////////////////////////////////////////
    /**
     * 活跃兑换奖励
     */
    public final static int GetActiveActivityScript = getActivityScriptId(ActivityType.GetActive);

    /**
     * 每日充值
     */

    public final static int DailyRechargeActivityScript = getActivityScriptId(ActivityType.DailyRecharge);

    /**
     * 限时登陆奖励
     */
    public final static int LimitTimeLoginActivityScript = getActivityScriptId(ActivityType.LimitTimeLogin);


    /**
     * 招财猫运营活动脚本
     */
    public final static int LuckyCatActivityScript = getActivityScriptId(ActivityType.LuckyCat);

    /**
     * 天帝宝库活动
     */
    public final static int DrawRewardActivityScript = getActivityScriptId(ActivityType.DrawReward);

    /**
     * 团购活动
     */
    public final static int GroupBuyActivityScript = getActivityScriptId(ActivityType.GroupBuy);

    /**
     * 限时累充
     */
    public final static int RechargeTotalActivityScript = getActivityScriptId(ActivityType.LimitedTotalRecharge);

    /**
     * 限时累计消耗
     */
    public final static int LimitedTotalConsumeActivityScript = getActivityScriptId(ActivityType.LimitedTotalConsume);

    /**
     * 限时累计消耗
     */
    public final static int LimitedTGiftBagActivityScript = getActivityScriptId(ActivityType.LimitGiftBag);

    /**
     * 节日限时礼包
     */
    public final static int HolidayDailyGiftScript = getActivityScriptId(ActivityType.HolidayDailyGift);

    /**
     * 节日许愿
     */
    public final static int FestivalWishActivityScript = getActivityScriptId(ActivityType.FestivalWish);

    /**
     * 春节祝福签到
     */
    public final static int XinNianZhuFuScript = getActivityScriptId(ActivityType.XinNianZhuFu);

    /**
     * 集物兑换
     */
    public final static int CollectGoodsExChangeScript = getActivityScriptId(ActivityType.CollectGoodsExChange);

    /**
     * 集物兑换
     */
    public final static int JumpGridScript = getActivityScriptId(ActivityType.ZhiTouzi);

    /**
     * 首领狂欢（圣诞）
     */
    public final static int HolidayBossScript = getActivityScriptId(ActivityType.HolidayBoss);
    /**
     * 庆典任务
     */
    public final static int HolidayTaskScript = getActivityScriptId(ActivityType.HolidayTask);
    /**
     * 节日集字
     */
    public final static int HolidayWordsScript = getActivityScriptId(ActivityType.HolidayWords);
    /**
     * 积分排名
     */
    public final static int HolidayScoreRankScript = getActivityScriptId(ActivityType.HolidayScoreRank);
    /**
     * 节日特惠
     */
    public final static int FestivalPreferenceScript = getActivityScriptId(ActivityType.FestivalPreference);

    /**
     * 连续累充
     */
    public final static int ContinuousRechargeScript = getActivityScriptId(ActivityType.ContinuousRecharge);


    /**
     * 限时商城
     */
    public final static int LimitShopActivtyScript = getActivityScriptId(ActivityType.LimitShopActivty);

    /**
     * FB分享
     */
    public final static int FBShareActivityScript = getActivityScriptId(ActivityType.FBShare);

    /**
     * 连续累充2
     */
    public final static int ContinuousRecharge2Script = getActivityScriptId(ActivityType.ContinuousRecharge2);

    /**
     * 外观展示
     */
    public final static int ShowScript = getActivityScriptId(ActivityType.Show);

    /**
     * 上线图片提示
     */
    public final static int PicTipsScript = getActivityScriptId(ActivityType.PicTips);

    /**
     * 聚宝盆
     */
    public final static int CornucopiaScript = getActivityScriptId(ActivityType.Cornucopia);

    /**
     * 砸金蛋
     */
    public final static int SmashEggScript = getActivityScriptId(ActivityType.SmashEgg);

    /**
     * 招财猫2运营活动脚本
     */
    public final static int LuckyCat2ActivityScript = getActivityScriptId(ActivityType.LuckyCat2);
    /**
     * 方泽探宝
     */
    public final static int FZTBActivityScript = getActivityScriptId(ActivityType.FZTB);
    //----------------------预留活动脚本ID----------------------
    public final static int TempleActivityScript1 = getActivityScriptId(ActivityType.TempleActivity1);
    public final static int TempleActivityScript2 = getActivityScriptId(ActivityType.TempleActivity2);
    public final static int TempleActivityScript3 = getActivityScriptId(ActivityType.TempleActivity3);
    public final static int TempleActivityScript4 = getActivityScriptId(ActivityType.TempleActivity4);
    public final static int TempleActivityScript5 = getActivityScriptId(ActivityType.TempleActivity5);
    public final static int TempleActivityScript6 = getActivityScriptId(ActivityType.TempleActivity6);
    public final static int TempleActivityScript7 = getActivityScriptId(ActivityType.TempleActivity7);
    public final static int TempleActivityScript8 = getActivityScriptId(ActivityType.TempleActivity8);
    public final static int TempleActivityScript9 = getActivityScriptId(ActivityType.TempleActivity9);
    public final static int TempleActivityScript10 = getActivityScriptId(ActivityType.TempleActivity10);
    public final static int TempleActivityScriptMax = getActivityScriptId(ActivityType.TempleActivityMax);

    public static int getActivityScriptId(int actType) {
        return ActivityScriptBaseScript * NodeLimit + actType;
    }
}
