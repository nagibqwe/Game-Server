package com.game.ninedaysfocused.structs;

import game.core.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by CLC on 2019/7/23.
 */
public class NineDaysPlayerData {


    private int curday = 0;//当前的天数

    private int gatherRewardNum = 0;//采集奖励次数

    private int bossRewardNum = 0; //首领奖励次数

    private ConcurrentHashMap<Integer, NineDaysTask> taskList = new ConcurrentHashMap<>();



    public void init()
    {
        gatherRewardNum = 0;
        bossRewardNum = 0;
    }

    public void checkDayValue() {
        int nowday = TimeUtils.getCurDay(0);
        if (curday != nowday) {
            curday =  nowday;
            gatherRewardNum = 0;
            bossRewardNum = 0;
            taskList.clear();
        }
    }


    public void setGatherRewardNum(int gatherRewardNum){this.gatherRewardNum = gatherRewardNum;}

    public int getGatherRewardNum(){return gatherRewardNum;}

    public void setBossRewardNum(int bossRewardNum){this.bossRewardNum = bossRewardNum;}

    public int getBossRewardNum(){return bossRewardNum;}

    public ConcurrentHashMap<Integer, NineDaysTask> getTaskList(){return taskList;}


}
