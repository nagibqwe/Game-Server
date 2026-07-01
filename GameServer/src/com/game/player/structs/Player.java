package com.game.player.structs;

import com.data.FunctionVariable;
import com.data.bean.Cfg_Skill_Bean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.achievement.structs.AchievementInfo;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.auction.structs.AuctionRecord;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.base.BI;
import com.game.boss.struct.Boss;
import com.game.boss.struct.BossData;
import com.game.cangbaoge.struct.SuperrewardData;
import com.game.chum.struct.ChumData;
import com.game.commercialize.struct.DailyRechargeData;
import com.game.commercialize.struct.FCCharge;
import com.game.cooldown.structs.Cooldown;
import com.game.copymap.structs.SingleTowerData;
import com.game.count.structs.Count;
import com.game.count.structs.ICount;
import com.game.dailyactive.structs.DailyActiveData;
import com.game.devilseries.structs.Devil;
import com.game.drop.structs.BossRelationDropLimit;
import com.game.equip.struct.EquipPart;
import com.game.equip.struct.SpiritData;
import com.game.fallingsky.struct.FallingSkyData;
import com.game.functionTask.struct.FunctionTaskData;
import com.game.godbook.struct.Amulet;
import com.game.gold.structs.Gold;
import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildMember;
import com.game.holyEquip.struct.HolyEquipBaseInfo;
import com.game.home.structs.Home;
import com.game.hook.struct.HookInfo;
import com.game.horse.structs.Horse;
import com.game.huaxinflysword.structs.FlyswordAllInfo;
import com.game.immortalequip.structs.ImmortalEquipPart;
import com.game.immortalsoul.structs.Immortalsoul;
import com.game.kaoshangling.struct.KaoShangLingData;
import com.game.mail.structs.MailData;
import com.game.manager.Manager;
import com.game.map.structs.MapGps;
import com.game.map.structs.MapUtils;
import com.game.marriage.struct.*;
import com.game.nature.structs.HuaxinEntity;
import com.game.nature.structs.Nature;
import com.game.nature.structs.Weapon;
import com.game.newfashion.structs.PlayerNewFashion;
import com.game.ninedaysfocused.structs.NineDaysPlayerData;
import com.game.openserverac.structs.*;
import com.game.pet.structs.ActivePet;
import com.game.player.script.IPlayerBattle;
import com.game.recharge.structs.RechargeDiscount;
import com.game.redpacket.structs.RedPacketEnum;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.shihai.struct.ShiHaiData;
import com.game.shop.structs.FreeShop;
import com.game.shop.structs.LimitShop;
import com.game.shop.structs.NewFreeShop;
import com.game.skill.structs.MentalSkill;
import com.game.skill.structs.Skill;
import com.game.skill.structs.SkillCellAtt;
import com.game.skill.structs.SkillData;
import com.game.soulArmor.struct.SoulArmor;
import com.game.soulbeast.structs.PlayerSoulBeastInfo;
import com.game.statestifle.structs.PlayerSateStifleData;
import com.game.statevip.structs.StateVip;
import com.game.structs.AttributeType;
import com.game.structs.Entity;
import com.game.structs.EntityState;
import com.game.structs.Fighter;
import com.game.task.structs.*;
import com.game.title.structs.TitleData;
import com.game.treasurehunt.struct.TreasuryHuntData;
import com.game.treasurehuntwuyou.struct.TreasureHuntWuyouData;
import com.game.treasurehuntxianjia.struct.TreasureHuntXianjiaData;
import com.game.unrealEquip.struct.UnrealEquipBaseInfo;
import com.game.vip.structs.Agate;
import com.game.vip.structs.SpecialVipStateBean;
import com.game.vip.structs.VipPearl;
import com.game.welfare.struct.*;
import game.core.map.IMapObject;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.AutoIncrementLongArray;
import game.core.util.TimeUtils;
import io.netty.channel.ChannelHandlerContext;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 玩家
 */
public class Player extends Entity implements Comparable<Player>, Cloneable, ICount {

    //region   transient 成员(JsonIgnore)
    @JsonIgnore
    private final transient ConcurrentHashMap<Long, Long> pklist = new ConcurrentHashMap<>();//自卫列表
    @JsonIgnore
    private final transient ConcurrentHashMap<PlayerAttributeType, BaseIntAttribute> playerCalculators = new ConcurrentHashMap<>();//各系统属性
    @JsonIgnore
    private final transient ConcurrentHashMap<PlayerAttributeType, BaseSystemIntAttribute> playerCalSystemCulators = new ConcurrentHashMap<>();//各系统属性增加百分比
    @JsonIgnore
    public final transient PlayerCrossData playerCrossData = new PlayerCrossData();//跨服信息
    @JsonIgnore
    public transient long targetDeferId = 0;//客户端发上来的主要攻击目标
    @JsonIgnore
    protected transient Position moveDir = null; //移动方向
    @JsonIgnore
    protected transient ConcurrentHashMap<String, Cooldown> cooldowns = new ConcurrentHashMap<>();//冷却列表, 不要保存此表的数据了
    @JsonIgnore
    private transient long saveToDBTime;//保存玩家数据到数据库的时间
    @JsonIgnore
    private transient boolean saveToDB = false;//true玩家数据需保存到DB
    @JsonIgnore
    private transient int deleteTime = 0; // 删除时间，0表示未删除
    @JsonIgnore
    private transient byte isOnline = 0; // 是否在线,0不在线1在线
    @JsonIgnore
    transient volatile private ChannelHandlerContext iosession;//socket
    @JsonIgnore
    private transient ConcurrentHashMap<Long, Long> enemys = new ConcurrentHashMap<>();  //仇恨列表
    @JsonIgnore
    private transient ConcurrentHashMap<Long, Long> beEnemys = new ConcurrentHashMap<>(); //被自卫列表(Id, lastTime)
    @JsonIgnore
    private transient int[] matchRobot; //匹配的机器人信息
    @JsonIgnore
    private transient float expRate = 1f; //经验加成
    @JsonIgnore
    private transient float moneyRate = 1f; //金币加成
    @JsonIgnore
    private transient Gold gold;//元宝
    @JsonIgnore
    private transient int createServerID; //创建角色时的服务器id
    @JsonIgnore
    private transient boolean testPos = false; //GM测试用
    @JsonIgnore
    private transient long lastWorldHelpTime = 0; //上一次使用世界支援的时间
    @JsonIgnore
    private transient long lastCallChumTime = 0; //上一次挚友召唤的时间
    @JsonIgnore
    private transient long lastRefreshSignTime = 0;//上次刷新sign时间
    @JsonIgnore
    private transient long reconnectTime = 0;//Sign = md5(roleId + userId + reconnectTime),reconnectTime每10分钟刷新下
    @JsonIgnore
    private transient String loginIP; // 登录ＩＰ
    @JsonIgnore
    private transient ConcurrentHashMap<Long, MailData> mailCacheMap = new ConcurrentHashMap<>(); //玩家邮件缓存map，最多缓存50封邮件

    private transient long oldFightPoint; //玩家战斗力
    private transient long fightPoint; //玩家战斗力
    @JsonIgnore
    private transient int marriagedType;//求婚类型
    @JsonIgnore
    private transient long marriageProposeId;//求婚者id
    @JsonIgnore
    private transient List<Long> unfilters = new ArrayList<>();
    @JsonIgnore
    private transient long brithProtect = 0; //出生保护
    @JsonIgnore
    private transient int expSyncTime = 0; //经验发生变化同步到排行榜的时间
    @JsonIgnore
    private transient int vipLv = 0;  //vip等级
    @JsonIgnore
    private transient HuaxinEntity curHuaxinEntity = null;
    @JsonIgnore
    private transient List<Wedding> wedding = new ArrayList<>();
    @JsonIgnore
    private transient int childId = 0;  //出战仙娃ID
    @JsonIgnore
    private transient HashSet<Integer> noodDrop = new HashSet<>();
    @JsonIgnore
    private transient boolean GM = false; //Gm
    //endregion

    private boolean isSendTaskInfo = false;
    //region  需要序列化存DB 的数据

    private final ConcurrentHashMap<Integer, Skill> skills = new ConcurrentHashMap<>();//技能列表
    public ConcurrentHashMap<Integer, Boolean> functionState = new ConcurrentHashMap<>();//系统开放列表
    private String platUserId;//平台的账号名（有可能是渠道的登录ID）
    private long userId; // 账号ＩＤ
    private String platformName; // 平台名
    private String os;//玩家的接口系统
    private String maCode;//机器设备码
    private String uuid; // funcell生成的uuid
    private byte career; // 职业
    private int xsGrade;    //洗髓阶
    private int xsLevel;    //洗髓级
    private long lastLoginTime; // 上次登录时间
    private int lastLoginLevel; //上次登录等级
    private long offLineTime = TimeUtils.Time(); //离线时间,0表示没有离线
    private byte sex;//性别
    private int languageType; //语言类型，0：中文简体，1：中文繁体，2：泰文，3：越南文，4：韩文 等等
    private MapGps old = new MapGps();//原位置
    private float height = 1f; //玩家飞行高度
    private int rollLevel = 1; //翻滚等级
    private long teamId = 0; //队伍ID
    private long lastDropExp = 0;//本次累加的掉落经验值
    private long lastDropTime = 0;//上一次更新的时间
    private String showSid = "";//显示服务器ID值
    private int mainGuide = 0;//主界面引导编号
    private List<Integer> resolveSettings = new ArrayList<>();//装备的自动分解
    private boolean autoRecycle;//自动熔炼低品质装备
    private boolean isCertify;//实名认证标识
    private ConcurrentHashMap<Integer, Boolean> settings = new ConcurrentHashMap<>();//玩家设置
    private BI bi = new BI();   //bi
    private long jjcCdTime = 0;
    private long jjcStartTime = 0;
    private int jjcHistoryMaxRank = 0; //竞技场最高的历史排名
    private int jjcHistoryMaxIndex = 0; //竞技场最高历史排名所在的级别
    private List<Integer> jjcFirstRewardList = new ArrayList<>(); //竞技场已经领取过的首次排名奖励列表
    private ConcurrentHashMap<Long, Immortalsoul> bagImmortalsoul = new ConcurrentHashMap<>(); //仙魂列表
    private ConcurrentHashMap<Integer, Immortalsoul> equipSouls = new ConcurrentHashMap<>(); //已装备仙魂列表
    private ConcurrentHashMap<Integer, Integer> ImmortalsoulCores = new ConcurrentHashMap<>(); //仙魄核心
    private ActivePet activePet = new ActivePet(); //宠物数据
    private long guildId;//公会Id
    private String guildName; //工会名称
    private long quitGuildTime; //退出公会时间
    private int pkState = PlayerDefine.PkStatePeace; //当前PK模式（0为和平，1为组队，2为帮派，3为全体）
    private BigInteger totalExp = new BigInteger("0");

