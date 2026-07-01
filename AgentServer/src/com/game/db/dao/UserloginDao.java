/**
 * Auto generated, do not edit it
 *
 * userlogin
 */
package com.game.db.dao;

import com.game.db.DBFactory;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.game.db.bean.UserloginBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

public class UserloginDao {

    private static final Logger logger = LogManager.getLogger(UserloginDao.class);

    public List<UserloginBean> selectAll() {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            List<UserloginBean> list = session.selectList("userlogin.selectAll");
            return list;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    //插入新账号
    public int insert(UserloginBean user) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            int rows = session.insert("userlogin.insert", user);
            session.commit();
            return rows;
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }

    //获取登录账号
    public UserloginBean selectByUserName(String userName) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            UserloginBean userLogin = (UserloginBean) session.selectOne("userlogin.selectByUserName", userName);
            return userLogin;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    //获取登录账号
    public UserloginBean selectByUserId(long userId) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            UserloginBean userLogin = (UserloginBean) session.selectOne("userlogin.selectByUserId", userId);
            return userLogin;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    //更新登录账号信息
    public int update(UserloginBean user) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            int rows = session.update("userlogin.update", user);
            session.commit();
            return rows;
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }

    //注销账号
    public int deleteUser(long userId) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            int rows = session.update("userlogin.deleteUser", userId);
            session.commit();
            return rows;
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }

    //注销账号
    public int deleteUserByUserName(String userName) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            int rows = session.update("userlogin.deleteUserByUserName", userName);
            session.commit();
            return rows;
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }

    //恢复账号
    public int recoverUser(long userId) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            int rows = session.update("userlogin.recoverUser", userId);
            session.commit();
            return rows;
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }
}
