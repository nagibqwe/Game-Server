package com.game.timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author zhaibiao
 */
public abstract class GameTimer extends ScheduledTask{
    
    private static final Logger log = LogManager.getLogger(GameTimer.class);
    
    long initialDelay;
    long period;
    int count;//执行次数
    int completed; //完成次数
    
    
    /**
     * @param initialDelay
     * @param period
     * @param count -1 表示永久执行
     */
    
    public GameTimer(long initialDelay, long period, int count) {
        this.initialDelay = initialDelay;
        this.period = period;
        this.count = count;
    }
    
    @Override
    public long getPeriod() {
        return this.period; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getInitialDelay() {
        return this.initialDelay; //To change body of generated methods, choose Tools | Templates.
    }
    
    //
    public boolean isOver(){
        if(count == -1){
            return false;
        }
        if (completed++ >= count) {
            super.future.cancel(true);
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        if(isOver()){
            return;
        }
        try{
            execute();        
        } catch(Exception e){
            log.error(e,e);
        }
    }
    public abstract void execute();
    
}
