/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.worker;

import io.netty.channel.ChannelHandlerContext;
import java.util.List;

/**
 * 战斗服的消息体
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class FightSMessage {

    private final List<Long> roleids;
    private final int msgid;
    private final byte[] mess;
    private final long srcId;
    private final int type;

    private final ChannelHandlerContext session;

    public FightSMessage(List<Long> roleids, int msgid, byte[] mess, long srcId, ChannelHandlerContext session, int dealType) {
        this.roleids = roleids;
        this.msgid = msgid;
        this.mess = mess;
        this.srcId = srcId;
        this.session = session;
        this.type = dealType;
    }

    public int getType() {
        return type;
    }

    public int getLengthWithRole() {
        if (roleids == null) {
            return mess.length + Integer.SIZE / Byte.SIZE + Long.SIZE / Byte.SIZE + Integer.SIZE / Byte.SIZE + Integer.SIZE / Byte.SIZE;
        }
        return mess.length + Integer.SIZE / Byte.SIZE + Long.SIZE / Byte.SIZE + Integer.SIZE / Byte.SIZE + roleids.size() * Long.SIZE / Byte.SIZE + Integer.SIZE / Byte.SIZE;
    }

    public List<Long> getRoleids() {
        return roleids;
    }

    public int getMsgid() {
        return msgid;
    }

    public byte[] getMess() {
        return mess;
    }

    public long getSrcId() {
        return srcId;
    }

    public ChannelHandlerContext getSession() {
        return session;
    }

}
