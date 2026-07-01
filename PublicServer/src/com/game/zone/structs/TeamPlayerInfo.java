package com.game.zone.structs;

/**
 *组队的成员信息
 * @author xuchangming <xysoko@qq.com>
 */
public class TeamPlayerInfo {
    private long roleId ;
    private String name;
    private boolean leader;
    private int career;
    private boolean ready;//近跨服的时候是没有玩家准备好的
    private int serverId;//玩家的ServerId
    private int lv;
    private long f;
    private boolean robot = false;
    private int campNo = 0;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public long getF() {
        return f;
    }

    public void setF(long f) {
        this.f = f;
    }

    public boolean isRobot() {
        return robot;
    }

    public void setRobot(boolean robot) {
        this.robot = robot;
    }

    public void  setCampNo(int campNo){this.campNo = campNo;}

    public int getCampNo(){return campNo;}


    

    @Override
    public String toString() {
        return "TeamPlayerInfo{" + "roleId=" + roleId + ", name=" + name + ", leader=" + leader + ", career=" + career + ", ready=" + ready + ", serverId=" + serverId + '}';
    }

    
}
