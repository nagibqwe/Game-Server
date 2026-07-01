package com.game.godbook.struct;

public class ConditionInfo {

    public static final int HAS_COMPLETED = 1;

    public static final int UNDER_WAY = 2;

    public static final int HAS_DRAW = 3;

    /**
     * 任务条件id
     */
    private int id;

    /**
     * 进度
     */
    private int progress;

    /**
     * 是否完成
     */
    private int status;

    public ConditionInfo() {}

    public ConditionInfo(int id) {
        this.id = id;
        this.status = UNDER_WAY;
    }

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ConditionInfo{" +
                "id=" + id +
                ", progress=" + progress +
                ", status=" + status +
                '}';
    }
}
