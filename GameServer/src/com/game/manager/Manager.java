package com.game.manager;

import com.game.RechargeRebate.manager.RechargeRebateManager;
import com.game.achievement.manager.AchievementManager;
import com.game.activity.manager.ActivityManager;
import com.game.activityRanklist.manager.ActivityRankManager;
import com.game.alienGem.manager.AlienGemManager;
import com.game.auction.manager.AuctionManager;
import com.game.backpack.manager.BackpackManager;
import com.game.backpack.manager.CurrencyManager;
import com.game.bi.manager.BIManager;
import com.game.boss.manager.BossManager;
import com.game.buff.manager.BuffManager;
import com.game.cangbaoge.manager.CangbaogeManager;
import com.game.chat.Manager.ChatManager;
import com.game.chat.Manager.LoopNotifyManager;
import com.game.chat.Manager.ShareManager;
import com.game.chum.manager.ChumManager;
import com.game.command.manager.CommandManager;
import com.game.commercialize.manager.CommercializeManager;
import com.game.community.manager.CommunityManager;
import com.game.control.manager.ControlManager;
import com.game.cooldown.manager.CooldownManager;
import com.game.copymap.manager.CopyMapManager;
import com.game.count.manager.CountManager;
import com.game.couplefight.manager.CouplefightManager;
import com.game.crazyweek.manager.CrazyWeekManager;
import com.game.crosshorseboss.manager.CrossHorseBossManager;
import com.game.crossrank.manager.CrossRankManager;
import com.game.crossserver.manager.CrossServerManager;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.devilseries.manager.DevilSeriesManager;
import com.game.drop.manager.DropManager;
import com.game.eightdiagrams.manger.EightDiagramsManager;
import com.game.equip.manager.EquipManager;
import com.game.equip.manager.GemManager;
import com.game.fallingsky.manager.FallingSkyManager;
import com.game.fight.manager.FightManager;
import com.game.friend.manager.FriendManager;
import com.game.functionTask.manager.FunctionTaskManager;
import com.game.gather.manager.GatherManager;
import com.game.gm.manager.GmCommandManager;
import com.game.godbook.manager.GodBookManager;
import com.game.gold.manager.GoldManager;
import com.game.guild.manager.GuildsManager;
import com.game.guildactivity.manager.GuildActivityManager;
import com.game.guildbattle.manager.GuildBattleManager;
import com.game.guildcrossfud.manager.CrossFudManager;
import com.game.heart.manager.HeartManager;
import com.game.holyEquip.manager.HolyEquipManager;
import com.game.hook.manager.PlayerHookManager;
import com.game.horse.manager.HorseManager;
import com.game.huaxinflysword.manager.HuaxinFlySwordManager;
import com.game.immortalequip.manager.ImmortalEquipManager;
import com.game.immortalsoul.manager.ImmortalSoulManager;
import com.game.jjc.manager.JJCManager;
import com.game.kaoshangling.manager.KaoShangLingManager;
import com.game.leaderpreach.manager.LeaderPreachManager;
import com.game.log.LogDataManager;
import com.game.luckydraw.manager.LuckyDrawManager;
import com.game.mail.manager.MailManager;
import com.game.map.manager.MapManager;
import com.game.map.manager.MapsConfigManager;
import com.game.marriage.manager.MarriageManager;
import com.game.memory.manager.HatredManager;
import com.game.monster.manager.MonsterManager;
import com.game.nature.manager.NatureManager;
import com.game.newfashion.manager.NewFashionManager;
import com.game.npc.manager.NpcManager;
import com.game.openserverac.manager.OpenServerAcManager;
import com.game.openserverac.manager.V4HelpManager;
import com.game.peak.manager.PeakManager;
import com.game.pet.manager.PetManager;
import com.game.platformevaluate.manager.PlatformEvaluateManager;
import com.game.player.manager.PlayerAttributeManager;
import com.game.player.manager.PlayerManager;
import com.game.publicserver.manager.PublicServerManager;
import com.game.questionnaire.manager.QuestionnaireManager;
import com.game.ranklist.manager.RankListManager;
import com.game.recharge.manager.RechargeManager;
import com.game.recycle.manager.RecycleManager;
import com.game.redpacket.manager.RedPacketManager;
import com.game.register.manager.RegisterManager;
import com.game.server.SaveThreadManager;
import com.game.setting.manager.SettingManager;
import com.game.shihai.manager.ShiHaiManager;
import com.game.shop.manager.ShopManager;
import com.game.skill.config.SkillEventContainer;
import com.game.skill.manager.SkillManager;
import com.game.soulArmor.manager.SoulArmorManager;
import com.game.soulanimalforest.manager.SoulAnimalForestCrossManager;
import com.game.soulbeast.manager.SoulBeastManager;
import com.game.statestifle.manager.StateStifleManager;
import com.game.statevip.mananger.StateVipManager;
import com.game.store.manager.StoreManager;
import com.game.task.manager.TaskManager;
import com.game.team.manager.TeamManager;
import com.game.title.manager.TitleManager;
import com.game.treasurehunt.manager.TreasureHuntManager;
import com.game.treasurehuntwuyou.manager.TreasureHuntWuyouManager;
import com.game.treasurehuntxianjia.manager.TreasureHuntXianjiaManager;
import com.game.universe.manager.UniverseManager;
import com.game.unrealEquip.manager.UnrealEquipManager;
import com.game.vip.manager.VipManager;
import com.game.welfare.manager.ActiveCodeManager;
import com.game.welfare.manager.RetrieveResManager;
import com.game.welfare.manager.WelfareManager;
import com.game.world_help.manager.WorldHelpManager;
import com.game.worldbonfire.manager.WorldBonfireManager;
import com.game.yed.YedMgr;
import game.core.script.ScriptManager;