    private int like;//0未点赞,1已点赞,2已领取
    private int share;//0未分享,1已分享,2已领取
    private int evaluate;//0未评价,1已评价

    private int platformEvaluateEveryDayShare; //每日分享未分享,1已分享,2已领取
    private String platformEvaluateEveryDayShareDay;//每日分享时间记录

    public void setShopEvaluate(int shopEvaluate) {
        this.shopEvaluate = shopEvaluate;
    }

    private int shopEvaluate;//0未评价,1已评价


    private String customHeadPath; //自定义头像路径
    private boolean useCustomHead = true; // 是否使用自定义头像 1 表示使用



    //------------------显示隐藏部分begin--------------------//
    //显示set
    private HashSet<Integer> showSet = new HashSet<>();
    //隐藏set
    private HashSet<Integer> hideSet = new HashSet<>();
    //------------------显示隐藏部分end--------------------//
    //-------------------任务---------------------------//
    //当前未完成的主线任务
    private List<MainTask> curMainTasks = new ArrayList<>();
    //当前未完成的日常、周常任务
    private ConcurrentHashMap<Integer, DailyTask> curDailyTasks = new ConcurrentHashMap<>();
    //当前未完成的帮会日常、周常任务
    private ConcurrentHashMap<Integer, ConquerTask> curConquerTasks = new ConcurrentHashMap<>();
    //公会任务
    private GuildTaskPool guildTaskPool = new GuildTaskPool();
    //家园
    Home home = new Home();
    //当前未完成的支线任务
    private List<BranchTask> curBranchTask = new ArrayList<>();
    //当前的转职任务
    private GenderTask curGenderTask = new GenderTask();
    private List<Integer> overMainTaskIDs = new ArrayList<>();
    private List<Integer> overGenderTaskIds = new ArrayList<>();
    private int curMainTaskId = 0;
    private boolean isFirstGetTask = true;
    //普通日常、周常完成数量、接取时间
    private HashMap<Integer, Integer> dailyTaskCount = new HashMap<>();
    private HashMap<Integer, Long> dailyTaskTime = new HashMap<>();
    //日常任务完成列表<日常类型,完成的任务ID>
    private HashMap<Integer, List<Integer>> dailyTaskFinishIds = new HashMap<>();
    //帮会日常、周常完成数量、接取时间
    private HashMap<Integer, Integer> conquerTaskCount = new HashMap<>();
    private HashMap<Integer, Long> conquerTaskTime = new HashMap<>();
    //上一个普通日常任务id，随机任务时不出现上一次已经做过的
    private HashMap<Integer, Integer> lastDailyTaskId = new HashMap<>();
    //上一个帮会周常日常任务id，随机任务时不出现上一次已经做过的
    private HashMap<Integer, Integer> lastConquerTaskId = new HashMap<>();
    private Set<Integer> branchOverList = new HashSet<>();
    //任务目标阶段
    private int taskTargetStage = 1;
    //背包物品
    private ConcurrentHashMap<Integer, Item> backpackItems = new ConcurrentHashMap<>();
    //仓库物品
    private ConcurrentHashMap<Integer, Item> storeItems = new ConcurrentHashMap<>();
    //宠物装备背包
    private ConcurrentHashMap<Long, Item> petEquipPackItems = new ConcurrentHashMap<>();
    /**
     * 坐骑装备背包
     */
    private ConcurrentHashMap<Long, Item> horseEquipPackItems = new ConcurrentHashMap<>();
    //魔魂系统背包
    private ConcurrentHashMap<Long, Item> devilPackItems = new ConcurrentHashMap<>();
    //魔魂信息
    private Devil devil = new Devil();
    // 包裹已开格子数
    private int bagCellsNum;
    // 仓库已开格子数
    private int storeCellsNum;
    // 包裹开格时间统计
    private int bagCellTimeCount;

    //钱
    private AutoIncrementLongArray currencys = new AutoIncrementLongArray(ItemCoinType.ItemCoinMaxId - 1);
    //历史货币上限值
    private AutoIncrementLongArray historyCoin = new AutoIncrementLongArray(ItemCoinType.ItemCoinMaxId - 1);
    private int moonCardDays;//月卡结束时的到1970年1月1日的天数
    private boolean lifeCard;//是否是终生特权卡
    private PlayerSoulBeastInfo soulBeastInfo = new PlayerSoulBeastInfo(); //魂兽信息类

    ////////////////////////////////////////////////////////////////////
    //////  福利相关
    ////////////////////////////////////////////////////////////////////
    // 登陆礼包
    private LoginGift loginGift = LoginGift.newLoginGift();
    // 每日签到
    private DayCheckIn dayCheckIn = DayCheckIn.newDayCheckIn();
    // 月卡尊享卡
    private List<ExclusiveCard> exclusiveCards = new ArrayList<>();
    // 成长基金
    private GrowthFund growthFund = GrowthFund.newGrowthFund();
    // 巅峰基金
    private GrowthFund investPeak = GrowthFund.newGrowthFund();
    // 首充、续充
    private FCCharge fcCharge = FCCharge.New();
    // 等级礼包
    private List<Integer> levelGifts = new ArrayList<>();

    // 等级vip礼包
    private List<Integer> levelVipGifts = new ArrayList<>();

   //最后一次领取奖励时间
    private long lastWelfareFreeGiftTime;

    // 今日完成的资源情况
    private RetrieveResData curRRD = new RetrieveResData(this.getVipLv());
    // 上次完成的资源情况
    private RetrieveResData lastRRD = new RetrieveResData(this.getVipLv());
    // 神秘限购
    private LimitShop limitShop = LimitShop.newLimitShop();
    // 神秘商店
    private LimitShop steryShop = LimitShop.newLimitShop();

    //计数列表
    private ConcurrentHashMap<String, Count> counts = new ConcurrentHashMap<>();
    //玩家新版技能
    private ConcurrentHashMap<Integer, SkillCellAtt> skillCells = new ConcurrentHashMap<>();
    /**
     * 装备部位，比如头，肩，胸等
     * 由于在当前项目，所有的强化都是针对于部位的，故需要把部位这个概念独立出来
     * 下标代表部位，装备部位(0头盔.1武器.2胸甲.3项链.4腰带.5腿甲.6鞋子.7戒指)
     */
    private List<EquipPart> equipParts = new ArrayList<>();

    //是否显示装备星级特效
    private boolean isShowEquipStar = true;
    //累计在线总时间,单位秒
    private int accunonlinetime;
    //累计登陆天数
    private int accumOnlineDays;
    private int clearLevel = 0;
    //玩家坐骑
    private Horse horse = new Horse();
    /**
     * 玩家翅膀
     */
    private Nature wing = new Nature();
    /**
     * 翅膀状态0未初始化 1已初始化
     */
    private int wingStatus;
    /**
     * 玩家法器
     */
    private Nature talisman = new Nature();
    /**
     * 玩家阵法
     */
    private Nature magic = new Nature();
    /**
     * 玩家神兵
     */
    private Weapon weapon = new Weapon();
    /**
     * 玩家寻宝数据
     */
    private TreasuryHuntData treasuryHuntData = new TreasuryHuntData();
    /**
     * 玩家圣装数据
     */
    private HolyEquipBaseInfo holyEquipBaseInfo = new HolyEquipBaseInfo();
    /**
     * 玩家仙甲寻宝数据
     */
    private TreasureHuntXianjiaData treasureHuntXianjiaData = new TreasureHuntXianjiaData();
    /**
     * 无忧宝库数据
     */
    private TreasureHuntWuyouData treasureHuntWuyouData = new TreasureHuntWuyouData();

    /**
     * 天禁令
     */
    private FallingSkyData fallingSkyData = new FallingSkyData();


    /**
     * 幻装
     */
    private UnrealEquipBaseInfo unrealEquipBaseInfo = new UnrealEquipBaseInfo();

    public UnrealEquipBaseInfo getUnrealEquipBaseInfo() {
        return unrealEquipBaseInfo;
    }

    public void setUnrealEquipBaseInfo(UnrealEquipBaseInfo unrealEquipBaseInfo) {
        this.unrealEquipBaseInfo = unrealEquipBaseInfo;
    }

    //属性药增加的属性
    private BaseIntAttribute medicinesAttribute = new BaseIntAttribute(AttributeType.ATTR_MAX);
    private BaseSystemIntAttribute medicinesAttributeSys = new BaseSystemIntAttribute(AttributeType.SystemAttr_Max);
    private ShiHaiData shiHaiData = new ShiHaiData();
    //已领取的万能激活码列表
    private List<String> usedActiveCodeList = new ArrayList<>();

    private long universeLastTime = 0;

    private boolean commentFlag = false;  //是否领取评论有奖
    private PlayerNewFashion newFashionData = new PlayerNewFashion();
    /**
     * 日常活动数据
     */
    private DailyActiveData dailyActiveData = new DailyActiveData();
    /**
     * 成就数据
     */
    private ConcurrentHashMap<Integer, AchievementInfo> achievementInfo = new ConcurrentHashMap<>();
    /**
     * 天书符咒数据
     */
    private ConcurrentHashMap<Integer, Amulet> godBookInfo = new ConcurrentHashMap<>();

    /**
     * 称号数据
     */
    private TitleData titleData = new TitleData();

    /**
     * 装备收集数据<灵体id, 收集装备list>
     */
    private SpiritData spiritData = new SpiritData();

    private int worshipDay; //崇拜时间（1970至今的天数）
    private HashSet<Long> worshipRoleIdSet = new HashSet<>(); //今日已崇拜玩家Id列表
    /////////////消息分级处理/////////////////
    /**
     * ******************************************充值相关*********************************************
     */
    private PlayerRedPacket redPacket = new PlayerRedPacket();        //红包

    private long firstRechargeTime = 0;
    private long lastRechargeTime = 0;
    //新手充值开启时间
    private long newbieRechargeTime = 0;

