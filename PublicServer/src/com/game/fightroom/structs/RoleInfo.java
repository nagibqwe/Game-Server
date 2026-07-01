/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.fightroom.structs;

/**
 * 战场中的玩家简略信息
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class RoleInfo {

    private int sid;//服务器编号
    private String plat;//平台标识
    private long roleId;//角色ID
    private int readyState;//准备状态，用于开始阶段的准备
    private int fightState;//战斗状态（ 成功或者失败）
    private long readyTime;//准备时间，或者报名进入的时间
    private long teamId;//组队ID号
    private int birthGroup;//出生阵营
    //战场中阵营编号 （用以区分敌我双方， 为0就是不区分）
    private int campNo = 0;
    private int f = 0;
    private int car = 0;//职业ID
    private String name="no";//玩家名字
    
    public RoleInfo() {

    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getReadyState() {
        return readyState;
    }

    public void setReadyState(int readyState) {
        this.readyState = readyState;
    }

    /**
     * 战斗状态（ 1 成功 0 失败）
     *
     * @return
     */
    public int getFightState() {
        return fightState;
    }

    /**
     * 战斗状态（ 成功或者失败）
     *
     * @param fightState
     */
    public void setFightState(int fightState) {
        this.fightState = fightState;
    }

    public long getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(long readyTime) {
        this.readyTime = readyTime;
    }

    /**
     * 战场中阵营编号 （用以区分敌我双方， 为0就是不区分）
     *
     * @return
     */
    public int getCampNo() {
        return campNo;
    }

    /**
     * 战场中阵营编号 （用以区分敌我双方， 为0就是不区分）
     *
     * @param campNo 0 为不区分， 其它值为要区分的编号
     */
    public void setCampNo(int campNo) {
        this.campNo = campNo;
    }
    //玩家的战斗力

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getCar() {
        return car;
    }

    public void setCar(int car) {
        this.car = car;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public int getBirthGroup() {
        return birthGroup;
    }

    public void setBirthGroup(int birthGroup) {
        this.birthGroup = birthGroup;
    }
     
}
