package common.boss;

import com.data.*;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.boss.log.BossDieReliveLog;
import com.game.boss.manager.BossManager;
import com.game.boss.script.IWorldBossScript;
import com.game.boss.struct.Boss;
import com.game.boss.struct.BossData;
import com.game.boss.struct.BossTypeConst;
import com.game.boss.struct.KilledRecord;
import com.game.chat.structs.Notify;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.monster.manager.MonsterManager;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.GlobalType;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import com.game.world_help.struct.SpecialHatred;
import com.game.world_help.struct.WorldHelp;
import com.game.world_help.struct.WorldHelpInfo;
import game.core.dblog.LogService;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.BossMessage;
import game.message.BossMessage.*;
import game.message.CrossServerMessage;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * boss的处理脚本
 */
public class WorldBossManagerScript implements IScript, IWorldBossScript {

    private final static Logger log = LogManager.getLogger(WorldBossManagerScript.class);

    final int CallDistanceLimit = 10; //召唤|刷新 boss 距离
    /**
     * 个人世界boss的地图id
     */
    final int PERSON_WORLD_BOSS_MAP = 1500;
    /**
     * 个人套装boss的地图id
     */
    final int PERSON_EQUIP_BOSS_MAP = 1510;
    /**
     * 个人宝石boss的地图id
     */
    final int PERSON_GEM_BOSS_MAP = 1520;