    private ConcurrentHashMap<Integer, RechargeDiscount> rechargeDiscounts = new ConcurrentHashMap<>();  //超值折扣
    private ConcurrentHashMap<Integer, RechargeDiscount> goldDiscounts = new ConcurrentHashMap<>();  //超值折扣(元宝版本)
    private long lastDiscFreeGiftTime;              //免费超值折扣领取时间
    private long lastGoldDiscFreeGiftTime;
    /**
     * ******************************************投资相关*********************************************
     */
    private List<Integer> loginDays = new ArrayList<>();                    //最近登录离1970的天数，只保留7条，用于条件活动检查
    //-----------------------------------------------------------------------------------------------
    private int forbid;//禁止登录时间
    //------------------------------------------------------------------------------------------------
    //////////////////////////天启宝库//////////////////////////////////
    private List<Integer> followedBossList = new ArrayList<>(); //玩家已关注的boss列表
    private List<Integer> autoFollowedBossList = new ArrayList<>(); //玩家升级自动关注的boss列表
    private List<Integer> fuDiFollowedBossList = new ArrayList<>();//福地关注boss列表:福地配置表id
    private int useIconState;//自定义头像状态
    //3.0.0 增加的血脉系统
    private Blood blood = new Blood();
    //九天争锋个人数据
    private NineDaysPlayerData personalData = null;
    /**
     * 玩家分享奖励数据 < shareId, count >
     */
    private ConcurrentHashMap<Integer, Integer> shareMap = new ConcurrentHashMap<>();
    /**
     * 挂机
     */
    private HookInfo hookInfo = new HookInfo();

    /**
     * 境界等级
     */
    private StateVip stateVip = new StateVip();

    /**
     * 境界任务列表
     */
    private HashMap<Integer, Boolean> stateVips = new HashMap<>();

    /**
     * 万妖卷
     */
    private SingleTowerData singleTowerData = new SingleTowerData();

    /**
     * 挚友单日数据
     */
    private ChumData chumData = ChumData.newClass();

    /**
     * 个人无限层boss
     */
    private HashMap<Integer,Boss> unLimitBoss = new HashMap<>();

    /**
     * 次数特殊掉落
     */
    private HashMap<Integer, BossRelationDropLimit> relationDropLimits = new HashMap<>();

    /**
     * 开关已领奖状态
     */
    private Set<Integer> funcRewardState = new HashSet<>();

    /**
     * 开服狂欢领奖状态
     */
    private HashMap<Integer, OpenServerRevel> openServerRevelMap = new HashMap<>();

    /**
     * 开服成长之路
     */
    private OpenServerGrowUp openServerGrowUp = new OpenServerGrowUp();

    /**
     * V4返利
     */
    private VIP4Rebate vip4Rebate = new VIP4Rebate();

    /**
     * 开服特殊活动
     */
    private OpenSeverSpec openServerSpec = new OpenSeverSpec();

    /**
     * 开服预告每日奖励领取
     */
    private boolean osDailyRewardGet = false;

    /**
     * 最近的开服每日功能预告奖励重置时间
     */
    private long lastOpsFuncRewardTime = TimeUtils.Time();

    /**
     * boss首杀数据
     */
    private ConcurrentHashMap<Integer, Integer> firstKillData = new ConcurrentHashMap<>();

    /**
     * boss首杀领取红包
     */
    private ArrayList<Integer> firstKillRedPacket = new ArrayList<>();

    /**
     * 新服活动数据
     */
    private ConcurrentHashMap<Integer, NewServerActInfo> actInfo = new ConcurrentHashMap<>();

    /**返利宝箱*/
    private RebateBoxData rebateBoxData = new RebateBoxData();

    /**仙盟争霸*/
    private Map<Integer, GuildBattleData> guildBattleData = new ConcurrentHashMap<>();

    /**
     * 玩家的境界灵压数据
     */
    private PlayerSateStifleData stifleData = new PlayerSateStifleData();

    /**
     * 境界boss 层数
     */
    private int stateBossCurrLayer = 0;
    /**
     * 境界boss首通领奖过的层数
     */
    private Set<Integer> firstLayers = new HashSet<>();

    /**
     * 个人世界boss数据
     */
    private ConcurrentHashMap<Integer, Boss> personWorldBoss = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, BossData> BossDataMap = new ConcurrentHashMap<>();

    /**
     * 拍卖行记录
     */
    private List<AuctionRecord> auctionRecords = new ArrayList<>();

    /**
     * 心法技能列表
     */
    private ConcurrentHashMap<Integer, MentalSkill> mentalskills = new ConcurrentHashMap<>();//心法技能列表

    /**
     * 新技能结构
     */
    private SkillData newSkillData = new SkillData();

    /**
     * VIP宝珠数据
     */
    private VipPearl vipPearl = new VipPearl();  //VIP宝珠

    /**
     * vip已经购买礼包
     */
    private int purVipGift = 0;

    /**
     * vip充值奖励
     */
    private int vipRechargeReward = 0;

    /**
     * 当前服务器ID
     */
    private int curServerId = 0;

    /**
     * 0元购
     */
    private ConcurrentHashMap<Integer, FreeShop> freeShopMap = new ConcurrentHashMap<>();

    /**
     * 新0元购
     */
    private ConcurrentHashMap<Integer, NewFreeShop> newFreeShopMap = new ConcurrentHashMap<>();

    /**
     * 魂甲系统
     */
    private SoulArmor soulArmor = new SoulArmor();

    /**
     * 仙甲 装备  部位
     */
    private ConcurrentHashMap<Integer, ImmortalEquipPart> immortalEquipPartLisit = new ConcurrentHashMap<>();

    /**
     * 仙甲 外观
     */
    private ConcurrentHashMap<Integer, Integer> immortalEquipFacadeMap = new ConcurrentHashMap<>();

    /**
     * 仙甲装备 背包
     */
    private ConcurrentHashMap<Long, Item> ImmEquipItemList = new ConcurrentHashMap<>();

    /**
     * 新剑灵
     */
    private FlyswordAllInfo flyswordAllInfo = new FlyswordAllInfo();

    /**
     * 是否领取VIP免费送的奖励
     */
    private boolean isGetVipFreeAward = false;
    private boolean isDownLoadOver = false; //是否下载完


    //记录最后的vip等级
    private int lastVipLevel;

    //
    private SpecialVipStateBean specialVipStateBean = new SpecialVipStateBean();


    private Agate agate = new Agate(); //聚宝盆

    private long marriageUid = 0;   //仙缘ID
    private MarryLoveLock marryLock = new MarryLoveLock();    //仙缘.心锁
    private MarryBox marryBox = new MarryBox();               //仙缘.仙匣
    private HashMap<Integer, MarryChild> childs = new HashMap<>();  //仙缘.仙娃
    private MarryActivity marryActivity = new MarryActivity();



    /**
     * vip助力
     */
    private V4HelpData v4HelpData = new V4HelpData();

    /**
     * 副本设置
     * key:     副本id
     * value:   bool位运算的整型值
     */
    private HashMap<Integer, Integer> cloneSetting = new HashMap<>();

    /**功能任务数据*/
    private FunctionTaskData functionTaskData = new FunctionTaskData();

    //region 一周福利抽奖数据
    //抽奖次数
    private int luckyDrawWeekTimes = 0;
    //手动替换的奖励索引列表,其中key表示奖励等级0:特等,1一等,2:二等,3,三等
    private HashMap<Integer, List<Integer>> luckyDrawWeekCustomAwards = new HashMap<>();
    //领取抽奖卷的数据
    private HashMap<Integer, Boolean> luckyDrawWeekAwardVolumes = new HashMap<>();

    /**活动排行榜奖励领取过的奖励id*/
    private Set<Integer> activityRankAwardGet = new HashSet<>();

    private SuperrewardData superrewardData = new SuperrewardData();

    public SuperrewardData getSuperrewardData() {
        return superrewardData;
    }

    public void setSuperrewardData(SuperrewardData superrewardData) {
        this.superrewardData = superrewardData;
    }

    private DailyRechargeData dailyRechargeData = new DailyRechargeData();

    public DailyRechargeData getDailyRechargeData() {
        return dailyRechargeData;
    }

    public void setDailyRechargeData(DailyRechargeData dailyRechargeData) {
        this.dailyRechargeData = dailyRechargeData;
    }

    //endregion


    public SoulArmor getSoulArmor() {
        return soulArmor;
    }

