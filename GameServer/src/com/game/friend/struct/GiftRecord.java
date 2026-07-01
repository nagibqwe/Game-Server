package com.game.friend.struct;

import com.game.backpack.structs.Item;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;

/**
 * @author gsj
 * @create 2020/7/28 9:54
 */
public class GiftRecord {

    private long id;

    private int type;

    private String sender;

    private String receiver;

    private int itemModelId;

    private int num;

    private int time;

    private int readStatus;

    public GiftRecord() {}

    public GiftRecord(String sender, String receiver, Item item, int type) {
        this.id = IDConfigUtil.getLogId();
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.itemModelId = item.getItemModelId();
        this.num = item.getNum();
        this.time = (int) (TimeUtils.Time() / 1000);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getItemModelId() {
        return itemModelId;
    }

    public void setItemModelId(int itemModelId) {
        this.itemModelId = itemModelId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }
}
