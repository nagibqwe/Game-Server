/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.errorlog;

/**
 *日志数据
 * @author soko <xuchangming@haowan123.com>
 */
public class ErrorLog {
    //类型
    private int type;
    
    //值
    private Long value;
    
    //上一次值
    private Long lastValue;
    
    //次数, 扩展用的
    private int times;
    
    //上一次发送的时间
    private long lastSendTime;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getLastValue() {
        return lastValue;
    }

    public void setLastValue(Long lastValue) {
        this.lastValue = lastValue;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public long getLastSendTime() {
        return lastSendTime;
    }

    public void setLastSendTime(long lastSendTime) {
        this.lastSendTime = lastSendTime;
    }
}
