package common.copyMap.boss;

import com.data.CfgManager;
import com.data.Global;
import com.data.bean.Cfg_Bossnew_world_Bean;
import com.data.struct.ReadArray;
import com.game.boss.manager.BossManager;
import com.game.boss.struct.*;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.dailyactive.script.IDailyScript;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.drop.structs.SpecialDropDefine;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.structs.Monster;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.Hatred;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.welfare.struct.RetrieveType;
import game.core.map.Position;
import game.core.util.TimeUtils;
import game.message.BossMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gsj
 */
public class GemBossScript implements IDailyScript {

    private static final Logger log = LogManager.getLogger(GemBossScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GemBossScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setAutoRemove(false);
        for (Boss boss : BossManager.getGemBossMap().values()) {
            if (boss.getMapID() != mapObject.getMapModelId() || mapObject.getLineId() != 0) {
                continue;
            }
            if (boss.getNextTime() > 0) {
                Manager.mapManager.createTombstone(mapObject, boss.getModelId());
                continue;
            }

            Monster monster = Manager.monsterManager.createMonster(mapObject, new Position(boss.getPos().getX(), boss.getPos().getY()), boss.getModelId());
            if (monster != null) {
                BossData data = ServerParamUtil.BossDataMap.get(boss.getConfigId());
                if (data != null) {
                    data.setBornTime(TimeUtils.Time());
                    data.setRebornTime(boss.getNextTime());
                } else {
                    log.error("Error! 刷新宝石boss时获取其存库数据data失败，不该发生的，bossId=" + boss.getConfigId());
                }
            } else {
                log.error("宝石Boss刷新怪物生成失败：monsterId=" + boss.getModelId());
            }
        }
        ServerParamUtil.saveWorldBoss();

        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 0, 1000);
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.GemBoss.getValue());
        return dailyRemainCount != 0;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        ConcurrentHashMap<Long, SuitGemBossInfo> data = MapParam.getSuitGemBossMapData(map);
        SuitGemBossInfo info = data.get(player.getId());
        if (info == null) {
            info = new SuitGemBossInfo(player.getId(), (int) (TimeUtils.Time() / 1000));
            data.put(player.getId(), info);

            Manager.retrieveResManager.getScript().count(player, RetrieveType.GemBoss);
            Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.GemBoss, 1);
        } else {
            if (info.isEnd()) {
                doKickPlayer(map, player);
                return;
            }
        }

        //天罚值
        BossMessage.ResSuitGemBossScourge.Builder builder = BossMessage.ResSuitGemBossScourge.newBuilder();
        builder.setScourge(info.getScourge() + info.getStayScourge());
        MessageUtils.send_to_player(player, BossMessage.ResSuitGemBossScourge.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        sendBossInfo(player, map);
    }

    private void getBossInfo(MapObject map, Player player, List<BossMessage.BossInfo> bossInfos) {
        int refreshTime;
        for (Boss boss : BossManager.getGemBossMap().values()) {
            if (map != null && boss.getMapID() != map.getMapModelId()) {
                continue;
            }
            BossMessage.BossInfo.Builder bInfo = BossMessage.BossInfo.newBuilder();
            bInfo.setBossId(boss.getConfigId());

            refreshTime = boss.getNextTime() > 0 ? (int) ((boss.getNextTime() - TimeUtils.Time()) / 1000) : 0;
            bInfo.setRefreshTime(refreshTime);

            if (player.getFollowedBossList().contains(boss.getConfigId())
                    || player.getAutoFollowedBossList().contains(boss.getConfigId())) {
                bInfo.setIsFollowed(true);

                //重启服了的话的处理
                List<Long> followedPlayerList = BossManager.getBossFollowedMap().get(boss.getConfigId());
                if (followedPlayerList == null) {
                    followedPlayerList = new ArrayList<>();
                    BossManager.getBossFollowedMap().put(boss.getConfigId(), followedPlayerList);
                }
                if (!followedPlayerList.contains(player.getId())) {
                    followedPlayerList.add(player.getId());
                }
            } else {
                bInfo.setIsFollowed(false);
            }
            bossInfos.add(bInfo.build());
        }
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        if (isQuit) {
            ConcurrentHashMap<Long, SuitGemBossInfo> data = MapParam.getSuitGemBossMapData(map);
            data.remove(player.getId());
        }
    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {
        if (!(attacker instanceof Player)) {
            return;
        }
        Player player = (Player) attacker;
        Manager.worldHelpManager.getScript().sendResSynHarmRank(BossTypeConst.SUIT_BOSS, player, monster);
        Manager.bossManager.manager().syncBossDamageRank(monster);
    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {
        int bossId = monster.getModelId();
        Player player = null;
        if (attacker instanceof Player) {
            player = (Player) attacker;
        } else if (attacker instanceof Pet) {
            player = Manager.playerManager.getPlayerOnline(((Pet) attacker).getOwnerId());
        }
        if (player == null) {
            return;
        }
        Cfg_Bossnew_world_Bean bean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(bossId);
        if (bean == null) {
            return;
        }


        ConcurrentHashMap<Long, SuitGemBossInfo> mapData = MapParam.getSuitGemBossMapData(map);
        SuitGemBossInfo info = mapData.get(player.getId());
        if (info != null && !info.isEnd()) {
            Manager.dropManager.deal().specialDropReward(monster, player, SpecialDropDefine.GemBoss, bean.getIf_raward() == 1, -1);
        }

        for (Hatred hatred : monster.getHatreds()) {
            if (hatred.getHatred() <= 0) {
                continue;
            }
            if (!(hatred.getTarget() instanceof Player)) {
                continue;
            }
            Player p1 = (Player) hatred.getTarget();
            if (!map.getPlayers().containsKey(p1.getId())) {
                continue;
            }
            addScourge(map, p1, bean.getScourge());
        }

        //计算下次刷新时间
        Boss boss = BossManager.getGemBossMap().get(bossId);
        if (boss == null) {
            log.error("宝石boss死亡后缓存里找不到了，monsterId=" + monster.getModelId() + ", bossId=" + bossId);
            return;
        }

        BossData data = ServerParamUtil.BossDataMap.get(bossId);
        data.setDieNum(data.getDieNum() + 1);
        int rebornTime;
        //非首次死亡的计算
        if (data.getDieNum() > 1) {
            rebornTime = data.getReBornBaseTime();

            //生命值相比于配置的标准值，重生时间做上下浮动
            int lifeTime = (int) ((TimeUtils.Time() - data.getBornTime()) / 1000);
            if (lifeTime > bean.getStandard_time()) {
                rebornTime = rebornTime + bean.getFloat_time();
            } else if (lifeTime < bean.getStandard_time()) {
                rebornTime = rebornTime - bean.getFloat_time();
            }

            int curOpenServerDay = TimeUtils.getOpenServerDay();
            ReadArray<Integer> arr = Manager.bossManager.manager().getLimitTime(curOpenServerDay, bean.getLimit_time());
            if (rebornTime < arr.get(1)) {
                rebornTime = arr.get(1);
            } else if (rebornTime > arr.get(2)) {
                rebornTime = arr.get(2);
            }
        } else {
            //首次死亡，重生时间按表里配置的初始值来
            rebornTime = bean.getInitial_time();
        }

        boss.setNextTime(TimeUtils.Time() + 1000 * rebornTime);

        data.setBornTime(0L);
        data.setReBornBaseTime(rebornTime);
        data.setRebornTime(boss.getNextTime());
        ServerParamUtil.saveWorldBoss();
        //记录击杀
        Manager.bossManager.manager().addBossKilledRecord(map, monster, player);

        Manager.bossManager.manager().syncWorldBossInfo(map, monster.getModelId(), BossTypeConst.GEM_BOSS);
    }

    /**
     * 增加天罚值
     */
    private void addScourge(MapObject mapObject, Player player, int scourge) {
        if (scourge < 0) {
            return;
        }
        ConcurrentHashMap<Long, SuitGemBossInfo> data = MapParam.getSuitGemBossMapData(mapObject);
        SuitGemBossInfo info = data.get(player.getId());
        info.setScourge(info.getScourge() + scourge);

        BossMessage.ResSuitGemBossScourge.Builder builder = BossMessage.ResSuitGemBossScourge.newBuilder();
        builder.setScourge(Integer.min(info.getScourge() + info.getStayScourge(), Global.World_Boss2_parm3));
        MessageUtils.send_to_player(player, BossMessage.ResSuitGemBossScourge.MsgID.eMsgID_VALUE, builder.build().toByteArray());
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
        switch (method) {
            case "tick":
                tick(map);
                break;
            case "doKickPlayer":
                MapObject mapObject = (MapObject) params[0];
                Player player = (Player) params[1];
                doKickPlayer(mapObject, player);
                break;
            default:
                break;
        }
    }

    /**
     * 每秒检查地图内玩家天罚值
     */
    private void tick(MapObject map) {
        int now = (int) (TimeUtils.Time() / 1000);
        Iterator<SuitGemBossInfo> iterator = MapParam.getSuitGemBossMapData(map).values().iterator();
        while (iterator.hasNext()) {
            SuitGemBossInfo info = iterator.next();
            if (info.isEnd()) {
                continue;
            }
            Player player = Manager.playerManager.getPlayerCache(info.getPlayerId());
            if (player == null) {
                iterator.remove();
            }
            int stayScourge = (now - info.getEnterTime()) / 60 * Global.World_Boss2_parm2;
            if (stayScourge > info.getStayScourge()) {
                info.setStayScourge(stayScourge);
                addScourge(map, player, 0);
            }
            if (info.getScourge() + info.getStayScourge() >= 100) {
                info.setEnd(true);
                BossMessage.ResSuitGemBossEndTime.Builder builder = BossMessage.ResSuitGemBossEndTime.newBuilder();
                MessageUtils.send_to_player(player, BossMessage.ResSuitGemBossEndTime.MsgID.eMsgID_VALUE, builder.build().toByteArray());

                map.addMapOnceScriptEventTimer(getId(), "doKickPlayer", 30000, map, player);
            }
        }
    }

    /**
     * 倒计时结束退出地图通知
     */
    private void doKickPlayer(MapObject mapObject, Player player) {
        if (mapObject.getPlayers().containsKey(player.getId())) {
            MapObject oldMap = Manager.mapManager.getMap(player.getOld().getMapId());
            Manager.mapManager.changeMap(player, oldMap.getId(), player.getOld().getPos(), false);
        }
    }

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
        BossMessage.ResSuitGemBossPanel.Builder builder = BossMessage.ResSuitGemBossPanel.newBuilder();
        List<BossMessage.BossInfo> bossInfos = new ArrayList<>();
        getBossInfo(null, player, bossInfos);
        builder.setType(BossTypeConst.GEM_BOSS);
        builder.addAllBossList(bossInfos);

        ConcurrentHashMap<Integer, Long> bossMap = DailyActiveManager.dailyMap.get(DailyActiveDefine.GemBoss.getValue());
        for (Map.Entry<Integer, Long> entry : bossMap.entrySet()) {
            MapObject mapObject = Manager.mapManager.getMap(entry.getValue());
            if (mapObject == null) {
                continue;
            }
            BossMessage.BossMapOlInfo.Builder olInfo = BossMessage.BossMapOlInfo.newBuilder();
            olInfo.setMapModelId(entry.getKey());
            olInfo.setNum(mapObject.getPlayers().size());
            builder.addMapOlList(olInfo);
        }
        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.GemBoss.getValue());
        int dailyMaxCount = Manager.dailyActiveManager.deal().getDailyMaxCount(player, DailyActiveDefine.GemBoss.getValue());
        int dailyCanBuyCount = Manager.dailyActiveManager.deal().getDailyCanBuyCount(player, DailyActiveDefine.GemBoss.getValue());
        int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(DailyActiveDefine.GemBoss.getValue(), 0);

        builder.setMaxCount(dailyMaxCount);
        builder.setRemainCount(dailyRemainCount);
        builder.setBuyCount(buyCount);
        builder.setCanBuyCount(dailyCanBuyCount);
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
        Manager.bossManager.manager().sendBossInfo(player, BossManager.getGemBossMap().values(), map.getZoneModelId(), BossTypeConst.GEM_BOSS);
    }

    /**
     * 刷新阵营
     *
     * @param player
     * @param map
     */
    @Override
    public void changeCamp(Player player, MapObject map) {

    }
}
