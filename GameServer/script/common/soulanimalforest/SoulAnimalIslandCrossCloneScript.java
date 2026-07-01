package common.soulanimalforest;

import com.data.CfgManager;
import com.data.Global;
import com.data.MessageString;
import com.data.bean.Cfg_Bossnew_SoulBeasts_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Gather_Bean;
import com.data.container.Cfg_Bossnew_SoulBeasts_Container;
import com.data.container.Cfg_Clone_map_Container;
import com.data.struct.ReadArray;
import com.game.boss.manager.BossManager;
import com.game.boss.struct.BossTypeConst;
import com.game.chat.structs.Notify;
import com.game.copymap.scripts.ICopyGatherScript;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.drop.structs.SpecialDropDefine;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.manager.MonsterManager;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.soulanimalforest.manager.SoulAnimalForestCrossManager;
import com.game.soulanimalforest.script.ISoulAnimalIslandClone;
import com.game.soulanimalforest.structs.CrossBossData;
import com.game.soulanimalforest.structs.CrossGroupBossData;
import com.game.soulanimalforest.structs.SoulLandType;
import com.game.structs.Fighter;
import com.game.structs.Gather;
import com.game.structs.Hatred;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import game.core.map.Position;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.CrossServerMessage;
import game.message.CrossServerMessage.F2GCloneCDRecordAdd;
import game.message.SoulAnimalForestMessage;
import game.message.SoulAnimalForestMessage.F2PReqCloneMonsterDie;
import game.message.SoulAnimalForestMessage.F2PSoulAnimalCloneOpen;
import game.message.SoulAnimalForestMessage.ResSoulAnimalForestCrossRefreshInfo;
import io.netty.util.internal.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 魂兽森林跨服副本
 */
public class SoulAnimalIslandCrossCloneScript implements IMapBaseScript, ICopyGatherScript, ISoulAnimalIslandClone {

    //TODO boss 召唤卷 和刷新卷 距离
    final int BossCallAndRefreshDistance = 10;

