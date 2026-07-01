package com.game.worldanswer.structs;

import com.game.gameserver.manager.GameServerManager;
import com.game.manager.Manager;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by clc on 2019/7/15.
 */
public class Question {
    private int id;

    private long sendTime = 0;//发题时间单位（秒）；

    private int  round = 0;  //本题答题的轮数

    private ConcurrentHashMap<Integer, ConcurrentHashMap<Long, PlayerSelectInfo>> playerSelectList = new ConcurrentHashMap<>();//组ID对应每组玩家数据

    private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> lastRoundResultList = new ConcurrentHashMap<>();//保存第上一轮的每题的支持

    private long overTime = 0;//结束时间


    public Question() {
       for (Integer groupid: Manager.worldAnswerManager.getGroupAnswerList().keySet()){
           ConcurrentHashMap<Long, PlayerSelectInfo> PlayerSelectInfoLsit = new  ConcurrentHashMap<>();
           playerSelectList.put(groupid,PlayerSelectInfoLsit);
           ConcurrentHashMap<Integer, Integer> resultList =  new ConcurrentHashMap<>();
           for (int i = 0;i<QuestionDefine.ResultIndex_4;i++){
               resultList.put(i+1,0);
           }
           lastRoundResultList.put(groupid,resultList);
       }
    }

    public void init()
    {
        id = 0;
        sendTime = 0;
        round = 0;
        overTime = 0;
        for (Integer groupid:  Manager.worldAnswerManager.getGroupAnswerList().keySet()){
            playerSelectList.get(groupid).clear();
            for (int i = 0;i<QuestionDefine.ResultIndex_4;i++){
                lastRoundResultList.get(groupid).put(i+1,0);
            }
        }

    }

    public void setId(int id){this.id = id;}

    public int getId() {
        return id;
    }

    public void setSendTime(long sendTime){this.sendTime = sendTime;}

    public long getSendTime() {
        return sendTime;
    }

    public void setRound(int round){this.round = round;}

    public int  getRound(){return round;}

    public void setSendTime(int sendTime) {
        this.sendTime = sendTime;
    }

    public void  setPlayerChooseList(ConcurrentHashMap<Integer, ConcurrentHashMap<Long, PlayerSelectInfo>> playerSelectList){this.playerSelectList = playerSelectList;}

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Long, PlayerSelectInfo>> getPlayerChooseList(){return playerSelectList;}

    public void setLastRoundResultList(ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> resultIndexList){this.lastRoundResultList = resultIndexList;}

    public ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Integer>> getLastRoundResultList() {return lastRoundResultList;}

    public void setOverTime(long overTime){this.overTime = overTime;}

    public long getOverTime(){return overTime;}
}
