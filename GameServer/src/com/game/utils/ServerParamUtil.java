package com.game.utils;

import com.game.auction.structs.AuctionRecord;
import com.game.boss.struct.Boss;
import com.game.boss.struct.BossData;
import com.game.boss.struct.BossTypeConst;
import com.game.cangbaoge.struct.CangbaogeRecord;
import com.game.count.structs.Count;
import com.game.db.bean.ServerParamBean;
import com.game.db.dao.serverParamDao;
import com.game.guildbattle.structs.GuildBattle;
import com.game.guildbattle.structs.GuildBattleWin;
import com.game.manager.Manager;
import com.game.openserverac.structs.LuckyCardRewardLog;
import com.game.openserverac.structs.V4HelpBean;
import com.game.openserverac.structs.V4HelpRecordLog;
import com.game.server.DbSqlName;
import com.game.server.GameServer;
import com.game.server.thread.SaveServer;
import com.game.shop.structs.NewFreeShop;
import com.game.treasurehunt.struct.TreasureHuntRecord;
import com.game.welfare.struct.UpdateNoticeData;
import game.core.json.TypeReference;
import game.core.util.JsonUtils;
import game.core.util.VersionUpdateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 */
public class ServerParamUtil {

    private static final Logger logger = LogManager.getLogger(ServerParamUtil.class);

    private static final serverParamDao serverParamDao = new serverParamDao();

    /**
     * 服务器名字
     */
    private static final String ServerParamKey_ServerName = "ServerName";

    /**
     * 服务器开放时间
     */
    private static final String ServerParamKey_SeverOpenTime = "ServerOpenTime";

    /**
     * 服务器注册限制人数
     */
    private static final String ServerParamKey_RegisterNumLimit = "registerNumLimit";
    /**
     * 服务器开关控制
     */
    private static final String ServerParamKey_Control = "ServerControl";
    /**
     * 服务器世界等级
     */
    private static final String ServerParamKey_WorldLv = "ServerWorldLv";
    /**
     * 服务器角色存储测试
     */
    private static final String ServerParamKey_RoleNameTest = "ServerRoleNameTest";

    /**
     * 巅峰竞技场赛季Key
     */
    private static final String ServerParamKey_PeakSeason = "ServerPeakSeason";

    /**
     * 服务器Pk之王称号
     */
    private static final String ServerParamKey_PkKingRoleID = "ServerPkKingRoleID";

    /**
     * 服务器世界boss
     */
    private static final String ServerParamKey_WorldBoss = "ServerWorldBoss";

    /**
     * 服务器boss之家
     */
    private static final String ServerParamKey_BossHome = "ServerBossHome";

    /**
     * 全服次数
     */
    private static final String ServerParamKey_Count = "ServerCount";
    /**
     * 全服寻宝记录
     */
    private static final String ServerParamKey_TreasureHunt = "ServerTreasureHunt";

    /**
     * 开服狂欢充值排行榜
     */
    private static final String ServerParamKey_OpenServerRevel = "ServerRevel";

    /**
     * 世界竞拍记录
     */
    private static final String ServerParamKey_AuctionRecord = "ServerAuction";

    /**
     * 藏宝阁记录
     */
    private static final String ServerParamKey_CangbaogeRecord = "CangbaogeRecord";

    /**
     * 幸运翻牌记录
     */
    private static final String ServerParamKey_LuckyCardRecord = "LuckyCardRecord";

    /**
     * 服务器魂兽boss
     */
    private static final String ServerParamKey_LocalSoulAnimal = "localSoulAnimalBossData";

    /**
     * 服务器仙盟评级数据
     */
    private static final String ServerParamKey_GuildBattleRate = "guildBattleRate";

    /**
     * 服务器仙盟连赢数据
     */
    private static final String ServerParamKey_GuildBattleWin = "guildBattleWin";

    /**
     * 等级礼包全服限领次数
     */
    private static final String ServerParamKey_LevelGift = "levelGift";

    /**
     * 适当通用处理
     */
    private static final String ServerParamKey_Common = "common";

    /**
     * 拍卖行 活动 拍卖 -玩家分红列表
     */
    private static final String ServerParamKey_AuctionRoleID = "auctionRoleIDData";

    /**
     * 仙甲寻宝数据
     */
    private static final String ServerParamKey_XianjiaHunt = "xianjiaHuntData";

    /**
     * 跨服活动数据
     */
    private static final String ServerParamKey_CrossDailyData = "crossDailyData";

