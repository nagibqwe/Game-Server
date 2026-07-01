package com.game.copymap.structs;

import java.util.HashMap;

/**
 * @Desc TODO
 * @Date 2021/11/5 16:45
 * @Auth ZUncle
 */
public class SuitCopyData extends ZoneCache {

    //天罚值
    HashMap<Long , Integer> limit  = new HashMap<>();
    //踢出玩家
    HashMap<Long , Long> tickOut  = new HashMap<>();

    public HashMap<Long, Long> getTickOut() {
        return tickOut;
    }

    public void setTickOut(HashMap<Long, Long> tickOut) {
        this.tickOut = tickOut;
    }

    public HashMap<Long, Integer> getLimit() {
        return limit;
    }

    public void setLimit(HashMap<Long, Integer> limit) {
        this.limit = limit;
    }
}
