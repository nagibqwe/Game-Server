package com.game.openserverac.structs;

/**
 * 仙盟争霸数据
 * @Auther: gouzhongliang
 * @Date: 2021/12/3 9:33
 */
public class GuildBattleData {

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
