package com.game.fallingsky.struct;

/**
 * 天禁令任务数据结构
 * Created by cxl on 2020/11/6.
 */
public class FallingSkyTask {

    private int taskID;

    private int progress;//进度

    private boolean state = false;//是否领取

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

}
