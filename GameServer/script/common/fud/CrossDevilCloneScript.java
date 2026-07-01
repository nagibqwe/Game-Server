package common.fud;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Cross_devil_Group_Copy_Bean;
import com.data.bean.Cfg_Cross_devil_boss_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.copymap.structs.FightRoomState;
import com.game.guildcrossfud.script.IDevilCloneScript;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import com.game.welfare.struct.RetrieveType;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
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
public class CrossDevilCloneScript implements IDevilCloneScript {


    final Logger logger = LogManager.getLogger(CrossDevilCloneScript.class);

    final int Type = 1; //0=诸界远征 1=魔王缝隙

    final int GroupKey = 1;         //魔王缝隙分组
    final int CityKey = 2;          //魔王缝隙ID
    final int BossKey = 3;          //魔王缝隙boss
    final int CampKey = 4;          //争夺阵营
    final int ScoreKey = 5;         //阵营积分
    final int RoleScoreKey = 6;     //玩家积分
    final int KillKey = 7;          //击杀
    final int CampKillKey = 8;      //阵营击杀
    final int SynBossRankKey = 9;   //boss排名同步
    final int SynCityInfoKey = 10;  //同步魔王缝隙数据
    final int BossRefreshKey = 11;  //魔王刷新时间

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

        long curTime = TimeUtils.Time();
        int groupId = 0;
        int cityId = 0;
        HashMap<Integer, Long> boss = new HashMap<>();
        HashMap<Integer, Long> refreshTime = new HashMap<>();

        List<CommonMessage.CrossAttribute> crossList = (List<CommonMessage.CrossAttribute>) args[1];
        for (CommonMessage.CrossAttribute ca : crossList) {
            if (ca.getType() == 0) {
                groupId = ca.getParam1();
            }
            if (ca.getType() == 1) {
                cityId = ca.getParam1();
            }
            if (ca.getType() == 2) {
                boss.put(ca.getParam1(), 0L);
                Cfg_Cross_devil_boss_Bean bean = CfgManager.getCfg_Cross_devil_boss_Container().getValueByKey(ca.getParam1());
                refreshTime.put(bean.getId(), curTime + bean.getWait_time() * 1000L);
            }
        }

