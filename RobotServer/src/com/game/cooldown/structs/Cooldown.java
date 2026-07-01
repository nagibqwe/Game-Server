/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.cooldown.structs;

import com.game.map.structs.BaseNpc;
import game.core.pool.MemoryObject;
import game.core.util.TimeUtils;

/**
 * @author Administrator
 */
public class Cooldown extends BaseNpc implements MemoryObject {

    //冷却类型
    private String type;
    //关键字
    private String key;
    //开始时间
    private long start;
    //持续时间
    private long delay;
    //结束时间
    protected transient long endTime;
    //剩余时间
    protected transient long remainTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 获取结束时间
     *
     * @return
     */
    public long getEndTime() {
        return start + delay;
    }

    /**
     * 获取剩余时间
     *
     * @return
     */
    public long getRemainTime() {
        long remain = getEndTime() - TimeUtils.Time();
        return remain > 0 ? remain : 0;
    }

    @Override
    public void release() {
        this.setKey(null);
        this.setType(null);
        this.setStart(0);
        this.setDelay(0);
    }

    @Override
    protected void onForceStopMove() {

    }
}
