/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db;

import com.game.structs.Config;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author hewei@haowan123.com
 */
public enum DBFactory {
    // 游戏数据库
    GAME_DB("gameDB", "config/db-game-config.xml");//游戏数据库，包含角色数据
//    PUBLIC_DB("publicDB", "config/db-public-config.xml");//公共数据库，包含激活码
//    GAME_DATA_DB("gameDataDB", "src/game/data/db-game-data-config.xml"); // 策划配置数据库��
    
    private final Logger logger;
    private final String name;
    private SqlSessionFactory sessionFactory;

    /**
     * 初始化一个DB实例.
     *
     * @param name logger名称
     * @param config mybatis配置文件的相对路径
     */
    private DBFactory(String name, String config) {
        this.name = name;
        this.logger = LogManager.getLogger(name + "Logger");
        try {
            try (InputStream in = new FileInputStream(config)) {
                this.sessionFactory = new SqlSessionFactoryBuilder().build(in, getProperties());
            }
        } catch (IOException e) {
            logger.info(e, e);
        }
    }

    /**
     * 获取日志管理器.
     *
     * @return
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * 获取SQLSeesionFactory.
     *
     * @return
     */
    public SqlSessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private Properties getProperties() {
        Properties properties = null;
        switch (name) {
            case "gameDB":
                properties = Config.getGameDBConfig().getProperties();
                break;
            case "gameDataDB":
                properties = Config.getGameDataDBConfig().getProperties();
                break;
            case "publicDB":
                properties = Config.getPublicDataDBConfig().getProperties();
                break;
            default:
                logger.error("unknow name: [" + name + "]");
                break;
        }
        return properties;
    }
}
