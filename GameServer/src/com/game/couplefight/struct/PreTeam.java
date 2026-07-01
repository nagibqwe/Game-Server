package com.game.couplefight.struct;

/**
 * 预报名信息
 * @Auther: gouzhongliang
 * @Date: 2021/7/2 17:36
 */
public class PreTeam {
    /**玩家1*/
    private long mid;
    private String mname;
    /**玩家2*/
    private long wid;
    private String wname;
    /**确认*/
    private boolean mconfirm = false;
    /***/
    private boolean wconfirm = false;
    /**战队名称*/
    private String name;
    /**时间戳*/
    private long time;

    public PreTeam(long mid, String mname, long wid, String wname, String name){
        this.mid = mid;
        this.wid = wid;
        this.mname = mname;
        this.wname = wname;
        this.name = name;
        time = System.currentTimeMillis();
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public long getWid() {
        return wid;
    }

    public void setWid(long wid) {
        this.wid = wid;
    }

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }

    public boolean isMconfirm() {
        return mconfirm;
    }

    public void setMconfirm(boolean mconfirm) {
        this.mconfirm = mconfirm;
    }

    public boolean isWconfirm() {
        return wconfirm;
    }

    public void setWconfirm(boolean wconfirm) {
        this.wconfirm = wconfirm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
