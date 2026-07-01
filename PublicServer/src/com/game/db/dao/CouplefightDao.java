package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.CouplefightBean;
import com.game.db.bean.CouplefightGuessBean;
import com.game.db.bean.CouplefightTeamBean;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/7/28 18:20
 */
public class CouplefightDao {

    /**
     * 查询活动信息
     * @param activityId
     * @return
     */
    public List<CouplefightBean> selectAll(int activityId) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<CouplefightBean> list = session.selectList("coupleFight.selectAll", activityId);
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    /**
     * 保存活动信息
     * @param bean
     */
    public int save(CouplefightBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int list = session.insert("coupleFight.saveCouplefight", bean);
            session.commit();
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    /**
     * 保存队伍信息
     * @param bean
     */
    public int save(CouplefightTeamBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int list = session.insert("coupleFight.saveTeam", bean);
            session.commit();
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    /**
     * 查询队伍信息
     * @param activityId
     * @return
     */
    public List<CouplefightTeamBean> selectCouplefightTeam(int activityId) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<CouplefightTeamBean> list = session.selectList("coupleFight.selectTeam", activityId);
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    /**
     * 保存竞猜数据
     * @param bean
     * @return
     */
    public int save(CouplefightGuessBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int r = session.insert("coupleFight.saveGuess", bean);
            session.commit();
            return r;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    /**
     * 查询竞猜信息
     * @param activityId
     * @return
     */
    public List<CouplefightGuessBean> selectCouplefightGuess(int activityId) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<CouplefightGuessBean> list = session.selectList("coupleFight.selectGuess", activityId);
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public void clear() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            session.delete("deleteCouplefight");
            session.delete("deleteCouplefightguess");
            session.delete("deleteCouplefightteam");
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
    }
}
