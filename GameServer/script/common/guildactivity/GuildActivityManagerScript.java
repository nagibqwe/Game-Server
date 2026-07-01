package common.guildactivity;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_Guild_title_Container;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.BooleanForever;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildMember;
import com.game.guildactivity.script.IGuildActivityHandler;
import com.game.guildactivity.struct.*;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.thread.SaveServer;
import com.game.structs.GlobalType;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamCommon;
import com.game.utils.Utils;
import common.title.TitleScript;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.GuildActivityMessage;
import game.message.GuildBossMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GuildActivityManagerScript implements IGuildActivityHandler, IScript {

    final static Logger logger = LogManager.getLogger(GuildActivityManagerScript.class);

    final int[] fudTop = {1, 2, 3};        //前三福地

    @Override
    public int getId() {
        return ScriptEnum.GuildActivityBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 请求打开排名面板，获取排行数据
     */
    @Override
    public void openRankPanel(Player player) {
        GuildActivityMessage.ResOpenRankPanel.Builder msg = GuildActivityMessage.ResOpenRankPanel.newBuilder();
        msg.setMyGuildRank(0);
        msg.setMyRank(0);
        for (GuildFud fud : Manager.guildActivityManager.getFud().values()) {
            Guild guild = fud.getGuild();
            GuildActivityMessage.guildRankInfo.Builder guildMsg = GuildActivityMessage.guildRankInfo.newBuilder();
            if (guild != null) {
                guildMsg.setGuildId(guild.getId());
                guildMsg.setName(guild.getName());
                for (Map.Entry<Long, Integer> value : guild.getTitleRankMap().entrySet()) {
                    int top = value.getValue();
                    GuildMember member = guild.getMembers().get(value.getKey());
                    if (member == null)
                        continue;

                    GuildActivityMessage.guildMenberRankInfo.Builder memberMsg = GuildActivityMessage.guildMenberRankInfo.newBuilder();
                    memberMsg.setRank(top);
                    memberMsg.setRealm(member.getPlayerWorldInfo().getStateVip());
                    memberMsg.setName(member.getPlayerWorldInfo().getRolename());
                    memberMsg.setLevel(member.getPlayerWorldInfo().getLevel());
                    memberMsg.setFight(member.gainPower());
                    guildMsg.addMenberRank(memberMsg);

                    if (player.getId() == member.getId()) {
                        guildMsg.setMyRank(top);
                        msg.setMyRank(top);
                    }
                }

                if (guild.getId() == player.getGuildId())
                    msg.setMyGuildRank(fud.getRank());
            } else {
                guildMsg.setGuildId(0);
                guildMsg.setName("");
                guildMsg.setMyRank(0);
            }
            msg.addGuildRank(guildMsg);
        }
        MessageUtils.send_to_player(player, GuildActivityMessage.ResOpenRankPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 打开福地boss总览
     */
    @Override
    public void openAllBossPanel(Player player) {
        GuildActivityMessage.ResOpenAllBossPanel.Builder msg = GuildActivityMessage.ResOpenAllBossPanel.newBuilder();
        for (GuildFud fud : Manager.guildActivityManager.getFud().values()) {
            GuildActivityMessage.monsterRemain.Builder monsterRemain = GuildActivityMessage.monsterRemain.newBuilder();
            GuildActivityMessage.detailFudiInfo.Builder detailFudiInfo = GuildActivityMessage.detailFudiInfo.newBuilder();
            detailFudiInfo.setType(fud.getRank());
            Guild guild = fud.getGuild();
            if (guild == null) {
                monsterRemain.setGuildId(0L);
                monsterRemain.setName("");
                detailFudiInfo.setScore(0);
            } else {
                monsterRemain.setGuildId(guild.getId());
                monsterRemain.setName(guild.getName());
                long score = Manager.countManager.getVariant(guild, VariantType.GuildFudi);
                detailFudiInfo.setScore((int) score);
            }
            //关注列表
            List<Integer> attention = getGuildAttention(player, fud.getRank());
            if (!attention.isEmpty()) {
                detailFudiInfo.addAllAttentionList(attention);
            }

            Cfg_Guild_battle_boss_Bean bean = CfgManager.getCfg_Guild_battle_boss_Container().getValueByKey(fud.getRank() * 100 + 1);
            if (bean == null) {
                logger.error("Cfg_Guild_battle_bossBean无法找到数据，id = " + fud.getRank() * 100 + 1);
                continue;
            }
            //怪物复活时间
            for (GuildFudBoss boss : fud.getBoss().values()) {

                Cfg_Guild_battle_boss_Bean tempBean = CfgManager.getCfg_Guild_battle_boss_Container().getValueByKey(boss.getBossId());

                GuildActivityMessage.monsterResurgenceTime.Builder resurgenceTime = GuildActivityMessage.monsterResurgenceTime.newBuilder();
                resurgenceTime.setMonsterType(tempBean.getType());
                resurgenceTime.setMonsterModelId(tempBean.getId());
                resurgenceTime.setLevel(0);
                resurgenceTime.setResurgenceTime(fud.getWaitBoss().containsKey(boss.getBossId()) ? boss.getBirthTime() - TimeUtils.Time() : 0);

                detailFudiInfo.addResurgenceTime(resurgenceTime);
            }
            msg.addInfos(detailFudiInfo);

            for (int j = 1; j <= 3; j++) {
                GuildActivityMessage.survival.Builder survival = GuildActivityMessage.survival.newBuilder();
                survival.setType(j);
                survival.setNum(fud.getMonsterNum().getOrDefault(j, 0));
                monsterRemain.addSurvival(survival);
            }
            msg.addRemain(monsterRemain);
        }

        int todayScore = 0;
        if (player.isHaveGuild()) {
            Guild myGuild = Manager.guildsManager.getGuildById(player.getGuildId());
            todayScore = (int) Manager.countManager.getVariant(myGuild, VariantType.GuildFudi);

            for (Cfg_Guild_battle_score_Bean bean : CfgManager.getCfg_Guild_battle_score_Container().getValuees()) {
                if (Manager.countManager.getCount(player, BaseCountType.FudScore, bean.getId()) == 1) {
                    msg.addRewards(bean.getId());
                }
            }
        }
        msg.setTodayScore(todayScore);
        msg.setWorldLevel(GlobalType.getWorldLevel());
        MessageUtils.send_to_player(player, GuildActivityMessage.ResOpenAllBossPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 检查福地红点提示
     */
    @Override
    public void checkFudiBossRedPoint() {
        checkFudiBossRedPoint(null);
    }

    /**
     * 检查福地红点提示
     */
    @Override
    public void checkFudiBossRedPoint(Player player) {
        GuildActivityMessage.ResRedPointInfo.Builder builder = GuildActivityMessage.ResRedPointInfo.newBuilder();
        builder.setOpenServerDay((int) (TimeUtils.getOpenServerTime() / 1000));
        int openServerDay = TimeUtils.getOpenServerDay();
        if (openServerDay != 1) {
            for (GuildFud fud : Manager.guildActivityManager.getFud().values()) {
                MapObject mapObjects = Manager.mapManager.getMap(fud.getMapId());
                if (mapObjects == null) {
                    continue;
                }
                if (fud.getBoss().size() > fud.getWaitBoss().size()) {
                    builder.addType(fud.getRank());
//                    logger.info("仙盟福地 id={}, BossSize={} ", fud.getRank(), mapObjects.getMonsters().size());
                }
            }
        }
        if (player != null) {
            MessageUtils.send_to_player(player, GuildActivityMessage.ResRedPointInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        } else {
            List<Player> onLines = Manager.playerManager.getOnLines();
            MessageUtils.send_to_players(onLines, GuildActivityMessage.ResRedPointInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray(), 0L);
        }

    }

    /**
     * 领取日常奖励
     */
    @Override
    public void dayScoreReward(Player player, GuildActivityMessage.ReqDayScoreReward messInfo) {
        int rewardId = messInfo.getId();
        GuildActivityMessage.ResDayScoreReward.Builder msg = GuildActivityMessage.ResDayScoreReward.newBuilder();
        msg.setId(rewardId);
        msg.setSuccess(getDayScoreReward(player, rewardId));
        MessageUtils.send_to_player(player, GuildActivityMessage.ResDayScoreReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 领取日常奖励
     *
     * @param player   玩家
     * @param rewardId 奖励id
     * @return 是否领取成功
     */
    private boolean getDayScoreReward(Player player, int rewardId) {
        if (!player.isHaveGuild()) {
            return false;
        }
        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());

        long count = Manager.countManager.getCount(player, BaseCountType.FudScore, rewardId);
        if (count == 1) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GUILD_ACTIVITY_BOSS_HASREWARD);
            return false;
        }
        long score = Manager.countManager.getVariant(guild, VariantType.GuildFudi);
        //分数不达标
        if (rewardId > score) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GUILD_ACTIVITY_BOSS_SCORE_NOT_ENOUGH);
            return false;
        }
        //找配置表数据
        Cfg_Guild_battle_score_Bean bean = CfgManager.getCfg_Guild_battle_score_Container().getValueByKey(rewardId);
        if (bean == null) {
            logger.error("Cfg_Guild_battle_scoreBean找不到数据，id = " + rewardId);
            return false;
        }

        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < bean.getItem().size(); i++) {
            ReadArray<Integer> array = bean.getItem().get(i);
            if (array.get(3) != player.getCareer() && array.get(3) != 9) {
                continue;
            }
            Item item = Item.createItem(array.get(0), array.get(1), array.get(2) == 1);
            if (item != null) {
                itemList.add(item);
            }
        }
        if (!Manager.backpackManager.manager().addItems(player, itemList, ItemChangeReason.GuildActivityDayRewardGet, IDConfigUtil.getLogId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_EQUIP_UNWEAR_NOBAG);
            return false;
        }
        Manager.countManager.setCount(player, BaseCountType.FudScore, rewardId, Count.RefreshType.CountType_Day, 1);
        Manager.saveThreadManager.getOtherServerSave().deal(guild.toGuildBean(), DbSqlName.GUILD_UPDATE, SaveServer.UPDATE);
        return true;
    }

    /**
     * 获取当前宗派的怪物关注列表
     */
    private List<Integer> getGuildAttention(Player player, int baseKey) {
        List<Integer> list = new ArrayList<>();
        for (int i = baseKey * 100 + 1; i <= baseKey * 100 + Global.GuildBattleBoss_MapMaxMonster; i++) {
            Cfg_Guild_battle_boss_Bean bean = CfgManager.getCfg_Guild_battle_boss_Container().getValueByKey(i);
            if (bean == null) {
                continue;
            }
            if (player.getFuDiFollowedBossList().contains(bean.getId())) {
                list.add(bean.getId());
            }
        }
        return list;
    }

    /**
     * 关注怪物
     */
    @Override
    public void attentionMonster(Player player, int bossId, int type) {
        if (type == 1) {
            if (!player.getFuDiFollowedBossList().contains(bossId)) {
                player.getFuDiFollowedBossList().add(bossId);
            }
        } else {
            if (player.getFuDiFollowedBossList().contains(bossId)) {
                player.getFuDiFollowedBossList().remove((Integer) bossId);
            }
        }
        GuildActivityMessage.ResAttentionMonster.Builder msg = GuildActivityMessage.ResAttentionMonster.newBuilder();
        msg.setAttention(bossId);
        msg.setType(type);
        MessageUtils.send_to_player(player, GuildActivityMessage.ResAttentionMonster.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 返回福地夺宝积分
     */
    @Override
    public void guildNowScore(Player player) {
        GuildActivityMessage.ResSnatchPanel.Builder msg = GuildActivityMessage.ResSnatchPanel.newBuilder();
        int score = 0;
        if (player.isHaveGuild()) {
            Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
            score = (int) Manager.countManager.getVariant(guild, VariantType.GuildFudi);
        }
        msg.setGuildScore(score);
        MessageUtils.send_to_player(player, GuildActivityMessage.ResSnatchPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 面板就绪，客户端要求在面板初始化以后再发送消息
     */
    @Override
    public void panelReady(Player player) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        GuildActivitySnatchMapInfo mapInfo = MapParam.getGuildActivitySnatchMapData(map);
        mapInfo.getPanelReady().add(player.getId());

        //发消息
        GuildActivityMessage.ResSynMonster.Builder msg = GuildActivityMessage.ResSynMonster.newBuilder();
        msg.setDegree(mapInfo.getLoop());
        msg.setMaxDegree(mapInfo.getMaxLoop());
        msg.setMonsterRemain(map.getMonsters().size());
        MessageUtils.send_to_player(player, GuildActivityMessage.ResSynMonster.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        int top = 0;
        long harm = 0L;
        GuildActivityMessage.ResSynHarmRank.Builder rankMsg = GuildActivityMessage.ResSynHarmRank.newBuilder();
        for (GuildActivitySnatchRank rank : mapInfo.getRanks()) {
            GuildActivityMessage.harmRank.Builder harmMsg = GuildActivityMessage.harmRank.newBuilder();
            harmMsg.setTop(rank.getRank());
            harmMsg.setName(rank.getName());
            harmMsg.setHarm(rank.getHarm());
            rankMsg.addRank(harmMsg);
            if (rank.getPlayerId() == player.getId()) {
                top = rank.getRank();
                harm = rank.getHarm();
            }
        }
        rankMsg.setMyRank(top);
        rankMsg.setMyHarm(harm);
        MessageUtils.send_to_player(player, GuildActivityMessage.ResSynHarmRank.MsgID.eMsgID_VALUE, rankMsg.build().toByteArray());
    }

    /**
     * 宗派活动--福地称号，除去特殊称号后宗派内的成员的排名
     */
    @Override
    public void rank(long lastCheckTime, long now, boolean isGM) {
        if (ServerParamCommon.isFuDiForeverTitle()) {
            return;
        }
        if (isFuDiOver()) {
            guildMemberRankRef(true);
            ServerParamCommon.setFuDiForeverTitle();
        }
        if (!Manager.dailyActiveManager.deal().isOpen(DailyActiveDefine.FUD_ACTIVITY_BOSS.getValue())) {
            return;
        }

        //福地第一天只展示不开放
        if (TimeUtils.getOpenServerDay() == 1) {
            return;
        }

        //每天重新分配宗派
        int minute = TimeUtils.getDayOfHour(now) * 60 + TimeUtils.getDayOfMin(now);
        int nextNinute = -1;
        for (int i = 0; i < Global.GuildBattleBoss_Openingtime.size(); i++) {
            GuildFudAlloc alloc = GuildFudAlloc.find(i);
            if (alloc == null || Manager.countManager.getBooleanCountValue(Manager.guildActivityManager, alloc)) {
                continue;
            }
            int allocMinute = Global.GuildBattleBoss_Openingtime.get(i);
            if (allocMinute <= minute) {
                Manager.countManager.setBooleanCountValue(Manager.guildActivityManager, alloc, true);
                logger.info("福地分配 allocMinute={} time={}", allocMinute, TimeUtils.format2string(TimeUtils.Time()));
                allocGuildActivityPlace();
                for (Long mapId : DailyActiveManager.dailyMap.get(DailyActiveDefine.FUD_ACTIVITY_BOSS.getValue()).values()) {
                    MapObject map = Manager.mapManager.getMap(mapId);
                    if (map != null) {
                        Manager.mapManager.base(ScriptEnum.GuildFudScript).call("allocGuildActivityPlace", map);
                    }
                }
                Manager.guildActivityManager.deal().checkFudiBossRedPoint();
            } else {
                nextNinute = allocMinute;
                break;
            }
        }
        // 每小时称号重新分配，前三帮会内部排名刷新
        if (Manager.countManager.getServerVariant(VariantType.GuildFudiTitleReset) == 0) {
            Manager.countManager.setServerVariant(VariantType.GuildFudiTitleReset, 1);
            logger.info("福地小时刷新排名 time={}", TimeUtils.format2string(TimeUtils.Time()));
            guildMemberRankRef(false);
            if (nextNinute > 0) {
                MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, ChatChannel.CHATCHANNEL_CHUANWEN, MessageString.Guild_Battle_Reset_Notice, String.valueOf(nextNinute - minute));
            }
        }
    }

    /**
     * 福地重新分配宗派
     */
    private void allocGuildActivityPlace() {

        for (GuildFud fud : Manager.guildActivityManager.getFud().values()) {
            if (fud.getGuild() == null) {
                continue;
            }
            for (Map.Entry<Long, GuildMember> entry : fud.getGuild().getMembers().entrySet()) {
                Player player = Manager.playerManager.getPlayer(entry.getValue().getId());
                if (player == null) {
                    continue;
                }
                Manager.titleManager.deal().uninstallTitleByType(player, TitleScript.TYPE_FUDI);
            }
        }

        List<Guild> ranks = Manager.rankListManager.getGuildRankScript().sortFuDiGuildRank();
        if (ranks.isEmpty()) {
            return;
        }
        ranks.forEach(g -> g.setFudLimit(false));

        for (int index = 0; index < fudTop.length && index < ranks.size(); index++) {
            Guild guild = ranks.get(index);
            guild.setFudLimit(true);
            ServerParamCommon.setFuDiTopGuild(index + 1, guild.getId());
        }

        // 重置仙盟排行
        Manager.guildActivityManager.deal().load();

        GuildActivityMessage.ResGuildRankChange.Builder builder = GuildActivityMessage.ResGuildRankChange.newBuilder();
        for (GuildFud fud : Manager.guildActivityManager.getFud().values()) {
            Guild guild = fud.getGuild();
            if (guild == null) {
                continue;
            }
            GuildActivityMessage.monsterRemain.Builder info = GuildActivityMessage.monsterRemain.newBuilder();
            info.setGuildId(guild.getId());
            info.setName(guild.getName());
            builder.addInfo(info);

            logger.info("仙盟：" + guild.getName() + "[" + guild.getId() + "]占领福地" + fud.getRank());
        }
        List<Player> players = Manager.playerManager.getOnLines();
        MessageUtils.send_to_players(players, GuildActivityMessage.ResGuildRankChange.MsgID.eMsgID_VALUE, builder.build().toByteArray(), 0L);

    }

    /**
     * 每小时称号重新分配，前三帮会内部排名刷新
     *
     * @param isOver 最后一次奖励称号，将奖励永久称号
     */
    @Override
    public void guildMemberRankRef(boolean isOver) {
        for (GuildFud fud : Manager.guildActivityManager.getFud().values()) {
            guildMemberRankRef(fud, isOver);
        }
    }

    /**
     * 打开仙盟boss面板
     */
    @Override
    public void onReqOpenGuildBossPanel(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }
        GuildBossMessage.ResGuildBossPannel.Builder builder = GuildBossMessage.ResGuildBossPannel.newBuilder();
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
        ConcurrentHashMap<Long, Long> bossPersonDamage = Manager.guildActivityManager.getBossPersonDamage().get(player.getGuildId());
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
        MessageUtils.send_to_player(player, GuildBossMessage.ResGuildBossPannel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 检查仙盟boss开启关闭
     *
     * @param open
     */
    @Override
    public void optGuildBossActivity(boolean open) {

        if (open) {
            Manager.guildActivityManager.getBossPersonDamage().clear();
            Manager.guildActivityManager.getBossGuildDamage().clear();
            Manager.guildActivityManager.getMoneyInspire().clear();
            Manager.guildActivityManager.getGoldInspire().clear();
            Manager.guildActivityManager.getTotalInspire().clear();
            Manager.guildActivityManager.setGuildBossRun(true);
            for (Player player : Manager.playerManager.getOnLines()) {
                syncGuildBossOCTime(player);
            }
        } else {
            Manager.guildActivityManager.setGuildBossRun(false);
        }
    }

    /**
     * 仙盟boss同步开启关闭时间
     *
     * @return
     */
    @Override
    public void syncGuildBossOCTime(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.GuildBoss)) {
            return;
        }
        long now = TimeUtils.Time();
        int nowMin = TimeUtils.getDayOfHour(now) * 60 + TimeUtils.getDayOfMin(now);
        int todayBeginTime = (int) (TimeUtils.getTodayBeginTime() / 1000);
        GuildBossMessage.ResGuildBossOCTime.Builder builder = GuildBossMessage.ResGuildBossOCTime.newBuilder();
        for (int i = 0; i < Global.Guild_Boss_Time.size(); i++) {
            if (nowMin < Global.Guild_Boss_Time.get(i).get(1)) {
                builder.setCloseTime(todayBeginTime + Global.Guild_Boss_Time.get(i).get(1) * 60);
                builder.setOpenTime(todayBeginTime + Global.Guild_Boss_Time.get(i).get(0) * 60);
                break;
            }
        }
        if (!builder.hasOpenTime()) {
            builder.setOpenTime(todayBeginTime + 24 * 60 * 60 + Global.Guild_Boss_Time.get(0).get(0) * 60);
            builder.setCloseTime(todayBeginTime + 24 * 60 * 60 + Global.Guild_Boss_Time.get(0).get(1) * 60);
        }
        MessageUtils.send_to_player(player, GuildBossMessage.ResGuildBossOCTime.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 每小时 重新排序
     *
     * @param fud
     * @param isOver
     */
    private void guildMemberRankRef(GuildFud fud, boolean isOver) {

        Guild guild = fud.getGuild();
        if (guild == null) {
            return;
        }
        HashMap<Long, Integer> history = new HashMap<>();
        //TODO 移除福地称号
        for (Map.Entry<Long, Integer> entry : guild.getTitleRankMap().entrySet()) {
            history.put(entry.getKey(), entry.getValue());
            Player player = Manager.playerManager.getPlayer(entry.getKey());
            if (player != null) {
                Manager.titleManager.deal().uninstallTitleByType(player, TitleScript.TYPE_FUDI);
            }
        }
        //TODO 重新排序
        List<GuildMember> members = guild.sortMember();

        guild.getTitleRankMap().clear();

        for (int i = 1; i <= Global.GuildFightLimit && i <= members.size(); i++) {
            Player player = Manager.playerManager.getPlayer(members.get(i - 1).getId());
            if (player == null) {
                continue;
            }
            int key = fud.getRank() * 100 + i;
            Cfg_Guild_title_Bean bean = Cfg_Guild_title_Container.GetInstance().getValueByKey(key);
            if (bean == null) {
                logger.error("公会前十称号，配置错误，key：" + key);
                continue;
            }
            guild.getTitleRankMap().put(player.getId(), i);
            //福地称号
            int titleId = isOver ? bean.getTitle_permanent() : bean.getTitle();

            Manager.titleManager.deal().useTitleItem(player, titleId, 1, ItemChangeReason.GuildActivityRankGet);
            Manager.titleManager.deal().onReqWearTitle(player, titleId);
            Manager.playerManager.savePlayer(player, SavePlayerLevel.HalfMinLater);
            if (isOver) {
                logger.info("福地永久称号已发放 player={} title={}", player, titleId);
            }

        }
        // 排名如果变化，则邮件通知
        for (Map.Entry<Long, Integer> entry : guild.getTitleRankMap().entrySet()) {
            int lastRank = history.getOrDefault(entry.getKey(), -1);
            if (lastRank == entry.getValue()) {
                continue;
            }
            String content = MessageString.GuildMemberRank_Content + "@_@" + entry.getValue();
            Manager.mailManager.sendMailToPlayer(entry.getKey()
                    , MailType.GuildMail
                    , MessageString.System
                    , MessageString.GuildMemberRank_Title
                    , content);
        }
    }

    /**
     * 解散仙盟
     *
     * @param guildId
     */
    @Override
    public void dissolveGuild(long guildId) {
        GuildFud fud = Utils.findOne(Manager.guildActivityManager.getFud().values(), g -> g.getGuild() != null && g.getGuild().getId() == guildId);
        if (fud != null) {
            rank(1, 1, false);
        }
    }

    /**
     * 退出仙盟
     *
     * @param guildId
     * @param player
     */
    @Override
    public void quitGuild(long guildId, Player player) {
        if (ServerParamCommon.isFuDiForeverTitle()) {
            return;
        }
        GuildFud fud = Utils.findOne(Manager.guildActivityManager.getFud().values(), g -> g.getGuild() != null && g.getGuild().getId() == guildId);
        if (fud == null) {
            return;
        }
        Guild guild = fud.getGuild();
        if (guild == null) {
            return;
        }
        // 当前这个人有没有在排行榜中
        if (!guild.getTitleRankMap().containsKey(player.getId())) {
            return;
        }
        // 宗派内部需要重新排名
        guildMemberRankRef(fud, false);
    }

    private boolean isFuDiOver() {
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.FUD_ACTIVITY_BOSS.getValue());
        int curOpenDay = TimeUtils.getOpenServerDay();
        if (curOpenDay > bean.getCloseTime().get(0)) {
            return true;
        }
        if (curOpenDay < bean.getCloseTime().get(0)) {
            return false;
        }
        long forceEnd = TimeUtils.getTodayBeginTime() + bean.getCloseTime().get(1) * 60 * 1000L;
        return TimeUtils.Time() > forceEnd;
    }

    /**
     * 请求福地支援
     */
    @Override
    public void onReqFudiHelp(Player player, int cfgId) {
        if (!player.isHaveGuild()) {
            return;
        }
        Cfg_Guild_battle_boss_Bean bean = CfgManager.getCfg_Guild_battle_boss_Container().getValueByKey(cfgId);
        if (bean == null) {
            logger.error("发起支援的福地Boss配置表不存在！！无法发起支援：" + cfgId);
            return;
        }
        Cfg_Clone_map_Bean cloneMapBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(bean.getMapID());
        if (cloneMapBean == null) {
            logger.error("福地boss配置的mapId找不到对应的配置：" + bean.getMapID());
            return;
        }
        Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
        MessageUtils.notify_Chat_To_GuildPlayer(player, guild, true, MessageString.GuildBattlebosstext1,
                cfgId + "", ServerStr.getChatTableName(cloneMapBean.getDuplicate_name()), ServerStr.getChatTableName(bean.getName()));
    }

    /**
     * 请求支援信息有效性
     */
    @Override
    public void onReqFudiCanHelp(Player player, int bossId) {
        Cfg_Guild_battle_boss_Bean bean = CfgManager.getCfg_Guild_battle_boss_Container().getValueByKey(bossId);
        if (bean == null) {
            logger.error("福地boss配置表不存在bossId={}", bossId);
            return;
        }
        GuildFud fud = Manager.guildActivityManager.getFud().get(bean.getMapID());
        if (fud == null) {
            return;
        }
        MapObject map = Manager.mapManager.getMap(fud.getMapId());
        if (map == null) {
            logger.error("福地地图未创建,支援失败！");
            return;
        }
        MapObject curMap = Manager.mapManager.getMap(player.gainMapId());

        if (curMap.getSetting().getType() != 0 && map.getId() != curMap.getId()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_CHANGEMAP_FAILED_COPYMAP);
            return;
        }
        GuildFudBoss boss = fud.getBoss().get(bossId);

        GuildActivityMessage.ResFudiCanHelp.Builder builder = GuildActivityMessage.ResFudiCanHelp.newBuilder();
        builder.setCfgId(bossId);
        builder.setCanHelp(!fud.getWaitBoss().containsKey(boss.getBossId()));
        MessageUtils.send_to_player(player, GuildActivityMessage.ResFudiCanHelp.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 获取本仙盟福地攻打信息
     *
     * @param player
     */
    @Override
    public void reqMyFightingBoss(Player player) {

        GuildActivityMessage.ResMyFightingBoss.Builder message = GuildActivityMessage.ResMyFightingBoss.newBuilder();

        Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
        if (guild == null) {
            MessageUtils.send_to_player(player, GuildActivityMessage.ResMyFightingBoss.MsgID.eMsgID_VALUE, message.build().toByteArray());
            return;
        }
        HashMap<Integer, GuildFudiAttack> hashMap = Manager.guildActivityManager.getFudiAttacking().get(guild.getId());
        if (hashMap == null) {
            MessageUtils.send_to_player(player, GuildActivityMessage.ResMyFightingBoss.MsgID.eMsgID_VALUE, message.build().toByteArray());
            return;
        }
        for (GuildFudiAttack attacking : hashMap.values()) {
            GuildActivityMessage.fightingBossInfo.Builder monster = GuildActivityMessage.fightingBossInfo.newBuilder();
            monster.setConfigId(attacking.getBossId());
            monster.setLevel(attacking.getLevel());
            monster.setHp(attacking.getHp());
            //1=本盟攻打中 2=我在攻打中
            monster.setType(attacking.getAttacking().containsKey(player.getId()) ? 2 : 1);
            message.addBoss(monster);
        }
        MessageUtils.send_to_player(player, GuildActivityMessage.ResMyFightingBoss.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 加载仙三数据
     */
    @Override
    public void load() {

        for (Cfg_Guild_battle_boss_Bean bean : CfgManager.getCfg_Guild_battle_boss_Container().getValuees()) {
            int rank = bean.getGroup();
            int cloneId = bean.getMapID();
            GuildFud fud = Manager.guildActivityManager.getFud().get(cloneId);
            if (fud == null) {
                fud = new GuildFud();
                fud.setCloneId(cloneId);
                fud.setRank(rank);
                Manager.guildActivityManager.getFud().put(fud.getCloneId(), fud);
            }
            fud.setGuild(null);
            long guildId = ServerParamCommon.getFuDiTopGuild(fud.getRank());
            if (guildId != 0) {
                Guild guild = Manager.guildsManager.getGuildById(guildId);
                fud.setGuild(guild);
            }
            GuildFudBoss boss = fud.getBoss().get(bean.getId());
            if (boss == null) {
                boss = new GuildFudBoss();
                boss.setBossId(bean.getId());
                fud.getBoss().put(boss.getBossId(), boss);
            }
        }
        logger.info("加载本服福地数据完成！");
    }

    /**
     * 是否有
     *
     * @param guildId
     * @return
     */
    @Override
    public boolean haveTopFudi(long guildId) {
        for (GuildFud fud : Manager.guildActivityManager.getFud().values()) {
            if (fud.getGuild() != null && fud.getGuild().getId() == guildId) {
                return true;
            }
        }
        return false;
    }

    /**
     * 自动关注boss
     *
     * @param player
     */
    @Override
    public void autoFollowBoss(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.FuDi)) {
            return;
        }

        if (Manager.countManager.getBooleanCountValue(player, BooleanForever.AutoFollowFudBoss)) {
            return;
        }

        Manager.countManager.setBooleanCountValue(player, BooleanForever.AutoFollowFudBoss, true);

        for (Cfg_Guild_battle_boss_Bean bean : CfgManager.getCfg_Guild_battle_boss_Container().getValuees()) {
            if (bean.getDefaultFollowOpen() == 1) {
                attentionMonster(player, bean.getId(), 1);
            }
        }
        logger.info("自动关注福地boss player={}", player);
    }
}
