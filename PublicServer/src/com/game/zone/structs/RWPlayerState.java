/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.zone.structs;

/**
 * 资源争夺战的报名信息表
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class RWPlayerState implements Comparable<RWPlayerState> {

    private int sid;
    private long roleId;
    private String roleName;
    private long baoMingTime;
    private int level = 0;
    private long fightID = 0;
    private int campNo = 0;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public long getBaoMingTime() {
        return baoMingTime;
    }

    public void setBaoMingTime(long baoMingTime) {
        this.baoMingTime = baoMingTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getFightID() {
        return fightID;
    }

    public void setFightID(long fightID) {
        this.fightID = fightID;
    }

    public int getCampNo() {
        return campNo;
    }

    public void setCampNo(int campNo) {
        this.campNo = campNo;
    }
   
    @Override
    public int compareTo(RWPlayerState o) {
        if (level > o.getLevel()) {
            return 1;
        }

        if (level == o.getLevel()) {
            return 0;
        }

        return -1;
    }

}
