/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.messageEvent;

import game.core.message.RMessage;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class OtherToGameMessage {

    private List<Long> roleIds;
    private int msgId;
    private RMessage msg;
    private byte[] data;
    private final int type;
    private ChannelHandlerContext session;
    private final long fromId;

    public OtherToGameMessage(RMessage msg, ChannelHandlerContext session, int dealType, long srcId) {
        this.msg = msg;
        this.session = session;
        this.type = dealType;
        this.fromId = srcId;
    }

    public OtherToGameMessage(List<Long> roleIds, int msgId, byte[] data, int dealType, long srcId) {
        this.roleIds = roleIds;
        this.msgId = msgId;
        this.data = data;
        this.type = dealType;
        this.fromId = srcId;
    }

    public long getFromId() {
        return fromId;
    }
    
    public int getType() {
        return type;
    }

    public ChannelHandlerContext getSession() {
        return session;
    }

    public void setSession(ChannelHandlerContext session) {
        this.session = session;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public RMessage getMsg() {
        return msg;
    }

    public void setMsg(RMessage msg) {
        this.msg = msg;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
