/**
 * Auto generated, do not edit it
 *
 * role
 */
package com.game.db.dao;


import com.game.register.struct.UserRoleInfo;
import com.game.db.DBFactory;
import com.game.db.bean.roleBean;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class roleDao {

    private static final Logger log = LogManager.getLogger(roleDao.class);

    public List<roleBean> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<roleBean> list = session.selectList("role.selectAll");
            return list;
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    public List<UserRoleInfo> selectByUserId(long userId) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<roleBean> list = session.selectList("role.selectByUserId", userId);
            List<UserRoleInfo> infoList = new ArrayList<>();
            if (list == null) {
                return infoList;
            }
            for (roleBean bean : list) {
                infoList.add(UserRoleInfo.roleBeanToInfo(bean));
            }
            return infoList;
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    public roleBean select(long roleID) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            roleBean role = (roleBean) session.selectOne("role.select", roleID);
            return role;
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    public String selectNameById(long roleID) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            String name = (String) session.selectOne("role.selectNameById", roleID);
            return name;
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    public List<String> selectAllNames() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<String> list = session.selectList("role.selectAllNames");
            return list;
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    //获取所有角色Id和对应的角色名
    public List<roleBean> selectAllIdAndNames() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<roleBean> list = session.selectList("role.selectAllIdAndNames");
            return list;
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }

    public int insert(roleBean data) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.insert("role.insert", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            log.error(e);
        }
        return 0;
    }

    public int update(roleBean data) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.update("role.update", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            log.error(e);
        }
        return 0;
    }

    public int deleteRole(long roleid) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.update("role.deleteRole", roleid);
            session.commit();
            return rows;
        } catch (Exception e) {
            log.error(e);
        }
        return 0;
    }

    public int regainRole(long roleid) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.update("role.regainRole", roleid);
            session.commit();
            return rows;
        } catch (Exception e) {
            log.error(e);
        }
        return 0;
    }

    public int updateUserId(roleBean data) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.update("role.updateUserId", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            log.error(e);
        }
        return 0;
    }

    public int updateIconStateByRoleId(roleBean data) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.update("role.updateIconStateByRoleId", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            log.error(e);
        }
        return 0;
    }

    public int updateIconStateByUserId(roleBean data) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.update("role.updateIconStateByUserId", data);
            session.commit();
            return rows;
        } catch (Exception e) {
            log.error(e);
        }
        return 0;
    }
}