public class Manager {
    /**
     * 公共服管理类
     */
    public static PublicServerManager publicServerManager;
    /**
     * 地图管理类
     */
    public static MapManager mapManager;
    /**
     * 地图配置管理类
     */
    public static MapsConfigManager mapCfgManager;
    /**
     * 怪物管理类
     */
    public static MonsterManager monsterManager;
    /**
     * 玩家管理类
     */
    public static PlayerManager playerManager;
    /**
     * 战斗管理类
     */
    public static FightManager fightManager;
    /**
     * 仇恨对象池
     */
    public static HatredManager hatredManager;
    /**
     * boss管理
     */
    public static BossManager bossManager;
    /**
     * 宠物系统
     */
    public static PetManager petManager;
    /**
     * 竞技场管理类
     */
    public static JJCManager jjcManager;
    /**
     * 注册管理类
     */
    public static RegisterManager registerManager;
    /**
     * 聊天管理类
     */
    public static ChatManager chatManager;
    /**
     * 组队管理类
     */
    public static TeamManager teamManager;
    /**
     * 属性管理类
     */
    public static PlayerAttributeManager playerAttAttributeManager;
    /**
     * 背包管理类
     */
    public static BackpackManager backpackManager;
    /**
     * 任务管理类
     */
    public static TaskManager taskManager;
    /**
     * 次数管理
     */
    public static CountManager countManager;
    /**
     * 冷却管理类
     */
    public static CooldownManager cooldownManager;
    /**
     * 装备管理类
     */
    public static EquipManager equipManager;
    /**
     * 魂甲系统
     */
    public static SoulArmorManager soulArmorManager;
    /**
     * 仓库管理类
     */
    public static StoreManager storeManager;
    /**
     * 掉落管理类
     */
    public static DropManager dropManager;
    /**
     * GM命令管理类
     */
    public static GmCommandManager gmCommandManager;
    /**
     * 脚本管理类
     */
    public static ScriptManager scriptManager;
    /**
     * 技能管理类
     */
    public static SkillManager skillManager;
    /**
     * 技能事件管理类
     */
    public static SkillEventContainer skilleventManager;
    /**
     * buff管理类
     */
    public static BuffManager buffManager;
    /**
     * Npc管理类
     */
    public static NpcManager npcManager;
    /**
     * 坐骑管理类
     */
    public static HorseManager horseManager;
    /**
     * 心跳管理类
     */
    public static HeartManager heartManager;
    /**
     * 好友管理类
     */
    public static FriendManager friendManager;
    /**
     * 商店管理接口类
     */
    public static ShopManager shopManager;
    /**
     * 副本管理接口类
     */
    public static CopyMapManager copyMapManager;
    /**
     * 邮件管理类
     */
    public static MailManager mailManager;
    /**
     * 成就管理类
     */
    public static AchievementManager achievementManager;
    /**
     * 排行榜管理类
     */
    public static RankListManager rankListManager;
    /**
     * 称号管理类
     */
    public static TitleManager titleManager;
    /**
     * 活动管理类
     */
    public static ActivityManager activityManager;
    /**
     * 充值管理类
     */
    public static RechargeManager rechargeManager;
    /**
     * 元宝管理类
     */
    public static GoldManager goldManager;
    /**
     * 巅峰竞技
     */
    public static PeakManager peakManager;
    /**
     * 功能开关管理类
     */
    public static ControlManager controlManager;
    /**
     * 跨服临时数据处理类
     */
    public static CrossServerManager crossServerManager;
    /**
     * 保存实例
     */
    public static SaveThreadManager saveThreadManager;
    /**
     * 平台评价
     */
    public static PlatformEvaluateManager platformevaluateManager;
    /**
     * 货币接口管理 类
     */
    public static CurrencyManager currencyManager;
    /**
     * 每日必做
     */
    public static DailyActiveManager dailyActiveManager;
    /**
     * 公会
     */
    public static GuildsManager guildsManager;
    /**
     * 商业化模块
     */
    public static CommercializeManager commercializeManager;
    /**
     * 挚友
     */
    public static ChumManager chumManager;
    /**
     * 掌门传道
     */
    public static LeaderPreachManager leaderPreachManager;
    /**
     * 福利模块
     */
    public static WelfareManager welfareManager;
    /**
     * 资源找回
     */
    public static RetrieveResManager retrieveResManager;
    /**
     * 激活码
     */
    public static ActiveCodeManager activeCodeManager;
    /**
     * 红包
     */
    public static RedPacketManager redPacketManager;
    /**
     * AI系统
     */
    public static YedMgr yedAiLoader;
    /**
     * 离线挂机/打坐管理器
     */
    public static PlayerHookManager playerHookManager;
    /**
     * 循环公告管理类
     */
    public static LoopNotifyManager loopNotifyManager;
    /**
     * 分享功能管理类
     */
    public static ShareManager shareManager;
    /**
     * 采集物管理类
     */
    public static GatherManager gatherManager;
    /**
     * 魂兽管理类
     */
    public static SoulBeastManager soulBeastManager;
    /**
     * 造化管理类
     */
    public static NatureManager natureManager;
    /**
     * 天书符咒管理类
     */
    public static GodBookManager godBookManager;
    /**
     * 宗派玩法管理类
     */
    public static GuildActivityManager guildActivityManager;
    /**
     * 宝石管理类
     */
    public static GemManager gemManager;
    /**
     * 境界vip管理类
     */
    public static StateVipManager stateVipManager;
    /**
     * 识海管理类
     */
    public static ShiHaiManager shiHaiManager;
    /**
     * 设置管理类
     */
    public static SettingManager settingManager;

