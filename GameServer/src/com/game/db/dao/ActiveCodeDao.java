/**
 * Auto generated, do not edit it
 * <p>
 * ActiveCode
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.ActiveCodeBean;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

public class ActiveCodeDao extends BaseDao {

    public List<ActiveCodeBean> selectAll() {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            List<ActiveCodeBean> list = session.selectList("ActiveCode.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public int update(ActiveCodeBean data) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int rows = session.update("ActiveCode.update", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public ActiveCodeBean select(String activeCode) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            return (ActiveCodeBean) session.selectOne("ActiveCode.select", activeCode);
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    //获取同类型激活码玩家是否领取过,大于0表示领取过
    public int getBatchCount(long roleId, String batch) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("get_player_id", roleId);
            map.put("batch", batch);
            return session.selectOne("ActiveCode.getBatchCount", map);
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 1;
    }

    @Override
    public int insert(String sqlName, BaseBean bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(String sqlName, BaseBean bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(String sqlName, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
