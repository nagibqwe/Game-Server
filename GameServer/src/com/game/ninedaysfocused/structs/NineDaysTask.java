package com.game.ninedaysfocused.structs;

/**
 * Created by 542 on 2019/7/23.
 */
public class NineDaysTask {

    private int taskID = 0;

    private int alreadyStage = 0;//已完成阶段

    private int targetStage = 0;//目标阶段

    private boolean isGet = false;//是否领取

    private int type = 0;//任务类型

    public void init(){
        alreadyStage = 0;
        isGet = false;
    }


    public void setTaskID(int taskID){this.taskID = taskID;}

    public int getTaskID(){return  taskID;}

    public void setAlreadyStage(int alreadyStage){this.alreadyStage = alreadyStage;}

    public int getAlreadyStage(){return alreadyStage;}

    public void setTargetStage(int targetStage){this.targetStage = targetStage;}

    public int getTargetStage(){return targetStage;}

    public void setGet(boolean isGet){this.isGet = isGet;}

    public boolean getIsGet(){return isGet;}

    public void setType(int type){this.type = type;}

    public int getType(){return type;}
}
