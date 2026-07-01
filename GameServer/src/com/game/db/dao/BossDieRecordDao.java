package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.BossDieRecordBean;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by zcd on 2018/4/19.
 */
public class BossDieRecordDao  extends BaseDao {

    public List<BossDieRecordBean> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<BossDieRecordBean> list = session.selectList("bossDieRecord.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
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
            int rows = session.insert(sqlName, bean);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int delete() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.delete("bossDieRecord.deleteAll");
            session.commit();
            return rows;
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
