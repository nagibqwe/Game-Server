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

    public int insert(ServerNameBean sub) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int row = session.insert("servername.insert", sub);
            session.commit();
            return row;
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }

    public int update(ServerNameBean sub) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int row = session.update("servername.update", sub);
            session.commit();
            return row;
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }
}
