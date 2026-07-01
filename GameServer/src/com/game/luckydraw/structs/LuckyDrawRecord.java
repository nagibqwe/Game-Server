package com.game.luckydraw.structs;

/**
 * @author gaozhaoguang
 * @desc LuckyDrawRecord
 * @date Created on 2020/8/20 16:31
 **/
public class LuckyDrawRecord {
    /**
     * 抽奖时间
     */
    private long time;
    /**
     * 奖品等级 0:特等,1:一等奖,2:二等奖,3:三等奖
     */
    private int awardType;
    /**
     * 抽奖的角色
     */
    private String roleName;
    /**
     * 奖品ID
     */
    private int itemModelID;
    /**
     * 奖品数量
     */
    private int num;
    /**
     * 奖品是否绑定
     */
    private boolean bind;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getItemModelID() {
        return itemModelID;
    }

    public void setItemModelID(int itemModelID) {
        this.itemModelID = itemModelID;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }
}
