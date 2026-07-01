/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.UserActivityBean;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import java.util.HashMap;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author hewei
 */
public class UserActivityDao extends BaseDao {

    public UserActivityBean select(long userId, int serverId) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("serverId", serverId);
            UserActivityBean bean = (UserActivityBean) session.selectOne("useractivity.select", map);
            return bean;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

//    public int insert(UserActivityBean data) {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
//            int rows = session.insert("useractivity.insert", data);
//            session.commit();
//            return rows;
//        }catch (Exception e) {
//            DBErrorToFile.error(e);
//        }
//        return 0;
//    }
//
//    public int update(UserActivityBean data) {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
//            int rows = session.update("useractivity.update", data);
//            session.commit();
//            return rows;
//        }catch (Exception e) {
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
