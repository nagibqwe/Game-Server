package common.team;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.Global;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.data.container.Cfg_Clone_map_Container;
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.friend.struct.Friend;
import com.game.friend.struct.PlayerRelation;
import com.game.guild.structs.Guild;
import com.game.hook.manager.PlayerHookManager;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.newfashion.manager.NewFashionManager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.EntityState;
import com.game.structs.ServerStr;
import com.game.team.manager.TeamManager;
import com.game.team.script.ITeamHandler;
import com.game.team.structs.MatchTeam;
import com.game.team.structs.TeamCall;
import com.game.team.structs.TeamConstant;
import com.game.team.structs.TeamInfo;
import com.game.utils.MessageUtils;
import game.core.util.CrossState;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.TeamMessage;
import game.message.TeamMessage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author admin
 */
public class TeamHandlerScript implements ITeamHandler {

    private static final Logger log = LogManager.getLogger(TeamHandlerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.TeamHandlerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }


    /**
     * 处理创建队伍请求
     */
    @Override
    public void reqCreateTeamHandler(Player player, int type, boolean auto) {
        createTeam(player,type,auto);
    }

    /**
     * 获取队伍信息
     */
    @Override
    public void reqGetTeamInfoHandler(Player player, TeamMessage.ReqGetTeamInfo messInfo) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if (team == null) {
            return;
        }
        ResTeamInfo.Builder teamInfo = buildTeamInfo(team);
        MessageUtils.send_to_player(player, ResTeamInfo.MsgID.eMsgID_VALUE, teamInfo.build().toByteArray());
    }

    /**
     * 请求修改队伍信息
     */
    @Override
    public void reqAlterTeamHandler(Player player, TeamMessage.ReqAlterTeam messInfo) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        //检查玩家是否为队长
        if (checkNotTeamLeader(team, player)) {
            return;
        }

        //修改队伍类型
        if (team.getType() != messInfo.getType()) {
            team.setType(messInfo.getType());
            Manager.teamManager.getMatchTeams().remove(team.getTeamId());
            ResTeamInfo.Builder builder = buildTeamInfo(team);
            sendMessage(team, ResTeamInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }

        //世界喊话
        if (messInfo.getIsNotice()) {
            if (team.isFull()) {
                MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Team_TeamFullInfo);
                return;
            }
            if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.TEAM_NOTICE_CD, null)) {
                long cd = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.TEAM_NOTICE_CD, null);
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.CooldownIng, String.valueOf((cd + 999) / 1000));
                return;
            }
            int level = Global.ChatWorldLevel.get(0);
            int vipLevel = Global.ChatWorldLevel.get(1);
            if (player.getLevel() < level && player.getVipLv() < vipLevel) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.CHATERROR_LEVELNOTENOUGH, ServerStr.getLevelNameHighStr(level), ServerStr.getLevelNameSlowStr(level), vipLevel + "");
                return;
            }
            notice(team);
            MessageUtils.notify_player(player, Notify.SUCCESS, MessageString.TeamalertMess);
            Manager.cooldownManager.addCooldown(player, CooldownTypes.TEAM_NOTICE_CD, null, TeamConstant.TEAM_NOTICE_CD);
        }
    }


    /**
     * 请求获取等待的队伍列表
     */
    @Override
    public void reqGetWaitListHandler(Player player, TeamMessage.ReqGetWaitList messInfo) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        ResWaitList.Builder msg = ResWaitList.newBuilder();
        msg.setType(messInfo.getType());

        //遍历所有队伍
        for (TeamInfo team : Manager.teamManager.getTeams().values()) {
            if (team == null) {
                continue;
            }
            if (messInfo.getType() != 0 && team.getType() != messInfo.getType()) {
                continue;
            }
            if (team.isFull()) {
                continue;
            }

            if (team.getRefuses().containsKey(player.getId())) {
                Long refuseTime = team.getRefuses().get(player.getId());
                if (TimeUtils.Time() - refuseTime < TeamConstant.TEAM_REFUSE_TIME) {
                    continue;
                }
            }

            Player leader = Manager.playerManager.getPlayerCache(team.getLeaderId());
            if (leader == null) {
                continue;
            }

            if (leader.playerCrossData.crossState != CrossState.PCS_LOCAL || leader.playerCrossData.isToFightServer()) {
                continue;
            }
            Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(leader.gainMapModelId());
            if (config.getCan_team() == 0) {
                continue;
            }
            msg.addTeams(buildTeamInfo1(team));
        }
        MessageUtils.send_to_player(player, ResWaitList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //队伍信息
    private TeamMessage.TeamInfo.Builder buildTeamInfo1(TeamInfo team) {
        TeamMessage.TeamInfo.Builder msg = TeamMessage.TeamInfo.newBuilder();
        msg.setTeamId(team.getTeamId());
        msg.setType(team.getType());
        msg.setAutoAccept(team.isAutoAccept());
        for (long roleId : team.getMembers()) {
            Player member = Manager.playerManager.getPlayerCache(roleId);
            if (member == null) {
                continue;
            }
            msg.addMembers(buildTeamMember(team, member));
        }


        return msg;
    }

    /**
     * 申请入队
     */
    @Override
    public void reqApplyEnterHandler(Player player, TeamMessage.ReqApplyEnter messInfo) {
        ReqApplyEnterHandler(player, messInfo.getTeamId());
    }

    /**
     * 申请入队
     */
    private void ReqApplyEnterHandler(Player player, long teamId) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if (team != null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_PlayerHaveTeamInfo);
            return;
        }
        team = Manager.teamManager.getTeam(teamId);
        if (team == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_TeamOverInfo);
            return;
        }
        //队伍已满
        if (team.isFull()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_TeamFullInfo);
            return;
        }

        Player leader = Manager.playerManager.getPlayerOnline(team.getLeaderId());
        if (leader == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_PlayerOfflineInfo);
            return;
        }
        //检查双方是否能组队
        if (checkCanNotMakeTeam(player, leader)) {
            return;
        }

        //同一队伍被拒绝2分钟后方可继续申请
        if (team.getRefuses().containsKey(player.getId())) {
            long refuseTime = team.getRefuses().get(player.getId());
            if (TimeUtils.Time() < refuseTime + TeamConstant.TEAM_REFUSE_TIME) {
                long time = refuseTime + TeamConstant.TEAM_REFUSE_TIME - TimeUtils.Time();
                time = (time + 999) / 1000;
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_TeamRefuseTime, String.valueOf(time));
                return;
            }
        }

        //自动接受组队申请
        if (team.isAutoAccept()) {
            addTeamMember(team, player);
            return;
        }

        //队伍中添加申请
        if (team.getApplies().containsKey(player.getId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TEAMAPPLYCLICKMORE);
        } else {
            team.getApplies().put(player.getId(), TimeUtils.Time());
            MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, MessageString.TEAM_ADDAPPLY_SUCCESS, leader.getName());
        }

        //返回给队长申请消息
        ResAddApplyer.Builder applyBuilder = ResAddApplyer.newBuilder();
        applyBuilder.setMember(buildFreedom(player, 0));
        MessageUtils.send_to_player(team.getLeaderId(), ResAddApplyer.MsgID.eMsgID_VALUE, applyBuilder.build().toByteArray());

    }

    /**
     * 请求获取申请列表
     */
    @Override
    public void reqGetApplyListHandler(Player player, TeamMessage.ReqGetApplyList messInfo) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());

        //检查是否是队长
        if (checkNotTeamLeader(team, player)) {
            return;
        }

        ResApplyList.Builder msg = ResApplyList.newBuilder();
        for (java.util.Map.Entry<Long, Long> entry :team.getApplies().entrySet()) {
            //申请列表中有自己
            if (entry.getKey() == player.getId()) {
                continue;
            }
            //申请已超时
            if (TimeUtils.Time() - entry.getValue() > TeamConstant.TEAM_APPLY_TIME) {
                continue;
            }
            Player tempPlayer = Manager.playerManager.getPlayerOnline(entry.getKey());
            if (tempPlayer == null) {
                continue;
            }
            msg.addMembers(buildFreedom(tempPlayer, 0));
        }
        MessageUtils.send_to_player(player, ResApplyList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 处理组队申请操作请求
     */
    @Override
    public void reqApplyOptHandler(Player player, TeamMessage.ReqApplyOpt messInfo) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());

        //检查玩家是否为队长
        if (checkNotTeamLeader(team, player)) {
            return;
        }

        //队伍已满
        if (team.isFull()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_TeamFullInfo);
            return;
        }

        //不在申请列表中
        if (!team.getApplies().containsKey(messInfo.getId())) {
            return;
        }

        Player applyPlayer = Manager.playerManager.getPlayerOnline(messInfo.getId());
        if (applyPlayer == null) {
            team.getApplies().remove(messInfo.getId());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_PlayerOfflineInfo);
            return;
        }

        //判定双方是否能组队
        if (checkCanNotMakeTeam(player, applyPlayer)) {
            return;
        }

        //对方已有队伍
        TeamInfo applyTeam = Manager.teamManager.getTeam(applyPlayer.getTeamId());
        if (applyTeam != null) {
            team.getApplies().remove(applyPlayer.getId());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_TargetHaveTeamInfo);
            return;
        }

        //同意申请
        if (messInfo.getType() == TeamConstant.ApplyTypeAgree) {

            Long applyTime = team.getApplies().get(messInfo.getId());
            //两分钟未同意，申请已超时
            if (TimeUtils.Time() - applyTime > TeamConstant.TEAM_APPLY_TIME) {
                team.getApplies().remove(applyPlayer.getId());
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_ApplyTimeOutInfo);
                return;
            }
            team.getApplies().remove(applyPlayer.getId());
            addTeamMember(team, applyPlayer);
            return;
        }

        //拒绝申请，删除申请，添加到拒绝列表
        team.getRefuses().put(applyPlayer.getId(), TimeUtils.Time());
        team.getApplies().remove(applyPlayer.getId());
        MessageUtils.notify_player(applyPlayer, Notify.ERROR, MessageString.Team_BeRefuseInfo, player.getName());
    }

    //增加队员处理逻辑
    private void addTeamMember(TeamInfo team, Player player) {

        team.addMember(player);
        //加入队伍成功后检查buff
        checkTeamBuff(team);

        //新增队员
        ResUpdateTeamMemberInfo.Builder msg = ResUpdateTeamMemberInfo.newBuilder();
        msg.setMember(buildTeamMember(team, player));
        sendMessage(team, ResUpdateTeamMemberInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //组队频道消息
        for (Long memberId : team.getMembers()) {
            Player member = Manager.playerManager.getPlayerCache(memberId);
            if (member.getId() == player.getId()) {
                continue;
            }
            MessageUtils.notify_chat_player(member, ChatChannel.CHATCHANNEL_TEAM, MessageString.JoinTeamNews, player.getName());
        }

        //所有队员
        ResTeamInfo.Builder teamInfo = buildTeamInfo(team);
        MessageUtils.send_to_player(player, ResTeamInfo.MsgID.eMsgID_VALUE, teamInfo.build().toByteArray());

        //自动召集
        boolean isAutoCall =CfgManager.getCfg_Mapsetting_Container().getValueByKey(player.gainMapModelId()).getTeamAuto() == 1;
        //向指定的玩家发送召唤
        if (isAutoCall) {
            callMemberToLeader(Collections.singletonList(player), team, Manager.mapManager.getMap(player.gainMapId()).getName(), player);
        }
        Manager.worldHelpManager.getScript().joinTaskHelpInfo(team.getLeaderId(), player.getId());
    }

    /**
     * 清空申请列表
     */
    @Override
    public void reqCleanApplyListHandler(Player player, TeamMessage.ReqCleanApplyList messInfo) {
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        //检查玩家是否为队长
        if (checkNotTeamLeader(team, player)) {
            return;
        }
        //没有返回消息，应该是客户端自动清了
        team.getApplies().clear();
    }

    /**
     * 请求获取邀请列表
     */
    @Override
    public void reqGetFreedomListHandler(Player player, TeamMessage.ReqGetFreedomList messInfo) {

        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        //检查玩家是否为队长
        if (checkNotTeamLeader(team, player)) {
            return;
        }

        ResFreedomList.Builder msg = ResFreedomList.newBuilder();
        //好友
        if (messInfo.getType() == TeamConstant.SearchTypeFriend) {
            PlayerRelation relation = Manager.friendManager.getPlayerRelation(player.getId());
            if (relation != null) {
                for (Friend friend : relation.getFriends().values()) {
                    Player p = Manager.playerManager.getPlayerOnline(friend.getRoleId());
                    if (p == null) {
                        continue;
                    }
                    if (p.getId() == player.getId() || p.getTeamId() > 0) {
                        continue;
                    }
                    msg.addMembers(buildFreedom(p, friend.getIntimacy()));
                    if (msg.getMembersCount() > Global.TeamInvitationMaxNum) {
                        break;
                    }
                }
            }
        }

        //帮会
        if (messInfo.getType() == TeamConstant.SearchTypeGuild) {
            if (player.isHaveGuild()) {
                Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
                if (guild != null) {
                    for (long memberId : guild.getMembers().keySet()) {
                        Player member = Manager.playerManager.getPlayerOnline(memberId);
                        if (member == null) {
                            continue;
                        }
                        if (member.getId() == player.getId() || member.getTeamId() > 0) {
                            continue;
                        }
                        msg.addMembers(buildFreedom(member, 0));
                        if (msg.getMembersCount() > Global.TeamInvitationMaxNum) {
                            break;
                        }
                    }
                }
            }
        }

        //周围玩家
        if (messInfo.getType() == TeamConstant.SearchTypeRand) {
            MapObject map = MapManager.getInstance().getMap(player.gainMapId());
            // 从全图搜改为了2屏(map.getRound()为直径,这里作为半径用,所以是2屏)
            List<Player> rounds = Manager.mapManager.getRoundPlayer(player, map.getRound());
            for (Player round : rounds) {
                if (!round.isOnline()) {
                    continue;
                }
                if (round.getId() == player.getId() || round.getTeamId() > 0) {
                    continue;
                }
                if (!Manager.controlManager.deal().isOpenFunction(round, FunctionStart.Team)) {
                    continue;
                }
                msg.addMembers(buildFreedom(round, 0));
                if (msg.getMembersCount() > Global.TeamInvitationMaxNum) {
                    break;
                }
            }
        }
        MessageUtils.send_to_player(player, ResFreedomList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 邀请入队
     */
    @Override
    public void reqInviteHandler(Player player, TeamMessage.ReqInvite messInfo) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        Player target = Manager.playerManager.getPlayerOnline(messInfo.getRoleid());
        if (target == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_PlayerOfflineInfo);
            return;
        }
        if (!Manager.controlManager.deal().isOpenFunction(target, FunctionStart.Team)) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.TeamCannotInvitLevelTooLower);
            return;
        }

        if (checkCanNotMakeTeam(player, target)) {
            return;
        }

        //检查队伍情况
        TeamInfo team = Manager.teamManager.getTeam(target.getTeamId());
        if (team != null) {
            team = Manager.teamManager.getTeam(player.getTeamId());
            if (team == null) {
                //对方有队伍，自己无队伍  处理成申请进入对方队伍
                ReqApplyEnterHandler(player, target.getTeamId());
                return;
            }
            //提示对方已有队伍
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_TargetHaveTeamInfo);
            return;
        }

        team = Manager.teamManager.getTeam(player.getTeamId());
        //自己没有队伍，则创建一个
        if (team == null) {
            reqCreateTeamHandler(player, 0, true);
        }

        team = Manager.teamManager.getTeam(player.getTeamId());
        //队伍创建失败，提示玩家没有队伍
        if (team == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_NoTeamInfo);
            return;
        }

        //队伍已满
        if (team.isFull()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_TeamFullInfo);
            return;
        }

        //好友屏蔽系统中有此玩家， 不能被邀请
        if (Manager.friendManager.isShield(player.getId(), target.getId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TEAMIVATEINBACKFREINDLIST);
            return;
        }

        //添加到邀请列表
        if (!team.getInvites().contains(target.getId())) {
            team.getInvites().add(target.getId());
        }

        //发送邀请组队消息
        ResInviteInfo.Builder msg = ResInviteInfo.newBuilder();
        msg.setTeamdId(team.getTeamId());
        msg.setRoleId(player.getId());
        msg.setName(player.getName());
        msg.setType(team.getType());
        MessageUtils.send_to_player(target, ResInviteInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 处理邀请请求
     */
    @Override
    public void reqInviteResHandler(Player player, TeamMessage.ReqInviteRes messInfo) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if (team != null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_PlayerHaveTeamInfo);
            team = Manager.teamManager.getTeam(messInfo.getTeamdId());
            if (team != null) {
                team.getInvites().remove( player.getId());
            }
            return;
        }

        Player target = Manager.playerManager.getPlayerOnline(messInfo.getRoleId());
        if (target == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_TeamOverInfo);
            return;
        }

        team = Manager.teamManager.getTeam(messInfo.getTeamdId());
        if (messInfo.getType() == TeamConstant.InviteTypeAgree) {
            if (team == null) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_TeamOverInfo);
                return;
            }
            if (team.isFull()) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_TeamFullInfo);
                return;
            }
            if (!team.getInvites().contains(player.getId())) {
                return;
            }
            team.getInvites().remove(player.getId());

            //添加队员
            addTeamMember(team, player);
            return;
        }
        //玩家拒绝
        if (team == null) {
            return;
        }
        team.getInvites().remove(player.getId());
        MessageUtils.notify_player(target, Notify.ERROR, MessageString.Team_RefuseInviteInfo, player.getName());
    }


    /**
     * 操作队伍
     * opt: 1提升队长；2踢出队伍；3退出队伍；4解散队伍；5自动接受组队申请
     */
    @Override
    public void reqTeamOptHandler(Player player, TeamMessage.ReqTeamOpt messInfo) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if (team == null) {
            player.setTeamId(0);
            ResDeleteTeamMember.Builder delete = ResDeleteTeamMember.newBuilder();
            delete.setRoleId(player.getId());
            MessageUtils.send_to_player(player, ResDeleteTeamMember.MsgID.eMsgID_VALUE, delete.build().toByteArray());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_TeamOverInfo);
            return;
        }

        //跨服中
        if (player.playerCrossData.crossState != CrossState.PCS_LOCAL) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_PlayerCrossState);
            return;
        }

        //退出队伍
        if (messInfo.getOpt() == TeamConstant.TeamOptTypeQuit) {
            OnQuitTeam(player);
            return;
        }

        //踢出队伍
        if (messInfo.getOpt() == TeamConstant.TeamOptTypeTickOut) {
            //不是队长
            if (team.getLeaderId() != player.getId()) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_NotLeaderInfo);
                return;
            }
            Player ticker = Manager.playerManager.getPlayerOnline(messInfo.getTargetId());
            if (ticker == null) {
                team.removeMember(messInfo.getTargetId());
                ResDeleteTeamMember.Builder delete = ResDeleteTeamMember.newBuilder();
                delete.setRoleId(messInfo.getTargetId());
                sendMessage(team, ResDeleteTeamMember.MsgID.eMsgID_VALUE, delete.build().toByteArray());
                checkTeamBuff(team);
                return;
            }
            //玩家退出队伍
            if(OnQuitTeam(ticker)){
                MessageUtils.notify_player(ticker, Notify.SUCCESS, MessageString.TEAM_BE_REMOVED);
            }
            return;
        }

        //解散队伍
        if (messInfo.getOpt() == TeamConstant.TeamOptTypeBreak) {
            Manager.teamManager.getTeams().remove(team.getTeamId());
            ResDeleteTeamMember.Builder delete = ResDeleteTeamMember.newBuilder();
            Player member;
            for (long roleId : team.getMembers()) {
                member = Manager.playerManager.getPlayerCache(roleId);
                if (member == null) {
                    continue;
                }
                //移除队伍成员的buff
                cleanTeamBuff(member);
                delete.setRoleId(roleId);
                MessageUtils.send_to_player(roleId, ResDeleteTeamMember.MsgID.eMsgID_VALUE, delete.build().toByteArray());
            }
            //移除任务支援相关
            Manager.worldHelpManager.getScript().clearTask(player.getId());
            return;
        }

        //提升队长
        if (messInfo.getOpt() == TeamConstant.TeamOptTypeUpLeader) {
            //不是队长
            if (team.getLeaderId() != player.getId()) {
                return;
            }
            //对方是队长
            if (team.getLeaderId() == messInfo.getTargetId()) {
                return;
            }
            //对方离线中
            Player target = Manager.playerManager.getPlayerOnline(messInfo.getTargetId());
            if (target == null) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_PlayerOfflineInfo);
                return;
            }
            if (!team.getMembers().contains(target.getId())) {
                return;
            }

            team.setLeaderId(target.getId());

            //更新求援信息
            Manager.worldHelpManager.getScript().clearTask(player.getId());//移除老队长的求援信息
            Manager.worldHelpManager.getScript().changeTeamLeader(target,player.getId(),team.getTeamId());

            //同步消息
            ResTeamInfo.Builder msg = buildTeamInfo(team);
            sendMessage(team, ResTeamInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());

            MessageUtils.notify_player(target, Notify.SUCCESS, MessageString.TEAM_PROMOTED_TO_CAPTAIN);
        }

        //自动接受组队申请
        if (messInfo.getOpt() == TeamConstant.TeamOptTypeAutoAccept) {
            team.setAutoAccept(!team.isAutoAccept());
        }

        //请求成为队长
        if (messInfo.getOpt() == TeamConstant.TeamOptReqBecomeLeader) {
            //本身就是队长
            if (team.getLeaderId() == player.getId()) {
                return;
            }
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.TEAM_REQBECOMELEADER);
            Player leader = Manager.playerManager.getPlayerOnline(team.getLeaderId());
            TeamMessage.ResBecomeLeader.Builder builder =TeamMessage.ResBecomeLeader.newBuilder();
            builder.setTargetId(player.getId());
            MessageUtils.send_to_player(leader, TeamMessage.ResBecomeLeader.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }

        //拒绝队员成为队长的申请
        if (messInfo.getOpt() == TeamConstant.TeamOptRefuceMember) {
            if (team.getLeaderId() != player.getId()) {
                return;
            }
            Player member = Manager.playerManager.getPlayerOnline(messInfo.getTargetId());
            MessageUtils.notify_player(member, Notify.ERROR, MessageString.Team_BeRefuseInfo, player.getName());
        }

    }

    /**
     * 请求召集所有队员
     */
    @Override
    public void reqCallAllMemberHandler(Player player, TeamMessage.ReqCallAllMember messInfo) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        if (Manager.cooldownManager.isCooldowning(player, CooldownTypes.TEAM_CALL_CD, null)) {
            long cd = Manager.cooldownManager.getCooldownTime(player, CooldownTypes.TEAM_CALL_CD, null);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CooldownIng, String.valueOf((cd + 999) / 1000));
            return;
        }

        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());

        //检查玩家是否为队长
        if (checkNotTeamLeader(team, player)) {
            return;
        }

        //队长地图是否能召集
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            return;
        }
        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (config.getTeam_sent() == 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_MapCannontCall);
            return;
        }

        //遍历队伍成员，发送召集信息
        List<Player> players = new ArrayList<>();
        for (long roleId : team.getMembers()) {
            //队长
            if (roleId == player.getId()) {
                continue;
            }
            Player member = Manager.playerManager.getPlayerOnline(roleId);
            if (member == null) {
                continue;
            }
            //该地图是否能传送
            config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(member.getCurGps().getModelId());
            if (config.getTeam_sent() == 0) {
                continue;
            }
            players.add(member);
        }
        callMemberToLeader(players, team, map.getName(), player);
        Manager.cooldownManager.addCooldown(player, CooldownTypes.TEAM_CALL_CD, null, 10000);
    }

    /**
     * 同意召唤
     */
    @Override
    public void reqAgreeCallHandler(Player player, TeamMessage.ReqAgreeCall messInfo) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if (team == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_CallTimeOut);
            return;
        }

        TeamCall call = team.getCall(messInfo.getCallId());
        if (call == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_CallTimeOut);
            return;
        }

        MapObject map = Manager.mapManager.getMap(call.getMapID());
        if (map == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_CallTimeOut);
            return;
        }

        //是否该地图等级限制
        if (!MapUtils.isLevelCanEnter(player, map.getMapModelId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.LevelNotEnough2);
            return;
        }
        //检查是否能进入副本
        if (checkCanNotEnterCloneMap(map, player)) {
            return;
        }

        Manager.mapManager.changeMap(player, call.getMapID(), call.getPos(), false);
    }

    /**
     * 传送到队长处
     */
    @Override
    public void reqTransport2LeaderHandler(Player player, TeamMessage.ReqTransport2Leader messInfo) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if (team == null) {
            player.setTeamId(0);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_NoTeamInfo);
            return;
        }
        //自己就是队长
        if (team.getLeaderId() == player.getId()) {
            return;
        }

        //所在地图是否支持传送
        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(player.getCurGps().getModelId());
        if (config.getTeam_sent() == 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_MapCannontTransptInfo);
            return;
        }

        //队长离线中
        Player leader = Manager.playerManager.getPlayerOnline(team.getLeaderId());
        if (leader == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_PlayerOfflineInfo);
            return;
        }

        //队长地图是否支持传送
        MapObject map = Manager.mapManager.getMap(leader.gainMapId());
        config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (config.getTeam_sent() == 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_Cannot2TargetMapInfo);
            return;
        }

        //该地图是否能进入
        if (!MapUtils.isLevelCanEnter(player, config.getMap_id())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_Cannot2TargetMapInfo);
            return;
        }

        //检查进入副本的条件
        if(checkCanNotEnterCloneMap(map, player)) {
            return;
        }


        Manager.mapManager.changeMap(player, leader.gainMapId(), leader.gainCurPos(), false);
    }

    /**
     * 自动匹配
     */
    @Override
    public void reqMatchAllHandler(Player player, TeamMessage.ReqMatchAll messInfo) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return;
        }
        TeamInfo teamInfo = TeamManager.getInstance().getTeam(player.getTeamId());
        if (teamInfo == null) {
            reqCreateTeamHandler(player, messInfo.getType(), true);
            teamInfo = TeamManager.getInstance().getTeam(player.getTeamId());
        }
        if (checkNotTeamLeader(teamInfo, player) || teamInfo.isFull()) {
            return;
        }
        if (!messInfo.getMatch()) {
            Manager.teamManager.getMatchTeams().remove(teamInfo.getTeamId());
            return;
        }
        if (teamInfo.getType() != messInfo.getType()) {
            teamInfo.setType(messInfo.getType());
            ResTeamInfo.Builder builder = buildTeamInfo(teamInfo);
            sendMessage(teamInfo, ResTeamInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
        MatchTeam match = new MatchTeam(player.getTeamId(), messInfo.getType(), teamInfo.getMembers().size());
        match.setMatchOverTime((int) (TimeUtils.Time() / 1000 + Global.TeamMatchTime));
        Manager.teamManager.getMatchTeams().put(match.getTeamId(), match);
    }


    /**
     * 定时器检查
     */
    @Override
    public void tick() {
        for (TeamInfo team : Manager.teamManager.getTeams().values()) {
            //检查离线队员自动退出队伍
            checkOffLineMemberAutoQuit(team);
        }

        //匹配队伍
        matchAllTeam();
    }

    //检查离线队员自动退出队伍
    private void checkOffLineMemberAutoQuit(TeamInfo team) {
        if (team.getMembers().isEmpty()) {
            Manager.teamManager.getTeams().remove(team.getTeamId());
            return;
        }
        for (long roleId : team.getMembers()) {
            Player member = Manager.playerManager.getPlayerCache(roleId);
            if (member == null) {
                OnQuitTeam(team, roleId);
                continue;
            }
            if (member.isOnline()) {
                continue;
            }
            //5分钟自动离队
            if (TimeUtils.Time() - member.getOffLineTime() > Global.Team_leave_time * 60 * 1000) {
                OnQuitTeam(member);
            }
        }
    }

    /**
     * 匹配队伍计时处理器
     * 同一目标的队伍匹配在一起，人数少的队伍加入到人数多的队伍
     * 队长给各队伍中等级最高的玩家
     */
    private void matchAllTeam() {

        ConcurrentHashMap<Long, MatchTeam> matchTeams = Manager.teamManager.getMatchTeams();
        Iterator<MatchTeam> iterator = matchTeams.values().iterator();
        while (iterator.hasNext()) {
            MatchTeam match = iterator.next();
            if (match.isFinish()) {
                iterator.remove();
                continue;
            }
            TeamInfo team = Manager.teamManager.getTeam(match.getTeamId());
            if (team == null || team.isFull() || team.getMembers().isEmpty()) {
                match.setFinish(true);
                continue;
            }
            match.setNum(team.getMembers().size());

            Player teamPlayer = Manager.playerManager.getPlayerCache(team.getLeaderId());
            if (match.getMatchOverTime() < TimeUtils.Time() / 1000) {
                match.setFinish(true);
                team.getMembers().stream().map(n -> Manager.playerManager.getPlayerCache(n))
                        .forEach(n -> sendMatchAll(n, false));
                continue;
            }

            List<MatchTeam> matchList = matchTeams.values().stream().filter(
                    n -> !n.isFinish() &&
                            n.getTeamId() != match.getTeamId() &&
                            n.getTargetId() == match.getTargetId() &&
                            n.getMatchOverTime() > TimeUtils.Time() / 1000
            ).sorted(Comparator.comparingInt(MatchTeam::getNum)).collect(Collectors.toList());

            for (MatchTeam matchTeam : matchList) {
                TeamInfo otherTeam = Manager.teamManager.getTeam(matchTeam.getTeamId());
                if (otherTeam == null || otherTeam.isFull() || otherTeam.getMembers().isEmpty()) {
                    match.setFinish(true);
                    continue;
                }
                matchTeam.setNum(otherTeam.getMembers().size());
                if (match.getNum() + matchTeam.getNum() > Global.TeamNum) {
                    continue;
                }
                long mergeTeamId = match.getNum() > matchTeam.getNum() ? match.getTeamId() : matchTeam.getTeamId();
                if (mergeTeamId == team.getTeamId()) {
                    otherTeam.getMembers().stream()
                            .map(n -> Manager.playerManager.getPlayerCache(n))
                            .filter(Objects::nonNull)
                            .forEach(team::addMember);
                    Manager.teamManager.getTeams().remove(matchTeam.getTeamId());
                } else {
                    team.getMembers().stream()
                            .map(n -> Manager.playerManager.getPlayerCache(n))
                            .filter(Objects::nonNull)
                            .forEach(otherTeam::addMember);
                    Manager.teamManager.getTeams().remove(match.getTeamId());
                }
                Player otherTeamPlayer = Manager.playerManager.getPlayerCache(otherTeam.getLeaderId());
                long leaderId = teamPlayer.getLevel() > otherTeamPlayer.getLevel() ? teamPlayer.getId() : otherTeam.getLeaderId();

                //匹配成功消息
                TeamInfo mergeTeam = Manager.teamManager.getTeam(mergeTeamId);
                ResMatchAll.Builder msg = ResMatchAll.newBuilder();
                msg.setSuccess(true);
                sendMessage(mergeTeam,ResMatchAll.MsgID.eMsgID_VALUE, msg.build().toByteArray());

                //同步新队伍消息
                mergeTeam.setLeaderId(leaderId);
                ResTeamInfo.Builder builder = buildTeamInfo(mergeTeam);
                sendMessage(mergeTeam, ResTeamInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
                break;
            }

        }
    }

    private void sendMatchAll(Player player, boolean isSuccess) {
        ResMatchAll.Builder msg = ResMatchAll.newBuilder();
        msg.setSuccess(isSuccess);
        MessageUtils.send_to_player(player, ResMatchAll.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void playerOnLine(Player player) {
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if (team == null || !team.getMembers().contains(player.getId())) {
            player.setTeamId(0);
            return;
        }

        ResTeamInfo.Builder teamInfo = buildTeamInfo(team);
        MessageUtils.send_to_player(player, ResTeamInfo.MsgID.eMsgID_VALUE, teamInfo.build().toByteArray());

        ResUpdateTeamMemberInfo.Builder msg = ResUpdateTeamMemberInfo.newBuilder();
        msg.setMember(buildTeamMember(team, player));
        sendMessage(team, ResUpdateTeamMemberInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        checkTeamBuff(team);
    }

    @Override
    public void playerOffLine(Player player) {
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if (team == null) {
            cleanTeamBuff(player);
            return;
        }

        ResUpdateTeamMemberInfo.Builder msg = ResUpdateTeamMemberInfo.newBuilder();
        msg.setMember(buildTeamMember(team, player));
        sendMessage(team, ResUpdateTeamMemberInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        checkTeamBuff(team);
    }

    @Override
    public boolean OnQuitTeam(Player player) {

        TeamInfo team = Manager.teamManager.getTeams().get(player.getTeamId());
        if (team == null) {
            return false;
        }
        player.setTeamId(0);
        cleanTeamBuff(player);

        //发送离开队伍消息
        ResDeleteTeamMember.Builder delete = ResDeleteTeamMember.newBuilder();
        delete.setRoleId(player.getId());
        MessageUtils.send_to_player(player, ResDeleteTeamMember.MsgID.eMsgID_VALUE, delete.build().toByteArray());

        if (team.getLeaderId() == player.getId() && Manager.teamManager.getMatchTeams().containsKey(team.getTeamId())) {
            sendMatchAll(player, false);
        }

        OnQuitTeam(team, player.getId());
        Manager.worldHelpManager.getScript().clearTask(player.getId());
        return true;
    }

    //退出队伍
    private void OnQuitTeam(TeamInfo team, long playerId) {

        if (!team.getMembers().contains(playerId)) {
            return;
        }
        team.removeMember(playerId);
        checkTeamBuff(team);

        //检测所有成员状态
        Player member;
        if (isAllMemberOffline(team)) {
            Manager.teamManager.getTeams().remove(team.getTeamId());
            for (long roleId : team.getMembers()) {
                member = Manager.playerManager.getPlayerCache(roleId);
                if (member == null) {
                    continue;
                }
                //移除队伍成员的buff
                cleanTeamBuff(member);
            }
            return;
        }

        //通知队员我离开
        ResDeleteTeamMember.Builder delete = ResDeleteTeamMember.newBuilder();
        delete.setRoleId(playerId);
        Player player = Manager.playerManager.getPlayerCache(playerId);
        Manager.playerHookManager.deal().checkPlayerRateChange(player, PlayerHookManager.TeamRate);
        for (long roleId : team.getMembers()) {
            member = Manager.playerManager.getPlayerCache(roleId);
            if (member == null) {
                continue;
            }
            MessageUtils.notify_chat_player(member, ChatChannel.CHATCHANNEL_TEAM, MessageString.LeaveTeamNews, player.getName());
            MessageUtils.send_to_player(member, ResDeleteTeamMember.MsgID.eMsgID_VALUE, delete.build().toByteArray());
        }

        //如果是队长，则移交给第一个在线的队员
        if (team.getLeaderId() != playerId) {
            return;
        }
        Player firstOnline = getFirstOnline(team);
        if (firstOnline == null) {
            Manager.teamManager.getTeams().remove(team.getTeamId());
            return;
        }
        team.setLeaderId(firstOnline.getId());
        //通知新队长信息
        ResUpdateTeamMemberInfo.Builder leader = ResUpdateTeamMemberInfo.newBuilder();
        leader.setMember(buildTeamMember(team, firstOnline));
        sendMessage(team, ResUpdateTeamMemberInfo.MsgID.eMsgID_VALUE, leader.build().toByteArray());
    }


    @Override
    public void updateTeamInfoToLeader(long teamId) {
        TeamInfo team = Manager.teamManager.getTeam(teamId);
        if (team == null) {
            return;
        }
        ResTeamInfo.Builder msg = buildTeamInfo(team);
        sendMessage(team, ResTeamInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void updateHpAndMapKey(Player player) {
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if (team == null) {
            return;
        }

        ResUpdateHPAndMapKey.Builder update = ResUpdateHPAndMapKey.newBuilder();
        update.setRoleId(player.getId());
        update.setHpPro(player.checkHpPercent());
        update.setMapKey(player.mapKey());
        sendMessage(team, ResUpdateHPAndMapKey.MsgID.eMsgID_VALUE, update.build().toByteArray());
    }

    //获取第一个在线的
    private Player getFirstOnline(TeamInfo team) {
        for (long roleId : team.getMembers()) {
            Player member = Manager.playerManager.getPlayerCache(roleId);
            if (member.isOnline()) {
                return member;
            }
        }
        return null;
    }

    //检测队伍情况
    private boolean isAllMemberOffline(TeamInfo team) {
        for (long roleId : team.getMembers()) {
            Player member = Manager.playerManager.getPlayerCache(roleId);
            if (member == null) {
                continue;
            }
            if (member.isOnline()) {
                return false;
            }
        }
        return true;
    }

    //检查玩家是否为队长
    private boolean checkNotTeamLeader(TeamInfo team, Player player) {
        if (team == null) {
            player.setTeamId(0);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_NoTeamInfo);
            return true;
        }

        if (team.getLeaderId() != player.getId()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_NotLeaderInfo);
            return true;
        }
        return false;
    }

    //检查地图是否能组队
    private boolean checkMapCanNotMakeTeam(Player player) {
        //自己所在地图不存在
        MapObject leaderMap = Manager.mapManager.getMap(player.gainMapId());
        if (leaderMap == null) {
            return true;
        }
        //自己所在地图不能组队
        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(player.gainMapModelId());
        return config.getCan_team() == 0;
    }


    //检查是否能进入副本
    private boolean checkCanNotEnterCloneMap(MapObject map, Player player) {
        if (map.getZoneModelId() == 0) {
            return false;
        }
        try {
            Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getZoneModelId());
            return !Manager.copyMapManager.manager().copyMapCheck(player, bean, 1, player);
        } catch (Exception e) {
            log.error(e, e);
            return true;
        }
    }

    private boolean checkCanNotMakeTeam(Player player, Player target) {

        //跨服中
        if (player.playerCrossData.crossState != CrossState.PCS_LOCAL || target.playerCrossData.crossState != CrossState.PCS_LOCAL) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_PlayerCrossState);
            return true;
        }
        //所在地图不允许组队
        if (checkMapCanNotMakeTeam(player)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GUILD_task_help_fail,player.getName());
            return true;
        }
        //对方地图不允许组队
        if (checkMapCanNotMakeTeam(target)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GUILD_task_help_fail,target.getName());
            return true;
        }
        return false;
    }

    //向队员发送召集
    private void callMemberToLeader(List<Player> playerList, TeamInfo team, String mapName, Player leader) {
        TeamCall call = new TeamCall();
        call.setMapID(leader.gainMapId());
        call.setLine(leader.gainLine());
        call.setPos(leader.gainCurPos());
        call.setId(team.getCallId());
        team.getCalls().add(call);

        //发送召集消息
        ResCallAllMemberRes.Builder msg = ResCallAllMemberRes.newBuilder();
        msg.setCallId(call.getId());
        msg.setName(mapName);
        msg.setX(call.getPos().getX());
        msg.setY(call.getPos().getY());

        for (Player player : playerList) {
            MessageUtils.send_to_player(player, ResCallAllMemberRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    //广播消息
    private void sendMessage(TeamInfo team, int msgID, byte[] bytes) {
        for (long roleId : team.getMembers()) {
            MessageUtils.send_to_player(roleId, msgID, bytes);
        }
    }

    //队伍成员信息
    private TeamMember.Builder buildTeamMember(TeamInfo team, Player player) {
        TeamMember.Builder msg = TeamMember.newBuilder();
        msg.setRoleId(player.getId());
        msg.setName(player.getName());
        msg.setLevel(player.getLevel());
        msg.setCareer(player.getCareer());
        msg.setPower(player.getFightPoint());
        msg.setIsLeader(player.getId() == team.getLeaderId());
        msg.setIsOnline(player.isOnline());
        msg.setStateLv(player.getStateVip().getLv());
        msg.setHpPro(player.checkHpPercent());
        msg.setMapKey(player.mapKey());
        msg.setInchat(Manager.chatManager.deal().inChatRoom(team.getTeamId(), player.getId()));
        msg.setFacade(MapUtils.getFacade(player));
        //@todo 头像修改
//        msg.setFashionHead(player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID());
//        msg.setFashionFrame( player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID()) ;

        msg.setHead(MapUtils.getHead(player));
        return msg;
    }

    //队伍信息
    private ResTeamInfo.Builder buildTeamInfo(TeamInfo team) {
        ResTeamInfo.Builder msg = ResTeamInfo.newBuilder();
        msg.setTeamId(team.getTeamId());
        msg.setType(team.getType());
        msg.setAutoAccept(team.isAutoAccept());
        for (long roleId : team.getMembers()) {
            Player member = Manager.playerManager.getPlayerCache(roleId);
            if (member == null) {
                continue;
            }
            msg.addMembers(buildTeamMember(team, member));
        }
        return msg;
    }

    //玩家信息
    private Freedomer.Builder buildFreedom(Player player, int honey) {
        Freedomer.Builder free = Freedomer.newBuilder();
        free.setRoleId(player.getId());
        free.setLevel(player.getLevel());
        free.setName(player.getName());
        free.setCareer(player.getCareer());
        free.setHoney(honey);
        free.setPower(player.getFightPoint());
        Guild guild = Manager.guildsManager.GetGuildByPlayer(player);
        free.setGuildName(guild == null ? "" : guild.getName());
        free.setMoonandOver(player.moonandOverCard());
        //@todo 头像修改
//        free.setFashionHead(player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_TYPE).getFashionID());
//        free.setFashionFrame( player.getNewFashionData().getWearDatas().get(NewFashionManager.HEAD_FRAME_TYPE).getFashionID()) ;

        free.setHead(MapUtils.getHead(player));

        return free;
    }

    //公告组队消息
    private void notice(TeamInfo team) {
        Player leader = Manager.playerManager.getPlayerOnline(team.getLeaderId());
        Cfg_Clone_map_Bean bean = Cfg_Clone_map_Container.GetInstance().getValueByKey(team.getType());
        if (bean == null) {
            log.error("Cfg_Clone_map_Bean配置表不存在：" + team.getType());
            return;
        }
        String cloneName = ServerStr.getChatTableName(bean.getDuplicate_name());
        MessageUtils.notify_Chat_To_AllPlayer(leader, ChatChannel.CHATCHANNEL_TEAMHELP, MessageString.TEAM_CALL_WORLD_NOTICE,
                team.getTeamId() + "", leader.getName(), cloneName, team.getMembers().size() + "", Global.TeamNum + "");
    }

    /**
     * 检查队伍buff
     */
    @Override
    public void checkTeamBuff(TeamInfo team) {
        if (team == null) {
            return;
        }
        Player player;
        for (long roleId : team.getMembers()) {
            player = Manager.playerManager.getPlayerOnline(roleId);
            if (player == null) {
                continue;
            }
            //如果队员还在切图中，属性改变消息不会生效。等切图完成再计算
            if (EntityState.ChangeMap.compare(player.getState())) {
                continue;
            }
            Manager.playerHookManager.deal().checkPlayerRateChange(player, PlayerHookManager.TeamRate);
        }

        Manager.chumManager.getScript().checkChumBuff(team);
    }

    /**
     * 检查玩家的buff
     */
    @Override
    public void checkTeamBuff(Player player) {
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if (team == null) {
            cleanTeamBuff(player);
            return;
        }
        checkTeamBuff(team);
    }

    /**
     * 解散队伍移除队员的buff
     */
    private void cleanTeamBuff(Player player) {

        Manager.chumManager.getScript().removeChumBuff(player);
    }

    /**
     * 通知队长开船
     */
    @Override
    public void onReqTeamLeaderOpenState(Player player, ReqTeamLeaderOpenState messInfo) {
        long teamId = player.getTeamId();
        if (teamId < 1) {
            sendOpenState(player, 2, 0);
            return;
        }

        TeamInfo ti = Manager.teamManager.getTeam(teamId);
        if (ti == null) {
            sendOpenState(player, 2, 0);
            return;
        }

        if (player.getId() == ti.getLeaderId()) {
            sendOpenState(player, 3, ti.getLeaderId());
            return;
        }
        Player pp = Manager.playerManager.getPlayerCache(ti.getLeaderId());
        if (pp != null) {
            sendOpenState(pp, 0, ti.getLeaderId());
        }
        sendOpenState(player, 0, ti.getLeaderId());
    }

    private void sendOpenState(Player player, int state, long leaderId) {
        ResTeamLeaderOpenState.Builder msg = ResTeamLeaderOpenState.newBuilder();
        msg.setLeaderId(leaderId);
        msg.setState(state);
        MessageUtils.send_to_player(player, ResTeamLeaderOpenState.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public TeamInfo createTeam(Player player, int type, boolean auto){
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Team)) {
            return null;
        }
        //玩家当前所在地图是否支持组队
        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(player.gainMapModelId());
        if (config.getCan_team() == 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TEAMMAPNOTCREATT);
            return null;
        }

        //是否在跨服中
        if ( player.playerCrossData.isToFightServer() || player.playerCrossData.isReqFight) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.INCROSSNOTTEAM);
            return null;
        }

        //已有队伍
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if (team != null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Team_TargetHaveTeamInfo);
            return null;
        }

        //创建队伍
        team = new TeamInfo();
        team.setTeamId(IDConfigUtil.getLogId());
        team.setType(type);
        team.setAutoAccept(auto);
        team.setLeaderId(player.getId());
        team.addMember(player);
        Manager.teamManager.getTeams().put(team.getTeamId(), team);

        //重置世界喊话冷却时间
        Manager.cooldownManager.removeCooldown(player, CooldownTypes.TEAM_NOTICE_CD, null);

        //返回创建的队伍消息
        ResTeamInfo.Builder msg = buildTeamInfo(team);
        MessageUtils.send_to_player(player, ResTeamInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        return team;
    }
    /**
     * 获取队伍成员所在的地图线
     */
    @Override
    public int gainTeamMapLine(Player player,int mapID)
    {
        TeamInfo team = Manager.teamManager.getTeam(player.getTeamId());
        if(team != null){
            for (long otherId : team.getMembers()) {
                if (otherId == player.getId()) {
                    continue;
                }
                Player other = Manager.playerManager.getPlayerOnline(otherId);
                if (other == null) {
                    continue;
                }
                if(other.getCurGps().getModelId() == mapID){
                    return other.getCurGps().getLine();
                }
            }
        }
        return 0;
    }
}
