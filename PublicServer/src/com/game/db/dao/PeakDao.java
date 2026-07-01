package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.PeakBean;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @Desc TODO
 * @Date 2020/11/16 17:15
 * @Auth ZUncle
 */
public class PeakDao {


    /**
     * 获取巅峰排名前 size 名
     * @param size
     * @return
     */
    public List<PeakBean> selectAll(Integer size) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<PeakBean> list = session.selectList("peak.selectAll", size);
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
            session.delete("peak.clearAll");
            session.commit();
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
    }

    /**
     * 更新
     *
     * @param bean
     * @return
     */
    public int update(PeakBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int row = session.insert("peak.insertOrUpdate", bean);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

}
