/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.dao;

import com.game.db.DBFactory;
import com.game.db.bean.ServerNameBean;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 *  服务器改名信息
 */
public class ServerNameDao {
    private static final Logger logger = LogManager.getLogger(ServerNameDao.class);

    public List<ServerNameBean> selectAll() {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            List<ServerNameBean> list = session.selectList("servername.selectAll");
            return list;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
}