    /**
     * 有奖问卷数据
     */
    private static final String ServerParamKey_QuestionnaireData = "QuestionnaireData";

    /**
     * 婚宴数据
     */
    private static final String ServerParamKey_WeddingData = "WeddingData";

    /**
     * 结婚对数
     */
    private static final String ServerParamKey_WeddingNum = "WeddingNum";

    /**
     * 服务器首杀数据
     */
    private static final String ServerParamKey_ServerFirstKillData = "ServerFirstKillData";

    /**
     * 更新公告数据
     */
    private static final String ServerParamKey_UpdateNoticeData = "UpdateNoticeData";

    /**
     * 藏宝阁抽奖轮数 wait时间
     */
    private static final String ServerParamKey_CangbaogeRoundData = "CangbaogeRoundData";

    /**
     * 藏宝阁兑换数据
     */
    private static final String ServerParamKey_CangbaogeExChangeData = "CangbaogeExChangeData";

    /**
     * 服务器SDK开关评价数据
     */
    private static final String ServerParamKey_EvaluateData = "EvaluateData";

    /**
     * 天禁令
     */
    private static final String ServerParamKey_FallingSkyData = "FallingSky";

    /**
     * 运营活动幸运值
     */
    private static final String ServerParamKey_TotalLuckyValue = "totalLuckyValue";

    /**
     * 限时活动排行榜奖励领取服务器次数
     */
    private static final String ServerParamKey_ActivityRank = "ActivityRankAwardTotal";

    /**
     * 节日签到活动服务器总签到次数
     */
    private static final String ServerParamKey_FestivalSignTotal = "FestivalSignTotal";

    /**
     * 登录检测
     */
    private static final String ServerParamKey_LoginCheck = "LoginCheck";

    /**
     * 充值检测
     */
    private static final String ServerParamKey_RchargeCheck = "RchargeCheck ";


    /**
     * 情缘商店购买记录
     */
    private static final String ServerParamKey_MarryActivityShopBuy = "MarryActivityShopBuy ";

    /**
     * 新零元购购买记录
     */
    private static final String ServerParamkey_NewFreeShopBuyInfos = "NewFreeShopBuyInfos";

    /**
     * 全服寻宝次数记录
     */
    private static final String  ServerParamKey_TreasureHuntCount = "TreasureHuntCountStr" ;//全服寻宝次数记录

    /**
     * v4助力申请列表
     */
    private static final String  ServerParamKey_v4HelpApplyMap = "v4HelpApplyMap" ;
    /**
     * v4助力数据
     */
    private static final String  ServerParamKey_v4HelpBeanMap = "v4HelpBeanMap" ;
    /**
     * v4助力日志
     */
    private static final String  ServerParamKey_v4HelpRecordLog = "v4HelpRecordLog" ;

    /**
     * 大玩咖刷新时间
     */
    private static final String ServerParamKey_BigPlayerRefreshTime = "bigPlayerRefreshTime";
    /**
     * 大玩咖刷新时间
     */
    public static long bigPlayerRefreshTime = 0L;

    public static String serverName = "";

    public static String serverOpenTime = "";

    public static int registerNumLimit = 0;

    public static String roleNameTest = "";

    public static int worldLv = 0;

    public static long pkKingRoleId = 0L;

    public static int peakSeason = 0;

    public static volatile int weddingNum = 0;

    public static int festivalSignTotal = 0;

    public static int isLoginCheck = 0;//登录检测

    public static int isRechargeCheck = 0;//充值检测

    public static  ConcurrentHashMap<Integer, Integer> treasureHuntCount = new ConcurrentHashMap<>() ;//全服寻宝次数记录

    public static List<AuctionRecord> auctionRecords = new ArrayList<>();

    public static List<CangbaogeRecord> cangbaogeRecords = new ArrayList<>();

    public static List<LuckyCardRewardLog> luckyCardRecords = new ArrayList<>();

    public static ConcurrentHashMap<Integer, Integer> funcs = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Integer, BossData> BossDataMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Count> counts = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Integer, List<TreasureHuntRecord>> treasureHuntRecordMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Integer, BossData> localSoulAnimalDataMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Long, Integer> serverRevelMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Long, List<Long>> auctionRoleIDMap = new ConcurrentHashMap<>();

    public static List<NewFreeShop> newFreeShopBuyInfos = new ArrayList<>();

