/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.cooldown.structs;

/**
 * @author Administrator
 */
public enum CooldownTypes {
    // ai做了什么需要持续一段时间的事,不需要单独的key,全局的
    AI_DOING_CD("AI_DOING_CD"),
    AI_GATHER_CD("AI_GATHER_CD"),;

    private final String value;

    CooldownTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
