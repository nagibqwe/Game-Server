/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.team.script;

import com.game.player.structs.Player;
import com.game.team.structs.TeamInfo;
import game.core.script.IScript;
import game.message.TeamMessage;
import game.message.TeamMessage.ReqTeamLeaderOpenState;

public interface ITeamHandler extends IScript {

    void reqAlterTeamHandler(Player player, TeamMessage.ReqAlterTeam messInfo);

    void reqApplyEnterHandler(Player player, TeamMessage.ReqApplyEnter messInfo);

    void reqApplyOptHandler(Player player, TeamMessage.ReqApplyOpt messInfo);

    void reqCallAllMemberHandler(Player player, TeamMessage.ReqCallAllMember messInfo);

    void reqCreateTeamHandler(Player player, int type, boolean auto);

    void reqGetApplyListHandler(Player player, TeamMessage.ReqGetApplyList messInfo);

    void reqGetFreedomListHandler(Player player, TeamMessage.ReqGetFreedomList messInfo);

    void reqGetWaitListHandler(Player player, TeamMessage.ReqGetWaitList messInfo);

    void reqInviteHandler(Player player, TeamMessage.ReqInvite messInfo);

    void reqInviteResHandler(Player player, TeamMessage.ReqInviteRes messInfo);

    void reqTeamOptHandler(Player player, TeamMessage.ReqTeamOpt messInfo);

    void reqTransport2LeaderHandler(Player player, TeamMessage.ReqTransport2Leader messInfo);

    void reqCleanApplyListHandler(Player player, TeamMessage.ReqCleanApplyList messInfo);

    void reqMatchAllHandler(Player player, TeamMessage.ReqMatchAll messInfo);

    void reqAgreeCallHandler(Player player, TeamMessage.ReqAgreeCall messInfo);

    void reqGetTeamInfoHandler(Player player, TeamMessage.ReqGetTeamInfo messInfo);

    void onReqTeamLeaderOpenState(Player player, ReqTeamLeaderOpenState messInfo);

    void checkTeamBuff(Player player);

    void checkTeamBuff(TeamInfo team);

    void tick();

    void playerOnLine(Player player);

    void playerOffLine(Player player);

    boolean OnQuitTeam(Player player);

    void updateTeamInfoToLeader(long teamId);

    void updateHpAndMapKey(Player player);

    TeamInfo createTeam(Player player, int type, boolean auto);

    int gainTeamMapLine(Player player,int mapID);

}
