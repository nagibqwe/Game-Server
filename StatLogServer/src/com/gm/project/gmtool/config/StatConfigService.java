package com.gm.project.gmtool.config;

import com.gm.project.gmtool.service.StatService;

import java.util.Properties;

public class StatConfigService {

    private Properties prop = null;

    private StatConfigService() {

    }

    private static StatConfigService instance = new StatConfigService();
    public static StatConfigService getInstance(){
        if(null == instance){
            instance = new StatConfigService();
        }
        return instance;
    }
    public void init(Properties prop){
        this.prop = prop;
    }

    public String getValue(String key) {
        return prop.getProperty(key);
    }

}
