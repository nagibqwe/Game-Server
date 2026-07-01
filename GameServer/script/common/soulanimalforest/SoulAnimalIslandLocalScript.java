package common.soulanimalforest;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Bossnew_SoulBeasts_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Gather_Bean;
import com.data.container.Cfg_Bossnew_SoulBeasts_Container;
import com.data.container.Cfg_Clone_map_Container;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.boss.manager.BossManager;
import com.game.boss.struct.Boss;
import com.game.boss.struct.BossData;
import com.game.boss.struct.BossTypeConst;
import com.game.chat.structs.Notify;
import com.game.copymap.scripts.ICopyGatherScript;
import com.game.copymap.structs.SoulAnimallslandData;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.drop.structs.SpecialDropDefine;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.manager.MonsterManager;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.soulanimalforest.script.ISoulAnimalIslandClone;
import com.game.structs.Fighter;
import com.game.structs.Gather;
import com.game.structs.ServerStr;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.ServerParamUtil;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.SoulAnimalForestMessage.ResSoulAnimalForestLocalRefreshInfo;
import game.message.SoulAnimalForestMessage.forestBossInfo;
import io.netty.util.internal.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 魂兽岛副本
 */
public class SoulAnimalIslandLocalScript implements IMapBaseScript, ICopyGatherScript, ISoulAnimalIslandClone {

