package com.backend.utils;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.nutz.mvc.Mvcs;

public class PropertiesUtil {

    private static final Logger log = Logger.getLogger(PropertiesUtil.class);

    private static PropertiesUtil instance = new PropertiesUtil();

    private PropertiesUtil() {
    }

    public static PropertiesUtil getInstance() {
        return instance;
    }

    public Properties readProperties(String fileName) {
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(fileName), StandardCharsets.UTF_8));
            return prop;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public Properties readPropertiesByPath(String filePath) {
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(Mvcs.getServletContext().getResourceAsStream(filePath), StandardCharsets.UTF_8));
            return prop;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
