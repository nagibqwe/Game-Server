package common.copyMap.boss;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_Bossnew_world_Bean;
import com.game.backpack.structs.ItemCoinType;
import com.game.boss.struct.Boss;
import com.game.boss.struct.BossTypeConst;
import com.game.copymap.structs.ZoneCache;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.dailyactive.script.IDailyScript;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.drop.structs.SpecialDropDefine;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.monster.manager.MonsterManager;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.BossMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class BossHomeScript implements IDailyScript {

    private final static Logger logger = LogManager.getLogger(BossHomeScript.class);

    @Override
    public int getId() {
        return ScriptEnum.BossHomeScript;
    }

    @Override
    public Object call(Object... objects) {
        String method = (String) objects[0];
        if ("activityEnd".equalsIgnoreCase(method)) {
            MapObject map = (MapObject) objects[1];
            activityEnd(map);
        }
        return null;
    }

    /**
     * boss之家结束处理
     *
     * @param mapObject
     */
    void activityEnd(MapObject mapObject) {
        for (Player player : mapObject.getPlayers().values()) {
            Manager.copyMapManager.manager().onReqCopyMapOut(player);
        }
    }

    /**
     * 地图创建初始化
     *
     * @param mapObject
     * @param objects
     */
    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setAutoRemove(false);

        doRefreshMonster(mapObject, true);

        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 0, 1000);
    }

    /**
     * 怪物出生
     */
    public void doRefreshMonster(MapObject mapObject, boolean create) {

        long curTime = TimeUtils.Time();

        ZoneCache zone = mapObject.getZone();

        for (Cfg_Bossnew_world_Bean bean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {

            if (bean.getClone_map() != mapObject.getZoneModelId()) {
                continue;
            }

            zone.setLevel(bean.getMapnum());

            Boss boss = Manager.bossManager.getBossHome().get(bean.getID());
            if (boss == null) {
                boss = new Boss();
                boss.setConfigId(bean.getID());
                boss.setModelId(bean.getID());
                boss.setMapID(bean.getClone_map());
                boss.setNextTime(curTime);
                Manager.bossManager.getBossHome().put(boss.getModelId(), boss);
            }

            if (create && boss.getNextTime() < 0) {
                refresh(mapObject, boss, bean);
                continue;
            }
            if (boss.getNextTime() < 0) {
                continue;
            }
            if (boss.getNextTime() > curTime) {
                continue;
            }
            boss.setNextTime(-1L);
            boss.setBornTime(curTime);
            refresh(mapObject, boss, bean);
        }
    }


    void refresh(MapObject mapObject, Boss boss, Cfg_Bossnew_world_Bean bean) {
        Monster monster = MonsterManager.getInstance().createMonster(boss.getModelId());
        if (monster == null) {
            logger.error("Home Boss刷新怪物生成失败：monsterId=" + boss.getModelId());
            return;
        }
        monster.setCamp(mapObject.getMapModelId());
        monster.changeLine(mapObject.getLineId());
        monster.changeMapId(mapObject.getId());
        monster.changeMapModelId(mapObject.getMapModelId());
        monster.setInitPos(MapManager.getPos(bean.getPos().get(0), bean.getPos().get(1)));

        Manager.mapManager.manager().onEnterMap(monster);

        Manager.bossManager.manager().syncWorldBossInfo(mapObject, boss.getConfigId(), BossTypeConst.HOME_BOSS);

        Manager.bossManager.manager().sendBossRefreshTip(boss.getConfigId(), BossTypeConst.HOME_BOSS);

//        logger.info("Home 刷新 boss={} ", boss);

    }

    /**
     * 是否满足进入条件
     * <p>
     * 若不满足，实现脚本给出提示或错误日志
     *
     * @param player
     * @param model  副本zoneId
     * @param level
     * @return 是否满足条件
     */
    @Override
    public boolean canEnterMap(Player player, int model, int level) {

        ConcurrentHashMap<Integer, Long> maps = DailyActiveManager.dailyMap.get(DailyActiveDefine.HOME_BOSS.getValue());
        long mapId = maps.getOrDefault(model, 0L);
        MapObject map = Manager.mapManager.getMap(mapId);
        if (map == null) {
            return false;
        }
        ZoneCache zone = map.getZone();
        if (zone.getLevel() <= 0) {
            return true;
        }
        if (player.getVipPearl().getState() <= 0) {
            logger.info("没有升仙令 player={}", player);
            return false;
        }
        int vip = Global.EnterBossHomeVipLevel.get(zone.getLevel() - 1, 0);
        if (player.getVipLv() >= vip) {
            return true;
        }
        //TODO VIP等级不足，消耗元宝进入
        int gold = Global.EnterBossHomeYB.get(zone.getLevel() - 1, 0);
        if (gold <= 0) {
            return true;
        }
        return Manager.backpackManager.manager().onRemoveItem(player, ItemCoinType.GoldCoin, gold, ItemChangeReason.EnterBossHomeDec, IDConfigUtil.getLogId());
    }

    /**
     * 进入副本地图接口
     *  @param player
     * @param map
     * @param login
     */
    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        sendBossInfo(player, map);

        changeCamp(player, map);
    }

    /**
     * 离开副本地图接口
     *
     * @param player
     * @param map
     * @param isQuit
     */
    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {

    }

    /**
     * 伤害接口
     *
     * @param mapObject
     * @param monster
     * @param damage
     * @param attacker
     */
    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

        if (attacker instanceof Player) {

            Player player = (Player) attacker;
            Manager.worldHelpManager.getScript().sendResSynHarmRank(BossTypeConst.WORLD_BOSS, player, monster);

            Manager.bossManager.manager().syncBossDamageRank(monster);
        }
    }

    /**
     * 怪物死亡接口
     *
     * @param map
     * @param monster
     * @param attacker
     */
    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

        Player player = (Player) attacker;
        Boss boss = Manager.bossManager.getBossHome().get(monster.getModelId());
        if (boss == null) {
            return;
        }
        Cfg_Bossnew_world_Bean bean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(boss.getModelId());

        Manager.bossManager.manager().calcRefreshTime(boss);

        Manager.dropManager.deal().specialDropReward(monster, player, SpecialDropDefine.BOSS_HOME, bean.getIf_raward() == 1, -1);

        //记录击杀
        Manager.bossManager.manager().addBossKilledRecord(map, monster, player);

        Manager.bossManager.manager().syncWorldBossInfo(map, boss.getConfigId(), BossTypeConst.HOME_BOSS);

        sendBossInfo(player, map);
    }

    /**
     * 怪物死亡后
     *
     * @param map
     * @param monster
     * @param attacker
     */
    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {


    }

    /**
     * 怪物脱离战斗
     *
     * @param map
     * @param monster
     * @param attacker
     */
    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    /**
     * 玩家死亡接口
     *
     * @param map
     * @param attacker
     * @param player
     */
    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {

    }

    /**
     * 定时执行的函数
     *
     * @param map
     * @param method
     * @param params
     */
    @Override
    public void action(MapObject map, String method, Object[] params) {

        if ("tick".equals(method)) {
            doRefreshMonster(map, false);
        }

    }

    /**
     * 删除地图调用接口
     *
     * @param map
     */
    @Override
    public void removeMap(MapObject map) {

    }


    /**
     * 同步日常首领版面数据
     *
     * @param player
     * @param map
     */
    @Override
    public void sendBossPanel(Player player, MapObject map) {

    }

    /**
     * 同步日常首领数据
     *
     * @param player
     * @param map
     */
    @Override
    public void sendBossInfo(Player player, MapObject map) {
        Manager.bossManager.manager().sendBossInfo(player, Manager.bossManager.getBossHome().values(), map.getZoneModelId(), BossTypeConst.HOME_BOSS);
    }

    /**
     * 刷新阵营
     *
     * @param player
     * @param map
     */
    @Override
    public void changeCamp(Player player, MapObject map) {
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.HOME_BOSS.getValue());
        if (remainCount == 0) {
            player.setCamp(map.getMapModelId(), true);

            if (!Manager.worldHelpManager.getScript().isHelp(player.getId())) {
                BossMessage.ResRankCountTips.Builder msg = BossMessage.ResRankCountTips.newBuilder();
                MessageUtils.send_to_player(player, BossMessage.ResRankCountTips.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        } else {
            player.setCamp(0, true);
        }
    }
}