    /**
     * 开服活动管理类
     */
    public static OpenServerAcManager openServerAcManager;

    /**
     * 仙魄系统
     */
    public static ImmortalSoulManager immortalSoulManager;

    /**
     * 寻宝系统
     */
    public static TreasureHuntManager treasureHuntManager;

    /**
     * 境界灵压
     */
    public static StateStifleManager stateStifleManager;

    /**
     * 回收炉
     */
    public static RecycleManager recycleManager;

    /**
     * 竞拍
     */
    public static AuctionManager auctionManager;

    /**
     * 世界篝火
     */
    public static WorldBonfireManager worldBonfireManager;

    /**
     * 圣装
     */
    public static HolyEquipManager holyEquipManager;

    /**
     * 八级阵图
     */
    public static EightDiagramsManager eightDiagramsManager;

    /**
     * 世界支援
     */
    public static WorldHelpManager worldHelpManager;

    /**
     * 有奖问答
     */
    public static QuestionnaireManager questionnaireManager;

    /**
     * BI
     */
    public static BIManager biManager;

    /**
     * Vip
     */
    public static VipManager vipManager;

    /**
     * 太虚战场
     */
    public static UniverseManager universeManager;

    /**
     * 仙盟争霸
     */
    public static GuildBattleManager guildBattleManager;