    @Override
    public int getId() {
        return ScriptEnum.BossManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void loadSpecialMonsterConfig() {
        buildWorldBoss();
    }

    @Override
    public void reloadSpecialMonsterConfig() {
        try {
            //清理旧boss
            cleanOldBoss();
            //装载最新boss
            buildWorldBoss();
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void loadPersonalWorldBoss(Player player) {
        if (!player.getBossDataMap().isEmpty()) {
            //增加新的
            for (Cfg_Bossnew_world_Bean bean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {
                if (bean.getMapnum() != 0) {
                    continue;
                }
                int bossId = bean.getID();
                if (player.getBossDataMap().containsKey(bossId)) {
                    continue;
                }
                BossData data = new BossData(bean.getID(), 0, 0, bean.getInitial_time(), 0);
                player.getBossDataMap().put(bean.getID(), data);
            }

            //删除老的
            BossData data;
            Iterator<Map.Entry<Integer, BossData>> it = player.getBossDataMap().entrySet().iterator();
            while (it.hasNext()) {
                data = it.next().getValue();
                Cfg_Bossnew_world_Bean bean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(data.getBossId());
                if (bean != null && bean.getMapnum() == 0) {
                    continue;
                }
                it.remove();
            }
        } else {
            for (Cfg_Bossnew_world_Bean bean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {
                if (bean.getMapnum() != 0) {
                    continue;
                }
                BossData data = new BossData(bean.getID(), 0, 0, bean.getInitial_time(), 0);
                player.getBossDataMap().put(bean.getID(), data);
            }
        }

        //加载计算boss数据
        ConcurrentHashMap<Integer, Boss> dreamBossMap = player.getPersonWorldBoss();
        if (!dreamBossMap.isEmpty()) {
            dreamBossMap.clear();
        }
        BossData data;
        for (Cfg_Bossnew_world_Bean bean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {
            if (bean.getMapnum() != 0) {
                continue;
            }
            Boss dreamBoss = new Boss();
            dreamBoss.setConfigId(bean.getID());
            dreamBoss.setModelId(bean.getID());
            dreamBoss.setMapID(bean.getClone_map());
            Position pos = MapManager.getPos(bean.getPos().get(0), bean.getPos().get(1));
            dreamBoss.setPos(pos);

            data = player.getBossDataMap().get(bean.getID());
            dreamBoss.setNextTime(data.getRebornTime());

            dreamBossMap.put(bean.getID(), dreamBoss);
        }
    }

    /**
     * 根据bean构建世界boss数据
     */
    private void buildWorldBoss() {
        //先加载boss存库记录的数据，要用于计算重生刷新时间
        BossManager.loadBossData();

        //加载计算boss数据
        ConcurrentHashMap<Integer, Boss> worldBossMap = BossManager.getWorldBossMap();
        ConcurrentHashMap<Integer, Boss> gemBossMap = BossManager.getGemBossMap();
        if (!worldBossMap.isEmpty()) {
            worldBossMap.clear();
        }
        if (!gemBossMap.isEmpty()) {
            gemBossMap.clear();
        }

        BossData data;
        for (Cfg_Bossnew_world_Bean bean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {
            if (bean.getMapnum() < 0) {
                continue;
            }
            data = ServerParamUtil.BossDataMap.get(bean.getID());

            Position pos = MapManager.getPos(bean.getPos().get(0), bean.getPos().get(1));

            Boss boss = new Boss();
            boss.setConfigId(bean.getID());
            boss.setModelId(bean.getID());
            boss.setMapID(bean.getClone_map());
            boss.setPos(pos);
            boss.setNextTime(data.getRebornTime());

            if (bean.getPage() == 1 || bean.getPage() == 2) {
                worldBossMap.put(bean.getID(), boss);
            } else if (bean.getPage() == 4) {
                gemBossMap.put(bean.getID(), boss);
            }

        }
    }

    //清除旧boss，别轻易调用
    private void cleanOldBoss() {
        Iterator<Boss> iter = BossManager.getWorldBossMap().values().iterator();
        Boss boss;
        while (iter.hasNext()) {
            boss = iter.next();
            MapObject map = Manager.mapManager.getMap(boss.getMapID());
            if (map == null) {
                continue;
            }
            Monster monster = map.getMonster(boss.getId());
            if (null == monster) {
                continue;
            }
            log.error(boss.name() + "_boss被清除");
            Manager.mapManager.manager().onQuitMap(map, monster, true);
        }
    }

    @Override
    public boolean canResetBossData(Player player, List<Cfg_Bossnew_world_Bean> beans, boolean all, boolean notify) {
        if (beans == null) {
            beans = new ArrayList<>();
        }

        MapObject ownMap = Manager.mapManager.getMap(player.gainMapId());
        for (Cfg_Bossnew_world_Bean bean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {
            if (bean.getInfinite() > 0 || bean.getMapnum() < 0) {
                continue;
            }
            if (ownMap.getMapModelId() != bean.getClone_map()) {
                continue;
            }
            List<Monster> monsters = Utils.find(ownMap.getMonsters().values(), o -> o.getModelId() == bean.getID());
            Monster one = Utils.findOne(monsters, m -> !m.isCallBoss());
            if (one == null) {
                if (all) {
                    beans.add(bean);
                    continue;
                }
                double distance = Utils.getDistance(new Position(bean.getPos().get(0), bean.getPos().get(1)), player.gainCurPos());
                if (distance <= CallDistanceLimit) {
                    beans.add(bean);
                    break;
                }
            }
        }

        if (notify && beans.isEmpty()) {
            int tips = all ? MessageString.BossItem_Layer_NotHaveBoss : MessageString.BossItem_NotNearBoss;
            MessageUtils.notify_player(player, Notify.NORMAL, tips);
            return false;
        }
        return !beans.isEmpty();
    }

    @Override
    public void resetBossData(Player player, int type, boolean all) {
        List<Cfg_Bossnew_world_Bean> beans = new ArrayList<>();

        boolean canRefresh = canResetBossData(player, beans, all, false);
        if (!canRefresh) {
            return;
        }

        ConcurrentHashMap<Integer, Boss> bossMap = BossManager.getBossMap(type);
        for (Cfg_Bossnew_world_Bean bean : beans) {
            int bossId = bean.getID();
            if (bossMap == null || !bossMap.containsKey(bossId)) {
                log.error(String.format("对应类型[%s]不存在该boss:%s", type, bossId));
                return;
            }
            Boss boss = bossMap.get(bossId);
            MapObject map = getBossMapByType(type, boss.getMapID());
            if (map == null) {
                log.error("找不到boss对应的地图");
                return;
            }
            Monster monster = MonsterManager.getInstance().createMonster(boss.getModelId());
            if (monster != null) {
                monster.changeLine(map.getLineId());
                monster.changeMapId(map.getId());
                monster.changeMapModelId(map.getMapModelId());
                Position position = new Position();
                position.setX(boss.getPos().getX());
                position.setY(boss.getPos().getY());
                monster.setInitPos(position);
                monster.setCamp(map.getMapModelId());
                Manager.mapManager.manager().onEnterMap(monster);
                log.info(String.format("[%s]使用道具：[%s]复活进入地图[%s]:", player.getName(), monster.getName(), map.getName()));

                boss.setNextTime(0L); //刷新后重置下次刷新时间
                boss.setHaveFlush(false);

                //刷新后同步给客户端
                Manager.bossManager.manager().syncWorldBossInfo(map, boss.getConfigId(), type);

                //闪现公告
                if (type == BossTypeConst.WORLD_BOSS) {
                    MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.DreamBossRefresh, boss.name(), Utils.makeUrlStr(MessageString.DreamBossRefresh), map.getNoticeName());
                } else if (type == BossTypeConst.SUIT_BOSS) {
                    MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.jingjiaBossRefresh, boss.name(), Utils.makeUrlStr(MessageString.jingjiaBossRefresh), map.getNoticeName());
                }

//                MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.DreamBossRefresh, boss.getName(), Utils.makeUrlStr(MessageString.DreamBossRefresh), map.getNoticeName());

                BossData data = ServerParamUtil.getBossMap(type).get(boss.getConfigId());
                if (data != null) {
                    data.setBornTime(TimeUtils.Time());
                    data.setRebornTime(0L);
                } else {
                    log.error("Error! 刷新boss时获取其存库数据data失败，不该发生的，bossId=" + boss.getConfigId());
                }
                BossDieReliveLog bossDieLog = new BossDieReliveLog();
                bossDieLog.setBossId(boss.getModelId());
                bossDieLog.setMapId(map.getMapModelId());
                bossDieLog.setType(1);
                LogService.getInstance().execute(bossDieLog);
                log.error("世界BOSS刷新成功，bossId=" + boss.getModelId());
            } else {
                log.error("Boss刷新怪物生成失败：monsterId=" + boss.getModelId() + ", bossId=" + boss.getConfigId());
            }
        }
        ServerParamUtil.saveBoss(type);
    }

    @Override
    public int canCallBoss(Player player, int type) {
        if (type != BossTypeConst.WORLD_BOSS && type != BossTypeConst.SUIT_BOSS) {
            return 0;
        }
        int page = type == BossTypeConst.WORLD_BOSS ? 2 : 3;
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        Cfg_Bossnew_world_Bean bean = null;
        for (Cfg_Bossnew_world_Bean tempBean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {
            if (tempBean.getInfinite() > 0 || tempBean.getMapnum() < 0) {
                continue;
            }
            if (tempBean.getPage() != page) {
                continue;
            }
            if (map.getMapModelId() != tempBean.getClone_map()) {
                continue;
            }
            double distance = Utils.getDistance(new Position(tempBean.getPos().get(0), tempBean.getPos().get(1)), player.gainCurPos());
            if (distance > CallDistanceLimit) {
                continue;
            }
            List<Monster> monsters = Utils.find(map.getMonsters().values(), m -> m.getModelId() == tempBean.getID() && m.isCallBoss());
            if (monsters.size() >= Global.Boss_single_call_limit) {
                MessageUtils.notify_player(player, Notify.NORMAL, MessageString.BossItem_BossCall_Limit);
                return 0;
            }
            bean = tempBean;
            break;
        }
        if (bean == null) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.BossItem_NotNearBoss);
            return 0;
        }
        return bean.getID();
    }

    @Override
    public void callBoss(Player player, int type) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        int bossId = canCallBoss(player, type);
        if (bossId == 0) {
            return;
        }
        Cfg_Bossnew_world_Bean bean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(bossId);
        Monster monster = MonsterManager.getInstance().createMonster(bean.getID());
        if (monster != null) {
            monster.changeLine(map.getLineId());
            monster.changeMapId(map.getId());
            monster.changeMapModelId(map.getMapModelId());
            Position position = new Position();
            position.setX(bean.getPos().get(0));
            position.setY(bean.getPos().get(1));
            monster.setInitPos(position);
            monster.setCamp(map.getMapModelId());
            monster.setMakerId(player.getId());
            monster.setCallBoss(true);
            Manager.mapManager.manager().onEnterMap(monster);

            BossDieReliveLog bossDieLog = new BossDieReliveLog();
            bossDieLog.setBossId(monster.getModelId());
            bossDieLog.setMapId(map.getMapModelId());
            bossDieLog.setType(1);
            LogService.getInstance().execute(bossDieLog);
            log.info(String.format("[%s]使用道具：召唤Boss[%s]进入地图[%s]:", player.getName(), monster.getName(), map.getName()));
        } else {
            log.error(String.format("[%s]召唤boss[%s]失败:", player.getName(), bossId));
        }
    }

    /**
     * 这个方法用于用来计算有哪些boss该刷新了
     */
    @Override
    public void calcBossBirth(int type) {
        long curTime = TimeUtils.Time();

        boolean isSaveData = false;
        ConcurrentHashMap<Integer, Boss> bossMap = BossManager.getBossMap(type);
        if (bossMap == null) {
            return;
        }
        for (Boss dreamBoss : bossMap.values()) {
            if (dreamBoss.getNextTime() <= 0) { //已刷新的
                continue;
            }

            //boss刷新前一分钟通知玩家
            Cfg_Bossnew_world_Bean worldBean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(dreamBoss.getConfigId());
            boolean needNotice = worldBean != null;
            if (curTime < dreamBoss.getNextTime() && !dreamBoss.isHaveFlush()) {
                int betweenTime = (int) ((dreamBoss.getNextTime() - curTime) / 1000); //取秒
                if (betweenTime <= Global.Boss_attent_notice) {
                    sendBossRefreshTip(dreamBoss.getConfigId(), type);
                    dreamBoss.setHaveFlush(true); //借用此字段表示刷新提示已通知过了
                }
                continue;
            }

            if (curTime > dreamBoss.getNextTime()) {
                //获取boss对应的副本进行刷新
                MapObject map = getBossMapByType(type, dreamBoss.getMapID());
                if (map == null) {
                    continue;
                }
                Monster monster = MonsterManager.getInstance().createMonster(dreamBoss.getModelId());
                if (monster != null) {
                    monster.changeLine(map.getLineId());
                    monster.changeMapId(map.getId());
                    monster.changeMapModelId(map.getMapModelId());
                    Position position = new Position();
                    position.setX(dreamBoss.getPos().getX());
                    position.setY(dreamBoss.getPos().getY());
                    monster.setInitPos(position);
                    monster.setCamp(map.getMapModelId());
                    Manager.mapManager.manager().onEnterMap(monster);

                    dreamBoss.setNextTime(0L); //刷新后重置下次刷新时间
                    dreamBoss.setHaveFlush(false);

                    //刷新后同步给客户端
                    Manager.bossManager.manager().syncWorldBossInfo(map, dreamBoss.getConfigId(), type);

                    BossDieReliveLog bossDieLog = new BossDieReliveLog();
                    bossDieLog.setBossId(dreamBoss.getModelId());
                    bossDieLog.setMapId(map.getMapModelId());
                    bossDieLog.setType(1);
                    LogService.getInstance().execute(bossDieLog);
                    log.error("世界BOSS刷新成功，bossId=" + dreamBoss.getModelId());
                    //@todo 修改公告
                    //闪现公告
                    if (needNotice) {
                        if (type == BossTypeConst.WORLD_BOSS) {
                            MessageUtils.notify_allOnlinePlayer(worldBean.getIsNotice(), worldBean.getChatchannel(),MessageString.DreamBossRefresh, dreamBoss.name(), Utils.makeUrlStr(MessageString.DreamBossRefresh), map.getNoticeName());
                        } else if (type == BossTypeConst.SUIT_BOSS) {
                            MessageUtils.notify_allOnlinePlayer(worldBean.getIsNotice(), worldBean.getChatchannel(), MessageString.jingjiaBossRefresh, dreamBoss.name(), Utils.makeUrlStr(MessageString.jingjiaBossRefresh), map.getNoticeName());
                        }
//                        MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.DreamBossRefresh, dreamBoss.getName(), Utils.makeUrlStr(MessageString.DreamBossRefresh), map.getNoticeName());
                    }
                    BossData data = ServerParamUtil.getBossMap(type).get(dreamBoss.getConfigId());
                    if (data != null) {
                        data.setBornTime(TimeUtils.Time());
                        data.setRebornTime(0L);
                        isSaveData = true;
                    } else {
                        log.error("Error! 刷新boss时获取其存库数据data失败，不该发生的，bossId=" + dreamBoss.getConfigId());
                    }
                } else {
                    log.error("Boss刷新怪物生成失败：monsterId=" + dreamBoss.getModelId() + ", bossId=" + dreamBoss.getConfigId());
                }
            }
        }

        if (isSaveData) {
            ServerParamUtil.saveBoss(type);
        }
    }

    @Override
    public void calcPersonWorldBossBirth(Player player) {
        long curTime = TimeUtils.Time();

        ConcurrentHashMap<Integer, Boss> bossMap = player.getPersonWorldBoss();
        if (bossMap == null) {
            return;
        }
        for (Boss dreamBoss : bossMap.values()) {
            if (dreamBoss.getNextTime() <= 0) { //已刷新的
                continue;
            }

            //boss刷新前一分钟通知玩家
            if (curTime < dreamBoss.getNextTime() && !dreamBoss.isHaveFlush()) {
                int betweenTime = (int) ((dreamBoss.getNextTime() - curTime) / 1000); //取秒
                if (betweenTime <= Global.Boss_attent_notice
                        && (player.getFollowedBossList().contains(dreamBoss.getConfigId())
                        || player.getAutoFollowedBossList().contains(dreamBoss.getConfigId()))) {
                    sendBossRefresh(player, dreamBoss.getConfigId(), BossTypeConst.WORLD_BOSS);
                    dreamBoss.setHaveFlush(true); //借用此字段表示刷新提示已通知过了
                }
                continue;
            }

            if (curTime > dreamBoss.getNextTime()) {
                dreamBoss.setNextTime(0);
                //获取boss对应的副本进行刷新
                Cfg_Clone_map_Bean clone_map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(dreamBoss.getMapID());
                if (clone_map_bean == null) {
                    continue;
                }
                //闪现公告
                String clone = ServerStr.getChatTableName(clone_map_bean.getDuplicate_name());
                MessageUtils.notify_player(player, Notify.CHAT_SYS_URL_MARQUEE, MessageString.wuxianBossRefresh, dreamBoss.name(), Utils.makeUrlStr(MessageString.wuxianBossRefresh), clone);

                MapObject map = Manager.mapManager.getMap(player.gainMapId());
                if (map.getMapModelId() != PERSON_WORLD_BOSS_MAP
                        && map.getMapModelId() != PERSON_EQUIP_BOSS_MAP
                        && map.getMapModelId() != PERSON_GEM_BOSS_MAP) {
                    continue;
                }
                //怪物出生
                Monster monster = MonsterManager.getInstance().createMonster(dreamBoss.getModelId());
                if (monster != null) {
                    Cfg_Bossnew_world_Bean bean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(dreamBoss.getModelId());
                    if (bean.getIf_raward() != 0) {
                        monster.setCamp(map.getMapModelId());
                    }
                    monster.changeLine(map.getLineId());
                    monster.changeMapId(map.getId());
                    monster.changeMapModelId(map.getMapModelId());
                    Position position = new Position();
                    position.setX(dreamBoss.getPos().getX());
                    position.setY(dreamBoss.getPos().getY());
                    monster.setInitPos(position);
                    Manager.mapManager.manager().onEnterMap(monster);

                    BossData data = player.getBossDataMap().get(dreamBoss.getConfigId());
                    if (data != null) {
                        data.setBornTime(TimeUtils.Time());
                        data.setRebornTime(0);
                    } else {
                        log.error("Error! 刷新个人世界boss时获取其存库数据data失败，不该发生的，bossId=" + dreamBoss.getConfigId());
                    }
                    BossDieReliveLog bossDieLog = new BossDieReliveLog();
                    bossDieLog.setBossId(dreamBoss.getModelId());
                    bossDieLog.setMapId(map.getMapModelId());
                    bossDieLog.setType(1);
                    LogService.getInstance().execute(bossDieLog);
                    log.error("世界BOSS刷新成功，bossId=" + map.getMapModelId());
                } else {
                    log.error("个人世界Boss刷新怪物生成失败：monsterId=" + dreamBoss.getModelId());
                }
            }
        }
    }

    /**
     * 根据类型获取boss所在的地图
     *
     * @param type       bossType
     * @param mapModelId modelId
     * @return
     */
    private MapObject getBossMapByType(int type, int mapModelId) {
        Map<Integer, Long> maps = null;
        switch (type) {
            //世界boss直接返回世界地图
            case BossTypeConst.SUIT_BOSS:
            case BossTypeConst.WORLD_BOSS:
                if (!Manager.mapManager.getWorldMaps().containsKey(mapModelId)) {
                    return null;
                }
                long mapId = Manager.mapManager.getWorldMaps().get(mapModelId).get(0).getId();
                return Manager.mapManager.getMap(mapId);
            case BossTypeConst.HOME_BOSS:
                maps = DailyActiveManager.dailyMap.get(DailyActiveDefine.HOME_BOSS.getValue());
                break;
            case BossTypeConst.SOULANIMAL_BOSS:
                maps = DailyActiveManager.dailyMap.get(DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue());
                break;
            case BossTypeConst.GEM_BOSS:
                maps = DailyActiveManager.dailyMap.get(DailyActiveDefine.GemBoss.getValue());
                break;
        }
        if (maps == null || maps.get(mapModelId) == null) {
            return null;
        }
        return Manager.mapManager.getMap(maps.get(mapModelId));
    }

    /**
     * @param bossId
     * @param type
     */
    @Override
    public void sendBossRefreshTip(int bossId, int type) {
        for (Player p : Manager.playerManager.getPlayersCache().values()) {
            if (!p.isOnline()) {
                continue;
            }
            if (!Manager.controlManager.deal().isOpenFunction(p, FunctionStart.Boss)) {
                continue;
            }
            if (p.getFollowedBossList().contains(bossId) || p.getAutoFollowedBossList().contains(bossId)) {
                sendBossRefresh(p, bossId, type);
            }
        }
    }

    //世界boss刷新或死亡后的同步
    @Override
    public void syncWorldBossInfo(MapObject mapObject, int bossId, int type) {
        if (mapObject.getPlayers().isEmpty()) {
            return;
        }
        Boss boss = BossManager.getBossMap(type).get(bossId);
        if (boss == null) {
            return;
        }

        BossInfo.Builder bInfo = BossInfo.newBuilder();
        bInfo.setBossId(bossId);
        int refreshTime = Math.max(0, (int) ((boss.getNextTime() - TimeUtils.Time()) / 1000));
        bInfo.setRefreshTime(refreshTime);

        ResBossRefreshInfo.Builder resMsg = ResBossRefreshInfo.newBuilder();
        resMsg.addBossRefreshList(bInfo);
        resMsg.setBossType(type);
        MessageUtils.send_to_players(mapObject.getPlayers().values(), ResBossRefreshInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray(), mapObject.getId());
    }

    @Override
    public void reqOpenDreamBoss(Player player, ReqOpenDreamBoss messInfo) {
        int type = messInfo.getBossType();
        ResOpenDreamBoss.Builder resMsg = ResOpenDreamBoss.newBuilder();
        int refreshTime;
        ConcurrentHashMap<Integer, Boss> bossList = BossManager.getBossMap(type);
        if (bossList == null) {
            return;
        }

        List<Boss> bossMerge = new ArrayList<>(bossList.values());
        if (type == BossTypeConst.WORLD_BOSS) {
            //无限层和世界boss在一个分页
            bossMerge.addAll(player.getUnLimitBoss().values());
            //新手层和世界boss在一个分页
            bossMerge.addAll(Manager.bossManager.getNoobBossList().values());
        }

        for (Boss dreamBoss : bossMerge) {
            BossInfo.Builder bInfo = BossInfo.newBuilder();
            bInfo.setBossId(dreamBoss.getConfigId());

            refreshTime = dreamBoss.getNextTime() > 0 ? (int) ((dreamBoss.getNextTime() - TimeUtils.Time()) / 1000) : 0;
            if (refreshTime < 0) {
                refreshTime = 0;
            }
            bInfo.setRefreshTime(refreshTime);

            if (player.getFollowedBossList().contains(dreamBoss.getConfigId())
                    || player.getAutoFollowedBossList().contains(dreamBoss.getConfigId())) {
                bInfo.setIsFollowed(true);

                //重启服了的话的处理
                List<Long> followedPlayerList = BossManager.getBossFollowedMap().get(dreamBoss.getConfigId());
                if (followedPlayerList == null) {
                    followedPlayerList = new ArrayList<>();
                    BossManager.getBossFollowedMap().put(dreamBoss.getConfigId(), followedPlayerList);
                }
                if (!followedPlayerList.contains(player.getId())) {
                    followedPlayerList.add(player.getId());
                }
            } else {
                bInfo.setIsFollowed(false);
            }

            resMsg.addBossList(bInfo);
        }

        int dailyId = getSpecialType(type);
        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, dailyId);
        int dailyMaxCount = Manager.dailyActiveManager.deal().getDailyMaxCount(player, dailyId);
        int dailyCanBuyCount = Manager.dailyActiveManager.deal().getDailyCanBuyCount(player, dailyId);
        int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(dailyId, 0);
        resMsg.setRemainCount(dailyRemainCount);
        resMsg.setMaxCount(dailyMaxCount);
        resMsg.setCanBuyCount(dailyCanBuyCount);
        resMsg.setBuyCount(buyCount);
        resMsg.setBossType(type);

        if (type == BossTypeConst.WORLD_BOSS) {
            Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.WORLD_BOSS.getValue());
            for (int i = 0; i < bean.getCloneID().size(); i++) {
                ArrayList<MapObject> mapObjects = Manager.mapManager.getWorldMaps().get(bean.getCloneID().get(i));
                if (mapObjects == null || mapObjects.get(0) == null) {
                    log.error("找不到世界boss地图对象!!");
                    continue;
                }
                BossMapOlInfo.Builder olInfo = BossMapOlInfo.newBuilder();
                olInfo.setMapModelId(bean.getCloneID().get(i));
                olInfo.setNum(mapObjects.get(0).getPlayers().size());
                resMsg.addMapOlList(olInfo);
            }

        } else if (type == BossTypeConst.HOME_BOSS) {
            ConcurrentHashMap<Integer, Long> bossMap = DailyActiveManager.dailyMap.get(DailyActiveDefine.HOME_BOSS.getValue());
            for (Map.Entry<Integer, Long> entry : bossMap.entrySet()) {
                MapObject mapObject = Manager.mapManager.getMap(entry.getValue());
                if (mapObject == null) {
                    continue;
                }
                BossMapOlInfo.Builder olInfo = BossMapOlInfo.newBuilder();
                olInfo.setMapModelId(entry.getKey());
                olInfo.setNum(mapObject.getPlayers().size());
                resMsg.addMapOlList(olInfo);
            }
        }
        MessageUtils.send_to_player(player, ResOpenDreamBoss.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());

        ResWuXianBossOCTime.Builder builder = ResWuXianBossOCTime.newBuilder();
        int todayBeginTime = (int) (TimeUtils.getTodayBeginTime() / 1000);
        int nowSec = (int) (TimeUtils.Time() / 1000) - todayBeginTime;
        boolean isOpen = nowSec < Global.Boss_Unlimit_Open_Time.get(0) * 60 || nowSec >= Global.Boss_Unlimit_Open_Time.get(1) * 60;
        int openTime = isOpen ? 0 : Global.Boss_Unlimit_Open_Time.get(1) * 60 - nowSec;
        if (openTime < 0) {
            openTime += 1440 * 60;
        }
        builder.setOpenTime(openTime);
        builder.setCloseTime(isOpen ? Global.Boss_Unlimit_Open_Time.get(1) * 60 - nowSec : 0);
        MessageUtils.send_to_player(player, ResWuXianBossOCTime.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 获取特殊掉落的DailyId
     */
    public int getSpecialType(int type) {
        switch (type) {
            case BossTypeConst.WORLD_BOSS:
                return DailyActiveDefine.WORLD_BOSS.getValue();
            case BossTypeConst.HOME_BOSS:
                return DailyActiveDefine.HOME_BOSS.getValue();
            case BossTypeConst.SUIT_BOSS:
                return DailyActiveDefine.SUIT_BOSS.getValue();
            case BossTypeConst.SOULANIMAL_BOSS:
                return DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue();
            default:
                log.error("未知类型的boss，无对应活动的dailyId");
                return 0;
        }
    }

    @Override
    public void reqBossKilledInfo(Player player, int bossId, int bossType) {
        ResBossKilledInfo.Builder resMsg = ResBossKilledInfo.newBuilder();
        resMsg.setBossId(bossId);
        resMsg.setBossType(bossType);
        List<KilledRecord> killedList = BossManager.getBossKilledRecordMap().get(bossId);
        if (killedList != null && !killedList.isEmpty()) {
            for (KilledRecord re : killedList) {
                BossKilledRecord.Builder record = BossKilledRecord.newBuilder();
                record.setKillTime(re.getKilledTime());
                record.setKiller(Manager.registerManager.getRoleName(re.getKillerId()));
                resMsg.addKilledRecordList(record);
            }
        }
        MessageUtils.send_to_player(player, ResBossKilledInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    @Override
    public void reqFollowBoss(Player player, int bossId, int type, int bossType) {
        Boss dreamBoss = BossManager.getBossMap(bossType).get(bossId);

        if (dreamBoss == null) {
            log.error("玩家" + player.getId() + "请求关注世界boss时获取boss信息失败！bossId=" + bossId);
            return;
        }

        ResFollowBoss.Builder resMsg = ResFollowBoss.newBuilder();
        resMsg.setBossId(bossId);
        resMsg.setType(type);
        resMsg.setBossType(bossType);
        resMsg.setIsSuccess(true);
        if (type == 1) { //请求关注
            if (!player.getFollowedBossList().contains(bossId)) {
                player.getFollowedBossList().add(bossId);
            }

            List<Long> followedPlayerList = BossManager.getBossFollowedMap().get(bossId);
            if (followedPlayerList == null) {
                followedPlayerList = new ArrayList<>();
                BossManager.getBossFollowedMap().put(bossId, followedPlayerList);
            }
            if (!followedPlayerList.contains(player.getId())) {
                followedPlayerList.add(player.getId());
            }
        } else if (type == 2) { //请求取消关注
            if (player.getFollowedBossList().contains(bossId) || player.getAutoFollowedBossList().contains(bossId)) {
                player.getAutoFollowedBossList().remove((Integer) bossId);
                player.getFollowedBossList().remove((Integer) bossId); //remove时int类型会被当做索引调用的remove(int index)，封装下就好，这样就调用remove(Object o)
            }

            List<Long> followedPlayerList = BossManager.getBossFollowedMap().get(bossId);
            if (followedPlayerList != null) {
                followedPlayerList.remove(player.getId());
            }
        } else {
            resMsg.setIsSuccess(false);
        }
        MessageUtils.send_to_player(player, ResFollowBoss.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    @Override
    public void addBossKilledRecord(MapObject map, Monster boss, Player killer) {

        List<SpecialHatred> hatreds = Manager.worldHelpManager.getScript().getSpecialHatredRes(boss);
        if (hatreds.isEmpty()){
            return;
        }
        //TODO 新需求 击杀者改成伤害最大者
        killer = hatreds.get(0).getPlayer();

        KilledRecord re = new KilledRecord((int) (TimeUtils.Time() / 1000), killer.getId());
        List<KilledRecord> killedList = BossManager.getBossKilledRecordMap().get(boss.getModelId());
        if (killedList == null) {
            killedList = new ArrayList<>();
            BossManager.getBossKilledRecordMap().put(boss.getModelId(), killedList);
        }
        killedList.add(re);
        //最多记录10条
        if (killedList.size() > 10) {
            killedList.remove(0);
        }
        BossDieReliveLog bossDieLog = new BossDieReliveLog();
        bossDieLog.setBossId(boss.getModelId());
        bossDieLog.setMapId(map.getMapModelId());
        bossDieLog.setType(0);
        bossDieLog.setParam(re.getKillerId());
        LogService.getInstance().execute(bossDieLog);
    }

    @Override
    public void calcBossRebornBaseTime() {
        int curOpenServerDay = TimeUtils.getOpenServerDay();
        Cfg_Bossnew_world_Bean bean;
        boolean isSaveData = false;
        for (BossData data : ServerParamUtil.BossDataMap.values()) {
            bean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(data.getBossId());
            if (bean == null) {
                log.error("获取世界boss数据失败，bossId=" + data.getBossId());
                continue;
            }

            for (ReadArray<Integer> increaseArr : bean.getIncrease_time().getValuees()) {
                if (increaseArr.get(0) != curOpenServerDay) {
                    continue;
                }
                data.setReBornBaseTime(data.getReBornBaseTime() + increaseArr.get(1));

                ReadArray<Integer> limitArr = getLimitTime(curOpenServerDay, bean.getLimit_time());
                if (data.getReBornBaseTime() > limitArr.get(2)) { //加了大于上限值则取上限值
                    data.setReBornBaseTime(limitArr.get(2));
                }
                isSaveData = true;
                break;
            }
        }

        if (isSaveData) {
            ServerParamUtil.saveWorldBoss();
        }
    }

    /**
     * 刷新boss
     *
     * @param boss
     */
    @Override
    public void calcRefreshTime(Boss boss) {
        Cfg_Bossnew_world_Bean bean = CfgManager.getCfg_Bossnew_world_Container().getValueByKey(boss.getModelId());
        int rebornTime = bean.getInitial_time();

        int lifeTime = (int) ((TimeUtils.Time() - boss.getBornTime()) / 1000L); //生命值相比于配置的标准值，重生时间做上下浮动
        if (lifeTime > bean.getStandard_time()) {
            rebornTime = rebornTime + bean.getFloat_time();
        } else if (lifeTime < bean.getStandard_time()) {
            rebornTime = rebornTime - bean.getFloat_time();
        } //"=="的情况不加减浮动值

        int curOpenServerDay = TimeUtils.getOpenServerDay();
        ReadArray<Integer> arr = Manager.bossManager.manager().getLimitTime(curOpenServerDay, bean.getLimit_time());
        if (arr != null) {
            if (rebornTime < arr.get(1)) { //低于下限了取下限值
                rebornTime = arr.get(1);
            } else if (rebornTime > arr.get(2)) { //高于上限了取上线值
                rebornTime = arr.get(2);
            }
        }
        boss.setNextTime(TimeUtils.Time() + 1000L * rebornTime);
    }


    @Override
    public ReadArray<Integer> getLimitTime(int openDay, ReadIntegerArrayEs arrlist) {
        ReadArray<Integer> limitArr = null;
        int tempDay = 0;
        for (ReadArray<Integer> arr : arrlist.getValuees()) {
            if (arr.get(0) > tempDay && arr.get(0) <= openDay) {
                tempDay = arr.get(0);
                limitArr = arr;
            }
        }
        if (limitArr == null) {
            int len = arrlist.size();
            if (len > 0) {
                return arrlist.get(len - 1);
            }
            log.error("配置表错误，世界boss对应开服天数刷新时间上下限不存在");
        }
        return limitArr;
    }

    @Override
    public void buyRankCount(Player player, int type) {

        DailyActiveDefine daily = DailyActiveDefine.findByBoss(type);
        if (daily == null) {
            return;
        }

        int canBuyCount = Manager.dailyActiveManager.deal().getDailyCanBuyCount(player, daily.getValue());
        if (canBuyCount <= 0) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.BOSSFIGHT_TISHENGVIPADDNUM);
            return;
        }
        int vipCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(daily.getValue(), 0);
        int needGold = Manager.dailyActiveManager.deal().getDailyBuyNeedGold(daily.getValue(), vipCount + 1);

        if (!Manager.currencyManager.manager().decBindGoldOrGold(player, needGold, ItemChangeReason.BuyBossRankDec, IDConfigUtil.getLogId())) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Not_Enough_Gold);
            return;
        }
        player.getDailyActiveData().getDailyBuyCount().put(daily.getValue(), vipCount + 1);

        //资源找回记录购买册数
        Manager.retrieveResManager.getScript().addVipBuyCount(player, daily.getValue(), 1);

        dealBossDailyCountAdd(player, daily.getValue());
    }

    /**
     * 处理日常次数增加
     *
     * @param player
     * @param dailyId
     */
    public void dealBossDailyCountAdd(Player player, int dailyId) {

        DailyActiveDefine daily = DailyActiveDefine.find(dailyId);
        if (daily == null) {
            return;
        }
        //更新玩家状态
        if (Manager.dailyActiveManager.deal().checkInBossMap(player, dailyId)) {
            Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(player.getCurGps().getModelId());
            int camp = 0;
            if (mapBean != null && mapBean.getScene_came_match_type() == 1) {
                camp = (player.getCamp() & 1) > 0 ? player.getCamp() - 1 : player.getCamp();
            }
            player.setCamp(camp, true);
        }


        if (GameServer.getInstance().IsFightServer()) {
            synDropDataFromFightToGame(player, daily.getValue());
        } else {
            int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, dailyId);
            int dailyMaxCount = Manager.dailyActiveManager.deal().getDailyMaxCount(player, dailyId);
            int dailyCanBuyCount = Manager.dailyActiveManager.deal().getDailyCanBuyCount(player, dailyId);
            int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(dailyId, 0);

            //向客户端同步消息
            ResAddWorldBossRankCount.Builder msg = ResAddWorldBossRankCount.newBuilder();
            msg.setBossType(daily.getBossType());
            msg.setRemainCount(dailyRemainCount);
            msg.setMaxCount(dailyMaxCount);
            msg.setCanBuyCount(dailyCanBuyCount);
            msg.setBuyCount(buyCount);
            MessageUtils.send_to_player(player, ResAddWorldBossRankCount.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void synDropDataFromFightToGame(Player player, int type) {
        CrossServerMessage.F2GDropData.Builder builder = CrossServerMessage.F2GDropData.newBuilder();
        builder.setRoleId(player.getId());
        int dropCount = player.getDailyActiveData().getDailyProgress().getOrDefault(type, 0);
        int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(type, 0);
        builder.setBuyCount(buyCount);
        builder.setJoinDropCount(dropCount);
        builder.setKillDropCount(dropCount);
        builder.setRankDropCount(dropCount);
        builder.setTime(0);
        builder.setType(type);
        FightClientManager.GetInstance().send_to_game(player.getIosession(), CrossServerMessage.F2GDropData.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void autoFollowBoss(Player player) {
        int newLevel = player.getLevel();
        boolean needAutoFollow = false;
        for (int i = 0; i < Global.Boss_attent_notice_level.size(); i++) {
            if (newLevel == Global.Boss_attent_notice_level.get(i)) {
                needAutoFollow = true;
                break;
            }
        }
        if (!needAutoFollow) {
            return;
        }
        //清理上一级自动关注的世界boss
        player.getAutoFollowedBossList().clear();
        //关注新boss
        int maxLevel = Integer.MIN_VALUE;
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        for (Cfg_Bossnew_world_Bean bean : CfgManager.getCfg_Bossnew_world_Container().getValuees()) {
            if (bean.getMapnum() < 0) {
                continue;
            }
            Cfg_Monster_Bean monsterBean = CfgManager.getCfg_Monster_Container().getValueByKey(bean.getID());
            if (monsterBean == null) {
                continue;
            }
            int monsterLevel = getMonsterLevel(monsterBean.getLevel(), newLevel);
            if (monsterLevel > newLevel) {
                continue;
            }
            if (monsterLevel > maxLevel) {
                maxLevel = monsterLevel;
            }
            List<Integer> list = map.get(monsterLevel);
            if (list == null) {
                list = new ArrayList<>();
                map.put(monsterLevel, list);
            }
            list.add(bean.getID());
        }
        if (map.get(maxLevel) == null) {
            return;
        }
        player.setAutoFollowedBossList(map.get(maxLevel));
    }

    /**
     * 获取怪物等级
     */
    private int getMonsterLevel(int level, int newLevel) {
        if (level > 0) {
            return level;
        } else if (level == 0) {
            return GlobalType.getWorldLevel();
        } else {
            return newLevel;
        }
    }

    /**
     * 同步boss掉落归属排名
     */
    @Override
    public void syncBossDamageRank(Monster monster) {
        if (monster.isCallBoss()) {
            return;
        }
        int top = 0;
        boolean isChange = false;
        List<SpecialHatred> list = Manager.worldHelpManager.getScript().getSpecialHatredRes(monster);
        for (SpecialHatred sh : list) {
            ++top;
            if (monster.getDropRoleIds().size() >= top) {
                if (monster.getDropRoleIds().get(top - 1) != sh.getPlayer().getId()) {
                    monster.getDropRoleIds().remove(top - 1);
                    monster.getDropRoleIds().add(top - 1, sh.getPlayer().getId());
                    isChange = true;
                }
            } else {
                monster.getDropRoleIds().add(sh.getPlayer().getId());
                isChange = true;
            }
            if (top == 3) break;
        }

        if (isChange) {
            MapMessage.ResMonsterDropMark.Builder msg1 = MapMessage.ResMonsterDropMark.newBuilder();
            msg1.setMonsterId(monster.getId());
            msg1.addAllDropUserIds(monster.getDropRoleIds());
            MessageUtils.send_to_roundPlayer(monster, MapMessage.ResMonsterDropMark.MsgID.eMsgID_VALUE, msg1.build().toByteArray());
        }
    }

    /**
     * 发送boss列表
     *
     * @param player
     * @param bossList
     * @param cloneId
     * @param type
     */
    @Override
    public void sendBossInfo(Player player, Collection<Boss> bossList, int cloneId, int type) {
        List<Player> players = new ArrayList<>();
        players.add(player);
        sendBossInfo(players, bossList, cloneId, type);
    }

    @Override
    public void sendBossInfo(Collection<Player> players, Collection<Boss> bossList, int cloneId, int type) {
        //推送当前副本地图世界boss刷新信息
        ResBossRefreshInfo.Builder resMsg = ResBossRefreshInfo.newBuilder();
        for (Boss boss : bossList) {
            if (boss.getMapID() != cloneId) {
                continue;
            }
            int refreshTime = boss.getNextTime() > 0 ? (int) ((boss.getNextTime() - TimeUtils.Time()) / 1000) : 0;
            if (refreshTime < 0) {
                refreshTime = 0;
            }
            BossInfo.Builder bInfo = BossInfo.newBuilder();
            bInfo.setBossId(boss.getConfigId());
            bInfo.setRefreshTime(refreshTime);
            resMsg.addBossRefreshList(bInfo);
        }
        resMsg.setBossType(type);
        MessageUtils.send_to_players(players, ResBossRefreshInfo.MsgID.eMsgID_VALUE, resMsg.build().toByteArray(), cloneId);
    }

    /**
     * 通知boss 刷新
     *
     * @param player
     * @param bossId
     * @param type
     */
    @Override
    public void sendBossRefresh(Player player, int bossId, int type) {
        BossMessage.ResBossRefreshTip.Builder resMsg = BossMessage.ResBossRefreshTip.newBuilder();
        resMsg.setBossId(bossId);
        resMsg.setBossType(type);
        MessageUtils.send_to_player(player, BossMessage.ResBossRefreshTip.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    /**
     * 发送首领 daily 次数
     *
     * @param player
     * @param daily
     */
    @Override
    public void sendDailyCount(Player player, DailyActiveDefine daily) {
        if (daily.getBossType() <= 0) {
            return;
        }
        BossMessage.ResUpDateWorldBossReMainRankCount.Builder updateMsg = BossMessage.ResUpDateWorldBossReMainRankCount.newBuilder();
        updateMsg.setBossType(daily.getBossType());
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, daily.getValue());
        int dailyMaxCount = Manager.dailyActiveManager.deal().getDailyMaxCount(player, daily.getValue());
        int dailyCanBuyCount = Manager.dailyActiveManager.deal().getDailyCanBuyCount(player, daily.getValue());
        int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(daily.getValue(), 0);
        updateMsg.setRemainCount(remainCount);
        updateMsg.setMaxCount(dailyMaxCount);
        updateMsg.setCanBuyCount(dailyCanBuyCount);
        updateMsg.setBuyCount(buyCount);
        MessageUtils.send_to_player(player, BossMessage.ResUpDateWorldBossReMainRankCount.MsgID.eMsgID_VALUE, updateMsg.build().toByteArray());
    }

    /**
     * 获取新手层 boss 多人副本
     *
     * @param modelId
     * @return
     */
    @Override
    public MapObject getNoodBoss(int modelId) {
        if (Manager.bossManager.getBossZone().containsKey(modelId)) {
            long mapId = Manager.bossManager.getBossZone().get(modelId);
            MapObject map = Manager.mapManager.getMap(mapId);
            if (map != null) {
                return map;
            }
        }
        MapObject map = Manager.mapManager.createCopyMap(modelId, 1, modelId);

        Manager.bossManager.getBossZone().put(modelId, map.getId());

        return map;
    }
}
