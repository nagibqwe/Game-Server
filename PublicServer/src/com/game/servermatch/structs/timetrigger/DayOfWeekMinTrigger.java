/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.servermatch.structs.timetrigger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.servermatch.structs.Trigger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 每周第一天
 * @author zhaibiao
 */
public class DayOfWeekMinTrigger  implements Trigger{

    @JsonIgnore
    private static final Logger logger = LogManager.getLogger("ServerMatchTrigger");
    
    @Override
    public boolean active() {
        try {

            return true;
        } catch (Exception e) {
            logger.error(e, e);
            return false;
        }

    }
    
}
