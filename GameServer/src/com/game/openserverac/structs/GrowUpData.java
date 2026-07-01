package com.game.openserverac.structs;

/**
 * 成长之路数据结构
 */
public class GrowUpData {

    private int id;

    private  int progress;//进度

    private boolean hasGet;


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

    public boolean isHasGet() {
        return hasGet;
    }

    public void setHasGet(boolean hasGet) {
        this.hasGet = hasGet;
    }
}
