/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.map.structs;

/**
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public enum VolumeType {
    None(0), // 然并卵
    Box(1), //盒子
    Sphere(2), //球
    ;
    private int value;

    private VolumeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public boolean compare(int volume) {
        return this.value == volume;
    }
    
}
