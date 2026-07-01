package com.game.openserverac.structs;


/**
 * V4返利
 */
public class VIP4GrouUpData {

    private int id;         //ID
    private int progress;         //进度
    private boolean  isComplete;         //是否完成，true领奖 false没领奖

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