    /**
     * 等级礼包全服限领次数
     */
    public static ConcurrentHashMap<Integer, Integer> levelGiftMap = new ConcurrentHashMap<>();

    /**
     * 仙甲寻宝轮数 到期时间
     */
    public static ConcurrentHashMap<Integer, Long> huntXianjiaData = new ConcurrentHashMap<>();

    /**
     * 跨服活动相关数据<活动ID,跨服活动数据>
     */
    public static ConcurrentHashMap<Integer, String> crossDailyData = new ConcurrentHashMap<>();

    /**
     * 适当通用的存储
     */
    public static ConcurrentHashMap<String, String> commonMap = new ConcurrentHashMap<>();

    /**
     * 有奖问卷数据
     */
    public static ConcurrentHashMap<Long, Long> questionnaireData = new ConcurrentHashMap<>();

    /**
     * 婚宴数据
     */
    public static ConcurrentHashMap<Long, Integer> weddingList = new ConcurrentHashMap<>();

    /**
     * 服务器首杀数据
     */
    public static ConcurrentHashMap<Integer, String> serverFirstKillList = new ConcurrentHashMap<>();

    /**
     * 更新公告数据
     */
    public static UpdateNoticeData updateNoticeData = new UpdateNoticeData();

    /**
     * 藏宝阁抽奖轮数 时间
     */
    public static ConcurrentHashMap<Integer, Long> cangbaogeRoundData = new ConcurrentHashMap<>();

    /**
     * 藏宝阁兑换数据
     */
    public static ConcurrentHashMap<Long, HashMap<Integer, Integer>> cangbaogeExchangeData = new ConcurrentHashMap<>();

    /**
     * 服务器SDK评价开关数据
     */
    public static ConcurrentHashMap<Integer, Boolean> serverEvaluateData = new ConcurrentHashMap<>();


    /**
     * 天紧令轮数 到期时间
     */
    public static ConcurrentHashMap<Integer, Long> fallingSkyData = new ConcurrentHashMap<>();

    /**
     * 限时活动排行榜奖励服务器领取次数
     */
    public static ConcurrentHashMap<Integer, Integer> activityRankAwardGetTimes = new ConcurrentHashMap<>();


    public static void saveServerOpenTime() {
        saveServerParam(ServerParamKey_SeverOpenTime, serverOpenTime);
    }

    public static void saveServerRegisterLimit() {
        saveServerParam(ServerParamKey_RegisterNumLimit, registerNumLimit + "");
    }

    public static void saveServerName() {
        saveServerParam(ServerParamKey_ServerName, serverName);
    }

    public static void saveWorldLv() {
        saveServerParam(ServerParamKey_WorldLv, String.valueOf(worldLv));
    }

    public static void savePeakSeason() {
        saveServerParam(ServerParamKey_PeakSeason, String.valueOf(peakSeason));
    }

    public static void saveTreasureHuntCount() {
        saveServerParam(ServerParamKey_TreasureHuntCount, JsonUtils.toJSONString(treasureHuntCount));
    }


    public static int saveRoleNameTest() {
        return nowServerSaveParam(ServerParamKey_RoleNameTest, roleNameTest);
    }

    public static void saveFestivalSignTotal() {
        saveServerParam(ServerParamKey_FestivalSignTotal, String.valueOf(festivalSignTotal));
    }

    public static void saveWorldBoss() {
        saveServerParam(ServerParamKey_WorldBoss, JsonUtils.toJSONString(BossDataMap));
    }

    public static void saveBossHome() {
        saveServerParam(ServerParamKey_BossHome, JsonUtils.toJSONString(Manager.bossManager.getBossHome()));
    }

    public static void saveSoulAnimalBoss() {
        saveServerParam(ServerParamKey_LocalSoulAnimal, JsonUtils.toJSONString(localSoulAnimalDataMap));
    }

    public static void saveGuildBattleRate() {
        saveServerParam(ServerParamKey_GuildBattleRate, JsonUtils.toJSONString(Manager.guildBattleManager.getGuildbattles()));
    }

    public static void saveGuildBattleWin() {
        saveServerParam(ServerParamKey_GuildBattleWin, JsonUtils.toJSONString(Manager.guildBattleManager.getGuildBattleWin()));
    }

