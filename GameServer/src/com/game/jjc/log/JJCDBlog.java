package com.game.jjc.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

public class JJCDBlog extends BaseLogBean{
    
    private static final Logger logger = LogManager.getLogger("JJCDBlog");
    
    private int sid;                //区服
    private long playerId;          //玩家ID
    private String event;           //事件
    
    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Log(logField = "sid", fieldType = "int", index = "0")
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Log(logField = "playerId", fieldType = "bigint", index = "0")
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Log(logField = "event", fieldType = "TEXT", index = "0")
    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
    
    
}
