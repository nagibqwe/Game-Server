package com.game.guildbattle.structs;

import com.game.manager.Manager;
import com.game.player.structs.PlayerWorldInfo;
import game.message.GuildBattleMessage;

/**
 * @Description
 * @auther lw
 * @create 2020-02-11 15:07
 */
public class GuildBattleMember {
    private long roleId;

    private int record;

    private int killNum;

    private int destroyNum;

    private int repairNum;

    private int breakNum;

    private int carrierNum;

    private boolean praise;

    private int rank;

    private int winNum;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    public int getKillNum() {
        return killNum;
    }

    public void setKillNum(int killNum) {
        this.killNum = killNum;
    }

    public int getDestroyNum() {
        return destroyNum;
    }

    public void setDestroyNum(int destroyNum) {
        this.destroyNum = destroyNum;
    }

    public int getRepairNum() {
        return repairNum;
    }

    public void setRepairNum(int repairNum) {
        this.repairNum = repairNum;
    }

    public int getBreakNum() {
        return breakNum;
    }

    public void setBreakNum(int breakNum) {
        this.breakNum = breakNum;
    }

    public int getCarrierNum() {
        return carrierNum;
    }

    public void setCarrierNum(int carrierNum) {
        this.carrierNum = carrierNum;
    }

    public boolean isPraise() {
        return praise;
    }

    public void setPraise(boolean praise) {
        this.praise = praise;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getWinNum() {
        return winNum;
    }

    public void setWinNum(int winNum) {
        this.winNum = winNum;
    }

    public GuildBattleMessage.GuildBattleMember.Builder toGuildBattleMemberMsg() {
        GuildBattleMessage.GuildBattleMember.Builder builder = GuildBattleMessage.GuildBattleMember.newBuilder();
        PlayerWorldInfo info = Manager.playerManager.getPlayerWorldInfo(roleId);
        builder.setName(info.getRolename());
        builder.setBreakNum(breakNum);
        builder.setCareer(info.getCareer());
        builder.setDestroyNum(destroyNum);
        builder.setKillNum(killNum);
        builder.setRecord(record);
        builder.setRepairNum(repairNum);
        return builder;
    }
}
