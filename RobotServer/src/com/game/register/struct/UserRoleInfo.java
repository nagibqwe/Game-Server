/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.register.struct;

import com.game.db.bean.roleBean;
import com.game.player.structs.Player;

/**
 * @author
 */
public class UserRoleInfo {

    private long roleId;//角色id
    private int csId;//创建服id
    private int deleteTime;//删除时间，0未删除
    private boolean onLine = false;//是否在线

    private int level; //角色等级

    public static UserRoleInfo roleBeanToInfo(roleBean bean) {
        UserRoleInfo info = new UserRoleInfo();
        info.setCsId(bean.getServerId());
        info.setDeleteTime(bean.getDeleteTime());
        info.setRoleId(bean.getRoleid());
        info.setLevel(bean.getLv());
        return info;
    }

    public static UserRoleInfo initCreatePlayerToInfo(Player player) {
        UserRoleInfo info = new UserRoleInfo();
        info.setDeleteTime(0);
        info.setRoleId(player.getId());
        info.setLevel(player.getLevel());
        return info;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getCsId() {
        return csId;
    }

    public void setCsId(int csId) {
        this.csId = csId;
    }

    public int getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(int deleteTime) {
        this.deleteTime = deleteTime;
    }

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
