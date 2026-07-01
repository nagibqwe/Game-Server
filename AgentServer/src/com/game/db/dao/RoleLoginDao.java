/**
 * Auto generated, do not edit it
 * <p>
 * userlogin
 */
package com.game.db.dao;

import com.game.db.DBFactory;
import com.game.db.bean.RoleLoginBean;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RoleLoginDao {

    private static final Logger logger = LogManager.getLogger(RoleLoginDao.class);

    public List<RoleLoginBean> selectAll() {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            List<RoleLoginBean> list = session.selectList("rolelogin.selectAll");
            return list;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public List<RoleLoginBean> selectByUserId(long userId) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            List<RoleLoginBean> roleLogin = session.selectList("rolelogin.selectByUserId", userId);
            return roleLogin;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public int insert(RoleLoginBean role) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            int rows = session.insert("rolelogin.insert", role);
            session.commit();
            return rows;
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }

    public int update(RoleLoginBean role) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            int rows = session.update("rolelogin.update", role);
            session.commit();
            return rows;
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }
}
