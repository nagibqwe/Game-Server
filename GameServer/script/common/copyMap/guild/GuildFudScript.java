package common.copyMap.guild;

import com.data.*;
import com.data.bean.Cfg_Guild_battle_boss_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.boss.log.BossDieReliveLog;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.drop.structs.SpecialDropDefine;
import com.game.guild.structs.Guild;
import com.game.guildactivity.struct.*;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.thread.SaveServer;
import com.game.structs.Fighter;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.GuildActivityMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuildFudScript implements IMapBaseScript {

    final Logger logger = LogManager.getLogger(GuildFudScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GuildFudScript;
    }

    @Override
    public Object call(Object... objects) {
        String method = (String) objects[0];
        MapObject mapObject = (MapObject) objects[1];
        switch (method) {
            case "allocGuildActivityPlace": {
                List<Player> members = new ArrayList<>(mapObject.getPlayers().values());
                for (Player player : members) {
                    /**
                     * 踢出无资格玩家
                     */
                    if (checkEnterState(player, mapObject.getZoneModelId()) < 0) {
                        Manager.copyMapManager.manager().onReqCopyMapOut(player);
                    }
                }
                allocGuildActivityPlace(mapObject);
            }
            break;
            case "activityEnd": {
                tickOutAllPlayer(mapObject);
                tickOutAllBoss(mapObject);
            }
            break;
        }
        return null;
    }

    private static final int topThree = 3;

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        //不自动销毁，通过活动时间来判断销毁
        mapObject.setAutoRemove(false);

        long curTime = TimeUtils.Time();
        GuildFud fud = Manager.guildActivityManager.getFud().get(mapObject.getZoneModelId());
        fud.setMapId(mapObject.getId());
        fud.getMonsterNum().clear();
        for (GuildFudBoss boss : fud.getBoss().values()) {
            if (fud.getWaitBoss().containsKey(boss.getBossId())) {
                continue;
            }
            Cfg_Guild_battle_boss_Bean bean = CfgManager.getCfg_Guild_battle_boss_Container().getValueByKey(boss.getBossId());
            int nextMonster = gainMonsterId(bean);

            ReadArray<Integer> posArray = bean.getPos().get(0);
            if (posArray.size() < 2 || nextMonster <= 0) {
                logger.error("下一次刷新怪物计算错误 boss={}", boss);
                continue;
            }
            boss.setMonsterId(nextMonster);
            boss.setBirthTime(curTime);
            boss.setNotify(true);

            fud.getMb().put(nextMonster, boss.getBossId());
            fud.getWaitBoss().put(boss.getBossId(), boss);
        }
        //刷新怪物计时器
        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 1000, 1000);

        logger.info("创建本服福地 map={}", mapObject);
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        int state = checkEnterState(player, model);
        if (state == -1) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.GUILD_BATTLE_entryprompt3);
        }
        if (state == -2) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.ThisFunctionIsUnlock);
        }
        if (state == -3) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.GUILD_BATTLE_entryprompt2);
        }
        if (state == -4) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.GUILD_BATTLE_entryprompt1);
        }
        return state == 0;
    }

    int checkEnterState(Player player, int model) {
        if (!player.isHaveGuild()) {
            return -1;
        }
        //福地第一天只展示不开放
        if (TimeUtils.getOpenServerDay() == 1) {
            return -2;
        }
        int type = 0;
        for (Cfg_Guild_battle_boss_Bean bean : CfgManager.getCfg_Guild_battle_boss_Container().getValuees()) {
            if (bean.getMapID() != model) {
                continue;
            }
            type = bean.getGroup();
            break;
        }
        boolean isTopThree = Manager.guildActivityManager.deal().haveTopFudi(player.getGuildId());

        if (type > 3 && isTopThree) {
            return -3;
        }
        if (type <= 3 && !isTopThree) {
            return -4;
        }
        return 0;
    }

    //关注列表中有此怪物，通知玩家
    void notifyRefresh(GuildFudBoss boss) {
        boss.setNotify(false);
        for (Player p : Manager.playerManager.getPlayersCache().values()) {
            if (!p.getFuDiFollowedBossList().contains(boss.getBossId())) {
                continue;
            }
            if (!p.isOnline()) {
                continue;
            }
            GuildActivityMessage.ResAttentionMonsterRefresh.Builder msg = GuildActivityMessage.ResAttentionMonsterRefresh.newBuilder();
            msg.setMonsterModelId(boss.getMonsterId());
            MessageUtils.send_to_player(p, GuildActivityMessage.ResAttentionMonsterRefresh.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    /**
     * 重新分配福地
     *
     * @param map
     */
    void allocGuildActivityPlace(MapObject map) {

        GuildFud fud = Manager.guildActivityManager.getFud().get(map.getZoneModelId());

        for (Monster monster : map.getMonsters().values()) {
            logger.info("重新分配福地时 存活 boss={}", monster);
        }
        List<Player> members = new ArrayList<>(map.getPlayers().values());
        for (Player player : members) {
            GuildFud gf = Utils.findOne(Manager.guildActivityManager.getFud().values(), o -> o.getGuild() != null && o.getGuild().getId() == player.getGuildId());
            if (gf != null) {
                player.setCamp(gf.getRank(), true);
            } else {
                player.setCamp(0, true);
            }
        }
        long curTime = TimeUtils.Time();
        for (GuildFudBoss boss : fud.getWaitBoss().values()) {
            boss.setBirthTime(curTime);
        }
        refreshMonster(map);
    }

    /**
     * 完全刷新，全部怪都重新创建
     */
    private void refreshMonster(MapObject mapObject) {

        GuildFud fud = Manager.guildActivityManager.getFud().get(mapObject.getZoneModelId());

        if (fud.getWaitBoss().isEmpty()) {
            return;
        }
        long curTime = TimeUtils.Time();
        HashMap<Integer, Monster> monsterHash = new HashMap<>();
        mapObject.getMonsters().values().forEach(m -> monsterHash.put(m.getModelId(), m));
        boolean notify = false;
        for (GuildFudBoss boss : new ArrayList<>(fud.getWaitBoss().values())) {
            Cfg_Guild_battle_boss_Bean bean = CfgManager.getCfg_Guild_battle_boss_Container().getValueByKey(boss.getBossId());
            if (monsterHash.containsKey(boss.getMonsterId())) {
                continue;
            }

            if (boss.isNotify() && curTime + Global.Boss_attent_notice * 1000 >= boss.getBirthTime()) {
                notifyRefresh(boss);
            }

            if (curTime < boss.getBirthTime()) {
                continue;
            }
            fud.getWaitBoss().remove(boss.getBossId());

            ReadArray<Integer> posArray = bean.getPos().get(0);
            //创建怪物
            Monster monster = Manager.monsterManager.createMonster(mapObject, new Position(posArray.get(0), posArray.get(1)), boss.getMonsterId(), fud.getRank());
            //计数
            Integer num = fud.getMonsterNum().getOrDefault(bean.getType(), 0);
            fud.getMonsterNum().put(bean.getType(), ++num);
            notify = true;
            //关注列表中有此怪物，通知玩家
            for (Player p : Manager.playerManager.getPlayersCache().values()) {
                if (!p.getFuDiFollowedBossList().contains(bean.getId())) {
                    continue;
                }
                if (!p.isOnline()) {
                    continue;
                }
                int state = checkEnterState(p, mapObject.getZoneModelId());
                if (state < 0) {
                    continue;
                }
                GuildActivityMessage.ResAttentionMonsterRefresh.Builder msg = GuildActivityMessage.ResAttentionMonsterRefresh.newBuilder();
                msg.setMonsterModelId(bean.getId());
                MessageUtils.send_to_player(p, GuildActivityMessage.ResAttentionMonsterRefresh.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }

            //刷新日志
            bossLog(mapObject, boss, 1);
            logger.info("本服福地 map={} monster={}", mapObject, monster);
        }
        if (notify) {
            Manager.guildActivityManager.deal().checkFudiBossRedPoint();
            List<Player> players = new ArrayList<>(mapObject.getPlayers().values());
            sendBossInfo(mapObject, 2, players.toArray(new Player[players.size()]));
        }
    }

    /**
     * 获取刷怪ID
     *
     * @param bean
     * @return
     */
    int gainMonsterId(Cfg_Guild_battle_boss_Bean bean) {
        ReadIntegerArrayEs config = bean.getMonsterID();
        //1：世界等级刷新
        //2：开服天数刷新
        if (bean.getRefreshType() == 1) {
            int index, level;
            for (index = 0; index < config.size(); index++) {
                level = config.get(index).get(0);
                if (level > GlobalType.getWorldLevel()) {
                    break;
                }
            }
            if (index > 0) {
                index = index - 1;
            }
            return config.get(index).get(1);
        }
        if (bean.getRefreshType() == 2) {
            int index, day;
            for (index = 0; index < config.size(); index++) {
                day = config.get(index).get(0);
                if (day == TimeUtils.getOpenServerDay()) {
                    return config.get(index).get(1);
                }
            }
            return config.get(index - 1).get(1);
        }
        return -1;
    }

    /**
     * 获取怪物共享掉落
     *
     * @param bean
     * @return
     */
    ReadArray<Integer> gainMonsterShareDrop(Cfg_Guild_battle_boss_Bean bean) {
        ReadIntegerArrayEs config = bean.getGuildTeam_reward();
        //1：世界等级刷新
        //2：开服天数刷新
        if (bean.getRefreshType() == 1) {
            int index, level;
            for (index = 0; index < config.size(); index++) {
                level = config.get(index).get(0);
                if (level > GlobalType.getWorldLevel()) {
                    break;
                }
            }
            if (index > 0) {
                index = index - 1;
            }
            return config.get(index);
        }
        if (bean.getRefreshType() == 2) {
            int index, day;
            for (index = 0; index < config.size(); index++) {
                day = config.get(index).get(0);
                if (day == TimeUtils.getOpenServerDay()) {
                    return config.get(index);
                }
            }
            return config.get(index - 1);
        }
        return null;
    }

    @Override
    public void onEnterMap(Player player, MapObject mapObject, boolean login) {

        GuildFud gf = Utils.findOne(Manager.guildActivityManager.getFud().values(), o -> o.getGuild() != null && o.getGuild().getId() == player.getGuildId());
        if (gf != null) {
            player.setCamp(gf.getRank(), true);
        } else {
            player.setCamp(0, true);
        }
        sendBossInfo(mapObject, 1, player);
    }

    /**
     * 同步怪物数据
     *
     * @param mapObject
     * @param state     1=进入地图 2更新数据
     * @param players
     */
    void sendBossInfo(MapObject mapObject, int state, Player... players) {

        GuildFud fud = Manager.guildActivityManager.getFud().get(mapObject.getZoneModelId());
        //同步怪物复活时间
        GuildActivityMessage.ResUpdateMonsterResurgenceTime.Builder resurgenceTime = GuildActivityMessage.ResUpdateMonsterResurgenceTime.newBuilder();
        for (GuildFudBoss boss : fud.getBoss().values()) {
            Cfg_Guild_battle_boss_Bean bean = CfgManager.getCfg_Guild_battle_boss_Container().getValueByKey(boss.getBossId());

            GuildActivityMessage.monsterResurgenceTime.Builder monsterResurgenceTime = GuildActivityMessage.monsterResurgenceTime.newBuilder();
            monsterResurgenceTime.setMonsterType(bean.getType());
            monsterResurgenceTime.setMonsterModelId(bean.getId());
            monsterResurgenceTime.setLevel(0);
            monsterResurgenceTime.setResurgenceTime(fud.getWaitBoss().containsKey(boss.getBossId()) ? boss.getBirthTime() - TimeUtils.Time() : 0);
            resurgenceTime.addResurgenceTime(monsterResurgenceTime);
        }
        resurgenceTime.setType(fud.getRank());
        resurgenceTime.setState(state);

        for (Player player : players) {
            MessageUtils.send_to_player(player, GuildActivityMessage.ResUpdateMonsterResurgenceTime.MsgID.eMsgID_VALUE, resurgenceTime.build().toByteArray());
        }
    }

    @Override
    public void onQuitMap(Player player, MapObject mapObject, boolean isQuit) {
        player.setCamp(0, true);
    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

        GuildFud fud = Manager.guildActivityManager.getFud().get(mapObject.getZoneModelId());
        GuildFudBoss boss = fud.findBoss(monster.getModelId());
        if (boss == null) {
            logger.error("福地boss的怪物找不到配置表Id!!!  boss={}", monster.getModelId());
            return;
        }

        GuildFudiAttack guildFudiAttack = null;
        if (attacker instanceof Player) {
            Guild guild = Manager.guildsManager.GetGuildByPlayer((Player) attacker);
            HashMap<Integer, GuildFudiAttack> attacking = Manager.guildActivityManager.getFudiAttacking().computeIfAbsent(guild.getId(), k -> new HashMap<>());
            guildFudiAttack = attacking.get(boss.getBossId());
            if (guildFudiAttack == null) {
                guildFudiAttack = new GuildFudiAttack();
                attacking.put(boss.getBossId(), guildFudiAttack);
            }
        }
        if (guildFudiAttack != null) {
            guildFudiAttack.setBossId(boss.getBossId());
            guildFudiAttack.setLevel(monster.getLevel());
            guildFudiAttack.setHp(monster.getCurHp());
            guildFudiAttack.getAttacking().put(attacker.getId(), attacker.getId());
        }

        HashMap<Long, Long> guildHarm = new HashMap<>();
        HashMap<Long, List<Player>> players = new HashMap<>();
        for (Map.Entry<Long, Long> entry : monster.getDamages().entrySet()) {
            Player player = Manager.playerManager.getPlayerCache(entry.getKey());
            if (player == null) {
                continue;
            }
            Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
            if (guild == null) {
                continue;
            }
            guildHarm.put(guild.getId(), guildHarm.getOrDefault(guild.getId(), 0L) + entry.getValue());
            if (!players.containsKey(guild.getId())) {
                players.put(guild.getId(), new ArrayList<>());
            }
            players.get(guild.getId()).add(player);
        }

        GuildActivityMessage.ResHarmData.Builder builder = GuildActivityMessage.ResHarmData.newBuilder();
        builder.setBossModelId(boss.getBossId());
        builder.setBossUid(monster.getId());
        for (Map.Entry<Long, Long> entry : guildHarm.entrySet()) {
            Guild guild = Manager.guildsManager.getGuildById(entry.getKey());
            GuildActivityMessage.bossHarmInfo.Builder guildHarmInfo = GuildActivityMessage.bossHarmInfo.newBuilder();
            guildHarmInfo.setId(guild.getId());
            guildHarmInfo.setName(guild.getName());
            guildHarmInfo.setDamage(entry.getValue());
            builder.addGuildHarmData(guildHarmInfo);
        }
        for (Map.Entry<Long, Long> entry : guildHarm.entrySet()) {
            List<Player> playerList = players.get(entry.getKey());
            for (Player player : playerList) {
                GuildActivityMessage.bossHarmInfo.Builder personHarmInfo = GuildActivityMessage.bossHarmInfo.newBuilder();
                personHarmInfo.setId(player.getId());
                personHarmInfo.setName(player.getName());
                personHarmInfo.setDamage(monster.getDamages().get(player.getId()));
                builder.addPersonHarmData(personHarmInfo);
            }
            MessageUtils.send_to_players(playerList, GuildActivityMessage.ResHarmData.MsgID.eMsgID_VALUE, builder.build().toByteArray(), 0);
        }

    }

    @Override
    public void onMonsterDie(MapObject mapObject, Monster monster, Fighter attacker) {

        GuildFud fud = Manager.guildActivityManager.getFud().get(mapObject.getZoneModelId());

        GuildFudBoss boss = Utils.findOne(fud.getBoss().values(), m -> m.getMonsterId() == monster.getModelId());
        if (boss == null) {
            logger.error("Cfg_Guild_battle_bossBean无法找到该怪物，cfgId = " + monster.getModelId());
            return;
        }
        //死亡日志
        bossLog(mapObject, boss, 0);

        Cfg_Guild_battle_boss_Bean bean = CfgManager.getCfg_Guild_battle_boss_Container().getValueByKey(boss.getBossId());
        if (bean == null) {
            logger.error("Cfg_Guild_battle_bossBean无法找到该怪物，cfgId = " + boss);
            return;
        }
        //计数
        int type = bean.getType();
        Integer num = fud.getMonsterNum().getOrDefault(type, 0);
        if (num > 0) {
            fud.getMonsterNum().put(type, num - 1);
        }
        //计算boss 下一次复活时间
        int nextMonster = gainMonsterId(bean);
        if (nextMonster <= 0) {
            nextMonster = boss.getMonsterId();
            logger.error("下一次刷新怪物计算错误 boss={}", boss);
        }
        boss.setMonsterId(nextMonster);
        boss.setBirthTime(TimeUtils.Time() + bean.getResp_time() * 60 * 1000L);
        boss.setNotify(true);
        fud.getMb().put(nextMonster, boss.getBossId());
        fud.getWaitBoss().put(boss.getBossId(), boss);

        if (!(attacker instanceof Player)) {
            return;
        }
        Player player = (Player) attacker;
        Manager.dropManager.deal().specialDropReward(monster, player, SpecialDropDefine.GuildFudiBoss, false, -1);

        //发奖励
        sendRewards(bean, mapObject, monster);


        //发消息
        GuildActivityMessage.ResUpdateMonsterResurgenceTime.Builder msg = GuildActivityMessage.ResUpdateMonsterResurgenceTime.newBuilder();
        GuildActivityMessage.monsterResurgenceTime.Builder monsterResurgenceTime = GuildActivityMessage.monsterResurgenceTime.newBuilder();
        monsterResurgenceTime.setMonsterType(type);
        monsterResurgenceTime.setMonsterModelId(bean.getId());
        monsterResurgenceTime.setLevel(0);
        monsterResurgenceTime.setResurgenceTime(bean.getResp_time() * 60 * 1000L);
        msg.addResurgenceTime(monsterResurgenceTime);
        msg.setType(bean.getGroup());
        msg.setState(2);
        MessageUtils.send_to_players(mapObject.getPlayers().values(), GuildActivityMessage.ResUpdateMonsterResurgenceTime.MsgID.eMsgID_VALUE, msg.build().toByteArray(), 0);
    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {

        if (mapObject.getMonsters().isEmpty()) {
            Manager.guildActivityManager.deal().checkFudiBossRedPoint();
        }
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

        GuildFud fud = Manager.guildActivityManager.getFud().get(map.getZoneModelId());

        GuildFudBoss boss = fud.findBoss(monster.getModelId());
        if (boss == null) {
            return;
        }
        Guild guild = Manager.guildsManager.GetGuildByPlayer(attacker);
        if (guild == null) {
            return;
        }
        HashMap<Integer, GuildFudiAttack> hashMap = Manager.guildActivityManager.getFudiAttacking().get(guild.getId());
        if (hashMap == null) {
            return;
        }
        GuildFudiAttack attacking = hashMap.get(boss.getBossId());
        if (attacking == null) {
            return;
        }
        attacking.getAttacking().remove(attacker.getId());
        if (attacking.getAttacking().isEmpty()) {
            hashMap.remove(boss.getBossId());
        }
        if (hashMap.isEmpty()) {
            Manager.guildActivityManager.getFudiAttacking().remove(guild.getId());
        }
        logger.info("仙盟福地玩家脱战 player={}", attacker);
    }

    /**
     * 发送奖励
     */
    private void sendRewards(Cfg_Guild_battle_boss_Bean bean, MapObject map, Monster monster) {
        HashMap<Long, Long> guildHarm = new HashMap<>();
        HashMap<Long, List<Player>> players = new HashMap<>();
        for (Map.Entry<Long, Long> entry : monster.getDamages().entrySet()) {
            Player player = Manager.playerManager.getPlayerCache(entry.getKey());
            if (player == null) {
                continue;
            }
            checkPlayerState(map, player);
            Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
            if (guild == null) {
                continue;
            }
            guildHarm.put(guild.getId(), guildHarm.getOrDefault(guild.getId(), 0L) + entry.getValue());
            if (!players.containsKey(guild.getId())) {
                players.put(guild.getId(), new ArrayList<>());
            }
            players.get(guild.getId()).add(player);

            Manager.controlManager.operate(player, FunctionVariable.KillfudiBoss_type, bean.getType(), 1);
            Manager.controlManager.operate(player, FunctionVariable.Kill_Guild_Territorial_Boss, 1);
        }


        //找伤害第一仙盟，和第一仙盟伤害第一的玩家
        long maxGuildHarm = 0, maxPlayerHarm = 0;
        long topGuildId = 0;
        Player topPlayer = null;
        for (Map.Entry<Long, Long> entry : guildHarm.entrySet()) {
            if (entry.getValue() > maxGuildHarm) {
                maxGuildHarm = entry.getValue();
                topGuildId = entry.getKey();
            }
        }
        if (topGuildId == 0) {
            logger.error("仙盟福地boss不存在伤害第一的仙盟，奖励发送失败！");
            return;
        }
        for (Player player : players.get(topGuildId)) {
            if (monster.getDamages().get(player.getId()) > maxPlayerHarm) {
                maxPlayerHarm = monster.getDamages().get(player.getId());
                topPlayer = player;
            }
        }
        if (topPlayer == null) {
            logger.error("不存在仙盟伤害第一的玩家，可能仙盟唯一造成伤害玩家退出了仙盟！");
            return;
        }
        //仙盟共享掉落
        ReadArray<Integer> sd = gainMonsterShareDrop(bean);
        HashMap<Long, List<Item>> shareHash = new HashMap<>();
        if (sd != null) {
            for (int i = 1; i < sd.size(); i++) {
                List<List<Integer>> drops = Manager.dropManager.deal().dropExecute(sd.get(i));
                List<Item> shareDrops = Item.createItems(drops, 1);
                List<Player> shares = new ArrayList<>(players.get(topGuildId));
                for (Item item : shareDrops) {
                    if (shares.isEmpty()) {
                        shares.addAll(players.get(topGuildId));
                    }
                    int random = RandomUtils.random(0, shares.size() - 1);
                    Player share = shares.remove(random);
                    List<Item> list = shareHash.getOrDefault(share.getId(), new ArrayList<>());
                    list.add(item);
                    shareHash.put(share.getId(), list);
                }
            }
        }
        int dropId = getLvDropId(bean.getParticipation_reward(), monster.getLevel());
        List<List<Integer>> drops;
        List<Item> dropItems;
        List<Long> roleIds = new ArrayList<>();
        for (Player player : players.get(topGuildId)) {
            roleIds.add(player.getId());
            long actionId = IDConfigUtil.getLogId();
            Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.GuildScore, bean.getPersonal_score(), ItemChangeReason.GuildActivityBossDropGet, actionId);
            drops = Manager.dropManager.deal().getItemDrops(player, dropId);
            if (player.getId() == topPlayer.getId() && bean.getFirst_reward().size() > 0) {
                int firstDropId = getLvDropId(bean.getFirst_reward(), monster.getLevel());
                if (firstDropId != 0) {
                    drops.addAll(Manager.dropManager.deal().getItemDrops(player, firstDropId));
                }
            }
            dropItems = Item.createItems(drops, 1);
            List<Item> shares = shareHash.get(player.getId());
            if (shares != null) {
                dropItems.addAll(shares);
            }
            if (!dropItems.isEmpty()) {
                if (!Manager.backpackManager.manager().addItems(player, dropItems, ItemChangeReason.GuildActivityBossDropGet, actionId)) {
                    //发送邮件
                    Manager.mailManager.sendMailToPlayer(player.getId(), MailType.CopymapSendMail, MessageString.System, MessageString.System, MessageString.Task_Content, dropItems, ItemChangeReason.GuildActivityBossDropGet);
                }
            }
            Manager.dropManager.deal().syncDropItemToRoundPlayer(player, monster, dropItems, true);
        }

        //加积分
        addScore(topGuildId, bean.getScore());

        //几率掉落物品进仙盟拍卖
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < bean.getDrop().size(); i++) {
            if (bean.getDrop().get(i).get(0) != monster.getLevel()) {
                continue;
            }
            List<List<Integer>> itemDrops = Manager.dropManager.deal().getItemDrops(topPlayer, bean.getDrop().get(i).get(1));
            itemList.addAll(Item.createItems(itemDrops, 1));
        }
        Manager.auctionManager.manager().auctionActivityPut(roleIds, itemList, DailyActiveDefine.FUD_ACTIVITY_BOSS.getValue(), topGuildId);
    }

    private int getLvDropId(ReadIntegerArrayEs arrayEs, int level) {
        for (int i = 0; i < arrayEs.size(); i++) {
            if (arrayEs.get(i).get(0) == level) {
                return arrayEs.get(i).get(1);
            }
        }
        return 0;
    }

    /**
     * 加宗派积分
     */
    private void addScore(long topGuildId, int score) {
        Guild guild = Manager.guildsManager.getGuildById(topGuildId);
        if (guild == null) {
            return;
        }
        Manager.countManager.addVariant(guild, VariantType.GuildFudi, score);
        Manager.saveThreadManager.getOtherServerSave().deal(guild.toGuildBean(), DbSqlName.GUILD_UPDATE, SaveServer.UPDATE);
    }

    /**
     * 检查福地归属变化，并重置阵营
     */
    private void checkPlayerState(MapObject map, Player player) {
        GuildFud fud = Manager.guildActivityManager.getFud().get(map.getZoneModelId());
        int type = fud.getRank();
        if (type == 0) {
            return;
        }
        int reason = 0;
        if (!player.isHaveGuild()) {
            player.setCamp(type);
            reason = 3;
        }

        boolean inTopThree = Manager.guildActivityManager.deal().haveTopFudi(player.getGuildId());

        if (type <= topThree && !inTopThree) {
            player.setCamp(type, true);
            reason = 1;
        }
        if (type > topThree && inTopThree) {
            player.setCamp(type, true);
            reason = 2;
        }
        logger.info("=======fud={} player={}", fud.getRank(), player.getCamp());
        if (reason == 0) {
            return;
        }
        GuildActivityMessage.ResQuitTip.Builder builder = GuildActivityMessage.ResQuitTip.newBuilder();
        builder.setReason(reason);
        MessageUtils.send_to_player(player, GuildActivityMessage.ResQuitTip.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter attacker, Player player) {
        if (!(attacker instanceof Player)) {
            return;
        }
        Player attackPlayer = (Player) attacker;
        if (attackPlayer.getGuildId() == player.getGuildId()) {
            return;
        }
        checkPlayerState(mapObject, player);
        checkPlayerState(mapObject, attackPlayer);

        if (!attackPlayer.isHaveGuild()) {
            return;
        }
        int hasGetNum = (int) Manager.countManager.getVariant(attackPlayer, VariantType.GuildActivityBossPvpScore);
        if (hasGetNum >= Global.GuildBattlebosspvp.get(1)) {
            MessageUtils.notify_player(attackPlayer, Notify.NORMAL, MessageString.GUILD_BATTLE_PVPDES1);
            return;
        }
        int num = Math.min(Global.GuildBattlebosspvp.get(0), Global.GuildBattlebosspvp.get(1) - hasGetNum);
        addScore(attackPlayer.getGuildId(), num);
        Manager.currencyManager.manager().onAddItemCoin(attackPlayer, ItemCoinType.GuildScore, num, ItemChangeReason.GuildActivityBossDropGet, 0);
        Manager.countManager.addVariant(attackPlayer, VariantType.GuildActivityBossPvpScore, num);
    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {
        switch (method) {
            case "tick":
                refreshMonster(mapObject);
                break;
        }
    }

    @Override
    public void removeMap(MapObject mapObject) {

    }

    /**
     * 强制踢人
     *
     * @param mapObject
     */
    void tickOutAllPlayer(MapObject mapObject) {
        List<Player> members = new ArrayList<>(mapObject.getPlayers().values());
        for (Player player : members) {
            Manager.copyMapManager.manager().onReqCopyMapOut(player);
        }
    }

    /**
     * 踢出所有怪物
     *
     * @param map
     */
    void tickOutAllBoss(MapObject map) {

        GuildFud fud = Manager.guildActivityManager.getFud().get(map.getZoneModelId());

        List<Monster> bossList = new ArrayList<>(map.getMonsters().values());
        for (Monster monster : bossList) {

            Manager.mapManager.manager().onQuitMap(map, monster, true);
            logger.info("0点踢出福地boss={}", monster);

            GuildFudBoss boss = Utils.findOne(fud.getBoss().values(), m -> m.getMonsterId() == monster.getModelId());
            if (boss == null) {
                continue;
            }
            bossLog(map, boss, 2);
        }
        long curTime = TimeUtils.Time();
        fud.setMapId(map.getId());
        fud.getMonsterNum().clear();
        for (GuildFudBoss boss : fud.getBoss().values()) {

            Cfg_Guild_battle_boss_Bean bean = CfgManager.getCfg_Guild_battle_boss_Container().getValueByKey(boss.getBossId());
            int nextMonster = gainMonsterId(bean);

            ReadArray<Integer> posArray = bean.getPos().get(0);
            if (posArray.size() < 2 || nextMonster <= 0) {
                logger.error("下一次刷新怪物计算错误 boss={}", boss);
                continue;
            }
            boss.setMonsterId(nextMonster);
            boss.setBirthTime(curTime);
            boss.setNotify(true);

            fud.getMb().put(nextMonster, boss.getBossId());
            fud.getWaitBoss().put(boss.getBossId(), boss);
        }
    }

    /**
     * 刷新日志
     *
     * @param mapObject
     * @param boss
     * @param type      0=死亡 1=刷新 2=清理
     */
    void bossLog(MapObject mapObject, GuildFudBoss boss, int type) {
        BossDieReliveLog bossDieLog = new BossDieReliveLog();
        bossDieLog.setBossId(boss.getMonsterId());
        bossDieLog.setMapId(mapObject.getMapModelId());
        bossDieLog.setType(type);
        LogService.getInstance().execute(bossDieLog);
    }

}
