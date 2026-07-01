/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.redpacket.structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 红包数据表
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class RedPacketData {

    private long rpId;//实例ID
    private int type;//频道类型
    private int goldType;//口令类型
    private int maxValue;//总金额
    private int maxNum;//总个数
    private int curNum;//已领个数
    private String notice = "";//说明信息
    private long roleId;//发送的角色ID值
    private int groupId;//出生的阵营ID值
    private long guildId;//公会ID值
    private long createTime;//红包创建时间
    private int kouValue;//扣的元宝值
    /**
     * 玩家红包领取记录
     * key是玩家id，value是玩家领取的红包中的钱数
     * */
    private HashMap<Long, Integer> rolelist = new HashMap<>();
    /**
     * 红包列表，每个值代表红包中的钱的数目，领取一个红包则在列表中删除对应的
     * */
    private List<Integer> values = new ArrayList<>();

    public long getRpId() {
        return rpId;
    }

    public void setRpId(long rpId) {
        this.rpId = rpId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGoldType() {
        return goldType;
    }

    public void setGoldType(int goldType) {
        this.goldType = goldType;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getCurNum() {
        return curNum;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
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

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public int getKouValue() {
        return kouValue;
    }

    public void setKouValue(int kouValue) {
        this.kouValue = kouValue;
    }
   
}
