package com.backend.utils;

import org.nutz.dao.Dao;

import com.backend.bean.APILog;

/**
 * API操作日志
 *
 * @author Administrator
 */
public class APILogUtil {

    private Dao dao;

    private static APILogUtil instance = new APILogUtil();

    private APILogUtil() {

    }

    public static APILogUtil getInstance() {
        return instance;
    }

    public void init(Dao dao) {
        this.dao = dao;
    }

    public void log(APILog apiLog) {
        dao.insert(apiLog);
    }
}
