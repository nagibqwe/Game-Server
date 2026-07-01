/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.team.structs;

import com.data.Global;
import com.game.player.structs.Player;
import com.game.utils.RandomUtils;
import game.core.util.TimeUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TeamInfo {

    private int callId = RandomUtils.random(10000);

    private long teamId;                //队伍Id
    private long leaderId;              //队长
    private int type = -1;              //队伍类型
    private boolean autoAccept = true;  //自动接受队伍申请

    private final ConcurrentHashMap<Long, Long> members = new ConcurrentHashMap<>();        //成员列表
    private final List<TeamCall> calls = new ArrayList<>();                                 //召唤信息
    private final List<Long> Invites = new ArrayList<>();                                   //邀请列表
    private final ConcurrentHashMap<Long, Long> applies = new ConcurrentHashMap<>();        //申请列表
    private final ConcurrentHashMap<Long, Long> refuses = new ConcurrentHashMap<>();        //拒绝列表


    public int getCallId() {
        callId = callId + 1;
        return callId;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(long leaderId) {
        this.leaderId = leaderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAutoAccept() {
        return autoAccept;
    }

    public void setAutoAccept(boolean autoAccept) {
        this.autoAccept = autoAccept;
    }

    public List<Long> getMembers() {
        return new ArrayList<>(members.keySet());
    }

    public void addMember(Player player){
        members.put(player.getId(), TimeUtils.Time());
        player.setTeamId(teamId);
    }

    public void removeMember(long uid){
        members.remove(uid);
    }

    public List<TeamCall> getCalls() {
        return calls;
    }

    public TeamCall getCall(int callId) {
        for (TeamCall call : calls) {
            if (call.getId() == callId) {
                return call;
            }
        }
        return null;
    }

    public ConcurrentHashMap<Long, Long> getApplies() {
        return applies;
    }

    public ConcurrentHashMap<Long, Long> getRefuses() {
        return refuses;
    }

    public List<Long> getInvites() {
        return Invites;
    }

    public boolean isFull() {
        return members.size() >= Global.TeamNum;
    }
}
