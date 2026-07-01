package com.game.ninedaysfocused.structs;

/**
 * Created by cxl on 2019/7/23.
 */
public class NineDaysTask {

    private int taskID = 0;

    private int alreadyStage = 0;//已完成阶段

    private int targetStage = 0;//目标阶段

    private boolean isGet = false;//是否领取


    public void setTaskID(int taskID){this.taskID = taskID;}

    public int getTaskID(){return  taskID;}

    public void setAlreadyStage(int alreadyStage){this.alreadyStage = alreadyStage;}

    public int getAlreadyStage(){return alreadyStage;}

    public void setTargetStage(int targetStage){this.targetStage = targetStage;}

    public int getTargetStage(){return targetStage;}

    public void setGet(boolean isGet){this.isGet = isGet;}

    public boolean getIsGet(){return isGet;}

}
