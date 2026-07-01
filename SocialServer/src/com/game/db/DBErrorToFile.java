/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 *
 * @author hewei@haowan123.com
 */
public class DBErrorToFile {
    
    private static final Logger log = LogManager.getLogger("sqlErrorLog");
    
    public static void error(String str) {
       // SocialServer.getInstance().getErrorLogThread().pushErrorExcptionLog("DBERROR", str);
        log.error(str);
    }
    
    public static void error(Exception e) {
        String str = "";
        int size = e.getStackTrace().length;
            if (size < 3) {
                str = Arrays.toString(e.getStackTrace());
            } else {
                str = e.getStackTrace()[2].toString();
            }
      //  SocialServer.getInstance().getErrorLogThread().pushErrorExcptionLog("DBERROR", str);
        log.error(e, e);
    }

    public static void error(String str, Exception e) {
        String sstr = "";
        int size = e.getStackTrace().length;
            if (size < 3) {
                sstr = Arrays.toString(e.getStackTrace());
            } else {
                sstr = e.getStackTrace()[2].toString();
            }
       // SocialServer.getInstance().getErrorLogThread().pushErrorExcptionLog("DBERROR", sstr);
        log.error(str, e);
    }
    
    public static void error(Exception e1, Exception e2) {
        String sstr = "";
        int size = e1.getStackTrace().length;
            if (size < 3) {
                sstr = Arrays.toString(e1.getStackTrace());
            } else {
                sstr = e1.getStackTrace()[2].toString();
            }
       // SocialServer.getInstance().getErrorLogThread().pushErrorExcptionLog("DBERROR", sstr);
        log.error(e1, e2);
    }
    
}
