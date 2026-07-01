package com.game.guild.structs;

import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import game.message.CommonMessage;

import java.util.HashMap;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/7/13 15:24
 * @Auth ZUncle
 */
public class GuildGift {

    long id;
    long sender;         //发送者
    int giftId;
    long create;         //创建时间
    long timeout;        //过期时间
    HashMap<Long, Integer> notes = new HashMap<>();                             //领取记录
    HashMap<Long, List<ReadIntegerArray>> history = new HashMap<>();          //领奖记录

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public long getCreate() {
        return create;
    }

    public void setCreate(long create) {
        this.create = create;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public HashMap<Long, Integer> getNotes() {
        return notes;
    }

    public void setNotes(HashMap<Long, Integer> notes) {
        this.notes = notes;
    }

    public HashMap<Long, List<ReadIntegerArray>> getHistory() {
        return history;
    }

    public void setHistory(HashMap<Long, List<ReadIntegerArray>> history) {
        this.history = history;
    }
}
