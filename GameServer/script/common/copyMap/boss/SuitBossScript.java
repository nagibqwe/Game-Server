package common.copyMap.boss;

import com.data.*;
import com.data.bean.Cfg_Bossnew_world_Bean;
import com.data.bean.Cfg_Daily_Bean;
import com.game.boss.struct.*;
import com.game.chat.structs.Notify;
import com.game.copymap.structs.SuitCopyData;
import com.game.copymap.structs.ZoneCache;
import com.game.count.structs.VariantType;
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
import com.game.welfare.struct.RetrieveType;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.BossMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @author 套装boss || 又名晶甲boss
 */
public class SuitBossScript implements IDailyScript {

    static final Logger logger = LogManager.getLogger(SuitBossScript.class);

    final int ScoreLimitTickOutTime = 30 * 1000;

    final int ScoreLimitTickAddTime = 60 * 1000;

    @Override
    public int getId() {
        return ScriptEnum.SuitBossScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 同步日常首领版面数据
     */
    public void sendBossPanel(Player player, MapObject map) {

        BossMessage.ResSuitGemBossPanel.Builder builder = BossMessage.ResSuitGemBossPanel.newBuilder();
        builder.setType(BossTypeConst.SUIT_BOSS);

        for (Boss boss : Manager.bossManager.getSuitBossMap().values()) {
            int refreshTime = boss.getNextTime() > 0 ? (int) ((boss.getNextTime() - TimeUtils.Time()) / 1000) : 0;
            if (refreshTime < 0) {
                refreshTime = 0;
            }
            BossMessage.BossInfo.Builder bInfo = BossMessage.BossInfo.newBuilder();
            bInfo.setBossId(boss.getConfigId());
            bInfo.setRefreshTime(refreshTime);
            builder.addBossList(bInfo);
        }

        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.SUIT_BOSS.getValue());
        for (int i = 0; i < bean.getCloneID().size(); i++) {
            ArrayList<MapObject> mapObjects = Manager.mapManager.getWorldMaps().get(bean.getCloneID().get(i));
            if (mapObjects == null || mapObjects.size() < 1) {
                continue;
            }
            MapObject mapObject = mapObjects.get(0);
            BossMessage.BossMapOlInfo.Builder olInfo = BossMessage.BossMapOlInfo.newBuilder();
            olInfo.setMapModelId(bean.getCloneID().get(i));
            olInfo.setNum(mapObject.getPlayers().size());
            builder.addMapOlList(olInfo);
        }

        int maxCount = Manager.dailyActiveManager.deal().getDailyMaxCount(player, DailyActiveDefine.SUIT_BOSS.getValue());
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.SUIT_BOSS.getValue());
        int dailyCanBuyCount = Manager.dailyActiveManager.deal().getDailyCanBuyCount(player, DailyActiveDefine.SUIT_BOSS.getValue());
        int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(DailyActiveDefine.SUIT_BOSS.getValue(), 0);
        builder.setMaxCount(maxCount);
        builder.setRemainCount(remainCount);
        builder.setCanBuyCount(dailyCanBuyCount);
        builder.setBuyCount(buyCount);

        MessageUtils.send_to_player(player, BossMessage.ResSuitGemBossPanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 同步日常首领数据
     *
     * @param player
     * @param map
     */
    @Override
    public void sendBossInfo(Player player, MapObject map) {
        Manager.bossManager.manager().sendBossInfo(player, Manager.bossManager.getSuitBossMap().values(), map.getZoneModelId(), BossTypeConst.SUIT_BOSS);
    }

    /**
     * 刷新阵营
     *
     * @param player
     * @param map
     */
    @Override
    public void changeCamp(Player player, MapObject map) {

        player.setCamp(0, true);
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setAutoRemove(false);

        ZoneCache zc = mapObject.getZone();

        SuitCopyData zone = new SuitCopyData();
        zone.setZoneId(zc.getZoneId());
        zone.setLevel(zc.getLevel());
        mapObject.setZone(zone);

        doRefreshMonster(mapObject, true);

        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 0, 2000);

        mapObject.addMapLoopScriptEventTimer(getId(), "tickScore", -1, 0, ScoreLimitTickAddTime);
    }

