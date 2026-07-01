package com.game.db.dao;

import com.game.db.DBFactory;
import com.game.db.bean.RechargeReturnBean;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class RechargeReturnDao {

    private static final Logger logger = LogManager.getLogger(RechargeReturnDao.class);

    public List<RechargeReturnBean> selectAll() {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            List<RechargeReturnBean> list = session.selectList("rechargereturn.selectAll");
            return list;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public int update(RechargeReturnBean role) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            int rows = session.update("rechargereturn.update", role);
            session.commit();
            return rows;
        } catch (Exception e) {
            logger.error(e);
        }
        return 0;
    }

    //获取登录账号
    public RechargeReturnBean selectByUserName(String userName) {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            RechargeReturnBean returnBean = (RechargeReturnBean) session.selectOne("rechargereturn.selectByUserName", userName);
            return returnBean;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
}
