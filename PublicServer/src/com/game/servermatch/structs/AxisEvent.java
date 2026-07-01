package com.game.servermatch.structs;


import java.io.Serializable;

/**
 * 时间事件
 * @author zhaibiao
 */
public class AxisEvent implements Serializable {
    
    public String id ;
    public long startup;//开始时间
    public boolean repeat;//是否重复
    //以下两个只能同时存在一个
    public long spaced;//间隔时间
    public int nature;//自然间隔日期
    
    private Trigger trigger;
    private Condition condition;

    public boolean isAchieved() {
        if(condition != null){
            return condition.canActive();
        }
        return true;
    }
    
//    //执行事件
    public void trigger(){
        if(trigger!=null){
            trigger.active();
        }
    }

    public void setStartup(long startup) {
        this.startup = startup;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setSpaced(long spaced) {
        this.spaced = spaced;
    }

    public void setTriggr(Trigger trigger) {
        this.trigger = trigger;
    }
    
    public void addOnce(){
        if(spaced>0){
            startup += spaced;
            return;
        }
        if(nature>0){
            startup = LeagueConfig.getNature(nature);
        }
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public Condition getCondition() {
        return condition;
    }
    
    
    
    
    
    
    public int getNature() {
        return nature;
    }

    public void setNature(int nature) {
        this.nature = nature;
    }
    
}
