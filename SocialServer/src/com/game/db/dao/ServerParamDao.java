/**
 * Auto generated, do not edit it
 *
 * serverParam
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.ServerParamBean;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class ServerParamDao {

    public List<ServerParamBean> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<ServerParamBean> list = session.selectList("serverParam.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public int insert(ServerParamBean data) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.insert("serverParam.insert", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

}
