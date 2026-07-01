package com.game.recharge.structs;

/**
 * @Desc TODO 超值折扣
 * @Date 2020/8/7 14:40
 * @Auth ZUncle
 */
public class RechargeDiscount {

    int id;         //折扣充值商品ID
    int count;      //折扣商品已购次数
    long timeout;   //过期时间
    boolean delay;  //是否已经延期

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isDelay() {
        return delay;
    }

    public void setDelay(boolean delay) {
        this.delay = delay;
    }
}