    /**
     * 怪物出生
     */
    public void doRefreshMonster(MapObject mapObject, boolean create) {

        long curTime = TimeUtils.Time();

        ZoneCache zone = mapObject.getZone();

        for (Cfg_Bossnew_world_Bean bean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {

            if (bean.getClone_map() != mapObject.getMapModelId()) {
                continue;
            }

            zone.setLevel(bean.getMapnum());

            Boss boss = Manager.bossManager.getSuitBossMap().get(bean.getID());
            if (boss == null) {
                boss = new Boss();
                boss.setConfigId(bean.getID());
                boss.setModelId(bean.getID());
                boss.setMapID(bean.getClone_map());
                boss.setNextTime(curTime);
                Manager.bossManager.getSuitBossMap().put(boss.getModelId(), boss);
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

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        //检测 进入道具是否充足

        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.SUIT_BOSS.getValue());
        if (dailyRemainCount == 0) {
            return false;
        }
        int joinCount = player.getDailyActiveData().getDailyProgress().getOrDefault(DailyActiveDefine.SUIT_BOSS.getValue(), 0);

        int needItemCount = Global.World_Boss1_parm6 + Global.World_Boss1_parm7 * joinCount;

        int remainCount = Manager.backpackManager.manager().getItemNum(player, Global.World_Boss1_parm4);
        int needGoldCount = needItemCount - remainCount;

        long logId = IDConfigUtil.getLogId();

        if (needGoldCount > 0) {
            if (!Manager.currencyManager.manager().canDecBindGoldOrGold(player, Global.World_Boss1_parm5 * needGoldCount)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
                return false;
            }
            Manager.backpackManager.manager().onRemoveItem(player, Global.World_Boss1_parm4, remainCount, ItemChangeReason.SuitBossDec, logId);
            Manager.currencyManager.manager().decBindGoldOrGold(player, Global.World_Boss1_parm5 * needGoldCount, ItemChangeReason.SuitBossDec, logId);
        } else {
            Manager.backpackManager.manager().onRemoveItem(player, Global.World_Boss1_parm4, needItemCount, ItemChangeReason.SuitBossDec, logId);
        }
        return true;

    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        SuitCopyData zone = map.getZone();

        if (!login) {
            zone.getTickOut().remove(player.getId());
            zone.getLimit().put(player.getId(), 0);
            Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.SUIT_BOSS, 1);
        }

        sendBossInfo(player, map);

        sendScore(player, map);

        changeCamp(player, map);


        Manager.retrieveResManager.getScript().count(player, RetrieveType.SuitBoss);


    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

        Boss boss = Manager.bossManager.getSuitBossMap().get(monster.getModelId());
        if (boss == null) {
            return;
        }

        if (attacker instanceof Player) {

            Player player = (Player) attacker;
            Manager.worldHelpManager.getScript().sendResSynHarmRank(BossTypeConst.SUIT_BOSS, player, monster);

            Manager.bossManager.manager().syncBossDamageRank(monster);
        }
    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

        SuitCopyData zone = map.getZone();

        Player player = (Player) attacker;

        Boss boss = Manager.bossManager.getSuitBossMap().get(monster.getModelId());
        if (boss == null) {
            return;
        }

        Cfg_Bossnew_world_Bean bean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(boss.getConfigId());

        HashMap<Long, Player> dropRoles = Manager.dropManager.deal().specialDropReward(monster, player, SpecialDropDefine.SuitBoss, false, -1);
        for (Player role : dropRoles.values()) {
            //添加天罚值
            zone.getLimit().put(role.getId(), zone.getLimit().getOrDefault(role.getId(), 0) + bean.getScourge());
            sendScore(role, map);
        }

        if (monster.getMakerId() != 0) {
            logger.info(String.format("地图[%s]中召唤怪[%s]被玩家击杀[%s]", map.getName(), monster.getName(), player.getName()));
            return;
        }

        Manager.bossManager.manager().calcRefreshTime(boss);
        //记录击杀
        Manager.bossManager.manager().addBossKilledRecord(map, monster, player);

        Manager.bossManager.manager().syncWorldBossInfo(map, boss.getConfigId(), BossTypeConst.SUIT_BOSS);

        sendBossInfo(player, map);

    }

    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {

    }

    @Override
    public void action(MapObject map, String method, Object[] params) {
        if ("tick".equals(method)) {
            doRefreshMonster(map, false);
            tickOut(map);
        }
        if ("tickScore".equals(method)) {
            tickScore(map);
        }
    }

    /**
     * 发送天罚值
     *
     * @param player
     * @param map
     */
    void sendScore(Player player, MapObject map) {
        if (player == null) {
            return;
        }
        SuitCopyData zone = map.getZone();
        int score = zone.getLimit().getOrDefault(player.getId(), 0);

        BossMessage.ResSuitGemBossScourge.Builder message = BossMessage.ResSuitGemBossScourge.newBuilder();
        message.setScourge(zone.getLimit().getOrDefault(player.getId(), 0));
        MessageUtils.send_to_player(player, BossMessage.ResSuitGemBossScourge.MsgID.eMsgID_VALUE, message.build().toByteArray());

        if (score < Global.World_Boss1_parm3) {
            return;
        }
        if (zone.getTickOut().containsKey(player.getId())) {
            return;
        }
        zone.getTickOut().put(player.getId(), TimeUtils.Time());

        BossMessage.ResSuitGemBossEndTime.Builder limit = BossMessage.ResSuitGemBossEndTime.newBuilder();
        MessageUtils.send_to_player(player, BossMessage.ResSuitGemBossEndTime.MsgID.eMsgID_VALUE, limit.build().toByteArray());
    }

    /**
     * 定时添加天罚
     *
     * @param map
     */
    void tickScore(MapObject map) {
        SuitCopyData zone = map.getZone();
        Set<Long> roles = zone.getLimit().keySet();
        for (long roleId : roles) {
            zone.getLimit().put(roleId, zone.getLimit().getOrDefault(roleId, 0) + Global.World_Boss1_parm2);
            Player player = map.getPlayers().get(roleId);
            sendScore(player, map);
        }
    }

    /**
     * 检测天罚踢出玩家
     *
     * @param map
     */
    void tickOut(MapObject map) {
        SuitCopyData zone = map.getZone();
        long curTime = TimeUtils.Time();
        for (Map.Entry<Long, Long> entry : zone.getTickOut().entrySet()) {
            if (entry.getValue() + ScoreLimitTickOutTime > curTime) {
                continue;
            }
            Player player = map.getPlayers().get(entry.getKey());
            if (player == null) {
                continue;
            }
            zone.getLimit().remove(entry.getKey());
            zone.getTickOut().remove(entry.getKey());
            Manager.copyMapManager.manager().onReqCopyMapOut(player);
        }
    }

    @Override
    public void removeMap(MapObject map) {

    }
}
