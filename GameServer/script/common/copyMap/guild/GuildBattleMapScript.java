package common.copyMap.guild;

import com.data.*;
import com.data.bean.*;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.behavior.manager.BehaviorManager;
import com.game.chat.structs.Notify;
import com.game.copymap.scripts.ICopyGatherScript;
import com.game.copymap.scripts.ICopyReliveScript;
import com.game.copymap.structs.ExpNoteData;
import com.game.count.structs.VariantType;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildMember;
import com.game.guild.structs.GuildSysConfig;
import com.game.guildbattle.structs.*;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.Gather;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import com.game.welfare.struct.RetrieveType;
import game.core.map.Position;
import game.core.util.TimeUtils;
import game.message.CommandMessage;
import game.message.GuildBattleMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 仙盟战
 * @auther lw
 * @create 2020-02-11 15:27
 */
public class GuildBattleMapScript implements ICopyReliveScript, IMapBaseScript, ICopyGatherScript {
    private static final Logger log = LogManager.getLogger(GuildBattleMapScript.class);

    final int AttackCamp = 0;   //攻方阵营
    final int DefenseCamp = 1;  //守方阵营

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setAutoRemove(false);
        int globalLv = (int) objects[0];
        List<Long> guildIds = (List<Long>) objects[1];
        int lv = (int) objects[2];

        GuildBattleMapData zone = new GuildBattleMapData();

        zone.setZoneId(mapObject.zone.getZoneId());
        zone.setLevel(mapObject.zone.getLevel());
        mapObject.setZone(zone);

        //初始化公会
        ConcurrentHashMap<Long, GuildBattleData> gbd = zone.getGuild();
        int tempIndex = 0;
        for (long guildId : guildIds) {
            GuildBattleData data = new GuildBattleData();
            data.setGuildId(guildId);
            data.setCamp(AttackCamp);
            data.setIndex(tempIndex);
            gbd.put(guildId, data);
            tempIndex++;
        }

        //设置仙盟评级
        zone.setLv(lv);
        zone.setGlobalLv(globalLv);
        log.info("初始化战场当前世界等级:" + globalLv + "参加公会ID:" + guildIds.toString() + "仙盟评级等级:" + lv);

        //创建怪物
        initMonster(mapObject, zone);

        mapObject.addMapLoopScriptEventTimer(getId(), "doTick", -1, 0, 2000);

        //启动准备时间
        mapObject.addMapOnceScriptEventTimer(getId(), "readyTime", Global.Guild_War_Ready * 1000L);

        //防守方增加积分
        mapObject.addMapLoopScriptEventTimer(getId(), "addRecord", -1, 0, Global.Guild_War_Defense_Point_Add.get(0) * 1000L);

    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        if (player.getTeamId() > 0) {
            Manager.teamManager.OnQuitTeam(player);
        }
        GuildBattleMapData zone = map.getZone();

        GuildBattleData gbd = zone.getGuild().get(player.getGuildId());
        if (gbd == null) {
            log.error("玩家找不到仙盟数据:" + player.toString());
            return;
        }

        //初始化玩家数据
        GuildBattleMember gbm = gbd.getMemberList().get(player.getId());
        if (gbm == null) {
            gbm = new GuildBattleMember();
            gbm.setRoleId(player.getId());
            gbd.getMemberList().put(player.getId(), gbm);

            //第一次进入增加积分
            gbm.setRecord(Global.Guild_War_Enter_Point);
        }

        //修改正营模式
        player.setCamp(gbd.getCamp(), true);
        //Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStateCamp, true);

        int buffId = getWinBuff(map);
        //增加攻击方buff
        if (buffId != 0 && gbd.getCamp() == AttackCamp) {
            Manager.buffManager.deal().onAddBuff(player, player, buffId);
        }

        //同步数据
        int maxLiveBuildId = getMaxBuild(map, 1);
        int maxDieBuildId = getMaxBuild(map, 0);
        sysnPanelData(player, map, maxLiveBuildId, maxDieBuildId, gbd.getCamp());

        Manager.countManager.addVariant(player, VariantType.GuildWar, 1);
        Manager.controlManager.operate(player, FunctionVariable.GuildWar, 1);

