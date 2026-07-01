/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.servermatch.structs;

import com.game.servermatch.structs.condition.CurrentMonthCondition;
import com.game.servermatch.structs.condition.WeekOfCurrentMonthCondtion;
import com.game.servermatch.structs.condition.WeekOfMonthLastCondition;

/**
 *
 * @author zhaibiao
 */
public class ConditionParser {

    public static Condition parseType(String id) {
        Condition con = null;
        switch (id) {
            // 怪用function
            case Condition.WEEKOFMONThLASTCONDITION:
                con = new WeekOfMonthLastCondition();
                break;
            case Condition.CURRENTMONTHMONDITION:
                con = new CurrentMonthCondition();
                break;
            case Condition.WEEKOFCURRENTMONTHCONDTION:
                con = new WeekOfCurrentMonthCondtion();
                break;
            default:
        }
        return con;
    }
        
    
}