        mapObject.getParams().put(GroupKey, groupId);               //魔王缝隙分组
        mapObject.getParams().put(CityKey, cityId);                 //魔王缝隙ID
        mapObject.getParams().put(BossKey, boss);                   //魔王缝隙boss列表
        mapObject.getParams().put(CampKey, new HashMap<>());        //阵营配置 serverId ->campId
        mapObject.getParams().put(RoleScoreKey, new HashMap<>());   //玩家积分 roleId -> score
        mapObject.getParams().put(ScoreKey, new HashMap<>());       //阵营积分 campId ->score
        mapObject.getParams().put(KillKey, new HashMap<>());        //击杀 roleId -> count
        mapObject.getParams().put(CampKillKey, new HashMap<>());    //阵营击杀 campId -> count
        mapObject.getParams().put(SynBossRankKey, new HashMap<>()); //boss排名数据同步 bossId -> true
        mapObject.getParams().put(SynCityInfoKey, true);
        mapObject.getParams().put(BossRefreshKey, refreshTime);

        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 0, 3000);

        mapObject.addMapLoopScriptEventTimer(getId(), "tickPublic", -1, 500, 4500);

        for (Map.Entry<Integer, Long> entry : refreshTime.entrySet()) {
            mapObject.addMapOnceScriptEventTimer(getId(), "refreshBoss", entry.getValue() - curTime, entry.getKey());
        }

        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTING);

        logger.info("魔王缝隙创建 group={} city={}", groupId, cityId);
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

        Manager.retrieveResManager.getScript().onSendResourceFindChangeToGame(player, RetrieveType.MoWangRuQING.type());
        logger.info("玩家进入魔王缝隙 cityId={} player={}", cityId, player);
    }

    GuildCrossFudMessage.CrossFudBoss.Builder mBoss(int bossId, Monster monster) {
        GuildCrossFudMessage.CrossFudBoss.Builder mess = GuildCrossFudMessage.CrossFudBoss.newBuilder();
        mess.setBossId(bossId);
        mess.setHp(monster.getCurHp());
        mess.setIsDie(monster.getCurHp() <= 0);
        mess.setTime(0);
        return mess;
    }

    GuildCrossFudMessage.CrossFudBoss.Builder mBoss(int bossId, long time) {
        GuildCrossFudMessage.CrossFudBoss.Builder mess = GuildCrossFudMessage.CrossFudBoss.newBuilder();
        mess.setBossId(bossId);
        mess.setHp(0);
        mess.setIsDie(true);
        long wait = time - TimeUtils.Time();
        mess.setTime(wait > 0 ? (int) wait : 0);
        return mess;
    }

    void sendBossList(MapObject map, Player player) {
        ArrayList<Player> players = new ArrayList<>();
        players.add(player);
        sendBossList(map, players);
    }

    /**
     * 同步魔王缝隙boss数据
     *
     * @param players
     */
    void sendBossList(MapObject map, Collection<Player> players) {

        int cityId = map.getParam(CityKey);
        HashMap<Integer, Long> bossIds = map.getParam(BossKey);
        HashMap<Integer, Long> bossRefresh = map.getParam(BossRefreshKey);

        GuildCrossFudMessage.ResCrossFudReport.Builder message = GuildCrossFudMessage.ResCrossFudReport.newBuilder();
        message.setCityId(cityId);
        message.setType(Type);
        for (Map.Entry<Integer, Long> entry : bossIds.entrySet()) {
            Monster monster = map.getMonsters().get(entry.getValue());
            message.addBoss(monster == null ? mBoss(entry.getKey(), bossRefresh.get(entry.getKey())) : mBoss(entry.getKey(), monster));
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

        HashMap<Integer, Long> bossIds = mapObject.getParam(BossKey);

        Map.Entry<Integer, Long> boss = Utils.findOne(bossIds.entrySet(), m -> m.getValue() == monster.getId());
        if (boss == null) {
            return;
        }

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

        GuildCrossFudMessage.CrossFudBoss.Builder mBoss = mBoss(boss.getKey(), monster);

        GuildCrossFudMessage.ResCrossFudBossReport.Builder message = GuildCrossFudMessage.ResCrossFudBossReport.newBuilder();
        message.setType(Type);
        message.setBoss(mBoss);
        message.setCamp(camp.get(max.getKey()));
        damageRanks.forEach(message::addRankList);

        monster.getDamages().forEach((roleId, damage) -> {
            Player player = mapObject.getPlayers().get(roleId);
            if (player == null) {
                return;
            }
            MessageUtils.send_to_player(player, GuildCrossFudMessage.ResCrossFudBossReport.MsgID.eMsgID_VALUE, message.build().toByteArray());
//            logger.info("玩家魔王缝隙boss={}  player={}", boss.getBossId(), player);
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
     * 同步魔王缝隙数据到公共服
     */
    void sendFudInfo2Public(MapObject map) {

        int groupId = map.getParam(GroupKey);
        int cityId = map.getParam(CityKey);
        HashMap<Integer, Long> bossIds = map.getParam(BossKey);
        HashMap<Long, Integer> kill = map.getParam(KillKey);
        HashMap<Integer, Integer> score = map.getParam(ScoreKey);
        HashMap<Long, Integer> roleScore = map.getParam(RoleScoreKey);
        HashMap<Integer, Long> bossRefresh = map.getParam(BossRefreshKey);

        GuildCrossFudMessage.F2PCrossFudInfo.Builder message = GuildCrossFudMessage.F2PCrossFudInfo.newBuilder();
        message.setGroupId(groupId);
        message.setCityId(cityId);
        message.setRoomId(map.getId());
        for (Map.Entry<Integer, Long> entry : bossIds.entrySet()) {
            Monster monster = map.getMonsters().get(entry.getValue());
            message.addBoss(monster == null ? mBoss(entry.getKey(), bossRefresh.get(entry.getKey())) : mBoss(entry.getKey(), monster));
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

        logger.info("同步魔王缝隙信息 groupId={} city={}", groupId, cityId);
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

        HashMap<Integer, Long> bossIds = map.getParam(BossKey);

        Map.Entry<Integer, Long> entry = Utils.findOne(bossIds.entrySet(), o -> o.getValue() == monster.getId());
        if (entry == null) {
            return;
        }

        Cfg_Cross_devil_boss_Bean bean = CfgManager.getCfg_Cross_devil_boss_Container().getValueByKey(entry.getKey());
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

        //魔王缝隙共享掉落玩家
        List<Player> sharePlayers = new ArrayList<>();
        HashMap<Long, Long> campDamage = new HashMap<>();
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
            campDamage.put(player.getId(), e.getValue());
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
        //分配共享掉落道具
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
        //TODO 2021年6月21日 17:12:57 新增伤害排名奖励
        List<Map.Entry<Long, Long>> rankList = campDamage.entrySet().stream().sorted(Map.Entry.comparingByValue((v1, v2) -> v2 - v1 > 0 ? 1 : -1)).collect(Collectors.toList());
        for (ReadArray<Integer> minDrop : bean.getSpecial_drop().getValuees()) {
            if (minDrop.get(0) >  rankList.size()) {
                continue;
            }
            int fromIndex = minDrop.get(0);
            int toIndex = Math.min(minDrop.get(1), rankList.size());

            List<Map.Entry<Long, Long>> minRankList = rankList.subList(fromIndex - 1, toIndex);
            List<List<Integer>> temp = Manager.dropManager.deal().dropExecute(minDrop.get(2));

            for (Map.Entry<Long, Long> roleRank : minRankList) {
                List<Item> rankDrops = Item.createItems(temp, 1);
                List<Item> list = shareHash.getOrDefault(roleRank.getKey(), new ArrayList<>());
                list.addAll(rankDrops);
                shareHash.put(roleRank.getKey(), list);
            }
        }

        HashSet<Integer> secret = devilSecret();

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
            changeCamp(map, player, false);

            List<Item> items = shareHash.get(player.getId());
            if (items == null){
                continue;
            }
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CrossDevilBossOwnGain, logId);

            Item one = Utils.findOne(items, i -> secret.contains(i.getItemModelId()));
            if (one != null) {
                MessageUtils.notify_server(player.getIosession(), Notify.CHAT_SYS_URL_MARQUEE, ChatChannel.CHATCHANNEL_SYSTEM, MessageString.Cross_devil_notice2, player.getName());
            }
        }

        Player killer = (Player) attacker;
        MessageUtils.notify_AllServer(killer.getIosession(), Notify.CHAT_SYS_URL_MARQUEE, MessageString.Cross_devil_notice1,
                killer.getName(),
                killer.getCurServerId(),
                ServerStr.getChatTableName(monster.getName()));

        logger.info("魔王缝隙 boss={} 死亡", monster.getModelId());
    }

    //除魔秘钥
    HashSet<Integer> devilSecret() {
        HashSet<Integer> set = new HashSet<>();
        for (Cfg_Cross_devil_Group_Copy_Bean bean : CfgManager.getCfg_Cross_devil_Group_Copy_Container().getValuees()) {
            int itemId = bean.getOpen_Item().get(0);
            set.add(itemId);
        }
        return set;
    }

    /**
     * 获取魔王缝隙阵营
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

        logger.info("魔王缝隙 boss={} 死亡更新数据", monster.getModelId());
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
            case "refreshBoss":
                refreshBoss(map, (int) params[0]);
                break;
            default:
        }
    }

    void tickPublic(MapObject map) {

        boolean isSyn = map.getParam(SynCityInfoKey);
        if (isSyn) {
            map.getParams().put(SynCityInfoKey, false);
            sendFudInfo2Public(map);
//            logger.info("同步 魔王缝隙数据到公共服+++++++++++");
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

    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.CrossDevilCloneScript;
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
     * 玩家阵营仅用作战斗，，，，和魔王缝隙阵营不是同一个东西
     *
     * @param player
     */
    public void changeCamp(MapObject map, Player player, boolean force) {

        int old = player.getCamp();
        int camp = player.getCamp() & ~(1 << 2);
        if (old != camp || force) {
            player.setCamp(camp, true);
        }

        logger.info("魔王缝隙切换阵营 old={} camp={} player={}", old, camp, player);
    }

    /**
     * 玩家进入副本参数信息
     *
     * @param player
     * @param mapObject
     * @param cross
     */
    @Override
    public void enterCross(Player player, MapObject mapObject, List<CommonMessage.CrossAttribute> cross) {

        HashMap<Integer, Integer> score = mapObject.getParam(ScoreKey);
        HashMap<Integer, Integer> camp = mapObject.getParam(CampKey);
        for (CommonMessage.CrossAttribute ca : cross) {
            if (ca.getType() == 1) {
                camp.put((int) ca.getValue(), ca.getParam1());
                if (!score.containsKey(ca.getParam1())) {
                    score.put(ca.getParam1(), 0);
                }
            }
        }
        logger.info("进入魔王缝隙 player={}", player);
    }

    /**
     * 魔王缝隙刷新怪物
     *
     * @param map
     */
    public void refreshBoss(MapObject map, int bossId) {

        int cityId = map.getParam(CityKey);
        HashMap<Integer, Long> bossIds = map.getParam(BossKey);

        Long id = bossIds.get(bossId);
        Monster monster = map.getMonsters().get(id);
        if (monster != null) {
            return;
        }

        Cfg_Cross_devil_boss_Bean bean = CfgManager.getCfg_Cross_devil_boss_Container().getValueByKey(bossId);
        monster = Manager.monsterManager.createMonster(map, new Position(bean.getPos().get(0), bean.getPos().get(1)), bean.getMonsterId());
        if (monster == null) {
            return;
        }
        bossIds.put(bossId, monster.getId());

        logger.info("魔王缝隙刷新roomId={} cityId={} boss={}", map.getId(), cityId, bossId);
        map.getParams().put(SynCityInfoKey, true);

        sendBossList(map, map.getPlayers().values());
    }

    /**
     * 魔王缝隙关闭
     *
     * @param mapObject
     */
    @Override
    public void fudClose(MapObject mapObject) {
        List<Player> palyers = new ArrayList<>(mapObject.getPlayers().values());
        for (Player player : palyers) {
            Manager.copyMapManager.outZone(player);
            Manager.crossServerManager.sendToPublicPlayerOutCrossFight(player, player.playerCrossData.toFightId, player.playerCrossData.toZoneModelId);
        }
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTREWARDEND);
        mapObject.setStop(true);
        mapObject.setAutoRemove(true);
        int cityId = mapObject.getParam(CityKey);
        logger.info("关闭魔王缝隙 roomId={} city={} ", mapObject.getId(), cityId);
    }


}
