/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.map.structs;

/**
 *  触发器类型
 * @author zenghai <zenghai@haowan123.com>
 */
public enum TriggerType {
    
    TransPost ("Teleport"), //传送点
    
    FlyTransPost ("FlyTeleport"), //跳跃传送
    
    ;
    private String value;
    
    TriggerType(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
    public boolean compare(String value) {
        return  this.value.equals(value);
    }
}
