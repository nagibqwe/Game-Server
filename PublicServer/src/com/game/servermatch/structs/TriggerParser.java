/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.servermatch.structs;

import com.game.servermatch.structs.timetrigger.*;

/**
 *
 * @author zhaibiao
 */
public class TriggerParser {
    
    public static Trigger parseType(String id){
        Trigger tri = null;
        switch (id) {
            // 怪用function
            case Trigger.SERVER_MACTH_TRIGGER:
                tri = new ServerMatchTrigger();
                break;
            case Trigger.DAYOfWEEKMIN_TRIGGER:
                tri = new DayOfWeekMinTrigger();
                break;
            case Trigger.EIGHT_DIAGRAMS_TRIGGER:
                tri = new EightDiagramsTregger();
                break;

            default:
        }
        return tri;
    }
}
