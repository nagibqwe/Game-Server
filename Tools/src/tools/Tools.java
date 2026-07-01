/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import excelExport.ExcelToCode;
import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author hewei
 */
public class Tools {
    
    public final static Logger logger = Logger.getLogger(Tools.class);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            StringBuilder pathBuilder = new StringBuilder(System.getProperty("user.dir") + File.separator + "config" + File.separator);
            pathBuilder.append("log4j_server.xml");
            DOMConfigurator.configureAndWatch(pathBuilder.toString());      
            LoadConfig.load();
            ExcelToCode.toCode("Config");
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

}
