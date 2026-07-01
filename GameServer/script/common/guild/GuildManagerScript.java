package common.guild;

import com.data.*;
import com.data.bean.Cfg_Guild_gift_Bean;
import com.data.bean.Cfg_Guild_official_Bean;
import com.data.bean.Cfg_Guild_up_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.game.backpack.structs.Item;
import com.game.bi.manager.BIDefine;
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.Cooldown;
import com.game.cooldown.structs.CooldownTypes;
import com.game.count.structs.BooleanDay;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.db.bean.GuildBean;
import com.game.db.dao.GuildDao;
import com.game.db.dao.GuildMemberDao;
import com.game.guild.log.GuildBaseLog;
import com.game.guild.log.GuildBuildingUpLog;
import com.game.guild.log.GuildChangeRankLog;
import com.game.guild.log.GuildExpLog;
import com.game.guild.script.IGuildManagerScript;
import com.game.guild.structs.*;
import com.game.guildactivity.struct.GuildFud;
import com.game.guildbattle.structs.GuildBattle;
import com.game.mail.manager.MailManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.GameServer;
import com.game.server.thread.SaveServer;
import com.game.structs.GlobalType;
import com.game.task.structs.Task;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.script.IScript;
import game.core.util.CrossState;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommandMessage;
import game.message.CommonMessage;
import game.message.GuildMessage;
import game.message.GuildMessage.ResGuildLogList;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GuildManagerScript implements IGuildManagerScript, IScript {

    private final static Logger log = LogManager.getLogger(GuildManagerScript.class);

    /**
     * 仙盟宝箱
     */
    HashMap<Integer, VariantType> GuildGiftVariantType = new HashMap<>();

    HashMap<Integer, Integer> GuildGiftID = new HashMap<>();

    public GuildManagerScript() {
        GuildGiftVariantType.put(FunctionVariable.CurDayActiveValueCos, VariantType.DailyActivePointCost);
        GuildGiftVariantType.put(FunctionVariable.Daily_ShangJingFunc_Times, VariantType.Daily_ShangJingFunc_Times);
        GuildGiftVariantType.put(FunctionVariable.Daily_Kill_WuJIArea_Boss_Times, VariantType.Daily_Kill_WuJIArea_Boss_Times);
        GuildGiftVariantType.put(FunctionVariable.Daily_Kill_JingJia_Boss_Times, VariantType.Daily_Kill_JingJia_Boss_Times);
        GuildGiftVariantType.put(FunctionVariable.Kill_Guild_Territorial_Boss, VariantType.Kill_Guild_Territorial_Boss);
        GuildGiftVariantType.put(FunctionVariable.Kill_crossfudi_Boss, VariantType.Kill_crossfudi_Boss);
        GuildGiftVariantType.put(FunctionVariable.Recharge_Money_Day, VariantType.Daily_RechargeGold);

        GuildGiftID.put(FunctionVariable.CurDayActiveValueCos, 101);
        GuildGiftID.put(FunctionVariable.Daily_ShangJingFunc_Times, 201);
        GuildGiftID.put(FunctionVariable.Daily_Kill_WuJIArea_Boss_Times, 301);
        GuildGiftID.put(FunctionVariable.Daily_Kill_JingJia_Boss_Times, 401);
        GuildGiftID.put(FunctionVariable.Kill_Guild_Territorial_Boss, 501);
        GuildGiftID.put(FunctionVariable.Kill_crossfudi_Boss, 502);
        GuildGiftID.put(FunctionVariable.Recharge_Money_Day, 601);
    }

    @Override
    public int getId() {
        return ScriptEnum.GuildManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void tick() {
        if (GameServer.getInstance().IsFightServer()) {
            return;
        }
        Iterator<Map.Entry<Long, Guild>> iterator = Manager.guildsManager.getGuildMap().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Guild> entry = iterator.next();
            Guild guild = entry.getValue();

            if (guild.getMembers().size() == 0) {
                log.error("公会成员丢失,公会数据异常,需要解散公会:" + guild.getId());
                dissolveGuild(guild, 0);
                continue;
            }

            if (guild.getChairMan() == null) {
                log.error("公会会长丢失,公会数据异常,需要解散公会:" + guild.getId());
                dissolveGuild(guild, BIDefine.CTGuildMemberNoChain);
                continue;
            }

            //公会解散
            tickDissolve(guild);

            //公会弹劾
            tickImpeach(guild);

            //检测工会宝箱失效
            tickGuildGift(guild);
            //公会可以弹劾发送邮件
//            tickImpeachMail(guild);
        }
    }

    @Override
    public void reqGuildList(Player player) {
        GuildMessage.ResRecommendGuild.Builder builder = GuildMessage.ResRecommendGuild.newBuilder();
        Iterator<Map.Entry<Long, Guild>> iterator = Manager.guildsManager.getGuildMap().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, Guild> entry = iterator.next();
            Guild guild = entry.getValue();
            GuildMessage.GuildBaseInfo.Builder guildMsg = GuildMessage.GuildBaseInfo.newBuilder();
            guildMsg.setMember(guild.getChairMan().toGuildMemberMsg(guild));
            guildMsg.setGuildId(guild.getId());
            guildMsg.setName(guild.getName());
            guildMsg.setIsAutoJoin(guild.isAutoApply());
            guildMsg.setLimitfight(guild.getLimitFightPoint());
            guildMsg.setLimitLv(guild.getLimitLv());
            guildMsg.setLv(guild.getConstructions().get(GuildSysConfig.TYPE_BASE));
            guildMsg.setMemberNum(guild.getMembers().size());
            guildMsg.setFighting(guild.gainGuildPower());
            guildMsg.setIsApply(guild.getRequestList().contains(player.getId()));
            GuildBattle gb = Manager.guildBattleManager.getGuildbattles().get(guild.getId());
            if (gb != null) {
                guildMsg.setRate(gb.getType());
            } else {
                guildMsg.setRate(0);
            }
            int guildLv = guild.getConstructions().get(GuildSysConfig.TYPE_BASE);
            Cfg_Guild_up_Bean bean = CfgManager.getCfg_Guild_up_Container().getValueByKey(guildLv + GuildSysConfig.TYPE_BASE * 10000);

            guildMsg.setMaxNum(bean.getBase_num() + (guild.isFudLimit() ? Global.Guild_Member_Number : 0));


            builder.addInfoList(guildMsg);
        }
        MessageUtils.send_to_player(player, GuildMessage.ResRecommendGuild.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void reqGuildInfo(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        GuildMember guildMember = guild.getMembers().get(player.getId());
        Cfg_Guild_official_Bean bean = CfgManager.getCfg_Guild_official_Container().getValueByKey(guildMember.getPosition());

        GuildMessage.ResGuildInfo.Builder builder = GuildMessage.ResGuildInfo.newBuilder();
        GuildMessage.GuildInfo.Builder guildMsg = build(guild);
        if (isProxyChairMan(guildMember, guild) || bean.getCanAgree() == 1) {
            GuildMessage.GuildApplyInfo.Builder applyMsg = GuildMessage.GuildApplyInfo.newBuilder();
            for (long playerId : guild.getRequestList()) {
                PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(playerId);
                if (playerWorldInfo == null) {
                    guild.getRequestList().remove(playerId);
                    continue;
                }
                applyMsg.setRoleId(playerId);
                applyMsg.setFighting(playerWorldInfo.getFightPower());
                applyMsg.setLv(playerWorldInfo.getLevel());
                applyMsg.setName(playerWorldInfo.getRolename());
                applyMsg.setCareer(playerWorldInfo.getCareer());
                applyMsg.setHead(MapUtils.getHead(playerWorldInfo));
                guildMsg.addApplys(applyMsg);
            }
        }


        builder.setG(guildMsg);
        builder.setIsGet(Manager.countManager.getBooleanCountValue(player, BooleanDay.GuildDailyReward));
        MessageUtils.send_to_player(player, GuildMessage.ResGuildInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());

    }

    @Override
    public void createGuild(Player player, String name, int icon, String notice) {
        if (player.playerCrossData.crossState != CrossState.PCS_LOCAL) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CrossUnableCreate);
            log.error("玩家正在跨服中不能创建公会" + player.getId());
            return;
        }

        if (player.getGuildId() > 0) {
            log.error("玩家数据错误：存在公会Id，不存在公会实体 需要查验：playerId" + player.getId() + " GuildId:" + player.getGuildId());
            return;
        }

        if (player.getVipLv() < Global.Create_Guild_Vip_Limit) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.PERSONBOSS_VIPLEVELNOTZU);
            return;
        }
        if (Utils.isContainsShielding_symbol(name)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildCreateFailed_Shielding_symbol);
            return;
        }

        if (Utils.isForbiddenStr(name)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildCreateFailed_NameForbiddenStr);
            return;
        }

        if (notice.length() > Global.GuildNoticeMaxLength_G) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildNotice_WrongLength);
            return;
        }

        if (Utils.isForbiddenStrNoSpace(notice)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildCreateFailed_NoticeForbiddenStr);
            return;
        }

        int length = name.length();
        if (!(length >= Global.GuildNameLength_G.get(1) && length <= Global.GuildNameLength_G.get(0))) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildCreateFailed_LengthWrong);
            return;
        }

        if (!Manager.currencyManager.manager().canDecItemCoin(player, Global.GuildCreateMoney.get(1), Global.GuildCreateMoney.get(0))) {
            return;
        }

        if (Manager.guildsManager.isHaveName(name)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildCreateFailed_DupName);
            return;
        }

        long actionId = IDConfigUtil.getLogId();
        Manager.currencyManager.manager().onDecItemCoin(player, Global.GuildCreateMoney.get(1), ItemChangeReason.CreateGuildDec, actionId, Global.GuildCreateMoney.get(0));

        Guild guild = addGuild(player, name, icon, notice);
        if (guild != null) {
//            MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.GuildCreateSuccess, player.getName(), name, Utils.makeUrlStr(MessageString.GuildCreateSuccess));
            MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, ChatChannel.CHATCHANNEL_WORLD, MessageString.GuildCreateSuccess, player.getName(), name, Utils.makeUrlStr(MessageString.GuildCreateSuccess));

            //增加事件
            Manager.taskManager.deal().acceptTask(player, Task.BRANCH_TASK, 0, 0, false);
            Manager.taskManager.deal().computeTask(player, Task.GUILD_TASK, false, false);
            Manager.controlManager.operate(player, FunctionVariable.Joinguild, 0);

            Manager.biManager.getScript().biGuildMember(player, guild.getId(), guild.getLevel(), BIDefine.CTGuildMemberCreate, GuildSysConfig.TYPE_MASTER, 1, guild.getFightPower());

            //增加日志
            writeBaseLog(player, guild, 0, "", GuildSysConfig.BaseLog_type1, actionId, "");
            GuildMessage.ResCreateGuild.Builder msg = GuildMessage.ResCreateGuild.newBuilder();
            msg.setG(build(guild));
            msg.setIsGet(Manager.countManager.getBooleanCountValue(player, BooleanDay.GuildDailyReward));
            MessageUtils.send_to_player(player, GuildMessage.ResCreateGuild.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            List<String> params = new ArrayList<>();
            params.add(guild.getId() + "");
            Manager.biManager.get4399Script().chatInfoTo4399(player, ChatManager.GUILD_NAME, "", name, params);
            Manager.biManager.get4399Script().chatInfoTo4399(player, ChatManager.GUILD_NOTICE, "", notice, params);

            //红包处理
            Manager.redPacketManager.joinGuild(player);
        } else {
            // TODO: 2020/1/15 lw
            //退钱
        }
    }

    public void reqApplyGuild(Player player, List<Long> guildIds) {
        if (player.isHaveGuild()) {
            return;
        }

        if (guildIds.size() == 0) {
            return;
        }

        int quitNum = (int) Manager.countManager.getVariant(player, VariantType.GuildQuitFree);

        if (quitNum > 1) {
            if (player.getQuitGuildTime() + Global.GuildQuitCoolingTime * GlobalType.MILLIS_PER_MINUTE > TimeUtils.Time()) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.JoinGuildFailed_CDing, Global.GuildQuitCoolingTime + "");
                return;
            }
        }

        GuildMessage.ResJoinGuild.Builder builder = GuildMessage.ResJoinGuild.newBuilder();

        List<Long> canJoinList = new ArrayList<>();

        List<Long> applyDuplicateList = new ArrayList<>();

        for (long guildId : guildIds) {
            Guild guild = Manager.guildsManager.getGuildMap().get(guildId);
            if (guild == null) {
                continue;
            }

            int guildLv = guild.getConstructions().get(GuildSysConfig.TYPE_BASE);
            Cfg_Guild_up_Bean bean = CfgManager.getCfg_Guild_up_Container().getValueByKey(guildLv + GuildSysConfig.TYPE_BASE * 10000);

            if (guild.getMembers().size() >= bean.getBase_num() + (guild.isFudLimit() ? Global.Guild_Member_Number : 0)) {
                continue;
            }

            if (guild.getLimitLv() > player.getLevel()) {
                continue;
            }

            if (guild.getLimitFightPoint() > player.getFightPoint()) {
                continue;
            }

            if (guild.getRequestList().contains(player.getId())) {
                applyDuplicateList.add(guildId);
                continue;
            }

            if (guild.isAutoApply()) {
                if (joinGuild(player, guild) != null) {
                    builder.setG(build(guild));
                    builder.setIsGet(Manager.countManager.getBooleanCountValue(player, BooleanDay.GuildDailyReward));
                    MessageUtils.send_to_player(player, GuildMessage.ResJoinGuild.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                }
                return;
            } else {
                canJoinList.add(guildId);
            }
        }
        if (applyDuplicateList.isEmpty() && canJoinList.isEmpty()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Donate_Today_Already);
            return;
        }

        builder.addAllIds(guildIds);

        MessageUtils.send_to_player(player, GuildMessage.ResJoinGuild.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        GuildMessage.ResGuildApplyPlayer.Builder builder1 = GuildMessage.ResGuildApplyPlayer.newBuilder();
        GuildMessage.GuildApplyInfo.Builder applyMsg = GuildMessage.GuildApplyInfo.newBuilder();
        applyMsg.setRoleId(player.getId());
        applyMsg.setFighting(player.getFightPoint());
        applyMsg.setLv(player.getLevel());
        applyMsg.setName(player.getName());
        applyMsg.setCareer(player.getCareer());
        applyMsg.setHead(MapUtils.getHead(player));
        builder1.setApply(applyMsg);

        for (long guildId : canJoinList) {
            Guild guild = Manager.guildsManager.getGuildMap().get(guildId);
            guild.getRequestList().add(player.getId());

            if (guild.getRequestList().size() > Global.Guild_Application_cap) {
                guild.getRequestList().remove(0);
            }
            //发送给仙盟有权限处理申请列表的人 (推送功能没有太大必要 消耗太大)
            for (GuildMember guildMember : guild.getMembers().values()) {
                Cfg_Guild_official_Bean b = CfgManager.getCfg_Guild_official_Container().getValueByKey(guildMember.getPosition());
                if (isProxyChairMan(guildMember, guild) || b.getCanAgree() == 1) {
                    Player p = Manager.playerManager.getPlayerOnline(guildMember.getId());
                    if (p != null) {
                        MessageUtils.send_to_player(p, GuildMessage.ResGuildApplyPlayer.MsgID.eMsgID_VALUE, builder1.build().toByteArray());
                    }
                }
            }
        }

    }

    @Override
    public void reqApplyDelGuild(Player player, List<Long> aId, boolean flag) {
        if (!player.isHaveGuild()) {
            return;
        }

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        GuildMember guildMember = guild.getMembers().get(player.getId());
        Cfg_Guild_official_Bean bean = CfgManager.getCfg_Guild_official_Container().getValueByKey(guildMember.getPosition());
        if (isProxyChairMan(guildMember, guild) || bean.getCanAgree() == 1) {
            GuildMessage.ResGuildMemeberList.Builder membersBuilder = GuildMessage.ResGuildMemeberList.newBuilder();

            for (Long playerId : aId) {
                guild.getRequestList().remove(playerId);
                if (flag) {
                    Player dealPlayer = Manager.playerManager.getPlayer(playerId);
                    if (dealPlayer == null) {
                        continue;
                    }

                    if (dealPlayer.getGuildId() != 0) {
                        continue;
                    }

                    GuildMember otherMember = joinGuild(dealPlayer, guild);
                    if (otherMember != null) {
                        membersBuilder.addInfoList(otherMember.toGuildMemberMsg(guild));
                    }
                }
            }

            GuildMessage.ResDealApplyInfo.Builder dealBuilder = GuildMessage.ResDealApplyInfo.newBuilder();
            dealBuilder.addAllRoleId(aId);
            MessageUtils.send_to_player(player, GuildMessage.ResDealApplyInfo.MsgID.eMsgID_VALUE, dealBuilder.build().toByteArray());

            if (membersBuilder.getInfoListCount() != 0) {
                MessageUtils.send_to_player(player, GuildMessage.ResGuildMemeberList.MsgID.eMsgID_VALUE, membersBuilder.build().toByteArray());
            }
        }
    }

    @Override
    public void reqChangeGuildName(Player player, String name) {
        if (!player.isHaveGuild()) {
            return;
        }

        if (!Manager.backpackManager.manager().canDeleteItemNum(player, Global.GuildChangeNameItem, 1)) {
            return;
        }

        if (Manager.guildsManager.isHaveName(name)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildCreateFailed_DupName);
            return;
        }

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        GuildMember guildMember = guild.getMembers().get(player.getId());
        Cfg_Guild_official_Bean bean = CfgManager.getCfg_Guild_official_Container().getValueByKey(guildMember.getPosition());

        if (!isProxyChairMan(guildMember, guild) && bean.getCanAlter() == 0) {
            return;
        }

        if (Utils.isContainsShielding_symbol(name)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildCreateFailed_Shielding_symbol);
            return;
        }

        if (Utils.isForbiddenStr(name)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildCreateFailed_NameForbiddenStr);
            return;
        }

        int length = name.length();
        if (!(length >= Global.GuildNameLength_G.get(1) && length <= Global.GuildNameLength_G.get(0))) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildCreateFailed_LengthWrong);
            return;
        }

        Manager.backpackManager.manager().onRemoveItem(player, Global.GuildChangeNameItem, 1, ItemChangeReason.ChangeGuildNameDec, 0L);

        String oldName = guild.getName();
        Manager.guildsManager.getGuildName().remove(oldName);
        Manager.guildsManager.getGuildName().put(name, guild.getId());
        guild.setName(name);

        Manager.biManager.get4399Script().chatInfoTo4399(player, ChatManager.GUILD_NAME, "", name, new ArrayList<>());

        Manager.saveThreadManager.getOtherServerSave().deal(guild.toGuildBean(), DbSqlName.GUILD_CHANGE_GUILDNAME, SaveServer.UPDATE);
        Manager.biManager.get4399Script().updateGuild(guild);

        addGuildLog(guild, MessageString.GuildLog_ChangeName, name);
        writeBaseLog(player, guild, 0, "", GuildSysConfig.BaseLog_type7, 0, oldName);

        GuildMessage.ResChangeGuildName.Builder builder = GuildMessage.ResChangeGuildName.newBuilder();
        builder.setName(name);
        MessageUtils.send_to_player(player, GuildMessage.ResChangeGuildName.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void reqChangeGuildRank(Player player, long roleId, int rank) {
        if (!player.isHaveGuild()) {
            return;
        }

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        GuildMember guildMember = guild.getMembers().get(player.getId());

        GuildMember setGuildMember = guild.getMembers().get(roleId);

        if (setGuildMember == null) {
            return;
        }

        if (rank == GuildSysConfig.TYPE_MASTER && guildMember.getPosition() == GuildSysConfig.TYPE_MASTER
                && setGuildMember.getPosition() == GuildSysConfig.TYPE_VICE_MASTER) {
            //转让会长
            guildMember.setPosition(GuildSysConfig.TYPE_MEMBER);
            guild.setChairMan(setGuildMember);
            broadCastGuild(player, guild.getId(), guild.getName(), guildMember.getPosition());
            Manager.saveThreadManager.getOtherServerSave().deal(guildMember.toGuildMemberBean(), DbSqlName.GUILDMEMBER_UPDATE, SaveServer.UPDATE);
        } else {
            Cfg_Guild_official_Bean bean = CfgManager.getCfg_Guild_official_Container().getValueByKey(guildMember.getPosition());

            if (!isProxyChairMan(guildMember, guild) && bean.getCanSetOfficial() == 0) {
                return;
            }

            if (isProxyChairMan(guildMember, guild)) {
                if (roleId == guild.getChairMan().getId()) {
                    return;
                }
            } else {
                if (setGuildMember.getPosition() >= guildMember.getPosition()) {
                    return;
                }
            }

            Cfg_Guild_official_Bean rankBean = CfgManager.getCfg_Guild_official_Container().getValueByKey(rank);
            if (rankBean == null) {
                log.error("Cfg_Guild_official_Bean无法找到指定数据，id = " + rank + "，player = " + player.getId());
                return;
            }

            if (rank == GuildSysConfig.TYPE_MASTER || rankBean.getNum() <= guild.gainRankCount(rank)) {
                return;
            }
        }

        setGuildMember.setPosition(rank);
        Manager.saveThreadManager.getOtherServerSave().deal(setGuildMember.toGuildMemberBean(), DbSqlName.GUILDMEMBER_UPDATE, SaveServer.UPDATE);
        addGuildLog(guild, MessageString.GuildLog_RankChange, setGuildMember.getPlayerWorldInfo().getRolename(), player.getName(), getRankTitle(rank));
        writeChangeRankLog(guild.getId(), guildMember.getPosition(), player.getId(), roleId, rank);

        GuildBattle gb = Manager.guildBattleManager.getGuildbattles().get(guild.getId());
        if (gb != null) {
            if (gb.getType() == 1 && (setGuildMember.getPosition() == GuildSysConfig.TYPE_MASTER || (gb.getRank() == 1 && setGuildMember.getPosition() == GuildSysConfig.TYPE_VICE_MASTER))) {//公会职位变更
                sysPublicGuildBattleInfo(guild, gb);
            }
        }

        GuildMessage.ResPlayerGuildRankChange.Builder builder = GuildMessage.ResPlayerGuildRankChange.newBuilder();
        builder.setRoleId(roleId);
        builder.setRank(rank);
        MessageUtils.send_to_player(player, GuildMessage.ResPlayerGuildRankChange.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        Player setPlayer = Manager.playerManager.getPlayerCache(setGuildMember.getId());
        Manager.biManager.getScript().biGuildMember(setPlayer, guild.getId(), guild.getLevel(), BIDefine.CTGuildMemberRankChange, rank, guild.getMembers().size(), guild.getFightPower());
        if (setPlayer.isOnline()) {
            broadCastGuild(setPlayer, guild.getId(), guild.getName(), setGuildMember.getPosition());
        }
    }


    @Override
    public void reqChangeGuildSetting(Player player, boolean isAutoApply, int limitLv, long limitFightPoint, int icon, String notice) {
        if (!player.isHaveGuild()) {
            return;
        }

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());

        if (!guild.getNotice().equals(notice)) {
            if (notice.length() > Global.GuildNoticeMaxLength_G) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildNotice_WrongLength);
                return;
            }

            if (Utils.isForbiddenStrNoSpace(notice)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildCreateFailed_NoticeForbiddenStr);
                return;
            }
        }

        GuildMember guildMember = guild.getMembers().get(player.getId());
        Cfg_Guild_official_Bean bean = CfgManager.getCfg_Guild_official_Container().getValueByKey(guildMember.getPosition());
        if (!isProxyChairMan(guildMember, guild) && bean.getCanAlter() == 0) {
            return;
        }

        String s = "是否需要申请：" + isAutoApply;
        guild.setAutoApply(isAutoApply);
        boolean isAddGuildLog = false;
        if (guild.getLimitLv() != limitLv) {
            guild.setLimitLv(limitLv);
            s += "_限制等级：" + limitLv;
            isAddGuildLog = true;
        }

        if (guild.getLimitFightPoint() != limitFightPoint) {
            guild.setLimitFightPoint(limitFightPoint);
            s += "_限制战斗力：" + limitFightPoint;
            isAddGuildLog = true;
        }

        if (isAddGuildLog) {
            addGuildLog(guild, MessageString.GuildLog_Setting, player.getName(), limitFightPoint + "", limitLv + "");
        }

        if (guild.getIcon() != icon) {
            guild.setIcon(icon);
            s += "_公会图标：" + icon;
        }

        if (!guild.getNotice().equals(notice)) {
            s += "_公会公告：" + notice;
            guild.setNotice(notice);
            List<String> params = new ArrayList<>();
            params.add(guild.getId() + "");
            Manager.biManager.get4399Script().chatInfoTo4399(player, ChatManager.GUILD_NOTICE, "", notice, params);
            addGuildLog(guild, MessageString.GuildLog_Notice, player.getName(), notice);
        }

        Manager.saveThreadManager.getOtherServerSave().deal(guild.toGuildBean(), DbSqlName.GUILD_UPDATE, SaveServer.UPDATE);
        Manager.biManager.get4399Script().updateGuild(guild);

        writeBaseLog(player, guild, 0, "", GuildSysConfig.BaseLog_type6, 0, s);
        GuildMessage.ResChangeGuildSetting.Builder builder = GuildMessage.ResChangeGuildSetting.newBuilder();
        builder.setIsAutoApply(isAutoApply);
        builder.setFightPoint(limitFightPoint);
        builder.setIcon(icon);
        builder.setLv(limitLv);
        builder.setNotice(notice);
        MessageUtils.send_to_player(player, GuildMessage.ResChangeGuildSetting.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void reqGuildConstructUp(Player player, int type) {
        if (!player.isHaveGuild()) {
            return;
        }

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        GuildMember guildMember = guild.getMembers().get(player.getId());

        Cfg_Guild_official_Bean officialBean = CfgManager.getCfg_Guild_official_Container().getValueByKey(guildMember.getPosition());
        if (!isProxyChairMan(guildMember, guild) && officialBean.getCanUp() == 0) {
            return;
        }

        int curLevel = guild.getConstructions().get(type);
        Cfg_Guild_up_Bean bean = CfgManager.getCfg_Guild_up_Container().getValueByKey(type * 10000 + curLevel);

        Cfg_Guild_up_Bean bean1 = CfgManager.getCfg_Guild_up_Container().getValueByKey(10000 + guild.getLevel());
        if (bean1 == null) {
            return;
        }
        if (bean == null) {
            return;
        }
        if (bean.getNeedNum() == 0) {
            return;
        }

        long oldExp = guild.getExp();
        if (oldExp < bean.getNeedNum() + bean1.getMaintenance_fund()) {
            return;
        }
        for (ReadArray<Integer> a : bean.getOther().getValuees()) {
            if (!Manager.controlManager.deal().checkFuncProgress(player, a)) {
                return;
            }
        }
        int nextLevel = curLevel + 1;
        guild.setExp(oldExp - bean.getNeedNum());
        guild.getConstructions().put(type, nextLevel);

        Manager.saveThreadManager.getOtherServerSave().deal(guild.toGuildBean(), DbSqlName.GUILD_UPDATE, SaveServer.UPDATE);
        Manager.biManager.getScript().biGuildUpgrade(player, guild.getId(), guild.getName(), type, curLevel, guild.getConstructions().get(type));
        Manager.biManager.get4399Script().updateGuild(guild);

        addGuildLog(guild, MessageString.GuildLog_Up, getBuildingName(type), "" + (curLevel + 1));
        writeBuildingUpLog(guild.getId(), curLevel + 1, type, player.getId(), (int) guild.getExp(), 0, bean.getNeedNum());
        writeExpLog(guild.getId(), player.getId(), guild.getExp(), -bean.getNeedNum(), ItemChangeReason.GuildBuildExpDec);
        GuildMessage.ResUpBuildingSucces.Builder builder = GuildMessage.ResUpBuildingSucces.newBuilder();
        GuildMessage.GuildBuilding.Builder guildBuild = GuildMessage.GuildBuilding.newBuilder();
        guildBuild.setType(type);
        guildBuild.setLevel(nextLevel);
        builder.setB(guildBuild);
        builder.setGuildMoney(guild.getExp());
        MessageUtils.send_to_player(player, GuildMessage.ResUpBuildingSucces.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        /**
         * 发送工会升级邮件
         */
        guild.getNoticeList().clear();
        if (type == GuildSysConfig.TYPE_BASE) {
            for (Map.Entry<Long, GuildMember> entry : guild.getMembers().entrySet()) {
                guild.getNoticeList().add(entry.getKey());
                Player online = Manager.playerManager.getPlayerOnline(entry.getKey());
                if (online != null) {
                    sendLevelUpMail(guild, online);
                }
            }
        }
    }

    public void sendLevelUpMail(Guild guild, Player player) {
        if (!guild.getNoticeList().contains(player.getId())) {
            return;
        }
        guild.getNoticeList().remove(player.getId());
        String content = MailManager.linkContext(MessageString.guild_level_up_mail_tex, guild.getLevel());
        Manager.mailManager.sendMailToPlayer(player.getId(), MailType.GuildMail, MessageString.System, MessageString.guild_level_up_mail_title, content, null, 0);
    }

    @Override
    public void reqGuildLogList(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        ResGuildLogList.Builder msg = ResGuildLogList.newBuilder();
        List<GuildLog> logs = guild.getLog_list();
        for (GuildLog l : logs) {
            msg.addInfoList(l.toGuildLogInfoBuilder());
        }
        MessageUtils.send_to_player(player, GuildMessage.ResGuildLogList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void leaveGuild(Player player, long roleId) {
        if (!player.isHaveGuild()) {
            return;
        }
        if (Manager.dailyActiveManager.deal().isOpen(DailyActiveDefine.GUILD_LAST_BATTLE.getValue())) {
            if (roleId == 0) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_War_Sign_Out2);
            } else {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_War_Kick_Out2);
            }
            return;
        }
        if (Manager.dailyActiveManager.deal().isOpen(DailyActiveDefine.ACTIVITY_GUILDBATTLE.getValue())) {
            if (roleId == 0) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_War_Sign_Out);
            } else {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_War_Kick_Out);
            }
            return;
        }
        if (Manager.auctionManager.manager().checkIsGuildAuction(player, roleId)) {
            if (roleId <= 0) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_Sign_out_Test);
            } else {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_Kick_out_Test);
            }
            return;
        }
        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());

        GuildMember memberOne = guild.getMembers().get(player.getId());

        if (roleId != 0) {
            //踢人
            Cfg_Guild_official_Bean bean = CfgManager.getCfg_Guild_official_Container().getValueByKey(memberOne.getPosition());
            if (!isProxyChairMan(memberOne, guild) && bean.getCanKick() == 0) {
                return;
            }

            GuildMember memberTwo = guild.getMembers().get(roleId);
            if (isProxyChairMan(memberOne, guild)) {
                if (memberTwo.getPosition() != GuildSysConfig.TYPE_MEMBER) {
                    return;
                }
            } else {
                if (memberOne.getPosition() <= memberTwo.getPosition()) {
                    return;
                }
            }

            Player bePlayer = Manager.playerManager.getPlayer(roleId);
            if (bePlayer == null) {
                log.error("公会玩家不存在:" + roleId + "guildId:" + guild.getId());
                return;
            }
            bePlayer.setQuitGuildTime(TimeUtils.Time());
            quitGuild(bePlayer, guild, memberTwo);

            Manager.countManager.addVariant(bePlayer, VariantType.GuildQuitFree, 1);

            if (bePlayer.isOnline()) {
                MessageUtils.notify_player(bePlayer, Notify.CHAT, MessageString.GuildLog_KickOut, bePlayer.getName(), player.getName());
            }

            addGuildLog(guild, MessageString.GuildLog_KickOut, bePlayer.getName(), player.getName());
            writeBaseLog(player, guild, roleId, bePlayer.getName(), GuildSysConfig.BaseLog_type5, 0, "" + guild.getMembers().size());
            Manager.biManager.getScript().biGuildMember(bePlayer, guild.getId(), guild.getLevel(), BIDefine.CTGuildMemberBeQuit, memberTwo.getPosition(), guild.getMembers().size(), guild.getFightPower());

            String context = MessageString.GUILD_QUIT_MAIL1 + "@_@" + player.getName() + "@_@" + guild.getName();
            Manager.mailManager.sendMailToPlayer(roleId, 1, MessageString.System, MessageString.System, context);
        } else {
            //正常离开
            if (guild.getProxyChairMan() != null && guild.getProxyChairMan().getId() == player.getId()) {
                return;
            }

            if (guild.getChairMan().getId() == player.getId()) {
                if (guild.getMembers().size() != 1) {
                    return;
                }

                GuildBattle guildBattle = Manager.guildBattleManager.getGuildbattles().get(guild.getId());
                //仙盟争霸准备期间
                if (guildBattle != null && guildBattle.getRank() == 0) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildWarNotKickOut);
                    return;
                }

                Manager.biManager.getScript().biGuildMember(player, guild.getId(), guild.getLevel(), BIDefine.CTGuildMemberQuit, memberOne.getPosition(), 0, guild.getFightPower());
                dissolveGuild(guild, 0);
            } else {
                quitGuild(player, guild, memberOne);
                Manager.countManager.addVariant(player, VariantType.GuildQuitFree, 1);
                MessageUtils.notify_player(player, Notify.CHAT, MessageString.GuildLog_Leave, player.getName());
                addGuildLog(guild, MessageString.GuildLog_Leave, player.getName());
                writeBaseLog(player, guild, player.getId(), player.getName(), GuildSysConfig.BaseLog_type4, 0, "" + guild.getMembers().size());
                Manager.biManager.getScript().biGuildMember(player, guild.getId(), guild.getLevel(), BIDefine.CTGuildMemberQuit, memberOne.getPosition(), guild.getMembers().size(), guild.getFightPower());

                String context = MessageString.GUILD_QUIT_MAIL + "@_@" + guild.getName();
                Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.System, context);
            }
            player.setQuitGuildTime(TimeUtils.Time());
        }

        GuildMessage.ResQuitGuild.Builder builder = GuildMessage.ResQuitGuild.newBuilder();
        builder.setRoleId(roleId);
        MessageUtils.send_to_player(player, GuildMessage.ResQuitGuild.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }


    @Override
    public void reqImpeachMaster(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        GuildMember guildMember = guild.getMembers().get(player.getId());
        int res = 0;

        if (guild.getProxyChairMan() != null) {
            if (guild.getProxyChairMan().getId() == player.getId()) {
                res = 1;
            } else {
                res = 3;
            }
        } else {
            long openTime = TimeUtils.getOpenServerTime();
            long now = TimeUtils.Time();

            long chairOffTime = guild.getChairMan().getPlayerWorldInfo().getLastOffTime() * 1000L;

            if (chairOffTime == 0) {
                res = 2;
            } else {
                if ((now - openTime) < 24 * 60 * 60 * 1000) {
                    if ((now - chairOffTime) < 18 * 60 * 60 * 1000) {
                        res = 2;
                    }
                } else if ((now - openTime) < 24 * 2 * 60 * 60 * 1000) {
                    if ((now - chairOffTime) < 24 * 60 * 60 * 1000) {
                        res = 2;
                    }
                } else if ((now - openTime) >= 24 * 2 * 60 * 60 * 1000) {
                    if ((now - chairOffTime) < 24 * 2 * 60 * 60 * 1000) {
                        res = 2;
                    }
                }
            }

            if (res == 0) {
                guild.setProxyChairMan(guildMember);
                guild.setProxyTime(now);
                long actionId = IDConfigUtil.getLogId();
                writeBaseLog(player, guild, 0, "", GuildSysConfig.BaseLog_type8, actionId, "");
            }
        }

        GuildMessage.ResImpeach.Builder builder = GuildMessage.ResImpeach.newBuilder();
        builder.setRes(res);
        MessageUtils.send_to_player(player, GuildMessage.ResImpeach.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void reqGetItem(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }

        if (Manager.countManager.getBooleanCountValue(player, BooleanDay.GuildDailyReward)) {
            return;
        }

        List<Item> items = Item.createItems(Global.Guild_wage.get(0), Global.Guild_wage.get(1), false);

        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.GuildGetItemGet, Global.Guild_wage.get(0));
        } else {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.GuildGetItemGet);
        }

        Manager.countManager.setBooleanCountValue(player, BooleanDay.GuildDailyReward, true);
        GuildMessage.ResReceiveItem.Builder builder = GuildMessage.ResReceiveItem.newBuilder();
        MessageUtils.send_to_player(player, GuildMessage.ResReceiveItem.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void playerOnLine(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }
        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        if (!guild.getMembers().containsKey(player.getId())) {
            log.error(player + "公会找不到玩家,异常,退出公会");
            player.setGuildId(0);
            player.setGuildName("");
            Manager.playerManager.savePlayer(player, SavePlayerLevel.RightNow);
            return;
        }

        if (guild.getChairMan().getId() == player.getId()) {
            if (guild.getProxyTime() != 0) {
                guild.setProxyTime(0);
                guild.setProxyChairMan(null);
                addGuildLog(guild, MessageString.ImpeachFailed_NotInTime);
            }
        }
        sendLevelUpMail(guild, player);

        sendAllGift(player, guild);

    }

    @Override
    public void saveAllGuild() {
        try {
            ConcurrentHashMap<Long, Guild> guilds = Manager.guildsManager.getGuildMap();
            for (Guild g : guilds.values()) {
                Manager.saveThreadManager.getOtherServerSave().deal(g.toGuildBean(), DbSqlName.GUILD_UPDATE, 3);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void changeGuildExp(Guild guild, long exp, int res, long actionId) {
        guild.setExp(guild.getExp() + exp);
        Manager.saveThreadManager.getOtherServerSave().deal(guild.toGuildBean(), DbSqlName.GUILD_UPDATE, SaveServer.UPDATE);
        Manager.biManager.get4399Script().updateGuild(guild);
        writeExpLog(guild.getId(), actionId, guild.getExp(), exp, res);
    }

    @Override
    public boolean isProxyChairMan(GuildMember guildMember, Guild guild) {
        if (guild.getProxyChairMan() == null) {
            return false;
        }

        if (guild.getProxyChairMan().getId() != guildMember.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public void oneKeyJoinPlayer(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }
        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());

        GuildMember guildMember = guild.getMembers().get(player.getId());

        Cfg_Guild_official_Bean bean = CfgManager.getCfg_Guild_official_Container().getValueByKey(guildMember.getPosition());
        if (!isProxyChairMan(guildMember, guild) && bean.getCanHan() == 0) {
            return;
        }

        if (Manager.cooldownManager.isCooldowning(guild, CooldownTypes.Guild_Recruit_BbSub_cd, null)) {
            long remain = Manager.cooldownManager.getCooldownTime(guild, CooldownTypes.Guild_Recruit_BbSub_cd, null);
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Guild_Recruit_Member_CD, (remain + 999) / 1000);

            return;
        }

        Manager.cooldownManager.addCooldown(guild, CooldownTypes.Guild_Recruit_BbSub_cd, null, Global.Guild_Recruit_BbSub_cd * 1000);
        MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, ChatChannel.CHATCHANNEL_WORLD, MessageString.GuildRecruitingTips, guild.getId() + "", guild.getName(), Utils.makeUrlStr(MessageString.GuildRecruitBySub));
        GuildMessage.ResGuildJoinPlayer.Builder builder = GuildMessage.ResGuildJoinPlayer.newBuilder();
        builder.setCd(TimeUtils.Time());
        MessageUtils.send_to_player(player, GuildMessage.ResGuildJoinPlayer.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void reqEnterGuildBase(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }

        MapObject mapObject = Manager.mapManager.getMap(player.getGuildId(), Global.GuildMapID);

        if (mapObject == null) {
            mapObject = Manager.mapManager.createCopyMap(Global.GuildMapID, 1, player.getGuildId());
        }

        Manager.mapManager.changeMap(player, mapObject.getId(), null, false);
    }

    /**
     * 打开仙盟宝箱
     *
     * @param player
     * @param id
     */
    @Override
    public void reqGuildGiftOpen(Player player, long id) {
        if (!player.isHaveGuild()) {
            return;
        }
        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());

        GuildGift gift = guild.getGuildGift().get(id);
        if (gift == null || gift.getTimeout() < TimeUtils.Time()) {
            log.info("宝箱已过期 id={} player={}", id, player);
            return;
        }
        PlayerWorldInfo sender = Manager.playerManager.getPlayerWorldInfo(gift.getSender());
        if (sender == null) {
            log.info("宝箱已过期 id={} player={}", id, player);
            return;
        }
        if (!gift.getNotes().containsKey(player.getId())) {
            return;
        }
        if (gift.getNotes().get(player.getId()) > 0) {
            log.info("宝箱已领取 id={} player={}", id, player);
        }
        gift.getNotes().put(player.getId(), 1);
        //发放奖励
        Cfg_Guild_gift_Bean bean = CfgManager.getCfg_Guild_gift_Container().getValueByKey(gift.getGiftId());

        //根据权重随机奖励
        ReadArray<Integer> box = RandomUtils.random(bean.getReward().getValuees(), r -> r.get(2));

        List<Item> items = Item.createItems(box.get(0), box.get(1), false);

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.GuildGiftGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.GuildGiftGet);
        }
        //保存领取记录
        List<ReadIntegerArray> rewards = new ArrayList<>();
        ReadIntegerArray temp = new ReadIntegerArray(box.getValue());
        rewards.add(temp);
        gift.getHistory().put(player.getId(), rewards);
        //更新
        sendGift(player, gift);
    }

    void sendAllGift(Player player, Guild guild) {
        //通知仙盟宝箱信息
        GuildMessage.ResGuildGiftList.Builder message = GuildMessage.ResGuildGiftList.newBuilder();
        for (GuildGift gift : guild.getGiftHistory()) {
            PlayerWorldInfo sender = Manager.playerManager.getPlayerWorldInfo(gift.getSender());
            if (sender == null) {
                continue;
            }
            message.addHistory(pack(gift));
        }

        for (GuildGift gift : guild.getGuildGift().values()) {
            PlayerWorldInfo sender = Manager.playerManager.getPlayerWorldInfo(gift.getSender());
            if (sender == null) {
                continue;
            }
            if (gift.getNotes().containsKey(player.getId())) {
                message.addGifts(pack(player, gift));
            }
        }
        MessageUtils.send_to_player(player, GuildMessage.ResGuildGiftList.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 发送 宝箱更新
     *
     * @param player
     * @param gift
     */
    void sendGift(Player player, GuildGift gift) {
        if (player == null) {
            return;
        }
        GuildMessage.ResGuildGiftUpdate.Builder message = GuildMessage.ResGuildGiftUpdate.newBuilder();
        message.setGift(pack(player, gift));
        MessageUtils.send_to_player(player, GuildMessage.ResGuildGiftUpdate.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    GuildMessage.GuildGift.Builder pack(Player player, GuildGift gift) {
        List<ReadIntegerArray> reward = gift.getHistory().getOrDefault(player.getId(), new ArrayList<>());
        List<CommonMessage.ShowItemInfo> show = new ArrayList<>();
        for (ReadArray<Integer> box : reward) {
            CommonMessage.ShowItemInfo.Builder item = CommonMessage.ShowItemInfo.newBuilder();
            item.setModelId(box.get(0));
            item.setCount(box.get(1));
            show.add(item.build());
        }
        PlayerWorldInfo sender = Manager.playerManager.getPlayerWorldInfo(gift.getSender());
        GuildMessage.GuildGift.Builder mGift = GuildMessage.GuildGift.newBuilder();
        mGift.setId(gift.getId());
        mGift.setGift(gift.getGiftId());
        mGift.setTimeOut(gift.getTimeout());
        mGift.setSender(sender.getRolename());
        mGift.addAllReward(show);
        return mGift;
    }

    GuildMessage.GuildGiftHistory.Builder pack(GuildGift gift) {
        PlayerWorldInfo sender = Manager.playerManager.getPlayerWorldInfo(gift.getSender());
        GuildMessage.GuildGiftHistory.Builder history = GuildMessage.GuildGiftHistory.newBuilder();
        history.setSender(sender.getRolename());
        history.setGift(gift.getGiftId());
        history.setTime(gift.getCreate());
        return history;
    }

    /**
     * 更新仙盟宝匣进度
     *
     * @param player
     * @param type
     * @param changeNum
     */
    @Override
    public void incrGiftProgress(Player player, int type, int changeNum) {

        if (!player.isHaveGuild()) {
            return;
        }
        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        GuildMember guildMember = guild.getMembers().get(player.getId());

        if (FunctionVariable.Playertitle == type) {
            Cfg_Guild_gift_Bean bean = Utils.findOne(CfgManager.getCfg_Guild_gift_Container().getValuees(), b -> b.getVariable_ID().get(1) == changeNum);
            if (bean == null) {
                return;
            }
            createGift(guild, player, bean.getID());
            return;
        }
        if (!GuildGiftVariantType.containsKey(type)) {
            log.error("工会宝箱 未知类型 type={}", type);
            return;
        }
        VariantType variantType = GuildGiftVariantType.get(type);
        int id = GuildGiftID.get(type);
        Cfg_Guild_gift_Bean bean = CfgManager.getCfg_Guild_gift_Container().getValueByKey(id);

        long begin = Manager.countManager.getVariant(guildMember, variantType);

        Manager.countManager.addVariant(guildMember, variantType, bean.getRefresh_times() / 60, changeNum);

        long end = Manager.countManager.getVariant(guildMember, variantType);

        if (end >= bean.getVariable_ID().get(1) && begin < bean.getVariable_ID().get(1)) {
            createGift(guild, player, bean.getID());
        }
    }

    /**
     * 新增仙盟宝箱
     *
     * @param guild
     * @param player
     * @param id
     */
    private void createGift(Guild guild, Player player, int id) {

        Cfg_Guild_gift_Bean bean = CfgManager.getCfg_Guild_gift_Container().getValueByKey(id);
        GuildGift gift = new GuildGift();
        gift.setId(IDConfigUtil.getId());
        gift.setGiftId(id);
        gift.setSender(player.getId());
        gift.setCreate(TimeUtils.Time());
        gift.setTimeout(gift.getCreate() + bean.getInvalid_times() * 60 * 1000L);
        guild.getMembers().forEach((key, value) -> gift.getNotes().put(key, 0));

        guild.getGuildGift().put(gift.getId(), gift);

        GuildMessage.ResGuildGiftHistory.Builder message = GuildMessage.ResGuildGiftHistory.newBuilder();
        message.setHistory(pack(gift));
        guild.getGiftHistory().add(gift);

        guild.getMembers().forEach((key, value) ->
                {
                    Player online = Manager.playerManager.getPlayerOnline(key);
                    MessageUtils.send_to_player(online, GuildMessage.ResGuildGiftHistory.MsgID.eMsgID_VALUE, message.build().toByteArray());
                    sendGift(online, gift);
                }
        );
        if (guild.getGiftHistory().size() > 50) {
            guild.getGiftHistory().remove(0);
        }
        log.info("创建工会宝箱 gift={} player={}", gift.getGiftId(), player);
    }

    /**
     * 检测宝箱过期
     *
     * @param guild
     */
    private void tickGuildGift(Guild guild) {

        GuildMessage.ResGuildGiftDelete.Builder message = GuildMessage.ResGuildGiftDelete.newBuilder();
        Iterator<GuildGift> iterator = guild.getGuildGift().values().iterator();
        long curTime = TimeUtils.Time();
        while (iterator.hasNext()) {
            GuildGift next = iterator.next();
            if (next.getTimeout() < curTime) {
                iterator.remove();
                message.addIds(next.getId());
            }
        }
        if (message.getIdsCount() <= 0) {
            return;
        }
        MessageUtils.send_TO_Guild(guild, GuildMessage.ResGuildGiftDelete.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }


    public GuildMessage.GuildInfo.Builder build(Guild guild) {

        Cooldown cooldown = Manager.cooldownManager.getCooldown(guild, CooldownTypes.Guild_Recruit_BbSub_cd, null);

        GuildMessage.GuildInfo.Builder builder = GuildMessage.GuildInfo.newBuilder();
        builder.setGuildId(guild.getId());
        builder.setName(guild.getName());
        builder.setLv(guild.getConstructions().get(GuildSysConfig.TYPE_BASE));
        builder.setNotice(guild.getNotice());
        builder.setGuildMoney(guild.getExp());
        builder.setIcon(guild.getIcon());
        builder.setIsAutoJoin(guild.isAutoApply());
        builder.setLimitLv(guild.getLimitLv());
        builder.setFightLv(guild.getLimitFightPoint());
        builder.setPower(guild.gainGuildPower());
        builder.setRecruitCd(cooldown == null ? 0 : cooldown.getStart());
        Iterator<Map.Entry<Long, GuildMember>> iterator = guild.getMembers().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, GuildMember> entry = iterator.next();
            builder.addMembers(entry.getValue().toGuildMemberMsg(guild));
        }

        Iterator<Map.Entry<Integer, Integer>> iterator1 = guild.getConstructions().entrySet().iterator();

        while (iterator1.hasNext()) {
            Map.Entry<Integer, Integer> entry1 = iterator1.next();
            GuildMessage.GuildBuilding.Builder builder2 = GuildMessage.GuildBuilding.newBuilder();
            builder2.setType(entry1.getKey());
            builder2.setLevel(entry1.getValue());
            builder.addBuilds(builder2);
        }

        for (Map.Entry<Long, Integer> entry : guild.getTitleRankMap().entrySet()) {
            GuildMessage.GuildTitle.Builder b = GuildMessage.GuildTitle.newBuilder();
            b.setRoleId(entry.getKey());
            b.setRank(entry.getValue());
            builder.addTitles(b);
        }

        GuildFud fud = Utils.findOne(Manager.guildActivityManager.getFud().values(), fd -> fd.getGuild() != null && fd.getGuild().getId() == guild.getId());
        builder.setRank(fud == null ? 0 : fud.getRank());

        GuildBattle gb = Manager.guildBattleManager.getGuildbattles().get(guild.getId());
        if (gb != null) {
            builder.setRate(gb.getType());
        } else {
            builder.setRate(0);
        }

        int guildLv = guild.getConstructions().get(GuildSysConfig.TYPE_BASE);
        Cfg_Guild_up_Bean bean = CfgManager.getCfg_Guild_up_Container().getValueByKey(guildLv + GuildSysConfig.TYPE_BASE * 10000);

        builder.setMaxNum(bean.getBase_num() + (guild.isFudLimit() ? Global.Guild_Member_Number : 0));
        return builder;
    }


    private void writeBaseLog(Player p, Guild g, long roleId, String name, byte type, long actionId, String param) {
        GuildBaseLog blog = new GuildBaseLog();
        blog.setPlayer(p);
        blog.setGuildId(g.getId());
        blog.setGuildName(g.getName());
        blog.setActId(roleId);
        blog.setActName(name);
        blog.setType(type);
        blog.setActionId(actionId);
        blog.setParam(param);
        LogService.getInstance().execute(blog);
    }

    private void writeChangeRankLog(long guildId, int rank, long roleId1, long roleId2, int currRank) {
        GuildChangeRankLog gLog = new GuildChangeRankLog();
        gLog.setCurrRank(currRank);
        gLog.setGuildId(guildId);
        gLog.setRank(rank);
        gLog.setRoleId1(roleId1);
        gLog.setRoleId2(roleId2);
        LogService.getInstance().execute(gLog);
    }


    private void writeBuildingUpLog(long guildId, int level, int type, long roleId1, int remaining, long actionId, int expend) {
        GuildBuildingUpLog bLog = new GuildBuildingUpLog();
        bLog.setGuildId(guildId);
        bLog.setLevel(level);
        bLog.setType(type);
        bLog.setRoleId1(roleId1);
        bLog.setRemaining(remaining);
        bLog.setActionId(actionId);
        bLog.setExpend(expend);
        LogService.getInstance().execute(bLog);
    }

    private void writeExpLog(long guildId, long actionId, long exp, long changeExp, int res) {
        GuildExpLog log = new GuildExpLog();
        log.setGuildId(guildId);
        log.setActionId(actionId);
        log.setExp(exp);
        log.setChangeExp(changeExp);
        log.setRes(res);
        log.setTimes(TimeUtils.Time());
        LogService.getInstance().execute(log);
    }

    private String getRankTitle(int rank) {
        if (rank == GuildSysConfig.TYPE_MASTER) {
            return "1&_" + MessageString.GuildRank_Chairman_Str;
        } else if (rank == GuildSysConfig.TYPE_VICE_MASTER) {
            return "1&_" + MessageString.GuildRank_ViceChairman_Str;
        } else if (rank == GuildSysConfig.TYPE_OFFICIAL) {
            return "1&_" + MessageString.GuildRank_Elder_Str;
        } else if (rank == GuildSysConfig.TYPE_MEMBER) {
            return "1&_" + MessageString.GuildRank_Normal_Str;
        }
        return "";
    }

    private String getBuildingName(int type) {
        switch (type) {
            case GuildSysConfig.TYPE_BASE:
                return "1&_" + MessageString.GuildBuilding_Base_Str;
            case GuildSysConfig.TYPE_SHOP:
                return "1&_" + MessageString.GuildBuilding_Shop_Str;
            case GuildSysConfig.TYPE_HOME:
                return "1&_" + MessageString.GuildBuilding_HanLin_Str;
            default:
                return "";
        }
    }

    private void addGuildLog(Guild g, int type, String... info) {
        GuildLog gLog = new GuildLog();
        gLog.setType(type);
        gLog.setInfo(info);
        List<GuildLog> log_list = g.getLog_list();
        if (log_list.size() >= Global.GUILDLOGCOUNT) {
            //删除最后一条数据
            log_list.remove(Global.GUILDLOGCOUNT - 1);
        }
        log_list.add(0, gLog);
        //给公会成员发送聊天消息
        MessageUtils.notify_Chat_To_GuildPlayer(null, g, true, type, info);
    }

    private void quitGuild(Player player, Guild guild, GuildMember guildMember) {
        player.setGuildId(0);
        player.setGuildName("");
        guildMember.setGuildId(0);
        guildMember.setJoinTime(0);
        guildMember.setContribute(0);
        guildMember.setPosition(GuildSysConfig.TYPE_MEMBER);
        guildMember.getCounts().clear();
        guild.getGuildGift().forEach((k, v) -> v.getNotes().remove(guildMember.getId()));

        guild.getMembers().remove(player.getId());
        Manager.playerManager.savePlayer(player, SavePlayerLevel.RightNow);
        Manager.saveThreadManager.getOtherServerSave().deal(guildMember.toGuildMemberBean(), DbSqlName.GUILDMEMBER_UPDATE, SaveServer.UPDATE);
        Manager.biManager.get4399Script().updateGuild(guild);

        Manager.guildActivityManager.deal().quitGuild(guild.getId(), player);
        if (player.isOnline()) {
//            Cfg_Guild_up_Bean bean = CfgManager.getCfg_Guild_up_Container().getValueByKey(GuildSysConfig.TYPE_BASE * 10000 + guild.getLevel());
//            Manager.buffManager.deal().onRemoveBuff(player, bean.getLevel_buff());
            Manager.teamManager.deal().checkTeamBuff(player);
            broadCastGuild(player, 0, "", 0);
        }

        GuildBattle gb = Manager.guildBattleManager.getGuildbattles().get(guild.getId());
        if (gb != null) {
            if (gb.getType() == 1 && gb.getRank() == 1 && guildMember.getPosition() == GuildSysConfig.TYPE_VICE_MASTER) {//天仙仙盟第一名公会的副会长退会检查
                sysPublicGuildBattleInfo(guild, gb);
            }
        }
    }

    private Guild addGuild(Player player, String name, int icon, String notice) {
        long guildId = IDConfigUtil.getId();
        int creteTime = (int) (TimeUtils.Time() / 1000);
        Guild guild = new Guild();
        guild.setId(guildId);
        guild.setName(name);
        guild.setIcon(icon);
        guild.setNotice(notice);
        guild.setAutoApply(true);
        guild.setLimitLv(1);
        guild.setLimitFightPoint(1);
        guild.setCreateTime(creteTime);
        guild.setExp(Global.Guild_funds);

        //初始化公会建筑
        for (int i = 1; i <= GuildSysConfig.TYPE_ALL; i++) {
            guild.getConstructions().put(i, 1);
        }

        //及时写入数据库
        GuildBean guildBean = guild.toGuildBean(player.getId());
        GuildDao guildDao = new GuildDao();
        if (guildDao.insert(DbSqlName.GUILD_INSERT.getName(), guildBean) <= 0) {
            return null;
        }

        GuildMember guildMember = addMember(player, guild, true);
        if (guildMember == null) {
            guildDao.delete(DbSqlName.GUILD_DELETE.getName(), guild.getId());
            return null;
        }

        //保存到内存当中
        Manager.guildsManager.getGuildMap().put(guildId, guild);
        Manager.guildsManager.getGuildName().put(name, guildId);

        guild.getMembers().put(guildMember.getId(), guildMember);
        guild.setChairMan(guildMember);
        player.setGuildId(guildId);
        player.setGuildName(guild.getName());
        Manager.playerManager.savePlayer(player, SavePlayerLevel.RightNow);
        Manager.biManager.get4399Script().createGuild(player, guild);
        Manager.biManager.getScript().biGuildMoney(guild.getId(), guild.getName(), guild.getLevel(), guild.getExp());
        return guild;
    }

    private GuildMember joinGuild(Player player, Guild guild) {
        GuildMember guildMember = addMember(player, guild, false);
        if (guildMember != null) {
            player.setGuildId(guild.getId());
            player.setGuildName(guild.getName());
            Manager.playerManager.savePlayer(player, SavePlayerLevel.RightNow);

            guild.getMembers().put(guildMember.getId(), guildMember);
            addGuildLog(guild, MessageString.GuildLog_Join, player.getName());
            writeBaseLog(player, guild, player.getId(), "", GuildSysConfig.BaseLog_type3, 0, "");
            Manager.biManager.getScript().biGuildMember(player, guild.getId(), guild.getLevel(), BIDefine.CTGuildMemberJoin, guildMember.getPosition(), guild.getMembers().size(), guild.getFightPower());
            Manager.biManager.get4399Script().updateGuild(guild);

            Manager.teamManager.deal().checkTeamBuff(player);
            Manager.controlManager.operate(player, FunctionVariable.Joinguild, 0);
            Manager.taskManager.deal().computeTask(player, Task.GUILD_TASK, false, false);
            Manager.taskManager.deal().acceptTask(player, Task.BRANCH_TASK, 0, 0, false);

            //发送邮件
            String context = MessageString.GUILD_JOIN_MAIL + "@_@" + guild.getName();
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.System, context);

            //公会频道发送消息
            MessageUtils.notify_chat_player(player, ChatChannel.CHATCHANNEL_GUILD, MessageString.GUILD_JOIN_MAIL1, player.getName());

            //红包处理
            Manager.redPacketManager.joinGuild(player);
        }
        return guildMember;
    }

    private GuildMember addMember(Player player, Guild guild, boolean isCharman) {
        long now = TimeUtils.Time();
        GuildMemberDao guildMemberDao = new GuildMemberDao();
        GuildMember guildMember = Manager.guildsManager.getGuildMembers().get(player.getId());
        if (guildMember == null) {
            PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(player.getId());
            if (playerWorldInfo == null) {
                log.error("增加成员失败:" + player);
                return null;
            }

            guildMember = new GuildMember();
            guildMember.setId(player.getId());
            guildMember.setGuildId(guild.getId());
            if (isCharman) {
                guildMember.setPosition(GuildSysConfig.TYPE_MASTER);
            } else {
                guildMember.setPosition(GuildSysConfig.TYPE_MEMBER);
            }
            guildMember.setJoinTime(now);
            guildMember.setPlayerWorldInfo(playerWorldInfo);
            if (guildMemberDao.insert(DbSqlName.GUILDMEMBER_INSERT.getName(), guildMember.toGuildMemberBean()) <= 0) {
                log.error("增加成员插入数据库失败:" + player);
                return null;
            }
            Manager.guildsManager.getGuildMembers().put(player.getId(), guildMember);
        } else {
            guildMember.setGuildId(guild.getId());
            guildMember.setJoinTime(now);
            if (isCharman) {
                guildMember.setPosition(GuildSysConfig.TYPE_MASTER);
            } else {
                guildMember.setPosition(GuildSysConfig.TYPE_MEMBER);
            }
            Manager.saveThreadManager.getOtherServerSave().deal(guildMember.toGuildMemberBean(), DbSqlName.GUILDMEMBER_UPDATE, SaveServer.UPDATE);
        }

        if (player.isOnline()) {
//            Cfg_Guild_up_Bean bean = CfgManager.getCfg_Guild_up_Container().getValueByKey(GuildSysConfig.TYPE_BASE * 10000 + guild.getLevel());
//            Manager.buffManager.deal().onAddBuff(player, player, bean.getLevel_buff());
            broadCastGuild(player, guild.getId(), guild.getName(), guildMember.getPosition());
        }

        sendAllGift(player, guild);

        return guildMember;
    }

    private void tickDissolve(Guild guild) {
        int curHour = TimeUtils.getDayOfHour(TimeUtils.Time());
        int curMin = TimeUtils.getDayOfMin(TimeUtils.Time());
        if (curMin % 60 == 0 && curHour == Global.Guild_disband_notice.get(1)) {
            Cfg_Guild_up_Bean bean = CfgManager.getCfg_Guild_up_Container().getValueByKey(GuildSysConfig.TYPE_BASE * 10000 + guild.getLevel());
            if (bean.getNeedNum() == 0) {
                return;
            }

            if (bean.getMaintenance_fund() == 0) {
                return;
            }

            if (bean.getMaintenance_fund() > guild.getExp()) {
                guild.setTimes(guild.getTimes() + 1);
                //TODO 2020年8月20日 资金不足 通知会长和副会长
                String context = MailManager.linkContext(MessageString.Guild_Dissolve_Mail, guild.getTimes(), Global.Guild_disband_notice.get(0));
                for (GuildMember guildMember : guild.getMembers().values()) {
                    if (guildMember.getPosition() == GuildSysConfig.TYPE_MASTER || guildMember.getPosition() == GuildSysConfig.TYPE_VICE_MASTER) {
                        Manager.mailManager.sendMailToPlayer(guildMember.getId(), 1, MessageString.System, MessageString.GuildDissolveEmailTitle, context);
                    }
                }
            } else {
                guild.setExp(guild.getExp() - bean.getMaintenance_fund());
                guild.setTimes(0);
                Manager.saveThreadManager.getOtherServerSave().deal(guild.toGuildBean(), DbSqlName.GUILD_UPDATE, SaveServer.UPDATE);
                Manager.biManager.get4399Script().updateGuild(guild);
                writeExpLog(guild.getId(), 0, guild.getExp(), -bean.getMaintenance_fund(), ItemChangeReason.GuildFoundExpDec);
            }
            Manager.biManager.getScript().biGuildMoney(guild.getId(), guild.getName(), guild.getLevel(), guild.getExp());

            if (guild.getTimes() >= Global.Guild_disband_notice.get(0)) {
                dissolveGuild(guild, BIDefine.CTGuildMemberBeDissolveQuit);

                log.info("公会:" + guild.getName() + "id:" + guild.getId() + "因为维修资金不足而解散");
                String context = MailManager.linkContext(MessageString.GUILD_QUIT_MAIL2, Global.Guild_disband_notice.get(0), TimeUtils.format2string(TimeUtils.Time()));
                for (GuildMember guildMember : guild.getMembers().values()) {
                    Manager.mailManager.sendMailToPlayer(guildMember.getId(), 1, MessageString.System, MessageString.GuildDissolveEmailTitle, context);
                }
            }
        }
    }

    private void tickImpeach(Guild guild) {
        GuildMember proxyChairMan = guild.getProxyChairMan();
        if (guild.getProxyTime() == 0 || proxyChairMan == null) {
            guild.setProxyTime(0);
            guild.setProxyChairMan(null);
            return;
        }

        long actionId = IDConfigUtil.getLogId();
        long proxyChairOffTime = proxyChairMan.getPlayerWorldInfo().getLastOffTime() * 1000L;
        Player player = Manager.playerManager.getPlayer(proxyChairMan.getId());
        if (player == null) {
            return;
        }

        long now = TimeUtils.Time();

        if (proxyChairOffTime != 0 && now - proxyChairOffTime >= 24 * 60 * 60 * 1000) {
            guild.setProxyChairMan(null);
            guild.setProxyTime(0);
            writeBaseLog(player, guild, 0, "", GuildSysConfig.BaseLog_type9, actionId, "");
            return;
        }

        if (now - guild.getProxyTime() >= 24 * 3 * 60 * 60 * 1000) {
            guild.setProxyTime(0);
            guild.setProxyChairMan(null);

            GuildMember chariMan = guild.getChairMan();
            chariMan.setPosition(GuildSysConfig.TYPE_MEMBER);

            proxyChairMan.setPosition(GuildSysConfig.TYPE_MASTER);
            guild.setChairMan(proxyChairMan);

            Manager.saveThreadManager.getOtherServerSave().deal(chariMan.toGuildMemberBean(), DbSqlName.GUILDMEMBER_UPDATE, SaveServer.UPDATE);
            Manager.saveThreadManager.getOtherServerSave().deal(proxyChairMan.toGuildMemberBean(), DbSqlName.GUILDMEMBER_UPDATE, SaveServer.UPDATE);
            Manager.saveThreadManager.getOtherServerSave().deal(guild.toGuildBean(), DbSqlName.GUILD_UPDATE, SaveServer.UPDATE);
            Manager.biManager.get4399Script().updateGuild(guild);

            writeBaseLog(player, guild, 0, "", GuildSysConfig.BaseLog_type10, actionId, "");
            log.info(guild.getId() + "弹劾成功,玩家:" + proxyChairMan.getId() + "取代了:" + chariMan.getId());

            Manager.mailManager.sendMailToPlayer(chariMan.getId(), 1, MessageString.System, MessageString.System, MessageString.GuildImpeachSuccessMailToOldChairman + "@_@" + proxyChairMan.getPlayerWorldInfo().getRolename());
            Manager.mailManager.sendMailToPlayer(proxyChairMan.getId(), 1, MessageString.System, MessageString.System, MessageString.GuildImpeachSuccessMailToNew + "@_@" + guild.getName());

            for (long id : guild.getMembers().keySet()) {
                String context = MessageString.NoticeoftransferMailContent + "@_@" + proxyChairMan.getPlayerWorldInfo().getRolename() + "@_@" + guild.getName();
                Manager.mailManager.sendMailToPlayer(id, 1, MessageString.System, MessageString.NoticeoftransferMailTitle, context);
            }

            addGuildLog(guild, MessageString.GuildImpeachSuccessMailToOther, proxyChairMan.getPlayerWorldInfo().getRolename(), guild.getName());

            Player bePlayer = Manager.playerManager.getPlayerCache(proxyChairMan.getId());
            if (bePlayer.isOnline()) {
                broadCastGuild(player, guild.getId(), guild.getName(), proxyChairMan.getPosition());
            }

            GuildBattle gb = Manager.guildBattleManager.getGuildbattles().get(guild.getId());
            if (gb != null) {
                if (gb.getType() == 1) {//公会弹劾
                    sysPublicGuildBattleInfo(guild, gb);
                }
            }
        }
    }

    private void sysPublicGuildBattleInfo(Guild guild, GuildBattle gb) {
        CommandMessage.G2PSynGuildBattleInfo.Builder msg = CommandMessage.G2PSynGuildBattleInfo.newBuilder();
        CommandMessage.GuildBattleInfo.Builder gbi = CommandMessage.GuildBattleInfo.newBuilder();
        gbi.setRank(gb.getRank());
        for (GuildMember gm : guild.getMembers().values()) {
            if (gm.getPosition() == GuildSysConfig.TYPE_MASTER) {
                gbi.setMasterId(gm.getId());
            } else if (gm.getPosition() == GuildSysConfig.TYPE_VICE_MASTER) {
                if (!gbi.getSecMasterIdList().contains(gm.getId())) {
                    gbi.addSecMasterId(gm.getId());
                }
            }
        }
        msg.addGuildBattleInfos(gbi);
        MessageUtils.send_to_public(CommandMessage.G2PSynGuildBattleInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void tickImpeachMail(Guild guild) {
        long openTime = TimeUtils.getOpenServerTime();
        long now = TimeUtils.Time();

        long chairOffTime = guild.getChairMan().getPlayerWorldInfo().getLastOffTime();
        if ((now - openTime) < 24 * 60 * 60 * 1000) {
            if ((now - chairOffTime) >= 18 * 60 * 60 * 1000) {

            }
        }

        if ((now - openTime) < 24 * 2 * 60 * 60 * 1000) {
            if ((now - chairOffTime) >= 24 * 60 * 60 * 1000) {

            }
        }

        if ((now - openTime) >= 24 * 2 * 60 * 60 * 1000) {
            if ((now - chairOffTime) >= 24 * 2 * 60 * 60 * 1000) {

            }
        }
    }

    private void dissolveGuild(Guild g, int changeType) {
        int count = g.getMembers().size();
        for (GuildMember guildMember : g.getMembers().values()) {
            count--;
            Player player = Manager.playerManager.getPlayer(guildMember.getId());
            if (player == null) continue;

            player.setGuildId(0);
            player.setGuildName("");
            guildMember.setGuildId(0);
            guildMember.setJoinTime(0);
            guildMember.setContribute(0);
            guildMember.setPosition(GuildSysConfig.TYPE_MEMBER);
            Manager.playerManager.savePlayer(player, SavePlayerLevel.RightNow);
            Manager.saveThreadManager.getOtherServerSave().deal(guildMember.toGuildMemberBean(), DbSqlName.GUILDMEMBER_UPDATE, SaveServer.UPDATE);
            writeBaseLog(player, g, guildMember.getId(), player.getName(), GuildSysConfig.BaseLog_type2, 0, "");

            if (player.isOnline()) {
                broadCastGuild(player, 0, "", 0);
            }

            if (changeType != 0)
                Manager.biManager.getScript().biGuildMember(player, g.getId(), g.getLevel(), changeType, guildMember.getPosition(), count, g.getFightPower());
        }

        Manager.guildsManager.getGuildMap().remove(g.getId());
        Manager.guildsManager.getGuildName().remove(g.getName());
        Manager.saveThreadManager.getOtherServerSave().deal(g.toGuildBean(), DbSqlName.GUILD_DELETE, SaveServer.DELETE);
        Manager.guildActivityManager.deal().dissolveGuild(g.getId());
        Manager.guildBattleManager.manager().dissolveGuild(g.getId());
        Manager.biManager.getScript().biGuildMoney(g.getId(), g.getName(), g.getLevel(), g.getExp());
    }

    private void broadCastGuild(Player player, long guildId, String guildName, int guildPos) {
        MapMessage.ResGuildInfoBroadCast.Builder builder = MapMessage.ResGuildInfoBroadCast.newBuilder();
        builder.setGuildId(guildId);
        builder.setGuildName(guildName);
        builder.setGuildPos(guildPos);
        builder.setRoleId(player.getId());
        MessageUtils.send_to_roundPlayer(player, MapMessage.ResGuildInfoBroadCast.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }
}
