package com.gm.common.utils;


import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

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
            InputStream inStream = new FileInputStream(new File(fileName));
            prop.load(inStream);
            return prop;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
