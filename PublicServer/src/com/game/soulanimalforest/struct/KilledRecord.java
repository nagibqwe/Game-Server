/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.soulanimalforest.struct;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class KilledRecord {

    private int killedTime;  //被击杀时间

    private String killer;   //击杀者Id

    public KilledRecord() {
    }

    public int getKilledTime() {
        return killedTime;
    }

    public void setKilledTime(int killedTime) {
        this.killedTime = killedTime;
    }

    public String getKiller() {
        return killer;
    }

    public void setKiller(String killer) {
        this.killer = killer;
    }

}
