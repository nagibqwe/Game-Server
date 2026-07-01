/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.servermatch.structs;

/**
 *
 * @author zhaibiao
 */
public class TimeEventBean {
    private String trigger;
    private long initial;
    private long spaced;//固定时间间隔
    private boolean repeat;//是否重复
    private int nature;//自然时间（1，年；2，月；3，日；4，周；5，时）
    private String condition;

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    


    public long getSpaced() {
        return spaced;
    }

    public void setSpaced(long spaced) {
        this.spaced = spaced;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public long getInitial() {
        return initial;
    }

    public void setInitial(long initial) {
        this.initial = initial;
    }

    public int getNature() {
        return nature;
    }

    public void setNature(int nature) {
        this.nature = nature;
    }
    
    
}
