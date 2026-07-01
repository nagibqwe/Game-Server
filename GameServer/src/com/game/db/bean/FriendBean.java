/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.bean;

import game.core.db.BaseBean;

public class FriendBean extends BaseBean {

    private Long roleId;

    private String latelyPlayers;

    private String friends;

    private String enemies;

    private String shields;

    private String sendLogs;

    private String receiveLogs;

    private String approvalList;

    private String shieldAddFriend;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getLatelyPlayers() {
        return latelyPlayers;
    }

    public void setLatelyPlayers(String latelyPlayers) {
        this.latelyPlayers = latelyPlayers;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getEnemies() {
        return enemies;
    }

    public void setEnemies(String enemies) {
        this.enemies = enemies;
    }

    public String getShields() {
        return shields;
    }

    public void setShields(String shields) {
        this.shields = shields;
    }

    public String getReceiveLogs() {
        return receiveLogs;
    }

    public String getSendLogs() {
        return sendLogs;
    }

    public void setReceiveLogs(String receiveLogs) {
        this.receiveLogs = receiveLogs;
    }

    public void setSendLogs(String sendLogs) {
        this.sendLogs = sendLogs;
    }

    public String getApprovalList() {
        return approvalList;
    }

    public void setApprovalList(String approvalList) {
        this.approvalList = approvalList;
    }

    public String getShieldAddFriend() {
        return shieldAddFriend;
    }

    public void setShieldAddFriend(String shieldAddFriend) {
        this.shieldAddFriend = shieldAddFriend;
    }
}
