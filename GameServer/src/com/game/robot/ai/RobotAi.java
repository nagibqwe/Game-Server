/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.robot.ai;

/**
 *
 * @author zenghai
 */
public enum RobotAi {

    None(0), //无
    Help(1), //助战
    Anger(2),//愤怒
    JJC(3), //竞技场
    CUSTOM(4),// 自定义的ai
    ;
    private int ai;

    RobotAi(int ai) {
        this.ai = ai;
    }

    public boolean compareTo(int ai) {
        return this.ai == ai;
    }
}
