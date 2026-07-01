package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.player.structs.GlobalPlayerWorldInfo;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/6/23 11:34
 * @Auth ZUncle
 */
public class PlayerDao extends BaseDao {


    public List<GlobalPlayerWorldInfo> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            return session.selectList("player.selectAll");
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
            int row = session.update(sqlName, bean);
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
