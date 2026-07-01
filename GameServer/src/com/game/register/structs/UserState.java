/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.register.structs;

/**
 *
 * @author Administrator
 */
public enum UserState {

    /**
     * 1	登录中
     */
    LOGINING(1),
    /**
     * 2	登录成功
     */
    LOGININGSUCCESS(2),
    /**
     * 3	创建角色中
     */
    CREATEROLE(3),
    /**
     * 4	选择角色中
     */
    SELECTING(4),
    /**
     * 5	进入游戏中
     */
    ENTERGAMEING(5),
    /**
     * 6	游戏中
     */
    GAMEING(6),
    /**
     * 7	退出中
     */
    QUITING(7),;

    private final int value;

    UserState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean compare(int value) {
        return this.value == value;
    }
}