    public void setSoulArmor(SoulArmor soulArmor) {
        this.soulArmor = soulArmor;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public HashMap<Integer, MarryChild> getChilds() {
        return childs;
    }

    public void setChilds(HashMap<Integer, MarryChild> childs) {
        this.childs = childs;
    }

    public ConcurrentHashMap<Integer, RechargeDiscount> getRechargeDiscounts() {
        return rechargeDiscounts;
    }

    public void setRechargeDiscounts(ConcurrentHashMap<Integer, RechargeDiscount> rechargeDiscounts) {
        this.rechargeDiscounts = rechargeDiscounts;
    }

    public ConcurrentHashMap<Integer, RechargeDiscount> getGoldDiscounts() {
        return goldDiscounts;
    }

    public void setGoldDiscounts(ConcurrentHashMap<Integer, RechargeDiscount> goldDiscounts) {
        this.goldDiscounts = goldDiscounts;
    }

    public MarryActivity getMarryActivity() {
        return marryActivity;
    }

    public void setMarryActivity(MarryActivity marryActivity) {
        this.marryActivity = marryActivity;
    }

    public MarryBox getMarryBox() {
        return marryBox;
    }

    public void setMarryBox(MarryBox marryBox) {
        this.marryBox = marryBox;
    }

    public MarryLoveLock getMarryLock() {
        return marryLock;
    }

    public void setMarryLock(MarryLoveLock marryLock) {
        this.marryLock = marryLock;
    }

    public ConcurrentHashMap<Integer, FreeShop> getFreeShopMap() {
        return freeShopMap;
    }

    public void setFreeShopMap(ConcurrentHashMap<Integer, FreeShop> freeShopMap) {
        this.freeShopMap = freeShopMap;
    }

    public ConcurrentHashMap<Integer, NewFreeShop> getNewFreeShopMap() {
        return newFreeShopMap;
    }

    public void setNewFreeShopMap(ConcurrentHashMap<Integer, NewFreeShop> newFreeShopMap) {
        this.newFreeShopMap = newFreeShopMap;
    }

    public FlyswordAllInfo getFlyswordAllInfo() {
        return flyswordAllInfo;
    }

    public void setFlyswordAllInfo(FlyswordAllInfo flyswordAllInfo) {
        this.flyswordAllInfo = flyswordAllInfo;
    }

    public HuaxinEntity getCurHuaxinEntity() {
        return curHuaxinEntity;
    }

    public void setCurHuaxinEntity(HuaxinEntity curHuaxinEntity) {
        this.curHuaxinEntity = curHuaxinEntity;
    }

    public boolean isDownLoadOver() {
        return isDownLoadOver;
    }

    public List<Wedding> getWedding() {
        return wedding;
    }

    public void setWedding(List<Wedding> wedding) {
        this.wedding = wedding;
    }

    public void setDownLoadOver(boolean downLoadOver) {
        isDownLoadOver = downLoadOver;
    }

    public long getMarriageUid() {
        return marriageUid;
    }

    public void setMarriageUid(long marriageUid) {
        this.marriageUid = marriageUid;
    }

    public Agate getAgate() {
        return agate;
    }

    public void setAgate(Agate agate) {
        this.agate = agate;
    }

    public int getVipRechargeReward() {
        return vipRechargeReward;
    }

    public void setVipRechargeReward(int vipRechargeReward) {
        this.vipRechargeReward = vipRechargeReward;
    }

    public int getVipLv() {
        return vipLv;
    }

    public void setVipLv(int vipLv) {
        this.vipLv = vipLv;
    }

    public VipPearl getVipPearl() {
        return vipPearl;
    }

    public void setVipPearl(VipPearl vipPearl) {
        this.vipPearl = vipPearl;
    }

    public int getPurVipGift() {
        return purVipGift;
    }

    public void setPurVipGift(int purVipGift) {
        this.purVipGift = purVipGift;
    }

    public boolean isGetVipFreeAward() {
        return isGetVipFreeAward;
    }

    public void setGetVipFreeAward(boolean getVipFreeAward) {
        isGetVipFreeAward = getVipFreeAward;
    }

    public ConcurrentHashMap<Integer, MentalSkill> getMentalskills() {
        return mentalskills;
    }

    public void setMentalskills(ConcurrentHashMap<Integer, MentalSkill> mentalskills) {
        this.mentalskills = mentalskills;
    }

    public SkillData getNewSkillData() {
        return newSkillData;
    }

    public void setNewSkillData(SkillData newSkillData) {
        this.newSkillData = newSkillData;
    }

    public List<AuctionRecord> getAuctionRecords() {
        return auctionRecords;
    }

    public void setAuctionRecords(List<AuctionRecord> auctionRecords) {
        this.auctionRecords = auctionRecords;
    }

    public ConcurrentHashMap<Integer, BossData> getBossDataMap() {
        return BossDataMap;
    }

    public void setBossDataMap(ConcurrentHashMap<Integer, BossData> bossDataMap) {
        BossDataMap = bossDataMap;
    }

    public ConcurrentHashMap<Integer, Boss> getPersonWorldBoss() {
        return personWorldBoss;
    }

    public void setPersonWorldBoss(ConcurrentHashMap<Integer, Boss> personWorldBoss) {
        this.personWorldBoss = personWorldBoss;
    }

    public int getStateBossCurrLayer() {
        return stateBossCurrLayer;
    }

    public void setStateBossCurrLayer(int stateBossCurrLayer) {
        this.stateBossCurrLayer = stateBossCurrLayer;
    }

    public Set<Integer> getFirstLayers() {
        return firstLayers;
    }

    public void setFirstLayers(Set<Integer> firstLayers) {
        this.firstLayers = firstLayers;
    }

    public HashMap<Integer, Integer> getCloneSetting() {
        return cloneSetting;
    }

    public void setCloneSetting(HashMap<Integer, Integer> cloneSetting) {
        this.cloneSetting = cloneSetting;
    }

    public OpenServerGrowUp getOpenServerGrowUp() {
        return openServerGrowUp;
    }

    public OpenSeverSpec getOpenServerSpec() {
        return openServerSpec;
    }

    public void setOpenServerSpec(OpenSeverSpec openServerSpec) {
        this.openServerSpec = openServerSpec;
    }

    public void setOpenServerGrowUp(OpenServerGrowUp openServerGrowUp) {
        this.openServerGrowUp = openServerGrowUp;
    }

    public HashMap<Integer, OpenServerRevel> getOpenServerRevelMap() {
        return openServerRevelMap;
    }

    public void setOpenServerRevelMap(HashMap<Integer, OpenServerRevel> openServerRevelMap) {
        this.openServerRevelMap = openServerRevelMap;
    }

    public boolean isOsDailyRewardGet() {
        return osDailyRewardGet;
    }

    public void setOsDailyRewardGet(boolean osDailyRewardGet) {
        this.osDailyRewardGet = osDailyRewardGet;
    }

    public long getLastOpsFuncRewardTime() {
        return lastOpsFuncRewardTime;
    }

    public void setLastOpsFuncRewardTime(long lastOpsFuncRewardTime) {
        this.lastOpsFuncRewardTime = lastOpsFuncRewardTime;
    }

    public ConcurrentHashMap<Integer, Integer> getFirstKillData() {
        return firstKillData;
    }

    public void setFirstKillData(ConcurrentHashMap<Integer, Integer> firstKillData) {
        this.firstKillData = firstKillData;
    }

    public ArrayList<Integer> getFirstKillRedPacket() {
        return firstKillRedPacket;
    }

    public void setFirstKillRedPacket(ArrayList<Integer> firstKillRedPacket) {
        this.firstKillRedPacket = firstKillRedPacket;
    }

    public ConcurrentHashMap<Integer, NewServerActInfo> getActInfo() {
        return actInfo;
    }

    public void setActInfo(ConcurrentHashMap<Integer, NewServerActInfo> actInfo) {
        this.actInfo = actInfo;
    }

    public Set<Integer> getFuncRewardState() {
        return funcRewardState;
    }

    public void setFuncRewardState(Set<Integer> funcRewardState) {
        this.funcRewardState = funcRewardState;
    }

    public ActivePet getActivePet() {
        return activePet;
    }

    public void setActivePet(ActivePet activePet) {
        this.activePet = activePet;
    }

    public HashMap<Integer, BossRelationDropLimit> getRelationDropLimits() {
        return relationDropLimits;
    }

    public void setRelationDropLimits(HashMap<Integer, BossRelationDropLimit> relationDropLimits) {
        this.relationDropLimits = relationDropLimits;
    }

    public HashMap<Integer, Boss> getUnLimitBoss() {
        return unLimitBoss;
    }

    public void setUnLimitBoss(HashMap<Integer, Boss> unLimitBoss) {
        this.unLimitBoss = unLimitBoss;
    }

    public HashSet<Integer> getNoodDrop() {
        return noodDrop;
    }

    public void setNoodDrop(HashSet<Integer> noodDrop) {
        this.noodDrop = noodDrop;
    }

    public ChumData getChumData() {
        return chumData;
    }

    public void setChumData(ChumData chumData) {
        this.chumData = chumData;
    }

    public SingleTowerData getSingleTowerData() {
        return singleTowerData;
    }

    public void setSingleTowerData(SingleTowerData singleTowerData) {
        this.singleTowerData = singleTowerData;
    }

    public ConcurrentHashMap<Integer, ConquerTask> getCurConquerTasks() {
        return curConquerTasks;
    }

    public void setCurConquerTasks(ConcurrentHashMap<Integer, ConquerTask> curConquerTasks) {
        this.curConquerTasks = curConquerTasks;
    }

    public GuildTaskPool getGuildTaskPool() {
        return guildTaskPool;
    }

    public void setGuildTaskPool(GuildTaskPool guildTaskPool) {
        this.guildTaskPool = guildTaskPool;
    }

    public HashMap<Integer, Integer> getConquerTaskCount() {
        return conquerTaskCount;
    }

    public void setConquerTaskCount(HashMap<Integer, Integer> conquerTaskCount) {
        this.conquerTaskCount = conquerTaskCount;
    }

    public HashMap<Integer, Long> getConquerTaskTime() {
        return conquerTaskTime;
    }

    public void setConquerTaskTime(HashMap<Integer, Long> conquerTaskTime) {
        this.conquerTaskTime = conquerTaskTime;
    }

    public long getLastDropExp() {
        return lastDropExp;
    }

    public void setLastDropExp(long lastDropExp) {
        this.lastDropExp = lastDropExp;
    }

    public long getLastDropTime() {
        return lastDropTime;
    }

    public void setLastDropTime(long lastDropTime) {
        this.lastDropTime = lastDropTime;
    }

    public int getMainGuide() {
        return mainGuide;
    }

    public void setMainGuide(int mainGuide) {
        this.mainGuide = mainGuide;
    }

    public String getMaCode() {
        return maCode;
    }

    public void setMaCode(String machCode) {
        this.maCode = machCode;
    }

    public String getOs() {
        return os;
    }

    public PlayerSateStifleData getStifleData() {
        return stifleData;
    }

    public void setStifleData(PlayerSateStifleData stifleData) {
        this.stifleData = stifleData;
    }

    public void setOs(String os) {
        this.os = os;
    }

    //上一次离开经验副本的时间
    public int getClearLevel() {
        return clearLevel;
    }

    public void setClearLevel(int clearLevel) {
        this.clearLevel = clearLevel;
    }

    public long getUniverseLastTime() {
        return universeLastTime;
    }

    public void setUniverseLastTime(long universeLastTime) {
        this.universeLastTime = universeLastTime;
    }

    @Override
    public int getType() {
        return Fighter.PLAYER_TYPE;
    }

    public List<Integer> getFollowedBossList() {
        return followedBossList;
    }

    public void setFollowedBossList(List<Integer> followedBossList) {
        this.followedBossList = followedBossList;
    }

    public List<Integer> getAutoFollowedBossList() {
        return autoFollowedBossList;
    }

    public void setAutoFollowedBossList(List<Integer> autoFollowedBossList) {
        this.autoFollowedBossList = autoFollowedBossList;
    }

    public List<Integer> getFuDiFollowedBossList() {
        return fuDiFollowedBossList;
    }

    public void setFuDiFollowedBossList(List<Integer> fuDiFollowedBossList) {
        this.fuDiFollowedBossList = fuDiFollowedBossList;
    }

    public int getExpSyncTime() {
        return expSyncTime;
    }

    public void setExpSyncTime(int expSyncTime) {
        this.expSyncTime = expSyncTime;
    }

    public int getRollLevel() {
        return rollLevel;
    }

    public void setRollLevel(int rollLevel) {
        this.rollLevel = rollLevel;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float high) {
        this.height = high;
    }

    public boolean isShowEquipStar() {
        return isShowEquipStar;
    }

    @Override
    public long getBrithProtect() {
        return brithProtect;
    }

    public void setBrithProtect(long brithProtect) {
        this.brithProtect = brithProtect;
    }

    public List<Long> getUnfilters() {
        return unfilters;
    }


    public List<Integer> getLoginDays() {
        return loginDays;
    }

    public void setLoginDays(List<Integer> loginDays) {
        this.loginDays = loginDays;
    }

    public int getForbid() {
        return forbid;
    }

    public void setForbid(int forbid) {
        this.forbid = forbid;
    }

    public boolean isGM() {
        return GM;
    }

    public void setGM(boolean GM) {
        this.GM = GM;
    }


    public int getCurServerId() {
        return curServerId;
    }

    public void setCurServerId(int curServerId) {
        this.curServerId = curServerId;
    }

    public boolean isTestPos() {
        return testPos;
    }

    public void setTestPos(boolean testPos) {
        this.testPos = testPos;
    }

    public boolean isIsFirstGetTask() {
        return isFirstGetTask;
    }

    public void setIsFirstGetTask(boolean isFirstGetTask) {
        this.isFirstGetTask = isFirstGetTask;
    }

    public ConcurrentHashMap<Long, Long> getEnemys() {
        return enemys;
    }

    public void setEnemys(ConcurrentHashMap<Long, Long> enemys) {
        this.enemys = enemys;
    }

    public ConcurrentHashMap<Long, Long> getBeEnemys() {
        return beEnemys;
    }

    public float gainExpRate() {
        return expRate + getAttribute().getAdditionValue(AttributeType.ATTR_MonserExp) / 10000.0f;
    }

    public float expRateNotHaveAtt() {
        return expRate;
    }

    public void changeExpRate(float expRate) {
        this.expRate = expRate;
    }

    public float getMoneyRate() {
        return moneyRate;
    }

    public void setMoneyRate(float moneyRate) {
        this.moneyRate = moneyRate;
    }

    public int getPkState() {
        return pkState;
    }

    public void setPkState(int pkState) {
        this.pkState = pkState;
    }

    public BigInteger getTotalExp() {
        return totalExp;
    }

    public void setTotalExp(BigInteger totalExp) {
        this.totalExp = totalExp;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public int getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(int evaluate) {
        this.evaluate = evaluate;
    }

    public int getCreateServerId() {
        return createServerID;
    }

    public ChannelHandlerContext getIosession() {
        return iosession;
    }

    public void setIosession(ChannelHandlerContext iosession) {
        this.iosession = iosession;
    }

    public ConcurrentHashMap<Long, Immortalsoul> getImmortalsoul() {
        return bagImmortalsoul;
    }

    public void setImmortalsoul(ConcurrentHashMap<Long, Immortalsoul> immortalsoul) {
        this.bagImmortalsoul = immortalsoul;
    }

    public ConcurrentHashMap<Integer, Immortalsoul> getEquipSouls() {
        return equipSouls;
    }

    public void setEquipSouls(ConcurrentHashMap<Integer, Immortalsoul> equipSouls) {
        this.equipSouls = equipSouls;
    }

    public void setLuckyDrawWeekTimes(int luckyDrawWeekTimes) {
        this.luckyDrawWeekTimes = luckyDrawWeekTimes;
    }

    public int getLuckyDrawWeekTimes() {
        return luckyDrawWeekTimes;
    }

    public HashMap<Integer, List<Integer>> getLuckyDrawWeekCustomAwards() {
        return luckyDrawWeekCustomAwards;
    }

    public HashMap<Integer, Boolean> getLuckyDrawWeekAwardVolumes() {
        return luckyDrawWeekAwardVolumes;
    }




    //犒赏令数据
    public KaoShangLingData kaoShangLingData = new KaoShangLingData();
    public KaoShangLingData getKaoShangLingData() {
        return kaoShangLingData;
    }

    public void setKaoShangLingData(KaoShangLingData kaoShangLingData) {
        this.kaoShangLingData = kaoShangLingData;
    }
    // json专用
    @Override
    public void changeCurPos(Position pos) {
        //测试代码，捕捉异常
        if (pos == null) {
            logger.error(new Exception("pos is null"));
        }
        this.curGps.setPos(pos);
        samePositionPassenger(pos, false);

    }

    @Override
    public void changeCurPos(Position pos, boolean isBroadcast) {

        Position oldPos = this.gainCurPos();
        this.curGps.setPos(pos);
        boolean isChange = Manager.mapManager.changeArea(this, oldPos, pos);
        if (isChange) {
            Manager.mapManager.manager().OnChangePos(this, oldPos);
        }
        samePositionPassenger(pos, isBroadcast);

    }

    //添加状态
    private void samePositionPassenger(Position pos, boolean isBroadcast) {
        if (Manager.horseManager.getMultiPlayerHashMap().containsKey(this.getId())) {
            List<Long> passengers = Manager.horseManager.getMultiPlayerHashMap().get(this.getId());
            for (Long passenger : passengers) {
                Player tempPassenger = Manager.playerManager.getPlayerOnline(passenger);
                if (tempPassenger != null) {
                    tempPassenger.changeCurPos(pos, isBroadcast);
                }
            }
        }
    }

    @Override
    public Player clone() throws CloneNotSupportedException {
        return (Player) super.clone();
    }

    @Override
    protected void OnInBattleChange(boolean old) {
        MapUtils.synPlayerFightState(this);
    }

    @Override
    public int getState() {
        return state;
    }

    @Override
    public void OnAddState(int old) {
        if (!EntityState.Stand.compare(state) && !EntityState.LoginGame.compare(state) && !EntityState.ExitGame.compare(state)) {
        }
    }

    //重置状态到站立状态
    @Override
    protected void OnResetState(int old) {
        //LOGGER.info("重置状态=" + state + "__" + this);
    }

    @Override
    public String getName() {
        //return this.name;
        if (Manager.registerManager.getRoleName(getId()).equalsIgnoreCase("notfound")) { //注册角色时角色名表中尚没有玩家角色名
            return this.name;
        }
        return Manager.registerManager.getRoleName(getId()); //从角色名缓存表中取名字，这样角色改名时不用改很多地方
    }

    @Override
    public String getSrcName() {
        return getName();
    }

    @Override
    public void setName(String rolename) {
        this.name = rolename;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    public int getLanguageType() {
        return languageType;
    }

    public void setLanguageType(int languageType) {
        this.languageType = languageType;
    }

    public byte getCareer() {
        return career;
    }

    public void setCareer(byte career) {
        this.career = career;
    }

    public int getXsLevel() {
        return xsLevel;
    }

    public void setXsLevel(int xsLevel) {
        this.xsLevel = xsLevel;
    }

    public int getXsGrade() {
        return xsGrade;
    }

    public void setXsGrade(int xsGrade) {
        this.xsGrade = xsGrade;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getLastLoginLevel() {
        return lastLoginLevel;
    }

    public void setLastLoginLevel(int lastLoginLevel) {
        this.lastLoginLevel = lastLoginLevel;
    }

    public int getLastVipLevel() {
        return lastVipLevel;
    }

    public void setLastVipLevel(int lastVipLevel) {
        this.lastVipLevel = lastVipLevel;
    }

    public int getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(int deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Position getMoveDir() {
        return moveDir;
    }

    public void setMoveDir(Position moveDir) {
        this.moveDir = moveDir;
    }

    public MapGps getOld() {
        return old;
    }

    public void setOld(MapGps old) {
        this.old = old;
    }

    public void setCreateServerID(int serverID) {
        this.createServerID = serverID;
    }

    public byte getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(byte isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isOnline() {
        return isOnline == 1;
    }

    public long getOffLineTime() {
        return offLineTime;
    }

    public void setOffLineTime(long offLineTime) {
        this.offLineTime = offLineTime;
    }

    public int getAccumOnlineDays() {
        return accumOnlineDays;
    }

    public void setAccumOnlineDays(int accumOnlineDays) {
        this.accumOnlineDays = accumOnlineDays;
    }

    public void dealOnLine() {
        setIsOnline((byte) 1);
        long now = TimeUtils.Time();
        if (!TimeUtils.isSameDay(now, lastLoginTime)) {
            this.setAccumOnlineDays(this.getAccumOnlineDays() + 1);
            Manager.controlManager.operate(this, FunctionVariable.CumulativeLogin, 1);
        }
        setLastLoginTime(now);
        setLastLoginLevel(level);
        setLastVipLevel(vipLv);
    }

    public void dealOffLine() {
        //如果是战斗服， 则计算一次BUFF的消失
        if (GameServer.getInstance().IsFightServer()) {
            Manager.buffManager.deal().onDie(this);//清理战斗BUFF及死亡时不生效的BUFF
        }

        setIsOnline((byte) 0);
        setOffLineTime(TimeUtils.Time());
        setLastRefreshSignTime(0);

    }

    public long getSaveToDBTime() {
        return saveToDBTime;
    }

    public void setSaveToDBTime(long time) {
        saveToDBTime = time;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public boolean isSaveToDB() {
        return saveToDB;
    }

    public void setSaveToDB(boolean isSaveToDB) {
        saveToDB = isSaveToDB;
    }

    public List<MainTask> getCurMainTasks() {
        return curMainTasks;
    }

    public void setCurMainTasks(List<MainTask> curMainTasks) {
        this.curMainTasks = curMainTasks;
    }

    public List<Integer> getOverMainTaskIDs() {
        return overMainTaskIDs;
    }

    public void setOverMainTaskIDs(List<Integer> overMainTaskIDs) {
        this.overMainTaskIDs = overMainTaskIDs;
    }

    public void addOverMainTask(int taskid) {
        if (overMainTaskIDs.contains(taskid)) {
            return;
        }
        overMainTaskIDs.add(taskid);
    }

    //是否完成了某个主线任务
    public boolean overMainTask(int maintaskId) {
        return getOverMainTaskIDs().contains(maintaskId);
    }

    public int getCurMainTaskId() {
        return curMainTaskId;
    }

    public void setCurMainTaskId(int curMainTaskId) {
        this.curMainTaskId = curMainTaskId;
    }

    public HashSet<Integer> getShowSet() {
        return showSet;
    }

    public void setShowSet(HashSet<Integer> showSet) {
        this.showSet = showSet;
    }

    public HashSet<Integer> getHideSet() {
        return hideSet;
    }

    public void setHideSet(HashSet<Integer> hideSet) {
        this.hideSet = hideSet;
    }

    @Override
    public boolean canSee(IMapObject pp) {
        return true;

    }

    /**
     * 获取任务隐藏id集合
     *
     * @return
     */
    @Override
    public HashMap<Integer, List<Integer>> gainHideTaskIds() {
        return null;
    }

    public ConcurrentHashMap<Integer, Item> getBackpackItems() {
        return backpackItems;
    }

    public void setBackpackItems(ConcurrentHashMap<Integer, Item> backpackItems) {
        this.backpackItems = backpackItems;
    }

    public int getBagCellsNum() {
        return bagCellsNum;
    }

    public void setBagCellsNum(int bagCellsNum) {
        this.bagCellsNum = bagCellsNum;
    }

    public int getStoreCellsNum() {
        return storeCellsNum;
    }

    public void setStoreCellsNum(int storeCellsNum) {
        this.storeCellsNum = storeCellsNum;
    }

    @Override
    public ConcurrentHashMap<String, Count> getCounts() {
        return counts;
    }

    public void setCounts(ConcurrentHashMap<String, Count> counts) {
        this.counts = counts;
    }

    public ConcurrentHashMap<Integer, DailyTask> getCurDailyTasks() {
        return curDailyTasks;
    }

    public void setCurDailyTasks(ConcurrentHashMap<Integer, DailyTask> curDailyTasks) {
        this.curDailyTasks = curDailyTasks;
    }

    public HashMap<Integer, Integer> getDailyTaskCount() {
        return dailyTaskCount;
    }

    public void setDailyTaskCount(HashMap<Integer, Integer> dailyTaskCount) {
        this.dailyTaskCount = dailyTaskCount;
    }

    public HashMap<Integer, Long> getDailyTaskTime() {
        return dailyTaskTime;
    }

    public void setDailyTaskTime(HashMap<Integer, Long> dailyTaskTime) {
        this.dailyTaskTime = dailyTaskTime;
    }

    public HashMap<Integer, List<Integer>> getDailyTaskFinishIds() {
        return dailyTaskFinishIds;
    }

    public void setDailyTaskFinishIds(HashMap<Integer, List<Integer>> dailyTaskFinishIds) {
        this.dailyTaskFinishIds = dailyTaskFinishIds;
    }

    @Override
    public ConcurrentHashMap<String, Cooldown> getCooldowns() {
        return cooldowns;
    }

    public void setCooldowns(ConcurrentHashMap<String, Cooldown> cooldowns) {
        this.cooldowns = cooldowns;
    }

    public Gold getGold() {
        return gold;
    }

    public void setGold(Gold gold) {
        this.gold = gold;
    }

    public int getMoonCardDays() {
        return moonCardDays;
    }

    public void setMoonCardDays(int moonCardDays) {
        this.moonCardDays = moonCardDays;
    }
    /*
     获得月卡的天数
     */

    //是否是月卡用户
    public boolean TheMoonCard() {
        return MoonCardDays() != 0;
    }

    /**
     * 是否是月卡或者终身卡用户
     *
     * @return 0不什么也不是
     */
    public int moonandOverCard() {
        int moon = 0;
        if (TheMoonCard()) {
            moon = 1;
        }

        if (!lifeCard) {
            return moon;
        }
        return moon == 0 ? 2 : 3;
    }

    public int MoonCardDays() {
        if (this.moonCardDays < 1) {
            return 0;
        }
        int day = TimeUtils.getCurDay(0);
        int sheng = this.moonCardDays - day;
        return sheng < 0 ? 0 : sheng;
    }

    public boolean isLifeCard() {
        return lifeCard;
    }

    public void setLifeCard(boolean lifeCard) {
        this.lifeCard = lifeCard;
    }

    public ConcurrentHashMap<Integer, Item> getStoreItems() {
        return storeItems;
    }

    public void setStoreItems(ConcurrentHashMap<Integer, Item> storeItems) {
        this.storeItems = storeItems;
    }

    public ConcurrentHashMap<Integer, SkillCellAtt> getSkillCells() {
        return skillCells;
    }

    public void setSkillCells(ConcurrentHashMap<Integer, SkillCellAtt> skillCells) {
        this.skillCells = skillCells;
    }

    @Override
    public ConcurrentHashMap<Integer, Skill> getSkills() {
        return skills;
    }

    public List<EquipPart> getEquipParts() {
        return equipParts;
    }

    public void setEquipParts(List<EquipPart> equipParts) {
        this.equipParts = equipParts;
    }

    public int getAccunonlinetime() {
        return accunonlinetime;
    }

    public void setAccunonlinetime(int accunonlinetime) {
        this.accunonlinetime = accunonlinetime;
    }

    public int getBagCellTimeCount() {
        return bagCellTimeCount;
    }

    public void setBagCellTimeCount(int bagCellTimeCount) {
        this.bagCellTimeCount = bagCellTimeCount;
    }

    public Horse getHorse() {
        return horse;
    }

    public void setHorse(Horse horse) {
        this.horse = horse;
    }

    public Nature getWing() {
        return wing;
    }

    public void setWing(Nature wing) {
        this.wing = wing;
    }

    public Nature getMagic() {
        return magic;
    }

    public void setMagic(Nature magic) {
        this.magic = magic;
    }

    public Nature getTalisman() {
        return talisman;
    }

    public void setTalisman(Nature talisman) {
        this.talisman = talisman;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }


    public long getLastRefreshSignTime() {
        return lastRefreshSignTime;
    }

    public void setLastRefreshSignTime(long lastRefreshTime) {
        this.lastRefreshSignTime = lastRefreshTime;
    }

    public LoginGift getLoginGift() {
        return loginGift;
    }

    public void setLoginGift(LoginGift loginGift) {
        this.loginGift = loginGift;
    }

    public DayCheckIn getDayCheckIn() {
        return dayCheckIn;
    }

    public void setDayCheckIn(DayCheckIn dayCheckIn) {
        this.dayCheckIn = dayCheckIn;
    }

    public List<ExclusiveCard> getExclusiveCards() {
        return exclusiveCards;
    }

    public void setExclusiveCards(List<ExclusiveCard> exclusiveCards) {
        this.exclusiveCards = exclusiveCards;
    }

    public GrowthFund getGrowthFund() {
        return growthFund;
    }

    public void setGrowthFund(GrowthFund growthFund) {
        this.growthFund = growthFund;
    }

    public FCCharge getFcCharge() {
        return fcCharge;
    }

    public void setFcCharge(FCCharge fcCharge) {
        this.fcCharge = fcCharge;
    }

    public List<Integer> getLevelGifts() {
        return levelGifts;
    }

    public void setLevelGifts(List<Integer> levelGifts) {
        this.levelGifts = levelGifts;
    }

    public RetrieveResData getCurRRD() {
        return curRRD;
    }

    public void setCurRRD(RetrieveResData curRRD) {
        this.curRRD = curRRD;
    }

    public RetrieveResData getLastRRD() {
        return lastRRD;
    }

    public void setLastRRD(RetrieveResData lastRRD) {
        this.lastRRD = lastRRD;
    }

    public LimitShop getLimitShop() {
        return limitShop;
    }

    public void setLimitShop(LimitShop limitShop) {
        this.limitShop = limitShop;
    }

    public LimitShop getSteryShop() {
        return steryShop;
    }

    public void setSteryShop(LimitShop steryShop) {
        this.steryShop = steryShop;
    }

    @Override
    public void doDie(Fighter attacker) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerBattleBaseScript);
        if (is instanceof IPlayerBattle) {
            ((IPlayerBattle) is).doDie(this, attacker);
        } else {
            logger.error("没有找到玩家死亡的脚本实例！");
        }
        //LOGGER.error(this.getName() + "： 嗯哼，打不死我");
    }

    public long getReconnectTime() {
        return reconnectTime;
    }

    public void setReconnectTime(long reconnectTime) {
        this.reconnectTime = reconnectTime;
    }

    public BaseIntAttribute getMedicinesAttribute() {
        return medicinesAttribute;
    }

    public void setMedicinesAttribute(BaseIntAttribute medicinesAttribute) {
        this.medicinesAttribute = medicinesAttribute;
    }

    public BaseSystemIntAttribute getMedicinesAttributeSys() {
        return medicinesAttributeSys;
    }

    public void setMedicinesAttributeSys(BaseSystemIntAttribute medicinesAttributeSys) {
        this.medicinesAttributeSys = medicinesAttributeSys;
    }

    /**
     * 获取玩家识海属性
     */
    public ShiHaiData getShiHaiData() {
        return shiHaiData;
    }

    public void setShiHaiData(ShiHaiData shiHaiData) {
        this.shiHaiData = shiHaiData;
    }

    public ConcurrentHashMap<PlayerAttributeType, BaseIntAttribute> PlayerCalculators() {
        return playerCalculators;
    }

    public ConcurrentHashMap<PlayerAttributeType, BaseSystemIntAttribute> PlayerCalSystemCulators() {
        return playerCalSystemCulators;
    }

    public String getPlatUserId() {
        return platUserId;
    }

    public void setPlatUserId(String platUserId) {
        this.platUserId = platUserId;
    }

    public long getQuitGuildTime() {
        return quitGuildTime;
    }

    public void setQuitGuildTime(long quitGuildTime) {
        this.quitGuildTime = quitGuildTime;
    }

    @Override
    public int compareTo(Player o) {
        return o.getLevel() - getLevel();
    }

    @Override
    public void beAttack(Fighter attacker, Cfg_Skill_Bean skill, int skilllevel, long damage) {

        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerBattleBaseScript);
        if (is instanceof IPlayerBattle) {
            ((IPlayerBattle) is).beAttack(this, attacker, damage);
        } else {
            logger.error("没有找到玩家死亡的脚本实例！");
        }

    }

    public List<String> getUsedActiveCodeList() {
        return usedActiveCodeList;
    }

    public void serUsedActiveCodeList(List<String> usedActiveCodeList) {
        this.usedActiveCodeList = usedActiveCodeList;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public HashMap<Integer, Integer> getLastDailyTaskId() {
        return lastDailyTaskId;
    }

    public void setLastDailyTaskId(HashMap<Integer, Integer> lastDailyTaskId) {
        this.lastDailyTaskId = lastDailyTaskId;
    }

    public HashMap<Integer, Integer> getLastConquerTaskId() {
        return lastConquerTaskId;
    }

    public void setLastConquerTaskId(HashMap<Integer, Integer> lastConquerTaskId) {
        this.lastConquerTaskId = lastConquerTaskId;
    }

    @Override
    public String toString() {
        return " player: " + this.getId()
                + " f:" + this.getFightPoint()
                + " n: " + this.getName()
                + " p:" + this.gainCurPos()
                + " mMId: " + this.gainMapModelId()
                + " i: " + this.gainLine()
                + " e: " + this.getCurrencys().get(ItemCoinType.EXP);
    }

    @Override
    public void onHpChange(Fighter attacker) {
        //防止血量超过上限
        if (getCurHp() > attribute.MaxHP()) {
            setCurHp(attribute.MaxHP());
        }

        if (!isOnline()) {
            return;
        }

        MapUtils.sendHpChange(this);
    }


    public ConcurrentHashMap<Long, MailData> getMailCacheMap() {
        return mailCacheMap;
    }

    public void setMailCacheMap(ConcurrentHashMap<Long, MailData> mailCacheMap) {
        this.mailCacheMap = mailCacheMap;
    }

    public boolean isCommentFlag() {
        return commentFlag;
    }

    public void setCommentFlag(boolean commentFlag) {
        this.commentFlag = commentFlag;
    }

    public PlayerNewFashion getNewFashionData() {
        return newFashionData;
    }

    public void setNewFashionData(PlayerNewFashion newFashionData) {
        this.newFashionData = newFashionData;
    }


    @Override
    public long getFightPoint() {
        return fightPoint;
    }

    public void setFightPoint(long fightPoint) {
        this.oldFightPoint = this.fightPoint;
        this.fightPoint = fightPoint;
        if(this.bi != null){
            bi.setRole_combat(fightPoint);
        }
    }

    public BI getBi() {
        return bi;
    }

    public void setBi(BI bi) {
        this.bi = bi;
    }

    /**
     * 获取客户端设置的变量
     *
     * @param settingType
     * @return
     */
    public boolean getSetting(int settingType) {
        return getSettings().getOrDefault(settingType, false);
    }

    public ConcurrentHashMap<Integer, Boolean> getSettings() {
        return settings;
    }

    public void setSettings(ConcurrentHashMap<Integer, Boolean> settings) {
        this.settings = settings;
    }

    public int getMarriagedType() {
        return marriagedType;
    }

    public void setMarriagedType(int marriagedType) {
        this.marriagedType = marriagedType;
    }

    public long getMarriageProposeId() {
        return marriageProposeId;
    }

    public void setMarriageProposeId(long marriageProposeId) {
        this.marriageProposeId = marriageProposeId;
    }

    public int getWorshipDay() {
        return worshipDay;
    }

    public void setWorshipDay(int worshipDay) {
        this.worshipDay = worshipDay;
    }

    public HashSet<Long> getWorshipRoleIdSet() {
        return worshipRoleIdSet;
    }

    public void setWorshipRoleIdSet(HashSet<Long> worshipRoleIdSet) {
        this.worshipRoleIdSet = worshipRoleIdSet;
    }

    /**
     * funcell生成的uuid
     *
     * @return
     */
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Set<Integer> getBranchOverList() {
        return branchOverList;
    }

    public void setBranchOverList(Set<Integer> branchOverList) {
        this.branchOverList = branchOverList;
    }

    public List<BranchTask> getCurBranchTask() {
        return curBranchTask;
    }

    public void setCurBranchTask(List<BranchTask> curBranchTask) {
        this.curBranchTask = curBranchTask;
    }

    public int getTaskTargetStage() {
        return taskTargetStage;
    }

    public void setTaskTargetStage(int taskTargetStage) {
        this.taskTargetStage = taskTargetStage;
    }

    public GenderTask getCurGenderTask() {
        return curGenderTask;
    }

    public void setCurGenderTask(GenderTask curGenderTask) {
        this.curGenderTask = curGenderTask;
    }

    public AutoIncrementLongArray getCurrencys() {
        return currencys;
    }

    public void setCurrencys(AutoIncrementLongArray currencys) {
        this.currencys = currencys;
    }

    @Override
    public void reset() {
        this.clearHatred();
        this.setCurHp(getAttribute().MaxHP());
        this.resetState();
        this.setBrithProtect(TimeUtils.Time() + 8000);
        long roleWakan = this.getAttribute().getAdditionValue(AttributeType.ATTR_Wakan);
        this.setCurWakan(roleWakan);
    }

    @Override
    public int gainFinalMoveSpeed() {
        return getAttribute().gainFinalMoveSpeed();
    }

    public List<Integer> getOverGenderTaskIds() {
        return overGenderTaskIds;
    }

    public void setOverGenderTaskIds(List<Integer> overGenderTaskIds) {
        this.overGenderTaskIds = overGenderTaskIds;
    }

    public DailyActiveData getDailyActiveData() {
        return dailyActiveData;
    }

    public void setDailyActiveData(DailyActiveData dailyActiveData) {
        this.dailyActiveData = dailyActiveData;
    }

    public ConcurrentHashMap<Integer, AchievementInfo> getAchievementInfo() {
        return achievementInfo;
    }

    public void setAchievementInfo(ConcurrentHashMap<Integer, AchievementInfo> achievementInfo) {
        this.achievementInfo = achievementInfo;
    }

    public ConcurrentHashMap<Integer, Amulet> getGodBookInfo() {
        return godBookInfo;
    }

    public void setGodBookInfo(ConcurrentHashMap<Integer, Amulet> godBookInfo) {
        this.godBookInfo = godBookInfo;
    }

    public TitleData getTitleData() {
        return titleData;
    }

    public void setTitleData(TitleData titleData) {
        this.titleData = titleData;
    }

    public SpiritData getSpiritData() {
        return spiritData;
    }

    public void setSpiritData(SpiritData spiritData) {
        this.spiritData = spiritData;
    }

    public PlayerSoulBeastInfo getSoulBeastInfo() {
        return soulBeastInfo;
    }

    public void setSoulBeastInfo(PlayerSoulBeastInfo soulBeastInfo) {
        this.soulBeastInfo = soulBeastInfo;
    }

    public List<Integer> getResolveSettings() {
        return resolveSettings;
    }

    public void setResolveSettings(List<Integer> resolveSettings) {
        this.resolveSettings = resolveSettings;
    }

    public boolean isAutoRecycle() {
        return autoRecycle;
    }

    public void setAutoRecycle(boolean autoRecycle) {
        this.autoRecycle = autoRecycle;
    }

    public boolean isCertify() {
        return isCertify;
    }

    public void setCertify(boolean certify) {
        isCertify = certify;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public boolean isHaveGuild() {
        if (GameServer.getInstance().IsFightServer()) {
            return false;
        }
        if (this.guildId > 0) {
            if (Manager.guildsManager.getGuildById(getGuildId()) != null) {
                return true;
            } else {
                logger.error("玩家身上有公会Id，但不存在改ID的公会,玩家id：" + getId() + " 公会Id：" + this.guildId);
                this.guildId = 0;
            }
        }
        return false;
    }

    public String gainGuildName() {

        if (GameServer.getInstance().IsFightServer()) {
            return guildName;
        }
        if (isHaveGuild()) {
            Guild g = Manager.guildsManager.getGuildById(getGuildId());
            return g.getName();
        }
        return "";
    }

    public int gainGuildPost() {
        if (isHaveGuild()) {
            Guild g = Manager.guildsManager.getGuildById(getGuildId());
            GuildMember m = g.getMembers().get(id);
            if (m != null) {
                return m.getPosition();
            }
        }
        return 0;
    }

    @Override
    public long getFixDecHp() {
        return 0;
    }

    public String mapKey() {
        return GameServer.getInstance().getServerId() + "_" + gainMapId() + "_" + gainLine();
    }

    public AutoIncrementLongArray getHistoryCoin() {
        return historyCoin;
    }

    public void setHistoryCoin(AutoIncrementLongArray historyCoin) {
        this.historyCoin = historyCoin;
    }

    public PlayerRedPacket getRedPacket() {
        return redPacket;
    }

    public void setRedPacket(PlayerRedPacket redPacket) {
        this.redPacket = redPacket;
    }

    public long getFirstRechargeTime() {
        return firstRechargeTime;
    }

    public void setFirstRechargeTime(long firstRechargeTime) {
        this.firstRechargeTime = firstRechargeTime;
    }

    public long getLastRechargeTime() {
        return lastRechargeTime;
    }

    public void setLastRechargeTime(long lastRechargeTime) {
        this.lastRechargeTime = lastRechargeTime;
    }

    public void addRechargeTime(long time) {
        if (firstRechargeTime == 0) {
            firstRechargeTime = time;
            Manager.controlManager.operate(this, FunctionVariable.FirstRechargeReward, 1);
            Manager.redPacketManager.createRedpacket(this, RedPacketEnum.firstRecharge);
        }
        lastRechargeTime = time;
    }

    public ConcurrentHashMap<Long, Long> getPklist() {
        return pklist;
    }

    public int[] getMatchRobot() {
        return matchRobot;
    }

    public void setMatchRobot(int[] matchRobot) {
        this.matchRobot = matchRobot;
    }

    public ConcurrentHashMap<Long, Item> getPetEquipPackItems() {
        return petEquipPackItems;
    }

    public void setPetEquipPackItems(ConcurrentHashMap<Long, Item> petEquipPackItems) {
        this.petEquipPackItems = petEquipPackItems;
    }

    public ConcurrentHashMap<Long, Item> getHorseEquipPackItems() {
        return horseEquipPackItems;
    }

    public void setHorseEquipPackItems(ConcurrentHashMap<Long, Item> horseEquipPackItems) {
        this.horseEquipPackItems = horseEquipPackItems;
    }

    public String getShowSid() {
        return showSid;
    }

    public void setShowSid(String showSid) {
        this.showSid = showSid;
    }

    public long getJjcCdTime() {
        return jjcCdTime;
    }

    public void setJjcCdTime(long jjcCdTime) {
        this.jjcCdTime = jjcCdTime;
    }

    public long getJjcStartTime() {
        return jjcStartTime;
    }

    public void setJjcStartTime(long jjcStartTime) {
        this.jjcStartTime = jjcStartTime;
    }

    public long endJjcCdTime() {
        int beginday = TimeUtils.getCurDayByTime(getJjcStartTime());
        //日期不是相等的
        if (beginday != TimeUtils.getCurDay(0)) {
            setJjcStartTime(TimeUtils.Time());
            setJjcCdTime(0);
        }

        long endTime = jjcStartTime + jjcCdTime - TimeUtils.Time();
        if (endTime < 1) {
            return 0;
        }
        return endTime;
    }

    public int getJjcHistoryMaxRank() {
        return jjcHistoryMaxRank;
    }

    public void setJjcHistoryMaxRank(int jjcHistoryMaxRank) {
        this.jjcHistoryMaxRank = jjcHistoryMaxRank;
    }

    public int getJjcHistoryMaxIndex() {
        return jjcHistoryMaxIndex;
    }

    public void setJjcHistoryMaxIndex(int jjcHistoryMaxIndex) {
        this.jjcHistoryMaxIndex = jjcHistoryMaxIndex;
    }

    public List<Integer> getJjcFirstRewardList() {
        return jjcFirstRewardList;
    }

    public void setJjcFirstRewardList(List<Integer> jjcFirstRewardList) {
        this.jjcFirstRewardList = jjcFirstRewardList;
    }

    public Blood getBlood() {
        return blood;
    }

    public void setBlood(Blood blood) {
        this.blood = blood;
    }

    @Override
    public void setCurHp(long curHp) {
        int oldHpPec = checkHpPercent();
        super.setCurHp(curHp);

        //如果不在线， 则不需要走下面的逻辑
        if (!isOnline()) {
            return;
        }
        //组队的情况下通过玩家血量的变化
        int nowHpPer = checkHpPercent();
        if (teamId < 0) {
            return;
        }
        if (oldHpPec != nowHpPer) {
            Manager.teamManager.deal().updateHpAndMapKey(this);
        }

    }

    @Override
    public long getCurWakan() {
        return curWakan;
    }

    @Override
    public void setCurWakan(long curWakan) {
        if (curWakan <= 0) {
            curWakan = 0;
        }
        if (this.curWakan == curWakan) {
            return;
        }

        this.curWakan = curWakan;
        MapUtils.sendWakanChange(this);
    }

    //太乱了，我还是加到末尾吧

    public NineDaysPlayerData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(NineDaysPlayerData personalData) {
        this.personalData = personalData;
    }

    public int getUseIconState() {
        return useIconState;
    }

    public void setUseIconState(int useIconState) {
        this.useIconState = useIconState;
    }

    public ConcurrentHashMap<Integer, Integer> getShareMap() {
        return shareMap;
    }

    public void setShareMap(ConcurrentHashMap<Integer, Integer> shareMap) {
        this.shareMap = shareMap;
    }

    public HookInfo getHookInfo() {
        return hookInfo;
    }

    public void setHookInfo(HookInfo hookInfo) {
        this.hookInfo = hookInfo;
    }

    public StateVip getStateVip() {
        return stateVip;
    }

    public void setStateVip(StateVip stateVip) {
        this.stateVip = stateVip;
    }

    public HashMap<Integer, Boolean> getStateVips() {
        return stateVips;
    }

    public void setStateVips(HashMap<Integer, Boolean> stateVips) {
        this.stateVips = stateVips;
    }

    public TreasuryHuntData getTreasuryHuntData() {
        return treasuryHuntData;
    }

    public void setTreasuryHuntData(TreasuryHuntData treasuryHuntData) {
        this.treasuryHuntData = treasuryHuntData;
    }

    /**
     * 获取玩家信息
     *
     * @return
     */
    public String getInfo() {
        return "(角色名:" + this.getName() + " ID:" + this.getId() + ")";
    }

    public long getLastWorldHelpTime() {
        return lastWorldHelpTime;
    }

    public void setLastWorldHelpTime(long lastWorldHelpTime) {
        this.lastWorldHelpTime = lastWorldHelpTime;
    }

    public long getLastCallChumTime() {
        return lastCallChumTime;
    }

    public void setLastCallChumTime(long lastCallChumTime) {
        this.lastCallChumTime = lastCallChumTime;
    }

    public HolyEquipBaseInfo getHolyEquipBaseInfo() {
        return holyEquipBaseInfo;
    }

    public void setHolyEquipBaseInfo(HolyEquipBaseInfo holyEquipBaseInfo) {
        this.holyEquipBaseInfo = holyEquipBaseInfo;
    }

    @Override
    public void release() {

    }

    public ConcurrentHashMap<Integer, ImmortalEquipPart> getImmortalEquipPartLisit() {
        return immortalEquipPartLisit;
    }

    public void setImmortalEquipPartLisit(ConcurrentHashMap<Integer, ImmortalEquipPart> immortalEquipPartLisit) {
        this.immortalEquipPartLisit = immortalEquipPartLisit;
    }

    public ConcurrentHashMap<Integer, Integer> getImmortalEquipFacadeMap() {
        return immortalEquipFacadeMap;
    }

    public void setImmortalEquipFacadeMap(ConcurrentHashMap<Integer, Integer> immortalEquipFacadeMap) {
        this.immortalEquipFacadeMap = immortalEquipFacadeMap;
    }

    public ConcurrentHashMap<Long, Item> getImmEquipItemList() {
        return ImmEquipItemList;
    }

    public void setImmEquipItemList(ConcurrentHashMap<Long, Item> immEquipItemList) {
        ImmEquipItemList = immEquipItemList;
    }

    public TreasureHuntXianjiaData getTreasureHuntXianjiaData() {
        return treasureHuntXianjiaData;
    }

    public void setTreasureHuntXianjiaData(TreasureHuntXianjiaData treasureHuntXianjiaData) {
        this.treasureHuntXianjiaData = treasureHuntXianjiaData;
    }

    public TreasureHuntWuyouData getTreasureHuntWuyouData() {
        return treasureHuntWuyouData;
    }

    public void setTreasureHuntWuyouData(TreasureHuntWuyouData treasureHuntWuyouData) {
        this.treasureHuntWuyouData = treasureHuntWuyouData;
    }

    public long getNewbieRechargeTime() {
        return newbieRechargeTime;
    }

    public void setNewbieRechargeTime(long newbieRechargeTime) {
        this.newbieRechargeTime = newbieRechargeTime;
    }

    public FallingSkyData getFallingSkyData() {
        return fallingSkyData;
    }

    public void setFallingSkyData(FallingSkyData fallingSkyData) {
        this.fallingSkyData = fallingSkyData;
    }

    public Set<Integer> getActivityRankAwardGet() {
        return activityRankAwardGet;
    }

    public void setActivityRankAwardGet(Set<Integer> activityRankAwardGet) {
        this.activityRankAwardGet = activityRankAwardGet;
    }

    public void addActivityRankAwardGet(int rankAwardId) {
        this.activityRankAwardGet.add(rankAwardId);
    }

    public ConcurrentHashMap<Long, Item> getDevilPackItems() {
        return devilPackItems;
    }

    public void setDevilPackItems(ConcurrentHashMap<Long, Item> devilPackItems) {
        this.devilPackItems = devilPackItems;
    }

    public Devil getDevil() {
        return devil;
    }

    public void setDevil(Devil devil) {
        this.devil = devil;
    }

    public int getWingStatus() {
        return wingStatus;
    }

    public void setWingStatus(int wingStatus) {
        this.wingStatus = wingStatus;
    }

    public long getOldFightPoint() {
        return oldFightPoint;
    }

    public void setOldFightPoint(long oldFightPoint) {
        this.oldFightPoint = oldFightPoint;
    }

    public GrowthFund getInvestPeak() {
        return investPeak;
    }

    public void setInvestPeak(GrowthFund investPeak) {
        this.investPeak = investPeak;
    }

    public ConcurrentHashMap<Integer, Integer> getImmortalsoulCores() {
        return ImmortalsoulCores;
    }

    public void setImmortalsoulCores(ConcurrentHashMap<Integer, Integer> immortalsoulCores) {
        ImmortalsoulCores = immortalsoulCores;
    }

    public String getCustomHeadPath() {
        return customHeadPath;
    }

    public void setCustomHeadPath(String customHeadPath) {
        this.customHeadPath = customHeadPath;
    }

    public boolean isUseCustomHead() {
        return useCustomHead;
    }

    public void setUseCustomHead(boolean useCustomHead) {
        this.useCustomHead = useCustomHead;
    }

    public int getPlatformEvaluateEveryDayShare() {
        return platformEvaluateEveryDayShare;
    }

    public void setPlatformEvaluateEveryDayShare(int platformEvaluateEveryDayShare) {
        this.platformEvaluateEveryDayShare = platformEvaluateEveryDayShare;
    }

    public String getPlatformEvaluateEveryDayShareDay() {
        return platformEvaluateEveryDayShareDay;
    }

    public void setPlatformEvaluateEveryDayShareDay(String platformEvaluateEveryDayShareDay) {
        this.platformEvaluateEveryDayShareDay = platformEvaluateEveryDayShareDay;
    }

    public SpecialVipStateBean getSpecialVipStateBean() {
        return specialVipStateBean;
    }

    public void setSpecialVipStateBean(SpecialVipStateBean specialVipStateBean) {
        this.specialVipStateBean = specialVipStateBean;
    }

    public boolean isSendTaskInfo() {
        return isSendTaskInfo;
    }

    public void setSendTaskInfo(boolean sendTaskInfo) {
        isSendTaskInfo = sendTaskInfo;
    }

    public List<Integer> getLevelVipGifts() {
        return levelVipGifts;
    }

    public void setLevelVipGifts(List<Integer> levelVipGifts) {
        this.levelVipGifts = levelVipGifts;
    }

    public int getShopEvaluate() {
        return shopEvaluate;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public long getLastWelfareFreeGiftTime() {
        return lastWelfareFreeGiftTime;
    }

    public void setLastWelfareFreeGiftTime(long lastWelfareFreeGiftTime) {
        this.lastWelfareFreeGiftTime = lastWelfareFreeGiftTime;
    }

    public long getLastDiscFreeGiftTime() {
        return lastDiscFreeGiftTime;
    }

    public void setLastDiscFreeGiftTime(long lastDiscFreeGiftTime) {
        this.lastDiscFreeGiftTime = lastDiscFreeGiftTime;
    }

    public long getLastGoldDiscFreeGiftTime() {
        return lastGoldDiscFreeGiftTime;
    }

    public void setLastGoldDiscFreeGiftTime(long lastGoldDiscFreeGiftTime) {
        this.lastGoldDiscFreeGiftTime = lastGoldDiscFreeGiftTime;
    }

    public FunctionTaskData getFunctionTaskData() {
        return functionTaskData;
    }

    public void setFunctionTaskData(FunctionTaskData functionTaskData) {
        this.functionTaskData = functionTaskData;
    }

    public VIP4Rebate getVip4Rebate() {
        return vip4Rebate;
    }

    public void setVip4Rebate(VIP4Rebate vip4Rebate) {
        this.vip4Rebate = vip4Rebate;
    }

    /**
     * 玩家退出副本
     */
    public void exitCopyMap(){
        Manager.copyMapManager.manager().onReqCopyMapOut(this);
    }

    public V4HelpData getV4HelpData() {
        return v4HelpData;
    }

    public void setV4HelpData(V4HelpData v4HelpData) {
        this.v4HelpData = v4HelpData;
    }

    public RebateBoxData getRebateBoxData() {
        return rebateBoxData;
    }

    public void setRebateBoxData(RebateBoxData rebateBoxData) {
        this.rebateBoxData = rebateBoxData;
    }

    public Map<Integer, GuildBattleData> getGuildBattleData() {
        return guildBattleData;
    }

    public void setGuildBattleData(Map<Integer, GuildBattleData> guildBattleData) {
        this.guildBattleData = guildBattleData;
    }
}
