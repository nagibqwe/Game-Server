/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.timer;

import java.util.TimerTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 计划任务接口类
 * @author zhaibiao
 */
public abstract class ScheduledTask extends TimerTask implements ObjectName{

    public Future future;
    
    public abstract long getInitialDelay();
    
    public abstract long getPeriod();
    
    public void executeScheduleAtFixedRate(){
        future = ScheduledThreadPoolExecutorAgent.scheduleAtFixedRate(this, getInitialDelay(), getPeriod(), TimeUnit.MILLISECONDS);
    }
    
    public void executeScheduleWithFixedDelay(){
        future = ScheduledThreadPoolExecutorAgent.scheduleAtFixedRate(this, getInitialDelay(), getPeriod(), TimeUnit.MILLISECONDS);
    }
    
    public void executeSchedule(){
        ScheduledThreadPoolExecutorAgent.schedule(this, getInitialDelay(), TimeUnit.MILLISECONDS);
    }
}
