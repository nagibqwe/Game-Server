/**
 * Auto generated, do not edit it
 *
 * jjc
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.game.db.bean.JJCBean;

public class JJCDao {

    public List<JJCBean> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<JJCBean> list = session.selectList("jjc.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public int insert(JJCBean data) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.insert("jjc.insert", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int update(JJCBean data) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.update("jjc.update", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int delete(long roleId) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.delete("jjc.delete", roleId);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }
}
