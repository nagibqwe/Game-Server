package common.soulanimalforest;

import com.data.CfgManager;
import com.data.Global;
import com.data.MessageString;
import com.data.bean.Cfg_Bossnew_SoulBeasts_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Gather_Bean;
import com.data.container.Cfg_Bossnew_SoulBeasts_Container;
import com.data.container.Cfg_Gather_Container;
import com.data.struct.ReadArray;
import com.game.boss.log.BossDieReliveLog;
import com.game.boss.manager.BossManager;
import com.game.boss.struct.Boss;
import com.game.boss.struct.BossData;
import com.game.boss.struct.BossTypeConst;
import com.game.chat.structs.Notify;
import com.game.copymap.structs.SoulAnimallslandData;
import com.game.count.structs.BaseCountType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.manager.MonsterManager;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.soulanimalforest.manager.SoulAnimalForestCrossManager;
import com.game.soulanimalforest.script.ISoulAnimalForestManager;
import com.game.soulanimalforest.script.ISoulAnimalIslandClone;
import com.game.soulanimalforest.structs.CrossBossData;
import com.game.soulanimalforest.structs.CrossGroupBossData;
import com.game.soulanimalforest.structs.SoulLandType;
import com.game.structs.Gather;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage;
import game.message.SoulAnimalForestMessage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class SoulAnimalIslandManagerScript implements ISoulAnimalForestManager {


    //TODO 神兽岛副本
    final int localType = 6200;

    private static final Logger log = LogManager.getLogger(SoulAnimalIslandManagerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.SoulAnimalForestManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 处理请求本地副本的面板信息
     */
    @Override
    public void onReqSoulAnimalForestLocalPanel(Player player, ReqSoulAnimalForestLocalPanel messInfo) {
        ResSoulAnimalForestLocalPanel.Builder msg = ResSoulAnimalForestLocalPanel.newBuilder();
        int level = messInfo.getLevel();
        msg.setLevel(messInfo.getLevel());
        int bossId = 0;
        int modelId = localType + level;//根据层数获取对应的副本id
        for (Cfg_Bossnew_SoulBeasts_Bean bossbean : Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValuees()) {
            if (bossbean.getCanShow() == 0) {
                continue;
            }
            if (bossbean.getCrossSever() == 1) {
                continue;
            }
            if (bossbean.getType() == SoulLandType.AnimalBest) {
                continue;
            }
            if (level == 0 || bossbean.getCloneid() == modelId) {
                if (bossbean.getType() != SoulLandType.AnimalBoss) {
                    continue;
                }
                if (bossId < 1) {
                    bossId = bossbean.getMonsterid();
                }

                Boss boss = Manager.bossManager.getLocalSoulAnimalCache().get(bossbean.getID());
                forestBossInfo.Builder info = forestBossInfo.newBuilder();
                addBossInfo(info, bossbean.getID(), boss, player, bossbean.getType(), bossbean.getNum());
                msg.addBossList(info);
            }
        }
        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue());
        int dailyMaxCount = Manager.dailyActiveManager.deal().getDailyMaxCount(player, DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue());
        int dailyCanBuyCount = Manager.dailyActiveManager.deal().getDailyCanBuyCount(player, DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue());
        int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue(), 0);

        msg.setMaxCount(dailyMaxCount);
        msg.setRemainCount(dailyRemainCount);
        msg.setCanBuyCount(dailyCanBuyCount);
        msg.setBuyCount(buyCount);

        if (level != 0) {
            BossData bd = BossManager.getCrystalTime().get(modelId);
            int maxNum = BossManager.getCrystalMaxNum().get(modelId);
            forestBossInfo.Builder crystalInfo = forestBossInfo.newBuilder();
            crystalInfo.setBossId(bd.getBossId());
            crystalInfo.setIsFollowed(false);
            crystalInfo.setNum(maxNum - bd.getDieNum());
            crystalInfo.setRefreshTime((int) ((bd.getRebornTime() - TimeUtils.Time()) / 1000));
            crystalInfo.setType(SoulLandType.AnimalSmall);
            msg.addBossList(crystalInfo);
            bd = BossManager.getBeastlyBloodCrystalBirthTime().get(modelId);
            forestBossInfo.Builder shouXueInfo = forestBossInfo.newBuilder();
            shouXueInfo.setBossId(bd.getBossId());
            shouXueInfo.setIsFollowed(false);
            maxNum = BossManager.getBloodCrystalMaxNum().get(modelId);
            shouXueInfo.setNum(maxNum - bd.getDieNum());
            shouXueInfo.setRefreshTime((int) ((bd.getRebornTime() - TimeUtils.Time()) / 1000));
            shouXueInfo.setType(SoulLandType.AnimalGem);
            msg.addBossList(shouXueInfo);
        } else {
            Iterator<Entry<Integer, BossData>> iter = BossManager.getCrystalTime().entrySet().iterator();
            while (iter.hasNext()) {
                Entry<Integer, BossData> en = iter.next();
                int cloneId = en.getKey();
                BossData bd = en.getValue();
                int maxNum = BossManager.getCrystalMaxNum().get(cloneId);
                forestBossInfo.Builder crystalInfo = forestBossInfo.newBuilder();
                crystalInfo.setBossId(bd.getBossId());
                crystalInfo.setIsFollowed(false);
                crystalInfo.setNum(maxNum - bd.getDieNum());
                crystalInfo.setRefreshTime((int) ((bd.getRebornTime() - TimeUtils.Time()) / 1000));
                crystalInfo.setType(SoulLandType.AnimalSmall);
                msg.addBossList(crystalInfo);
            }
            iter = BossManager.getBeastlyBloodCrystalBirthTime().entrySet().iterator();
            while (iter.hasNext()) {
                Entry<Integer, BossData> en = iter.next();
                int cloneId = en.getKey();
                BossData bd = en.getValue();
                forestBossInfo.Builder shouXueInfo = forestBossInfo.newBuilder();
                shouXueInfo.setBossId(bd.getBossId());
                shouXueInfo.setIsFollowed(false);
                int maxNum = BossManager.getBloodCrystalMaxNum().get(cloneId);
                shouXueInfo.setNum(maxNum - bd.getDieNum());
                shouXueInfo.setRefreshTime((int) ((bd.getRebornTime() - TimeUtils.Time()) / 1000));
                shouXueInfo.setType(SoulLandType.AnimalGem);
                msg.addBossList(shouXueInfo);
            }
        }

        long hascount = Manager.countManager.getCount(player, BaseCountType.SOULANIMALFORESTGATHERNUM, 1);
        msg.setCrystalHaveNum((int) (Global.BossOld2_DailyGather1OpenTimes - hascount));

        hascount = Manager.countManager.getCount(player, BaseCountType.SOULANIMALFORESTGATHERNUM, 2);
        msg.setCrystalBloodHaveNum((int) (Global.BossOld2_DailyGather2OpenTimes - hascount));

        MessageUtils.send_to_player(player, ResSoulAnimalForestLocalPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 处理请求跨服副本的面板信息
     */
    @Override
    public void onReqSoulAnimalForestCrossPanel(Player player, ReqSoulAnimalForestCrossPanel messInfo) {
        G2PReqSoulAnimalForestCrossPanel.Builder msg = G2PReqSoulAnimalForestCrossPanel.newBuilder();
        msg.setLevel(messInfo.getLevel());

        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue());
        int dailyMaxCount = Manager.dailyActiveManager.deal().getDailyMaxCount(player, DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue());
        msg.setMaxCount(dailyMaxCount);
        msg.setRemainCount(dailyRemainCount);
        msg.setMaxRankCount(dailyMaxCount);
        msg.setRemainRankCount(dailyRemainCount);

        long hascount = Manager.countManager.getCount(player, BaseCountType.SOULANIMALFORESTGATHERNUM, 1);
        msg.setCrystalHaveNum((int) (Global.BossOld2_DailyGather1OpenTimes - hascount));

        hascount = Manager.countManager.getCount(player, BaseCountType.SOULANIMALFORESTGATHERNUM, 2);
        msg.setCrystalBloodHaveNum((int) (Global.BossOld2_DailyGather2OpenTimes - hascount));

        msg.setRoleId(player.getId());
        MessageUtils.send_to_public(G2PReqSoulAnimalForestCrossPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void addBossInfo(forestBossInfo.Builder info, int configId, Boss boss, Player player, int type, int num) {
        info.setBossId(configId);
        info.setNum(num);
        info.setType(type);
        int refreshTime = boss.getNextTime() > 0 ? (int) ((boss.getNextTime() - TimeUtils.Time()) / 1000) : 0;
        if (refreshTime < 0) {
            refreshTime = 0;
        }
        info.setRefreshTime(refreshTime);

        if (player.getFollowedBossList().contains(configId)) {
            info.setIsFollowed(true);

            //重启服了的话的处理
            List<Long> followedPlayerList = BossManager.getBossFollowedMap().get(configId);
            if (followedPlayerList == null) {
                followedPlayerList = new ArrayList<>();
                List<Long> rset = BossManager.getBossFollowedMap().putIfAbsent(configId, followedPlayerList);
                if (rset != null) {
                    followedPlayerList = rset;
                }
            }

            if (!followedPlayerList.contains(player.getId())) {
                followedPlayerList.add(player.getId());
            }
        } else {
            info.setIsFollowed(false);
        }
    }

    /**
     * 设置单个分组副本的数据模型
     */
    private CrossGroupBossData setAGroupBossInfo(CrossGroupBossData cgbd, crossGroupBossInfo cgbi) {
        int groupId = cgbi.getGroupId();
        cgbd.getBeastlyBloodCrystalBirthTime().clear();
        cgbd.getCrystalTime().clear();
        cgbd.getSoulAnimalDataMap().clear();
        cgbd.getSoulAnimalForestMonsterTime().clear();
        cgbd.setGroupId(groupId);
        for (crossCloneBossInfo ccbi : cgbi.getCloneBossInfoList()) {
            int cloneModelId = ccbi.getCloneModelId();
            cgbd.getBeastlyBloodCrystalBirthTime().put(cloneModelId, crossBossInfoToCrossBossData(ccbi.getCloneSoulXue()));
            cgbd.getCrystalTime().put(cloneModelId, crossBossInfoToCrossBossData(ccbi.getSoulAnimalInfo()));
            if (ccbi.getCloneShouwei().getModelConfigId() != 0) {
                cgbd.getSoulAnimalForestMonsterTime().put(cloneModelId, crossBossInfoToCrossBossData(ccbi.getCloneShouwei()));
            }
            for (crossBossInfo cbi : ccbi.getBossListList()) {
                if (cbi.getModelConfigId() == 0) {
                    continue;
                }
                CrossBossData cbd = crossBossInfoToCrossBossData(cbi);
                cgbd.getSoulAnimalDataMap().put(cbi.getModelConfigId(), cbd);
                //检查副本是否存在， 如果存在则查看是否需要刷新
                Cfg_Bossnew_SoulBeasts_Bean bean = CfgManager.getCfg_Bossnew_SoulBeasts_Container().getValueByKey(cbi.getModelConfigId());
                Cfg_Clone_map_Bean clone_map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(bean.getCloneid());
                if (bean == null) {
                    log.error("公共服下发了一个BOSS的更新，但是配置表已经不在了!,configId =" + cbi.getModelConfigId(), new NullPointerException());
                } else {
                    //检查BOSSID值
                    if (BossManager.getSoulAnimalForestBossId() < 1) {
                        BossManager.setSoulAnimalForestBossId(bean.getMonsterid());
                    }

                    MapObject map = Manager.mapManager.getMap(cbd.getMapUid());
                    if (map == null) {
                        continue;
                    }
                    MapObject mapObject = null;
                    int crossServerGroupId = MapParam.getSoulAnimalIsland(map).getCrossServerGroupId();
                    if (crossServerGroupId == groupId) {
                        mapObject = map;
                    }
                    if (mapObject == null) {
                        continue;
                    }
                    if (cbd.getNextTime() < 1) {
                        int haveNum = 0;
                        List<Monster> monsters = new ArrayList<>(mapObject.getMonsters().values());
                        for (Monster monster : monsters) {
                            if (monster.getModelId() == bean.getMonsterid()) {
                                haveNum += 1;
                                break;
                            }
                        }
                        if (haveNum < 1) {
                            Monster monster = MonsterManager.getInstance().createMonster(bean.getMonsterid());
                            if (monster != null) {
                                monster.changeLine(mapObject.getLineId());
                                monster.changeMapId(mapObject.getId());
                                monster.changeMapModelId(mapObject.getMapModelId());
                                Position position = new Position();
                                position.setX(bean.getPos().get(0).get(0));
                                position.setY(bean.getPos().get(0).get(1));
                                monster.setInitPos(position);
                                Manager.mapManager.manager().onEnterMap(monster);
                            }
                        }
                    } else {
                        Manager.mapManager.createTombstone(mapObject, bean.getMonsterid());
                    }
                }
            }
        }
        return cgbd;
    }

    /**
     * 获取采集时候的buffid
     *
     * @param player
     * @param gather
     * @return
     */
    public static int getGatherBuffId(Player player, Gather gather) {
        Cfg_Gather_Bean cfg_gather_bean = Cfg_Gather_Container.GetInstance().getValueByKey(gather.getMapModelId());
        if (cfg_gather_bean == null) {
            log.error(String.format("玩家{%s}采集{%s},失败，表cfg_gather_bean找不到对应的配置", TaskHelp.getPlayerInfo(player), gather.getMapModelId()));
            return 0;
        }
        return cfg_gather_bean.getGetbuff();
    }

    @Override
    public void onP2FResSoulAnimalForestBossInfo(P2FResSoulAnimalForestBossInfo messInfo) {
        for (crossGroupBossInfo cgbi : messInfo.getGroupInfoList()) {
            CrossGroupBossData cgbd = setAGroupBossInfo(new CrossGroupBossData(), cgbi);
            SoulAnimalForestCrossManager.getGroupInfo().put(cgbi.getGroupId(), cgbd);
        }
        //初始化怪物数量
        crossBossInfoInit();
    }

    private CrossBossData crossBossInfoToCrossBossData(crossBossInfo cbi) {
        CrossBossData cbd = new CrossBossData();
        cbd.setModelConfigId(cbi.getModelConfigId());
        cbd.setBornTime(cbi.getBornTime());
        cbd.setDieNum(cbi.getDieNum());
        cbd.setDieTime(cbi.getDieTime());
        cbd.setMaxNum(cbi.getMaxNum());
        cbd.setNextTime(cbi.getNextTime());
        cbd.setRebornTime(cbi.getRebornTime());
        cbd.setMapUid(cbi.getFightRoomId());
        Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(cbi.getModelConfigId());
        if (bean == null) {
            log.error("公共服下发了一个BOSS的更新，但是配置表已经不在了!,configId =" + cbi.getModelConfigId(), new NullPointerException());
        }
        return cbd;
    }

    @Override
    public void onP2FUpdateOneSoulAnimalForestBossInfo(P2FUpdateOneSoulAnimalForestBossInfo messInfo) {
        int type = messInfo.getType();
        int configId = messInfo.getBossInfo().getModelConfigId();
        Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(configId);
        if (bean == null) {
            log.error("公共服下发了一个BOSS的更新，但是配置表已经不在了!,configId =" + configId, new NullPointerException());
            return;
        }
        long fightId = messInfo.getFightRoomId();
        crossBossInfo cbi = messInfo.getBossInfo();

        MapObject mapObject = Manager.mapManager.getMap(fightId);
        int groupId = messInfo.getGroupId();

        CrossGroupBossData cgbd = SoulAnimalForestCrossManager.getGroupBossData(groupId);
        if (cgbd == null) {
            log.error("公共服下发了一个BOSS的更新，但是配置表已经不在了!,configId =" + configId + " , groupId=" + groupId, new NullPointerException());
            return;
        }

        //副本已经存在了， 则把BOSS刷出来
        if (type == SoulLandType.AnimalBoss) {
            int haveNum = 0;
            List<Monster> monsters = new ArrayList<>(mapObject.getMonsters().values());
            for (Monster monster : monsters) {
                if (monster.getModelId() == bean.getMonsterid()) {
                    haveNum += 1;
                    break;
                }
            }
            //如果没有刷新怪，则刷新BOSS
            if (haveNum < 1) {
                Monster monster = MonsterManager.getInstance().createMonster(bean.getMonsterid());
                if (monster != null) {
                    monster.changeLine(mapObject.getLineId());
                    monster.changeMapId(mapObject.getId());
                    monster.changeMapModelId(mapObject.getMapModelId());
                    monster.setCamp(mapObject.getMapModelId());
                    Position position = new Position();
                    position.setX(bean.getPos().get(0).get(0));
                    position.setY(bean.getPos().get(0).get(1));
                    monster.setInitPos(position);
                    Manager.mapManager.manager().onEnterMap(monster);
                }
            }
            CrossBossData cbd = crossBossInfoToCrossBossData(cbi);
            cbd.setDieNum(0);
            cgbd.getSoulAnimalDataMap().put(bean.getID(), cbd);

            IScript is = Manager.scriptManager.GetScriptClass(mapObject.getSetting().getIsscript());
            if (is instanceof ISoulAnimalIslandClone) {
                ISoulAnimalIslandClone isafc = (ISoulAnimalIslandClone) is;
                isafc.syncCrossBossInfo(mapObject, configId, type);
            }
            return;
        }

        IScript is = Manager.scriptManager.GetScriptClass(mapObject.getSetting().getIsscript());
        if (is instanceof ISoulAnimalIslandClone) {
            ISoulAnimalIslandClone isafc = (ISoulAnimalIslandClone) is;
            isafc.birthTime(type, mapObject, cbi.getDieNum());
        }
        CrossBossData cbd = crossBossInfoToCrossBossData(cbi);
        cbd.setDieNum(0);
        switch (type) {
            case SoulLandType.AnimalSmall: {
                cgbd.getCrystalTime().put(bean.getCloneid(), cbd);
            }
            break;
            case SoulLandType.AnimalGem: {
                cgbd.getBeastlyBloodCrystalBirthTime().put(bean.getCloneid(), cbd);
            }
            break;
            case SoulLandType.AnimalBest: {
                cgbd.getSoulAnimalForestMonsterTime().put(bean.getCloneid(), cbd);
            }
            break;
            default:
                return;
        }
        F2PUpdateOneSoulAnimalForestBossInfo.Builder msg = F2PUpdateOneSoulAnimalForestBossInfo.newBuilder();
        msg.setType(type);
        msg.setFightRoomId(fightId);
        msg.setBossInfo(cbi);
        msg.setGroupId(groupId);
        MessageUtils.send_to_public(F2PUpdateOneSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        //执行同步本地图的功能
        if (is instanceof ISoulAnimalIslandClone) {
            ISoulAnimalIslandClone isafc = (ISoulAnimalIslandClone) is;
            isafc.syncCrossBossInfo(mapObject, configId, type);
        }
    }


    @Override
    public void createMonster(MapObject map, ConcurrentHashMap<Integer, Integer> cristal, int type, BossData bd, int maxNum) {
        int endNum = maxNum - bd.getDieNum();
        bd.setMapUid(map.getId());
        if (endNum < 1) {
            return;
        }
        Cfg_Bossnew_SoulBeasts_Bean bean = null;
        if (cristal == null) {
            return;
        }
        for (int configId : cristal.keySet()) {
            bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(configId);
            if (bean == null) {
                log.info(map.getName() + " 找不到配置的水晶配置项:" + configId);
                continue;
            }
            int criModelId = bean.getMonsterid();
            List<Position> poslist = new ArrayList<>();
            ReadArray<Integer>[] posArr = bean.getPos().getValuees();
            for (ReadArray<Integer> aii : posArr) {
                poslist.add(MapManager.getPos(aii.get(0), aii.get(1)));
            }
            int end = Math.min(bean.getNum(), endNum);
            endNum -= end;
            for (int k = 0; k < end; ++k) {
                Position pos = poslist.get(0);
                int randIndex = -1;
                if (poslist.size() > 1) {
                    randIndex = RandomUtils.random(poslist.size());
                    pos = poslist.get(randIndex);
                    poslist.remove(randIndex);
                }
                if (type == 1) {
                    Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(criModelId);
                    if (null == gatherCfg) {
                        log.error("Cfg_Gather_Bean无法找到指定数据，神兽岛生成采集物失败，id = " + criModelId);
                        continue;
                    }
                    Gather gather = Manager.gatherManager.deal().createGather(map, gatherCfg, pos);
                    if (gather == null) {
                        log.error(map.getName() + " 创建采集物时出错了", new NullPointerException());
                        if (randIndex >= 0) {
                            poslist.add(pos);
                        }
                        continue;
                    }
                    //记录下来这个坐标位置上有物品
                    SoulAnimalIslandManagerScript.getIdPosMap(map).put(gather.getId(), pos.getX() + "_" + pos.getY());
                    gather.setNo(bean.getID());//设置关联的表数据
                    SoulAnimalIslandManagerScript.getPosHasValueMap(map).put(pos.getX() + "_" + pos.getY(), 1);
                } else {
                    Monster monster = Manager.monsterManager.createMonster(map, pos, criModelId);
                    monster.setCamp(map.getMapModelId());
                    if (monster == null) {
                        log.error(map.getName() + " 创建怪物时出错了", new NullPointerException());
                        if (randIndex >= 0) {
                            poslist.add(pos);
                        }
                        continue;
                    }
                    //记录下来这个坐标位置上有物品
                    SoulAnimalIslandManagerScript.getIdPosMap(map).put(monster.getId(), pos.getX() + "_" + pos.getY());
                    SoulAnimalIslandManagerScript.getPosHasValueMap(map).put(pos.getX() + "_" + pos.getY(), 1);
                }
            }
        }

    }

    @Override
    public void createCrossMonster(MapObject map, ConcurrentHashMap<Integer, Integer> cristal, int type, CrossBossData bd) {
        int endNum = bd.getMaxNum() - bd.getDieNum();
        bd.setMapUid(map.getId());
        if (endNum < 1) {
            return;
        }
        Cfg_Bossnew_SoulBeasts_Bean bean = null;
        if (cristal == null) {
            return;
        }
        for (int configId : cristal.keySet()) {
            bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(configId);
            if (bean == null) {
                log.info(map.getName() + " 找不到配置的水晶配置项:" + configId);
                continue;
            }
            int criModelId = bean.getMonsterid();
            List<Position> poslist = new ArrayList<>();
            ReadArray<Integer>[] posArr = bean.getPos().getValuees();
            for (ReadArray<Integer> aii : posArr) {
                poslist.add(MapManager.getPos(aii.get(0), aii.get(1)));
            }
            int end = Math.min(bean.getNum(), endNum);
            endNum -= end;
            for (int k = 0; k < end; ++k) {
                Position pos = poslist.get(0);
                int randIndex = -1;
                if (poslist.size() > 1) {
                    randIndex = RandomUtils.random(poslist.size());
                    pos = poslist.get(randIndex);
                    poslist.remove(randIndex);
                }
                if (type == SoulLandType.AnimalSmall) {
                    Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(criModelId);
                    if (null == gatherCfg) {
                        log.error("Cfg_Gather_Bean无法找到指定数据，神兽岛生成采集物失败，id = " + criModelId);
                        continue;
                    }
                    Gather gather = Manager.gatherManager.deal().createGather(map, gatherCfg, pos);
                    if (gather == null) {
                        log.error(map.getName() + " 创建采集物时出错了", new NullPointerException());
                        if (randIndex >= 0) {
                            poslist.add(pos);
                        }
                        continue;
                    }
                    //记录下来这个坐标位置上有物品
                    SoulAnimalIslandManagerScript.getIdPosMap(map).put(gather.getId(), pos.getX() + "_" + pos.getY());
                    gather.setNo(bean.getID());//设置关联的表数据
                    SoulAnimalIslandManagerScript.getPosHasValueMap(map).put(pos.getX() + "_" + pos.getY(), 1);
                } else {
                    Monster monster = Manager.monsterManager.createMonster(map, pos, criModelId);
                    monster.setCamp(map.getMapModelId());
                    if (monster == null) {
                        log.error(map.getName() + " 创建怪物时出错了", new NullPointerException());
                        if (randIndex >= 0) {
                            poslist.add(pos);
                        }
                        continue;
                    }
                    //记录下来这个坐标位置上有物品
                    SoulAnimalIslandManagerScript.getIdPosMap(map).put(monster.getId(), pos.getX() + "_" + pos.getY());
                    SoulAnimalIslandManagerScript.getPosHasValueMap(map).put(pos.getX() + "_" + pos.getY(), 1);
                }
            }
        }

    }

    @Override
    public void onP2FResCloneMonsterDie(P2FResCloneMonsterDie messInfo) {
        /**
         * 需要通知当前地图的玩家， BOSS的消息有变更
         */
        long fightId = messInfo.getFightId();
        MapObject mapObject = Manager.mapManager.getMap(fightId);
        if (mapObject == null) {
            log.error("发过来的地图ID" + fightId + "编号 查不到副本");
            return;
        }

        int groupId = messInfo.getGroupId();

        CrossGroupBossData cgbd = SoulAnimalForestCrossManager.getGroupBossData(groupId);
        if (cgbd == null) {
            log.error("发过来的副本ID =" + fightId + " , groupId=" + groupId, new NullPointerException());
            return;
        }

        int state = messInfo.getState();
        if (state != 0) {
            log.error("发布死亡结果时， 副本ID" + fightId + "的状态值是：" + state + ", 副本ID= " + messInfo);
            return;
        }
        int type = messInfo.getType();

        CrossBossData data = null;
        switch (type) {
            case SoulLandType.AnimalSmall:
                data = cgbd.getCrystalTime().get(messInfo.getCloneModelId());
                break;
            case SoulLandType.AnimalGem:
                data = cgbd.getBeastlyBloodCrystalBirthTime().get(messInfo.getCloneModelId());
                break;
            case SoulLandType.AnimalBest:
                data = cgbd.getSoulAnimalForestMonsterTime().get(messInfo.getCloneModelId());
                break;
            case SoulLandType.AnimalBoss:
                data = cgbd.getSoulAnimalDataMap().get(messInfo.getModelConfigId());
                break;
            default:
                break;
        }

        if (data == null) {
            log.error("发过来的数据有一个错误:" + messInfo);
            return;
        }
        //更新时间值
        data.setNextTime(messInfo.getReBornTime());
        data.setRebornTime(messInfo.getRebornBaseTime());
        data.setDieNum(messInfo.getDieNum());
        if (data.getMaxNum() <= data.getDieNum()) {
            data.setDieTime(data.getDieTime() + 1);
        }

        IScript is = Manager.scriptManager.GetScriptClass(mapObject.getSetting().getIsscript());
        if (is instanceof ISoulAnimalIslandClone) {
            ISoulAnimalIslandClone isafc = (ISoulAnimalIslandClone) is;
            isafc.syncCrossBossInfo(mapObject, messInfo.getModelConfigId(), messInfo.getType());
        } else {
            log.error("没有找到实例类！", new NullPointerException());
        }
    }

    @Override
    public void onP2GResSoulAnimalForestCrossBossRefreshTip(P2GResSoulAnimalForestCrossBossRefreshTip messInfo) {
        List<Long> roleIds = messInfo.getRoleIdsList();
        ResSoulAnimalForestCrossBossRefreshTip.Builder msg = ResSoulAnimalForestCrossBossRefreshTip.newBuilder();
        msg.setBossId(messInfo.getBossId());
        Iterator<Long> iterator = roleIds.iterator();
        Set<Long> hasSend = new HashSet<>();
        while (iterator.hasNext()) {
            long roleId = iterator.next();
            Player player = Manager.playerManager.getPlayerOnline(roleId);
            if (player == null || hasSend.contains(roleId)) {
                continue;
            }
            hasSend.add(roleId);
            MessageUtils.send_to_player(player, ResSoulAnimalForestCrossBossRefreshTip.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }

        if (roleIds.isEmpty()) {
            return;
        }
        Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(messInfo.getBossId());
        if (bean == null) {
            log.error("configId =" + messInfo.getBossId() + "在配置文件中找不到哦！", new NullPointerException());
            return;
        }
        // String bossName = ServerStr.getBossName(bean.getMonsterid());
        // String title = ServerStr.ServerLocalString(ServerStr.Offline_Hangup_Message_Title_Str);
        // String serverlang = ServerStr.ServerLocalString(ServerStr.DreamBossRefreshTip_Str);
        // String content = String.format(serverlang, bossName);
        // for (long roleId : roleIds) {
        //     PushMessManager.PushMessage(roleId, 0, title, content);
        // }
    }

    @Override
    public void crossBossInfoInit() {
//        SoulAnimalForestCrossManager.getBeastSpiritCrystalMap().clear();
//        SoulAnimalForestCrossManager.getBeastlyBloodCrystalMap().clear();
//        SoulAnimalForestCrossManager.getSoulAnimalForestMonsterMap().clear();
        ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>> groupDest = new ConcurrentHashMap<>();
        for (Cfg_Bossnew_SoulBeasts_Bean bean : Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValuees()) {
            if (bean.getCanShow() == 0) {
                continue;
            }
            if (bean.getCrossSever() == 0) {
                continue;
            }
            ConcurrentHashMap<Integer, Integer> configIDs = null;
            int cloneModelId = bean.getCloneid();
            ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> dest = null;
            switch (bean.getType()) {
                case SoulLandType.AnimalSmall:
                case SoulLandType.AnimalGem:
                case SoulLandType.AnimalBest: {
//                    dest = SoulAnimalForestCrossManager.getBeastSpiritCrystalMap();
                    if (groupDest.containsKey(bean.getType())) {
                        dest = groupDest.get(bean.getType());
                    } else {
                        dest = new ConcurrentHashMap<>();
                        groupDest.put(bean.getType(), dest);
                    }
                }
                break;
                default:
                    continue;
            }

            if (dest == null) {
                continue;
            }

            configIDs = dest.get(cloneModelId);
            if (configIDs == null) {
                configIDs = new ConcurrentHashMap<>();
                dest.put(cloneModelId, configIDs);
            }
            configIDs.put(bean.getID(), bean.getNum());
        }

        for (Entry<Integer, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>>> entry : groupDest.entrySet()) {
            int type = entry.getKey();
            ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> dest = entry.getValue();
            for (CrossGroupBossData cgbd : SoulAnimalForestCrossManager.getGroupInfo().values()) {
                switch (type) {
                    case SoulLandType.AnimalSmall: {
                        cgbd.getBeastSpiritCrystalMap().clear();
                        for (Entry<Integer, ConcurrentHashMap<Integer, Integer>> iter : dest.entrySet()) {
                            cgbd.getBeastSpiritCrystalMap().put(iter.getKey(), iter.getValue());
                        }
                    }
                    break;
                    case SoulLandType.AnimalGem: {
                        cgbd.getBeastlyBloodCrystalMap().clear();
                        for (Entry<Integer, ConcurrentHashMap<Integer, Integer>> iter : dest.entrySet()) {
                            cgbd.getBeastlyBloodCrystalMap().put(iter.getKey(), iter.getValue());
                        }
                    }
                    break;
                    case SoulLandType.AnimalBest: {
                        cgbd.getSoulAnimalForestMonsterMap().clear();
                        for (Entry<Integer, ConcurrentHashMap<Integer, Integer>> iter : dest.entrySet()) {
                            cgbd.getSoulAnimalForestMonsterMap().put(iter.getKey(), iter.getValue());
                        }
                    }
                    break;
                    default:
                        break;
                }
            }
        }
    }


    /**
     * 获取地图参数 id----->pos
     *
     * @param map
     * @return
     */
    public static Map<Long, String> getIdPosMap(MapObject map) {
        SoulAnimallslandData data = MapParam.getSoulAnimalIsland(map);
        return data.getIdPosMap();
    }

    /**
     * 获取地图上坐标是的对应值
     *
     * @param map
     * @return
     */
    public static Map<String, Integer> getPosHasValueMap(MapObject map) {
        SoulAnimallslandData data = MapParam.getSoulAnimalIsland(map);
        return data.getPosHasValueMap();
    }

    @Override
    public void load() {
        if (GameServer.getInstance().IsFightServer()) {
            return;
        }
        BossManager.getSoulAnimalForestMonsterMap().clear();
        BossManager.getCrystalTime().clear();
        BossManager.getBeastlyBloodCrystalMap().clear();
        Manager.bossManager.getLocalSoulAnimalCache().clear();
        ConcurrentHashMap<Integer, Boss> soulAnimalForestBossMap = BossManager.getInstance().getLocalSoulAnimalCache();
        boolean isSave = false;
        ConcurrentHashMap<Integer, BossData> localSoulAnimalDataMap = ServerParamUtil.localSoulAnimalDataMap;
        Set<Integer> soulanimalboss = new HashSet<>(localSoulAnimalDataMap.keySet());
        BossData data;
        int firstBossID = BossManager.getSoulAnimalForestBossId();
        if (soulanimalboss.contains(firstBossID) == false) {
            BossManager.setSoulAnimalForestBossId(0);
        }
        Cfg_Bossnew_SoulBeasts_Bean[] arr = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValuees();
        for (Cfg_Bossnew_SoulBeasts_Bean bean : arr) {
            if (bean.getCanShow() == 0) {
                continue;
            }

            if (bean.getCrossSever() == 1) {
                continue;
            }
            int cloneID = bean.getCloneid();
            if (bean.getType() != SoulLandType.AnimalBoss) {
                ConcurrentHashMap<Integer, Integer> configIDs = null;
                ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> dest = null;
                switch (bean.getType()) {
                    case SoulLandType.AnimalSmall: {
                        dest = BossManager.getBeastSpiritCrystalMap();
                        //初始化时间值
                        if (!BossManager.getCrystalTime().containsKey(cloneID)) {
                            BossData bd = new BossData();
                            bd.setBornTime(TimeUtils.Time());
                            bd.setBossId(bean.getID());
                            bd.setDieNum(0);
                            bd.setReBornBaseTime(bean.getInitial_time());
                            bd.setRebornTime(-1);
                            BossManager.getCrystalTime().put(cloneID, bd);
                        }

                        if (BossManager.getCrystalMaxNum().containsKey(cloneID)) {
                            int old = BossManager.getCrystalMaxNum().get(cloneID);
                            old += bean.getNum();
                            BossManager.getCrystalMaxNum().put(cloneID, old);
                        } else {
                            BossManager.getCrystalMaxNum().put(cloneID, bean.getNum());
                        }

                    }
                    break;
                    case SoulLandType.AnimalGem: {
                        dest = BossManager.getBeastlyBloodCrystalMap();
                        //初始化时间值
                        if (!BossManager.getBeastlyBloodCrystalBirthTime().containsKey(cloneID)) {
                            BossData bd = new BossData();
                            bd.setBornTime(TimeUtils.Time());
                            bd.setBossId(bean.getID());
                            bd.setDieNum(0);
                            bd.setReBornBaseTime(bean.getInitial_time());
                            bd.setRebornTime(-1);
                            BossManager.getBeastlyBloodCrystalBirthTime().put(cloneID, bd);
                        }
                        if (BossManager.getBloodCrystalMaxNum().containsKey(cloneID)) {
                            int old = BossManager.getBloodCrystalMaxNum().get(cloneID);
                            old += bean.getNum();
                            BossManager.getBloodCrystalMaxNum().put(cloneID, old);
                        } else {
                            BossManager.getBloodCrystalMaxNum().put(cloneID, bean.getNum());
                        }
                    }
                    break;
                    case SoulLandType.AnimalBest: {
                        dest = BossManager.getSoulAnimalForestMonsterMap();
                        //初始化时间值
                        if (!BossManager.getSoulAnimalForestMonsterTime().containsKey(cloneID)) {
                            BossData bd = new BossData();
                            bd.setBornTime(TimeUtils.Time());
                            bd.setBossId(bean.getID());
                            bd.setDieNum(0);
                            bd.setReBornBaseTime(bean.getInitial_time());
                            bd.setRebornTime(bd.getBornTime() + bd.getReBornBaseTime());
                            BossManager.getSoulAnimalForestMonsterTime().put(cloneID, bd);
                        }

                        if (BossManager.getSoulAnimalForestMonsterMaxNum().containsKey(cloneID)) {
                            int old = BossManager.getSoulAnimalForestMonsterMaxNum().get(cloneID);
                            old += bean.getNum();
                            BossManager.getSoulAnimalForestMonsterMaxNum().put(cloneID, old);
                        } else {
                            BossManager.getSoulAnimalForestMonsterMaxNum().put(cloneID, bean.getNum());
                        }
                    }
                    break;
                    default:
                        continue;
                }

                if (dest == null) {
                    continue;
                }

                configIDs = dest.get(cloneID);
                if (configIDs == null) {
                    configIDs = new ConcurrentHashMap<>();
                    dest.put(cloneID, configIDs);
                }
                configIDs.put(bean.getID(), bean.getNum());
                continue;
            }

            Boss dreamBoss = new Boss();
            dreamBoss.setConfigId(bean.getID()); //bossId，约定bossId = monsterId * 1000 + mapId
            dreamBoss.setModelId(bean.getMonsterid()); //boss对应的怪物Id
            dreamBoss.setMapID(bean.getCloneid());
            ReadArray<Integer> aii = bean.getPos().getValuees()[0];
            Position pos = MapManager.getPos(aii.get(0), aii.get(1));
            dreamBoss.setPos(pos);
            soulanimalboss.remove(bean.getID());
            if (bean.getLayer() == 1) {
                if (BossManager.getSoulAnimalForestBossId() < 1) {
                    BossManager.setSoulAnimalForestBossId(bean.getMonsterid());
                }
            }
            data = localSoulAnimalDataMap.get(bean.getID());
            if (data == null) { //防止数据表策划新加了怪，而原来数据库里存的bossData里没有，则要加入
                data = new BossData();
                data.setBossId(bean.getID());
                data.setBornTime(0);
                data.setDieNum(0);
                data.setReBornBaseTime(bean.getInitial_time());
                data.setRebornTime(0);
                localSoulAnimalDataMap.put(bean.getID(), data);
                isSave = true;
            }

            dreamBoss.setNextTime(data.getRebornTime());
            soulAnimalForestBossMap.put(bean.getID(), dreamBoss);
        }
        //如果有变化则需要保存
        if (isSave) {
            ServerParamUtil.saveSoulAnimalBoss();
        }
    }

    @Override
    public void syncSoulAnilmasnfo(MapObject mapObject, int bossId, int type) {
        ResSoulAnimalForestLocalRefreshInfo.Builder msg = ResSoulAnimalForestLocalRefreshInfo.newBuilder();
        SoulAnimalForestMessage.forestBossInfo.Builder synBoss = SoulAnimalForestMessage.forestBossInfo.newBuilder();
        if (type == SoulLandType.AnimalSmall) {
            BossData bd = BossManager.getCrystalTime().get(mapObject.getZoneModelId());
            int maxNum = BossManager.getCrystalMaxNum().get(mapObject.getZoneModelId());
            synBoss.setBossId(bd.getBossId());
            synBoss.setIsFollowed(false);
            synBoss.setNum(maxNum - bd.getDieNum());
            synBoss.setRefreshTime((int) ((bd.getRebornTime() - TimeUtils.Time()) / 1000));
            synBoss.setType(type);
        }
        if (type == SoulLandType.AnimalGem) {
            BossData bd = BossManager.getBeastlyBloodCrystalBirthTime().get(mapObject.getZoneModelId());
            synBoss.setBossId(bd.getBossId());
            synBoss.setIsFollowed(false);
            int maxNum = BossManager.getBloodCrystalMaxNum().get(mapObject.getZoneModelId());
            synBoss.setNum(maxNum - bd.getDieNum());
            synBoss.setRefreshTime((int) ((bd.getRebornTime() - TimeUtils.Time()) / 1000));
            synBoss.setType(type);
        }
        if (type == SoulLandType.AnimalBest) {
            BossData bd = BossManager.getSoulAnimalForestMonsterTime().get(mapObject.getZoneModelId());
            synBoss.setBossId(bd.getBossId());
            synBoss.setIsFollowed(false);
            int maxNum = BossManager.getSoulAnimalForestMonsterMaxNum().get(mapObject.getZoneModelId());
            synBoss.setNum(maxNum - bd.getDieNum());
            synBoss.setRefreshTime((int) ((bd.getRebornTime() - TimeUtils.Time()) / 1000));
            synBoss.setType(type);
        }
        if (type == SoulLandType.AnimalBoss) {
            Boss boss = Manager.bossManager.getLocalSoulAnimalCache().get(bossId);
            if (boss == null) {
                return;
            }
            synBoss.setBossId(boss.getConfigId());
            int refreshTime = boss.getNextTime() > 0 ? (int) ((boss.getNextTime() - TimeUtils.Time()) / 1000) : 0;
            if (refreshTime < 0) {
                refreshTime = 0;
            }
            synBoss.setRefreshTime(refreshTime);
            synBoss.setIsFollowed(false);
            synBoss.setNum(1);
            synBoss.setType(type);
        }
        msg.addBossRefreshList(synBoss);
        MessageUtils.send_to_map(mapObject, ResSoulAnimalForestLocalRefreshInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void calcAnimalIslandBossBirth() {
        long curTime = TimeUtils.Time();
        boolean isSaveData = false;
        for (Boss soulAnimalBoss : Manager.bossManager.getLocalSoulAnimalCache().values()) {
            if (soulAnimalBoss.getNextTime() <= 0) { //已刷新的
                continue;
            }
            //boss刷新前一分钟通知玩家
            if (curTime < soulAnimalBoss.getNextTime() && !soulAnimalBoss.isHaveFlush()) {
                int betweenTime = (int) ((soulAnimalBoss.getNextTime() - curTime) / 1000); //取秒
                if (betweenTime <= 60) {
                    Manager.bossManager.manager().sendBossRefreshTip(soulAnimalBoss.getConfigId(), BossTypeConst.SOULANIMAL_BOSS);
                    soulAnimalBoss.setHaveFlush(true); //借用此字段表示刷新提示已通知过了
                }
                continue;
            }
            if (curTime > soulAnimalBoss.getNextTime()) {
                //获取boss对应的副本进行刷新
                // Cfg_Clone_map_Bean clone_map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(soulAnimalBoss.getMapID());

                MapObject map = Manager.mapManager.getMap(soulAnimalBoss.getMapUid()); //dreamBoss借用了loop字段表示副本Id
                if (map == null) { //副本都还没创建(还没有玩家进入过，首次进入会创建)，怪就不刷了呗
                    continue;
                }
                Monster monster = MonsterManager.getInstance().createMonster(soulAnimalBoss.getModelId());
                if (monster != null) {
                    monster.changeLine(map.getLineId());
                    monster.changeMapId(map.getId());
                    monster.changeMapModelId(map.getMapModelId());
                    Position position = new Position();
                    position.setX(soulAnimalBoss.getPos().getX());
                    position.setY(soulAnimalBoss.getPos().getY());
                    monster.setInitPos(position);
                    monster.setCamp(map.getMapModelId());
                    Manager.mapManager.manager().onEnterMap(monster);
                    soulAnimalBoss.setNextTime(0L); //刷新后重置下次刷新时间
                    soulAnimalBoss.setHaveFlush(false);
                    //刷新后同步给客户端
                    //syncSoulAnimalForestBossInfo(map, soulAnimalBoss.getConfigId(), 3);

                    BossDieReliveLog bossDieLog = new BossDieReliveLog();
                    bossDieLog.setBossId(soulAnimalBoss.getModelId());
                    bossDieLog.setMapId(map.getMapModelId());
                    bossDieLog.setType(1);
                    LogService.getInstance().execute(bossDieLog);

                    //闪现公告
                    MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.shenshouBossRefresh, soulAnimalBoss.name(), Utils.makeUrlStr(MessageString.shenshouBossRefresh), map.getNoticeName());

//                    MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.DreamBossRefresh, soulAnimalBoss.getName(), Utils.makeUrlStr(MessageString.DreamBossRefresh), map.getNoticeName());
                    BossData data = ServerParamUtil.localSoulAnimalDataMap.get(soulAnimalBoss.getConfigId());
                    if (data != null) {
                        data.setBornTime(TimeUtils.Time());
                        data.setRebornTime(0L);
                        isSaveData = true;
                    } else {
                        log.error("Error! 刷新幻境boss时获取其存库数据data失败，不该发生的，bossId=" + soulAnimalBoss.getConfigId());
                    }
                } else {
                    log.error("幻境Boss刷新怪物生成失败：monsterId=" + soulAnimalBoss.getModelId() + ", bossId=" + soulAnimalBoss.getConfigId());
                }
            }
        }

        if (isSaveData) {
            ServerParamUtil.saveSoulAnimalBoss();
        }

        //计算下一次刷新的时间值
        Iterator<Entry<Integer, BossData>> iter = BossManager.getCrystalTime().entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Integer, BossData> en = iter.next();
            int cloneModelId = en.getKey();
            BossData bd = en.getValue();

            MapObject mapObject = Manager.mapManager.getMap(bd.getMapUid());

            if (mapObject == null) { //副本都还没创建(还没有玩家进入过，首次进入会创建)，怪就不刷了呗
                if (curTime >= bd.getRebornTime()) {
                    bd.setRebornTime(bd.getRebornTime() + bd.getReBornBaseTime() * 1000);
                }
            } else {
                if (bd.getRebornTime() <= 0) {
                    continue;
                }
                //刷新下一波怪
                if (curTime > bd.getRebornTime()) {
                    IScript is = Manager.scriptManager.GetScriptClass(mapObject.getSetting().getIsscript());
                    if (is instanceof SoulAnimalIslandLocalScript) {
                        SoulAnimalIslandLocalScript isafc = (SoulAnimalIslandLocalScript) is;
                        isafc.birthTime(1, mapObject, curTime - bd.getRebornTime());
                    }
                }
            }
        }

        iter = BossManager.getBeastlyBloodCrystalBirthTime().entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Integer, BossData> en = iter.next();
            int cloneModelId = en.getKey();
            BossData bd = en.getValue();

            MapObject mapObject = Manager.mapManager.getMap(bd.getMapUid()); //dreamBoss借用了loop字段表示副本Id
            if (mapObject != null) {
                if (bd.getRebornTime() <= 0) {
                    continue;
                }
                //刷新下一波怪
                if (curTime > bd.getRebornTime()) {
                    IScript is = Manager.scriptManager.GetScriptClass(mapObject.getSetting().getIsscript());
                    if (is instanceof SoulAnimalIslandLocalScript) {
                        SoulAnimalIslandLocalScript isafc = (SoulAnimalIslandLocalScript) is;
                        isafc.birthTime(2, mapObject, curTime - bd.getRebornTime());
                    }
                }
            }
        }
    }

}
