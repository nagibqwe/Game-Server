package com.backend.utils;

import com.backend.bean.Server;
import org.apache.log4j.Logger;
import org.nutz.dao.impl.SimpleDataSource;

import com.alibaba.druid.pool.DruidDataSource;

import java.util.Properties;

public class DbConfigUtil {

    private final static Logger log = Logger.getLogger(DbConfigUtil.class);

    private final static String driverClass = "com.mysql.jdbc.Driver";

    private static DbConfigUtil instance = new DbConfigUtil();

    private DbConfigUtil() {
    }

    public static DbConfigUtil getInstance() {
        return instance;
    }

    /**
     * 获取快速数据库连接，一般用于查游戏日志库短连接操作
     */
    public SimpleDataSource getSDS(Server serverDB) {
        SimpleDataSource ds = new SimpleDataSource();
        try {
            ds.setDriverClassName(driverClass);
            ds.setJdbcUrl("jdbc:mysql://" + serverDB.getDblogIp() + ":" + serverDB.getDblogPort() + "/" + serverDB.getDblogName() + "?useUnicode=true&characterEncoding=UTF-8");
            ds.setUsername(serverDB.getDblogUser());
            ds.setPassword(serverDB.getDblogPwd());
            return ds;
        } catch (ClassNotFoundException e) {
            log.error(e, e);
        }

        return null;
    }

    /**
     * 获取数据库连接池
     *
     * @param serverDB    数据库连接信息
     * @param isReconnect 是否断开重连
     */
    public DruidDataSource getDataSource(Server serverDB, boolean isReconnect) {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(driverClass);
        ds.setUrl("jdbc:mysql://" + serverDB.getDblogIp()+":"+serverDB.getDblogPort() + "/" + serverDB.getDblogName());
        ds.setUsername(serverDB.getDblogUser());
        ds.setPassword(serverDB.getDblogPwd());
        Properties properties = new Properties();
        properties.setProperty("serverTimezone", "UTC");
        properties.setProperty("useUnicode", "true");
        properties.setProperty("characterEncoding", "UTF-8");
        ds.setConnectProperties(properties);
        ds.setTestWhileIdle(isReconnect);
        ds.setValidationQuery("select 1");
        ds.setMaxWait(5000);
        ds.setTimeBetweenEvictionRunsMillis(5000);
        return ds;
    }
}
