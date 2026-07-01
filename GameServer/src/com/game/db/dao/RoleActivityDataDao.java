/**
 * Auto generated, do not edit it
 *
 * roleActivityData
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.game.db.bean.RoleActivityDataBean;
import game.core.db.BaseBean;
import game.core.db.BaseDao;

public class RoleActivityDataDao extends BaseDao {

    public List<RoleActivityDataBean> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<RoleActivityDataBean> list = session.selectList("roleActivityData.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public RoleActivityDataBean select(long roleId) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            RoleActivityDataBean bean = (RoleActivityDataBean) session.selectOne("roleActivityData.select", roleId);
            return bean;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

//    public int insert(roleActivityBean data) {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
//            int rows = session.insert("roleActivityData.insert", data);
//            session.commit();
//            return rows;
//        }catch (Exception e) {
//            DBErrorToFile.error(e);
//        }
//        return 0;
//    }
//
//    public int update(roleActivityBean data) {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
//            int rows = session.update("roleActivityData.update", data);
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
