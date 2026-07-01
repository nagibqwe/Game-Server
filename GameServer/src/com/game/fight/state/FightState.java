/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.fight.state;

/**
 * 战斗状态基类
 *
 * @author zenghai
 */
public abstract class FightState implements IFightState {

    private long overTime; //结束时间

    public long getOverTime() {
        return overTime;
    }

    public void setOverTime(long overTime) {
        this.overTime = overTime;
    }

}
