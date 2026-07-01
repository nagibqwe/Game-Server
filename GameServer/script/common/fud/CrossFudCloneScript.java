package common.fud;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_Cross_fudi_boss_Bean;
import com.data.bean.Cfg_Cross_fudi_main_Bean;
import com.data.container.Cfg_Cross_fudi_main_Container;
import com.game.backpack.structs.Item;
import com.game.copymap.structs.FightRoomState;
import com.game.count.structs.Count;
import com.game.guildcrossfud.script.IFudCloneScript;
import com.game.guildcrossfud.struct.FudCalcOwn;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.message.CommonMessage;
import game.message.GuildCrossFudMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Desc TODO
 * @Date 2021/2/18 10:20
 * @Auth ZUncle
 */
public class CrossFudCloneScript implements IFudCloneScript {

    final Logger logger = LogManager.getLogger(CrossFudCloneScript.class);

    final int Type = 0; //0=诸界远征 1=魔王缝隙

    final int GroupKey = 1;         //福地分组
    final int CityKey = 2;          //福地ID
    final int TValueKey = 3;        //天禁值
    final int BossKey = 4;          //福地boss
    final int CampKey = 5;          //争夺阵营
    final int ScoreKey = 6;         //阵营积分
    final int RoleScoreKey = 7;     //玩家积分
    final int KillKey = 8;          //击杀
    final int CampKillKey = 12;     //阵营击杀
    final int SynBossRankKey = 9;   //boss排名同步
    final int SynCityInfoKey = 10;  //同步福地数据
    final int OwnKey = 11;          //福地占领标记

    /**
     * 地图创建初始化
     *
     * @param mapObject
     * @param args
     */
    @Override
    public void onCreate(MapObject mapObject, Object... args) {

        mapObject.setDelTime(0);
        mapObject.setAutoRemove(false);

        int groupId = 0;
        int cityId = 0;
        List<Integer> boss = new ArrayList<>();

        List<CommonMessage.CrossAttribute> crossList = (List<CommonMessage.CrossAttribute>) args[1];
        for (CommonMessage.CrossAttribute ca : crossList) {
            if (ca.getType() == 0) {
                groupId = ca.getParam1();
            }
            if (ca.getType() == 1) {
                cityId = ca.getParam1();
            }
            if (ca.getType() == 2) {
                boss.add(ca.getParam1());
            }
        }

        mapObject.getParams().put(GroupKey, groupId);               //福地分组
        mapObject.getParams().put(CityKey, cityId);                 //福地ID
        mapObject.getParams().put(BossKey, boss);                   //福地boss列表
        mapObject.getParams().put(CampKey, new HashMap<>());        //阵营配置 serverId ->campId
        mapObject.getParams().put(TValueKey, new HashMap<>());      //天禁缓存 roleId -> value
        mapObject.getParams().put(RoleScoreKey, new HashMap<>());   //玩家积分 roleId -> score
        mapObject.getParams().put(ScoreKey, new HashMap<>());       //阵营积分 campId ->score
        mapObject.getParams().put(KillKey, new HashMap<>());        //击杀 roleId -> count
        mapObject.getParams().put(CampKillKey, new HashMap<>());    //阵营击杀 campId -> count
        mapObject.getParams().put(SynBossRankKey, new HashMap<>()); //boss排名数据同步 bossId -> true
        mapObject.getParams().put(SynCityInfoKey, false);

        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 0, 3000);

