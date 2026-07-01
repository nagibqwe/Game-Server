package common.world_help;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.*;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildMember;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapDefine;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Entity;
import com.game.structs.Fighter;
import com.game.structs.Hatred;
import com.game.task.structs.ConquerTask;
import com.game.team.manager.TeamManager;
import com.game.team.structs.TeamInfo;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.world_help.inter.IWorldHelpScript;
import com.game.world_help.struct.SpecialHatred;
import com.game.world_help.struct.TaskHelpInfo;
import com.game.world_help.struct.WorldHelp;
import com.game.world_help.struct.WorldHelpInfo;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.BossMessage;
import game.message.WorldHelpMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @explain: desc
 * @time Created on 2019/12/11 15:14.
 * @author: tc
 */
public class WorldHelpScript implements IWorldHelpScript {
    private final Logger log = LogManager.getLogger(WorldHelpScript.class);

    private final int DEFAULT_VALUE = -1;

    /**
     * 有奖励次数限制的Map：地图脚本ID，活动类型
     */
    private HashMap<Integer, Integer> rewardLimitMap = new HashMap<>();

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.WorldHelpScript;
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
     * 玩家上线
     *
     * @param player
     */
    @Override
    public void online(Player player) {
        // modify error data
        clear(player.getId());

        for (Map.Entry<Integer, WorldHelpInfo> entry : Manager.worldHelpManager.getHelp().entrySet()) {
            if (!valid(entry.getValue())) continue;
            MessageUtils.send_to_player(player, WorldHelpMessage.SyncWorldHelp.MsgID.eMsgID_VALUE,
                    WorldHelpMessage.SyncWorldHelp.newBuilder().build().toByteArray());
            break;
        }
    }

    /**
     * offline
     *
     * @param player
     */
    @Override
    public void offline(Player player) {
        clear(player.getId());
    }

    /**
     * 玩家死亡
     *
     * @param player
     */
    @Override
    public void die(Player player) {
        clear(player.getId());
    }

