/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.player.structs;

/**
 *
 * @author zenghai
 */
public enum ReliveType {

    CurPos(0), //当前
    GoBack(1), //回城
    Gm(99), //Gm复活
    ;
    private final int value;

    ReliveType(int type) {
        this.value = type;
    }

    public int getValue() {
        return this.value;
    }

    public boolean compareTo(int type) {
        return this.value == type;
    }

}
