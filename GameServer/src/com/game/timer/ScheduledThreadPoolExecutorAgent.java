/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.timer;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * 定时任务调度器
 * 
 * @author zhaibiao
 */
public class ScheduledThreadPoolExecutorAgent {
    
    private static final Logger Log = LogManager.getLogger(ScheduledThreadPoolExecutorAgent.class);
    
    //初始化线程池数量，固定不会改变
    public static ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(10);
    
    //单次执行时间
    public static void schedule(ScheduledTask task, long delay, TimeUnit unit){
        try{
            exec.schedule(task, delay, unit);
        }catch(Exception e){
             Log.error("执行定时任务异常:"+task.getName(),e);
        }
    }
    /**
     * 固定间隔时间执行
     * @param task 执行任务主体，用TimerTask可以线程同步问题
     * @param initialDelay 初次执行时间
     * @param period 间隔执行时间
     * @param unit 时间单位 TimeUnit
     * @return 
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(ScheduledTask task, long initialDelay, long period, TimeUnit unit){
        try{
            return exec.scheduleAtFixedRate(task, initialDelay, period, unit);
        }catch(Exception e){
            Log.error("执行定时任务异常:"+task.getName(),e);
            return null;
        }
    }
    /**
     * 相对间隔时间执行
     * @param task 执行任务主体，用TimerTask可以线程同步问题
     * @param initialDelay 初次执行时间
     * @param period 间隔执行时间
     * @param unit 时间单位 TimeUnit
     * @return 
     */
    public static ScheduledFuture<?> scheduleWithFixedDelay(ScheduledTask task, long initialDelay, long period, TimeUnit unit){
        try {
            return exec.scheduleWithFixedDelay(task, initialDelay, period, unit);
        } catch (Exception e) {
            Log.error("执行定时任务异常:" + task.getName(), e);
            return null;
        }
    }
    
}
