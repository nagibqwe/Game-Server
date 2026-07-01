/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.skill.config;

import com.game.utils.Shape;

/**
 * 事件内容
 * @author zenghai <zenghai@haowan123.com>
 *     2018 - 8 -30  soko 修改技能内容
 */
public abstract class SkillEvent {

    /**
     * 事件ID值
     */
    private int eventID;
    /**
     * 事件帧数
     */
    private int eventFrame;
    /**
     * 事件类型
     */
    private int eventType;
    /**
     * 毫秒
     */
    private int fpsTime;

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getEventFrame() {
        return eventFrame;
    }

    public void setEventFrame(int eventFrame) {
        this.eventFrame = eventFrame;
        this.fpsTime = (int) (eventFrame * 1f / 30f * 1000);
    }

    public int getFpsTime() {
        return fpsTime;
    }
    
    //获取受击速度
    public abstract float getSpeed(int type);
    
    //获取受击时间
    public abstract int getRunTime(int type);
    
    //获取受击类型
    public abstract int getHitType();
    
    //获取受击距离
    public abstract float getHitDis(int type);
    
    public abstract Shape getShape();

    public abstract int getUniqueID();

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    /**
     * 解析事件参数
     *
     * @param param
     */
    public abstract void split(String param);

    @Override
    public String toString() {
        return "SkillEvent{" +
                "eventID=" + eventID +
                ", eventFrame=" + eventFrame +
                ", eventType=" + eventType +
                ", fpsTime=" + fpsTime +
                '}';
    }
}