    private final static Logger log = LogManager.getLogger(SoulAnimalIslandLocalScript.class);

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        int zoneModelId = mapObject.getZoneModelId();
        Cfg_Clone_map_Bean clone_mapBean = Cfg_Clone_map_Container.GetInstance().getValueByKey(zoneModelId);
        if (clone_mapBean == null) {
            log.error("本地魂兽森林boss副本配置表中不存在：" + zoneModelId);
            return;
        }
        mapObject.setAutoRemove(false);
        initMapMonsterAndCrystal(mapObject);
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    /**
     * 初始化地图怪物和水晶
     *
     * @param map
     */
    private void initMapMonsterAndCrystal(MapObject map) {
        int zoneModelId = map.getZoneModelId();
        Cfg_Bossnew_SoulBeasts_Bean bean = null;
        for (Boss boss : Manager.bossManager.getLocalSoulAnimalCache().values()) {
            if (boss.getMapID() != map.getZoneModelId()) {
                continue;
            }
            bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(boss.getConfigId());
            if (bean == null) {
                log.error(map.getName() + " 找不到配置的BOSS配置项:" + boss.getConfigId());
                continue;
            }
            boss.setMapUid(map.getId());
            //记录当前BOSS及当前的记录ID值
            MapParam.getSoulAnimalIsland(map).getBossConfigIds().put(bean.getMonsterid(), bean.getID());
            if (boss.getNextTime() > 0) {
                Manager.mapManager.createTombstone(map, boss.getModelId());
                continue;
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
                BossData data = ServerParamUtil.localSoulAnimalDataMap.get(boss.getConfigId());
                if (data != null) {
                    data.setBornTime(TimeUtils.Time());
                    data.setRebornTime(boss.getNextTime());
                } else {
                    log.error("Error! 刷新众神遗迹boss时获取其存库数据data失败，不该发生的，bossId=" + boss.getConfigId());
                }
            } else {
                log.error("众神遗迹Boss刷新怪物生成失败：monsterId=" + boss.getModelId());
            }
        }
        // 创建水晶
        ConcurrentHashMap<Integer, Integer> cristal = BossManager.getBeastSpiritCrystalMap().get(zoneModelId);
        if (cristal != null) {
            BossManager.getInstance().localSoulAnimalManager().createMonster(map, cristal, 1, BossManager.getCrystalTime().get(map.getMapModelId()), BossManager.getCrystalMaxNum().get(zoneModelId));
        }
        //创建兽血水晶
        cristal = BossManager.getBeastlyBloodCrystalMap().get(zoneModelId);
        if (cristal != null) {
            BossManager.getInstance().localSoulAnimalManager().createMonster(map, cristal, 1, BossManager.getBeastlyBloodCrystalBirthTime().get(zoneModelId), BossManager.getBloodCrystalMaxNum().get(zoneModelId));
        }
    }


    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.ACTIVITY_LOCAl_SOULANIMALISLAND.getValue());
        if (remainCount == 0) {
            player.setCamp(map.getMapModelId(), true);
            MessageUtils.notify_player(player, Notify.SHOWBOX, MessageString.DREAMBOSSTIMEOVERSERROR);
        }
        syncCloneInfo(player, map);
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        if(!isQuit){
            Manager.copyMapManager.outZone(player);
        }
    }


    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter acktter) {
        if (!(acktter instanceof Player)) {
            return;
        }
        Player player = (Player) acktter;
        Manager.worldHelpManager.getScript().sendResSynHarmRank(BossTypeConst.SOULANIMAL_BOSS, player, monster);
        Manager.bossManager.manager().syncBossDamageRank(monster);
    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {
        if ((attacker instanceof Robot)){
            return;
        }
        int modelId = monster.getModelId();
        String posKeys = SoulAnimalIslandManagerScript.getIdPosMap(map).get(monster.getId());
        if (!StringUtil.isNullOrEmpty(posKeys)) {
            Map<String, Integer> posHasValueMap = SoulAnimalIslandManagerScript.getPosHasValueMap(map);
            posHasValueMap.remove(posKeys);//怪物死亡， 释放可用坐标
        }
        Integer configId = MapParam.getSoulAnimalIsland(map).getBossConfigIds().get(modelId);
        int recordId = 0;
        if (configId != null) {
            recordId = configId; //怪物数量如何取
        }
        if (recordId < 1) {//精英怪是不再boss列表中的
            //检查守卫是否已经死完
            checkMonsterJinYinAllDle(map);
            return;
        }
        Player player = (Player) attacker;
        Manager.dropManager.deal().specialDropReward(monster, player, SpecialDropDefine.SOULANIMALISLAND_BOSS, true, -1);
        Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(recordId);
        if (bean == null) {
            log.error("幻境boss死亡后获取策划数据失败，bossId=" + recordId);
            return;
        }
        BossData data = ServerParamUtil.localSoulAnimalDataMap.get(recordId);
        data.setDieNum(data.getDieNum() + 1);
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.ACTIVITY_LOCAl_SOULANIMALISLAND.getValue());
        if (remainCount == 0) {
            player.setCamp(map.getMapModelId(), true);
            MessageUtils.notify_player(player, Notify.SHOWBOX, MessageString.DREAMBOSSTIMEOVERSERROR);
        }
        Boss boss = Manager.bossManager.getLocalSoulAnimalCache().get(recordId);
        int rebornTime;
        if (data.getDieNum() > 1) { //非首次死亡的计算
            rebornTime = data.getReBornBaseTime();

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
                log.error("Bossnew_SoulBeasts表字段Limit_time异常!!!");
            }
        } else { //首次死亡，重生时间按表里配置的初始值来
            rebornTime = bean.getInitial_time();
        }

        boss.setNextTime(TimeUtils.Time() + 1000 * rebornTime);
        data.setBornTime(0L);
        data.setReBornBaseTime(rebornTime);
        data.setRebornTime(boss.getNextTime());
        ServerParamUtil.saveSoulAnimalBoss();
        //击杀后推送给玩家当前怪物刷新情况
        Manager.bossManager.manager().addBossKilledRecord(map, monster, player);
        Manager.bossManager.syncSoulAnimalIslandInfo(map, recordId, 4);
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
            case "nextAfterMonsterJinYin": //刷新小怪
                nextAfterMonsterJinYin(mapObject);
                break;
            case "nextAfterBeastSpiritCrystal": //刷新 水晶
                nextAfterBeastSpiritCrystal(mapObject);
                break;
            case "nextAfterBeastlyBloodCrystal"://刷新兽血
                nextAfterBeastlyBloodCrystal(mapObject);
                break;
        }
    }

    @Override
    public void removeMap(MapObject map) {

    }

    @Override
    public int getId() {
        return ScriptEnum.SoulAnimalForestLocalCloneActivityScript;
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
        if (bean.getType() == 2) {
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
            log.info(String.format("玩家{%s}采集{%s}失败!!所在地图已经被销毁!!!",TaskHelp.getPlayerInfo(player),gather.getName()));
            return;
        }
        String posKeys = SoulAnimalIslandManagerScript.getIdPosMap(map).get(gather.getId());
        if (!StringUtil.isNullOrEmpty(posKeys)) {
            SoulAnimalIslandManagerScript.getPosHasValueMap(map).remove(posKeys);//怪物死亡， 释放可用坐标
        }

        int configId = gather.getNo();
        Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(configId);
        if (bean == null) {
            log.info(String.format("玩家{%s}采集{%s}失败!!找不到对应的{%s}表Bossnew_SoulBeasts!!!",TaskHelp.getPlayerInfo(player),gather.getName(),configId));
            return;
        }
        Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(gather.getModelId());
        if (null == gatherCfg) {
            log.error("配置的没有，怎么初始化的！");
            return;
        }
        BossData data = null;
        //只有兽血水晶 才去检查时间
        if (bean.getType() == 2 || bean.getType() == 1) {
            //检查地图是否还有此水晶
            int num  = getGatherNumById(gather, map);
            data = bean.getType() == 2 ? getBestBooldBossData(map, bean) :getCrystalData(map, bean) ;
            //计算下一次出现的时间
            if (num < 1) {
                int rebornTime = data.getReBornBaseTime();
                int lifeTime = (int) ((TimeUtils.Time() - data.getBornTime()) / 1000); //生命值相比于配置的标准值，重生时间做上下浮动 有疑问 todo
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
                    log.error(String.format("表Bossnew_SoulBeast对应的 {%s}不存在！！",configId));
                }
                long nextRebornTime  = TimeUtils.Time() + rebornTime*1000;
                data.setRebornTime(nextRebornTime);
                //boss.setNextTime(rebornTime);
            }
        }else {
            log.info(String.format("魂兽岛采集失败，收到未知类型{%s}",bean.getType()));
            return;
        }
        //已经采集得次数
        long hascount = Manager.countManager.getCount(player, BaseCountType.SOULANIMALFORESTGATHERNUM, bean.getType());
        int max = Global.BossOld2_DailyGather1OpenTimes;
        //采集掉落
        if(bean.getType() == 2){
            max = Global.BossOld2_DailyGather2OpenTimes;
        }
        if(hascount < max){
            //采集掉落
            List<List<Integer>> itemDrops = Manager.dropManager.deal().getItemDrops(player, gatherCfg.getDropId());
            List<Item> list = Item.createItems(itemDrops, 1);
            if (!Manager.backpackManager.manager().addItems(player, list, ItemChangeReason.DropByGatherGet, IDConfigUtil.getLogId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                        MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, list, ItemChangeReason.DropByGatherGet);
                MessageUtils.notify_player(player, Notify.CHAT,MessageString.Bossnew_Soulgather);
            }
        }
        data.setDieNum(data.getDieNum() + 1);
        Manager.countManager.addCount(player, BaseCountType.SOULANIMALFORESTGATHERNUM, bean.getType(), Count.RefreshType.CountType_Day, 1);
        Manager.bossManager.syncSoulAnimalIslandInfo(map, bean.getID(), bean.getType());

    }

    /**
     * 获取兽血水晶 bossData 数据
     *
     * @param mapObject
     * @param bean
     * @return
     */
    private BossData getBestBooldBossData(MapObject mapObject, Cfg_Bossnew_SoulBeasts_Bean bean) {
        ConcurrentHashMap<Integer, BossData> beastlyBloodCrystalBirthTime = BossManager.getBeastlyBloodCrystalBirthTime();
        int zoneModelId = mapObject.getZoneModelId();
        BossData data = beastlyBloodCrystalBirthTime.get(zoneModelId);
        if (data == null) {
            data = new BossData();
            data.setBornTime(TimeUtils.Time());
            data.setBossId(bean.getID());
            data.setDieNum(0);
            data.setReBornBaseTime(bean.getInitial_time());
            data.setRebornTime(data.getBornTime() + data.getReBornBaseTime());
            beastlyBloodCrystalBirthTime.put(zoneModelId, data);
        }
        return data;
    }

    /**
     * 获取兽血水晶 bossData 数据
     *
     * @param mapObject
     * @param bean
     * @return
     */
    private BossData getCrystalData(MapObject mapObject, Cfg_Bossnew_SoulBeasts_Bean bean) {
        ConcurrentHashMap<Integer, BossData> crystal = BossManager.getCrystalTime();
        int zoneModelId = mapObject.getZoneModelId();
        BossData data = crystal.get(zoneModelId);
        if (data == null) {
            data = new BossData();
            data.setBornTime(TimeUtils.Time());
            data.setBossId(bean.getID());
            data.setDieNum(0);
            data.setReBornBaseTime(bean.getInitial_time());
            data.setRebornTime(data.getBornTime() + data.getReBornBaseTime());
            crystal.put(zoneModelId, data);
        }
        return data;
    }

    private int getGatherNumById(Gather gather, MapObject map) {
        int num = 0;
        for (Gather has : map.getCollects().values()) {
            if (has.getModelId() == gather.getModelId()) {
                num = 1;
                break;
            }
        }
        return num;
    }

    @Override
    public void onOutGather(Player player, Gather gather) {
        int gatherBuffId = SoulAnimalIslandManagerScript.getGatherBuffId(player, gather);
        Manager.buffManager.deal().onRemoveBuff(player, gatherBuffId);
    }


    /**
     * 获取精英小怪的刷新时间
     *
     * @param map
     * @return
     */
    private Map<Integer, Integer> getAllThingBornTime(MapObject map) {
        SoulAnimallslandData data = MapParam.getSoulAnimalIsland(map);
        return data.getAllThingBornTimeMap();
    }


    /**
     * 刷新小怪
     *
     * @param mapObject
     */
    private void nextAfterMonsterJinYin(MapObject mapObject) {
        int mapModelId = mapObject.getMapModelId();
        ConcurrentHashMap<Integer, Integer> cristal = BossManager.getSoulAnimalForestMonsterMap().get(mapModelId);
        //计算下一次执行本函数的时间
        BossData bd = BossManager.getSoulAnimalForestMonsterTime().get(mapModelId);
        updateMonster(mapObject, cristal, 3, bd);
        Integer rebornTime = getAllThingBornTime(mapObject).get(bd.getBossId());
        if (rebornTime == null) {
            rebornTime = bd.getReBornBaseTime();
            Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(bd.getBossId());
            if (bean == null) {
                log.error("boss死亡后获取策划数据失败，bossId=" + bd.getBossId());
                rebornTime += 5;
            } else {
                rebornTime += bean.getFloat_time();
                int curOpenServerDay = TimeUtils.getOpenServerDay();
                ReadArray<Integer> arr = Manager.bossManager.manager().getLimitTime(curOpenServerDay, bean.getLimit_time());
                if (arr != null) {
                    if (rebornTime < arr.get(1)) { //低于下限了取下限值
                        rebornTime = arr.get(1);
                    } else if (rebornTime > arr.get(2)) { //高于上限了取上线值
                        rebornTime = arr.get(2);
                    }
                } else {
                    log.error(" 获取下一次刷新时间失败:" + bean.getID());
                }
            }
        }
        bd.setReBornBaseTime(rebornTime);
        bd.setRebornTime(TimeUtils.Time() + 1000 * rebornTime);
        bd.setBornTime(TimeUtils.Time());
        getAllThingBornTime(mapObject).remove(bd.getBossId());
        bd.setDieNum(0);
        Manager.bossManager.syncSoulAnimalIslandInfo(mapObject, bd.getBossId(), 3);
    }

    private void nextAfterBeastSpiritCrystal(MapObject mapObject) {
        ConcurrentHashMap<Integer, Integer> cristal = BossManager.beastSpiritCrystalMap.get(mapObject.getZoneModelId());
        BossData bd = BossManager.getCrystalTime().get(mapObject.getZoneModelId());
        updateMonster(mapObject, cristal, 1, bd);
        bd.setBornTime(TimeUtils.Time());
        bd.setRebornTime(-1);//下一次的刷新时间
        bd.setDieNum(0);
        Manager.bossManager.syncSoulAnimalIslandInfo(mapObject, bd.getBossId(), 1);

    }

    private void nextAfterBeastlyBloodCrystal(MapObject mapObject) {
        int zoneModelId = mapObject.getZoneModelId();
        ConcurrentHashMap<Integer, Integer> cristal = BossManager.getBeastlyBloodCrystalMap().get(zoneModelId);
        BossData bd = BossManager.getBeastlyBloodCrystalBirthTime().get(zoneModelId);
        updateMonster(mapObject, cristal, 2, bd);
        bd.setRebornTime(-1);
        bd.setBornTime(TimeUtils.Time());
        bd.setDieNum(0);
        Manager.bossManager.syncSoulAnimalIslandInfo(mapObject, bd.getBossId(), 2);

    }

    private int inMapNum(MapObject map, int monsterId, int type) {
        int haveNum = 0;
        if (type == 1) {
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

    /**
     * 更新坐标
     *
     * @param mapObject
     * @param cristal
     * @param type
     * @param bd
     */
    private void updateMonster(MapObject mapObject, ConcurrentHashMap<Integer, Integer> cristal, int type, BossData bd) {
        int endNum = bd.getDieNum();
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
            log.debug(mapObject.getName() + "还需要创建" + needNum + " ," + bean.getMonster_name() + "(" + criModelId + ")");
            for (int k = 0; k < needNum; ++k) {
                Position pos = poslist.get(0);
                int randIndex = -1;
                if (poslist.size() > 1) {
                    randIndex = RandomUtils.random(poslist.size());
                    pos = poslist.get(randIndex);
                    poslist.remove(randIndex);
                }
                if (type == 1 || type == 2) {
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

    @Override
    public void birthTime(int type, MapObject mapObject, long birthTime) {
        switch (type) {
            case 1: {
                mapObject.addMapOnceScriptEventTimer(getId(), "nextAfterBeastSpiritCrystal", 0,type);
                log.error(mapObject.getName() + " ," + birthTime + "后刷新 兽灵(神)水晶！");
            }
            break;
            case 2: {
                mapObject.addMapOnceScriptEventTimer(getId(), "nextAfterBeastlyBloodCrystal", 0,type);
                log.error(mapObject.getName() + " ," + birthTime + "后刷新 兽血水晶！");
            }
            break;
            case 3: {
                mapObject.addMapOnceScriptEventTimer(getId(), "nextAfterMonsterJinYin", birthTime);
                log.error(mapObject.getName() + " ," + birthTime + "后刷新 守卫！");
            }
            break;
            default:
                log.error(mapObject.getName() + " 处理了错误的类型！");
                break;
        }
    }

    private boolean checkMonsterJinYinAllDle(MapObject map) {
        BossData data = BossManager.getSoulAnimalForestMonsterTime().get(map.getZoneModelId());
        data.setDieNum(data.getDieNum() + 1);
        int MaxNum = BossManager.getSoulAnimalForestMonsterMaxNum().get(map.getZoneModelId());
        int num = MaxNum - data.getDieNum();
        //计算下一次出现的时间
        if (num < 1) {
            Cfg_Bossnew_SoulBeasts_Bean bean = Cfg_Bossnew_SoulBeasts_Container.GetInstance().getValueByKey(data.getBossId());
            if (bean == null) {
                log.error("幻境boss死亡后获取策划数据失败，bossId=" + data.getBossId());
                return false;
            }
            int rebornTime = data.getReBornBaseTime();
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
                log.error(" 时间");
            }
            getAllThingBornTime(map).put(data.getBossId(), rebornTime);
            log.error(map.getName() + "兽血水晶已经完了， 下一次的时间为:" + rebornTime);
        }
        Manager.bossManager.syncSoulAnimalIslandInfo(map, data.getBossId(), 3);
        return true;
    }


    private void syncCloneInfo(Player p, MapObject mapObject) {
        ResSoulAnimalForestLocalRefreshInfo.Builder msg = ResSoulAnimalForestLocalRefreshInfo.newBuilder();
        BossData bd = BossManager.getCrystalTime().get(mapObject.getZoneModelId());
        int maxNum = BossManager.getCrystalMaxNum().get(mapObject.getZoneModelId());
        forestBossInfo.Builder crystalInfo = forestBossInfo.newBuilder();
        crystalInfo.setBossId(bd.getBossId());
        crystalInfo.setIsFollowed(false);
        crystalInfo.setNum(maxNum - bd.getDieNum());
        crystalInfo.setRefreshTime((int) ((bd.getRebornTime() - TimeUtils.Time()) / 1000));
        crystalInfo.setType(1);
        msg.addBossRefreshList(crystalInfo);

        bd = BossManager.getBeastlyBloodCrystalBirthTime().get(mapObject.getZoneModelId());
        maxNum = BossManager.getBloodCrystalMaxNum().get(mapObject.getZoneModelId());
        forestBossInfo.Builder shouXueInfo = forestBossInfo.newBuilder();
        shouXueInfo.setBossId(bd.getBossId());
        shouXueInfo.setIsFollowed(false);
        shouXueInfo.setNum(maxNum - bd.getDieNum());
        shouXueInfo.setRefreshTime((int) ((bd.getRebornTime() - TimeUtils.Time()) / 1000));
        shouXueInfo.setType(2);
        msg.addBossRefreshList(shouXueInfo);

        for (Boss boss : Manager.bossManager.getLocalSoulAnimalCache().values()) {
            if (boss.getMapID() != mapObject.getZoneModelId()) {
                continue;
            }

            forestBossInfo.Builder bInfo = forestBossInfo.newBuilder();
            bInfo.setBossId(boss.getConfigId());
            int refreshTime = boss.getNextTime() > 0 ? (int) ((boss.getNextTime() - TimeUtils.Time()) / 1000) : 0;
            if (refreshTime < 0) {
                refreshTime = 0;
            }
            bInfo.setRefreshTime(refreshTime);
            bInfo.setIsFollowed(false);
            bInfo.setNum(1);
            bInfo.setType(4);
            msg.addBossRefreshList(bInfo);
        }
        if (p != null) {
            MessageUtils.send_to_player(p, ResSoulAnimalForestLocalRefreshInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        } else {
            MessageUtils.send_to_map(mapObject, ResSoulAnimalForestLocalRefreshInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void syncCrossBossInfo(MapObject mapObject, int configId, int type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canResetBossData(Player player, List<Cfg_Bossnew_SoulBeasts_Bean> beans, boolean all, boolean notify) {
        return false;
    }

    @Override
    public void resetBossData(Player player, boolean all) {

    }

    @Override
    public int canCallBoss(Player player) {
        return 0;
    }

    @Override
    public void callBoss(Player player) {

    }

}
