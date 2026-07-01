package common.copyMap.base;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.game.chat.structs.Notify;
import com.game.copymap.log.CopyMapEnterLog;
import com.game.copymap.scripts.ICopyManagerScript;
import com.game.copymap.structs.CopyMapTeam;
import com.game.copymap.structs.CopyMapType;
import com.game.copymap.structs.PlaneMapData;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.EntityState;
import com.game.task.structs.ConquerTask;
import com.game.team.structs.TeamInfo;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import game.core.dblog.LogService;
import game.core.map.Position;
import game.core.net.Config.ServerConfig;
import game.core.util.CrossState;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import game.message.CrossServerMessage;
import game.message.ZoneMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 副本基本的管理类接口处理
 *
 * @author lw
 */
public class CopyMapScriptManager implements ICopyManagerScript {

    private static final Logger LOG = LogManager.getLogger(CopyMapScriptManager.class);

    private static final int waitAcceptTime = 30 * 1000;
    private static final int waitReadyTime = 30 * 1000;


    @Override
    public int getId() {
        return ScriptEnum.CopyMapManagerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean onReqCopyMapEnter(Player player, int modelId, int param) {

        LOG.error("请求进入副本 modelId={} param={} player={}", modelId, param, player);


        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(modelId);
        if (null == bean) {
            LOG.error("进入副本失败 null modelId={} param={} player={}", modelId, param, player);
            return false;
        }

        if (bean.getType() == CopyMapType.GuildTask_CopyMap) {//处理任务支援副本
            long guildId = player.getGuildId();
            if (guildId <= 0) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guildtaskhint1);
                return false;
            }
            //检查队长是否接受该任务
            ConquerTask task = player.getCurConquerTasks().get(2);
            if (task == null || task.getModelId() <= 0) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_task_clone2);
                return false;
            }

            Cfg_Task_conquer_Bean taskBean = CfgManager.getCfg_Task_conquer_Container().getValueByKey(task.getModelId());
            if (taskBean == null) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ConfigError_GetByKey, String.valueOf(task.getModelId()));
                return false;
            }
            if (taskBean.getTask_grade() != 4) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_task_clone2);
                return false;
            }

            if (task.isFinish()) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.GUILD_task_text1);
                return false;
            }

            TeamInfo teamInfo = Manager.teamManager.getTeam(player.getTeamId());
            if (teamInfo != null) {
                //检查队员是否是同一公会
                for (Long roleId : teamInfo.getMembers()) {
                    if (roleId == player.getId()) {
                        continue;
                    }
                    Player member = Manager.playerManager.getPlayerOnline(roleId);
                    if (member == null) {
                        return false;
                    }
                    if (member.getGuildId() <= 0) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_task_clone1);
                        return false;
                    }
                    if (member.getGuildId() != guildId) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.Guild_task_clone1);
                        return false;
                    }
                }
            }
        }
        boolean isEnterMap = false;
        if (bean.getEnter_type() == CopyMapType.CopyMapLocal) {
            isEnterMap = enterLocalCopyMap(player, bean, param);
        } else if (bean.getEnter_type() == CopyMapType.CopyMapCross) {
            isEnterMap = enterCrossCopyMap(player, bean, param);
        } else {
            LOG.error(player + " 进行副本id=" + modelId + " , enter_type =" + bean.getEnter_type() + "错误！");
        }
        return isEnterMap;
    }

    @Override
    public void onReqCopyMapOut(Player player) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            LOG.error("玩家（" + player.getName() + "）" + player.getId() + "当前地图为空，主动离开了副本!");
            player.setHideMe(false);
            player.setHideOther(false);

            if (GameServer.getInstance().IsFightServer()) {
//                manager.mapManager.onQuitMap(map, player, true);
                Manager.mapManager.manager().onCrossOutMap(player);
                //设置下线
                player.dealOffLine();
                Manager.crossServerManager.sendToPublicPlayerOutCrossFight(player, player.playerCrossData.toFightId, player.playerCrossData.toZoneModelId);
                CrossServerMessage.F2GCloneClose.Builder msg = CrossServerMessage.F2GCloneClose.newBuilder();
                msg.setFightId(player.playerCrossData.toFightId);
                msg.setModelId(player.playerCrossData.toZoneModelId);
                msg.addRoleIds(player.getId());
                FightClientManager.GetInstance().send_to_game(player.getIosession(), CrossServerMessage.F2GCloneClose.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            } else {
                Manager.copyMapManager.outZone(player);
            }
            return;
        }

        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());

        //如果是战斗服收到主动退出
        if (GameServer.getInstance().IsFightServer()) {
            LOG.error("玩家（" + player.getName() + "）" + player.getId() + "在跨服副本中离开了副本!");
            Manager.mapManager.manager().onCrossOutMap(player);
            Manager.crossServerManager.sendToPublicPlayerOutCrossFight(player, player.playerCrossData.toFightId, player.playerCrossData.toZoneModelId);

            if (mapObject != null) {
                Manager.crossServerManager.NoticeCrossCloneOutToPlayer(player, mapObject);
            } else {
                //设置下线
                player.dealOffLine();
                CrossServerMessage.F2GCloneClose.Builder msg = CrossServerMessage.F2GCloneClose.newBuilder();
                msg.setFightId(player.playerCrossData.toFightId);
                msg.setModelId(player.playerCrossData.toZoneModelId);
                msg.addRoleIds(player.getId());
                FightClientManager.GetInstance().send_to_game(player.getIosession(), CrossServerMessage.F2GCloneClose.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                //退出离开
//                player.setIosession(null);
                //设置下线
                player.setIsOnline((byte) 0);
            }
            return;
        }

        LOG.info("玩家（" + player.getName() + "）" + player.getId() + "主动离开了副本! 发送了副本ID为： " + map.getZoneModelId() + " mapid :" + map.getName() + "(" + map.getId() + "/" + map.getMapModelId() + ")");
        Manager.copyMapManager.outZone(player);
    }

    @Override
    public boolean copyMapCheck(Player player, Cfg_Clone_map_Bean bean, int level, Player leader) {

        Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(bean.getMapid());
        if (mapBean == null || mapBean.getIsscript() <= 0) {
            return false;
        }
        if (!Manager.mapManager.base(mapBean.getIsscript()).canEnterMap(player, bean.getId(), level)) {
            return false;
        }

        boolean isSelf = player.getId() == leader.getId();
        //TODO 如果玩家死亡
        if (player.isDie()) {
            if (isSelf) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ZONE_ENTERMAP_PLAYERDIE, player.getName());
            } else {
                MessageUtils.notify_player(leader, Notify.SHOWBOX, MessageString.ZONE_ENTERMAP_PLAYERDIE, player.getName());
            }
            return false;
        }

        //TODO 跨服中
        if (player.playerCrossData.isToFightServer()) {
            if (isSelf) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.EXISTINCLONE);
            } else {
                MessageUtils.notify_player(leader, Notify.SHOWBOX, MessageString.TEAMENTERCLONEERRORCROSS, player.getName());
            }
            return false;
        }

        //TODO 如果进入副本已经在等待状态则不能进入其它副本
        if (((player.playerCrossData.crossState >= CrossState.PCS_REQUIRE)) || player.playerCrossData.isCrossReqFight()) {
            if (isSelf) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.CROSSFIGHT_ISWAIT);
            } else {
                MessageUtils.notify_player(leader, Notify.SHOWBOX, MessageString.TEAMENTERCLONEERRORCROSSWAIT, player.getName());
            }
            return false;
        }

        //TODO 任务开启条件
        if (bean.getNeedTaskId().size() != 0) {
            boolean isTask = true;
            for (int taskId : bean.getNeedTaskId().getValue()) {
                if (player.overMainTask(taskId)) {
                    isTask = false;
                    break;
                }
            }

            if (isTask) {
                if (isSelf) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CLONE_OPEN_NEED_TASKID, Manager.taskManager.deal().getMainTaskChatName(bean.getNeedTaskId().get(0)));// + " 副本未开放！");
                } else {
                    MessageUtils.notify_player(leader, Notify.SHOWBOX, MessageString.TEAMENTERCLONEERRORTASK, player.getName(), Manager.taskManager.deal().getMainTaskChatName(bean.getNeedTaskId().get(0)));
                }
                return false;
            }
        }

        Cfg_Clone_level_Bean clone_level_bean = CfgManager.getCfg_Clone_level_Container().getValueByKey(bean.getId() * 100 + level);

        int maxLevel = clone_level_bean == null ? bean.getMax_lv() : clone_level_bean.getMax_lv();
        int minLevel = clone_level_bean == null ? bean.getMin_lv() : clone_level_bean.getMin_lv();

        //TODO 检查等级
        if (minLevel > player.getLevel() || (maxLevel > 0 && player.getLevel() > maxLevel)) {
            if (isSelf) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.CLONEENTERNEEDLEVEL, minLevel);
            } else {
                if (player.getLevel() > leader.getLevel()) {
                    MessageUtils.notify_player(leader, Notify.SHOWBOX, MessageString.ENTERZONELEVELLOW, player.getName());
                } else {
                    MessageUtils.notify_player(leader, Notify.SHOWBOX, MessageString.TEAMENTERCLONEERRORLEVEL, player.getName(), minLevel);
                }
            }
            return false;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map != null && map.getZoneModelId() > 0) {
            if (isSelf) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.EXISTINCLONE);
            } else {
                MessageUtils.notify_player(leader, Notify.SHOWBOX, MessageString.TEAMENTERCLONEERRORCROSS, player.getName());
            }
            return false;
        }

        //TODO 玩家正在切换地图 或在 登录中
        if (EntityState.ChangeMap.compare(player.getState()) || EntityState.LoginGame.compare(player.getState())) {
            if (isSelf) {
                MessageUtils.notify_player(leader, Notify.SHOWBOX, MessageString.ENTERZONEWAITOVERMAP);
            } else {
                MessageUtils.notify_player(leader, Notify.SHOWBOX, MessageString.PlayerChangeMAP, player.getName());
            }
            return false;
        }
        if (bean.getType() == CopyMapType.Marry_CopyMap) {
            if (!isSelf && !Manager.marriageManager.manager().isCouple(player, leader)) {
                //TODO 检测性别
                MessageUtils.notify_player(leader, Notify.SHOWBOX, MessageString.MARRIAGECLONE_ENTER4);
                return false;
            }
        }
        //TODO 检测副本合并次数
        int mergeCount = (int) Manager.countManager.getCount(player, BaseCountType.COPY_Merge_Count, bean.getId());
        if (mergeCount > 1) {
            if (!Manager.backpackManager.manager().onRemoveItem(player, Global.Wweep_Need_Item, mergeCount - 1, ItemChangeReason.ZoneMergeDec, IDConfigUtil.getLogId())) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(Global.Wweep_Need_Item));
                return false;
            }
            Manager.countManager.setCount(player, BaseCountType.COPY_Merge_Count, bean.getId(), Count.RefreshType.CountType_Day, 0);
            player.getCloneSetting().put(bean.getId(), mergeCount);
        }
        return true;
    }

    @Override
    public List<Monster> copyMapRefreshMonster(MapObject map, int copyModelID, int loop) {
        Cfg_Clone_monster_Bean bean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(copyModelID * 1000 + loop);
        if (bean == null) {
            LOG.error("刷怪的找不到数据表配置信息：" + copyModelID + "_" + loop);
            return null;
        }
        if (bean.getMonster_information().size() < 1) {
            return null;
        }
        List<Monster> list = new ArrayList<>();
        for (ReadArray<Integer> ll : bean.getMonster_information().getValuees()) {
            int x = ll.get(2);
            int y = ll.get(3);
            for (int i = 0; i < ll.get(1); ++i) {
                if (ll.size() > 5) {
                    x = RandomUtils.random(Math.min(ll.get(2), ll.get(4)), Math.max(ll.get(2), ll.get(4)));
                    y = RandomUtils.random(Math.min(ll.get(3), ll.get(5)), Math.max(ll.get(3), ll.get(5)));
                }
                //刷新怪物
                Monster monster = Manager.monsterManager.createMonster(map, new Position(x, y), ll.get(0));
                list.add(monster);
            }
        }
        return list;
    }

    @Override
    public void copyMapTeamReady() {
        long now = TimeUtils.Time();
        for (CopyMapTeam copyMapTeam : Manager.copyMapManager.getCopyMapTeams().values()) {
            TeamInfo teamInfo = Manager.teamManager.getTeam(copyMapTeam.getTeamId());
            if (null == teamInfo) {
                Manager.copyMapManager.getCopyMapTeams().remove(copyMapTeam.getTeamId());
                LOG.info(copyMapTeam.getTeamId() + " 已经解散了！");
                continue;
            }

            List<Player> plist = new ArrayList<>();
            for (long roleId : teamInfo.getMembers()) {
                Player player = Manager.playerManager.getPlayerOnline(roleId);
                if (null == player) {
                    continue;
                }
                plist.add(player);
            }

            if (plist.isEmpty()) {
                Manager.copyMapManager.getCopyMapTeams().remove(copyMapTeam.getTeamId());
                continue;
            }

            //检查有队长变化， 则修改取消挑战操作
            if (teamInfo.getLeaderId() != copyMapTeam.getCreateRoleId()) {
                sendNotOkTeamEnter(plist, copyMapTeam.getCreateRoleId(), Manager.registerManager.getRoleName(copyMapTeam.getCreateRoleId()), 2, copyMapTeam.getTeamId());
                Manager.copyMapManager.getCopyMapTeams().remove(copyMapTeam.getTeamId());
                continue;
            }


            long offset = now - copyMapTeam.getStartTime();
            int st = 0;

            switch (copyMapTeam.getType()) {
                case CopyMapType.CopyMap_LocalTeam: {
                    long rrid = 0;
                    for (long roleId : teamInfo.getMembers()) {
                        Player player = Manager.playerManager.getPlayerOnline(roleId);
                        if (null == player) {
                            continue;
                        }

                        if (roleId == teamInfo.getLeaderId()) {
                            st += 1;
                            continue;
                        }

                        if (copyMapTeam.getRoleReady().containsKey(roleId)) {
                            st += 1;
                            continue;
                        }

                        if (rrid < 1) {
                            rrid = roleId;
                        }
                    }

                    if (st == teamInfo.getMembers().size()) {
                        Player leader = Manager.playerManager.getPlayerOnline(teamInfo.getLeaderId());
                        if (null == leader) {
                            sendNotOkTeamEnter(plist, rrid, Manager.registerManager.getRoleName(rrid), 2, copyMapTeam.getTeamId());
                            Manager.copyMapManager.getCopyMapTeams().remove(copyMapTeam.getTeamId());
                            break;
                        }
                        enterCopyMap(leader, plist, copyMapTeam.getModelId(), 1);
                        sendNotOkTeamEnter(plist, teamInfo.getLeaderId(), Manager.registerManager.getRoleName(teamInfo.getLeaderId()), 0, copyMapTeam.getTeamId());
                        Manager.copyMapManager.getCopyMapTeams().remove(copyMapTeam.getTeamId());
                        break;
                    }

                    //超时了
                    if (offset >= waitReadyTime) {
                        LOG.error(rrid + "　没有准备，他" + Manager.registerManager.getRoleName(rrid) + "不参加了！");
                        sendNotOkTeamEnter(plist, rrid, Manager.registerManager.getRoleName(rrid), 2, copyMapTeam.getTeamId());
                        Manager.copyMapManager.getCopyMapTeams().remove(copyMapTeam.getTeamId());
                    }
                }
                break;
                case CopyMapType.CopyMap_CrossTeam: {
                    long rrid = 0;
                    for (long roleId : teamInfo.getMembers()) {
                        Player player = Manager.playerManager.getPlayerOnline(roleId);
                        if (null == player) {
                            continue;
                        }

                        if (roleId == teamInfo.getLeaderId()) {
                            st += 1;
                            continue;
                        }

                        if (copyMapTeam.getRoleState().containsKey(roleId)) {
                            st += 1;
                            continue;
                        }

                        if (rrid < 1) {
                            rrid = roleId;
                        }
                    }

                    if (st == teamInfo.getMembers().size()) {
                        ZoneMessage.G2PReqEnterZone.Builder msg = ZoneMessage.G2PReqEnterZone.newBuilder();
                        msg.setModelId(copyMapTeam.getModelId());
                        msg.setRoleId(teamInfo.getLeaderId());
//                        int headId = 0;
//                        int headFrameId = 0;
                        for (Player player : plist) {
                            //设置跨服的标识
//                            if (player.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_TYPE)) {
//                                headId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID();
//                            }
//                            if (player.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_FRAME_TYPE)) {
//                                headFrameId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID();
//                            }
                            player.playerCrossData.reqFightTime = TimeUtils.Time();
                            player.playerCrossData.isReqFight = true;
                            ZoneMessage.cloneTeamInfo.Builder info = ZoneMessage.cloneTeamInfo.newBuilder();
                            info.setCarear(player.getCareer());
                            info.setLeader(teamInfo.getLeaderId() == player.getId());
                            info.setReady(true);
                            info.setRoleId(player.getId());
                            info.setRoleName(player.getName());
                            info.setServerId(ServerConfig.getServerId());
                            info.setLevel(player.getLevel());
                            info.setPower(player.getFightPoint());
//                            info.setHeadId(headId);
//                            info.setHeadFrameId(headFrameId);

                            info.setHead(MapUtils.getHead(player));
                            msg.addList(info);
                        }
                        msg.setPlat(ServerConfig.getServerPlatform());
                        msg.setSid(ServerConfig.getServerId());
                        MessageUtils.send_to_public(ZoneMessage.G2PReqEnterZone.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                        sendNotOkTeamEnter(plist, rrid, Manager.registerManager.getRoleName(rrid), 0, copyMapTeam.getTeamId());
                        Manager.copyMapManager.getCopyMapTeams().remove(copyMapTeam.getTeamId());
                        break;
                    }
                    //超时了
                    if (offset >= waitAcceptTime) {
                        LOG.error(rrid + "　没有同意，他" + Manager.registerManager.getRoleName(rrid) + "不参加了！");
                        sendNotOkTeamEnter(plist, rrid, Manager.registerManager.getRoleName(rrid), 2, copyMapTeam.getTeamId());
                        Manager.copyMapManager.getCopyMapTeams().remove(copyMapTeam.getTeamId());
                    }
                }
                break;
                default:
                    LOG.info(copyMapTeam.getTeamId() + " 没有找到组队副本要进入的类型:！" + copyMapTeam.getType() + "!");
                    break;
            }
        }
    }

    @Override
    public void onP2GResEnterZone(ChannelHandlerContext context, ZoneMessage.P2GResEnterZone mess) {
        if (mess.getState() < 1) {

            int modelId = mess.getModelId();
            Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(modelId);

            if (bean == null) {
                LOG.error(mess.getRoleId() + " 创建的跨服副本" + mess.getModelId() + " 成功了！但返回时配置没有找到！");
                return;
            }
            // TODO: 2019/7/10 lw
//            if (mess.getEnterType() == CopyMapType.SINGLEPLAYER) {
//                for (long roleId : mess.getRoleIdsList()) {
//                    Player player = Manager.playerManager.getCachePlayer(roleId);
//                    if (null == player) {
//                        LOG.error(mess.getRoleId() + " 创建的组队跨服副本" + mess.getModelId() + " 成功了！但玩家" + roleId + "已经不存在了！");
//                        continue;
//                    }
//                    LOG.error(mess.getRoleId() + " 创建的组队跨服副本" + mess.getModelId() + " 成功, 单人退出队伍");
////                    int state = player.playerCrossData.crossState;
////                    player.playerCrossData.crossState = CrossState.PCS_LOCAL;
//////                    manager.teamManager.OnQuitTeam(player);
////                    player.playerCrossData.crossState = state;
//                }
//            }

            LOG.error(mess.getRoleId() + " 创建的跨服副本" + mess.getModelId() + "(" + bean.getDuplicate_name() + ") 成功了！");


            for (long roleId : mess.getRoleIdsList()) {
                Player player = Manager.playerManager.getPlayerCache(roleId);
                if (null == player) {
                    LOG.error(mess.getRoleId() + " 创建的组队跨服副本" + mess.getModelId() + " 成功了！但玩家" + roleId + "已经不存在了！");
                    continue;
                }
            }
            return;
        }

        //解除玩家的跨服标志
        for (long roleId : mess.getRoleIdsList()) {
            Player player = Manager.playerManager.getPlayerCache(roleId);
            if (null == player) {
                continue;
            }

            player.playerCrossData.reqFightTime = 0;
            player.playerCrossData.isReqFight = false;
            player.playerCrossData.crossState = CrossState.PCS_LOCAL;
            player.playerCrossData.setToFightServer(false);
            //告之玩家已经没有开放了
            if (mess.getState() == 5) {
                if (player.isOnline()) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CLONENOTOPEN, Manager.copyMapManager.getCopyMapName(mess.getModelId()));//, "副本已经关闭了！");//("您已经在副本中。"));
                }
            }

            if (mess.getState() == 8) {
                if (roleId == mess.getRoleId()) {
                    if (player.isOnline()) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.OUTLANDNOTSAMEMODELSELF);
                    }
                } else {
                    if (player.isOnline()) {
                        MessageUtils.notify_player(player, Notify.ERROR, MessageString.OUTLANDPLAYERNOTSAMEMODEL, Manager.registerManager.getRoleName(mess.getRoleId()));//, "副本已经关闭了！");//("您已经在副本中。"));
                    }
                }
            }
        }
    }

    @Override
    public void onP2GResReadyZone(ChannelHandlerContext context, ZoneMessage.P2GResCrossZoneReadyZone mess) {

    }

    @Override
    public void onP2GMatchSucceed(ChannelHandlerContext context, ZoneMessage.P2GReqMatchSucceed mess) {

        ZoneMessage.ResEnterZoneTeamInfo.Builder info = ZoneMessage.ResEnterZoneTeamInfo.newBuilder();
        info.setEndTime(mess.getEndTime()).setModelId(mess.getModelId()).setTeamId(mess.getTeamId()).addAllInfolist(mess.getInfolistList()).setCross(true);
        List<Integer> robots = new ArrayList<>();
        for (ZoneMessage.cloneTeamInfo teamInfo : mess.getInfolistList()) {
            if (teamInfo.getIsRobot()) {
                robots.add(teamInfo.getCarear());
            }
        }
        int[] robot = new int[robots.size()];
        if (robot.length != 0) {
            for (int i = 0; i < robot.length; i++) {
                robot[i] = robots.get(i);
            }
        }
        for (ZoneMessage.cloneTeamInfo teamInfo : mess.getInfolistList()) {
            if (teamInfo.getIsRobot()) {
                robots.add(teamInfo.getCarear());
                continue;
            }
            //只发送本服的
            if (teamInfo.getServerId() == ServerConfig.getServerId()) {
                Player player = Manager.playerManager.getPlayerOnline(teamInfo.getRoleId());
                if (player != null) {
                    player.setMatchRobot(robot);
                    MessageUtils.send_to_player(player, ZoneMessage.ResEnterZoneTeamInfo.MsgID.eMsgID_VALUE, info.build().toByteArray());
                }
            }
        }
    }

    @Override
    public void onReqCancelMatch(Player player) {
        //发送跨服处理有人取消。整个队伍不参与匹配了
        ZoneMessage.G2PReqCancelMatch.Builder msg = ZoneMessage.G2PReqCancelMatch.newBuilder();
        msg.setRoleId(player.getId());
        msg.setName(player.getName());
        MessageUtils.send_to_public(ZoneMessage.G2PReqCancelMatch.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onP2GReqCancelCrossTag(ChannelHandlerContext context, ZoneMessage.P2GReqCancelCrossTag mess) {
        List<ZoneMessage.cloneTeamInfo> list = mess.getInfolistList();
        ZoneMessage.ResMatchFailure.Builder msg = ZoneMessage.ResMatchFailure.newBuilder();
        msg.setName(mess.getRoleName());
        msg.setReason(1);
        int type = mess.getType();
        for (ZoneMessage.cloneTeamInfo info : list) {
            Player player = Manager.playerManager.getPlayerCache(info.getRoleId());
            if (player != null) {
                //取消玩家跨服标志
                msg.setModelId(player.playerCrossData.toZoneModelId);
                player.playerCrossData.setToFightServer(false);//清除跨服标识
                player.playerCrossData.toFightSid = 0;
                player.playerCrossData.toZoneModelId = 0;
                player.playerCrossData.toFightId = 0;
                player.playerCrossData.isReqFight = false;
                player.playerCrossData.crossState = CrossState.PCS_LOCAL;
                if (type == 1) {
                    MessageUtils.send_to_player(player, ZoneMessage.ResMatchFailure.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                } else {
                    sendNotOkTeamEnter(Collections.singletonList(player), mess.getCulpritId(), mess.getRoleName(), 2, 0);
                }
            }
        }
    }

    // TODO: 2019/7/10 lw 需要扩展跨服匹配
    @Override
    public void onReqReadyZone(Player player, ZoneMessage.ReqReadyZone mess) {
        //准备只分为两种 一种跨服，一种本地当玩家在跨服当中，直接发送
        if (player.playerCrossData.isReqFight || player.playerCrossData.crossState == CrossState.PCS_PIPEI) {
            //去跨服准备好了就开始战斗
            ZoneMessage.G2PReqCrossZoneReadyZone.Builder msg = ZoneMessage.G2PReqCrossZoneReadyZone.newBuilder();
            msg.setReady(mess.getReady());
            msg.setRoleId(player.getId());
            msg.setTeamId(mess.getTeamId());
            MessageUtils.send_to_public(ZoneMessage.G2PReqCrossZoneReadyZone.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            return;
        }
        //匹配的类型
        if (player.getTeamId() < 1) {
            LOG.error(player + " 的不是组队的成员了！");
            sendNotOkTeamEnter(Collections.singletonList(player), player.getId(), player.getName(), 1, player.getTeamId());
            return;
        }

        CopyMapTeam zt = Manager.copyMapManager.getCopyMapTeams().get(player.getTeamId());
        if (null == zt) {
            LOG.error(player + " 的准备时，副本组队已经不存在了！");
            sendNotOkTeamEnter(Collections.singletonList(player), player.getId(), player.getName(), 1, player.getTeamId());
            return;
        }
        List<Player> plist = new ArrayList<>();
        TeamInfo ti = Manager.teamManager.getTeam(player.getTeamId());
        if (null == ti) {
            Manager.copyMapManager.getCopyMapTeams().remove(player.getTeamId());
            LOG.error(player + " 的准备时，组队已经不存在了！");
            sendNotOkTeamEnter(Collections.singletonList(player), player.getId(), player.getName(), 1, player.getTeamId());
            return;
        }

        for (long roleId : ti.getMembers()) {
            Player pp = Manager.playerManager.getPlayerOnline(roleId);
            if (null == pp) {
                continue;
            }
            plist.add(pp);
        }

        //准备状态， 同意准备
        if (mess.getReady()) {
            zt.getRoleReady().put(player.getId(), true);
            sendTeamState(ti, plist, zt, (zt.getType() == CopyMapType.CopyMap_CrossTeam));
        } else {
            //不同意进入， 通知， 然后解散副本组
            sendNotOkTeamEnter(plist, player.getId(), player.getName(), 1, player.getTeamId());
            Manager.copyMapManager.getCopyMapTeams().remove(player.getTeamId());
        }
    }

    private void sendNotOkTeamEnter(List<Player> plist, long roleId, String name, int type, long teamId) {
        ZoneMessage.ResTeamEnterZoneFailure.Builder msg = ZoneMessage.ResTeamEnterZoneFailure.newBuilder();
        msg.setTeamId(teamId);
        msg.setRoleId(roleId);
        msg.setRoleName(name);
        msg.setType(type);
        MessageUtils.send_to_players(plist, ZoneMessage.ResTeamEnterZoneFailure.MsgID.eMsgID_VALUE, msg.build().toByteArray(), 0);
    }

    @Override
    public void writeEnterLog(int cloneId, Player player, boolean isAuto) {
        player.setInBattle(false);

        CopyMapEnterLog cel = new CopyMapEnterLog();
        cel.setAuto(isAuto ? 1 : 0);
        cel.setCloneId(cloneId);
        cel.setLevel(player.getLevel());
        cel.setPlayer(player);
        LogService.getInstance().execute(cel);
    }


    private void sendTeamState(TeamInfo ti, List<Player> plist, CopyMapTeam zt, boolean isCross) {
        ZoneMessage.ResEnterZoneTeamInfo.Builder msg = ZoneMessage.ResEnterZoneTeamInfo.newBuilder();
        msg.setModelId(zt.getModelId());
        msg.setEndTime((int) (waitReadyTime + zt.getStartTime() - TimeUtils.Time()) / 1000);
        msg.setTeamId(ti.getTeamId());
        int headId = 0;
        int headFrameId = 0;

        for (Player pp : plist) {
//            if (pp.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_TYPE)) {
//                headId = pp.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID();
//            }
//            if (pp.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_FRAME_TYPE)) {
//                headFrameId = pp.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID();
//            }
            ZoneMessage.cloneTeamInfo.Builder info = ZoneMessage.cloneTeamInfo.newBuilder();
            info.setCarear(pp.getCareer());
            info.setReady(zt.getReady(pp.getId()));
            info.setLeader(pp.getId() == ti.getLeaderId());
            info.setRoleId(pp.getId());
            info.setRoleName(pp.getName());
            info.setServerId(ServerConfig.getServerId());
            info.setLevel(pp.getLevel());
            info.setPower(pp.getFightPoint());
//            info.setHeadFrameId(headFrameId);
//            info.setHeadId(headId);
            info.setHead(MapUtils.getHead(pp));

            msg.addInfolist(info);
        }
        msg.setCross(isCross);
        MessageUtils.send_to_players(plist, ZoneMessage.ResEnterZoneTeamInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray(), 0);
    }

    private boolean checkTeam(Player player, Cfg_Clone_map_Bean bean, TeamInfo ti, int param) {
        if (ti.getLeaderId() != player.getId()) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.ENTERZONEERROR6);
            LOG.error(player + " 发起了进入副本ID =" + bean.getId() + "还不是队长， 不能使用进入副本功能");
            return false;
        }

        if (Manager.copyMapManager.getCopyMapTeams().containsKey(ti.getTeamId())) {
            LOG.error(player + " 发起了进入副本ID =" + bean.getId() + " 队伍已经有副本在排队了");
            return false;
        }
        return checkTeamMember(player, ti, bean, param);
    }

    private boolean checkTeamMember(Player player, TeamInfo ti, Cfg_Clone_map_Bean bean, int param) {
        for (long roleId : ti.getMembers()) {
            Player pp = Manager.playerManager.getPlayerOnline(roleId);
            if (null == pp) {
                Player leader = Manager.playerManager.getPlayerOnline(ti.getLeaderId());
                PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(roleId);
                if (leader != null && info != null) {
                    MessageUtils.notify_player(leader, Notify.NORMAL, MessageString.PlayerNotOnline, info.getRolename());
                }
                return false;
            }

            if (!copyMapCheck(pp, bean, param, player)) {
                Player leader = Manager.playerManager.getPlayerOnline(ti.getLeaderId());
                MessageUtils.notify_player(leader, Notify.NORMAL, MessageString.MEMBER_CANNOT_JOIN_TEAM_COPY, pp.getName());
                return false;
            }
        }
        return true;
    }

    private boolean enterSingleLocalCopyMap(Player player, Cfg_Clone_map_Bean bean, int param) {
        List<Player> list = new ArrayList<>();
        list.add(player);
        if (!copyMapCheck(player, bean, param, player)) {
            return false;
        }
        if (!enterCopyMap(player, list, bean.getId(), param)) {
            return false;
        }
        return true;
    }

    private boolean enterSingleCrossCopyMap(Player player, Cfg_Clone_map_Bean bean, int param) {
        if (!copyMapCheck(player, bean, param, player)) {
            return false;
        }

        player.playerCrossData.reqFightTime = TimeUtils.Time();
        player.playerCrossData.isReqFight = true;

        ZoneMessage.G2PReqEnterZone.Builder msg = ZoneMessage.G2PReqEnterZone.newBuilder();
        msg.setModelId(bean.getId());
        msg.setRoleId(player.getId());

//        int headId = 0;
//        int headFrameId = 0;
//        if (player.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_TYPE)) {
//            headId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID();
//        }
//        if (player.getNewFashionData().getWearDatas().containsKey(NewFashionManager.HEAD_FRAME_TYPE)) {
//            headFrameId = player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID();
//        }
        ZoneMessage.cloneTeamInfo.Builder info = ZoneMessage.cloneTeamInfo.newBuilder();
        info.setCarear(player.getCareer());
        info.setLeader(true);
        info.setReady(true);
        info.setRoleId(player.getId());
        info.setRoleName(player.getName());
        info.setServerId(ServerConfig.getServerId());
        info.setLevel(player.getLevel());
        info.setPower(player.getFightPoint());
//        info.setHeadId(headId);
//        info.setHeadFrameId(headFrameId);

        info.setHead(MapUtils.getHead(player));

        msg.addList(info);
        msg.setPlat(ServerConfig.getServerPlatform());
        msg.setSid(ServerConfig.getServerId());
        msg.setBirthGroup(0);
//        msg.setEnterType(type);
        msg.setGuildId(player.getGuildId());
        MessageUtils.send_to_public(ZoneMessage.G2PReqEnterZone.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        player.playerCrossData.crossState = CrossState.PCS_PIPEI;
        return true;
    }

    private boolean createCopyMapTeam(Player player, Cfg_Clone_map_Bean bean, TeamInfo ti, boolean isCross) {
        CopyMapTeam copyMapTeam = new CopyMapTeam();
        copyMapTeam.setTeamId(ti.getTeamId());
        copyMapTeam.setStartTime(TimeUtils.Time());
        copyMapTeam.setModelId(bean.getId());
        copyMapTeam.setCreateRoleId(player.getId());
        copyMapTeam.getRoleReady().put(player.getId(), true);
        if (isCross) {
            copyMapTeam.setType(CopyMapType.CopyMap_CrossTeam);
        } else {
            copyMapTeam.setType(CopyMapType.CopyMap_LocalTeam);
        }
        Manager.copyMapManager.getCopyMapTeams().put(ti.getTeamId(), copyMapTeam);

        List<Player> plist = new ArrayList<>();
        for (long roleId : ti.getMembers()) {
            Player pp = Manager.playerManager.getPlayerOnline(roleId);
            if (null == pp) {
                continue;
            }
            plist.add(pp);
        }
        sendTeamState(ti, plist, copyMapTeam, isCross);
        return true;
    }

    private boolean enterLocalCopyMap(Player player, Cfg_Clone_map_Bean bean, int param) {

        //单人匹配
        if (bean.getEnter_sub_type() == 1) {
            return enterSingleLocalCopyMap(player, bean, param);
        }
        //组队
        if (bean.getEnter_sub_type() == 2) {
            if (player.getTeamId() <= 0) {
                return false;
            }

            TeamInfo ti = Manager.teamManager.getTeam(player.getTeamId());
            if (ti == null) {
                LOG.error(player + " 发起了进入副本ID =" + bean.getId() + "还没有加入队伍");
                return false;
            }
            if (ti.getMembers().size() != 2) {
                return false;
            }

            if (!checkTeam(player, bean, ti, param)) {
                return false;
            }

            return createCopyMapTeam(player, bean, ti, false);
        }

        //单人加组队
        if (bean.getEnter_sub_type() == 3) {
            if (player.getTeamId() <= 0) {
                return enterSingleLocalCopyMap(player, bean, param);
            }

            TeamInfo ti = Manager.teamManager.getTeam(player.getTeamId());
            if (ti == null) {
                LOG.error(player + " 发起了进入副本ID =" + bean.getId() + "还没有加入队伍");
                return false;
            }
            if (ti.getMembers().size() == 1) {
                return enterSingleLocalCopyMap(player, bean, param);
            }

            if (!checkTeam(player, bean, ti, param)) {
                return false;
            }
            return createCopyMapTeam(player, bean, ti, false);
        }
        return false;
    }

    private boolean enterCrossCopyMap(Player player, Cfg_Clone_map_Bean bean, int param) {
        if (Manager.publicServerManager.getPublicSession() == null) {
            LOG.error(player + " 跨服公共服务器还没有连接， 不能正常使用此跨服功能！");
            return false;
        }

        //单人匹配
        if (bean.getEnter_sub_type() == 1) {
            return enterSingleCrossCopyMap(player, bean, param);
        }

        //组队匹配
        if (bean.getEnter_sub_type() == 2) {
            return false;
        }

        //单人加组队
        if (bean.getEnter_sub_type() == 3) {
            if (player.getTeamId() <= 0) {
                return enterSingleCrossCopyMap(player, bean, param);
            }

            TeamInfo ti = Manager.teamManager.getTeam(player.getTeamId());
            if (ti == null) {
                LOG.error(player + " 发起了进入副本ID =" + bean.getId() + "还没有加入队伍");
                return false;
            }
            if (ti.getMembers().size() == 1) {
                return enterSingleCrossCopyMap(player, bean, param);
            }

            if (!checkTeam(player, bean, ti, param)) {
                return false;
            }

            return createCopyMapTeam(player, bean, ti, true);
        }
        return false;
    }

    private boolean enterCopyMap(Player player, List<Player> plist, int modelId, int param) {
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(modelId);
        Cfg_Mapsetting_Bean mapBean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(bean.getMapid());
        MapObject mapObject;
        if (mapBean.getIsscript() == ScriptEnum.NoodBossMapScript) {
            mapObject = Manager.bossManager.manager().getNoodBoss(modelId);
        } else if (mapBean.getIsscript() == ScriptEnum.MarryWeddingActivityScript) {
            mapObject = Manager.marriageManager.manager().getWeddingScene();
        } else {
            mapObject = Manager.mapManager.createCopyMap(modelId, param, player.getId(), player, plist);
        }
        if (mapObject == null) {
            return false;
        }

        for (Player pp : plist) {
            Manager.mapManager.changeMap(pp, mapObject.getId(), mapObject.getBrithPos(), false);

            writeEnterLog(modelId, pp, false);
            Manager.copyMapManager.logic().biInstance(pp, bean.getId(), 0, 1, param, false);
        }
        return true;
    }

    @Override
    public void onReqFlashMonster(Player player, CopyMapMessage.ReqFlashMonster messInfo) {
        int loop = messInfo.getNum();
        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        PlaneMapData mapData = MapParam.getPlaneMapData(mapObject);
        int modelId = mapObject.getZoneModelId();
        Cfg_Clone_monster_Bean bean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(modelId * 1000 + loop);
        if (bean == null) {
            LOG.error("客户端通知位面刷怪，找不到怪物表 modeId = " + modelId + ", loop = " + loop + "，玩家 = " + player.getId());
            return;
        }

        //是不是最后一波怪了
        if (bean.getIf_end() == 1) {
            mapData.setEnd(true);
        }
        for (ReadArray<Integer> ll : bean.getMonster_information().getValuees()) {
            int x = ll.get(2);
            int y = ll.get(3);
            Position pos = new Position(x, y);
            for (int i = 0; i < ll.get(1); i++) {
                Manager.monsterManager.createMonster(mapObject, pos, ll.get(0));
            }
//            logger.error("位面刷怪，位面id = " + modelId +
//                    "，clone_monster = " + (modelId * 1000 + loop) + "，怪物id = " + ll.get(0) + "，数量 = " + ll.get(1) +
//                    "，位置（" + x + "，" + y + "）");
        }
        mapData.setLoop(++loop);
    }

    public void sendF2GCloneEnterAddOne(MapObject map, Player player, Object... objects) {
        CrossServerMessage.F2GCloneEnterAddOne.Builder builder = CrossServerMessage.F2GCloneEnterAddOne.newBuilder();
        builder.setFightId(map.getId());
        builder.setModelId(map.getZoneModelId());
        builder.addScriptId(map.getSetting().getIsscript());
        builder.addRoleIds(player.getId());
        if (objects != null && objects.length > 0) {
            builder.setValue((Long) objects[0]);
        }
        FightClientManager.GetInstance().send_to_game(player.getIosession(), CrossServerMessage.F2GCloneEnterAddOne.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 数据初始化
     */
    @Override
    public void load() {
        for (Cfg_Daily_Bean dailyBean : CfgManager.getCfg_Daily_Container().getValuees()) {
            for (int i = 0; i < dailyBean.getCloneID().size(); i++) {
                Manager.copyMapManager.getZoneDaily().put(dailyBean.getCloneID().get(i), dailyBean.getId());
            }
        }
    }
}
