package com.game.servermatch.structs;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 *
 * @author zhaibiao
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public interface Condition extends Serializable {
 
    public static String WEEKOFMONThLASTCONDITION  = "weekofmonthlastcondition";
    public static String CURRENTMONTHMONDITION = "currentmonthmondition";
    public static String WEEKOFCURRENTMONTHCONDTION =  "weekofcurrentmonthcondtion";

    
    boolean canActive();
}
