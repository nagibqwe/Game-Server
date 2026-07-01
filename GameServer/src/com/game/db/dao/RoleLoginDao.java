/**
 * Auto generated, do not edit it
 * <p>
 * userlogin
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.RoleLoginBean;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RoleLoginDao{

    private static final Logger logger = LogManager.getLogger(RoleLoginDao.class);

    public int insert(RoleLoginBean data) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int rows = session.insert("rolelogin.insert", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int updateName(RoleLoginBean data) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int rows = session.update("rolelogin.updateName", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int updateLv(RoleLoginBean data) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int rows = session.update("rolelogin.updateLv", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int updateCareer(RoleLoginBean data) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int rows = session.update("rolelogin.updateCareer", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int updateDelete(RoleLoginBean data) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int rows = session.update("rolelogin.updateDelete", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int updateFight(RoleLoginBean data) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int rows = session.update("rolelogin.updateFight", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int updateUserId(RoleLoginBean data) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int rows = session.update("rolelogin.updateUserId", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

}
