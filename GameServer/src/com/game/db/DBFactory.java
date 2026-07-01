package com.game.db;

import game.core.net.Config.ServerConfig;
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
 * <b>DB工厂.</b>
 * <p>
 * 使用枚举来实现单例, 可以避免多线程问题, 并且, 当有多个不同的数据库时, 每个数据库都作为枚举的一个属性来定义即可, 使用简单, 维护方便.
 * <p>
 * <b>Sample:</b>
 *
 * @author <a href="mailto:wjv.1983@gmail.com">wangJingWei</a>
 * @version 1.0.0
 */
public enum DBFactory {
    //公共数据库，包含激活码
    PUBLIC_DB("publicDB", "config/db-public-config.xml", ServerConfig.getInstance().getPublicDBConfig().getProperties()),

    // 游戏数据库
    GAME_DB("gameDB", "config/db-game-config.xml", ServerConfig.getInstance().getGameDBConfig().getProperties());

    // 策划配置数据库
//    GAME_DATA_DB("gameDataDB", "src/game/data/db-game-data-config.xml", ServerConfig.getInstance().getGameDataDBConfig().getProperties());

    private SqlSessionFactory sessionFactory;

    /**
     * 初始化一个DB实例.
     *
     * @param name logger名称
     * @param config mybatis配置文件的相对路径
     */
    DBFactory(String name, String config, Properties properties) {
        try {
            try (InputStream in = new FileInputStream(config)) {
                this.sessionFactory = new SqlSessionFactoryBuilder().build(in, properties);
            }
        } catch (IOException e) {
            Logger logger = LogManager.getLogger(name + " Logger");
            logger.error(e, e);
        }
    }

    /**
     * 获取SQLSeesionFactory.
     *
     * @return
     */
    public SqlSessionFactory getSessionFactory() {
        return sessionFactory;
    }
}