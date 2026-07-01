package common.fud;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Cross_Alien_Boss_Bean;
import com.data.bean.Cfg_Cross_Alien_Connect_Bean;
import com.data.bean.Cfg_Item_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemTypeConst;
import com.game.boss.struct.Boss;
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.copymap.structs.AlienCopyData;
import com.game.copymap.structs.FightRoomState;
import com.game.copymap.structs.ZoneCache;
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
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.AlienBossMessage;
import game.message.CommonMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Desc TODO
 * @Date 2021/11/23 11:19
 * @Auth ZUncle
 */
public class CrossAlienScript implements IDevilCloneScript {

    final Logger logger = LogManager.getLogger(CrossAlienScript.class);

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.CrossAlienScript;
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
     * 玩家进入跨服副本参数信息
     *
     * @param player
     * @param mapObject
     * @param cross
     */
    @Override
    public void enterCross(Player player, MapObject mapObject, List<CommonMessage.CrossAttribute> cross) {

    }

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

        ZoneCache zone = mapObject.getZone();

        AlienCopyData alien = new AlienCopyData();
        alien.setZoneId(zone.getZoneId());
        alien.setLevel(zone.getLevel());
        mapObject.setZone(alien);

        int total = 0;
        long curTime = TimeUtils.Time();
        List<CommonMessage.CrossAttribute> crossList = (List<CommonMessage.CrossAttribute>) args[1];
        for (CommonMessage.CrossAttribute ca : crossList) {
            if (ca.getType() == 0) {
                alien.setGroup(ca.getParam1());
            }
            if (ca.getType() == 1) {
                alien.setCity(ca.getParam1());
            }
            if (ca.getType() == 2) {

                Cfg_Cross_Alien_Boss_Bean bean = CfgManager.getCfg_Cross_Alien_Boss_Container().getValueByKey(ca.getParam1());
                Boss boss = new Boss();
                boss.setModelId(bean.getId());
                boss.setNextTime(curTime + bean.getWaitTime() * 1000);
                total += bean.getPoint();
                alien.getBoss().put(ca.getParam1(), boss);
            }
            if (ca.getType() == 3) {
                alien.getServer().put(ca.getParam1(), 0);
            }
        }
        alien.setTotalScore(total);

        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 0, 1500);

        mapObject.addMapLoopScriptEventTimer(getId(), "tickPublic", -1, 500, 3000);

        for (Boss boss : alien.getBoss().values()) {
            mapObject.addMapOnceScriptEventTimer(getId(), "refreshBoss", boss.getNextTime() - curTime, boss);
        }

        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTING);

        Manager.crossFudManager.getFud().put(mapObject.getId(), mapObject);

        logger.info("混沌虚空创建 alien={} ", alien);

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
        return false;
    }

    /**
     * 进入副本地图接口
     *
     * @param player
     * @param map
     * @param login
     */
    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        sendBossList(map, player);

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
     * @param map
     * @param monster
     * @param damage
     * @param attacker
     */
    @Override
    public void onDamage(MapObject map, Monster monster, long damage, Fighter attacker) {

        AlienCopyData alien = map.getZone();

        alien.getBeAttack().add(monster);

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

        Player killer = (Player) attacker;

        AlienCopyData alien = map.getZone();
        alien.getState().add(monster);

        Cfg_Cross_Alien_Connect_Bean city = CfgManager.getCfg_Cross_Alien_Connect_Container().getValueByKey(alien.getCity());

        Boss boss = alien.getMonster().get(monster.getId());
        Cfg_Cross_Alien_Boss_Bean bean = CfgManager.getCfg_Cross_Alien_Boss_Container().getValueByKey(boss.getModelId());

        HashMap<Integer, Long> damageScoreTotal = new HashMap<>();
        alien.getServer().keySet().forEach(k -> damageScoreTotal.put(k, 0L));

        monster.getDamages().forEach((roleId, dm) -> {
            Player player = map.getPlayers().get(roleId);
            if (player == null) {
                return;
            }
            long old = damageScoreTotal.getOrDefault(player.getCamp(), 0L);
            damageScoreTotal.put(player.getCamp(), old + dm);
        });
        //归属阵营
        Map.Entry<Integer, Long> kill = damageScoreTotal.entrySet().stream().max((a, b) -> a.getValue() > b.getValue() ? 1 : -1).get();
        alien.getBossKill().put(boss.getModelId(), kill.getKey());

        //同步Boss死亡消息到公共服
        AlienBossMessage.F2PCrossAlienBossDie.Builder message = AlienBossMessage.F2PCrossAlienBossDie.newBuilder();
        message.setGroupId(alien.getGroup());
        message.setCityId(alien.getCity());
        message.setRoomId(map.getId());
        message.setBoss(builder(alien, boss, monster));
        message.setServerKill(kill.getKey());
        MessageUtils.send_to_public(AlienBossMessage.F2PCrossAlienBossDie.MsgID.eMsgID_VALUE, message.build().toByteArray());

        //添加击杀积分
        int score = alien.getServer().getOrDefault(kill.getKey(), 0);
        alien.getServer().put(kill.getKey(), score + bean.getPoint());

        // 积分超过一半直接占领
        if ((score + bean.getPoint()) * 2 >= alien.getTotalScore() && alien.getExtraServerId() <= 0) {
            alien.setExtraServerId(kill.getKey());

            MessageUtils.notify_AllServer(killer.getIosession(), Notify.CHAT_SYS_URL_MARQUEE, MessageString.Cross_Alien_Gem_Belong,
                    killer.getCurServerId(),
                    ServerStr.getChatTableName(city.getCopyName()));
        }
        sendBossList(map, map.getPlayers().values());

        //混沌虚空共享掉落玩家
        List<Player> sharePlayers = new ArrayList<>();
        HashMap<Long, Long> campDamage = new HashMap<>();
        for (Map.Entry<Long, Long> e : monster.getDamages().entrySet()) {
            Player player = map.getPlayers().get(e.getKey());
            if (player == null) {
                continue;
            }
            if (player.getCamp() != kill.getKey()) {
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
        //TODO 伤害排名奖励
        List<Map.Entry<Long, Long>> rankList = campDamage.entrySet().stream().sorted(Map.Entry.comparingByValue((v1, v2) -> v2 - v1 > 0 ? 1 : -1)).collect(Collectors.toList());
        for (ReadArray<Integer> minDrop : bean.getSpecialDrop().getValuees()) {
            if (minDrop.get(0) > rankList.size()) {
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

        //发放boss 归属掉落掉落奖励
        long logId = IDConfigUtil.getLogId();
        for (Map.Entry<Long, Long> e : monster.getDamages().entrySet()) {
            Player player = map.getPlayers().get(e.getKey());
            if (player == null) {
                continue;
            }
            if (player.getCamp() != kill.getKey()) {
                continue;
            }

            List<Item> items = shareHash.get(player.getId());
            if (items == null) {
                continue;
            }
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CrossFudBossOwnGain, logId);

            for (Item show : items) {
                Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(show.getItemModelId());
                //幻装广播
                if (itemBean.getType() == ItemTypeConst.UNREAL_EQUIP || itemBean.getType() == ItemTypeConst.UNREAL_EQUIP_CARD) {
                    MessageUtils.notify_server(killer.getIosession(), Notify.CHAT_SYS_URL_MARQUEE, ChatChannel.CHATCHANNEL_SYSTEM, MessageString.Cross_Alien_Gem_Get_Treasure,
                            player.getName(),
                            ServerStr.getChatTableName(city.getCopyName()),
                            ServerStr.getChatTableName(monster.getName()),
                            ServerStr.getChatTableName(itemBean.getName())
                    );
                }
            }
        }
        logger.info("混沌虚空 boss={} 死亡", monster.getModelId());
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
        switch (method) {
            case "tick":
                tick(map);
                break;
            case "tickPublic":
                tickPublic(map);
                break;
            case "refreshBoss":
                refreshBoss(map, (Boss) params[0]);
                break;
            default:
        }
    }

    private void tickPublic(MapObject map) {

        AlienCopyData alien = map.getZone();
        List<Monster> monsters = new ArrayList<>(alien.getState());
        if (monsters.isEmpty()) {
            return;
        }

        alien.getState().clear();

        AlienBossMessage.F2PCrossAlienBoss.Builder message = AlienBossMessage.F2PCrossAlienBoss.newBuilder();
        message.setGroupId(alien.getGroup());
        message.setCityId(alien.getCity());
        message.setRoomId(map.getId());

        for (Boss boss : alien.getBoss().values()) {
            Monster monster = map.getMonsters().get(boss.getId());
            message.addBoss(builder(alien, boss, monster));
        }
        MessageUtils.send_to_public(AlienBossMessage.F2PCrossAlienBoss.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 刷新伤害列表
     *
     * @param map
     */
    private void tick(MapObject map) {
        AlienCopyData alien = map.getZone();
        List<Monster> monsters = new ArrayList<>(alien.getBeAttack());
        if (monsters.isEmpty()) {
            return;
        }
        alien.getBeAttack().clear();

        for (Monster monster : monsters) {
            Boss boss = alien.getMonster().get(monster.getId());

            HashMap<Integer, Long> damageScoreTotal = new HashMap<>();
            alien.getServer().keySet().forEach(k -> damageScoreTotal.put(k, 0L));
            ArrayList<AlienBossMessage.AlienRole.Builder> damageRanks = new ArrayList<>();


            AlienBossMessage.ResCrossAlienBossDamageList.Builder message = AlienBossMessage.ResCrossAlienBossDamageList.newBuilder();
            message.setBoss(builder(alien, boss, monster));

            monster.getDamages().forEach((roleId, dm) -> {
                Player player = map.getPlayers().get(roleId);
                if (player == null) {
                    return;
                }
                AlienBossMessage.AlienRole.Builder mRole = builder(player);
                mRole.setDamage(dm);
                damageRanks.add(mRole);

                long old = damageScoreTotal.getOrDefault(player.getCamp(), 0L);
                damageScoreTotal.put(player.getCamp(), old + dm);
            });

            //归属阵营
            Map.Entry<Integer, Long> max = damageScoreTotal.entrySet().stream().max((a, b) -> a.getValue() > b.getValue() ? 1 : -1).get();
            message.setServerId(max.getKey());

            //伤害排序
            damageRanks.sort(Comparator.comparingLong(AlienBossMessage.AlienRole.Builder::getDamage).reversed());
            int i = 1;
            for (AlienBossMessage.AlienRole.Builder m : damageRanks) {
                m.setRank(i++);
                message.addRankList(m);
            }
            monster.getDamages().forEach((roleId, v) -> {
                Player player = map.getPlayers().get(roleId);
                if (player == null) {
                    return;
                }
                MessageUtils.send_to_player(player, AlienBossMessage.ResCrossAlienBossDamageList.MsgID.eMsgID_VALUE, message.build().toByteArray());
            });
        }
    }

    /**
     * 刷新boss
     *
     * @param map
     * @param boss
     */
    private void refreshBoss(MapObject map, Boss boss) {

        AlienCopyData alien = map.getZone();

        Cfg_Cross_Alien_Boss_Bean bean = CfgManager.getCfg_Cross_Alien_Boss_Container().getValueByKey(boss.getModelId());
        Monster monster = Manager.monsterManager.createMonster(map, new Position(bean.getPos().get(0), bean.getPos().get(1)), bean.getMonsterId());
        if (monster == null) {
            return;
        }
        boss.setId(monster.getId());
        alien.getMonster().put(monster.getId(), boss);
        alien.getState().add(monster);

        logger.info("混沌虚空刷新 roomId={} cityId={} boss={}", map.getId(), alien.getCity(), boss);
        sendBossList(map, map.getPlayers().values());
    }

    void sendBossList(MapObject map, Player player) {
        List<Player> players = new ArrayList<>();
        players.add(player);
        sendBossList(map, players);
    }

    /**
     * 同步首领列表
     *
     * @param map
     * @param players
     */
    void sendBossList(MapObject map, Collection<Player> players) {

        AlienCopyData alien = map.getZone();

        AlienBossMessage.ResCrossAlienBossList.Builder message = AlienBossMessage.ResCrossAlienBossList.newBuilder();
        message.setCity(builder(alien));
        for (Boss boss : alien.getBoss().values()) {
            Monster monster = map.getMonsters().get(boss.getId());
            message.addBoss(builder(alien, boss, monster));
        }
        for (Player player : players) {
            MessageUtils.send_to_player(player, AlienBossMessage.ResCrossAlienBossList.MsgID.eMsgID_VALUE, message.build().toByteArray());
        }
    }

    AlienBossMessage.AlienCity.Builder builder(AlienCopyData alien) {
        AlienBossMessage.AlienCity.Builder city = AlienBossMessage.AlienCity.newBuilder();
        city.setCityId(alien.getCity());
        city.setServerId(alien.getExtraServerId());
        city.addAllAuthEnterList(alien.getServer().values());
        return city;
    }

    AlienBossMessage.AlienRole.Builder builder(Player player) {
        AlienBossMessage.AlienRole.Builder city = AlienBossMessage.AlienRole.newBuilder();
        city.setPlayerId(player.getId());
        city.setName(player.getName());
        return city;
    }

    AlienBossMessage.AlienBoss.Builder builder(AlienCopyData alien, Boss boss, Monster monster) {
        AlienBossMessage.AlienBoss.Builder mBoss = AlienBossMessage.AlienBoss.newBuilder();
        mBoss.setBossId(boss.getModelId());
        if (monster != null) {
            mBoss.setMonsterId(monster.getId());
            mBoss.setHp(monster.getCurHp());
        }
        int kill = alien.getBossKill().getOrDefault(boss.getModelId(), 0);
        mBoss.setKillServerId(kill);
        long wait = boss.getNextTime() - TimeUtils.Time();
        mBoss.setTime(wait > 0 ? wait : 0);
        return mBoss;
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
     * 福地关闭
     *
     * @param mapObject
     */
    @Override
    public void fudClose(MapObject mapObject) {

    }
}
