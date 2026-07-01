package com.game.worldbonfire.structs;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @auther lw
 * @create 2019-10-22 10:01
 */
public class WorldBonfireTeam {
    private long id;

    private long time;

    private int curNum;

    private List<WorldBonfireMatch> mms = new ArrayList<>();

    private long fingerTime;

    public long getFingerTime() {
        return fingerTime;
    }

    public void setFingerTime(long fingerTime) {
        this.fingerTime = fingerTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public int getCurNum() {
        return curNum;
    }

    public void setCurNum(int curNum) {
        this.curNum = curNum;
    }

    public List<WorldBonfireMatch> getMms() {
        return mms;
    }

    public void setMms(List<WorldBonfireMatch> mms) {
        this.mms = mms;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (WorldBonfireMatch mm : mms) {
            buffer = buffer.append(mm.toString());
        }
        return "WorldBonfireTeam{" +
                "id=" + id +
                ", time=" + time +
                ", mms=" + buffer +
                '}';
    }
}
