package com.game.gold.structs;

public class Gold {

    //角色id
    private long userId;   ///策划要求充值元宝不在共享，所以这个ID，将用 ROLEID 来代替
    //服务器id
    private int serverId;
    //渠道名
    private String platformName;
    //总充值获得元宝数量
    private int rechargeGold;
    //剩余元宝数量
    private int reaminGold;
    //非交易元宝消耗
    private int costGold;
    //交易增加元宝
    private int tradeAddGold;
    //交易减少元宝
    private int tradeCostGold;


    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public int getRechargeGold() {
        return rechargeGold;
    }

    public void setRechargeGold(int rechargeGold) {
        this.rechargeGold = rechargeGold;
    }

    public int getReaminGold() {
        return reaminGold;
    }

    public void setReaminGold(int reaminGold) {
        this.reaminGold = reaminGold;
    }

    public int getCostGold() {
        return costGold;
    }

    public void setCostGold(int costGold) {
        this.costGold = costGold;
    }

    public int getTradeAddGold() {
        return tradeAddGold;
    }

    public void setTradeAddGold(int tradeAddGold) {
        this.tradeAddGold = tradeAddGold;
    }

    public int getTradeCostGold() {
        return tradeCostGold;
    }

    public void setTradeCostGold(int tradeCostGold) {
        this.tradeCostGold = tradeCostGold;
    }

}