    /**
     * 请求世界支援
     *
     * @param player
     * @param bossCode
     */
    @Override
    public void onReqWorldHelp(Player player, long bossCode) {
        if (player.isDie())
            return;

        Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
        if (guild == null)
            return;

        if (Manager.worldHelpManager.getJoin().containsKey(player.getId())) {
            if (Manager.worldHelpManager.getJoin().get(player.getId()).isInitiate())
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpAlreadyInitiate);
            else
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpAlreadyJoin);
            return;
        }

        Cfg_World_Support_Bean wsB = cfg(player.getLevel());
        if (wsB == null)
            return;

        long now = TimeUtils.Time();
        if (now - player.getLastWorldHelpTime() < wsB.getCold_times() * 1000) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpCollDown);
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map.getType() != MapDefine.WORLD_MAP)
            return;

        Cfg_Mapsetting_Bean mB = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (mB == null)
            return;

        if (mB.getIf_World_Support() != 1)
            return;

        // is attack the boss
        Monster monster = map.getMonsters().get(bossCode);
        if (monster == null || monster.isDie())
            return;

        boolean isAttack = false;
        for (Hatred hatred : monster.getHatreds()) {
            if (!(hatred.getTarget() instanceof Player)) {
                continue;
            }

            if (hatred.getTarget().getId() == player.getId()) {
                isAttack = true;
                break;
            }
        }
        if (!isAttack) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpNoAttack);
            return;
        }

        // cool
        player.setLastWorldHelpTime(now);

        // yes
        int id = Manager.worldHelpManager.newID();
        WorldHelpInfo info = new WorldHelpInfo();
        info.setId(id);
        info.setBossCode(bossCode);
        info.setBossId(monster.getModelId());
        info.setHelpIds(new ArrayList<>());
        info.setMapId(mB.getMap_id());
        info.setMapUid(map.getId());
        info.setOwnerId(player.getId());
        info.setTime(now);
        WorldHelp worldHelp = new WorldHelp(id, true);
        Manager.worldHelpManager.getHelp().put(id, info);
        Manager.worldHelpManager.getJoin().put(player.getId(), worldHelp);

        List<Integer> list = Manager.worldHelpManager.getBoss().computeIfAbsent(bossCode, k -> new ArrayList<>());
        list.add(id);

        Manager.countManager.addVariant(player, VariantType.WorldHelpNum, 1);
        Manager.controlManager.operate(player, FunctionVariable.WorldSupportSeek, 1);

        //仙盟求援 发送到 仙盟频道信息
        MessageUtils.notify_guild_Chat(guild, MessageString.C_Xianmeng_Support, monster.getName(), Utils.makeUrlStr(MessageString.C_Xianmeng_Support));

        // 通知自己公会的玩家
        MessageUtils.send_TO_Guild(guild, WorldHelpMessage.SyncWorldHelp.MsgID.eMsgID_VALUE,
                WorldHelpMessage.SyncWorldHelp.newBuilder().build().toByteArray());
    }

    /**
     * 请求已有的世界支援列表
     *
     * @param player
     */
    @Override
    public void onReqWorldHelpList(Player player) {
        WorldHelpMessage.ResWorldHelp.Builder builder = WorldHelpMessage.ResWorldHelp.newBuilder();
        long guildID = player.getGuildId();
        if (guildID <= 0) {
            return;
        }
        for (Map.Entry<Integer, WorldHelpInfo> entry : Manager.worldHelpManager.getHelp().entrySet()) {
            if (!valid(entry.getValue())) continue;

            // judge guild
            GuildMember member = Manager.guildsManager.getGuildMembers().get(entry.getValue().getOwnerId());
            if (member == null || member.getGuildId() != guildID)
                continue;

            builder.addHelps(packWorldHelp(entry.getValue()));
        }
        //任务副本支援
        for (Map.Entry<Integer, TaskHelpInfo> entry : Manager.worldHelpManager.getTaskHelp().entrySet()) {
            if (!valid(entry.getValue())) continue;

            // judge guild
            GuildMember member = Manager.guildsManager.getGuildMembers().get(entry.getValue().getOwnerId());
            if (member == null || member.getGuildId() != guildID)
                continue;

            builder.addTaskHelps(packTaskHelp(entry.getValue()));
        }

        MessageUtils.send_to_player(player, WorldHelpMessage.ResWorldHelp.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        Cfg_World_Support_Bean bean = cfg(player.getLevel());
        WorldHelpMessage.SyncPrestige.Builder builder1 = WorldHelpMessage.SyncPrestige.newBuilder();
        builder1.setPrestige(count(player, bean.getPic_res().get(0)));
        MessageUtils.send_to_player(player, WorldHelpMessage.SyncPrestige.MsgID.eMsgID_VALUE, builder1.build().toByteArray());
    }

    /**
     * 同意加入世界支援
     *
     * @param player
     * @param id
     */
    @Override
    public void onReqJoinHelp(Player player, int id) {
        if (player.isDie())
            return;

        if (Manager.worldHelpManager.getJoin().containsKey(player.getId())) {
            if (Manager.worldHelpManager.getJoin().get(player.getId()).isInitiate())
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpAlreadyInitiate);
            else
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpAlreadyJoin);
            return;
        }

        WorldHelpInfo info = Manager.worldHelpManager.getHelp().get(id);
        if (info == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpJoinEnd);
            return;
        }

        Player owner = Manager.playerManager.getPlayer(info.getOwnerId());
        if (owner == null)
            return;

        if (!owner.isOnline())
            return;

        if (player.getGuildId() != owner.getGuildId()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildNotTheSame);
            return;
        }

        Cfg_World_Support_Bean wsB = cfg(owner.getLevel());
        if (wsB == null)
            return;

        if (info.getHelpIds().size() >= wsB.getMax_times()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpMaxNum);
            return;
        }

        // 只有野图能加入
        MapObject selfMap = Manager.mapManager.getMap(player.gainMapId());
        MapObject map = Manager.mapManager.getMap(owner.gainMapId());
        if (map.getType() != MapDefine.WORLD_MAP)
            return;

        if (!Manager.mapManager.transport().canEnterMap(player, map.getMapModelId()))
            return;

        if (selfMap.getZoneModelId() > 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_CHANGEMAP_FAILED_COPYMAP);
            return;
        }

        // owner is attacking the boss
        Monster monster = map.getMonsters().get(info.getBossCode());
        if (monster == null || monster.isDie())
            return;

        boolean isAttack = false;
        for (Hatred hatred : monster.getHatreds()) {
            if (!(hatred.getTarget() instanceof Player)) {
                continue;
            }

            if (hatred.getTarget().getId() == info.getOwnerId()) {
                isAttack = true;
                break;
            }
        }
        if (!isAttack) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpNoAttack);
            return;
        }

        Manager.worldHelpManager.getJoin().put(player.getId(), new WorldHelp(id, false));
        info.getHelpIds().add(player.getId());

        // yes, change map
        // 判断玩家当前是否正在此地图
        if (map.getId() != selfMap.getId()) {

            Manager.mapManager.changeMap(player, map.getId(), map.getBrithPos(), false);
        } else {
            enterMap(player, selfMap);
        }
    }

    /**
     * enter map
     *
     * @param player
     * @param map
     */
    @Override
    public void enterMap(Player player, MapObject map) {
        Cfg_Mapsetting_Bean mB = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (mB == null)
            return;

        if (mB.getIf_World_Support() != 1) {
            clear(player.getId());
            return;
        }
        // 进入到可以支援的地图了
        if (!Manager.worldHelpManager.getJoin().containsKey(player.getId()))
            return;

        WorldHelp join = Manager.worldHelpManager.getJoin().get(player.getId());
        WorldHelpInfo info = Manager.worldHelpManager.getHelp().get(join.getId());
        if (info == null) {
            clear(player.getId());
            return;
        }

        if (map.getId() != info.getMapUid()) {
            clear(player.getId());
            return;
        }

        Player owner = Manager.playerManager.getPlayer(info.getOwnerId());
        if (owner == null) {
            log.error("发起支援的玩家已经不存在了：" + info.getOwnerId());
            clear(player.getId());
            return;
        }

        // 同步
        if (!join.isInitiate()) {
            WorldHelpMessage.SyncHelpTarget.Builder builder = WorldHelpMessage.SyncHelpTarget.newBuilder();
            builder.setBossID(info.getBossId());
            builder.setName(owner.getName());
            MessageUtils.send_to_player(player, WorldHelpMessage.SyncHelpTarget.MsgID.eMsgID_VALUE,
                    builder.build().toByteArray());

            helpCallBack(player);
        }
    }

    /**
     * 获取上一次被支援的信息
     *
     * @param player
     */
    @Override
    public void onReqAtLastHelp(Player player) {
        WorldHelpMessage.ResAtLastHelp.Builder builder = WorldHelpMessage.ResAtLastHelp.newBuilder();
        WorldHelpInfo info = getLastInfo(player.getId());
        if (info == null) {
            TaskHelpInfo taskInfo = getLastTaskInfo(player.getId());
            if (taskInfo != null) {
                builder.setType(1);
                builder.setId(taskInfo.getId());
                builder.setBossID(taskInfo.getTaskId());
                builder.setMapId(0);
                for (long id : taskInfo.getHelpIds()) {
                    Player p = Manager.playerManager.getPlayer(id);
                    if (p == null)
                        continue;

                    builder.addPlayers(packPlayer(p));
                }
            } else {
                builder.setType(0);
                builder.setBossID(0);
                builder.setId(0);
                builder.setMapId(0);
                builder.addAllPlayers(new ArrayList<>());
            }
        } else {
            builder.setType(0);
            builder.setBossID(info.getBossId());
            builder.setId(info.getId());
            builder.setMapId(info.getMapId());
            for (long id : info.getHelpIds()) {
                Player p = Manager.playerManager.getPlayer(id);
                if (p == null)
                    continue;

                builder.addPlayers(packPlayer(p));
            }
        }
        MessageUtils.send_to_player(player, WorldHelpMessage.ResAtLastHelp.MsgID.eMsgID_VALUE,
                builder.build().toByteArray());
    }

    /**
     * thk
     *
     * @param player
     * @param id
     * @param words
     */
    @Override
    public void onReqThkHelp(Player player, int id, String words) {
        WorldHelpInfo info = Manager.worldHelpManager.getHistory().get(id);
        long ownerId = 0;
        List<Long> helpRoleIds = null;
        boolean isTask = false;
        if (info == null) {
            TaskHelpInfo taskInfo = Manager.worldHelpManager.getTaskHistory().get(id);
            if (taskInfo == null) {
                return;
            } else {
                ownerId = taskInfo.getOwnerId();
                helpRoleIds = taskInfo.getHelpIds();
                isTask = true;
            }
        } else {
            ownerId = info.getOwnerId();
            helpRoleIds = info.getHelpIds();
        }

        if (ownerId != player.getId())
            return;

        Cfg_World_Support_Bean wsB = cfg(player.getLevel());
        if (wsB == null)
            return;

        // 扣除道具
        if (!Manager.backpackManager.manager().canDeleteItemNum(player, wsB.getS_title_rank().get(0), 1))
            return;

        long action = IDConfigUtil.getLogId();

        Manager.backpackManager.manager().onRemoveItem(player, wsB.getS_title_rank().get(0), 1, ItemChangeReason.WorldHelpThkDec, action);
        if (isTask) {
            Manager.worldHelpManager.getTaskHistory().remove(id);
        } else
            Manager.worldHelpManager.getHistory().remove(id);

        reward(player, wsB.getS_title_item().get(0), wsB.getS_title_item().get(1), MessageString.WorldHelpMailThkOwnerTitle,
                String.valueOf(MessageString.WorldHelpMailThkOwnerContent), ItemChangeReason.WorldHelpThkAddGet, action);
        for (long pId : helpRoleIds) {
            Player p = Manager.playerManager.getPlayer(pId);
            if (p == null)
                continue;

            int add = Math.min(wsB.getS_title_fight().get(1), wsB.getS_title_fight().get(2) - count(p, wsB.getS_title_fight().get(0)));
            if (add > 0) {
                addCount(p, wsB.getS_title_fight().get(0), add);
                reward(p, wsB.getS_title_fight().get(0), wsB.getS_title_fight().get(1), MessageString.WorldHelpMailThkHelperTitle,
                        String.valueOf(MessageString.WorldHelpMailThkHelperContent), ItemChangeReason.WorldHelpThkAdd2Get, action);

                if (p.isOnline()) {
                    WorldHelpMessage.ResThkHelp.Builder builder = WorldHelpMessage.ResThkHelp.newBuilder();
                    builder.setPlayer(packPlayer(player));
                    builder.setWords(words);
                    MessageUtils.send_to_player(p, WorldHelpMessage.ResThkHelp.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                }
            }
        }
    }

    /**
     * 取消支援
     *
     * @param player
     */
    @Override
    public void onReqCancelHelp(Player player) {
        if (!Manager.worldHelpManager.getJoin().containsKey(player.getId()))
            return;

        if (Manager.worldHelpManager.getJoin().get(player.getId()).isInitiate())
            return;

        clear(player.getId());
        syncClearHelpTarget(player);
    }

    @Override
    public void onReqGuildTaskHelp(Player player) {
        if (player.isDie()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Die_CanNotOperate);
            return;
        }

        Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
        if (guild == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guildtaskhint1);
            return;
        }

        ConquerTask task = player.getCurConquerTasks().get(2);
        if (task == null || task.getModelId() <= 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_task_clone2);
            return;
        }
        Cfg_Task_conquer_Bean bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(task.getModelId());
        if (bean == null) {
            log.error("公会任务配置表中没有此任务(" + task.getModelId() + ")");
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, String.valueOf(task.getModelId()));
            return;
        }
        if (bean.getTask_grade() != 4) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_task_clone2);
            return;
        }
        if (task.isFinish()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GUILD_task_text2);
            return;
        }

        int cloneMapId = bean.getClonemap();
        if (cloneMapId <= 0) {
            log.error("公会任务配置表中副本没有配置,任务:" + task.getModelId());
            return;
        }

        if (Manager.worldHelpManager.getTaskJoin().containsKey(player.getId())) {
            if (Manager.worldHelpManager.getTaskJoin().get(player.getId()).isInitiate())
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpAlreadyInitiate);
            else
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpAlreadyJoin);
            return;
        }

        TeamInfo team = Manager.teamManager.deal().createTeam(player, cloneMapId, true);
        if (team == null) {
            team = TeamManager.getInstance().getTeam(player.getTeamId());
            if (team == null) {
                return;
            }
        }

        Cfg_World_Support_Bean wsB = cfg(player.getLevel());
        if (wsB == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, String.valueOf(player.getLevel()));
            return;
        }

        long now = TimeUtils.Time();
        if (now - player.getLastWorldHelpTime() < wsB.getCold_times() * 1000) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpCollDown);
            return;
        }

        Cfg_Clone_map_Bean cloneBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(cloneMapId);
        if (cloneBean == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, String.valueOf(cloneMapId));
            return;
        }
        Cfg_Mapsetting_Bean mB = CfgManager.getCfg_Mapsetting_Container().getValueByKey(cloneBean.getMapid());
        if (mB == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, String.valueOf(cloneBean.getMapid()));
            return;
        }
