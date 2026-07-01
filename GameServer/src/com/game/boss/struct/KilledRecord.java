package com.game.boss.struct;

/**
 * 击杀记录
 */
public class KilledRecord {
    
    private int killedTime;  //被击杀时间
    
    private long killerId;   //击杀者Id
    
    public KilledRecord() {}
    
    public KilledRecord(int killedTime, long killerId) {
        this.killedTime = killedTime;
        this.killerId = killerId;
    }

    public int getKilledTime() {
        return killedTime;
    }

    public void setKilledTime(int killedTime) {
        this.killedTime = killedTime;
    }

    public long getKillerId() {
        return killerId;
    }

    public void setKillerId(long killerId) {
        this.killerId = killerId;
    }
  
}
