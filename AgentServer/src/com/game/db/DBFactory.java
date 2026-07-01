package com.game.db;

import game.core.net.Config.ServerConfig;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

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
public enum DBFactory
{
    LOGIN_DB("loginDB","config/db-login-config.xml");//登录数据库

    private final Logger logger;
    private final String name;
    private SqlSessionFactory sessionFactory;

    /**
     * 初始化一个DB实例.
     *
     * @param name logger名称
     * @param config mybatis配置文件的相对路径
     */
    DBFactory(String name, String config)
    {
        this.name = name;
        this.logger = LogManager.getLogger(name + "Logger");
        try
        {
            try (InputStream in = new FileInputStream(config))
            {
                this.sessionFactory = new SqlSessionFactoryBuilder().build(in, getProperties());
            }
        }
        catch (IOException e)
        {
            logger.error(e, e);
        }
    }

    /**
     * 获取日志管理器.
     *
     * @return
     */
    public Logger getLogger()
    {
        return logger;
    }

    /**
     * 获取SQLSeesionFactory.
     *
     * @return
     */
    public SqlSessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    private Properties getProperties()
    {
        Properties properties = null;
        switch (name)
        {
            case "loginDB":
                properties = ServerConfig.getInstance().getLoginDBConfig().getProperties();
                break;
            default:
                logger.error("unknow name: [" + name + "]");
                break;
        }
        return properties;
    }
    
}
