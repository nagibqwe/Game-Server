package com.game.achievement.structs;

public class AchievementInfo {

    public static final int STATE_UNFINISH = 0;

    public static final int STATE_FINISH = 1;

    public static final int STATE_DRAW = 2;

    private int id;

    private int progress;

    private int state;

    public AchievementInfo() {}

    public AchievementInfo(int id) {
        this.id = id;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "AchievementInfo{" +
                "id=" + id +
                ", progress=" + progress +
                ", state=" + state +
                '}';
    }
}
