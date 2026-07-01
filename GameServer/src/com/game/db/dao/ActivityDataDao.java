/**
 * Auto generated, do not edit it
 *
 * activityData
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import java.util.List;

import com.game.db.bean.ActivityDataBean;
import org.apache.ibatis.session.SqlSession;
import game.core.db.BaseBean;
import game.core.db.BaseDao;

public class ActivityDataDao extends BaseDao {

    public List<ActivityDataBean> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<ActivityDataBean> list = session.selectList("activityData.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public ActivityDataBean select(int actType) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            ActivityDataBean bean = (ActivityDataBean) session.selectOne("activityData.select", actType);
            return bean;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

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
