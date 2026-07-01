package com.backend.manager;

import org.apache.log4j.Logger;

public class LogFieldManager {

    private final static Logger log = Logger.getLogger(LogFieldManager.class);

    private enum Singleton {

        INSTANCE;
        LogFieldManager manager;

        Singleton() {
            this.manager = new LogFieldManager();
        }

        LogFieldManager getProcessor() {
            return manager;
        }
    }

    public static LogFieldManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

//    private Map<String, LogFieldEntity> logFieldMap = new ConcurrentHashMap<>();
//
//    public void load() {
//        Ioc ioc = new NutIoc(new JsonLoader("ioc/fields.js"));
//        String[] names = ioc.getNames();
//        logFieldMap.clear();
//        for (String name : names) {
//            LogFieldEntity logFieldEntity = ioc.get(LogFieldEntity.class, name);
//            logFieldMap.put(name, logFieldEntity);
//        }
//    }
//
//    public LogFieldEntity getLogField(String name) {
//        if (logFieldMap.containsKey(name)) {
//            return logFieldMap.get(name);
//        }
//        Ioc ioc = new NutIoc(new JsonLoader("ioc/fields.js"));
//        try {
//            return ioc.get(LogFieldEntity.class, name);
//        } catch (Exception e) {
//            log.error("没有找到字段实体为：" + name + "的相关信息，请检查配置文件fields.js", e);
//        }
//        return null;
//    }
}
