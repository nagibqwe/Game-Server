package common.copyMap.guild;


import com.data.*;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_GuildBoss_Reward_Bean;
import com.data.bean.Cfg_Monster_Bean;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.boss.log.BossDieReliveLog;
import com.game.chat.structs.Notify;
import com.game.copymap.structs.CopyMapType;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.guild.structs.Guild;
import com.game.guildactivity.struct.GuildBaseMapData;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.GlobalType;
import com.game.utils.MessageUtils;
import com.game.welfare.struct.RetrieveType;
import game.core.dblog.LogService;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.GuildBossMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Description 仙盟驻地脚本
 * @auther lw
 * @create 2020-02-19 22:27
 */
public class GuildBaseMapScript implements IMapBaseScript {
    private static final Logger log = LogManager.getLogger(GuildBaseMapScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GuildBaseMapScript;
    }

    @Override
    public Object call(Object... args) {
        String method = (String) args[0];
        Player player = (Player) args[1];
        switch (method) {
            case "onReqGuildBossInspire":
                Integer type = (Integer) args[2];
                onReqGuildBossInspire(player, type);
        }
        return null;
    }

    private void onReqGuildBossInspire(Player player, int type) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getZoneModelId());
        if (bean == null || bean.getType() != CopyMapType.GuildBase_CopyMap) {
            return;
        }
        if (!Manager.guildActivityManager.isGuildBossRun()) {
            return;
        }
        if (!player.isHaveGuild()) {
            return;
        }
        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        ConcurrentHashMap<Long, ConcurrentHashMap<Long, Integer>> inspireInfo;
        if (type == 0) {
            inspireInfo = Manager.guildActivityManager.getMoneyInspire();
        } else if(type == 1) {
            inspireInfo = Manager.guildActivityManager.getGoldInspire();
        } else {
            log.error("错误的鼓舞类型！！");
            return;
        }
        if (!inspireInfo.containsKey(player.getGuildId())) {
            inspireInfo.put(player.getGuildId(), new ConcurrentHashMap<>());
        }
        int hasInspireNum = inspireInfo.get(player.getGuildId()).getOrDefault(player.getId(), 0);
        if (hasInspireNum >= Global.Guild_Growup_Time.get(type)) {
            return;
        }
        int moneyType, num;
        if (type == 0) {
            moneyType = ItemCoinType.BindMoney;
            num = Global.Guild_Gold_Growup_Cost.get(hasInspireNum);
        } else {
            moneyType = ItemCoinType.GemCoin;
            num = Global.Guild_YB_Growup_Cost.get(hasInspireNum);
        }
        long actionId = IDConfigUtil.getLogId();
        if (!Manager.currencyManager.manager().onDecItemCoin(player, num, ItemChangeReason.GuildBossInspireDec, actionId, moneyType)) {
            MessageUtils.notify_player(player, Notify.NORMAL, type == 0 ? MessageString.MoneyNotEnough : MessageString.GoldNotEnough);
            return;
        }
        Manager.currencyManager.manager().onAddItemCoin(player, Global.Guild_Growup_Add_item.get(0), Global.Guild_Growup_Add_item.get(1), ItemChangeReason.GuildBossInspireGet, actionId);
        inspireInfo.get(player.getGuildId()).put(player.getId(), hasInspireNum + 1);
        int lastValue = Manager.guildActivityManager.getTotalInspire().getOrDefault(player.getGuildId(), 0);
        Manager.guildActivityManager.getTotalInspire().put(player.getGuildId(), lastValue + 1);

        for (Player p : map.getPlayers().values()) {
            sendGuildBossInspireInfo(p);
        }
        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.GuildBossGrowUp, String.valueOf(Global.Guild_Growup_Add / 100));

        MessageUtils.notify_Chat_To_GuildPlayer(player, guild,true, MessageString.GUILDBOSSINSPIRENOTICE, player.getName());

        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.GuildBoss_Inspire, ItemChangeReason.GuildBossInspireDec, type);
    }

    private void sendGuildBossInspireInfo(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }
        GuildBossMessage.ResGuildBossInspire.Builder builder = GuildBossMessage.ResGuildBossInspire.newBuilder();
        if (!Manager.guildActivityManager.getMoneyInspire().containsKey(player.getGuildId())) {
            Manager.guildActivityManager.getMoneyInspire().put(player.getGuildId(), new ConcurrentHashMap<>());
        }
        if (!Manager.guildActivityManager.getGoldInspire().containsKey(player.getGuildId())) {
            Manager.guildActivityManager.getGoldInspire().put(player.getGuildId(), new ConcurrentHashMap<>());
        }
        builder.setOwnMoneyNum(Manager.guildActivityManager.getMoneyInspire().get(player.getGuildId()).getOrDefault(player.getId(), 0));
        builder.setOwnGoldNum(Manager.guildActivityManager.getGoldInspire().get(player.getGuildId()).getOrDefault(player.getId(), 0));
        builder.setGuildNum(Manager.guildActivityManager.getTotalInspire().getOrDefault(player.getGuildId(), 0));
        MessageUtils.send_to_player(player, GuildBossMessage.ResGuildBossInspire.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.addMapLoopScriptEventTimer(getId(), "tick", -1, 0, 1000);
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return player.isHaveGuild();
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        if (Manager.guildActivityManager.isGuildBossRun()) {
            sendGuildBossInspireInfo(player);

            Manager.retrieveResManager.getScript().count(player, RetrieveType.GuildBoss);
        }
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {

    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {
        if (!(attacker instanceof Player)) {
            return;
        }
        Player player = (Player) attacker;
        if (!player.isHaveGuild()) {
            return;
        }
        int inspireNum = Manager.guildActivityManager.getTotalInspire().getOrDefault(player.getGuildId(), 0);
        int realDamage = (int) (damage * (1 + Global.Guild_Growup_Add * inspireNum / 10000.0));
        if (!Manager.guildActivityManager.getBossPersonDamage().containsKey(player.getGuildId())) {
            Manager.guildActivityManager.getBossPersonDamage().put(player.getGuildId(), new ConcurrentHashMap<>());
        }
        long lastGuildDamage = Manager.guildActivityManager.getBossGuildDamage().getOrDefault(player.getGuildId(), 0L);
        long lastPersonDamage = Manager.guildActivityManager.getBossPersonDamage().get(player.getGuildId()).getOrDefault(player.getId(), 0L);
        Manager.guildActivityManager.getBossGuildDamage().put(player.getGuildId(), lastGuildDamage + realDamage);
        Manager.guildActivityManager.getBossPersonDamage().get(player.getGuildId()).put(player.getId(), lastPersonDamage + realDamage);
    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {
        int worldLevel = GlobalType.getWorldLevel();
        Cfg_GuildBoss_Reward_Bean rewardBean = null;
        for (Cfg_GuildBoss_Reward_Bean bean : CfgManager.getCfg_GuildBoss_Reward_Container().getValuees()) {
            if (worldLevel >= bean.getWorld_level().get(0) && worldLevel <= bean.getWorld_level().get(1)) {
                rewardBean = bean;
            }
        }
        if (rewardBean == null) {
            log.error("未找到Boss的奖励配置，worldLevel：" + worldLevel);
            return;
        }

        GuildBaseMapData guildBaseMapData = MapParam.getGuildBaseMapData(map);
        for (Map.Entry<Integer, List<Long>> entry : guildBaseMapData.getMonsterInfo().entrySet()) {
            if (entry.getValue().isEmpty()) {
                continue;
            }
            Iterator<Long> iterator = entry.getValue().iterator();
            while (iterator.hasNext()) {
                long monsterId = iterator.next();
                if (monsterId == monster.getId()) {
                    iterator.remove();
                }
            }
            if (!entry.getValue().isEmpty()) {
                continue;
            }
            //小怪全部击杀获得奖励
            long actionId = IDConfigUtil.getLogId();
            for (Player player : map.getPlayers().values()) {
                if (!player.isHaveGuild()) {
                    continue;
                }
                List<Item> items = Item.createItems(rewardBean.getMonster_reward());
                if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.GuildBossRewardGet, actionId)) {
                    Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                            MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.GuildBossRewardGet);
                }
            }
        }
        if (monster.getModelId() != Global.Guild_Boss_ID.get(0)) {
            return;
        }
        Manager.guildActivityManager.setGuildBossRun(false);

        guildBaseMapData.setProgress(0);
        guildBaseMapData.getMonsterInfo().clear();

        //Boss排名奖励
        TreeMap<Long, Long> guildRankMap = new TreeMap<>(Comparator.comparingLong(n -> (long) n).reversed());
        TreeMap<Long, Long> guildMemberRankMap = new TreeMap<>(Comparator.comparingLong(n -> (long) n).reversed());
        for (Map.Entry<Long, Long> entry : Manager.guildActivityManager.getBossGuildDamage().entrySet()) {
            guildRankMap.put(entry.getValue(), entry.getKey());
        }
        if(!Manager.guildActivityManager.getBossPersonDamage().containsKey(map.getOwnId())) {
            log.error("伤害数据中不存在改仙盟数据！");
            return;
        }
        for (Map.Entry<Long, Long> entry : Manager.guildActivityManager.getBossPersonDamage().get(map.getOwnId()).entrySet()) {
            guildMemberRankMap.put(entry.getValue(), entry.getKey());
        }

        Player topRankPlayer = null;
        for (Map.Entry<Long, Long> entry : guildMemberRankMap.entrySet()) {
            Player player = Manager.playerManager.getPlayerCache(entry.getValue());
            if (player == null) {
                continue;
            }
            topRankPlayer = player;
            break;
        }

        //仙盟排名奖励上架拍卖
        int guildRank = 0;
        HashMap<Long, Integer> rankSet = new HashMap<>();
        List<Item> items = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : guildRankMap.entrySet()) {
            guildRank += 1;
            rankSet.put(entry.getValue(), guildRank);
            if (entry.getValue() != map.getOwnId()) {
                continue;
            }
            int rewardIndex = -1;
            for (int i = 0; i < rewardBean.getRank().size(); i++) {
                ReadArray<Integer> array = rewardBean.getRank().get(i);
                if (guildRank >= array.get(0) && guildRank <= array.get(1)) {
                    rewardIndex = i;
                }
            }
            if (rewardIndex == -1) {
                break;
            }
            Guild guild = Manager.guildsManager.getGuildById(entry.getValue());
            if (guild == null) {
                break;
            }
            if (rewardBean.getGuild_auction_reward().size() < rewardIndex + 1) {
                log.error("仙盟Boss配置表排名对应的仙盟拍卖id不存在！！！请检查");
                break;
            }
            List<List<Integer>> itemList = new ArrayList<>();
            for (int i = 0; i < rewardBean.getGuild_auction_reward().get(rewardIndex).size(); i++) {
                itemList.addAll(Manager.dropManager.deal().getItemDrops(topRankPlayer, rewardBean.getGuild_auction_reward().get(rewardIndex).get(i)));
            }
            items.addAll(Item.createItems(itemList, 1));
            List<Long> roleIds = new ArrayList<>(guildMemberRankMap.values());
            Manager.auctionManager.manager().auctionActivityPut(roleIds, items, DailyActiveDefine.ACTIVITY_GUILD_BOSS.getValue(), guild.getId());
        }

        Cfg_Monster_Bean bean = CfgManager.getCfg_Monster_Container().getValueByKey(monster.getModelId());

        //个人排名奖励
        int rank = 0;
        Long damage = Manager.guildActivityManager.getBossGuildDamage().get(map.getOwnId());
        for (Map.Entry<Long, Long> entry : guildMemberRankMap.entrySet()) {
            Player player = Manager.playerManager.getPlayerCache(entry.getValue());
            if (!player.isHaveGuild()) {
                continue;
            }
            rank += 1;
            List<Item> personItems = new ArrayList<>();
            for (int i = 0; i < rewardBean.getPersonal_reward().size(); i++) {
                ReadArray<Integer> array = rewardBean.getPersonal_reward().get(i);
                if (rank >= array.get(0) && rank <= array.get(1)) {
                    personItems.add(Item.createItem(array.get(2), array.get(3), false));
                }
            }
            if (!map.getPlayers().containsKey(player.getId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.GuildBossMailTitle, MessageString.GuildBossMailContext, personItems, ItemChangeReason.GuildBossRewardGet);
                continue;
            }
            if (!Manager.backpackManager.manager().addItems(player, personItems, ItemChangeReason.GuildBossRewardGet, IDConfigUtil.getLogId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, personItems, ItemChangeReason.GuildBossRewardGet);
            }
            Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.GuildBossFinish, Count.RefreshType.CountType_Forever, 1);
            Manager.controlManager.operate(player, FunctionVariable.GuildBossFinish, 1);
            Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.ACTIVITY_GUILD_BOSS, 1);

            GuildBossMessage.ResGuildBossResult.Builder builder = GuildBossMessage.ResGuildBossResult.newBuilder();
            builder.setRank(rankSet.getOrDefault(player.getGuildId(), 99));
            builder.setDamage(damage);
            builder.addAllItemInfoList(Manager.backpackManager.manager().buildItemInfo(personItems));
            builder.addAllAuctionList(Manager.backpackManager.manager().buildItemInfo(items));
            MessageUtils.send_to_player(player, GuildBossMessage.ResGuildBossResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());

            Long dps = 0l;
            ConcurrentHashMap<Long, Long> guildDps = Manager.guildActivityManager.getBossPersonDamage().get(player.getGuildId());
            if(guildDps != null){
                dps = guildDps.get(player.getId());
                if(dps == null){
                    dps = 0l;
                }
            }
            //BI
            Cfg_Clone_map_Bean mapBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getMapModelId());
            Manager.biManager.getScript().biBoss(player,map.getMapModelId(),map.getName(),map.getType(),mapBean == null ? null : mapBean.getMin_lv(), bean.getMonster_type(), monster.getModelId(), monster.getName(), monster.getLevel(), dps, rank, player.getGuildId(), bean.getBOSS_type(), rankSet.getOrDefault(player.getGuildId(), 99));
        }

        //死亡日志
        BossDieReliveLog bossDieLog = new BossDieReliveLog();
        bossDieLog.setBossId(monster.getModelId());
        bossDieLog.setMapId(map.getMapModelId());
        bossDieLog.setType(0);
        bossDieLog.setParam(map.getOwnId());
        LogService.getInstance().execute(bossDieLog);
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
        }
    }

    private void tick(MapObject map) {
        if (Manager.guildActivityManager.isGuildBossRun()) {
            if (map.isAutoRemove()) {
                map.setAutoRemove(false);
            }
            boolean isRefresh = false;
            for (Monster monster : map.getMonsters().values()) {
                if (monster.getModelId() == Global.Guild_Boss_ID.get(0)) {
                    isRefresh = true;
                    break;
                }
            }
            if (!isRefresh) {
                Cfg_Monster_Bean bean = CfgManager.getCfg_Monster_Container().getValueByKey(Global.Guild_Boss_ID.get(0));
                if (bean == null) {
                    return;
                }
                Position position = new Position(Global.Guild_Boss_ID.get(1), Global.Guild_Boss_ID.get(2));
                Monster boss = Manager.monsterManager.createMonster(map, position, bean.getId());
                double hpPercent = getActivityTimePersent();
                int curHp = (int) (Math.floor(hpPercent * bean.getMaxHp()));
                if (boss != null) {
                    boss.setCurHp(curHp);
                    log.info("创建首领boss成功,当前血量:"+curHp+" = 总血量:"+bean.getMaxHp()+" X 百分比:"+hpPercent);
                }
                for (Player player : map.getPlayers().values()) {
                    sendGuildBossInspireInfo(player);
                }

                BossDieReliveLog bossDieLog = new BossDieReliveLog();
                bossDieLog.setBossId(boss.getModelId());
                bossDieLog.setMapId(map.getMapModelId());
                bossDieLog.setType(1);
                bossDieLog.setParam(map.getOwnId());
                LogService.getInstance().execute(bossDieLog);
            }

            //同步伤害排名
            syncGuildBossDamage(map);

            //检查刷小怪
            onBossHpEvent(map);
        } else {
            if (!map.isAutoRemove()) {
                map.setAutoRemove(true);
            }
        }

    }

    /**
     * 根据当前时间获取怪物血量比率
     */
    private double getActivityTimePersent() {
        long now = TimeUtils.Time();
        int nowMin = TimeUtils.getDayOfHour(now) * 60 + TimeUtils.getDayOfMin(now);
        for (int i = 0; i < Global.Guild_Boss_Time.size(); i++) {
            ReadArray<Integer> array = Global.Guild_Boss_Time.get(i);
            if (nowMin >= array.get(0) && nowMin < array.get(1)) {
                double hasGoSec = (int)((now - TimeUtils.getTodayBeginTime()) / 1000) - array.get(0) * 60;
                int activityTime = ((array.get(1) - array.get(0)) * 60);
                return (activityTime - hasGoSec) / activityTime;
            }
        }
        log.error("仙盟boss错误的时间点调用，请检查！！");
        return 1d;
    }

    /**
     * 同步伤害排名
     */
    private void syncGuildBossDamage(MapObject map) {
        GuildBossMessage.ResSyncGuildBossDamage.Builder builder = GuildBossMessage.ResSyncGuildBossDamage.newBuilder();
        for (Map.Entry<Long, Long> entry : Manager.guildActivityManager.getBossGuildDamage().entrySet()) {
            Guild guild = Manager.guildsManager.getGuildById(entry.getKey());
            if (guild == null) {
                continue;
            }
            GuildBossMessage.guildBossDamageInfo.Builder info = GuildBossMessage.guildBossDamageInfo.newBuilder();
            info.setId(guild.getId());
            info.setName(guild.getName());
            info.setDamage(entry.getValue());
            builder.addGuildInfo(info);
        }
        ConcurrentHashMap<Long, Long> bossPersonDamage = Manager.guildActivityManager.getBossPersonDamage().get(map.getOwnId());
        if (bossPersonDamage != null) {
            for (Map.Entry<Long, Long> entry : bossPersonDamage.entrySet()) {
                PlayerWorldInfo worldInfo = Manager.playerManager.getPlayerWorldInfo(entry.getKey());
                GuildBossMessage.guildBossDamageInfo.Builder info = GuildBossMessage.guildBossDamageInfo.newBuilder();
                info.setId(worldInfo.getRoleid());
                info.setName(worldInfo.getRolename());
                info.setDamage(entry.getValue());
                builder.addPersonInfo(info);
            }
        }
        MessageUtils.send_to_map(map, GuildBossMessage.ResSyncGuildBossDamage.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * Boss血量掉到一定百分比，刷小怪
     */
    private void onBossHpEvent(MapObject mapObject) {
        Monster boss = null;
        for (Monster monster : mapObject.getMonsters().values()) {
            if (monster.getModelId() == Global.Guild_Boss_ID.get(0)) {
                boss = monster;
            }
        }
        if (boss == null) {
            return;
        }
        Cfg_Monster_Bean bean = CfgManager.getCfg_Monster_Container().getValueByKey(boss.getModelId());
        if (bean == null) {
            return;
        }

        if (boss.getCurHp() <= bean.getMaxHp() * 0.6 && boss.getCurHp() > bean.getMaxHp() * 0.3) {
            refreshMonsterLoop(mapObject, 1);
        }
        if (boss.getCurHp() <= bean.getMaxHp() * 0.3) {
            refreshMonsterLoop(mapObject, 2);
        }
    }

    /**
     * 刷小怪
     *
     * @param loop 波数
     */
    private void refreshMonsterLoop(MapObject mapObject, int loop) {
        GuildBaseMapData guildBaseMapData = MapParam.getGuildBaseMapData(mapObject);
        if (guildBaseMapData.getProgress() >= loop) {
            return;
        }
        guildBaseMapData.setProgress(loop);
        for (int i = 0; i < Global.Guild_monster_ID.size(); i++) {
            Position pos = new Position(Global.Guild_monster_ID.get(i).get(1), Global.Guild_monster_ID.get(i).get(2));
            Monster monster = Manager.monsterManager.createMonster(mapObject, pos, Global.Guild_monster_ID.get(i).get(0));
            if (!guildBaseMapData.getMonsterInfo().containsKey(loop)) {
                guildBaseMapData.getMonsterInfo().put(loop, new ArrayList<>());
            }
            guildBaseMapData.getMonsterInfo().get(loop).add(monster.getId());
        }
    }

    @Override
    public void removeMap(MapObject map) {

    }

}
