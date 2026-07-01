/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.ActivityRankBean;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**2
 *
 * @author lanxiang@haowan123.com
 */
public class ActivityRankDao {
    
    public List<ActivityRankBean> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<ActivityRankBean> list = session.selectList("activityrank.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public ActivityRankBean selectById(ActivityRankBean bean){
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            ActivityRankBean retbean = session.selectOne("activityrank.selectById",bean);
            return retbean;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    
    }

    public List<ActivityRankBean> selectByType(ActivityRankBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<ActivityRankBean> retbean = session.selectList("activityrank.selectByType", bean);
            return retbean;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }
    
    
    public int insert(ActivityRankBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.insert("activityrank.insert", bean);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int update(ActivityRankBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.update("activityrank.update", bean);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }
    public int delete(long id) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.delete("activityrank.delete", id);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }
}
