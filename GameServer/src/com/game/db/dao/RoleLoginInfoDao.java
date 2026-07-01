/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author zhaibiao
 */
public class RoleLoginInfoDao extends BaseDao {

    /**
     * 查看数据是存在该用户
     *
     * @param userId;
     * @return
     */
    public boolean isExistUser(long userId) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int count = (int) session.selectOne("roleLoginInfo.selectCount", userId);
            return count > 0;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return false;
    }

//    public int insert(RoleLoginInfoBean bean) {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
//            int row = session.insert("roleLoginInfo.insert", bean);
//            session.commit();
//            return row;
//        } catch (Exception e) {
//            DBErrorToFile.error(e);
//        }
//        return 0;
//    }
//
//    public int update(RoleLoginInfoBean bean) {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
//            int rows = session.update("roleLoginInfo.update", bean);
//            session.commit();
//            return rows;
//        } catch (Exception e) {
//            DBErrorToFile.error(e);
//        }
//        return 0;
//    }
    @Override
    public int insert(String sqlName, BaseBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int row = session.insert(sqlName, bean);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    @Override
    public int update(String sqlName, BaseBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int row = session.update(sqlName, bean);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    @Override
    public int delete(String sqlName, Object o) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int row = session.delete(sqlName, o);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }
}
