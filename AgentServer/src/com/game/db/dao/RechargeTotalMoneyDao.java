package com.game.db.dao;


import com.game.db.DBFactory;
import com.game.db.bean.RechargeTotalMoneyBean;
import com.game.db.bean.RoleLoginBean;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
public class RechargeTotalMoneyDao {

    private static final Logger logger = LogManager.getLogger(RechargeTotalMoneyDao.class);

    public List<RechargeTotalMoneyBean> selectAll() {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            List<RechargeTotalMoneyBean> list = session.selectList("rechargetotalmoney.selectAll");
            return list;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }


    public int insert(RechargeTotalMoneyBean role) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            int rows = session.insert("rechargetotalmoney.insert", role);
            session.commit();
            return rows;
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }

    public int update(RechargeTotalMoneyBean role) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            int rows = session.update("rechargetotalmoney.update", role);
            session.commit();
            return rows;
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }
}
