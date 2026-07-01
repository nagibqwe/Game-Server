package com.game.auction.structs;

/**
 * @Description
 * @auther lw
 * @create 2019-10-10 19:43
 */
public class AuctionRecord {

    private int itemId;

    private long time;

    private int price;

    private int num;

    //0购买 1卖出
    private int type;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
