package com.game.openserverac.structs;


import java.util.ArrayList;
import java.util.List;

/**
 * vip 申请人实体
 */
public class V4HelpBean {
    public void setBeHelperPlayerId(long beHelperPlayerId) {
        this.beHelperPlayerId = beHelperPlayerId;
    }

    public void setHelperPlayerId(long helperPlayerId) {
        this.helperPlayerId = helperPlayerId;
    }

    public void setHelpTime(int helpTime) {
        this.helpTime = helpTime;
    }

    public long getBeHelperPlayerId() {
        return beHelperPlayerId;
    }

    public long getHelperPlayerId() {
        return helperPlayerId;
    }

    public int getHelpTime() {
        return helpTime;
    }

    /**
     * 申请人id 即被投资人
     */
    private long beHelperPlayerId;

    public List<Integer> getBeHelperAwardState() {
        return beHelperAwardState;
    }

    public void setBeHelperAwardState(List<Integer> beHelperAwardState) {
        this.beHelperAwardState = beHelperAwardState;
    }

    public List<Integer> getHelperAwardState() {
        return helperAwardState;
    }

    public void setHelperAwardState(List<Integer> helperAwardState) {
        this.helperAwardState = helperAwardState;
    }

    /**
     * 被投资人 领取状态
     */
    private List<Integer> beHelperAwardState = new ArrayList<>();
    /**
     * 投资人
     */
    private long helperPlayerId;
    /**
     * 投资人 领奖状态
     */
    private List<Integer> helperAwardState = new ArrayList<>();
    /**
     * 投资时间 单位秒
     */
    private int helpTime;

}