    private final static Logger log = LogManager.getLogger(SoulAnimalIslandCrossCloneScript.class);

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        int zoneModelId = mapObject.getZoneModelId();
        Cfg_Clone_map_Bean clone_mapBean = Cfg_Clone_map_Container.GetInstance().getValueByKey(zoneModelId);
        if (clone_mapBean == null) {
            log.error("跨服魂兽森林boss副本配置表中不存在：" + zoneModelId);
            return;
        }
        mapObject.setAutoRemove(false);
        List<CommonMessage.CrossAttribute> createParams = (List<CommonMessage.CrossAttribute>) objects[1];
        for (CommonMessage.CrossAttribute ca : createParams) {
            if (ca.getType() == 100000) {
                //跨服副本
                MapParam.getSoulAnimalIsland(mapObject).setCrossServerGroupId(ca.getParam1());
            }
        }
        //加入计时器
        int tickTime = 60;
        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 0, tickTime * 1000);
        onCreateMonster(mapObject);
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue());
        if (remainCount == 0) {
            player.setCamp(map.getMapModelId(), true);
            MessageUtils.notify_player(player, Notify.SHOWBOX, MessageString.DREAMBOSSTIMEOVERSERROR);
        } else {
            player.setCamp(0, true);
        }
        syncCloneInfo(player, map);
        map.getPlayers().put(player.getId(), player);
        //增加跨服副本次数
        Manager.copyMapManager.manager().sendF2GCloneEnterAddOne(map, player,0l);
    }


    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        Manager.bossManager.manager().synDropDataFromFightToGame(player, DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue());
    }


    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {
        if (!(attacker instanceof Player)) {
            return;
        }
        Player player = (Player) attacker;
        Manager.worldHelpManager.getScript().sendResSynHarmRank(BossTypeConst.SOULANIMAL_BOSS, player, monster);
        Manager.bossManager.manager().syncBossDamageRank(monster);
    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {
        if (!(attacker instanceof Player)) {
            return;
        }
        String posKeys = SoulAnimalIslandManagerScript.getIdPosMap(map).get(monster.getId());
        if (!StringUtil.isNullOrEmpty(posKeys)) {
            SoulAnimalIslandManagerScript.getPosHasValueMap(map).remove(posKeys);//怪物死亡， 释放可用坐标
        }
        //计算下次刷新时间
        int modelId = monster.getModelId();
        Integer configId = MapParam.getSoulAnimalIsland(map).getBossConfigIds().get(modelId);

        if (configId == null) {
            //检查守卫是否已经死完
            checkMonsterJinYinAllDle(map, monster.getModelId());
            return;
        }
        Player player = (Player) attacker;
        Manager.dropManager.deal().specialDropReward(monster, player, SpecialDropDefine.SOULANIMALISLAND_BOSS, true, -1);
        Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(configId);
        if (bean == null) {
            log.error("魂兽boss死亡后获取策划数据失败，bossId=" + configId);
            return;
        }

        CrossGroupBossData cgbd = getGroupBossData(map);
        if (cgbd == null) {
            log.error("魂兽森林boss死亡后获取策划数据失败，bossId=" + configId);
            return;
        }
        CrossBossData data = cgbd.getSoulAnimalDataMap().get(configId);
        data.setDieNum(data.getDieNum() + 1);
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue());
        if (remainCount == 0) {
            player.setCamp(map.getMapModelId(), true);
            MessageUtils.notify_player(player, Notify.SHOWBOX, MessageString.DREAMBOSSTIMEOVERSERROR);
        }

        for (Hatred hatred : monster.getHatreds()) {
            if (hatred.getHatred() <= 0) {
                continue;
            }
            if (!(hatred.getTarget() instanceof Player)) {
                continue;
            }
            Player p1 = (Player) hatred.getTarget();
            Manager.bossManager.manager().synDropDataFromFightToGame(p1, DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue());
        }

        if (monster.getMakerId() != 0) {
            log.info(String.format("地图[%s]中召唤怪[%s]被玩家击杀[%s]", map.getName(), monster.getName(), player.getName()));
            return;
        }

        int rebornTime;
        if (data.getDieNum() > 1) { //非首次死亡的计算
            rebornTime = data.getRebornTime();
            int lifeTime = (int) ((TimeUtils.Time() - data.getBornTime()) / 1000); //生命值相比于配置的标准值，重生时间做上下浮动
            if (lifeTime > bean.getStandard_time()) {
                rebornTime = rebornTime + bean.getFloat_time();
            } else if (lifeTime < bean.getStandard_time()) {
                rebornTime = rebornTime - bean.getFloat_time();
            }

            int curOpenServerDay = TimeUtils.getOpenServerDay();
            ReadArray<Integer> arr = Manager.bossManager.manager().getLimitTime(curOpenServerDay, bean.getLimit_time());
            if (arr != null) {
                if (rebornTime < arr.get(1)) { //低于下限了取下限值
                    rebornTime = arr.get(1);
                } else if (rebornTime > arr.get(2)) { //高于上限了取上线值
                    rebornTime = arr.get(2);
                }
            } else {
                log.error(String.format("id{%s}在表Cfg_Bossnew_SoulBeasts_Bean中Limit_time不存在!!!", bean.getID()));
            }
        } else { //首次死亡，重生时间按表里配置的初始值来
            rebornTime = bean.getInitial_time();
        }
        data.setRebornTime(rebornTime);
        data.setNextTime(TimeUtils.Time() + 1000L * rebornTime);
        Manager.bossManager.manager().addBossKilledRecord(map, monster, player);
        //发消息给服务器
        String killName = "";
        if ( monster.getHatreds().size()>0){
            Player p1 = (Player) monster.getHatreds().get(0).getTarget();
            killName =  p1.getName();
        }
        syncMonsterDie(map, killName, configId, TimeUtils.Time() + rebornTime * 1000, rebornTime, 4);

        Manager.copyMapManager.manager().sendF2GCloneEnterAddOne(map, player,1l);
    }

    /**
     * 获取分组的怪物ID值
     *
     * @return
     */
    private CrossGroupBossData getGroupBossData(MapObject mapObject) {
        int groupId = MapParam.getSoulAnimalIsland(mapObject).getCrossServerGroupId();
        return SoulAnimalForestCrossManager.getGroupBossData(groupId);
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
    public void action(MapObject mapObject, String method, Object[] params) {
        switch (method) {
            case "nextAfterBeastSpiritCrystal": {
                nextAfterBeastSpiritCrystal(mapObject, (int) params[0]);
            }
            break;
            case "nextAfterBeastlyBloodCrystal": {
                nextAfterBeastlyBloodCrystal(mapObject, (int) params[0]);
            }
            break;
            case "nextAfterMonsterJinYin": {
                nextAfterMonsterJinYin(mapObject, (int) params[0]);
            }
            break;
            case "tick":
                tick(mapObject);
                break;
        }
    }

    public void tick(Object arg) {

        MapObject map = (MapObject) (arg);
        if (map == null) {
            return;
        }

        if (map.getPlayers().size() < 1) {
            return;
        }
        Set<Player> moverplayers = new HashSet<>();
        if (map.getPlayers().size() == 0) {
            return;
        }
        //给地图所有玩家加上怒气值
        for (Player player : map.getPlayers().values()) {
            if (player.isDie()) {
                continue;
            }
            if (player.getIsOnline() == 0) {
                moverplayers.add(player);
                continue;
            }

            if (player.getIosession() == null) {
                moverplayers.add(player);
                continue;
            }
        }

        for (Player player : moverplayers) {
            Manager.mapManager.manager().onQuitMap(map, player, true);
            log.info(map.getName() + " 玩家" + player.nameIdString() + "的服务器断开");
        }
    }


    @Override
    public void removeMap(MapObject map) {
    }


    @Override
    public int getId() {
        return ScriptEnum.SoulAnimalForestCrossCloneCrossActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean onBeginGather(Player player, Gather gather) {
        int configId = gather.getNo();
        Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(configId);
        if (bean == null) {
            return false;
        }
        long count = Manager.countManager.getCount(player, BaseCountType.SOULANIMALFORESTGATHERNUM, bean.getType());
        int max = Global.BossOld2_DailyGather1OpenTimes;
        if (bean.getType() == SoulLandType.AnimalGem) {
            max = Global.BossOld2_DailyGather2OpenTimes;
        }
        if (count < max) {
            Manager.buffManager.deal().onAddBuff(player, player, SoulAnimalIslandManagerScript.getGatherBuffId(player, gather));
            return true;
        } else {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.SOULCLONEGATHERMAXNUM, ServerStr.getChatTableName(gather.getName()));
            return false;
        }
    }

    @Override
    public void onGather(Player player, Gather gather) {
        onOutGather(player, gather);
        MapObject map = Manager.mapManager.getMap(gather.gainMapId());
        if (map == null) {
            return;
        }

        CrossGroupBossData cgbd = getGroupBossData(map);
        if (cgbd == null) {
            log.error(map.getName() + "魂兽森林的基本数据不见了，mapId=" + map.getId());
            return;
        }
        String posKeys = SoulAnimalIslandManagerScript.getIdPosMap(map).get(gather.getId());
        if (!StringUtil.isNullOrEmpty(posKeys)) {
            SoulAnimalIslandManagerScript.getPosHasValueMap(map).remove(posKeys);//怪物死亡， 释放可用坐标

        }

        Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(gather.getModelId());
        if (null == gatherCfg) {
            log.error("配置的没有，怎么初始化的！");
            return;
        }
        int configId = gather.getNo();
        Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(configId);
        if (bean == null) {
            return;
        }
        int max = Global.BossOld2_DailyGather1OpenTimes;
        if (bean.getType() == SoulLandType.AnimalGem) {
            max = Global.BossOld2_DailyGather2OpenTimes;
        }
        //兽血水晶不记录次数
        if (bean.getType() == SoulLandType.AnimalGem || bean.getType() == SoulLandType.AnimalSmall) {
            //检查地图是否还有此水晶
            int num = 0;
            for (Gather has : map.getCollects().values()) {
                if (has.getModelId() == gather.getModelId()) {
                    num = 1;
                    break;
                }
            }
            //记录玩家一次采集次数
            Manager.countManager.addCount(player, BaseCountType.SOULANIMALFORESTGATHERNUM, bean.getType(), Count.RefreshType.CountType_Day, 1);
            //通知游戏服，需要加上统计次数
            F2GCloneCDRecordAdd.Builder msg = F2GCloneCDRecordAdd.newBuilder();
            msg.setCdTimes(1);
            msg.setCdType(Count.RefreshType.CountType_Day.getValue());
            msg.setCdHour(Count.RefreshType.CountType_Day.getHour());
            msg.setCdkey(BaseCountType.SOULANIMALFORESTGATHERNUM.getValue());
            msg.setDefinekey(bean.getType());
            msg.setFightId(map.getId());
            msg.addRoleIds(player.getId());
            FightClientManager.GetInstance().send_to_game(player.getIosession(), F2GCloneCDRecordAdd.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            long count = Manager.countManager.getCount(player, BaseCountType.SOULANIMALFORESTGATHERNUM, bean.getType());
            int lastTime = max - (int) count;
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GatherResiduedegree, lastTime + "");

            CrossBossData data = cgbd.getBeastlyBloodCrystalBirthTime().get(map.getZoneModelId());

            //计算下一次出现的时间
            if (num < 1) {
                int rebornTime = data.getRebornTime();
                int lifeTime = (int) ((TimeUtils.Time() - data.getBornTime()) / 1000); //生命值相比于配置的标准值，重生时间做上下浮动
                if (lifeTime > bean.getStandard_time()) {
                    rebornTime = rebornTime + bean.getFloat_time();
                } else if (lifeTime < bean.getStandard_time()) {
                    rebornTime = rebornTime - bean.getFloat_time();
                }

                int curOpenServerDay = TimeUtils.getOpenServerDay();
                ReadArray<Integer> arr = Manager.bossManager.manager().getLimitTime(curOpenServerDay, bean.getLimit_time());
                if (arr != null) {
                    if (rebornTime < arr.get(1)) { //低于下限了取下限值
                        rebornTime = arr.get(1);
                    } else if (rebornTime > arr.get(2)) { //高于上限了取上线值
                        rebornTime = arr.get(2);
                    }
                } else {
                    log.error(" 采集失败!!!配置为空");
                }
                //发消息给服务器
                syncMonsterDie(map, "", bean.getID(), TimeUtils.Time() + rebornTime * 1000, rebornTime, bean.getType());
                log.error(map.getName() + "兽血水晶已经完了， 下一次的时间为:" + rebornTime);
            } else {
                syncMonsterDie(map, "", bean.getID(), 0, 0, bean.getType());
            }
            return;
        }
        Manager.countManager.addCount(player, BaseCountType.SOULANIMALFORESTGATHERNUM, bean.getType(), Count.RefreshType.CountType_Day, 1);//
        //通知游戏服，需要加上统计次数
        F2GCloneCDRecordAdd.Builder msg = F2GCloneCDRecordAdd.newBuilder();
        msg.setCdTimes(1);
        msg.setCdType(Count.RefreshType.CountType_Day.getValue());
        msg.setCdHour(Count.RefreshType.CountType_Day.getHour());
        msg.setCdkey(BaseCountType.SOULANIMALFORESTGATHERNUM.getValue());
        msg.setDefinekey(bean.getType());
        msg.setFightId(map.getId());
        msg.addRoleIds(player.getId());
        FightClientManager.GetInstance().send_to_game(player.getIosession(), F2GCloneCDRecordAdd.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        long count = Manager.countManager.getCount(player, BaseCountType.SOULANIMALFORESTGATHERNUM, bean.getType());
        int lastTime = max - (int) count;
        MessageUtils.notify_player(player, Notify.ERROR, MessageString.GatherResiduedegree, lastTime + "");
        syncMonsterDie(map, "", bean.getID(), 0, 0, 1);
    }


    @Override
    public void onOutGather(Player player, Gather gather) {
        int gatherBuffId = SoulAnimalIslandManagerScript.getGatherBuffId(player, gather);
        Manager.buffManager.deal().onRemoveBuff(player, gatherBuffId);
    }

    private void syncCloneInfo(Player p, MapObject mapObject) {
        CrossGroupBossData cgbd = getGroupBossData(mapObject);
        if (cgbd == null) {
            log.error(mapObject.getName() + "魂兽森林的基本数据不见了，groupId=" + MapParam.getSoulAnimalIsland(mapObject).getCrossServerGroupId());
            return;
        }
        ResSoulAnimalForestCrossRefreshInfo.Builder msg = ResSoulAnimalForestCrossRefreshInfo.newBuilder();
        CrossBossData bd = cgbd.getCrystalTime().get(mapObject.getZoneModelId());
        int shengId = bd.getMaxNum() - bd.getDieNum();

        SoulAnimalForestMessage.forestBossInfo.Builder crystalInfo = SoulAnimalForestMessage.forestBossInfo.newBuilder();
        crystalInfo.setBossId(bd.getModelConfigId());
        crystalInfo.setIsFollowed(false);
        crystalInfo.setNum(shengId);
        crystalInfo.setRefreshTime((int) ((bd.getNextTime() - TimeUtils.Time()) / 1000));
        crystalInfo.setType(1);
        msg.addBossRefreshList(crystalInfo);

        bd = cgbd.getBeastlyBloodCrystalBirthTime().get(mapObject.getZoneModelId());
        SoulAnimalForestMessage.forestBossInfo.Builder shouXueInfo = SoulAnimalForestMessage.forestBossInfo.newBuilder();
        shouXueInfo.setBossId(bd.getModelConfigId());
        shouXueInfo.setIsFollowed(false);
        shouXueInfo.setNum(bd.getMaxNum() - bd.getDieNum());
        shouXueInfo.setRefreshTime((int) ((bd.getNextTime() - TimeUtils.Time()) / 1000));
        shouXueInfo.setType(2);
        msg.addBossRefreshList(shouXueInfo);

        for (CrossBossData boss : cgbd.getSoulAnimalDataMap().values()) {
            Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(boss.getModelConfigId());
            if (bean == null) {
                continue;
            }
            if (bean.getCloneid() != mapObject.getZoneModelId()) {
                continue;
            }

            SoulAnimalForestMessage.forestBossInfo.Builder bInfo = SoulAnimalForestMessage.forestBossInfo.newBuilder();
            bInfo.setBossId(boss.getModelConfigId());
            int refreshTime = boss.getNextTime() > 0 ? (int) ((boss.getNextTime() - TimeUtils.Time()) / 1000) : 0;
            if (refreshTime < 0) {
                refreshTime = 0;
            }
            bInfo.setRefreshTime(refreshTime);
            bInfo.setIsFollowed(false);
            bInfo.setNum(1);
            bInfo.setType(bean.getType());
            msg.addBossRefreshList(bInfo);
        }
        if (p != null) {
            MessageUtils.send_to_player(p, ResSoulAnimalForestCrossRefreshInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        } else {
            MessageUtils.send_to_map(mapObject, ResSoulAnimalForestCrossRefreshInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private int inMapNum(MapObject map, int monsterId, int type) {
        int haveNum = 0;
        if (type == SoulLandType.AnimalSmall) {
            for (Gather gather : map.getCollects().values()) {
                if (gather.getModelId() == monsterId) {
                    haveNum += 1;
                }
            }
        } else {
            for (Monster monster : map.getMonsters().values()) {
                if (monster.getModelId() == monsterId) {
                    haveNum += 1;
                }
            }
        }
        return haveNum;
    }

    private void updateMonster(MapObject mapObject, ConcurrentHashMap<Integer, Integer> cristal, int type, int dieNum) {
        int endNum = dieNum;
        if (endNum < 1) {
            return;
        }
        Cfg_Bossnew_SoulBeasts_Bean bean = null;
        for (int configId : cristal.keySet()) {
            if (endNum < 1) {
                break;
            }
            bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(configId);
            if (bean == null) {
                log.error(mapObject.getName() + " 找不到配置的水晶配置项:" + configId);
                continue;
            }

            int criModelId = bean.getMonsterid();
            List<Position> poslist = new ArrayList<>();
            ReadArray<Integer>[] posArr = bean.getPos().getValuees();
            for (ReadArray<Integer> aii : posArr) {
                if (SoulAnimalIslandManagerScript.getPosHasValueMap(mapObject).containsKey(aii.get(0) + "_" + aii.get(1))) {
                    continue;
                }
                poslist.add(MapManager.getPos(aii.get(0), aii.get(1)));
            }
            int needNum = bean.getNum() - inMapNum(mapObject, criModelId, type);
            if (needNum > endNum) {
                needNum = endNum;
            }
            endNum -= needNum;
            for (int k = 0; k < needNum; ++k) {
                Position pos = poslist.get(0);
                int randIndex = -1;
                if (poslist.size() > 1) {
                    randIndex = RandomUtils.random(poslist.size());
                    pos = poslist.get(randIndex);
                    poslist.remove(randIndex);
                }
                if (type == SoulLandType.AnimalSmall || type == SoulLandType.AnimalGem) {
                    Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(criModelId);
                    if (null == gatherCfg) {
                        log.error("Cfg_Gather_Bean无法找到指定数据，神兽岛生成采集物失败，id = " + criModelId);
                        continue;
                    }
                    Gather gather = Manager.gatherManager.deal().createGather(mapObject, gatherCfg, pos);
                    if (gather == null) {
                        log.error(mapObject.getName() + " 创建采集物时出错了", new NullPointerException());
                        if (randIndex >= 0) {
                            poslist.add(pos);
                        }
                        continue;
                    }
                    //记录下来这个坐标位置上有物品 采集物id
                    SoulAnimalIslandManagerScript.getIdPosMap(mapObject).put(gather.getId(), pos.getX() + "_" + pos.getY());
                    gather.setNo(bean.getID());//设置关联的表数据
                    SoulAnimalIslandManagerScript.getPosHasValueMap(mapObject).put(pos.getX() + "_" + pos.getY(), 1);
                    log.error(mapObject.getName() + " 更新在" + pos.getX() + "_" + pos.getY() + "创建采集物:" + gather.getName() + "(" + String.valueOf(gather.getId()) + ")");
                } else {
                    Monster monster = Manager.monsterManager.createMonster(mapObject, pos, criModelId);
                    if (monster == null) {
                        log.error(mapObject.getName() + " 创建怪物时出错了", new NullPointerException());
                        if (randIndex >= 0) {
                            poslist.add(pos);
                        }
                        continue;
                    }
                    //记录下来这个坐标位置上有物品
                    SoulAnimalIslandManagerScript.getIdPosMap(mapObject).put(monster.getId(), pos.getX() + "_" + pos.getY());
                    SoulAnimalIslandManagerScript.getPosHasValueMap(mapObject).put(pos.getX() + "_" + pos.getY(), 1);
                }
            }
        }
    }


    private void onCreateMonster(MapObject map) {
        CrossGroupBossData cgbd = getGroupBossData(map);
        int crossServerGroupId = MapParam.getSoulAnimalIsland(map).getCrossServerGroupId();
        if (cgbd == null) {
            log.error(map.getName() + "魂兽森林的基本数据不见了，groupId=" + crossServerGroupId);
            return;
        }
        Cfg_Bossnew_SoulBeasts_Bean bean = null;
        int zoneModelId = map.getZoneModelId();
        List<Integer> bossIds = new ArrayList<>();
        for (CrossBossData boss : cgbd.getSoulAnimalDataMap().values()) {
            bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(boss.getModelConfigId());
            if (bean == null) {
                log.error(map.getName() + " 找不到配置的BOSS配置项:" + boss.getModelConfigId());
                continue;
            }

            if (bean.getCloneid() != map.getZoneModelId()) {
                continue;
            }
            boss.setMapUid(map.getId());
            bossIds.add(bean.getID());
            //记录当前BOSS及当前的记录ID值
            MapParam.getSoulAnimalIsland(map).getBossConfigIds().put(bean.getMonsterid(), bean.getID());
            if (boss.getNextTime() > 0) {
                Manager.mapManager.createTombstone(map, bean.getMonsterid());
                continue;
            }
            Monster monster = MonsterManager.getInstance().createMonster(bean.getMonsterid());
            if (monster != null) {
                monster.changeLine(map.getLineId());
                monster.changeMapId(map.getId());
                monster.changeMapModelId(map.getMapModelId());
                Position position = new Position();
                position.setX(bean.getPos().get(0).get(0));
                position.setY(bean.getPos().get(0).get(1));
                monster.setInitPos(position);
                monster.setCamp(map.getMapModelId());
                Manager.mapManager.manager().onEnterMap(monster);
                log.error(map.getName() + " 在" + position.getX() + "_" + position.getY() + "创建BOSS:" + monster.getName());
            } else {
                log.error("魂兽Boss刷新怪物生成失败：monsterId=" + bean.getMonsterid());
            }
        }
        //TODO  创建水晶
        ConcurrentHashMap<Integer, Integer> cristal = cgbd.getBeastSpiritCrystalMap().get(zoneModelId);
        if (cristal != null) {
            BossManager.getInstance().localSoulAnimalManager().createCrossMonster(map, cristal, SoulLandType.AnimalSmall, cgbd.getCrystalTime().get(zoneModelId));
        }
        //TODO 创建兽血水晶
        cristal = cgbd.getBeastlyBloodCrystalMap().get(zoneModelId);
        if (cristal != null) {
            BossManager.getInstance().localSoulAnimalManager().createCrossMonster(map, cristal, SoulLandType.AnimalSmall, cgbd.getBeastlyBloodCrystalBirthTime().get(zoneModelId));
        }
        //TODO 创建守卫
        cristal = cgbd.getSoulAnimalForestMonsterMap().get(zoneModelId);
        CrossBossData bossdata = cgbd.getSoulAnimalForestMonsterTime().get(zoneModelId);
        if (cristal != null && bossdata != null) {
            BossManager.getInstance().localSoulAnimalManager().createCrossMonster(map, cristal, SoulLandType.AnimalGem, bossdata);
        }
        //TODO 报告公共服， 我的数据要提交了
        F2PSoulAnimalCloneOpen.Builder msg = F2PSoulAnimalCloneOpen.newBuilder();
        msg.setCloneModelId(map.getZoneModelId());
        msg.addAllBossIds(bossIds);
        msg.setFightRoomId(map.getId());
        msg.setGroupId(crossServerGroupId);
        MessageUtils.send_to_public(F2PSoulAnimalCloneOpen.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    public void birthTime(int type, MapObject mapObject, long dieNum) {
        //分别计算下一次更新的时间
        //其中需要检查当前是否还有些种类型的怪物
        switch (type) {
            case SoulLandType.AnimalSmall: {
                mapObject.addMapOnceScriptEventTimer(getId(), "nextAfterBeastSpiritCrystal", 0, (int) dieNum);
            }
            break;
            case SoulLandType.AnimalGem: {
                mapObject.addMapOnceScriptEventTimer(getId(), "nextAfterBeastlyBloodCrystal", 0, (int) dieNum);
            }
            break;
            case SoulLandType.AnimalBest: {
                mapObject.addMapOnceScriptEventTimer(getId(), "nextAfterMonsterJinYin", 0, (int) dieNum);
            }
            case SoulLandType.AnimalBoss: {
                mapObject.addMapOnceScriptEventTimer(getId(), "nextAfterMonsterJinYin", 0, (int) dieNum);
            }
            break;
            default:
                log.error(mapObject.getName() + " 处理了错误的类型！");
                break;
        }
    }

    private void checkMonsterJinYinAllDle(MapObject map, int monsterId) {
        CrossGroupBossData cgbd = getGroupBossData(map);
        if (cgbd == null) {
            return;
        }

        CrossBossData cbd = cgbd.getSoulAnimalForestMonsterTime().get(map.getZoneModelId());
        if (cbd == null) {
            return;
        }

        Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(cbd.getModelConfigId());
        if (bean == null) {
            log.error("魂兽boss死亡后获取策划数据失败，bossId=" + cbd.getModelConfigId());
            return;
        }
        cbd.setDieNum(cbd.getDieNum() + 1);
        int dieNum = cbd.getDieNum();
        int num = cbd.getMaxNum() - dieNum;
        //计算下一次出现的时间
        if (num < 1) {

            int rebornTime = cbd.getRebornTime();
            int lifeTime = (int) ((TimeUtils.Time() - cbd.getBornTime()) / 1000); //生命值相比于配置的标准值，重生时间做上下浮动
            if (lifeTime > bean.getStandard_time()) {
                rebornTime = rebornTime + bean.getFloat_time();
            } else if (lifeTime < bean.getStandard_time()) {
                rebornTime = rebornTime - bean.getFloat_time();
            }

            int curOpenServerDay = TimeUtils.getOpenServerDay();
            ReadArray<Integer> arr = Manager.bossManager.manager().getLimitTime(curOpenServerDay, bean.getLimit_time());
            if (arr != null) {
                if (rebornTime < arr.get(1)) { //低于下限了取下限值
                    rebornTime = arr.get(1);
                } else if (rebornTime > arr.get(2)) { //高于上限了取上线值
                    rebornTime = arr.get(2);
                }
            } else {
                log.error(" 这里一定会有值");
            }
            syncMonsterDie(map, "", bean.getID(), TimeUtils.Time() + rebornTime * 1000, rebornTime, 3);
        } else {
            syncMonsterDie(map, "", bean.getID(), 0, 0, 3);//需要记录死亡数量
        }
    }

    private void syncMonsterDie(MapObject mapObject, String name, int configId, long nextTime, int rebornTime, int type) {
        int crossServerGroupId = MapParam.getSoulAnimalIsland(mapObject).getCrossServerGroupId();
        //发消息给服务器
        F2PReqCloneMonsterDie.Builder msg = F2PReqCloneMonsterDie.newBuilder();
        msg.setGroupId(crossServerGroupId);
        msg.setCloneModelId(mapObject.getZoneModelId());
        msg.setFightId(mapObject.getId());
        msg.setKiller(name);
        msg.setModelConfigId(configId);
        msg.setReBornTime(nextTime);
        msg.setRebornBaseTime(rebornTime);
        msg.setType(type);
        MessageUtils.send_to_public(F2PReqCloneMonsterDie.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 同步跨服BOSS的信息
     *
     * @param mapObject
     * @param configId
     * @param type
     */
    @Override
    public void syncCrossBossInfo(MapObject mapObject, int configId, int type) {
        CrossGroupBossData cgbd = getGroupBossData(mapObject);
        if (cgbd == null) {
            log.error(mapObject.getName() + "魂兽的基本数据不见了，bossId=" + configId);
            return;
        }
        Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(configId);
        if (bean == null) {
            log.error("魂兽获取策划数据失败，bossId=" + configId);
            return;
        }
        CrossBossData cbd = null;
        switch (type) {
            case SoulLandType.AnimalSmall:
                cbd = cgbd.getCrystalTime().get(mapObject.getZoneModelId());
                break;
            case SoulLandType.AnimalGem:
                cbd = cgbd.getBeastlyBloodCrystalBirthTime().get(mapObject.getZoneModelId());
                break;
            case SoulLandType.AnimalBest:
                cbd = cgbd.getSoulAnimalForestMonsterTime().get(mapObject.getZoneModelId());
                break;
            case SoulLandType.AnimalBoss:
                cbd = cgbd.getSoulAnimalDataMap().get(configId);
                break;
            default:
                break;
        }

        if (cbd == null) {
            return;
        }
        ResSoulAnimalForestCrossRefreshInfo.Builder msg = ResSoulAnimalForestCrossRefreshInfo.newBuilder();
        SoulAnimalForestMessage.forestBossInfo.Builder crystalInfo = SoulAnimalForestMessage.forestBossInfo.newBuilder();
        crystalInfo.setBossId(cbd.getModelConfigId());
        crystalInfo.setIsFollowed(false);
        crystalInfo.setNum(cbd.getMaxNum() - cbd.getDieNum());
        if (cbd.getNextTime() != 0) {
            crystalInfo.setRefreshTime((int) ((cbd.getNextTime() - TimeUtils.Time()) / 1000));
        } else {
            crystalInfo.setRefreshTime(0);
        }
        crystalInfo.setType(type);
        msg.addBossRefreshList(crystalInfo);
        MessageUtils.send_to_map(mapObject, ResSoulAnimalForestCrossRefreshInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public boolean canResetBossData(Player player, List<Cfg_Bossnew_SoulBeasts_Bean> beans, boolean all, boolean notify) {
        if (beans == null) {
            beans = new ArrayList<>();
        }
        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        if (mapObject == null) {
            return false;
        }
        for (Cfg_Bossnew_SoulBeasts_Bean bean : CfgManager.getCfg_Bossnew_SoulBeasts_Container().getValuees()) {
            if (bean.getCloneid() != mapObject.getZoneModelId()) {
                continue;
            }
            if (bean.getType() != SoulLandType.AnimalBoss) {
                continue;
            }
            List<Monster> monsters = Utils.find(mapObject.getMonsters().values(), m -> m.getModelId() == bean.getMonsterid());
            Monster one = Utils.findOne(monsters, m -> !m.isCallBoss());
            if (one == null ) {
                if (all) {
                    beans.add(bean);
                    continue;
                }
                double distance = Utils.getDistance(new Position(bean.getPos().get(0).get(0), bean.getPos().get(0).get(1)), player.gainCurPos());
                if (distance <= BossCallAndRefreshDistance) {
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

    /**
     * 刷新boss
     *
     * @param player
     * @param all
     */
    @Override
    public void resetBossData(Player player, boolean all) {
        List<Cfg_Bossnew_SoulBeasts_Bean> beans = new ArrayList<>();

        boolean canRefresh = canResetBossData(player, beans, all, false);
        if (!canRefresh) {
            return;
        }

        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        int groupId = MapParam.getSoulAnimalIsland(mapObject).getCrossServerGroupId();
        CrossServerMessage.F2PMakeBossRefresh.Builder builder = CrossServerMessage.F2PMakeBossRefresh.newBuilder();
        builder.setGroupID(groupId);
        for (Cfg_Bossnew_SoulBeasts_Bean bean : beans) {
            log.info(String.format("groupId: %s ,玩家[%s]复活Boss[%s]", groupId, player.getName(), bean.getMonsterid()));
            builder.addBossID(bean.getID());
        }
        MessageUtils.send_to_public(CrossServerMessage.F2PMakeBossRefresh.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public int canCallBoss(Player player) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            return 0;
        }
        Cfg_Bossnew_SoulBeasts_Bean bean = null;
        for (Cfg_Bossnew_SoulBeasts_Bean tempBean : CfgManager.getCfg_Bossnew_SoulBeasts_Container().getValuees()) {
            if (tempBean.getCloneid() != map.getZoneModelId()) {
                continue;
            }
            if (tempBean.getType() != SoulLandType.AnimalBoss) {
                continue;
            }
            double distance = Utils.getDistance(new Position(tempBean.getPos().get(0).get(0), tempBean.getPos().get(0).get(1)), player.gainCurPos());
            if (distance > BossCallAndRefreshDistance) {
                continue;
            }
            List<Monster> monsters = Utils.find(map.getMonsters().values(), m -> m.getModelId() == tempBean.getMonsterid() && m.isCallBoss());
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

    /**
     * 召唤boss
     *
     * @param player
     */
    @Override
    public void callBoss(Player player) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            return;
        }
        int cfgId = canCallBoss(player);
        if (cfgId == 0) {
            return;
        }
        Cfg_Bossnew_SoulBeasts_Bean bean = CfgManager.getCfg_Bossnew_SoulBeasts_Container().getValueByKey(cfgId);

        Monster monster = MonsterManager.getInstance().createMonster(bean.getMonsterid());
        if (monster != null) {
            monster.changeLine(map.getLineId());
            monster.changeMapId(map.getId());
            monster.changeMapModelId(map.getMapModelId());
            Position position = new Position();
            position.setX(bean.getPos().get(0).get(0));
            position.setY(bean.getPos().get(0).get(1));
            monster.setInitPos(position);
            monster.setCamp(map.getMapModelId());
            monster.setMakerId(player.getId());
            monster.setCallBoss(true);
            Manager.mapManager.manager().onEnterMap(monster);
            log.info(String.format("[%s]使用道具：召唤Boss[%s]进入地图[%s]:", player.getName(), monster.getName(), map.getName()));
        } else {
            log.error(String.format("[%s]召唤boss[%s]失败:", player.getName(), bean.getMonsterid()));
        }
    }


    private void nextAfterBeastSpiritCrystal(MapObject mapObject, int num) {
        CrossGroupBossData cgbd = getGroupBossData(mapObject);
        if (cgbd == null) {
            log.error(mapObject.getName() + "魂兽森林的基本数据不见了，groupId=" + MapParam.getSoulAnimalIsland(mapObject).getCrossServerGroupId());
            return;
        }
        ConcurrentHashMap<Integer, Integer> cristal = cgbd.getBeastSpiritCrystalMap().get(mapObject.getZoneModelId());
        updateMonster(mapObject, cristal, 1, num);
    }

    private void nextAfterBeastlyBloodCrystal(MapObject mapObject, int num) {
        CrossGroupBossData cgbd = getGroupBossData(mapObject);
        if (cgbd == null) {
            log.error(mapObject.getName() + "魂兽森林的基本数据不见了，groupId=" + MapParam.getSoulAnimalIsland(mapObject).getCrossServerGroupId());
            return;
        }
        ConcurrentHashMap<Integer, Integer> cristal = cgbd.getBeastlyBloodCrystalMap().get(mapObject.getZoneModelId());
        updateMonster(mapObject, cristal, 1, num);
    }

    private void nextAfterMonsterJinYin(MapObject mapObject, int num) {
        CrossGroupBossData cgbd = getGroupBossData(mapObject);
        if (cgbd == null) {
            log.error(mapObject.getName() + "魂兽森林的基本数据不见了，groupId=" + MapParam.getSoulAnimalIsland(mapObject).getCrossServerGroupId());
            return;
        }
        ConcurrentHashMap<Integer, Integer> cristal = cgbd.getSoulAnimalForestMonsterMap().get(mapObject.getZoneModelId());
        updateMonster(mapObject, cristal, 2, num);
    }
}
