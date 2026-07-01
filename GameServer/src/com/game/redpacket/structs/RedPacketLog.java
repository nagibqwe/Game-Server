/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.redpacket.structs;

import java.util.Date;

/**
 * 红包日志
 * @author gouzhongliang
 */
public class RedPacketLog {

    private long roleId;//角色ID
    private int reason;//首充类型
    private int value;//充值金额
    private long sendtime;//发送时间
    private int itemType;//货币类型

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public long getSendtime() {
        return sendtime;
    }

    public void setSendtime(long sendtime) {
        this.sendtime = sendtime;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
