package com.game.servermatch.structs;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 *
 * @author zhaibiao
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public interface Trigger extends Serializable {
    public static String SERVER_MACTH_TRIGGER = "server_macth_trigger";
    public static String DAYOfWEEKMIN_TRIGGER = "dayofweekmin_trigger";
    public static String LEAGUEWARBEGINFIRST_TRIGGER = "leaguewarbeginfirst_trigger";
    public static String LEAGUEWARENDFIRST_TRIGGER = "leaguewarendfirst_trigger";
    public static String LEAGUEWARBEGINSECOND_TRIGGER = "leaguewarbeginsecond_trigger";
    public static String LEAGUEWARENDSECOND_TRIGGER = "leaguewarendsecond_trigger";
    public static String LEAGUESETTLEMENT_TRIGGER = "leaguesettlement_trigger";
    public static String CITYAPPLYBEGIN_TRIGGER = "cityapplybegin_trigger";
    public static String CITYAPPLYEND_TRIGGER = "cityapplyend_trigger";
    public static String CITYWARBEGIN_TRIGGER = "citywarbegin_trigger";
    public static String CITYWAREND_TRIGGER = "citywarend_trigger";
    public static String LEAGUEWARMATCH_TRIGGER = "leaguewarmatch_trigger";
    public static String LEAGUEAPPLYBEGIN_TRIGGER = "leagueapplybegin_trigger";

    public static String EIGHT_DIAGRAMS_TRIGGER = "eight_diagrams_trigger";
    
    public boolean active();
    
}
