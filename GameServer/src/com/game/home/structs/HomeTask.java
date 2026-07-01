package com.game.home.structs;

import java.util.HashMap;

/**
 * @Desc TODO
 * @Date 2021/9/16 16:26
 * @Auth ZUncle
 */
public class HomeTask {

    int id;       //任务ID
    int process;    //任务进度
    int state;      //任务状态 0=未完成 1=完成 2=已领取

    HashMap<Integer, Integer> params = new HashMap<>();     //额外参数


    public void reset(){
        this.process = 0;
        this.state = 0;
        this.params.clear();
    }

    public HashMap<Integer, Integer> getParams() {
        return params;
    }

    public void setParams(HashMap<Integer, Integer> params) {
        this.params = params;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