//		if (mB.getIf_World_Support() != 1)
//			return;

        // cool
        player.setLastWorldHelpTime(now);

        // yes
        int id = Manager.worldHelpManager.newID();
        TaskHelpInfo info = new TaskHelpInfo();
        info.setId(id);
        info.setCloneMapId(cloneMapId);
        info.setTaskId(task.getModelId());
        info.setTeamId(team.getTeamId());
        info.setOwnerId(player.getId());
        info.setTime(now);
        info.setHelpIds(new ArrayList<>());
        WorldHelp worldHelp = new WorldHelp(id, true);
        Manager.worldHelpManager.getTaskHelp().put(id, info);
        Manager.worldHelpManager.getTaskJoin().put(player.getId(), worldHelp);

        //检查队伍中是否存在可加入支援的队友
        for (Long roleId : team.getMembers()) {
            if (roleId != player.getId()) {
                joinTaskHelpInfo(player.getId(), roleId);
            }
        }

//		Manager.countManager.addVariant(player, VariantType.WorldHelpNum, 1);
//		Manager.controlManager.operate(player, FunctionVariable.WorldSupportSeek, 1);

        // 通知自己公会的玩家有新的支援请求
        MessageUtils.send_TO_Guild(guild, WorldHelpMessage.SyncWorldHelp.MsgID.eMsgID_VALUE,
                WorldHelpMessage.SyncWorldHelp.newBuilder().build().toByteArray());
    }

    /**
     * BOSS死亡
     *
     * @param mapObject
     * @param monster
     */
    @Override
    public void bossDie(MapObject mapObject, Monster monster) {
        if (!Manager.worldHelpManager.getBoss().containsKey(monster.getId()))
            return;

        List<Integer> list = new ArrayList<>(Manager.worldHelpManager.getBoss().get(monster.getId()));
        for (Integer integer : list) {
            bossDie(mapObject, monster, integer);
        }
        Manager.worldHelpManager.getBoss().remove(monster.getId());
    }

    /**
     * 清空仇恨目标
     *
     * @param code
     */
    @Override
    public void clearHatred(long code) {
        if (!Manager.worldHelpManager.getBoss().containsKey(code))
            return;

        List<Integer> list = new ArrayList<>(Manager.worldHelpManager.getBoss().get(code));
        for (int id : list) {
            removeInfo(id);
        }
        Manager.worldHelpManager.getBoss().remove(code);
    }

    /**
     * 移除仇恨目标
     *
     * @param code
     * @param tar
     */
    @Override
    public void removeHatred(long code, long tar) {
        if (!Manager.worldHelpManager.getBoss().containsKey(code))
            return;

        clear(tar);
    }

    /**
     * 玩家当前是否在仙盟BOSS支援的地图
     *
     * @param player
     * @return
     */
    @Override
    public boolean isInWorldHelpMap(Player player) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map.getType() != MapDefine.WORLD_MAP)
            return false;

        Cfg_Mapsetting_Bean mB = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (mB == null)
            return false;

        return mB.getIf_World_Support() == 1;
    }

    /**
     * 是否世界支援的玩家
     *
     * @param roleId
     * @return
     */
    @Override
    public boolean isHelp(long roleId) {
        if (!Manager.worldHelpManager.getJoin().containsKey(roleId))
            return false;
        return !Manager.worldHelpManager.getJoin().get(roleId).isInitiate();
    }

    /**
     * 是否世界支援的玩家
     *
     * @param roleId
     * @param bossCode
     * @return
     */
    @Override
    public boolean isHelpBoss(long roleId, long bossCode) {
        if (!isHelp(roleId))
            return false;

        WorldHelp help = Manager.worldHelpManager.getJoin().get(roleId);
        WorldHelpInfo info = Manager.worldHelpManager.getHelp().get(help.getId());
        if (info == null)
            return false;

        return info.getBossCode() == bossCode;
    }

    /**
     * 检查是否在支援状态，且在支援地图
     *
     * @param player
     * @return
     */
    @Override
    public boolean isHelpAndMap(Player player) {
        return isHelp(player.getId()) && isInWorldHelpMap(player);
    }

    /**
     * 检查是否在支援状态，且在支援地图
     *
     * @param player
     * @param bossCode
     * @return
     */
    @Override
    public boolean isHelpAndMapBoss(Player player, long bossCode) {
        return isHelpBoss(player.getId(), bossCode) && isInWorldHelpMap(player);
    }

    /**
     * 得到新的仇恨伤害列表
     *
     * @param monster
     * @return
     */
    @Override
    public List<SpecialHatred> getSpecialHatredRes(Monster monster) {
        List<SpecialHatred> list = new ArrayList<>();
        HashMap<Long, Long> helperDamage = new HashMap<>();

        boolean isWH = Manager.worldHelpManager.getBoss().containsKey(monster.getId());
        for (Hatred hatred : monster.getHatreds()) {
            if (hatred.getTarget() == null) {
                continue;
            }
            if (!(hatred.getTarget() instanceof Player)) {
                continue;
            }
            Player p = (Player) hatred.getTarget();
            if (!isWH) {
                list.add(new SpecialHatred(p, hatred.getHatred()));
                continue;
            }
            if (!isHelp(hatred.getTarget().getId())) {
                list.add(new SpecialHatred(p, hatred.getHatred()));
                continue;
            }
            // 是支援者
            WorldHelp owner = Manager.worldHelpManager.getJoin().get(hatred.getTarget().getId());
            WorldHelpInfo info = Manager.worldHelpManager.getHelp().get(owner.getId());
            if (info == null) {
                log.error("数据出错");
                list.add(new SpecialHatred(p, hatred.getHatred()));
                clear(hatred.getTarget().getId());
                continue;
            }

            long damage = helperDamage.getOrDefault(info.getOwnerId(), 0L);
            helperDamage.put(info.getOwnerId(), damage + hatred.getHatred());
        }

        if (!isWH || helperDamage.size() == 0)
            return list;

        for (SpecialHatred sh : list) {
            if (helperDamage.containsKey(sh.getPlayer().getId())) {
                sh.setDamage(sh.getDamage() + helperDamage.get(sh.getPlayer().getId()));
            }
        }

        Collections.sort(list, (v1, v2) -> {
            if (v2.getDamage() > v1.getDamage()) return 1;
            else return -1;
        });
        return list;
    }

    /**
     * 通知客户端BOSS伤害
     *
     * @param bossTyp
     * @param attacker
     * @param monster
     */
    @Override
    public void sendResSynHarmRank(int bossTyp, Player attacker, Monster monster) {
        if (Manager.worldHelpManager.getJoin().containsKey(attacker.getId())) {
            // 我在支援或者被支援状态，判断这个BOSS是不是我的支援或者被支援的BOSS
            WorldHelp wh = Manager.worldHelpManager.getJoin().get(attacker.getId());
            WorldHelpInfo whi = Manager.worldHelpManager.getHelp().get(wh.getId());
            if (whi != null && whi.getBossCode() != monster.getId())
                clear(attacker.getId());
        }
        if (monster.isCallBoss()) {
            return;
        }
        // 是否被支援的BOSS
        boolean isSpecBoss = Manager.worldHelpManager.getBoss().containsKey(monster.getId());
        if (!isSpecBoss)
            sendResSynHarmRankNormal(bossTyp, attacker, monster);
        else
            sendResSynHarmRankHelp(bossTyp, attacker, monster);
    }

    /**
     * 支援者的回调
     *
     * @param player
     */
    private void helpCallBack(Player player) {
        if (player == null) return;
        int typ = getDailyId(player);
        if (typ == DEFAULT_VALUE) {
            return;
        }

        // 是特殊地图，且没有次数，又进入了支援，则设置不同阵营
//        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, typ);
//        if (remainCount <= 0) {
//			player.setCamp(0, true);
//		}
    }

    /**
     * 支援者取消时的回调
     *
     * @param player
     */
    private void cancelCallBack(Player player) {
        if (player == null) return;

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        int typ = queryActivityType(map.getSetting().getIsscript());

        if (typ == DEFAULT_VALUE) {
            return;
        }
        if (typ == DailyActiveDefine.SUIT_BOSS.getValue()) {
            int camp = player.getCamp();
            int bossCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.SUIT_BOSS.getValue());
            int eliteCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, DailyActiveDefine.ELITE_BOSS.getValue());
            if (bossCount == 0) {
                camp = camp | 1;
                BossMessage.ResRankCountTips.Builder msg = BossMessage.ResRankCountTips.newBuilder();
                MessageUtils.send_to_player(player, BossMessage.ResRankCountTips.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
            if (eliteCount == 0) {
                camp = camp | 1 << 2;
            }
            player.setCamp(camp, true);
            return;
        }

        // 是特殊地图，且没有次数，又取消了支援，则设置同阵营
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, typ);
        if (remainCount == 0) {
            player.setCamp(map.getMapModelId(), true);
            BossMessage.ResRankCountTips.Builder msg = BossMessage.ResRankCountTips.newBuilder();
            MessageUtils.send_to_player(player, BossMessage.ResRankCountTips.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private int queryActivityType(int scriptId) {
        if (rewardLimitMap.size() == 0) {
            rewardLimitMap.put(ScriptEnum.WorldBossActivityScript, DailyActiveDefine.WORLD_BOSS.getValue());
//			rewardLimitMap.put(ScriptEnum.BossHomeActivityScript, DailyActiveDefine.ACTIVITY_BOSS_HOME);
            rewardLimitMap.put(ScriptEnum.SoulAnimalForestCrossCloneCrossActivityScript, DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS.getValue());
            rewardLimitMap.put(ScriptEnum.SoulAnimalForestLocalCloneActivityScript, DailyActiveDefine.ACTIVITY_LOCAl_SOULANIMALISLAND.getValue());
            rewardLimitMap.put(ScriptEnum.SuitBossScript, DailyActiveDefine.SUIT_BOSS.getValue());
        }
        return rewardLimitMap.getOrDefault(scriptId, DEFAULT_VALUE);
    }

    private WorldHelpInfo getLastInfo(long roleID) {
        WorldHelpInfo info = null;
        for (Map.Entry<Integer, WorldHelpInfo> entry : Manager.worldHelpManager.getHistory().entrySet()) {
            if (entry.getValue().getOwnerId() != roleID)
                continue;

            if (info == null)
                info = entry.getValue();

            if (info.getTime() < entry.getValue().getTime())
                info = entry.getValue();
        }
        return info;
    }

    private TaskHelpInfo getLastTaskInfo(long roleID) {
        TaskHelpInfo info = null;
        //遍历任务支援历史记录
        for (Map.Entry<Integer, TaskHelpInfo> entry : Manager.worldHelpManager.getTaskHistory().entrySet()) {
            if (entry.getValue().getOwnerId() != roleID)
                continue;

            if (info == null)
                info = entry.getValue();

            if (info.getTime() < entry.getValue().getTime())
                info = entry.getValue();
        }
        return info;
    }

    private Cfg_World_Support_Bean cfg(int lvl) {
        for (Cfg_World_Support_Bean bean : CfgManager.getCfg_World_Support_Container().getValuees()) {
            if (bean.getLevel_rank().size() == 2
                    && bean.getLevel_rank().get(0) <= lvl && lvl <= bean.getLevel_rank().get(1))
                return bean;
        }
        return null;
    }

    private WorldHelpMessage.WorldHelp.Builder packWorldHelp(WorldHelpInfo info) {
        WorldHelpMessage.WorldHelp.Builder builder = WorldHelpMessage.WorldHelp.newBuilder();
        builder.setId(info.getId());
        builder.setBossID(info.getBossId());
        builder.setMapId(info.getMapId());
        builder.setHelpNum(info.getHelpIds().size());
        builder.setPlayer(packPlayer(Manager.playerManager.getPlayer(info.getOwnerId())));
        builder.setReqTime(info.getTime());
        return builder;
    }

    private WorldHelpMessage.GuildTaskHelp.Builder packTaskHelp(TaskHelpInfo info) {
        WorldHelpMessage.GuildTaskHelp.Builder builder = WorldHelpMessage.GuildTaskHelp.newBuilder();
        builder.setId(info.getId());
        builder.setTaskId(info.getTaskId());
        builder.setTeamId(info.getTeamId());
        builder.setHelpNum(info.getHelpIds().size());
        builder.setPlayer(packPlayer(Manager.playerManager.getPlayer(info.getOwnerId())));
        return builder;
    }

    private WorldHelpMessage.Player.Builder packPlayer(Player player) {
        WorldHelpMessage.Player.Builder builder = WorldHelpMessage.Player.newBuilder();
        if (player != null) {
            builder.setRoleID(player.getId());
            builder.setName(player.getName());
            builder.setLevel(player.getLevel());
            builder.setCareer(player.getCareer());
            builder.setHead(MapUtils.getHead(player));
        } else {
            builder.setRoleID(0);
            builder.setName("");
            builder.setLevel(0);
            builder.setCareer(0);
            builder.setHead(MapUtils.getHead(0, 0, "", false));
//            builder.setHeadId(0);
//            builder.setHeadFrameId(0);
        }
        return builder;
    }

    /**
     * 是否支援目标
     *
     * @param roleId  支援者
     * @param ownerId 被支援者
     * @return
     */
    private boolean isHelp(long roleId, long ownerId) {
        if (ownerId == 0)
            return false;

        if (roleId == ownerId)
            return false;

        WorldHelp owner = Manager.worldHelpManager.getJoin().get(ownerId);
        if (owner == null)
            return false;

        WorldHelp role = Manager.worldHelpManager.getJoin().get(roleId);
        if (role == null)
            return false;
        return owner.getId() == role.getId();
    }

    @Override
    public boolean canClear(Entity entity) {
        if (entity instanceof Monster) {
            return true;
        }
        if (!(entity instanceof Player)) {
            return false;
        }

        WorldHelp help = Manager.worldHelpManager.getJoin().get(entity.getId());
        if (help == null) {
            return true;
        }

        WorldHelpInfo whi = Manager.worldHelpManager.getHelp().get(help.getId());
        if (whi == null) {
            return true;
        }

        List<Long> helpIds = whi.getHelpIds();
        if (helpIds.size() > 0) {
            return false;
        }

        return true;
    }

    private void clear(long roleId) {
        if (!Manager.worldHelpManager.getJoin().containsKey(roleId))
            return;

        WorldHelp help = Manager.worldHelpManager.getJoin().get(roleId);
        if (!help.isInitiate()) {
            // help
            removeHelp(help, roleId);
        } else
            // call
            removeInfo(help.getId());
    }

    @Override
    public void clearTask(long roleId) {
        if (!Manager.worldHelpManager.getTaskJoin().containsKey(roleId))
            return;

        WorldHelp help = Manager.worldHelpManager.getTaskJoin().get(roleId);
        if (help.isInitiate()) {//是发起者就清理支援相关所有人
            removeTaskInfo(Manager.worldHelpManager.getTaskHelp().get(help.getId()));
        } else
            removeTaskHelp(help, roleId);
    }

    private boolean valid(WorldHelpInfo info) {
        if (info == null)
            return false;

        Player p = Manager.playerManager.getPlayer(info.getOwnerId());
        if (p == null)
            return false;

        Cfg_World_Support_Bean bean = cfg(p.getLevel());
        if (bean == null)
            return false;

        return info.getHelpIds().size() < bean.getMax_times();
    }

    private boolean valid(TaskHelpInfo info) {
        if (info == null)
            return false;

        Player p = Manager.playerManager.getPlayer(info.getOwnerId());
        if (p == null)
            return false;

        Cfg_World_Support_Bean bean = cfg(p.getLevel());
        if (bean == null)
            return false;

        return info.getHelpIds().size() < bean.getMax_times();
    }

    private void reward(long id, int itemId, int num, int mailTitle, String mailContext, int reason, long action) {
        Player player = Manager.playerManager.getPlayer(id);
        if (player != null)
            reward(player, itemId, num, mailTitle, mailContext, reason, action);
    }

    private void reward(Player player, int itemId, int num, int mailTitle, String mailContext, int reason, long action) {
        List<Item> items = Item.createItems(itemId, num, true);
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) == 0) {
            Manager.backpackManager.manager().addItems(player, items, reason, action);
        } else {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail, MessageString.System,
                    mailTitle, mailContext, items, reason, action);
        }
    }

    private void bossDie(MapObject mapObject, Monster monster, int id) {
        if (!Manager.worldHelpManager.getHelp().containsKey(id))
            return;

        WorldHelpInfo info = Manager.worldHelpManager.getHelp().get(id);
        if (info.getBossCode() != monster.getId()) {
            log.error("数据不一致错误,boss map error");
            return;
        }

        // 没人支援就不发
        if (info.getHelpIds().size() == 0) {
            removeInfo(info);
            return;
        }

        Player owner = Manager.playerManager.getPlayer(info.getOwnerId());
        if (owner == null) {
            removeInfo(info);
            return;
        }

        Cfg_World_Support_Bean bean = cfg(owner.getLevel());
        if (bean == null) {
            removeInfo(info);
            return;
        }

        long action = IDConfigUtil.getLogId();

        boolean haveZY = false;
        if (bean.getPic_res().size() == 3)
            for (long pId : info.getHelpIds()) {
                Player p = Manager.playerManager.getPlayer(pId);
                if (p == null)
                    continue;

                boolean isDamage = false;
                for (Hatred hatred : monster.getHatreds()) {
                    if (hatred.getTarget().getId() == pId) {
                        isDamage = true;
                        haveZY = true;
                        break;
                    }
                }

                if (!isDamage) continue;

                int add = Math.min(bean.getPic_res().get(1), bean.getPic_res().get(2) - count(p, bean.getPic_res().get(0)));
                if (add > 0) {
                    addCount(p, bean.getPic_res().get(0), add);
                    reward(pId, bean.getPic_res().get(0), add, MessageString.WorldHelpMailBossDieHelperTitle,
                            String.valueOf(MessageString.WorldHelpMailBossDieHelperContent), ItemChangeReason.WorldHelpBossDieAdd2Get, action);

                    WorldHelpMessage.SyncPrestige.Builder builder = WorldHelpMessage.SyncPrestige.newBuilder();
                    builder.setPrestige(count(p, bean.getPic_res().get(0)));
                    MessageUtils.send_to_player(p, WorldHelpMessage.SyncPrestige.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                }
                Manager.countManager.addVariant(p, VariantType.WorldBeHelpNum, 1);
                Manager.controlManager.operate(p, FunctionVariable.WorldSupportHelp, 1);
            }

        if (haveZY && count(owner, bean.getS_title_rank().get(0)) < bean.getS_title_rank().get(1)) {
            addCount(owner, bean.getS_title_rank().get(0), 1);
            reward(owner, bean.getS_title_rank().get(0), 1, MessageString.WorldHelpMailBossDieOwnerTitle,
                    String.valueOf(MessageString.WorldHelpMailBossDieOwnerContent), ItemChangeReason.WorldHelpBossDieAddGet, action);
        }

        removeInfo(info);

        if (haveZY)
            Manager.worldHelpManager.getHistory().put(id, info);
    }

    private void removeHelp(WorldHelp help, long roleId) {
        Manager.worldHelpManager.getJoin().remove(roleId);
        Player player = Manager.playerManager.getPlayer(roleId);
        cancelCallBack(player);
        syncClearHelpTarget(roleId);

        if (help == null)
            return;

        WorldHelpInfo info = Manager.worldHelpManager.getHelp().get(help.getId());
        if (info != null) {
            info.getHelpIds().remove(roleId);
        }
    }

    //修复取消支援状态，怪物仇恨未清除的bug
    private void clearMonsterHatred(WorldHelpInfo info) {
        MapObject map = MapManager.getInstance().getMap(info.getMapUid());
        if (map != null) {
            for (Monster monster : map.getMonsters().values()) {
                if (monster.getModelId() == info.getBossId()) {
                    Iterator<Hatred> iterator = monster.getHatreds().iterator();
                    while (iterator.hasNext()) {
                        Hatred next = iterator.next();
                        if (info.getHelpIds().contains(next.getTarget().getId())) {
                            iterator.remove();
                        }
                    }
                }
            }
        }
    }

    private void removeInfo(int id) {
        removeInfo(Manager.worldHelpManager.getHelp().get(id));
    }

    private void removeInfo(WorldHelpInfo info) {
        if (info == null)
            return;

        Manager.worldHelpManager.getHelp().remove(info.getId());
        Manager.worldHelpManager.getJoin().remove(info.getOwnerId());
        //修复支援状态取消，仇恨未清除的问题
        clearMonsterHatred(info);
        for (long id : info.getHelpIds()) {
            Manager.worldHelpManager.getJoin().remove(id);
            cancelCallBack(Manager.playerManager.getPlayer(id));
            syncClearHelpTarget(id);
        }

        List<Integer> list = Manager.worldHelpManager.getBoss().get(info.getBossCode());
        if (list != null)
            list.remove((Object) info.getId());
    }

    private void removeTaskHelp(WorldHelp help, long roleId) {
        Manager.worldHelpManager.getTaskJoin().remove(roleId);
        if (help == null)
            return;

        TaskHelpInfo info = Manager.worldHelpManager.getTaskHelp().get(help.getId());
        if (info != null) {
            info.getHelpIds().remove(roleId);
        }
    }

    private void removeTaskInfo(TaskHelpInfo info) {
        if (info == null)
            return;

        Manager.worldHelpManager.getTaskHelp().remove(info.getId());
        Manager.worldHelpManager.getTaskJoin().remove(info.getOwnerId());
        for (long id : info.getHelpIds()) {
            Manager.worldHelpManager.getTaskJoin().remove(id);
        }
    }

    private void syncClearHelpTarget(long id) {
        syncClearHelpTarget(Manager.playerManager.getPlayer(id));
    }

    private void syncClearHelpTarget(Player player) {
        if (player == null)
            return;

        WorldHelpMessage.SyncHelpTarget.Builder builder = WorldHelpMessage.SyncHelpTarget.newBuilder();
        builder.setBossID(0);
        builder.setName("");
        MessageUtils.send_to_player(player, WorldHelpMessage.SyncHelpTarget.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void sendResSynHarmRankHelp(int bossTyp, Player attacker, Monster monster) {
        List<SpecialHatred> list = getSpecialHatredRes(monster);

        // 攻击者支援的目标ID
        long targetId = 0;
        // 攻击者
        BossMessage.harmRank.Builder self = null;
        // 被支援者
        BossMessage.harmRank.Builder owner = null;

        WorldHelp wh = Manager.worldHelpManager.getJoin().get(attacker.getId());
        WorldHelpInfo whi = null;
        if (wh != null) {
            whi = Manager.worldHelpManager.getHelp().get(wh.getId());
            if (whi != null)
                targetId = whi.getOwnerId();
        }

        BossMessage.ResSynHarmRank.Builder msg = BossMessage.ResSynHarmRank.newBuilder();

        // 找出自己和支援者
        int rank = 0;
        for (Hatred hatred : monster.getHatreds()) {
            if (!(hatred.getTarget() instanceof Player))
                continue;

            if (isHelp(hatred.getTarget().getId()) && !isHelp(hatred.getTarget().getId(), targetId))
                continue;

            if (hatred.getHatred() <= 0)
                continue;
            ++rank;
            BossMessage.harmRank.Builder harmMsg = null;
            if (hatred.getTarget().getId() == attacker.getId()) {
                harmMsg = BossMessage.harmRank.newBuilder();
                harmMsg.setName(hatred.getTarget().getName());
                harmMsg.setHarm(hatred.getHatred());
                harmMsg.setTop(rank);
                self = harmMsg;
            }

            if (isHelp(hatred.getTarget().getId(), targetId)) {
                if (harmMsg == null) {
                    harmMsg = BossMessage.harmRank.newBuilder();
                    harmMsg.setName(hatred.getTarget().getName());
                    harmMsg.setHarm(hatred.getHatred());
                    harmMsg.setTop(rank);
                }
                msg.addUpportHarmInfo(harmMsg);
            }
        }

        if (self == null) return;

        rank = 0;
        for (SpecialHatred sh : list) {
            BossMessage.harmRank.Builder harmMsg = BossMessage.harmRank.newBuilder();
            harmMsg.setName(sh.getPlayer().getName());
            harmMsg.setHarm(sh.getDamage());
            harmMsg.setTop(++rank);
            msg.addRank(harmMsg);

            if (targetId != 0 && sh.getPlayer().getId() == targetId)
                owner = harmMsg;
        }

        if (owner != null)
            msg.setMyRank(owner.getTop());
        else
            msg.setMyRank(self.getTop());
        msg.setMyHarm(self.getHarm());
        msg.setBossType(bossTyp);
        msg.setBossCode(monster.getId());
        MessageUtils.send_to_player(attacker, BossMessage.ResSynHarmRank.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private void sendResSynHarmRankNormal(int bossTyp, Player attacker, Monster monster) {

        BossMessage.ResSynHarmRank.Builder msg = BossMessage.ResSynHarmRank.newBuilder();
        msg.setBossType(bossTyp);
        msg.setBossCode(monster.getId());

        int rank = 0;
        for (Hatred hatred : monster.getHatreds()) {
            if (!(hatred.getTarget() instanceof Player)) {
                continue;
            }
            if (hatred.getHatred() <= 0) {
                continue;
            }
            BossMessage.harmRank.Builder harmMsg = BossMessage.harmRank.newBuilder();
            harmMsg.setName(hatred.getTarget().getName());
            harmMsg.setHarm(hatred.getHatred());
            harmMsg.setTop(++rank);
            msg.addRank(harmMsg);

        }
        rank = 0;
        for (Hatred hatred : monster.getHatreds()) {
            if (!(hatred.getTarget() instanceof Player)) {
                continue;
            }
            if (hatred.getHatred() <= 0) {
                continue;
            }
            msg.setMyRank(++rank);
            msg.setMyHarm(hatred.getHatred());
            MessageUtils.send_to_player(hatred.getTarget().getId(), BossMessage.ResSynHarmRank.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    private int count(Player player, long key) {
        return (int) Manager.countManager.getCount(player, BaseCountType.WorldHelp, key);
    }

    private void addCount(Player player, long key, long count) {
        Manager.countManager.addCount(player, BaseCountType.WorldHelp, key, Count.RefreshType.CountType_Day, count);
    }

    @Override
    public void joinTaskHelpInfo(long leaderId, long roleId) {
        if (!Manager.worldHelpManager.getTaskJoin().containsKey(leaderId)) {
            return;
        }

        WorldHelp worldHelp = Manager.worldHelpManager.getTaskJoin().get(leaderId);
        if (!Manager.worldHelpManager.getTaskHelp().containsKey(worldHelp.getId())) {
            return;
        }
        TaskHelpInfo info = Manager.worldHelpManager.getTaskHelp().get(worldHelp.getId());
        info.getHelpIds().add(roleId);
        Manager.worldHelpManager.getTaskJoin().put(roleId, new WorldHelp(info.getId(), false));
    }

    @Override
    public void changeTeamLeader(Player player, long helperId, long teamId) {
        Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
        if (guild == null) {
//			MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guildtaskhint1);
            return;
        }

        ConquerTask task = player.getCurConquerTasks().get(2);
        if (task == null || task.getModelId() <= 0) {
//			MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_task_clone2);
            return;
        }

        Cfg_Task_conquer_Bean bean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(task.getModelId());
        if (bean == null) {
            log.error("公会任务配置表中没有此任务(" + task.getModelId() + ")");
            return;
        }
        if (bean.getTask_grade() != 4) {
            return;
        }
        if (task.isFinish()) {
//			MessageUtils.notify_player(player, Notify.ERROR, MessageString.GUILD_task_text2);
            return;
        }

        int cloneMapId = bean.getClonemap();
        if (cloneMapId <= 0) {
            log.error("公会任务配置表中副本没有配置,任务:" + task.getModelId());
            return;
        }

        if (Manager.worldHelpManager.getTaskJoin().containsKey(player.getId())) {
            if (Manager.worldHelpManager.getTaskJoin().get(player.getId()).isInitiate()) {
//				MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpAlreadyInitiate);
            } else {
//				MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpAlreadyJoin);
            }
            return;
        }

        Cfg_World_Support_Bean wsB = cfg(player.getLevel());
        if (wsB == null) {
//			MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey,String.valueOf(player.getLevel()));
            return;
        }

        long now = TimeUtils.Time();
        if (now - player.getLastWorldHelpTime() < wsB.getCold_times() * 1000) {
//			MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpCollDown);
            return;
        }

        Cfg_Clone_map_Bean cloneBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(cloneMapId);
        if (cloneBean == null) {
//			MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, String.valueOf(cloneMapId));
            return;
        }
        Cfg_Mapsetting_Bean mB = CfgManager.getCfg_Mapsetting_Container().getValueByKey(cloneBean.getMapid());
        if (mB == null) {
//			MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, String.valueOf(cloneBean.getMapid()));
            return;
        }
//		if (mB.getIf_World_Support() != 1)
//			return;

        // cool
        player.setLastWorldHelpTime(now);

        // yes
        int id = Manager.worldHelpManager.newID();
        TaskHelpInfo info = new TaskHelpInfo();
        info.setId(id);
        info.setCloneMapId(cloneMapId);
        info.setTaskId(task.getModelId());
        info.setTeamId(teamId);
        info.setOwnerId(player.getId());
        info.setTime(now);
        info.setHelpIds(new ArrayList<>());
        WorldHelp worldHelp = new WorldHelp(id, true);
        Manager.worldHelpManager.getTaskHelp().put(id, info);
        Manager.worldHelpManager.getTaskJoin().put(player.getId(), worldHelp);

//		Manager.countManager.addVariant(player, VariantType.WorldHelpNum, 1);
//		Manager.controlManager.operate(player, FunctionVariable.WorldSupportSeek, 1);

        // 通知自己公会的玩家有新的支援请求
//		MessageUtils.send_TO_Guild(guild, WorldHelpMessage.SyncWorldHelp.MsgID.eMsgID_VALUE,
//				WorldHelpMessage.SyncWorldHelp.newBuilder().build().toByteArray());

        Manager.worldHelpManager.getScript().joinTaskHelpInfo(player.getId(), helperId);
    }

    @Override
    public void sendTaskHelpReward(Player player, MapObject mapObject, int helpId, boolean isHelp) {
        if (!Manager.worldHelpManager.getTaskHelp().containsKey(helpId))
            return;

        TaskHelpInfo info = Manager.worldHelpManager.getTaskHelp().get(helpId);
        if (mapObject.getZoneModelId() != info.getCloneMapId()) {
            return;
        }

        // 没人支援就不发
        if (info.getHelpIds().size() == 0) {
            removeTaskInfo(info);
            return;
        }

        Player owner = Manager.playerManager.getPlayer(info.getOwnerId());
        if (owner == null) {
            removeTaskInfo(info);
            return;
        }

        Cfg_World_Support_Bean bean = cfg(owner.getLevel());
        if (bean == null) {
            removeTaskInfo(info);
            return;
        }

        long action = IDConfigUtil.getLogId();

        if (bean.getPic_res().size() == 3)
            for (long pId : info.getHelpIds()) {
//				Player p = mapObject.getPlayer(pId);
                Player p = Manager.playerManager.getPlayer(pId);
                if (p == null)
                    continue;

                int add = Math.min(bean.getPic_res().get(1), bean.getPic_res().get(2) - count(p, bean.getPic_res().get(0)));
                if (add > 0) {
                    addCount(p, bean.getPic_res().get(0), add);
                    reward(pId, bean.getPic_res().get(0), add, MessageString.WorldHelpMailBossDieHelperTitle,
                            String.valueOf(MessageString.WorldHelpMailBossDieHelperContent), ItemChangeReason.WorldHelpBossDieAdd2Get, action);

                    WorldHelpMessage.SyncPrestige.Builder builder = WorldHelpMessage.SyncPrestige.newBuilder();
                    builder.setPrestige(count(p, bean.getPic_res().get(0)));
                    MessageUtils.send_to_player(p, WorldHelpMessage.SyncPrestige.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                }
                Manager.countManager.addVariant(p, VariantType.WorldBeHelpNum, 1);
                Manager.controlManager.operate(p, FunctionVariable.WorldSupportHelp, 1);
            }

        if (isHelp && count(owner, bean.getS_title_rank().get(0)) < bean.getS_title_rank().get(1)) {
            addCount(owner, bean.getS_title_rank().get(0), 1);
            reward(owner, bean.getS_title_rank().get(0), 1, MessageString.WorldHelpMailBossDieOwnerTitle,
                    String.valueOf(MessageString.WorldHelpMailBossDieOwnerContent), ItemChangeReason.WorldHelpBossDieAddGet, action);
        }

        removeTaskInfo(info);

        if (isHelp)
            Manager.worldHelpManager.getTaskHistory().put(helpId, info);
    }

    private int getDailyId(Player player) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        return queryActivityType(map.getSetting().getIsscript());
    }

    @Override
    public void checkWorldHelpCamp(Monster monster, Fighter defer) {
        if (defer.getCamp() == 0 || (defer.getCamp() != 0 && !(defer.getCamp() == (monster.getCamp() | 1 << 2) || defer.getCamp() == monster.getCamp()))) {
            return;
        }
        Player p = (Player) defer;
        int dailyId = getDailyId(p);
        if (dailyId == -1) {
            return;
        }

        //是特殊地图，且没有次数，又进入了支援，则设置不同阵营
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(p, dailyId);
        if (remainCount == 0) {
            //检查玩家是否在支援当前boss
            if (!Manager.worldHelpManager.getScript().isHelpBoss(p.getId(), monster.getId())) {
                return;
            }

            float hartDis = Utils.getDistance(monster.getInitPos(), defer.gainCurPos());
            if (hartDis < 1000f / 100f) {//支援玩家可攻击怪物距离写死（策划要求）
                p.setCamp(0, true);
            }
        }
    }

    /**
     * 死亡求援
     *
     * @param player
     */
    @Override
    public void reqDieCallHelp(Player player) {

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            return;
        }
        if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.DIE_CALL_HELP_CD, null)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WorldHelpCollDown);
            return;
        }

        Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
        if (guild == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guildtaskhint1);
            return;
        }
        Cfg_Relive_Bean bean = CfgManager.getCfg_Relive_Container().getValueByKey(map.getSetting().getRelive_type());
        if (bean == null || bean.getIsSeekHelp() != 1) {
            return;
        }
        Map.Entry<Integer, ConcurrentHashMap<Integer, Long>> daily = Utils.findOne(DailyActiveManager.dailyMap.entrySet(), e -> e.getValue().containsKey(map.getZoneModelId()));
        if (daily == null) {
            return;
        }
        int max = Math.max(bean.getSafe_relive_time(), bean.getSitu_relive_time());
        max = Math.max(max, bean.getAuto_relive_time());
        Manager.cooldownManager.addCooldown(player, CooldownTypes.DIE_CALL_HELP_CD, null, max);

        WorldHelpMessage.ResDieCallHelp.Builder message = WorldHelpMessage.ResDieCallHelp.newBuilder();
        message.setPlayer(packPlayer(player));
        message.setCloneId(map.getZoneModelId());
        message.setDailyId(daily.getKey());
        message.setX(player.gainCurPos().getX());
        message.setY(player.gainCurPos().getY());

        for (long id : guild.getMembers().keySet()) {
            Player pp = Manager.playerManager.getPlayerOnline(id);
            if (pp == null || player.getId() == pp.getId()) {
                continue;
            }
            MessageUtils.send_to_player(pp, WorldHelpMessage.ResDieCallHelp.MsgID.eMsgID_VALUE, message.build().toByteArray());
        }
        log.info("死亡求援 player={}", player);
    }
}
