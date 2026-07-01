package com.game.task.structs;

import game.message.taskMessage.mainTaskInfo;
import game.message.taskMessage.taskAttribute;

import java.util.HashMap;

/**
 * 公会任务对象
 */
public class GuildTask {
    /**
     * 当前任务id
     */
    private int nowTaskId;
    /**
     * 当前目标数据值 例如：<怪物ID，杀怪数量>或<副本ID，完成次数>
     */
    HashMap<Integer, Integer> targetValue = new HashMap<>();
    /**
     * 任务实例ID
     */
    private long instanceId;
    /**
     * 目标模型ID（当任务为位面任务时，则为位面副本ID或者怪物ID）   对应task表的target字段
     */
    private int modelId;
    /**
     * 目标完成数量
     */
    private int needNum;
    /**
     * 任务寻径地图ID
     */
    private int mapId;
    /**
     * 任务目标坐标
     */
    private int xPos;
    private int yPos;

    /**
     * 任务是否完成
     */
    private boolean finish;

    public long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public HashMap<Integer, Integer> getTargetValue() {
        return targetValue;
    }

    public int getNowTaskId() {
        return nowTaskId;
    }

    public void setNowTaskId(int nowTaskId) {
        this.nowTaskId = nowTaskId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getNeedNum() {
        return needNum;
    }

    public void setNeedNum(int needNum) {
        this.needNum = needNum;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    /**
     * 设置任务信息
     * @param taskInfo
     */
    public void resetTaskInfo(mainTaskInfo taskInfo) {
        nowTaskId = taskInfo.getModelId();
        instanceId = taskInfo.getModelId();

        taskAttribute a = taskInfo.getUseItems();
        targetValue.put(a.getModel(), a.getNum());
        modelId = a.getModel();
        needNum = a.getNeedNum();
        mapId = a.getMapId();
        xPos = a.getXPos();
        yPos = a.getYPos();

        if(targetValue.get(modelId) == needNum){
            this.finish = true;
        }
    }

    /**
     * 更新任务进度
     */
    public void updateTaskInfo(taskAttribute a) {
        this.targetValue.put(a.getModel(), a.getNum());
        this.modelId = a.getModel();
        this.needNum = a.getNeedNum();
        this.mapId = a.getMapId();
        this.xPos = a.getXPos();
        this.yPos = a.getYPos();

        if(targetValue.get(modelId) == needNum){
            this.finish = true;
        }
    }

    /**
     * 清除任务信息
     */
    public void clear(){
        this.nowTaskId = 0;
        this.targetValue.clear();
        this.instanceId = 0;
        this.modelId = 0;
        this.needNum = 0;
        this.mapId = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.finish = false;
    }

    @Override
    public String toString() {
        return "MainTask{" +
                "nowTaskId=" + nowTaskId +
                ", nowValue=" + targetValue +
                ", instanceId=" + instanceId +
                ", modelId=" + modelId +
                ", needNum=" + needNum +
                ", mapId=" + mapId +
                ", xPos=" + xPos +
                ", yPos=" + yPos +
                '}';
    }
}
