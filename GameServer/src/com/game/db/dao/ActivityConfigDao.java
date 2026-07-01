/**
 * Auto generated, do not edit it
 *
 * activityData
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.ActivityConfigBean;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class ActivityConfigDao extends BaseDao {

    public List<ActivityConfigBean> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<ActivityConfigBean> list = session.selectList("activityConfig.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    //增加根据活动Id查询活动接口
    public ActivityConfigBean selectById(int activityId) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            ActivityConfigBean bean = session.selectOne("activityConfig.selectById", activityId);
            return bean;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    //返回true表示已经存在，或者数据库查询失败，也认为不能进行
    public boolean isExitActivity(int id) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            ActivityConfigBean bean = session.selectOne("activityConfig.selectById", id);
            return bean != null;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return true;
    }

    //返回true表示已经删除，或者数据库查询失败，也认为不能进行
    public boolean isActivityDeleted(int activityType) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            ActivityConfigBean bean = session.selectOne("activityConfig.selectById", activityType);
            if (bean == null) {
                return true;
            }
            return bean.getIsDelete() == 1;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return true;
    }

//    public int insert(activityDataBean data) {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
//            int rows = session.insert("activityConfig.insert", data);
//            session.commit();
//            return rows;
//        } catch (Exception e) {
//            DBErrorToFile.error(e);
//        }
//        return 0;
//    }
//
//    public int update(activityDataBean data) {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
//            int rows = session.update("activityConfig.update", data);
//            session.commit();
//            return rows;
//        } catch (Exception e) {
//            DBErrorToFile.error(e);
//        }
//        return 0;
//    }
//
//    public int delete(long activityId) {
//        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
//            int rows = session.update("activityConfig.delete", activityId);
//            session.commit();
//            return rows;
//        } catch (Exception e) {
//            DBErrorToFile.error(e);
//        }
//        return 0;
//    }
    //服务器改时间后GM指令恢复活动，即将活动的isDelete字段值恢复为0
    public int recoverActivity(int id) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.update("activityConfig.recover", id);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
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