    public static void saveFuncs() {
        saveServerParam(ServerParamKey_Control, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(funcs)));
    }

    public static void saveCounts() {
        saveServerParam(ServerParamKey_Count, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(counts)));
    }

    public static void saveTreasureHunt() {
        saveServerParam(ServerParamKey_TreasureHunt, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(treasureHuntRecordMap)));
    }

    public static void saveRevel() {
        saveServerParam(ServerParamKey_OpenServerRevel, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(serverRevelMap)));
    }

    public static void saveAuctionRecord() {
        saveServerParam(ServerParamKey_AuctionRecord, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(auctionRecords)));
    }

    public static void saveCangbaogeRecord() {
        saveServerParam(ServerParamKey_CangbaogeRecord, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(cangbaogeRecords)));
    }

    public static void saveLuckyCardRecord() {
        saveServerParam(ServerParamKey_LuckyCardRecord, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(luckyCardRecords)));
    }

    public static void saveNewFreeShopBuyInfosRecord() {
        saveServerParam(ServerParamkey_NewFreeShopBuyInfos, JsonUtils.toJSONString(newFreeShopBuyInfos));
    }


    public static void saveLevelGift() {
        saveServerParam(ServerParamKey_LevelGift, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(levelGiftMap)));
    }

    public static void saveCommon() {
        saveServerParam(ServerParamKey_Common, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(commonMap)));
    }

    public static void saveAuctionRoleID() {
        saveServerParam(ServerParamKey_AuctionRoleID, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(auctionRoleIDMap)));
    }

    public static void savaXianjiaHuntData() {
        saveServerParam(ServerParamKey_XianjiaHunt, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(huntXianjiaData)));
    }

    public static void saveFallingSkyData() {
        saveServerParam(ServerParamKey_FallingSkyData, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(fallingSkyData)));
    }

    public static void saveCrossDailyData() {
        saveServerParam(ServerParamKey_CrossDailyData, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(crossDailyData)));
    }

    public static void saveQuestionnaire() {
        saveServerParam(ServerParamKey_QuestionnaireData, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(questionnaireData)));
    }

    public static void saveWeddingData() {
        saveServerParam(ServerParamKey_WeddingData, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(weddingList)));
    }

    public static void saveWeddingNum() {
        saveServerParam(ServerParamKey_WeddingNum, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(weddingNum)));
    }

    public static void saveServerFirstKillData() {
        saveServerParam(ServerParamKey_ServerFirstKillData, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(serverFirstKillList)));
    }

    public static void saveUpdateNoticeData() {
        saveServerParam(ServerParamKey_UpdateNoticeData, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(updateNoticeData)));
    }

    public static void savaCangbaogeRoundData() {
        saveServerParam(ServerParamKey_CangbaogeRoundData, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(cangbaogeRoundData)));
    }

    public static void saveCangbaogeExchangeData() {
        saveServerParam(ServerParamKey_CangbaogeExChangeData, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(cangbaogeExchangeData)));
    }

    public static void saveTotalLuckyValue() {
        saveServerParam(ServerParamKey_TotalLuckyValue, Manager.activityManager.getTotalLuckyValue() + "");
    }

    public static void saveEvaluateData() {
        saveServerParam(ServerParamKey_EvaluateData, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(serverEvaluateData)));
    }

    public static void saveActivityRankAwardGetData() {
        saveServerParam(ServerParamKey_ActivityRank, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(activityRankAwardGetTimes)));
    }

    public static void saveLoginCheck(){
        isLoginCheck = 1;
        saveServerParam(ServerParamKey_LoginCheck, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(isLoginCheck)));
    }

    public static void saveRchargeCheck(){
        isRechargeCheck = 1;
        saveServerParam(ServerParamKey_RchargeCheck, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(isRechargeCheck)));
    }


    public static void saveMarryActivityShopBuy() {
        saveServerParam(ServerParamKey_MarryActivityShopBuy, VersionUpdateUtil.dataSave(JsonUtils.toJSONString( Manager.marriageManager.getMarryActivityShopBuyCountMap())));
    }


    public static void saveV4HelpApplyMap() {
        saveServerParam(ServerParamKey_v4HelpApplyMap, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(Manager.v4HelpManager.getV4HelpApplyMap())));
    }

    public static void saveV4HelpBeanMap() {
        saveServerParam(ServerParamKey_v4HelpBeanMap, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(Manager.v4HelpManager.getV4HelpBeanMap())));
    }

    public static void saveV4HelpRecordLog() {
        saveServerParam(ServerParamKey_v4HelpRecordLog, VersionUpdateUtil.dataSave(JsonUtils.toJSONString(Manager.v4HelpManager.getV4HelpRecordLog())));
    }
    public static void saveBigPlayerRefreshTime(){
            saveServerParam(ServerParamKey_BigPlayerRefreshTime, String.valueOf(bigPlayerRefreshTime));
    }

    /**
     * 服务器开启时立即读取
     *
     * @return
     */
    public static boolean loadServerParam() {
        List<ServerParamBean> serverParams = serverParamDao.selectAll();
        for (ServerParamBean serverParam : serverParams) {
            if (serverParam == null) {
                continue;
            }

            if (serverParam.getServerid() != GameServer.getInstance().getServerId()) {
                continue;
            }
            switch (serverParam.getParamkey()) {
                case ServerParamKey_TotalLuckyValue:
                    Manager.activityManager.setTotalLuckyValue(Integer.parseInt(serverParam.getParamvalue()));
                    break;
                case ServerParamKey_ServerName:
                    serverName = serverParam.getParamvalue();
                    break;
                case ServerParamKey_SeverOpenTime:
                    serverOpenTime = serverParam.getParamvalue();
                    break;
                case ServerParamKey_RegisterNumLimit:
                    try {
                        registerNumLimit = Integer.valueOf(serverParam.getParamvalue());
                    } catch (Exception e) {
                        logger.error("服务器注册限制人数加载错误", e);
                    }
                    break;
                case ServerParamKey_Control:
                    try {
                        ConcurrentHashMap<Integer, Integer> func = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Integer, Integer>>() {
                        });
                        ServerParamUtil.funcs.putAll(func);
                    } catch (Exception e) {
                        logger.error("后台开关加载错误", e);
                    }
                    break;
                case ServerParamKey_PkKingRoleID:
                    pkKingRoleId = Long.parseLong(serverParam.getParamvalue());
                    break;
                case ServerParamKey_BigPlayerRefreshTime:
                    bigPlayerRefreshTime = Long.parseLong(serverParam.getParamvalue());
                    break;
                case ServerParamKey_PeakSeason:
                    peakSeason = Integer.parseInt(serverParam.getParamvalue());
                    break;
                case ServerParamKey_WorldBoss:
                    try {
                        ConcurrentHashMap<Integer, BossData> BossDataMap = JsonUtils.parseObject(serverParam.getParamvalue(), new TypeReference<ConcurrentHashMap<Integer, BossData>>() {
                        });
                        ServerParamUtil.BossDataMap.putAll(BossDataMap);
                    } catch (Exception e) {
                        logger.error("世界boss soul错误", e);
                    }
                    break;
                case ServerParamKey_BossHome:
                    try {
                        ConcurrentHashMap<Integer, Boss> BossHomeMap = JsonUtils.parseObject(serverParam.getParamvalue(), new TypeReference<ConcurrentHashMap<Integer, Boss>>() {
                        });
                        Manager.bossManager.getBossHome().putAll(BossHomeMap);
                    } catch (Exception e) {
                        logger.error("boss之家 解析错误", e);
                    }
                    break;
                case ServerParamKey_LocalSoulAnimal:
                    try {
                        ConcurrentHashMap<Integer, BossData> localSoulAnimalDataMap = JsonUtils.parseObject(serverParam.getParamvalue(), new TypeReference<ConcurrentHashMap<Integer, BossData>>() {
                        });
                        ServerParamUtil.localSoulAnimalDataMap.putAll(localSoulAnimalDataMap);
                    } catch (Exception e) {
                        logger.error("世界boss soul错误", e);
                    }
                    break;
                case ServerParamKey_GuildBattleRate:
                    try {
                        ConcurrentHashMap<Long, GuildBattle> guildBattleMap = JsonUtils.parseObject(serverParam.getParamvalue(), new TypeReference<ConcurrentHashMap<Long, GuildBattle>>() {
                        });
                        Manager.guildBattleManager.getGuildbattles().putAll(guildBattleMap);
                    } catch (Exception e) {
                        logger.error("公会评级数据错误", e);
                    }
                    break;
                case ServerParamKey_GuildBattleWin:
                    try {
                        GuildBattleWin guildBattleWin = JsonUtils.parseObject(serverParam.getParamvalue(), GuildBattleWin.class);
                        Manager.guildBattleManager.setGuildBattleWin(guildBattleWin);
                    } catch (Exception e) {
                        logger.error("公会连胜数据错误", e);
                    }
                    break;
                case ServerParamKey_Count:
                    try {
                        ConcurrentHashMap<String, Count> counts = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<String, Count>>() {
                        });
                        ServerParamUtil.counts.putAll(counts);
                    } catch (Exception e) {
                        logger.error("服务器变量次数错误", e);
                    }
                    break;
                case ServerParamKey_WorldLv:
                    worldLv = Integer.parseInt(serverParam.getParamvalue());
                    break;
                case ServerParamKey_TreasureHunt:
                    try {
                        ConcurrentHashMap<Integer, List<TreasureHuntRecord>> treasureHuntRecordMap = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Integer, List<TreasureHuntRecord>>>() {
                        });
                        ServerParamUtil.treasureHuntRecordMap.putAll(treasureHuntRecordMap);
                    } catch (Exception e) {
                        logger.error("世界boss soul错误", e);
                    }
                    break;
                case ServerParamKey_OpenServerRevel:
                    try {
                        ConcurrentHashMap<Long, Integer> serverRevelMap = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Long, Integer>>() {
                        });
                        ServerParamUtil.serverRevelMap.putAll(serverRevelMap);
                    } catch (Exception e) {
                        logger.error("开服狂欢充值活动记录", e);
                    }
                    break;
                case ServerParamKey_AuctionRecord:
                    try {
                        List<AuctionRecord> auctionRecords = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ArrayList<AuctionRecord>>() {
                        });
                        ServerParamUtil.auctionRecords.addAll(auctionRecords);
                    } catch (Exception e) {
                        logger.error("拍卖行世界记录", e);
                    }
                    break;
                case ServerParamKey_CangbaogeRecord:
                    try {
                        List<CangbaogeRecord> records = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ArrayList<CangbaogeRecord>>() {
                        });
                        ServerParamUtil.cangbaogeRecords.addAll(records);
                    } catch (Exception e) {
                        logger.error("藏宝阁记录", e);
                    }
                    break;
                case ServerParamKey_LuckyCardRecord:
                    try {
                        List<LuckyCardRewardLog> records = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ArrayList<LuckyCardRewardLog>>() {
                        });
                        ServerParamUtil.luckyCardRecords.addAll(records);
                    } catch (Exception e) {
                        logger.error("幸运翻牌记录", e);
                    }
                    break;
                case ServerParamKey_LevelGift:
                    try {
                        ConcurrentHashMap<Integer, Integer> lg = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Integer, Integer>>() {
                        });
                        ServerParamUtil.levelGiftMap.putAll(lg);
                    } catch (Exception e) {
                        logger.error("等级礼包全服限领数据加载错误", e);
                    }
                    break;
                case ServerParamKey_Common:
                    try {
                        ConcurrentHashMap<String, String> c = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<String, String>>() {
                        });
                        ServerParamUtil.commonMap.putAll(c);
                    } catch (Exception e) {
                        logger.error("通用全局数据保存：", e);
                    }
                    break;
                case ServerParamKey_AuctionRoleID:
                    try {
                        ConcurrentHashMap<Long, List<Long>> c = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Long, List<Long>>>() {
                        });
                        ServerParamUtil.auctionRoleIDMap.putAll(c);
                    } catch (Exception e) {
                        logger.error("拍卖行分红玩家数据出错：", e);
                    }
                    break;

                case ServerParamKey_XianjiaHunt:
                    try {
                        ConcurrentHashMap<Integer, Long> huntXianjiaData = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Integer, Long>>() {
                        });
                        ServerParamUtil.huntXianjiaData.putAll(huntXianjiaData);
                    } catch (Exception e) {
                        logger.error("仙甲寻宝数据错误：", e);
                    }
                    break;
                case ServerParamKey_FallingSkyData:
                    try {
                        ConcurrentHashMap<Integer, Long> fallingSkyData = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Integer, Long>>() {
                        });
                        ServerParamUtil.fallingSkyData.putAll(fallingSkyData);
                    } catch (Exception e) {
                        logger.error("天禁令数据错误：", e);
                    }
                    break;
                case ServerParamKey_CangbaogeRoundData:
                    try {
                        ConcurrentHashMap<Integer, Long> cangbaogeRoundData = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Integer, Long>>() {
                        });
                        ServerParamUtil.cangbaogeRoundData.putAll(cangbaogeRoundData);
                    } catch (Exception e) {
                        logger.error("藏宝阁轮数据错误：", e);
                    }
                    break;

                case ServerParamKey_CangbaogeExChangeData:
                    try {
                        ConcurrentHashMap<Long, HashMap<Integer, Integer>> cangbaogeExChangeData = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Long, HashMap<Integer, Integer>>>() {
                        });
                        ServerParamUtil.cangbaogeExchangeData.putAll(cangbaogeExChangeData);
                    } catch (Exception e) {
                        logger.error("藏宝阁兑换据错误：", e);
                    }
                    break;

                case ServerParamKey_CrossDailyData:
                    try {
                        ConcurrentHashMap<Integer, String> crossDailyData = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Integer, String>>() {
                        });
                        ServerParamUtil.crossDailyData.putAll(crossDailyData);
                    } catch (Exception e) {
                        logger.error("跨服活动数据错误：", e);
                    }
                    break;
                case ServerParamKey_QuestionnaireData:
                    try {
                        ConcurrentHashMap<Long, Long> questionnaireData = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Long, Long>>() {
                        });
                        ServerParamUtil.questionnaireData.putAll(questionnaireData);
                    } catch (Exception e) {
                        logger.error("有奖问卷数据错误：", e);
                    }
                    break;
                case ServerParamKey_WeddingData:
                    try {
                        ConcurrentHashMap<Long, Integer> weddingListData = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Long, Integer>>() {
                        });
                        ServerParamUtil.weddingList.putAll(weddingListData);
                    } catch (Exception e) {
                        logger.error("婚宴数据错误:", e);
                    }
                    break;
                case ServerParamKey_WeddingNum:
                    weddingNum = Integer.parseInt(serverParam.getParamvalue());
                    break;
                case ServerParamKey_ServerFirstKillData:
                    try {
                        ConcurrentHashMap<Integer, String> serverFirsList = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Integer, String>>() {
                        });
                        ServerParamUtil.serverFirstKillList.putAll(serverFirsList);
                    } catch (Exception e) {
                        logger.error("服务器首杀boss数据错误:", e);
                    }
                    break;
                case ServerParamKey_UpdateNoticeData:
                    try {
                        updateNoticeData = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), UpdateNoticeData.class);
                        //logger.error("领取角色ID列表:"+updateNoticeData.getReceiveRoles().size());
                    } catch (Exception e) {
                        logger.error("更新公告数据错误;", e);
                    }
                    break;
                case ServerParamKey_EvaluateData:
                    try {
                        ConcurrentHashMap<Integer, Boolean> serverEvaluateData = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Integer, Boolean>>() {
                        });
                        ServerParamUtil.serverEvaluateData.putAll(serverEvaluateData);
                        //logger.error("服务器SDK评价开关数据:");
                    } catch (Exception e) {
                        logger.error("服务器SDK评价开关数据错误;", e);
                    }
                    break;
                case ServerParamKey_ActivityRank:
                    try {
                        ConcurrentHashMap<Integer, Integer> serverData = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Integer, Integer>>() {
                        });
                        ServerParamUtil.activityRankAwardGetTimes.putAll(serverData);
                    } catch (Exception e) {
                        logger.error("限时活动排行榜服务器领奖次数数据错误;", e);
                    }
                    break;
                case ServerParamKey_FestivalSignTotal:
                    try {
                        festivalSignTotal = Integer.valueOf(serverParam.getParamvalue());
                    } catch (Exception e) {
                        logger.error("节日活动全服签到次数加载错误", e);
                    }
                    break;
                case ServerParamKey_LoginCheck:
                    try {
                        isLoginCheck = Integer.valueOf(serverParam.getParamvalue());
                    } catch (Exception e) {
                        logger.error("登录检测加载错误", e);
                    }
                    break;

                case ServerParamKey_RchargeCheck:
                    try {
                        isRechargeCheck = Integer.valueOf(serverParam.getParamvalue());
                    } catch (Exception e) {
                        logger.error("登录检测加载错误", e);
                    }
                    break;
                case ServerParamKey_MarryActivityShopBuy:
                    try {
                        ConcurrentHashMap<Long, ConcurrentHashMap<Integer,Integer>> marryActivityShopBuyCountMap = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(serverParam.getParamvalue()), new TypeReference<ConcurrentHashMap<Long, ConcurrentHashMap<Integer,Integer>>>() {
                        });
                        Manager.marriageManager.getMarryActivityShopBuyCountMap().putAll(marryActivityShopBuyCountMap);
                    } catch (Exception e) {
                        logger.error("登录检测加载错误", e);
                    }
                    break;
                case ServerParamKey_TreasureHuntCount:
                    try {
                        ConcurrentHashMap<Integer, Integer> data = JsonUtils.parseObject(serverParam.getParamvalue(), new TypeReference<ConcurrentHashMap<Integer, Integer>>() {
                        });
                        ServerParamUtil.treasureHuntCount.putAll(data);
                    } catch (Exception e) {
                        logger.error("寻宝次数记录;", e);
                    }
                    break;
                case ServerParamkey_NewFreeShopBuyInfos:
                    try {
                        List<NewFreeShop>  data = JsonUtils.parseObject(serverParam.getParamvalue(), new TypeReference<List<NewFreeShop>>() {
                        });
                        ServerParamUtil.newFreeShopBuyInfos.addAll(data);
                    } catch (Exception e) {
                        logger.error("寻宝次数记录;", e);
                    }
                    break;
                case ServerParamKey_v4HelpApplyMap:
                    try {
                        ConcurrentHashMap<Long, Long>  data = JsonUtils.parseObject(serverParam.getParamvalue(), new TypeReference<ConcurrentHashMap<Long, Long>>() {
                        });
                       Manager.v4HelpManager.getV4HelpApplyMap().putAll(data);
                    } catch (Exception e) {
                        logger.error("v4助力申请列表记录;", e);
                    }
                    break;
                case ServerParamKey_v4HelpBeanMap:
                    try {
                        ConcurrentHashMap<Long, V4HelpBean>  data = JsonUtils.parseObject(serverParam.getParamvalue(), new TypeReference<ConcurrentHashMap<Long, V4HelpBean>>() {
                        });
                        Manager.v4HelpManager.getV4HelpBeanMap().putAll(data);
                    } catch (Exception e) {
                        logger.error("v4助力数据记录;", e);
                    }
                    break;
                case ServerParamKey_v4HelpRecordLog:
                    try {
                        List<V4HelpRecordLog>  data = JsonUtils.parseObject(serverParam.getParamvalue(), new TypeReference<List<V4HelpRecordLog>>() {
                        });
                        Manager.v4HelpManager.getV4HelpRecordLog().addAll(data);
                    } catch (Exception e) {
                        logger.error("v4助力日志记录;", e);
                    }
                    break;
            }
        }
        logger.info("服务器参数加载成功！");
        return true;
    }

    private static void saveServerParam(String paramKey, String paramValue) {
        ServerParamBean serverParam = new ServerParamBean();
        serverParam.setParamkey(paramKey);
        serverParam.setServerid(GameServer.getInstance().getServerId());
        serverParam.setParamvalue(paramValue);
        Manager.saveThreadManager.getOtherServerSave().deal(serverParam, DbSqlName.SERVERPARAM_UPDATE, SaveServer.UPDATE);
    }

    private static int nowServerSaveParam(String paramKey, String paramValue) {
        ServerParamBean serverParam = new ServerParamBean();
        serverParam.setParamkey(paramKey);
        serverParam.setServerid(GameServer.getInstance().getServerId());
        serverParam.setParamvalue(paramValue);
        if (serverParamDao.update(DbSqlName.SERVERPARAM_UPDATE.getName(), serverParam) == 0) {
            logger.error(String.format("serverParamMap保存update错误！paramKey = %s", paramKey));
            if (serverParamDao.insert(DbSqlName.SERVERPARAM_INSERT.getName(), serverParam) == 0) {
                logger.error(String.format("serverParamMap保存insert错误！paramKey = %s", paramKey));
                return 0;
            }
        }
        return 1;
    }

    public static ConcurrentHashMap<Integer, BossData> getBossMap(int type) {
        switch (type) {
            //世界、套装、宝石在同一个配置表，数据库中共用一个数据
            case BossTypeConst.WORLD_BOSS:
            case BossTypeConst.SUIT_BOSS:
            case BossTypeConst.GEM_BOSS:
                return BossDataMap;
            default:
                return null;
        }
    }

    public static void saveBoss(int type) {
        switch (type) {
            case BossTypeConst.WORLD_BOSS:
                saveWorldBoss();
                break;
            case BossTypeConst.HOME_BOSS:
                saveBossHome();
            case BossTypeConst.SOULANIMAL_BOSS:
                saveSoulAnimalBoss();
                break;
        }
    }
}
