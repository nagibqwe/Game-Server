package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.RankPlayer;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class RankPlayerDao extends BaseDao {

    public List<RankPlayer> selectAll() {
        List<RankPlayer> list = new ArrayList<>();
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            list = session.selectList("rankplayer.selectAll");
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return list;
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