        mapObject.addMapLoopScriptEventTimer(getId(), "tickPublic", -1, 500, 4500);

        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTING);

        Manager.crossFudManager.getFud().put(mapObject.getId(), mapObject);

        logger.info("跨服福地创建 group={} city={}", groupId, cityId);
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
        return true;
    }

    /**
     * 进入副本地图接口
     *  @param player
     * @param map
     * @param login
     */
    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        int cityId = map.getParam(CityKey);
        HashMap<Long, Integer> roleScore = map.getParam(RoleScoreKey);
        if (!roleScore.containsKey(player.getId())) {
            roleScore.put(player.getId(), 0);
        }
        changeCamp(map, player, true);

        map.getParams().put(SynCityInfoKey, true);

        sendBossList(map, player);
        logger.info("玩家进入跨服福地 cityId={} player={}", cityId, player);
    }

    GuildCrossFudMessage.CrossFudBoss.Builder mBoss(Monster monster) {
        GuildCrossFudMessage.CrossFudBoss.Builder mess = GuildCrossFudMessage.CrossFudBoss.newBuilder();
        mess.setBossId(monster.getModelId());
        mess.setHp(monster.getCurHp());
        mess.setIsDie(monster.getCurHp() <= 0);
        return mess;
    }

    GuildCrossFudMessage.CrossFudBoss.Builder mBoss(int bossId) {
        GuildCrossFudMessage.CrossFudBoss.Builder mess = GuildCrossFudMessage.CrossFudBoss.newBuilder();
        mess.setBossId(bossId);
        mess.setHp(0);
        mess.setIsDie(true);
        return mess;
    }

    void sendBossList(MapObject map, Player player) {
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        sendBossList(map, players);
    }

    /**
     * 同步福地boss数据
     *
     * @param players
     */
    void sendBossList(MapObject map, Collection<Player> players) {

        int cityId = map.getParam(CityKey);
        List<Integer> bossIds = map.getParam(BossKey);

        HashMap<Integer, Monster> bossMap = new HashMap<>();
        for (Monster monster : map.getMonsters().values()) {
            bossMap.put(monster.getModelId(), monster);
        }
        GuildCrossFudMessage.ResCrossFudReport.Builder message = GuildCrossFudMessage.ResCrossFudReport.newBuilder();
        message.setCityId(cityId);
        message.setType(Type);
        for (int bossId : bossIds) {
            Monster monster = bossMap.get(bossId);
            message.addBoss(monster == null ? mBoss(bossId) : mBoss(monster));
        }
        for (Player player : players) {
            MessageUtils.send_to_player(player, GuildCrossFudMessage.ResCrossFudReport.MsgID.eMsgID_VALUE, message.build().toByteArray());
        }
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

        map.getParams().put(SynCityInfoKey, true);
    }

    /**
     * 伤害接口
     *
     * @param mapObject
     * @param monster
     * @param _damage
     * @param attacker
     */
    @Override
    public void onDamage(MapObject mapObject, Monster monster, long _damage, Fighter attacker) {

        HashMap<Long, Monster> flag = mapObject.getParam(SynBossRankKey);
        flag.put(monster.getId(), monster);

        mapObject.getParams().put(SynCityInfoKey, true);

    }

    /**
     * 广播boss伤害排名
     *
     * @param mapObject
     * @param monster
     */
    public void sendBossDamageInfo(MapObject mapObject, Monster monster) {
        HashMap<Integer, Integer> score = mapObject.getParam(ScoreKey);
        if (score.isEmpty()) {
            return;
        }

        HashMap<Integer, GuildCrossFudMessage.CrossFudCamp.Builder> camp = new HashMap<>();
        score.forEach((campId, v) -> camp.put(campId, mCamp(mapObject, campId)));

        ArrayList<GuildCrossFudMessage.CrossFudRole.Builder> damageRanks = new ArrayList<>();

        HashMap<Integer, Long> damageScoreTotal = new HashMap<>();
        score.forEach((campId, v) -> damageScoreTotal.put(campId, 0L));

        monster.getDamages().forEach((roleId, damage) -> {
            Player player = mapObject.getPlayers().get(roleId);
            if (player == null) {
                return;
            }
            GuildCrossFudMessage.CrossFudRole.Builder mRole = mRole(player);
            mRole.setDamage(damage);
            damageRanks.add(mRole);

            int campId = gainCamp(mapObject, player);
            long old = damageScoreTotal.getOrDefault(campId, 0L);
            damageScoreTotal.put(campId, old + damage);
        });

        //伤害排序
        damageRanks.sort(Comparator.comparingLong(GuildCrossFudMessage.CrossFudRole.Builder::getDamage).reversed());
        int i = 1;
        for (GuildCrossFudMessage.CrossFudRole.Builder m : damageRanks) {
            m.setRank(i++);
        }

        //归属阵营
        Map.Entry<Integer, Long> max = damageScoreTotal.entrySet().stream().max((a, b) -> a.getValue() > b.getValue() ? 1 : -1).get();

        GuildCrossFudMessage.CrossFudBoss.Builder boss = mBoss(monster);

        GuildCrossFudMessage.ResCrossFudBossReport.Builder message = GuildCrossFudMessage.ResCrossFudBossReport.newBuilder();
        message.setType(Type);
        message.setBoss(boss);
        message.setCamp(camp.get(max.getKey()));
        damageRanks.forEach(message::addRankList);

        monster.getDamages().forEach((roleId, damage) -> {
            Player player = mapObject.getPlayers().get(roleId);
            if (player == null) {
                return;
            }
            MessageUtils.send_to_player(player, GuildCrossFudMessage.ResCrossFudBossReport.MsgID.eMsgID_VALUE, message.build().toByteArray());
//            logger.info("玩家跨服福地boss={}  player={}", boss.getBossId(), player);
        });
    }

    GuildCrossFudMessage.CrossFudRole.Builder mRole(Player player) {
        GuildCrossFudMessage.CrossFudRole.Builder m = GuildCrossFudMessage.CrossFudRole.newBuilder();
        m.setPlayerId(player.getId());
        m.setName(player.getName());
        m.setInFud(true);
        return m;
    }

    GuildCrossFudMessage.CrossFudRole.Builder mRole(long player) {
        GuildCrossFudMessage.CrossFudRole.Builder m = GuildCrossFudMessage.CrossFudRole.newBuilder();
        m.setPlayerId(player);
        return m;
    }

    GuildCrossFudMessage.CrossFudCamp.Builder mCamp(MapObject map, int campId) {
        HashMap<Integer, Integer> score = map.getParam(ScoreKey);
        HashMap<Integer, Integer> camp = map.getParam(CampKey);

        GuildCrossFudMessage.CrossFudCamp.Builder m = GuildCrossFudMessage.CrossFudCamp.newBuilder();
        m.setCamp(campId);
        m.setScore(score.getOrDefault(campId, 0));
        camp.forEach((serverId, cc) -> {
            if (cc == campId) {
                m.addServerId(serverId);
            }
        });
        return m;
    }

    /**
     * 同步福地数据到公共服
     */
    void sendFudInfo2Public(MapObject map) {

        HashMap<Integer, Monster> bossMap = new HashMap<>();
        for (Monster monster : map.getMonsters().values()) {
            bossMap.put(monster.getModelId(), monster);
        }

        int groupId = map.getParam(GroupKey);
        int cityId = map.getParam(CityKey);
        List<Integer> bossIds = map.getParam(BossKey);
        HashMap<Long, Integer> kill = map.getParam(KillKey);
        HashMap<Integer, Integer> score = map.getParam(ScoreKey);
        HashMap<Long, Integer> roleScore = map.getParam(RoleScoreKey);

        GuildCrossFudMessage.F2PCrossFudInfo.Builder message = GuildCrossFudMessage.F2PCrossFudInfo.newBuilder();
        message.setGroupId(groupId);
        message.setCityId(cityId);
        message.setRoomId(map.getId());
        for (int bossId : bossIds) {
            Monster monster = bossMap.get(bossId);
            message.addBoss(monster == null ? mBoss(bossId) : mBoss(monster));
        }
        for (Map.Entry<Long, Integer> e : roleScore.entrySet()) {
            Player player = map.getPlayer(e.getKey());
            GuildCrossFudMessage.CrossFudRole.Builder mRole = player == null ? mRole(e.getKey()) : mRole(player);
            mRole.setScore(e.getValue());
            mRole.setKill(kill.getOrDefault(e.getKey(), 0));
            message.addRole(mRole);
        }
        for (Map.Entry<Integer, Integer> e : score.entrySet()) {
            GuildCrossFudMessage.CrossFudCamp.Builder mCamp = mCamp(map, e.getKey());
            message.addCamp(mCamp);
        }
        MessageUtils.send_to_public(GuildCrossFudMessage.F2PCrossFudInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());

        //logger.info("同步跨服福地信息 groupId={} city={}", groupId, cityId);
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

        Cfg_Cross_fudi_boss_Bean bean = CfgManager.getCfg_Cross_fudi_boss_Container().getValueByKey(monster.getModelId());
        HashMap<Long, Integer> tv = map.getParam(TValueKey);
        //统计击杀
        HashMap<Long, Integer> kill = map.getParam(KillKey);
        int oldKill = kill.getOrDefault(attacker.getId(), 0);
        kill.put(attacker.getId(), oldKill + 1);
        //阵营击杀
        int campId = gainCamp(map, (Player) attacker);
        HashMap<Integer, Integer> campKill = map.getParam(CampKillKey);
        int oldCampKill = campKill.getOrDefault(campId, 0);
        campKill.put(campId, oldCampKill + 1);

        //阵营伤害统计
        HashMap<Integer, Long> damageScoreTotal = new HashMap<>();
        monster.getDamages().forEach((roleId, damage) -> {
            Player player = map.getPlayers().get(roleId);
            if (player == null) {
                return;
            }
            int iCampId = gainCamp(map, player);
            long old = damageScoreTotal.getOrDefault(iCampId, 0L);
            damageScoreTotal.put(iCampId, old + damage);
        });
        //阵营积分最大为归属地
        Map.Entry<Integer, Long> max = damageScoreTotal.entrySet().stream().max((a, b) -> a.getValue() > b.getValue() ? 1 : -1).get();
        //添加阵营积分
        HashMap<Integer, Integer> score = map.getParam(ScoreKey);
        int oldCampScore = score.getOrDefault(max.getKey(), 0);
        score.put(max.getKey(), oldCampScore + bean.getScore());
        //个人积分分配
        HashMap<Long, Integer> roleScore = map.getParam(RoleScoreKey);
        int remainPersonalScore = bean.getGeren_score();
        long total = sum(damageScoreTotal.values());
        for (Map.Entry<Long, Long> e : monster.getDamages().entrySet()) {
            if (remainPersonalScore <= 0) {
                break;
            }
            Player player = map.getPlayers().get(e.getKey());
            if (player == null) {
                continue;
            }
            //按照伤害比例分配个人积分
            int cake = (int) Math.ceil(bean.getGeren_score() * (e.getValue().doubleValue() / total));
            cake = Math.min(remainPersonalScore, cake);
            remainPersonalScore = remainPersonalScore - cake;
            int old = roleScore.getOrDefault(player.getId(), 0);
            roleScore.put(player.getId(), old + cake);

        }
        //福地共享掉落玩家
        List<Player> sharePlayers = new ArrayList<>();
        for (Map.Entry<Long, Long> e : monster.getDamages().entrySet()) {
            Player player = map.getPlayers().get(e.getKey());
            if (player == null) {
                continue;
            }
            campId = gainCamp(map, player);
            if (campId != max.getKey()) {
                continue;
            }
            sharePlayers.add(player);
        }
        //计算共享掉落包
        List<List<Integer>> drops = new ArrayList<>();
        for (int dropId : bean.getDrop().getValue()) {
            List<List<Integer>> temp = Manager.dropManager.deal().dropExecute(dropId);
            drops.addAll(temp);

        }
        List<Item> shareDrops = Item.createItems(drops, 1);

        List<Player> shares = new ArrayList<>();
        HashMap<Long, List<Item>> shareHash = new HashMap<>();
        for (Item item : shareDrops) {
            if (shares.isEmpty()) {
                shares.addAll(sharePlayers);
            }
            int random = RandomUtils.random(0, shares.size() - 1);
            Player share = shares.remove(random);
            List<Item> list = shareHash.getOrDefault(share.getId(), new ArrayList<>());
            list.add(item);
            shareHash.put(share.getId(), list);
        }

        int cityId = map.getParam(CityKey);
        int groupId = map.getParam(GroupKey);
        GuildCrossFudMessage.F2PKillFudBoss.Builder mKill = GuildCrossFudMessage.F2PKillFudBoss.newBuilder();
        mKill.setGroupId(groupId);
        mKill.setRoomId(map.getId());
        mKill.setCityId(cityId);
        mKill.setBoss(bean.getId());

        //发放boss 归属掉落掉落奖励
        long logId = IDConfigUtil.getLogId();
        for (Map.Entry<Long, Long> e : monster.getDamages().entrySet()) {
            Player player = map.getPlayers().get(e.getKey());
            if (player == null) {
                continue;
            }
            campId = gainCamp(map, player);
            if (campId != max.getKey()) {
                continue;
            }
            mKill.addKiller(player.getId());
            //增加天禁值
            int oldTv = tv.getOrDefault(player.getId(), 0);
            tv.put(player.getId(), oldTv + bean.getRage());

            changeCamp(map, player, false);

            List<List<Integer>> itemDrops = new ArrayList<>();
            List<Integer> coin = new ArrayList<>();
            coin.add(bean.getPersonal_score().get(0));
            coin.add(bean.getPersonal_score().get(1));
            itemDrops.add(coin);
            List<Item> items = Item.createItems(itemDrops, 1);

            List<Item> shareItems = shareHash.get(player.getId());
            if (shareItems != null) {
                items.addAll(shareItems);
            }
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CrossFudBossOwnGain, logId);
            //记录boss类型
            Manager.crossServerManager.crossFightdeal().sendF2GSynRoleFVInfo(player, FunctionVariable.KillfudiBoss_type, 1, bean.getType(), map.getMapModelId(), Count.RefreshType.CountType_Day.getValue(), 1);
        }

        MessageUtils.send_to_public(GuildCrossFudMessage.F2PKillFudBoss.MsgID.eMsgID_VALUE, mKill.build().toByteArray());

        logger.info("跨服福地 boss={} 死亡", monster.getModelId());
    }


    Long sum(Collection<Long> c) {
        Long sum = 0L;
        for (Long m : c) {
            sum += m;
        }
        return sum;
    }

    /**
     * 获取福地阵营
     *
     * @param map
     * @param attacker
     * @return
     */
    int gainCamp(MapObject map, Player attacker) {
        HashMap<Integer, Integer> campList = map.getParam(CampKey);
        return campList.get(attacker.getCurServerId());
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

        map.getParams().put(SynCityInfoKey, true);

        sendBossList(map, map.getPlayers().values());

        int cityId = map.getParam(CityKey);

        HashMap<Integer, Integer> score = map.getParam(ScoreKey);
        HashMap<Long, Integer> roleScore = map.getParam(RoleScoreKey);
        HashMap<Integer, Integer> campKill = map.getParam(CampKillKey);

        Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(cityId);

        for (Map.Entry<Integer, Integer> e : score.entrySet()) {
            //达到占领积分
            if (e.getValue() >= bean.getOccupy_personal_score()) {
                owner(map, e.getKey());
            }
        }

        if (map.getMonsters().isEmpty()) {
            //统计玩家总积分
            HashMap<Integer, Integer> roleTotal = new HashMap<>();
            for (Map.Entry<Long, Integer> e : roleScore.entrySet()) {
                Player player = map.getPlayers().get(e.getKey());
                if (player == null) {
                    continue;
                }
                int campId = gainCamp(map, player);
                int total = roleTotal.getOrDefault(campId, 0);
                roleTotal.put(campId, total + roleScore.getOrDefault(player.getId(), 0));
            }
            List<FudCalcOwn> members = new ArrayList<>();
            for (Map.Entry<Integer, Integer> e : score.entrySet()) {
                FudCalcOwn own = new FudCalcOwn();
                own.setCamp(e.getKey());
                own.setScore(e.getValue());
                own.setKill(-campKill.getOrDefault(e.getKey(), 0));
                own.setRoleTotalScore(roleTotal.getOrDefault(e.getKey(), 0));
                members.add(own);
            }
            /**
             * 1.	当前占领分数最高。
             * 2.	在当前福地内击杀首领数量最少。
             * 3.	当前轮数福地内该服务器的个人积分总和排名最高。
             * 4.	服务器编号最高（更晚开服）。
             */
            members.sort(Comparator.comparingInt(FudCalcOwn::getScore)
                    .thenComparingInt(FudCalcOwn::getKill)
                    .thenComparingInt(FudCalcOwn::getRoleTotalScore)
                    .thenComparingInt(FudCalcOwn::getServerId).reversed());
            FudCalcOwn fudCalcOwn = members.get(0);
            owner(map, fudCalcOwn.getCamp());
        }

        logger.info("跨服福地 boss={} 死亡更新数据", monster.getModelId());
    }

    /**
     * 福地占领结算
     *
     * @param map
     * @param camp
     */
    void owner(MapObject map, int camp) {

        if (map.getParams().containsKey(OwnKey)) {
            return;
        }
        map.getParams().put(OwnKey, camp);

        sendFudInfo2Public(map);

        int cityId = map.getParam(CityKey);
        int groupId = map.getParam(GroupKey);

        HashMap<Long, Integer> kill = map.getParam(KillKey);

        List<Map.Entry<Long, Integer>> killRank = kill.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue((v1, v2) -> v2 - v1))
                .collect(Collectors.toList());
        //击杀排名第一
        Player player = map.getPlayer(killRank.get(0).getKey());
        GuildCrossFudMessage.CrossFudRole.Builder firstKill = player == null ? mRole(killRank.get(0).getKey()) : mRole(player);
        if (player != null) {
            firstKill.setRank(1);
            firstKill.setCareer(player.getCareer());
            firstKill.setFacade(MapUtils.getFacade(player));
        }

        //同步福地占领消息
        GuildCrossFudMessage.F2PCrossFudGain.Builder message = GuildCrossFudMessage.F2PCrossFudGain.newBuilder();
        message.setCityId(cityId);
        message.setGroupId(groupId);
        message.setRoomId(map.getId());
        message.setCamp(mCamp(map, camp));
        message.setFirst(firstKill);
        MessageUtils.send_to_public(GuildCrossFudMessage.F2PCrossFudGain.MsgID.eMsgID_VALUE, message.build().toByteArray());

        logger.info("福地占领 group={} city={} camp={} ", groupId, cityId, camp);

        int serverId = getServerId(map, camp);
        int score = 0;
        HashMap<Integer, Integer> campScores = map.getParam(ScoreKey);
        if (campScores != null) {
            score = campScores.get(camp);
        }
        Cfg_Cross_fudi_main_Bean bean = Cfg_Cross_fudi_main_Container.GetInstance().getValueByKey(cityId);
        Manager.biManager.getScript().biWorldExpedition(
                0,
                cityId,
                bean == null ? 0 : bean.getPosition(),
                0,
                score,
                serverId);
    }

    private int getServerId(MapObject map, int camp) {
        HashMap<Integer, Integer> camps = map.getParam(CampKey);
        if (camps != null) {
            for (Map.Entry<Integer, Integer> e : camps.entrySet()) {
                if (e.getValue() == camp) {
                    return e.getKey();
                }
            }
        }
        return 0;
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

        switch (method) {
            case "tick":
                tick(map);
                break;
            case "tickPublic":
                tickPublic(map);
                break;
            default:
        }
    }

    void tickPublic(MapObject map) {

        boolean isSyn = map.getParam(SynCityInfoKey);
        if (isSyn) {
            map.getParams().put(SynCityInfoKey, false);
            sendFudInfo2Public(map);
//            logger.info("同步 福地数据到公共服+++++++++++");
        }
    }

    void tick(MapObject map) {

        HashMap<Long, Monster> flag = map.getParam(SynBossRankKey);

        for (Monster monster : flag.values()) {
            sendBossDamageInfo(map, monster);
        }
        if (flag.size() > 0) {
//            logger.info("同步boss 伤害排名+++++++++++");
        }
        flag.clear();

    }

    /**
     * 删除地图调用接口
     *
     * @param map
     */
    @Override
    public void removeMap(MapObject map) {

        Manager.crossFudManager.getFud().remove(map.getId());

    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.CrossFudCloneScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }


    /**
     * 切换阵营
     * 玩家阵营仅用作战斗，，，，和福地阵营不是同一个东西
     *
     * @param player
     */
    public void changeCamp(MapObject map, Player player, boolean force) {

        HashMap<Long, Integer> tv = map.getParam(TValueKey);
        int cityId = map.getParam(CityKey);

        Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(cityId);
        int old = player.getCamp();
        int camp = 0;
        if (tv.getOrDefault(player.getId(), 0) >= bean.getMax_tianjin()) {
            camp = player.getCamp() | 1 << 2;
        } else {
            camp = player.getCamp() & ~(1 << 2);
        }
        if (old != camp || force) {
            player.setCamp(camp, true);
        }
        logger.info("跨服福地切换阵营 old={} camp={} player={}", old, camp, player);
    }

    /**
     * 玩家进入跨服副本参数信息
     *
     * @param player
     * @param mapObject
     * @param cross
     */
    @Override
    public void enterCross(Player player, MapObject mapObject, List<CommonMessage.CrossAttribute> cross) {

        HashMap<Integer, Integer> score = mapObject.getParam(ScoreKey);
        HashMap<Integer, Integer> camp = mapObject.getParam(CampKey);
        HashMap<Long, Integer> tv = mapObject.getParam(TValueKey);
        for (CommonMessage.CrossAttribute ca : cross) {
            if (ca.getType() == 0) {
                tv.put(player.getId(), ca.getParam1());
            }
            if (ca.getType() == 1) {
                camp.put((int) ca.getValue(), ca.getParam1());
                if (!score.containsKey(ca.getParam1())) {
                    score.put(ca.getParam1(), 0);
                }
            }
        }
        logger.info("进入跨服福地 player={}", player);
    }

    /**
     * 福地刷新怪物
     *
     * @param map
     */
    @Override
    public void refreshBoss(MapObject map) {

        //开始重置上一轮数据
        map.getParams().remove(OwnKey);                       //移除占领标记
        map.getParams().put(KillKey, new HashMap<>());        //击杀 roleId -> count
        map.getParams().put(CampKillKey, new HashMap<>());    //阵营击杀 campId -> count

        HashMap<Long, Integer> roleHash = map.getParam(RoleScoreKey); //玩家积分 roleId -> score
        Set<Long> roles = roleHash.keySet();
        for (long roleId : roles) {
            roleHash.put(roleId, 0);
        }
        HashMap<Integer, Integer> campHash = map.getParam(ScoreKey); //阵营积分 campId ->score
        Set<Integer> camps = campHash.keySet();
        for (int camp : camps) {
            campHash.put(camp, 0);
        }

        int cityId = map.getParam(CityKey);
        List<Integer> bossIds = map.getParam(BossKey);
        HashMap<Integer, Monster> bossMap = new HashMap<>();
        for (Monster monster : map.getMonsters().values()) {
            bossMap.put(monster.getModelId(), monster);
        }
        for (int bossId : bossIds) {
            if (bossMap.containsKey(bossId)) {
                continue;
            }
            Cfg_Cross_fudi_boss_Bean bean = CfgManager.getCfg_Cross_fudi_boss_Container().getValueByKey(bossId);
            Monster monster = Manager.monsterManager.createMonster(map, new Position(bean.getPos().get(0), bean.getPos().get(1)), bossId);
            if (monster == null) {
                continue;
            }
            logger.info("福地刷新roomId={} cityId={} boss={}", map.getId(), cityId, bossId);
        }
        map.getParams().put(SynCityInfoKey, true);

        sendBossList(map, map.getPlayers().values());
    }

    /**
     * 福地关闭
     *
     * @param mapObject
     */
    @Override
    public void fudClose(MapObject mapObject) {
        List<Player> palyers = new ArrayList<>(mapObject.getPlayers().values());
        for (Player player : palyers) {
            Manager.copyMapManager.manager().onReqCopyMapOut(player);
        }
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTREWARDEND);
        mapObject.setStop(true);
        mapObject.setAutoRemove(true);
        int cityId = mapObject.getParam(CityKey);
        logger.info("关闭跨服福地 roomId={} city={} ", mapObject.getId(), cityId);
    }

    /**
     * 活动2-8点 踢出玩家
     *
     * @param mapObject
     */
    @Override
    public void fudTickOut(MapObject mapObject) {
        List<Player> palyers = new ArrayList<>(mapObject.getPlayers().values());
        for (Player player : palyers) {
            Manager.copyMapManager.manager().onReqCopyMapOut(player);
        }
        int cityId = mapObject.getParam(CityKey);
        logger.info("跨服福地2-8点踢人 roomId={} city={} ", mapObject.getId(), cityId);
    }

    /**
     * 刷新天禁值
     *
     * @param mapObject
     */
    @Override
    public void refreshTv(MapObject mapObject) {

        HashMap<Long, Integer> tv = mapObject.getParam(TValueKey);
        tv.replaceAll((i, v) -> 0);

        for (Player player : mapObject.getPlayers().values()) {
            changeCamp(mapObject, player, true);
        }

    }
}
