package com.game.db.bean;

import game.core.db.BaseBean;

/**
 * 登录需要显示的角色信息
 */
public class RoleLoginBean extends BaseBean {

    private long roleId;                //角色唯一id
    private long userId;                // 游戏生成的账号id
    private int serverId;               //所在服务器id
    private String roleName;            // 角色名
    private int lv;                     // 角色等级
    private int career;                 // 职业
    private int deleteTime;               // 0未删除
    private long fight;                 // 战力

    public long getFight() {
        return fight;
    }

    public void setFight(long fight) {
        this.fight = fight;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public int getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(int deleteTime) {
        this.deleteTime = deleteTime;
    }
}