    /**
     * 仙甲装备
     */
    public static ImmortalEquipManager immortalEquipManager;

    /**
     * 仙甲寻宝
     */
    public static TreasureHuntXianjiaManager treasureHuntXianjiaManager;

    /**
     * 新时装
     */
    public static NewFashionManager newFashionManager;

    /**
     * 跨服排行榜
     */
    public static CrossRankManager crossRankManager;

    /**
     * 新化形
     */
    public static HuaxinFlySwordManager huaxinFlySwordManager;

    /**
     * 新婚姻
     */
    public static MarriageManager marriageManager;

    /**
     * 指挥
     */
    public static CommandManager commandManager;

    /**
     * 跨服福地
     */
    public static CrossFudManager crossFudManager;

    /**
     * 跨服神兽岛
     */
    public static SoulAnimalForestCrossManager soulAnimalForestCrossManager;

    /**
     * 抽奖管理器
     */
    public static LuckyDrawManager  luckyDrawManager;

    /**
     * 藏宝阁
     */
    public static CangbaogeManager cangbaogeManager;

    /**
     * 狂欢周
     */
    public static CrazyWeekManager crazyWeekManager;

    /**
     * 天紧令
     */
    public static FallingSkyManager fallingSkyManager;

    /**
     * 活动排行榜管理类
     */
    public static ActivityRankManager activityRankManager;

    /**
     * 犒赏令
     */
    public  static KaoShangLingManager kaoShangLingManager;
    /**
     * 日志管理
     */
    public static LogDataManager logManager;

    /**
     * 跨服坐骑Bos
     */
    public static CrossHorseBossManager crossHorseBossManager;

    /**
     * 魔魂系统
     */
    public static DevilSeriesManager devilSeriesManager;

    /**
     * 仙侣对决
     */
    public static CouplefightManager couplefightManager;

    /**
     * 社区管理
     */
    public static CommunityManager communityManager;

    /**
     * 无忧宝库
     */
    public static TreasureHuntWuyouManager treasureHuntWuyouManager;

    /**
     * 功能任务
     */
    public static FunctionTaskManager functionTaskManager;

    /**
     * 混沌虚空第二阶段须弥宝库
     */
    public static AlienGemManager alienGemManager;

    /**
     * 幻装
     */
    public static UnrealEquipManager unrealEquipManager;


    public static V4HelpManager v4HelpManager;


    /**
     * 返利
     */
    public static RechargeRebateManager rechargeRebateManager;

