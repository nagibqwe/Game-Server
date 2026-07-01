package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.EightIntegralRankBean;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by CXL on 2020/4/9.
 */
public class EightIntegralRankDao {

    public List<EightIntegralRankBean> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<EightIntegralRankBean> list = session.selectList("eightintegralrank.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public EightIntegralRankBean selectByRoleId(long roleID){
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            EightIntegralRankBean retbean = (EightIntegralRankBean) session.selectOne("eightintegralrank.selectByRoleId",roleID);
            return retbean;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;

    }


    /**
     * 更新
     *
     * @param bean
     * @return
     */
    public int update(EightIntegralRankBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int row = session.insert("eightintegralrank.insertOrUpdate", bean);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int delete(long id) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.delete("eightintegralrank.delete", id);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public void clearAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            session.delete("eightintegralrank.clearAll");
            session.commit();
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
    }
}
