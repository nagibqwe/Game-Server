package com.game.servermatch.structs;

import game.core.util.TimeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author zhaibiao
 */
public class TimeAxis implements Serializable {

    List<AxisEvent> events = new LinkedList<>();

    public List<AxisEvent> getEvents() {
        return events;
    }

    public void setEvents(List<AxisEvent> events) {
        this.events = events;
    }

    private static final Logger logger = LogManager.getLogger("TimeAxis");
    public void tick(){
        Iterator<AxisEvent> it = events.iterator();
        while (it.hasNext()) {
            AxisEvent ae = it.next();
            long now = TimeUtils.Time();
            long sec =  now - ae.startup;
            if (TimeUtils.Time() > ae.startup) {
//            logger.error("查看执行时间："+ae.id + " 执行时间"+ae.startup);
                if(ae.isAchieved()){
                    logger.info("查看执行时间："+ae.id);
                    ae.trigger();
                }
                it.remove();
                if (ae.repeat && (ae.spaced > 0 || ae.nature > 0)) {
                    tempEvents.add(ae);
                    ae.addOnce();        
                }
                break;
            }
        }
        if(tempEvents.size()>0){
            addTempTimeEvents();
            tempEvents.clear();
        }
        
    }
     List<AxisEvent> tempEvents = new ArrayList<>();
    
    public boolean addTempTimeEvents() {
        for (AxisEvent ae : tempEvents) {
            logger.info(ae.id + "执行时间="+ TimeUtils.format2string(ae.startup, "yyyy-MM-dd HH:mm:ss"));
            addAxisEvent(ae);
        }
        return true;
    }
    /**
     * 增加一个事件
     * @param ae 
     */
    public void addAxisEvent(AxisEvent ae) {
        int i = 0;
        for (; i < events.size(); i++) {
            if (ae.startup< events.get(i).startup) {
                break;
            }
        }
        events.add(i, ae);
    }
    
}
