/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.servermatch.structs.condition;

import com.game.servermatch.structs.Condition;
import game.core.util.TimeUtils;

/**
 *
 * @author zhaibiao
 */
public class WeekOfCurrentMonthCondtion implements Condition{
    
    @Override
    public boolean canActive() {
        return TimeUtils.getMonth(TimeUtils.getTime("*-*-*-1-00-00")) != TimeUtils.getMonth(TimeUtils.Time()) || TimeUtils.getMonth(TimeUtils.getTime("*-*-*-7-00-00")) != TimeUtils.getMonth(TimeUtils.Time());
    }
}
