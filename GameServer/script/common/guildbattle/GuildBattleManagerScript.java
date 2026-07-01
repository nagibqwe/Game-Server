package common.guildbattle;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Guild_official_Bean;
import com.data.bean.Cfg_Guild_war_rank_Bean;
import com.data.bean.Cfg_Guild_war_reward_Bean;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildMember;
import com.game.guild.structs.GuildSysConfig;
import com.game.guildbattle.script.IGuildBattleManagerScript;
import com.game.guildbattle.structs.*;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.GlobalType;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.CommandMessage;
import game.message.GuildBattleMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description
 * @auther lw
 * @create 2020-02-11 15:25
 */
public class GuildBattleManagerScript implements IGuildBattleManagerScript, IScript {

    private static final Logger log = LogManager.getLogger(GuildBattleManagerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GuildBattleBaseScript;
    }

    @Override
    public Object call(Object... args) {
        String method = (String) args[1];
        int state = (int) args[0];
        switch (method) {
            case "activityChangeCallBack":
                activityChangeCallBack(state);
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public void guildBattleRate() {
        ConcurrentHashMap<Long, GuildBattle> gbs = Manager.guildBattleManager.getGuildbattles();
        List<Guild> guilds = new ArrayList<>(Manager.guildsManager.getGuildMap().values());
        Collections.sort(guilds, new GuildSort());

        if (gbs.size() == 0) {
            initGuildBattle(guilds, gbs, 1);
        } else {
            Iterator<Map.Entry<Long, GuildBattle>> iterator = gbs.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, GuildBattle> entry = iterator.next();
                GuildBattle gb = entry.getValue();
                Guild guild = Manager.guildsManager.getGuildById(gb.getGuildId());

                if (guild == null || gb.getRank() == 0 || gb.getRank() > Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV * 3) {
                    log.info("仙盟争霸淘汰评级 guildId:" + gb.getGuildId() + "type:" + gb.getType() + "rank:" + gb.getRank());
                    iterator.remove();
                    continue;
                }

                int tempType = gb.getRank() % Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV;
                if (tempType == 0) {
                    gb.setType(gb.getRank() / Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV);
                } else {
                    gb.setType(gb.getRank() / Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV + 1);
                }
                log.info("仙盟争霸重新评级 guildId:" + gb.getGuildId() + "type:" + gb.getType() + "rank:" + gb.getRank());

                gb.getMemberList().clear();
                gb.setRank(0);
                gb.setMapId(0);
            }
            if (gbs.size() == 0) {
                initGuildBattle(guilds, gbs, 1);
            } else {
                initGuildBattle(guilds, gbs, 10);
            }
        }

        for (GuildBattle gb : gbs.values()) {
            Guild guild = Manager.guildsManager.getGuildById(gb.getGuildId());
            Cfg_Guild_war_rank_Bean bean = CfgManager.getCfg_Guild_war_rank_Container().getValueByKey(gb.getType());
            String str1 = MessageString.GuildWarRankMailContext + "@_@" + guild.getName() + "@_@" +
                    ServerStr.getChatTableName(bean.getName());
            for (long id : guild.getMembers().keySet()) {
                Manager.mailManager.sendMailToPlayer(id, 1, MessageString.System, MessageString.GuildWarRankMailTitle, str1);
            }
        }
        ServerParamUtil.saveGuildBattleRate();
    }

    @Override
    public void guildBattleBegin(int mapModelId) {
        //分配仙盟
        HashMap<Integer, List<Long>> groups = new HashMap<>();
        ConcurrentHashMap<Long, GuildBattle> gbs = Manager.guildBattleManager.getGuildbattles();
        Iterator<Map.Entry<Long, GuildBattle>> iterator = gbs.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, GuildBattle> entry = iterator.next();
            List<Long> list = groups.get(entry.getValue().getType());
            if (list == null) {
                list = new ArrayList<>();
                list.add(entry.getKey());
                groups.put(entry.getValue().getType(), list);
            } else {
                list.add(entry.getKey());
            }
        }

        //创建线路
        Iterator<Map.Entry<Integer, List<Long>>> iterator2 = groups.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry<Integer, List<Long>> entry = iterator2.next();
            MapObject mapObject = Manager.mapManager.createCopyMap(mapModelId, 1, MapManager.CopyMapOwnerSystemId, GlobalType.getWorldLevel(), entry.getValue(), entry.getKey());
            for (Long guildId : entry.getValue()) {
                gbs.get(guildId).setMapId(mapObject.getId());
            }
        }

        MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_MARQUEE, MessageString.GuildWarEnter);
    }

    @Override
    public void guildBattleEnd() {
        HashMap<Long, MapObject> maps = new HashMap<>();
        ConcurrentHashMap<Long, GuildBattle> gbs = Manager.guildBattleManager.getGuildbattles();
        Iterator<Map.Entry<Long, GuildBattle>> iterator = gbs.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Long, GuildBattle> entry = iterator.next();
            MapObject mapObject = Manager.mapManager.getMap(entry.getValue().getMapId());
            if (mapObject != null) {
                if (maps.get(mapObject.getId()) == null) {
                    maps.put(mapObject.getId(), mapObject);
                }
            }
        }

        for (MapObject mapObject : maps.values()) {
            Manager.scriptManager.GetScriptClass(ScriptEnum.GuildBatleMapScript).call("endTime", mapObject);
        }
    }

    @Override
    public void reqGuildBattleRate(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }

        GuildBattleMessage.ResGuildBattleRateList.Builder builder = GuildBattleMessage.ResGuildBattleRateList.newBuilder();
        Iterator<Map.Entry<Long, GuildBattle>> iterator = Manager.guildBattleManager.getGuildbattles().entrySet().iterator();
        while (iterator.hasNext()) {
            GuildBattleMessage.GuildBattleRate.Builder gbMsg = GuildBattleMessage.GuildBattleRate.newBuilder();
            Map.Entry<Long, GuildBattle> entry = iterator.next();
            GuildBattle gb = entry.getValue();
            Guild guild = Manager.guildsManager.getGuildById(gb.getGuildId());
            gbMsg.setGuildName(guild.getName());
            gbMsg.setType(gb.getType());
            if (gb.getRank() == 0) {
                gbMsg.setFlag(1);
            } else if (gb.getRank() == 1) {
                gbMsg.setFlag(0);
            } else {
                int tempType = gb.getRank() % Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV;
                if (tempType == 0) {
                    if ((gb.getRank() / Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV) > gb.getType()) {
                        gbMsg.setFlag(2);
                    } else if ((gb.getRank() / Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV) < gb.getType()) {
                        gbMsg.setFlag(0);
                    } else {
                        gbMsg.setFlag(1);
                    }
                } else {
                    if ((gb.getRank() / Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV + 1) > gb.getType()) {
                        gbMsg.setFlag(2);
                    } else if ((gb.getRank() / Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV + 1) < gb.getType()) {
                        gbMsg.setFlag(0);
                    } else {
                        gbMsg.setFlag(1);
                    }
                }
            }
            builder.addRateList(gbMsg);
        }
        MessageUtils.send_to_player(player, GuildBattleMessage.ResGuildBattleRateList.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void reqGuildBattleRecord(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }

        GuildBattleMessage.ResGuildBattleRecordList.Builder builder = GuildBattleMessage.ResGuildBattleRecordList.newBuilder();
        GuildBattle gb = Manager.guildBattleManager.getGuildbattles().get(player.getGuildId());
        if (gb != null) {
            Iterator<Map.Entry<Long, GuildBattle>> iterator = Manager.guildBattleManager.getGuildbattles().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, GuildBattle> entry = iterator.next();
                GuildBattle gbEntry = entry.getValue();
                if (gbEntry.getType() != gb.getType()) {
                    continue;
                }

                GuildBattleMessage.GuildBattleRecord.Builder b = GuildBattleMessage.GuildBattleRecord.newBuilder();
                Guild guild = Manager.guildsManager.getGuildById(gbEntry.getGuildId());
                b.setGuildScore(gbEntry.getMemberList().stream().map(GuildBattleMember::getRecord).reduce(0, Integer::sum));
                b.setGuildName(guild.getName());
                b.setIcon(guild.getIcon());

                for (GuildBattleMember gbm : gbEntry.getMemberList()) {
                    b.addMembers(gbm.toGuildBattleMemberMsg());
                }

                if (gbEntry.getRank() == 1 || ((gbEntry.getRank() % Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV == 0)
                        && gbEntry.getRank() != Manager.guildBattleManager.MAX_GUILD_NUM)) {
                    b.setRes(true);
                } else {
                    b.setRes(false);
                }
                builder.addRecordList(b);
            }
        }
        MessageUtils.send_to_player(player, GuildBattleMessage.ResGuildBattleRecordList.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void reqGuildBattleWin(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }
        GuildBattleWin win = Manager.guildBattleManager.getGuildBattleWin();
        GuildBattleMessage.ResGuildBattleWin.Builder builder = GuildBattleMessage.ResGuildBattleWin.newBuilder();
        builder.setGuildId(win.getGuildId());
        builder.setHasGet(win.getHasGet());
        builder.setNum(win.getNum());
        builder.setType(win.getType());
        if (win.getGuildId() != 0) {
            Guild guild = Manager.guildsManager.getGuildById(win.getGuildId());
            builder.setGuildName(guild.getName());
        } else {
            builder.setGuildName("");
        }
        builder.setBeguildName(win.getBeGuildName());
        MessageUtils.send_to_player(player, GuildBattleMessage.ResGuildBattleWin.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void sendGuildBattleRed(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }

        GuildBattleWin win = Manager.guildBattleManager.getGuildBattleWin();
        if (win.getGuildId() == 0) {
            return;
        }

        if (win.getGuildId() != player.getGuildId()) {
            return;
        }

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        GuildMember guildMember = guild.getMembers().get(player.getId());

        Cfg_Guild_official_Bean b = CfgManager.getCfg_Guild_official_Container().getValueByKey(guildMember.getPosition());

        if (!Manager.guildsManager.manager().isProxyChairMan(guildMember, guild) && b.getWarRewardLimit() == 0) {
            return;
        }

        boolean isRed = false;

        if (win.getType() == 0) {
            for (Cfg_Guild_war_reward_Bean bean : CfgManager.getCfg_Guild_war_reward_Container().getValuees()) {
                if (win.getNum() >= bean.getCount()) {
                    if (!hasGet(win.getHasGet(), bean.getId())) {
                        isRed = true;
                        break;
                    }
                }
            }
        } else {
            if (win.getHasGet() == 0) {
                Cfg_Guild_war_reward_Bean bb = null;
                for (Cfg_Guild_war_reward_Bean bean : CfgManager.getCfg_Guild_war_reward_Container().getValuees()) {
                    if (win.getNum() >= bean.getCount()) {
                        bb = bean;
                    } else {
                        if (bb != null) {
                            isRed = true;
                            break;
                        }
                    }
                }
            }
        }

        GuildBattleMessage.ResGuildBattleRedPoint.Builder builder = GuildBattleMessage.ResGuildBattleRedPoint.newBuilder();
        builder.setRedPoint(isRed);
        MessageUtils.send_to_player(player, GuildBattleMessage.ResGuildBattleRedPoint.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void reqGuildBattleRecordReward(Player player, int id) {
        if (!player.isHaveGuild()) {
            return;
        }

        GuildBattleWin win = Manager.guildBattleManager.getGuildBattleWin();
        if (win.getGuildId() == 0) {
            return;
        }

        if (win.getGuildId() != player.getGuildId()) {
            return;
        }

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        GuildMember guildMember = guild.getMembers().get(player.getId());

        Cfg_Guild_official_Bean b = CfgManager.getCfg_Guild_official_Container().getValueByKey(guildMember.getPosition());

        if (!Manager.guildsManager.manager().isProxyChairMan(guildMember, guild) && b.getWarRewardLimit() == 0) {
            return;
        }

        Cfg_Guild_war_reward_Bean bean = CfgManager.getCfg_Guild_war_reward_Container().getValueByKey(id);
        if (bean == null) {
            return;
        }

        int flag = win.getHasGet();
        if (win.getType() == 0) {
            if (hasGet(flag, id)) {
                return;
            }
        } else {
            if (flag != 0) {
                return;
            }
        }

        if (bean.getCount() > win.getNum()) {
            return;
        }

        //上架拍卖行
        List<Item> list;

        if (win.getType() == 0) {
            flag |= (1 << (id - 1));
            win.setHasGet(flag);
            list = Item.createItems(bean.getContinueReward());
        } else {
            win.setHasGet(id);
            list = Item.createItems(bean.getEndReward());
        }

        Manager.auctionManager.manager().auctionActivityPut(win.getRoleIds(), list, DailyActiveDefine.ACTIVITY_GUILDBATTLE.getValue(), player.getGuildId());

        ServerParamUtil.saveGuildBattleWin();
        GuildBattleMessage.ResGuildBattleRecordReward.Builder builder = GuildBattleMessage.ResGuildBattleRecordReward.newBuilder();
        builder.setId(id);
        MessageUtils.send_to_player(player, GuildBattleMessage.ResGuildBattleRecordReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        sendMail(guild.getId());

    }

    /**
     * 发送仙盟拍卖邮件
     *
     * @param guildId
     */
    void sendMail(long guildId) {
        Guild guild = Manager.guildsManager.getGuildById(guildId);

        for (GuildMember member : guild.getMembers().values()) {
            Player player = Manager.playerManager.getPlayerOnline(member.getId());
            Manager.mailManager.sendMailToPlayer(member.getId(), 1,
                    MessageString.System,
                    MessageString.guild_war_win_continue_reward_mail_title,
                    MessageString.guild_war_win_continue_reward_mail_tex
            );
            if (player == null) {
                continue;
            }
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.guild_war_win_continue_reward_mail_tex);
        }
    }

    @Override
    public void reqGuildBattleBack(Player player) {
        GuildBattleData gbd = getGuildBattleData(player);
        if (gbd == null) {
            return;
        }
        MapObject mapObject = Manager.mapManager.getMap(player.getCurGps().getMapId());
        Manager.mapManager.transport().ResCurMapTransport(player, mapObject, mapObject.getBriths().get(gbd.getIndex()), 0, 0);
    }

    @Override
    public void reqGuildBattleCall(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        GuildMember guildMember = guild.getMembers().get(player.getId());
        if (guildMember.getPosition() != GuildSysConfig.TYPE_MASTER) {
            return;
        }

        GuildBattle gb = Manager.guildBattleManager.getGuildbattles().get(player.getGuildId());
        if (gb == null) {
            return;
        }

        if (gb.getMapId() != player.gainMapId()) {
            return;
        }

        if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.GUILDBATTLECALL_CD, null)) {
            return;
        }

        Manager.cooldownManager.addCooldown(player, CooldownTypes.GUILDBATTLECALL_CD, null, Global.Guild_War_CallUp_CD * 1000);

        GuildBattleMessage.ResGuildBattleCall.Builder builder = GuildBattleMessage.ResGuildBattleCall.newBuilder();
        builder.setName(player.getName());
        for (long id : guild.getMembers().keySet()) {
            Player pp = Manager.playerManager.getPlayerOnline(id);
            if (pp == null) {
                continue;
            }

            if (id == player.getId()) {
                continue;
            }
            MessageUtils.send_to_player(pp, GuildBattleMessage.ResGuildBattleCall.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    @Override
    public void reqGuildBattleLike(Player player, GuildBattleMessage.ReqGuildBattleLike mess) {
        GuildBattleData gbd = getGuildBattleData(player);
        if (gbd == null) {
            return;
        }
        //还没有结算
        if (gbd.getRank() == 0) {
            return;
        }
        GuildBattleMember gbm = gbd.getMemberList().get(mess.getRoleId());
        if (gbm == null) {
            return;
        }
        if (gbm.getRank() == 0) {
            return;
        }
        if (gbm.isPraise()) {
            return;
        }

        Player bePlayer = Manager.playerManager.getPlayer(mess.getRoleId());
        if (bePlayer == null) {
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        gbm.setPraise(true);
        Manager.currencyManager.manager().onAddItemCoin(bePlayer, Global.Guild_War_Praise_Reward.get(0), Global.Guild_War_Praise_Reward.get(1), ItemChangeReason.GuildBattlePraiseGet, actionId);
        Manager.currencyManager.manager().onAddItemCoin(player, Global.Guild_War_Praise_Reward.get(0), Global.Guild_War_Praise_Reward.get(1), ItemChangeReason.GuildBattlePraiseGet, actionId);
        GuildBattleMessage.ResGuildBattleLike.Builder builder = GuildBattleMessage.ResGuildBattleLike.newBuilder();
        builder.setRoleId(mess.getRoleId());
        MessageUtils.send_to_player(player, GuildBattleMessage.ResGuildBattleLike.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        if (bePlayer.isOnline()) {
            MessageUtils.notify_player(bePlayer, Notify.NORMAL, MessageString.GUILDWARPraised, player.getName());
        }
        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.GUILDWARPraise);

        GuildBattleMessage.ResSendBulletScreen.Builder message = GuildBattleMessage.ResSendBulletScreen.newBuilder();
        message.setRoleId(player.getId());
        message.setRoleName(player.getName());
        message.setRoleCareer(player.getCareer());
        message.setType(mess.getType());

        MessageUtils.send_to_player(bePlayer, GuildBattleMessage.ResSendBulletScreen.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void reqGuildBattleResult(Player player) {
        GuildBattleData gbd = getGuildBattleData(player);
        if (gbd == null) {
            return;
        }

        MapObject mapObject = Manager.mapManager.getMap(player.getCurGps().getMapId());
        GuildBattleMapData zone = mapObject.getZone();

        GuildBattleMessage.ResGuildBattleResult.Builder builder = GuildBattleMessage.ResGuildBattleResult.newBuilder();
        builder.setRank(gbd.getRank());
        builder.setType(zone.getLv());
        builder.setRecord(gbd.getMemberList().values().stream().map(GuildBattleMember::getRecord).reduce(0, Integer::sum));
        builder.setWin(Manager.guildBattleManager.getGuildBattleWin().getNum());
        builder.setRes(Manager.guildBattleManager.getGuildBattleWin().getType());
        MessageUtils.send_to_player(player, GuildBattleMessage.ResGuildBattleResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    public void reqGuildBattleStat(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }

        GuildBattle gb = Manager.guildBattleManager.getGuildbattles().get(player.getGuildId());
        if (gb == null) {
            return;
        }

        if (gb.getMapId() != player.getCurGps().getMapId()) {
            return;
        }

        MapObject mapObject = Manager.mapManager.getMap(gb.getMapId());
        GuildBattleMapData zone = mapObject.getZone();

        GuildBattleMessage.ResGuildBattleRecordList.Builder builder = GuildBattleMessage.ResGuildBattleRecordList.newBuilder();
        for (GuildBattleData gbd : zone.getGuild().values()) {
            GuildBattleMessage.GuildBattleRecord.Builder b = GuildBattleMessage.GuildBattleRecord.newBuilder();
            Guild guild = Manager.guildsManager.getGuildById(gbd.getGuildId());
            b.setGuildScore(gbd.getMemberList().values().stream().map(GuildBattleMember::getRecord).reduce(0, Integer::sum));
            b.setGuildName(guild.getName());
            b.setIcon(guild.getIcon());

            for (GuildBattleMember gbm : gbd.getMemberList().values()) {
                b.addMembers(gbm.toGuildBattleMemberMsg());
            }

            if (gbd.getRank() == 1 || (gbd.getRank() != 0 && gbd.getRank() != Manager.guildBattleManager.MAX_GUILD_NUM &&
                    (gbd.getRank() % Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV == 0))) {
                b.setRes(true);
            } else {
                b.setRes(false);
            }
            builder.addRecordList(b);
        }
        MessageUtils.send_to_player(player, GuildBattleMessage.ResGuildBattleRecordList.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void dissolveGuild(long guildId) {
        GuildBattle gb = Manager.guildBattleManager.getGuildbattles().get(guildId);
        if (gb == null) {
            return;
        }

        Manager.guildBattleManager.getGuildbattles().remove(guildId);
        GuildBattleWin win = Manager.guildBattleManager.getGuildBattleWin();
        if (win.getGuildId() == guildId) {
            win = new GuildBattleWin();
            Manager.guildBattleManager.setGuildBattleWin(win);
        }

        if (gb.getType() == 1) {
            CommandMessage.G2PSynGuildBattleInfo.Builder msg = CommandMessage.G2PSynGuildBattleInfo.newBuilder();
            CommandMessage.GuildBattleInfo.Builder gbi = CommandMessage.GuildBattleInfo.newBuilder();
            gbi.setRank(gb.getRank());
            gbi.setMasterId(0);
            msg.addGuildBattleInfos(gbi);
            MessageUtils.send_to_public(CommandMessage.G2PSynGuildBattleInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    /**
     * 发送弹幕
     *
     * @param player
     * @param messInfo
     */
    @Override
    public void reqSendBulletScreen(Player player, GuildBattleMessage.ReqSendBulletScreen messInfo) {
        if (!player.isHaveGuild()) {
            return;
        }

        GuildBattle gb = Manager.guildBattleManager.getGuildbattles().get(player.getGuildId());
        if (gb == null) {
            return;
        }

        if (gb.getMapId() != player.gainMapId()) {
            return;
        }
        Guild guild = Manager.guildsManager.getGuildById(gb.getGuildId());
        GuildMember guildMember = guild.getMembers().get(player.getId());
        if (guildMember.getPosition() == GuildSysConfig.TYPE_VICE_MASTER || guildMember.getPosition() == GuildSysConfig.TYPE_MASTER) {
            MapObject mapObject = Manager.mapManager.getMap(gb.getMapId());

            GuildBattleMessage.ResSendBulletScreen.Builder message = GuildBattleMessage.ResSendBulletScreen.newBuilder();
            message.setRoleId(player.getId());
            message.setRoleName(player.getName());
            message.setRoleCareer(player.getCareer());
            message.setContext(messInfo.getContext());
            message.setType(messInfo.getType());
            for (Player member : mapObject.getPlayers().values()) {
                if (member.getGuildId() != player.getGuildId()) {
                    continue;
                }
                MessageUtils.send_to_player(member, GuildBattleMessage.ResSendBulletScreen.MsgID.eMsgID_VALUE, message.build().toByteArray());
            }
        }

    }

    private void initGuildBattle(List<Guild> guilds, ConcurrentHashMap<Long, GuildBattle> gbs, int temp) {
        for (Guild guild : guilds) {
            if (temp > Manager.guildBattleManager.MAX_GUILD_NUM) {
                return;
            }

            if (gbs.get(guild.getId()) != null) {
                continue;
            }

            GuildBattle guildBattle = new GuildBattle();
            guildBattle.setGuildId(guild.getId());
            int tempType = temp % Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV;
            if (tempType == 0) {
                guildBattle.setType(temp / Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV);
            } else {
                guildBattle.setType(temp / Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV + 1);
            }
            gbs.put(guild.getId(), guildBattle);
            log.info("仙盟争霸评级初始化 guildId:" + guild.getId() + " guildPower:" + guild.gainGuildPower() + "type:" + guildBattle.getType() + "rank:" + temp);
            temp++;
        }
    }

    private boolean hasGet(int hasget, int id) {
        return (hasget & (1 << (id - 1))) != 0;
    }

    private GuildBattleData getGuildBattleData(Player player) {
        if (!player.isHaveGuild()) {
            return null;
        }

        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        if (mapObject == null) {
            return null;
        }
        Object zone = mapObject.getZone();
        if (zone instanceof GuildBattleMapData) {
            GuildBattleMapData battleMapData = (GuildBattleMapData) zone;
            return battleMapData.getGuild().get(player.getGuildId());
        }
        return null;
    }

    private void activityChangeCallBack(int state) {
        if (state == 1) {//只有在准备阶段才进行评级
            Manager.guildBattleManager.manager().guildBattleRate();
        }

    }
}


