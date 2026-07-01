/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.player.structs.PlayerWorldInfo;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author hewei@haowan123.com
 */
public class PlayerWorldInfoDao extends BaseDao {

    public List<PlayerWorldInfo> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<PlayerWorldInfo> list = session.selectList("PlayerWorldInfo.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

//    public int insert(PlayerWorldInfo info) {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
//            int rows = session.insert("PlayerWorldInfo.insert",info);
//            session.commit();
//            return rows;
//        } catch (Exception e) {
//            DBErrorToFile.error(e);
//        }
//        return 0;
//    }
//    
//    public int update(PlayerWorldInfo info) {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
//            int rows = session.update("PlayerWorldInfo.update",info);
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