    public static void init(){
        publicServerManager = PublicServerManager.getInstance();
        mapManager = MapManager.getInstance();
        mapCfgManager = MapsConfigManager.getInstance();
        monsterManager = MonsterManager.getInstance();
        playerManager = PlayerManager.getInstance();
        fightManager = FightManager.getInstance();
        hatredManager = HatredManager.getInstance();
        bossManager = BossManager.getInstance();
        petManager = PetManager.getInstance();
        jjcManager = JJCManager.getInstance();
        registerManager = RegisterManager.getInstance();
        chatManager = ChatManager.getInstance();
        teamManager = TeamManager.getInstance();
        playerAttAttributeManager = PlayerAttributeManager.getInstance();
        backpackManager = BackpackManager.getInstance();
        taskManager = TaskManager.getInstance();
        countManager = CountManager.getInstance();
        cooldownManager = CooldownManager.getInstance();
        equipManager = EquipManager.getInstance();
        soulArmorManager = SoulArmorManager.getInstance();
        storeManager = StoreManager.getInstance();
        dropManager = DropManager.getInstance();
        gmCommandManager = GmCommandManager.getInstance();
        scriptManager = ScriptManager.getInstance();
        skillManager = SkillManager.getInstance();
        skilleventManager = SkillEventContainer.getInstance();
        buffManager = BuffManager.getInstance();
        npcManager = NpcManager.getInstance();
        horseManager = HorseManager.getInstance();
        heartManager = HeartManager.getInstance();
        friendManager = FriendManager.getInstance();
        shopManager = ShopManager.getInstance();
        copyMapManager = CopyMapManager.getInstance();
        mailManager = MailManager.getInstance();
        achievementManager = AchievementManager.getInstance();
        rankListManager = RankListManager.getInstance();
        titleManager = TitleManager.getInstance();
        activityManager = ActivityManager.getInstance();
        rechargeManager = RechargeManager.getInstance();
        goldManager = GoldManager.getInstance();
        peakManager = PeakManager.getInstance();
        controlManager = ControlManager.getInstance();
        crossServerManager = CrossServerManager.getInstance();
        saveThreadManager = new SaveThreadManager();
        platformevaluateManager = PlatformEvaluateManager.getInstance();
        currencyManager = new CurrencyManager();
        dailyActiveManager = DailyActiveManager.getInstance();
        guildsManager = GuildsManager.getInstance();
        commercializeManager = CommercializeManager.getInstance();
        chumManager = ChumManager.getInstance();
        leaderPreachManager = LeaderPreachManager.getInstance();
        welfareManager = WelfareManager.getInstance();
        retrieveResManager = RetrieveResManager.getInstance();
        activeCodeManager = ActiveCodeManager.getInstance();
        redPacketManager = RedPacketManager.getInstance();
        yedAiLoader = YedMgr.getInstance();
        playerHookManager = PlayerHookManager.getInstance();
        loopNotifyManager = LoopNotifyManager.getInstance();
        shareManager = ShareManager.getInstance();
        gatherManager = GatherManager.getInstance();
        soulBeastManager = SoulBeastManager.getInstance();
        natureManager = NatureManager.getInstance();
        godBookManager = GodBookManager.getInstance();
        guildActivityManager = GuildActivityManager.getInstance();
        gemManager = GemManager.getInstance();
        stateVipManager = StateVipManager.getInstance();
        shiHaiManager = ShiHaiManager.getInstance();
        settingManager = SettingManager.getInstance();
        openServerAcManager = OpenServerAcManager.getInstance();
        immortalSoulManager = ImmortalSoulManager.getInstance();
        treasureHuntManager = TreasureHuntManager.getInstance();
        stateStifleManager = StateStifleManager.getInstance();
        recycleManager = RecycleManager.getInstance();
        auctionManager = AuctionManager.getInstance();
        worldBonfireManager = WorldBonfireManager.getInstance();
        holyEquipManager = HolyEquipManager.getInstance();
        eightDiagramsManager = EightDiagramsManager.getInstance();
        worldHelpManager = WorldHelpManager.getInstance();
        questionnaireManager = QuestionnaireManager.getInstance();
        biManager = BIManager.getInstance();
        vipManager = VipManager.getInstance();
        universeManager = UniverseManager.getInstance();
        guildBattleManager = GuildBattleManager.getInstance();
        immortalEquipManager = ImmortalEquipManager.getInstance();
        treasureHuntXianjiaManager = TreasureHuntXianjiaManager.getInstance();
        newFashionManager  = NewFashionManager.getInstance();
        crossRankManager = CrossRankManager.getInstance();
        huaxinFlySwordManager = HuaxinFlySwordManager.getInstance();
        marriageManager = MarriageManager.getInstance();
        commandManager = CommandManager.getInstance();
        crossFudManager = CrossFudManager.getInstance();
        soulAnimalForestCrossManager = SoulAnimalForestCrossManager.getInstance();
        luckyDrawManager = LuckyDrawManager.getInstance();
        cangbaogeManager = CangbaogeManager.getInstance();
        crazyWeekManager = CrazyWeekManager.getInstance();
        fallingSkyManager = FallingSkyManager.getInstance();
        activityRankManager = ActivityRankManager.getInstance();

        kaoShangLingManager = KaoShangLingManager.getInstance();

        logManager = LogDataManager.instance;
        crossHorseBossManager = CrossHorseBossManager.getInstance();
        devilSeriesManager = DevilSeriesManager.instance;
        couplefightManager = CouplefightManager.getInstance();
        communityManager = CommunityManager.getInstance();
        treasureHuntWuyouManager = TreasureHuntWuyouManager.instance;
        functionTaskManager = FunctionTaskManager.instance;
        alienGemManager = AlienGemManager.instance;
        unrealEquipManager = UnrealEquipManager.getInstance();

        v4HelpManager = V4HelpManager.getInstance();

        rechargeRebateManager = RechargeRebateManager.getInstance();
    }
}
