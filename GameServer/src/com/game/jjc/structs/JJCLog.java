/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.jjc.structs;

/**
 *
 * @author zenghai <zenghai@haowan123.com>
 */
public class JJCLog {
    private int time;
    private int type;
    private String who;
    private int param;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }
    
}
