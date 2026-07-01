package com.game.boss.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Bossnew_world_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.boss.script.IStateBossScript;
import com.game.boss.script.IWorldBossScript;
import com.game.boss.struct.*;
import com.game.db.bean.BossDieRecordBean;
import com.game.db.dao.BossDieRecordDao;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.soulanimalforest.script.ISoulAnimalForestManager;
import com.game.universe.script.IUniverseWarScript;
import com.game.utils.ServerParamUtil;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.core.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class BossManager {

    private final static Logger logger = LogManager.getLogger(BossManager.class);

    /**
     * boss副本ID ->地图ID
     */
    final ConcurrentHashMap<Integer, Long> bossZone = new ConcurrentHashMap<>();
    /**
     * 新手层 boss缓存
     */
    final ConcurrentHashMap<Integer, Boss> noobBossList = new ConcurrentHashMap<>();
    /**
     * boss之家缓存
     */
    final ConcurrentHashMap<Integer, Boss> bossHome = new ConcurrentHashMap<>();

    /**
     * 套装boss刷新数据
     */
    final ConcurrentHashMap<Integer, Boss> suitBossMap = new ConcurrentHashMap<>();


    public ConcurrentHashMap<Integer, Boss> getNoobBossList() {
        return noobBossList;
    }

    public ConcurrentHashMap<Integer, Long> getBossZone() {
        return bossZone;
    }

    public ConcurrentHashMap<Integer, Boss> getBossHome() {
        return bossHome;
    }

    public ConcurrentHashMap<Integer, Boss> getSuitBossMap() {
        return suitBossMap;
    }

    /**
     * 世界boss数据缓存
     */
    private static final ConcurrentHashMap<Integer, Boss> worldBossMap = new ConcurrentHashMap<>();

    /**
     * 本地魂兽森林 的BOSS信息
     */
    private static final ConcurrentHashMap<Integer, Boss> localSoulAnimalCache = new ConcurrentHashMap<>();
    private static int soulAnimalForestBossId = 0;

    /**
     * 兽灵水晶的最后一次刷新的时间 BeastSpiritCrystal  不需要保存数据库
     */
    private static final ConcurrentHashMap<Integer, BossData> crystalTime = new ConcurrentHashMap<>();//<副本ID，兽灵与兽神的重生时间管理>
    public static final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> beastSpiritCrystalMap = new ConcurrentHashMap<>(); //，怪的配置ID， 怪物数量
    private static final ConcurrentHashMap<Integer, Integer> crystalMaxNum = new ConcurrentHashMap<>();//副本ID， 怪物总数量

    /**
     * 兽血水晶的最后一次刷新的时间 BeastlyBloodCrystal .兽血水晶根据地图内所有兽血水晶被采集完的时间动态刷新，配置方式使用幻境BOSS的配置方式。每次刷新40个。刷新数量可配置调整 fa
     */
    private static final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> beastlyBloodCrystalMap = new ConcurrentHashMap<>(); //副本id，怪的配置ID， 怪物数量 发给前端用
    public static final ConcurrentHashMap<Integer, BossData> beastlyBloodCrystalBirthTime = new ConcurrentHashMap<>();//<副本ID， 兽血水晶的重生时间管理>
    private static final ConcurrentHashMap<Integer, Integer> bloodCrystalMaxNum = new ConcurrentHashMap<>();//副本ID， 怪物总数量

    /**
     * 本服领地战 boss 刷新数据
     */
    private static final ConcurrentHashMap<Integer, Boss> manorBossMap = new ConcurrentHashMap<>();

    /**
     * 魂兽森林的守卫的最后更新时间
     */
    private static final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> soulAnimalForestMonsterMap = new ConcurrentHashMap<>(); //副本id，怪的配置ID， 怪物数量 发给前端用
    private static final ConcurrentHashMap<Integer, BossData> soulAnimalForestMonsterTime = new ConcurrentHashMap<>();//<副本ID， 精英怪的重生时间管理>
    private static final ConcurrentHashMap<Integer, Integer> soulAnimalForestMonsterMaxNum = new ConcurrentHashMap<>();//副本ID， 怪物总数量

    /**
     * Boss死亡记录dao对象
     */
    private static final BossDieRecordDao bossDieRecordDao = new BossDieRecordDao();

    /**
     * 保存记录0点计算boss的rebornBaseTime的时间(天数)
     */
    private static int calcDay;

    /**
     * 精英怪和boss之家的boss死亡都要记录进入
     */
    private List<BossDieRecord> bossDieRecords = new ArrayList<>(150);

    /**
     * 世界boss关注数据表<bossId, playerIdList> (玩家身上存一份，这里也存一份，不在线也要推送滴)
     */
    private static final ConcurrentHashMap<Integer, List<Long>> bossFollowedMap = new ConcurrentHashMap<>();

    /**
     * Boss击杀数据
     */
    private static final ConcurrentHashMap<Integer, List<KilledRecord>> bossKilledRecordMap = new ConcurrentHashMap<>();

    /**
     * 宝石boss刷新数据
     */
    private static final ConcurrentHashMap<Integer, Boss> gemBossMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<Integer, Boss> universeBossMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Integer, Boss> getWorldBossMap() {
        return worldBossMap;
    }

    public static ConcurrentHashMap<Integer, Boss> getGemBossMap() {
        return gemBossMap;
    }

    public static ConcurrentHashMap<Integer, Boss> getLocalSoulAnimalCache() {
        return localSoulAnimalCache;
    }

    public static int getSoulAnimalForestBossId() {
        return soulAnimalForestBossId;
    }

    public static void setSoulAnimalForestBossId(int soulAnimalForestBossId) {
        BossManager.soulAnimalForestBossId = soulAnimalForestBossId;
    }

    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> getBeastlyBloodCrystalMap() {
        return beastlyBloodCrystalMap;
    }

    public static ConcurrentHashMap<Integer, BossData> getBeastlyBloodCrystalBirthTime() {
        return beastlyBloodCrystalBirthTime;
    }

    public static ConcurrentHashMap<Integer, BossData> getCrystalTime() {
        return crystalTime;
    }

    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> getBeastSpiritCrystalMap() {
        return beastSpiritCrystalMap;
    }

    public static ConcurrentHashMap<Integer, Integer> getCrystalMaxNum() {
        return crystalMaxNum;
    }

    public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> getSoulAnimalForestMonsterMap() {
        return soulAnimalForestMonsterMap;
    }

    public static ConcurrentHashMap<Integer, BossData> getSoulAnimalForestMonsterTime() {
        return soulAnimalForestMonsterTime;
    }

    public static ConcurrentHashMap<Integer, List<Long>> getBossFollowedMap() {
        return bossFollowedMap;
    }

    public static ConcurrentHashMap<Integer, List<KilledRecord>> getBossKilledRecordMap() {
        return bossKilledRecordMap;
    }

    public static ConcurrentHashMap<Integer, Boss> getUniverseBossMap() {
        return universeBossMap;
    }

    public static ConcurrentHashMap<Integer, Boss> getBossMap(int type) {
        switch (type) {
            case BossTypeConst.WORLD_BOSS:
                return worldBossMap;
            case BossTypeConst.SOULANIMAL_BOSS:
                return localSoulAnimalCache;
            case BossTypeConst.SUIT_BOSS:
                return Manager.bossManager.getSuitBossMap();
            case BossTypeConst.GEM_BOSS:
                return gemBossMap;
            case BossTypeConst.UNIVERSE_WAR_BOSS:
                return universeBossMap;
            case BossTypeConst.MANOR_WAR_BOSS:
                return manorBossMap;
            case BossTypeConst.HOME_BOSS:
                return Manager.bossManager.getBossHome();
            default:
                logger.debug(String.format("收到未知boss类型{%s}", type));
                return null;
        }
    }

    public void loadBossConfig() {
        manager().loadSpecialMonsterConfig();
        localSoulAnimalManager().load();
        universeScript().load();
    }

    public void calcBossBirth() {
        localSoulAnimalManager().calcAnimalIslandBossBirth();
        int[] arr = {1, 6, 8};
        for (int i : arr) {
            manager().calcBossBirth(i);
        }
    }

    /**
     * 向服务器存boss死亡记录数据
     */
    public void saveBossDieRecord() {
        bossDieRecordDao.delete();
        for (BossDieRecord bossDieRecord : bossDieRecords) {
            BossDieRecordBean bean = bossDieRecordToBean(bossDieRecord);
            bossDieRecordDao.insert(DbSqlName.BOSS_DIE_RECORD_INSERT.getName(), bean);
        }
    }

    private BossDieRecordBean bossDieRecordToBean(BossDieRecord record) {
        BossDieRecordBean bean = new BossDieRecordBean();
        bean.setBossName(record.getBossName());
        bean.setKilledTime(record.getKilledTime());
        bean.setMapName(record.getMapName());
        bean.setPlayerId(record.getPlayerId());
        bean.setxPos(record.getxPos());
        bean.setyPos(record.getyPos());
        bean.setReward(JsonUtils.toJSONString(record.getReward()));
        return bean;
    }

    private BossDieRecord beanToBossDieRecord(BossDieRecordBean bean) {
        BossDieRecord record = new BossDieRecord();
        record.setxPos(bean.getxPos());
        record.setyPos(bean.getyPos());
        record.setMapName(bean.getMapName());
        record.setBossName(bean.getBossName());
        record.setPlayerId(bean.getPlayerId());
        record.setKilledTime(bean.getKilledTime());
        record.setReward(JsonUtils.parseArray(bean.getReward(), Item.class));
        return record;
    }

    public void initBossDieRecord() {
        if (!bossDieRecords.isEmpty()) {
            bossDieRecords.clear();
        }

        List<BossDieRecordBean> beanList = bossDieRecordDao.selectAll();
        BossDieRecord bossDieRecord;
        for (BossDieRecordBean bean : beanList) {
            bossDieRecord = beanToBossDieRecord(bean);
            if (bossDieRecord != null) {
                bossDieRecords.add(bossDieRecord);
            }
        }
        logger.info(bossDieRecords);
    }

    /**
     * 加载世界boss存库数据
     */
    public static void loadBossData() {
        if (!ServerParamUtil.BossDataMap.isEmpty()) {
            //加载后再检测一次
            //增加新的
            boolean isSave = false;
            for (Cfg_Bossnew_world_Bean bean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {
                if (bean.getMapnum() < 0) {
                    continue;
                }
                int bossId = bean.getID();
                if (ServerParamUtil.BossDataMap.containsKey(bossId)) {
                    continue;
                }
                BossData data = new BossData(bean.getID(), 0, 0, bean.getInitial_time(), 0);
                ServerParamUtil.BossDataMap.put(bean.getID(), data);
                isSave = true;
            }

            //删除老的
            BossData data;
            Iterator<Entry<Integer, BossData>> it = ServerParamUtil.BossDataMap.entrySet().iterator();
            while (it.hasNext()) {
                data = it.next().getValue();
                Cfg_Bossnew_world_Bean bean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(data.getBossId());
                if (bean != null && bean.getMapnum() >= 0) {
                    continue;
                }
                it.remove();
                logger.error(data.getBossId() + "已经不在配置表中了！");
                isSave = true;
            }
            if (isSave) {
                ServerParamUtil.saveWorldBoss();
            }
        } else {
            for (Cfg_Bossnew_world_Bean bean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {
                if (bean.getMapnum() < 0) {
                    continue;
                }
                BossData data = new BossData(bean.getID(), 0, 0, bean.getInitial_time(), 0);
                ServerParamUtil.BossDataMap.put(bean.getID(), data);
            }
            ServerParamUtil.saveWorldBoss();
        }
    }

    //0点计算幻境boss重生基数时间(按开服天数提升一定时间)
    public void calcBossRebornBaseTime() {
        try {
            int curDay = TimeUtils.getCurDay(0);
            if (calcDay != curDay) {
                calcDay = curDay;
                manager().calcBossRebornBaseTime();
            }
        } catch (Exception e) {
            logger.error("Error! 0点计算幻境boss重生基数时间出现异常啦！");
        }
    }

    public static ConcurrentHashMap<Integer, Integer> getBloodCrystalMaxNum() {
        return bloodCrystalMaxNum;
    }

    public static ConcurrentHashMap<Integer, Integer> getSoulAnimalForestMonsterMaxNum() {
        return soulAnimalForestMonsterMaxNum;
    }

    public IWorldBossScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BossManagerBaseScript);
        if (is instanceof IWorldBossScript) {
            return (IWorldBossScript) is;
        }
        return null;
    }

    public IMapBaseScript personalBossScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PersonalBossActivityScript);
        if (is instanceof IMapBaseScript) {
            return (IMapBaseScript) is;
        }
        return null;
    }

    public IMapBaseScript bossHomeScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BossHomeScript);
        if (is instanceof IMapBaseScript) {
            return (IMapBaseScript) is;
        }
        return null;
    }

    /**
     * 单服魂兽岛
     */
    public ISoulAnimalForestManager localSoulAnimalManager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.SoulAnimalForestManagerBaseScript);
        if (is instanceof ISoulAnimalForestManager) {
            return (ISoulAnimalForestManager) is;
        }
        return null;
    }

    public IStateBossScript stateBoss() {
        IScript iScript = Manager.scriptManager.GetScriptClass(ScriptEnum.BossStateActivityScript);
        return (IStateBossScript) iScript;
    }


    public void syncSoulAnimalIslandInfo(MapObject map, int bossId, int type) {
        localSoulAnimalManager().syncSoulAnilmasnfo(map, bossId, type);
    }

    /**
     * 太虚战场脚本
     */
    public IUniverseWarScript universeScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.UniverseWarActivityScript);
        if (is instanceof IUniverseWarScript) {
            return (IUniverseWarScript) is;
        }
        return null;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        BossManager manager;

        Singleton() {
            this.manager = new BossManager();
        }

        BossManager getProcessor() {
            return manager;
        }
    }

    public static BossManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

}
