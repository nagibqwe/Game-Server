package com.game.home.struct;

/**
 * @Desc TODO
 * @Date 2021/7/26 16:39
 * @Auth ZUncle
 */
public class VisitorEvent {
    long roleId;
    long time;
    int gift;       //送礼
    int popularity; //人气

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getGift() {
        return gift;
    }

    public void setGift(int gift) {
        this.gift = gift;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
}
