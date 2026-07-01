package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.PeakBean;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @Desc TODO
 * @Date 2020/10/13 11:30
 * @Auth ZUncle
 */
public class PeakDao extends BaseDao {

    /**
     * 获取巅峰排名前 size 名
     * @param size
     * @return
     */
    public List<PeakBean> selectAll(Integer size) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<PeakBean> list = session.selectList("peakpk.selectAll", size);
            session.commit();
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    /**
     * 赛季清空巅峰数据
     */
    public void clearAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            session.delete("peakpk.clearAll");
            session.commit();
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
    }

    /**
     * 插入
     *
     * @param sqlName
     * @param bean
     * @return
     */
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

    /**
     * 更新
     *
     * @param sqlName
     * @param bean
     * @return
     */
    @Override
    public int update(String sqlName, BaseBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int row = session.insert(sqlName, bean);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    /**
     * 删除
     *
     * @param sqlName
     * @param o
     * @return
     */
    @Override
    public int delete(String sqlName, Object o) {
        return 0;
    }
}
