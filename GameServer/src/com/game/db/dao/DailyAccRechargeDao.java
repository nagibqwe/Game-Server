package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.DailyAccRechargeBean;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @author luosv
 * Created on 2018/4/18 0018.
 */
public class DailyAccRechargeDao extends BaseDao {

    public List<DailyAccRechargeBean> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<DailyAccRechargeBean> list = session.selectList("DailyAccRecharge.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public DailyAccRechargeBean selectByRoleId(long roleId) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            DailyAccRechargeBean bean = session.selectOne("DailyAccRecharge.selectByRoleId", roleId);
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
