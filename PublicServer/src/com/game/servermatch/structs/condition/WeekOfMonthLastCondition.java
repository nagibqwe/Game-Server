/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.servermatch.structs.condition;

import com.game.servermatch.structs.Condition;
import game.core.util.TimeUtils;

/**
 *  判断这个时间是否是最后一周
 * @author zhaibiao
 */
public class WeekOfMonthLastCondition implements Condition{


    
    @Override
    public boolean canActive() {
        long time = TimeUtils.Time();
        int curMonth = TimeUtils.getMonth(time);
        int nextMonth = TimeUtils.getMonth(time+7*24*60*60*1000);
        return curMonth != nextMonth;
    }
    
}
