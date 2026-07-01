package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.CrossRankBean;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by CXL on 2020/4/9.
 */
public class CrossRankDao {

    public List<CrossRankBean> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<CrossRankBean> list = session.selectList("crossrank.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public CrossRankBean selectByRoleId(long roleID){
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            CrossRankBean retbean = (CrossRankBean) session.selectOne("crossrank.selectByRoleId",roleID);
            return retbean;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;

    }


    public int insert(CrossRankBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.insert("crossrank.insert", bean);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int update(CrossRankBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.update("crossrank.update", bean);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }
    public int delete(long id) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.delete("crossrank.delete", id);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }
}
