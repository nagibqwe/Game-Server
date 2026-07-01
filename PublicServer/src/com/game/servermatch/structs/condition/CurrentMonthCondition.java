/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.servermatch.structs.condition;

import com.game.servermatch.manager.ServerMatchManager;
import com.game.servermatch.structs.Condition;
import game.core.util.TimeUtils;

/**
 *
 * @author zhaibiao
 */
public class CurrentMonthCondition implements Condition{
    

    @Override
    public boolean canActive() {
        return TimeUtils.getMonth(TimeUtils.Time())!=ServerMatchManager.matchMonth;
    }
    
}
