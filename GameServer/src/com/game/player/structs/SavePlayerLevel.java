/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.player.structs;

/**
 *
 * @author Administrator
 */
public enum SavePlayerLevel {

    RightNow(-1), //立即保存
    HalfMinLater(30 * 1000), //半分钟后保存
    OneMinLater(60 * 1000), //1分总后保存
    FiveMinLater(5 * 60 * 1000), //5分钟后保存
    TenMinLater(10 * 60 * 1000) //10分钟后保存
    ;
    private final int value;

    private SavePlayerLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
