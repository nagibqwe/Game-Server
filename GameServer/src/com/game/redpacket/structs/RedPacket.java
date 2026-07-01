package com.game.redpacket.structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 红包
 * @Auther: gouzhongliang
 * @Date: 2021/11/23 9:22
 */
public class RedPacket {

    public final static int TYPE_SYSTEM = 1;
    public final static int TYPE_PLAYER = 2;
    /**ID*/
    private long id;
    /**创建时间*/
    private long createTime;
    /**发放时间*/
    private long sendTime;
    /**红包类型 1系统 2玩家*/
    private int type;
    /**红包来源 来自配置表*/
    private int reason;
    /**系统红包配置id*/
    private int configId;
    /**发送的角色ID值*/
    private long roleId;
    /**总金额*/
    private int totalMoney;
    /**总数量*/
    private int totalNum;
    /**留言*/
    private String notice;
    /**发送货币类型（某些发送领取不一致的情况）*/
    private int sendItemType;
    /**领取货币类型*/
    private int itemType;
    /**仙盟id*/
    private long guildId;
    /**
     * 玩家红包领取记录
     * key是玩家id，value是玩家领取的红包中的钱数
     * */
    private HashMap<Long, Integer> rolelist = new HashMap<>();
    /**
     * 红包列表，每个值代表红包中的钱的数目，领取一个红包则在列表中删除对应的
     * */
    private List<Integer> values = new ArrayList<>();

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

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public HashMap<Long, Integer> getRolelist() {
        return rolelist;
    }

    public void setRolelist(HashMap<Long, Integer> rolelist) {
        this.rolelist = rolelist;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public int getSendItemType() {
        return sendItemType;
    }

    public void setSendItemType(int sendItemType) {
        this.sendItemType = sendItemType;
    }

    public int getConfigId() {
        return configId;
    }

    public void setConfigId(int configId) {
        this.configId = configId;
    }

}