        Manager.retrieveResManager.getScript().count(player, RetrieveType.GuildBattle);
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {

    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {
        Cfg_Guild_war_building_Bean bean = CfgManager.getCfg_Guild_war_building_Container().getValueByKey(monster.getModelId());
        if (bean == null) {
            return;
        }

        GuildBattleMapData zone = mapObject.getZone();

        boolean isPlayer = attacker instanceof Player;
        if (bean.getType() == 0 && isPlayer) {
            Player player = (Player) attacker;

            GuildBattleData gbd = zone.getGuild().get(player.getGuildId());
            if (gbd.getCamp() == AttackCamp) {
                gbd.setAllHurt(gbd.getAllHurt() + damage);
            }
        }

        GuildBattleBuild battleBuild = zone.getBuildInfos().get(monster.getModelId());
        if (battleBuild == null) {
            return;
        }

        long curHp = monster.getCurHp() * 100 / monster.getAttribute().MaxHP();
        if (curHp == 0) {
            curHp = 1;
        }
        if (battleBuild.getHp() != curHp) {
            battleBuild.setHp((int) curHp);
//            MapParam.getGuildBattleMapData(mapObject).getBuildInfos().put(monster.getModelId(), (int)curHp);
            int maxLiveBuildId = getMaxBuild(mapObject, 1);
            if (maxLiveBuildId == monster.getModelId()) {
                int maxDieBuildId = getMaxBuild(mapObject, 0);
                for (Player player : mapObject.getPlayers().values()) {
                    sysnPanelData(player, mapObject, maxLiveBuildId, maxDieBuildId, player.getCamp());
                }
            }
        }
    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {
        Cfg_Guild_war_building_Bean bean = CfgManager.getCfg_Guild_war_building_Container().getValueByKey(monster.getModelId());
        if (bean == null) {
            return;
        }
        GuildBattleMapData zone = map.getZone();

        if (bean.getType() != 0) {
            GuildBattleBuild guildBattleBuild = zone.getBuildInfos().get(monster.getModelId());
            guildBattleBuild.setHp(0);
            //移除空气墙
            MapManager.getInstance().setBlockDoor(map, bean.getAirWall(), true);
            //移除buff
            Manager.buffManager.deal().onRemoveBuff(zone.getMonster(), bean.getReduceHurt());
            //更新目标
            int curType = zone.getCurType();
            if (bean.getType() == curType) {
                zone.setCurType(curType - 1);
            }
            //同步数据
            int maxLiveBuildId = getMaxBuild(map, 1);
            int maxDieBuildId = getMaxBuild(map, 0);
            for (Player player : map.getPlayers().values()) {
                sysnPanelData(player, map, maxLiveBuildId, maxDieBuildId, player.getCamp());
            }
            Cfg_Guild_war_building_Bean next = CfgManager.getCfg_Guild_war_building_Container().getValueByKey(maxLiveBuildId);
            String dieName = ServerStr.getChatTableName(bean.getName());
            String nextName = ServerStr.getChatTableName(next.getName());
            if (next.getType() == 0) {
                MessageUtils.notify_Map(map, Notify.FIXED, MessageString.GuildWarDestroyTips2, dieName, dieName, nextName);
            } else {
                MessageUtils.notify_Map(map, Notify.FIXED, MessageString.GuildWarDestroyTips, dieName, dieName, nextName);
            }
        } else {
            for (Long attackId : monster.getDamages().keySet()) {
                Player player = Manager.playerManager.getPlayerCache(attackId);
                if (player != null) {
                    Manager.controlManager.operate(player, FunctionVariable.GuildWar_KillfudiBoss, 1);
                    Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
                    if (guild != null && guild.getChairMan().getId() == player.getId()) {
                        Manager.controlManager.operate(player, FunctionVariable.GuildWar_KillfudiBoss_leader, 1);
                    }
                }
            }
            //结束当前轮
            resetGuildBattle(map);
        }
    }

    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {
        Cfg_Guild_war_building_Bean bean = CfgManager.getCfg_Guild_war_building_Container().getValueByKey(monster.getModelId());
        if (bean == null) {
            return;
        }
        GuildBattleMapData zone = map.getZone();

        List<Map.Entry<Long, Long>> list = new ArrayList<>(monster.getDamages().entrySet());
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        int i = 1;
        for (Map.Entry<Long, Long> entry : list) {
            if (i > bean.getRankPoint()) {
                break;
            }
            Player player = Manager.playerManager.getPlayer(entry.getKey());
            GuildBattleMember gbm = zone.getGuild().get(player.getGuildId()).getMemberList().get(player.getId());
            if (bean.getType() == 0) {
                gbm.setBreakNum(gbm.getBreakNum() + 1);
            } else {
                gbm.setDestroyNum(gbm.getDestroyNum() + 1);
            }
            i++;
        }

        if (attacker instanceof Player) {
            Player player = (Player) attacker;
            GuildBattleData gbd = zone.getGuild().get(player.getGuildId());
            for (GuildBattleMember m : gbd.getMemberList().values()) {
                if (!map.getPlayers().containsKey(m.getRoleId())) {
                    continue;
                }
                if (attacker.getChangeModelState()) {
                    if (m.getRoleId() == player.getId()) {
                        m.setRecord(m.getRecord() + bean.getCarryDestroyPoint().get(0));
                    } else {
                        m.setRecord(m.getRecord() + bean.getCarryDestroyPoint().get(1));
                    }
                } else {
                    if (m.getRoleId() == player.getId()) {
                        m.setRecord(m.getRecord() + bean.getDestroyPoint().get(0));
                    } else {
                        m.setRecord(m.getRecord() + bean.getDestroyPoint().get(1));
                    }
                }

            }
        }
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {
        if (attacker instanceof Player) {
            GuildBattleMapData zone = map.getZone();

            Player att = (Player) attacker;
            GuildBattleData gbd = zone.getGuild().get(att.getGuildId());
            GuildBattleMember m = gbd.getMemberList().get(att.getId());
            m.setKillNum(m.getKillNum() + 1);
            m.setRecord(m.getRecord() + Global.Guild_War_Kill_Point);

            if (attacker.getChangeModelState()) {
                m.setCarrierNum(m.getCarrierNum() + 1);
            }

            GuildBattleData gbd1 = zone.getGuild().get(player.getGuildId());
            GuildBattleMember m1 = gbd1.getMemberList().get(player.getId());

            int winNum = getWinNum(2, m1.getWinNum());
            if (winNum == 0) {
                winNum = getWinNum(1, m.getWinNum() + 1);
            }

            m1.setWinNum(0);
            m.setWinNum(m.getWinNum() + 1);


            if (winNum != 0) {
//
//                int headId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID();
//                int headFrameId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID();
//                int aheadId = att.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID();
//                int aheadFrameId = att.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID();
                GuildBattleMessage.ResGuildBattleKillNum.Builder builder = GuildBattleMessage.ResGuildBattleKillNum.newBuilder();
                builder.setAttCareer(att.getCareer());
                // builder.setAttHead(aheadId);
                builder.setAttName(att.getName());
                builder.setDefCareer(player.getCareer());
                // builder.setDefHead(headId);
                builder.setDefName(player.getName());
                builder.setCfgId(winNum);
                //builder.setAtlHeadFrameId(aheadFrameId);
                //builder.setDefHeadFrameId(headFrameId);
                //玩家id
                builder.setAttId(att.getId());
                builder.setDefId(player.getId());

                builder.setAttHead(MapUtils.getHead(att));
                builder.setDefHead(MapUtils.getHead(player));

                MessageUtils.send_to_map(map, GuildBattleMessage.ResGuildBattleKillNum.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            }
        }
    }

    @Override
    public void action(MapObject map, String method, Object[] params) {
        switch (method) {
            case "readyTime":
                removeReadyBlockDoor(map);
                sendBattleAuth(map, null);
                break;
            case "addRecord":
                addGuardRecord(map);
                break;
            case "doTick":
                sendExpNote(map);
                sendBattleAuth(map, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void removeMap(MapObject map) {

    }

    @Override
    public int getId() {
        return ScriptEnum.GuildBatleMapScript;
    }

    @Override
    public Object call(Object... args) {
        String method = (String) args[0];
        MapObject map = (MapObject) args[1];
        switch (method) {
            case "endTime":
                endTime(map);
                break;
            case "gmTranCamp":
                resetGuildBattle(map);
                break;
            default:
                break;
        }
        return null;
    }

    private void sendExpNote(MapObject map) {

        ExpNoteData zone = map.getZone();

        for (Player player : map.getPlayers().values()) {
            long exp = zone.getExpNote().getOrDefault(player.getId(), 0L);
            GuildBattleMessage.ResGuildBattleExp.Builder message = GuildBattleMessage.ResGuildBattleExp.newBuilder();
            message.setExpReward(exp);
            MessageUtils.send_to_player(player, GuildBattleMessage.ResGuildBattleExp.MsgID.eMsgID_VALUE, message.build().toByteArray());
        }
    }


    //关闭准备空气墙
    private void removeReadyBlockDoor(MapObject mapObject) {
        MessageUtils.notify_Map(mapObject, Notify.FIXED, MessageString.GuildWarStart);
        for (String id : Global.Guild_War_Born_Air_Wall.getValue()) {
            MapManager.getInstance().setBlockDoor(mapObject, id, true);
        }
    }

    //发送上古意志归属
    void sendBattleAuth(MapObject mapObject, Guild guild) {
        if (guild != null) {
            MessageUtils.notify_Map(mapObject, Notify.FIXED, MessageString.GUILD_BATTLE_AUTHEVENT, guild.getName());
            return;
        }
        GuildBattleMapData zone = mapObject.getZone();
        long curTime = TimeUtils.Time();
        if (curTime - zone.getLastNotifyAuth() < 10 * 1000) {
            return;
        }
        if (zone.getGuild().isEmpty()) {
            return;
        }
        List<GuildBattleData> list = Utils.find(zone.getGuild().values(), g -> g.getAllHurt() > 0);
        if (list.isEmpty()) {
            return;
        }
        zone.setLastNotifyAuth(curTime);
        Collections.sort(list);
        GuildBattleData max = list.get(0);

        if (zone.getMonster() != null && !zone.getMonster().isDie()) {
            guild = Manager.guildsManager.getGuildById(max.getGuildId());
            MessageUtils.notify_Map(mapObject, Notify.FIXED, MessageString.GUILD_BATTLE_AUTHEVENT, guild.getName());
        }
    }

    //增加守护者积分
    private void addGuardRecord(MapObject mapObject) {
        GuildBattleMapData zone = mapObject.getZone();

        for (GuildBattleData guildBattleData : zone.getGuild().values()) {
            if (guildBattleData.getCamp() != DefenseCamp) {
                continue;
            }
            for (GuildBattleMember m : guildBattleData.getMemberList().values()) {
                if (mapObject.getPlayers().containsKey(m.getRoleId())) {
                    m.setRecord(m.getRecord() + Global.Guild_War_Defense_Point_Add.get(1));
                }
            }
        }
    }

    private void endTime(MapObject mapObject) {

        GuildBattleMapData zone = mapObject.getZone();

        //清理所有所有怪物
        for (Monster monster : mapObject.getMonsters().values()) {
            BehaviorManager.CancelAllBehavior(monster);
            Manager.mapManager.manager().onQuitMap(mapObject, monster, true);
        }

        int rateLv = zone.getLv();
        int globalLv = zone.getGlobalLv();
        //仙盟排名和奖励
        List<GuildBattleData> gbds = new ArrayList<>(zone.getGuild().values());
        Collections.sort(gbds, new GuildBattleResSort());

        int size = gbds.size();
        int i = 1;

        //是否有守方
        for (GuildBattleData gbd : gbds) {
            if (gbd.getCamp() == DefenseCamp) {
                gbd.setRank(i);
                i = i + 1;
                break;
            }
        }

        for (GuildBattleData gbd : gbds) {
            if (i > size) {
                break;
            }

            if (gbd.getCamp() == DefenseCamp) {
                continue;
            }
            gbd.setRank(i);
            i++;
        }

        for (GuildBattleData gbd : gbds) {
            //连赢奖励
            balanceGuildWin(gbd, rateLv);
        }

        CommandMessage.G2PSynGuildBattleInfo.Builder msg = CommandMessage.G2PSynGuildBattleInfo.newBuilder();
        Cfg_Guild_war_rank_Bean bean = CfgManager.getCfg_Guild_war_rank_Container().getValueByKey(rateLv);
        for (GuildBattleData gbd : gbds) {

            //结束消息
            List<GuildBattleMessage.GuildBattleMemberRecord> members = createEndMessage(gbd);

            //仙盟奖励
            List<Item> list = balanceGuildReward(gbd, rateLv, globalLv);

            List<GuildBattleMessage.GuildBattleItem> guildShow = new ArrayList<>();
            //增加道具
            for (Item item : list) {
                GuildBattleMessage.GuildBattleItem.Builder b = GuildBattleMessage.GuildBattleItem.newBuilder();
                b.setId(item.getItemModelId());
                b.setNum(item.getNum());
                guildShow.add(b.build());
            }
            //个人奖励
            balancePersonReward(mapObject, gbd, members, guildShow, bean);

            //重新计算排名
            doRankCalc(gbd, rateLv);


            Guild guild = Manager.guildsManager.getGuildById(gbd.getGuildId());

            if (rateLv == 1) {
                CommandMessage.GuildBattleInfo.Builder gbi = CommandMessage.GuildBattleInfo.newBuilder();
                gbi.setRank(gbd.getRank());
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
            }

            Manager.biManager.getScript().biGuildWar(gbd.getGuildId(), guild.getName(), guild.getLevel(), rateLv, 2, gbd.getCamp(), zone.getCurRound());
        }
        if (msg.getGuildBattleInfosCount() > 0) {
            MessageUtils.send_to_public(CommandMessage.G2PSynGuildBattleInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }

        ServerParamUtil.saveGuildBattleRate();
        ServerParamUtil.saveGuildBattleWin();
    }

    //获取当前建筑信息
    private int getMaxBuild(MapObject mapObject, int type) {

        GuildBattleMapData zone = mapObject.getZone();

        int buildId = 0;
        int tempCmp = 0;
        Iterator<Map.Entry<Integer, GuildBattleBuild>> buildInfos = zone.getBuildInfos().entrySet().iterator();
        if (type == 0) {
            //修复最外层建筑
            while (buildInfos.hasNext()) {
                Map.Entry<Integer, GuildBattleBuild> entry = buildInfos.next();
                if (entry.getValue().getHp() != 0) {
                    continue;
                }

                Cfg_Guild_war_building_Bean bean = CfgManager.getCfg_Guild_war_building_Container().getValueByKey(entry.getKey());
                if (bean.getType() >= tempCmp) {
                    buildId = entry.getKey();
                    tempCmp = bean.getType();
                }
            }
        } else {
            while (buildInfos.hasNext()) {
                Map.Entry<Integer, GuildBattleBuild> entry = buildInfos.next();
                if (entry.getValue().getHp() == 0) {
                    continue;
                }

                Cfg_Guild_war_building_Bean bean = CfgManager.getCfg_Guild_war_building_Container().getValueByKey(entry.getKey());
                if (bean.getType() == zone.getCurType()) {
                    buildId = entry.getKey();
                    break;
                }
            }
        }
        return buildId;
    }

    private void resetGuildBattle(MapObject mapObject) {

        GuildBattleMapData zone = mapObject.getZone();

        //清理所有所有怪物
        for (Monster monster : mapObject.getMonsters().values()) {
            BehaviorManager.CancelAllBehavior(monster);
            Manager.mapManager.manager().onQuitMap(mapObject, monster, true);
        }

        //刷怪物
        initMonster(mapObject, zone);

        //分配阵营
        int perBuffId = getWinBuff(mapObject);
        List<GuildBattleData> gbds = new ArrayList<>(zone.getGuild().values());
        Collections.sort(gbds);

        GuildBattleMessage.ResGuildBattleTranCamp.Builder builder = GuildBattleMessage.ResGuildBattleTranCamp.newBuilder();
        int tempIndex = 0;
        for (GuildBattleData gbd : gbds) {
            Guild guild = Manager.guildsManager.getGuildById(gbd.getGuildId());
            GuildBattleMessage.GuildBattleCamp.Builder campBuilder = GuildBattleMessage.GuildBattleCamp.newBuilder();
            campBuilder.setGuildName(guild.getName());
            campBuilder.setIcon(guild.getIcon());
            if (tempIndex == 0) {
                gbd.setCamp(DefenseCamp);
                gbd.setIndex(Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV);
                builder.setDefCamp(campBuilder);
                sendBattleAuth(mapObject, guild);
            } else {
                gbd.setCamp(AttackCamp);
                gbd.setIndex(tempIndex - 1);
                builder.addAttCamps(campBuilder);
            }
            gbd.setAllHurt(0);
            tempIndex++;
            Manager.biManager.getScript().biGuildWar(gbd.getGuildId(), guild.getName(), guild.getLevel(), zone.getLv(), 1, gbd.getCamp(), zone.getCurRound() - 1);
        }

        int afterBuffId = getWinBuff(mapObject);

        int maxLiveBuildId = getMaxBuild(mapObject, 1);
        int maxDieBuildId = getMaxBuild(mapObject, 0);


        for (GuildBattleData gbd : gbds) {
            for (GuildBattleMember gbm : gbd.getMemberList().values()) {
                Player player = mapObject.getPlayers().get(gbm.getRoleId());
                if (player == null) {
                    continue;
                }

                player.setCamp(gbd.getCamp(), true);

                //移除玩家转换前buff
                if (perBuffId != 0) {
                    Manager.buffManager.deal().onRemoveBuff(player, perBuffId);
                }

                //移除变身buff
                if (player.getChangeModleID() != 0) {
                    Manager.buffManager.deal().onReqRemoveChangeModeBuff(player, player.getChangeModleID());
                }

                //增加攻击方buff
                if (afterBuffId != 0 && gbd.getCamp() == AttackCamp) {
                    Manager.buffManager.deal().onAddBuff(player, player, afterBuffId);
                }

                //发送tips
                if (gbd.getCamp() == AttackCamp) {
                    MessageUtils.notify_player(player, Notify.FIXED, MessageString.GuildWarDistributionAtt);
                } else {
                    MessageUtils.notify_player(player, Notify.FIXED, MessageString.GuildWarDistributionDistribution);
                }

                //同步数据
                sysnPanelData(player, mapObject, maxLiveBuildId, maxDieBuildId, player.getCamp());

                //迁移玩家
                Manager.mapManager.transport().ResCurMapTransport(player, mapObject, mapObject.getBriths().get(gbd.getIndex()), 0, 0);

                //发送战绩结果
                MessageUtils.send_to_player(player, GuildBattleMessage.ResGuildBattleTranCamp.MsgID.eMsgID_VALUE, builder.build().toByteArray());

            }
        }
    }

    private void sysnPanelData(Player player, MapObject mapObject, int maxLiveBuildId, int maxDieBuildId, int camp) {

        GuildBattleMapData zone = mapObject.getZone();

        GuildBattleMessage.ResGuildBattlePanel.Builder builder = GuildBattleMessage.ResGuildBattlePanel.newBuilder();
        builder.setCamp(camp);

        if (maxLiveBuildId != 0) {
            int pc = zone.getBuildInfos().get(maxLiveBuildId).getHp();
            builder.setBuildId(maxLiveBuildId);
            builder.setBuildPc(pc);
        } else {
            builder.setBuildId(0);
            builder.setBuildPc(0);
        }

        if (camp == 1) {
            builder.setRepairBuildId(maxDieBuildId);
        } else {
            builder.setRepairBuildId(0);
        }

        for (GuildBattleBuild build : zone.getBuildInfos().values()) {
            GuildBattleMessage.GuildBattleGather.Builder gather = GuildBattleMessage.GuildBattleGather.newBuilder();
            gather.setMonsterId(build.getBuildId());
            gather.setTime(build.getBeginTime());
            builder.addGathers(gather);

            //正在被修复的兼职
            if (build.getStartRepair() > 0) {
                GuildBattleMessage.BattleEvent.Builder repair = GuildBattleMessage.BattleEvent.newBuilder();
                repair.setModelId(build.getBuildId());
                repair.setStart(build.getStartRepair());
                builder.addStopRepairs(repair);
            }
        }
        MessageUtils.send_to_player(player, GuildBattleMessage.ResGuildBattlePanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void initMonster(MapObject mapObject, GuildBattleMapData zone) {

        //创建怪物
        int minGlobalLv = 0;
        for (Cfg_Guild_war_building_Bean bean : CfgManager.getCfg_Guild_war_building_Container().getValuees()) {
            if (minGlobalLv == 0 && zone.getGlobalLv() <= bean.getWorldLevel()) {
                minGlobalLv = bean.getWorldLevel();
                continue;
            }

            if (zone.getGlobalLv() <= bean.getWorldLevel() && minGlobalLv > bean.getWorldLevel()) {
                minGlobalLv = bean.getWorldLevel();
            }
        }
        Monster m = null;
        List<Integer> buffs = new ArrayList<>();

        zone.getBuildInfos().clear();
        if (minGlobalLv != 0) {
            for (Cfg_Guild_war_building_Bean bean : CfgManager.getCfg_Guild_war_building_Container().getValuees()) {
                if (bean.getWorldLevel() == minGlobalLv) {
                    Position pos = new Position(bean.getPos().get(0), bean.getPos().get(1));
                    Monster monster = Manager.monsterManager.createMonster(mapObject, pos, bean.getId(), DefenseCamp);

                    if (bean.getReduceHurt() != 0) {
                        buffs.add(bean.getReduceHurt());
                    }

                    if (bean.getType() == 0) {
                        m = monster;
                        zone.setMonster(m);
                    }

                    if (bean.getType() == 3) {
                        zone.setCurType(bean.getType());
                    }
                    GuildBattleBuild battleBuild = new GuildBattleBuild();
                    battleBuild.setBuildId(bean.getId());
                    battleBuild.setHp(100);
                    battleBuild.setBeginTime(0);
                    battleBuild.setStartRepair(0);
                    zone.getBuildInfos().put(bean.getId(), battleBuild);
                    MapManager.getInstance().setBlockDoor(mapObject, bean.getAirWall(), false);
                }
            }
        }

        zone.setCurRound(zone.getCurRound() + 1);
        log.info("地图:" + mapObject.toString() + "仙盟战第" + zone.getCurRound() + "轮");
        //增加buff
        for (int id : buffs) {
            Manager.buffManager.deal().onAddBuff(m, m, id);
        }
    }

    private List<GuildBattleMessage.GuildBattleMemberRecord> createEndMessage(GuildBattleData gbd) {

        List<GuildBattleMessage.GuildBattleMemberRecord> members = new ArrayList<>();

        List<Map.Entry<Long, GuildBattleMember>> list = new ArrayList<>(gbd.getMemberList().entrySet());
        Collections.sort(list, new GuildBattleKillSort());

        int temp = 1;
        HashMap<Long, Integer> killRanks = new HashMap<>();
        for (Map.Entry<Long, GuildBattleMember> entry : list) {
            if (temp > 3 || (entry.getValue().getKillNum() == 0)) {
                break;
            }
            killRanks.put(entry.getKey(), temp);
            temp++;
        }

        long maxBeast = 0;
        Collections.sort(list, new GuildBattleBeastSort());
        if (list.size() != 0 && list.get(0).getValue().getCarrierNum() != 0) {
            maxBeast = list.get(0).getKey();
        }

        long maxAttaCity = 0;
        Collections.sort(list, new GuildBattleAttaCitySort());
        if (list.size() != 0 && (list.get(0).getValue().getRepairNum() + list.get(0).getValue().getDestroyNum()) != 0) {
            maxAttaCity = list.get(0).getKey();
        }

        Collections.sort(list, new GuildBattleRecordSort());

        temp = 1;
        for (Map.Entry<Long, GuildBattleMember> entry : list) {
            if (temp > 5) {
                break;
            }

            GuildBattleMessage.GuildBattleMemberRecord.Builder gmrMsg = GuildBattleMessage.GuildBattleMemberRecord.newBuilder();
            Player player = Manager.playerManager.getPlayerCache(entry.getKey());
            if (player == null) {
                continue;
            }
            entry.getValue().setRank(temp);
            gmrMsg.setRoelId(player.getId());
            gmrMsg.setCareer(player.getCareer());
            gmrMsg.setName(player.getName());
            gmrMsg.setIsAttCity(maxAttaCity == player.getId());
            gmrMsg.setIsBast(maxBeast == player.getId());
            gmrMsg.setRecord(entry.getValue().getRecord());
            gmrMsg.setHead(MapUtils.getHead(player));

            Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
            if (guild != null) {
                GuildMember guildMember = guild.getMembers().get(player.getId());
                gmrMsg.setTitle(guildMember.getPosition());
            } else {
                gmrMsg.setTitle(0);
            }
            Integer rank = killRanks.get(player.getId());
            if (rank == null) {
                gmrMsg.setKillRank(0);
            } else {
                gmrMsg.setKillRank(rank);
            }
            members.add(gmrMsg.build());
            temp++;
        }
        return members;
    }

    /**
     * 重新计算排名
     *
     * @param gbd
     * @param rateLv
     */
    private void doRankCalc(GuildBattleData gbd, int rateLv) {
        GuildBattle guildBattle = Manager.guildBattleManager.getGuildbattles().get(gbd.getGuildId());

        //同步公会成员数据
        guildBattle.setMapId(0);
        guildBattle.setMemberList(new ArrayList<>(gbd.getMemberList().values()));

        //同步名次
        if (rateLv == 1 && gbd.getRank() == 1) {
            //战神
            guildBattle.setRank(1);
            Guild guild = Manager.guildsManager.getGuildById(gbd.getGuildId());
            Cfg_Guild_war_rank_Bean bean = CfgManager.getCfg_Guild_war_rank_Container().getValueByKey(rateLv);
            MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_MARQUEE, MessageString.GuildWarRankTips, guild.getName(), ServerStr.getChatTableName(bean.getName()));
        } else if (rateLv != 1 && gbd.getRank() == 1) {
            //晋级
            guildBattle.setRank((rateLv - 1) * Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV);
        } else if (rateLv != 4 && gbd.getRank() == 3) {
            //降级
            guildBattle.setRank(rateLv * Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV + 1);
        } else if (rateLv == 4 && gbd.getRank() == 3) {
            //最低降级
            guildBattle.setRank(rateLv * Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV);
        } else {
            guildBattle.setRank((rateLv - 1) * Manager.guildBattleManager.MAX_GUILD_NUM_EVERY_LV + 2);
        }

        log.info("仙盟战结束:当前仙盟:" + gbd.getGuildId() + "仙盟等级:" + rateLv + "仙盟名次:" + guildBattle.getRank());
    }

    private void balancePersonReward(MapObject mapObject, GuildBattleData gbd, List<GuildBattleMessage.GuildBattleMemberRecord> members, List<GuildBattleMessage.GuildBattleItem> guildShow, Cfg_Guild_war_rank_Bean bean) {
        GuildBattleMapData zone = mapObject.getZone();

        ReadIntegerArrayEs readIntegerArrayEs = null;
        if (gbd.getRank() == 1) {
            readIntegerArrayEs = bean.getPersonalReward1();
        }

        if (gbd.getRank() == 2) {
            readIntegerArrayEs = bean.getPersonalReward2();
        }

        if (gbd.getRank() == 3) {
            readIntegerArrayEs = bean.getPersonalReward3();
        }

        for (GuildBattleMember gbm : gbd.getMemberList().values()) {
            Guild guild = Manager.guildsManager.getGuildById(gbd.getGuildId());

            PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(gbm.getRoleId());
            Player player = mapObject.getPlayer(gbm.getRoleId());

            List<Item> items = Item.createItems(playerWorldInfo.getCareer(), readIntegerArrayEs, 1);

            String str = MessageString.GuildWarPersonalRewardMailContext + "@_@" + guild.getName() + "@_@" +
                    ServerStr.getChatTableName(bean.getName()) + "@_@" + gbd.getRank() + "@_@" + gbm.getRecord();
            Manager.mailManager.sendMailToPlayer(gbm.getRoleId(), 1, MessageString.System, MessageString.GuildWarPersonalRewardMailTitle, str, items, ItemChangeReason.GuildWarPersonalRewardGet);

            String str1 = MessageString.GuildWarRewardMailContext + "@_@" + guild.getName() + "@_@" +
                    ServerStr.getChatTableName(bean.getName()) + "@_@" + gbd.getRank();
            Manager.mailManager.sendMailToPlayer(gbm.getRoleId(), 1, MessageString.System, MessageString.GuildWarRewardMailTitle, str1);

            if (player != null && player.isOnline()) {
                player.setCamp(DefenseCamp, true);
                GuildBattleMessage.ResGuildBattleEnd.Builder builder = GuildBattleMessage.ResGuildBattleEnd.newBuilder();
                builder.addAllRecords(members);
                builder.addAllItems(guildShow);
                //个人奖励展示
                GuildBattleMessage.GuildBattleItem.Builder mExp = GuildBattleMessage.GuildBattleItem.newBuilder();
                mExp.setId(ItemCoinType.EXP);
                mExp.setNum(zone.getExpNote().getOrDefault(playerWorldInfo.getRoleid(), 0L));
                builder.addPersonal(mExp);

                for (Item item : items) {
                    GuildBattleMessage.GuildBattleItem.Builder mItem = GuildBattleMessage.GuildBattleItem.newBuilder();
                    mItem.setId(item.getItemModelId());
                    mItem.setNum(item.getNum());
                    builder.addPersonal(mItem);
                }
                MessageUtils.send_to_player(player, GuildBattleMessage.ResGuildBattleEnd.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            }
        }

    }

    private List<Item> balanceGuildReward(GuildBattleData gbd, int rateLv, int globaLv) {
        List<Item> list = new ArrayList<>();

        Cfg_Guild_war_closereward_Bean bean = null;
        for (Cfg_Guild_war_closereward_Bean b : CfgManager.getCfg_Guild_war_closereward_Container().getValuees()) {
            if (b.getType() == rateLv && globaLv >= b.getWorldLevel().get(0) && globaLv <= b.getWorldLevel().get(1)) {
                bean = b;
            }
        }
        if (bean == null) {
            return list;
        }
        Player player = null;

        for (long id : gbd.getMemberList().keySet()) {
            player = Manager.playerManager.getPlayer(id);
            if (player != null) {
                break;
            }
        }
        if (player == null) {
            return list;
        }
        ReadIntegerArray guildDrop = null;
        if (gbd.getRank() == 1) {
            guildDrop = bean.getGuildDropGoup1();
        } else if (gbd.getRank() == 2) {
            guildDrop = bean.getGuildDropGoup2();
        } else if (gbd.getRank() == 3) {
            guildDrop = bean.getGuildDropGoup3();
        }
        if (guildDrop == null) {
            return list;
        }
        for (int dropId : guildDrop.getValue()) {
            list.addAll(Item.createItems(Manager.dropManager.deal().getItemDrops(player, dropId), 1));
        }

        List<Long> l = new ArrayList<>(gbd.getMemberList().keySet());
        Manager.auctionManager.manager().auctionActivityPut(l, list, DailyActiveDefine.ACTIVITY_GUILDBATTLE.getValue(), gbd.getGuildId());
        return list;
    }

    private void balanceGuildWin(GuildBattleData gbd, int rateLv) {
        if (rateLv == 1 && gbd.getRank() == 1) {
            GuildBattleWin win = Manager.guildBattleManager.getGuildBattleWin();
            win.getRoleIds().clear();
            log.info("仙盟战上次连胜次数:" + win.getGuildId() + "类型:" + win.getType() + "数量:" + win.getNum() + "终结公会:" + win.getNum());
            String n = "";
            if (win.getGuildId() == 0 || win.getGuildId() == gbd.getGuildId()) {
                //连赢
                //上次终结 这次连赢
                if (win.getType() == 1) {
                    win.setHasGet(0);
                    win.setNum(2);
                } else {
                    win.setNum(win.getNum() + 1);
                }
                win.setGuildId(gbd.getGuildId());
                win.setType(0);
            } else {
                //终结
                Guild guild = Manager.guildsManager.getGuildById(win.getGuildId());
                if (guild != null) {
                    n = guild.getName();
                    win.setBeGuildName(guild.getName());
                    sendGuildWinBreak(guild);
                }
                win.setHasGet(0);
                win.setGuildId(gbd.getGuildId());
                win.setType(1);
            }
            log.info("仙盟战连胜次数:" + gbd.getGuildId() + "类型:" + win.getType() + "数量:" + win.getNum() + "终结公会:" + n);
            win.getRoleIds().addAll(gbd.getMemberList().keySet());
        }
    }

    void sendGuildWinBreak(Guild guild) {
        if (guild.getProxyChairMan() != null) {
            long playerId = guild.getProxyChairMan().getPlayerWorldInfo().getRoleid();
            GuildBattleMessage.ResGuildBattleRedPoint.Builder builder = GuildBattleMessage.ResGuildBattleRedPoint.newBuilder();
            builder.setRedPoint(false);
            MessageUtils.send_to_player(playerId, GuildBattleMessage.ResGuildBattleRedPoint.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
        if (guild.getChairMan() != null) {
            long playerId = guild.getChairMan().getPlayerWorldInfo().getRoleid();
            GuildBattleMessage.ResGuildBattleRedPoint.Builder builder = GuildBattleMessage.ResGuildBattleRedPoint.newBuilder();
            builder.setRedPoint(false);
            MessageUtils.send_to_player(playerId, GuildBattleMessage.ResGuildBattleRedPoint.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    private int getWinBuff(MapObject mapObject) {

        GuildBattleMapData zone = mapObject.getZone();

        long guildId = 0;

        for (GuildBattleData gbd : zone.getGuild().values()) {
            if (gbd.getCamp() == DefenseCamp) {
                guildId = gbd.getGuildId();
                break;
            }
        }

        if (guildId == 0) {
            return 0;
        }

        GuildBattleWin win = Manager.guildBattleManager.getGuildBattleWin();
        int winNum = 0;
        if (win.getGuildId() == guildId && win.getType() == 0) {
            winNum = win.getNum();
        }

        if (winNum == 0) {
            return 0;
        }

        int panguBuff = 0;
        for (Cfg_Guild_war_reward_Bean bean : CfgManager.getCfg_Guild_war_reward_Container().getValuees()) {
            if (bean.getCount() > winNum) {
//                panguBuff = bean.getPanguBuff();
                break;
            } else {
                panguBuff = bean.getPanguBuff();
            }
        }

        return panguBuff;
    }

    private int getWinNum(int type, int num) {
        int id = 0;
        for (Cfg_Guild_war_contkill_Bean bean : CfgManager.getCfg_Guild_war_contkill_Container().getValuees()) {
            if (bean.getType() == type && num >= bean.getCount()) {
                id = bean.getId();
            }
        }
        return id;
    }

    @Override
    public boolean onBeginGather(Player player, Gather gather) {
        if (player.getCamp() == AttackCamp) {
            return false;
        }

        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(gather.getModelId());
        if (null == gatherCfg) {
            return false;
        }
        GuildBattleMapData zone = mapObject.getZone();

        int globalLv = zone.getGlobalLv();

        //重新刷新怪物
        Cfg_Guild_war_building_Bean bean = null;
        for (Cfg_Guild_war_building_Bean b : CfgManager.getCfg_Guild_war_building_Container().getValuees()) {
            if (b.getGather() == gather.getModelId() && globalLv <= b.getWorldLevel()) {
                bean = b;
                break;
            }
        }

        if (bean == null) {
            return false;
        }

        for (Monster monster : mapObject.getMonsters().values()) {
            if (monster.getModelId() == bean.getId()) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_War_Repair_Build_Notice);
                return false;
            }
        }

        GuildBattleBuild guildBattleBuild = zone.getBuildInfos().get(bean.getId());
        if (guildBattleBuild == null) {
            log.error("guildBattleBuild error=====:" + bean.getId());
            for (GuildBattleBuild gbb : zone.getBuildInfos().values()) {
                log.error("gbb has ========:" + gbb.getBuildId());
            }
            return false;
        }

        long cd = guildBattleBuild.getBeginTime();
        if (cd != 0 && TimeUtils.Time() < cd + bean.getRepairCD() * 1000L) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_War_Repair_Build_CD, String.valueOf((cd + bean.getRepairCD() * 1000L - TimeUtils.Time()) / 1000L));
            return false;
        }
        guildBattleBuild.setStartRepair(TimeUtils.Time());

        //刷新面板
        int maxLiveBuildId = getMaxBuild(mapObject, 1);
        int maxDieBuildId = getMaxBuild(mapObject, 0);
        for (Player p : mapObject.getPlayers().values()) {
            sysnPanelData(p, mapObject, maxLiveBuildId, maxDieBuildId, p.getCamp());
        }
        return true;
    }

    @Override
    public void onGather(Player player, Gather gather) {
        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        Cfg_Gather_Bean gatherCfg = CfgManager.getCfg_Gather_Container().getValueByKey(gather.getModelId());
        if (null == gatherCfg) {
            return;
        }
        GuildBattleMapData zone = mapObject.getZone();

        //重新刷新怪物
        Cfg_Guild_war_building_Bean bean = null;
        for (Cfg_Guild_war_building_Bean b : CfgManager.getCfg_Guild_war_building_Container().getValuees()) {
            if (b.getGather() == gather.getModelId() && zone.getGlobalLv() <= b.getWorldLevel()) {
                bean = b;
                break;
            }
        }

        if (bean == null) {
            return;
        }
        GuildBattleBuild guildBattleBuild = zone.getBuildInfos().get(bean.getId());

        if (guildBattleBuild.getHp() > 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_War_Repair_Build_Notice);
            return;
        }

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        String guildName = "";
        if (guild != null) {
            guildName = guild.getName();
        }

        MessageUtils.notify_Map(mapObject, Notify.FIXED, MessageString.GuildWarRepiarTips, guildName, player.getName(), ServerStr.getChatTableName(bean.getName()), ServerStr.getChatTableName(bean.getName()));

        Position pos = new Position(bean.getPos().get(0), bean.getPos().get(1));
        Manager.monsterManager.createMonster(mapObject, pos, bean.getId(), 1);

        guildBattleBuild.setHp(100);
        guildBattleBuild.setBeginTime(TimeUtils.Time());
        guildBattleBuild.setStartRepair(0);

        GuildBattleData gbd = zone.getGuild().get(player.getGuildId());
        GuildBattleMember gbm = gbd.getMemberList().get(player.getId());
        gbm.setRepairNum(gbm.getRepairNum() + 1);

        //增加积分
        for (GuildBattleMember m : gbd.getMemberList().values()) {
            if (!mapObject.getPlayers().containsKey(m.getRoleId())) {
                continue;
            }
            if (player.getChangeModelState()) {
                if (m.getRoleId() == player.getId()) {
                    m.setRecord(m.getRecord() + bean.getCarryRepiarPoint().get(0));
                } else {
                    m.setRecord(m.getRecord() + bean.getCarryRepiarPoint().get(1));
                }
            } else {
                if (m.getRoleId() == player.getId()) {
                    m.setRecord(m.getRecord() + bean.getRepairPoint().get(0));
                } else {
                    m.setRecord(m.getRecord() + bean.getRepairPoint().get(1));
                }
            }
        }

        //增加buff
        Manager.buffManager.deal().onAddBuff(zone.getMonster(), zone.getMonster(), bean.getReduceHurt());

        //空气墙
        MapManager.getInstance().setBlockDoor(mapObject, bean.getAirWall(), false);

        //刷新面板
        int maxLiveBuildId = getMaxBuild(mapObject, 1);
        int maxDieBuildId = getMaxBuild(mapObject, 0);
        for (Player p : mapObject.getPlayers().values()) {
            sysnPanelData(p, mapObject, maxLiveBuildId, maxDieBuildId, p.getCamp());
        }
    }

    @Override
    public void onOutGather(Player player, Gather gather) {

        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        GuildBattleMapData zone = mapObject.getZone();

        Cfg_Guild_war_building_Bean bean = null;
        for (Cfg_Guild_war_building_Bean b : CfgManager.getCfg_Guild_war_building_Container().getValuees()) {
            if (b.getGather() == gather.getModelId() && zone.getGlobalLv() <= b.getWorldLevel()) {
                bean = b;
                break;
            }
        }

        if (bean == null) {
            return;
        }

        GuildBattleBuild guildBattleBuild = zone.getBuildInfos().get(bean.getId());
        guildBattleBuild.setStartRepair(0);

        //刷新面板
        int maxLiveBuildId = getMaxBuild(mapObject, 1);
        int maxDieBuildId = getMaxBuild(mapObject, 0);
        for (Player p : mapObject.getPlayers().values()) {
            sysnPanelData(p, mapObject, maxLiveBuildId, maxDieBuildId, p.getCamp());
        }
    }

    /**
     * 计算副本复活点
     *
     * @param map
     * @param player
     * @return
     */
    @Override
    public Position doCreateRelivePosition(MapObject map, Player player) {
        GuildBattleMapData zone = map.getZone();
        GuildBattleData guildBattleData = zone.getGuild().get(player.getGuildId());

        return map.getRelives().get(guildBattleData.getIndex());
    }
}
