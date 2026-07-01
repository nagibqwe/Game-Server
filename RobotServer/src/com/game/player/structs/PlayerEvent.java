/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.player.structs;

import java.util.HashMap;

/**
 *
 * @author hewei@haowan123.com
 */
public class PlayerEvent {

    private int eventType;//事件类型
    private long doTime;//执行时间,单位毫秒
    private final HashMap<String, Object> parms = new HashMap<>();//参数

    public PlayerEvent() {

    }

    public PlayerEvent(int eventType, long doTime, HashMap<String, Object> parms) {
        this.eventType = eventType;
        this.doTime = doTime;
        if (parms != null) {
            this.parms.putAll(parms);
        }
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public long getDoTime() {
        return doTime;
    }

    public void setDoTime(long doTime) {
        this.doTime = doTime;
    }

    public void putParm(String key, Object object) {
        parms.put(key, object);
    }

    public Object getParmByKey(String key) {
        return parms.get(key);
    }
}
