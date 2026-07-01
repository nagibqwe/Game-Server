/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import game.core.db.bean.ConfigDbBean;
import game.core.net.Config.ServerConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang.SystemUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
public class ConfigDbDao {

    private SqlSessionFactory sessionFactory;

    public ConfigDbDao() {

        try {
            try (InputStream in = new FileInputStream("config/config-db-info.xml")) {
                this.sessionFactory = new SqlSessionFactoryBuilder().build(in, ServerConfig.getInstance().getConfigDBConfig().getProperties());
            }
        } catch (IOException e) {

        }

    }

    public List<ConfigDbBean> selectAll() {
        try (SqlSession session = sessionFactory.openSession()) {
            List<ConfigDbBean> list = session.selectList("configdb.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public ConfigDbBean select(int serverId, int lsId) {
        try (SqlSession session = sessionFactory.openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("serverId", serverId);
            map.put("lsId", lsId);
            ConfigDbBean bean = (ConfigDbBean) session.selectOne("configdb.select", map);
            return bean;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public static void main(String[] arg) throws ParserConfigurationException, SAXException, IOException {
        String filePath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "config-db.xml";
        ServerConfig.getInstance().loadConfigDB(filePath);
        ConfigDbDao bean = new ConfigDbDao();
        System.out.println(bean.select(1006, 1));
    }

}
