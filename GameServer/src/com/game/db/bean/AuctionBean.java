package com.game.db.bean;

import game.core.db.BaseBean;

public class AuctionBean extends BaseBean {
    private long auctionId;

    private String auctionItem;

    private long auctionTime;

    private int auctionPrice;

    private long auctionOwnId;

    private long auctionRoleId;

    private long auctionGuild;

    private String password;

    public long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(long auctionId) {
        this.auctionId = auctionId;
    }

    public String getAuctionItem() {
        return auctionItem;
    }

    public void setAuctionItem(String auctionItem) {
        this.auctionItem = auctionItem;
    }

    public long getAuctionTime() {
        return auctionTime;
    }

    public void setAuctionTime(long auctionTime) {
        this.auctionTime = auctionTime;
    }

    public int getAuctionPrice() {
        return auctionPrice;
    }

    public void setAuctionPrice(int auctionPrice) {
        this.auctionPrice = auctionPrice;
    }

    public long getAuctionOwnId() {
        return auctionOwnId;
    }

    public void setAuctionOwnId(long auctionOwnId) {
        this.auctionOwnId = auctionOwnId;
    }

    public long getAuctionRoleId() {
        return auctionRoleId;
    }

    public void setAuctionRoleId(long auctionRoleId) {
        this.auctionRoleId = auctionRoleId;
    }

    public long getAuctionGuild() {
        return auctionGuild;
    }

    public void setAuctionGuild(long auctionGuild) {
        this.auctionGuild = auctionGuild;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
