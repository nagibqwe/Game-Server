package com.game.zone.structs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zcd on 2018/2/6.
 */
public class BravePeakInfo {

    /**
     * 记录玩家最大打到了多少层
     */
    private Map<Long, BravePeakPlayerProcessInfo> maxFloor = new ConcurrentHashMap<>(64);


    /**
     * 最后一次刷新的时间
     */
    private long lastModifyTime;

    public Map<Long, BravePeakPlayerProcessInfo> getMaxFloor() {
        return maxFloor;
    }

    public void setMaxFloor(Map<Long, BravePeakPlayerProcessInfo> maxFloor) {
        this.maxFloor = maxFloor;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    /**
     * 数据刷新为后期的活动做准备
     */
    public void clear(){
        maxFloor.clear();
    }
}
