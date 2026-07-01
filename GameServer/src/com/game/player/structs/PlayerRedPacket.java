package com.game.player.structs;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerRedPacket {
    private ConcurrentHashMap<Integer, Boolean> redpacket = new ConcurrentHashMap<>();//x红包领取过的记录
    private ConcurrentHashMap<Integer, Boolean> dayredpacket = new ConcurrentHashMap<>();//x红包领取过的记录

    public ConcurrentHashMap<Integer, Boolean> getRedpacket() {
        return redpacket;
    }

    public void setRedpacket(ConcurrentHashMap<Integer, Boolean> redpacket) {
        this.redpacket = redpacket;
    }

    public ConcurrentHashMap<Integer, Boolean> getDayredpacket() {
        return dayredpacket;
    }

    public void setDayredpacket(ConcurrentHashMap<Integer, Boolean> dayredpacket) {
        this.dayredpacket = dayredpacket;
    }

}
