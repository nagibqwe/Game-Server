/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.txpushsdk.struct;

/**
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class PushMessInfo {
    private int bigtype ; //1单体 2全体
    private int dectype; //1安卓， 2IOS
    private String title;//标题
    private String context;//内容
    private String tag;//设备标签

    public int getBigtype() {
        return bigtype;
    }

    public void setBigtype(int bigtype) {
        this.bigtype = bigtype;
    }

    public int getDectype() {
        return dectype;
    }

    public void setDectype(int dectype) {
        this.dectype = dectype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "PushMessInfo{" + "bigtype=" + bigtype + ", dectype=" + dectype + ", title=" + title + ", context=" + context + ", tag=" + tag + '}';
    }
    
}
