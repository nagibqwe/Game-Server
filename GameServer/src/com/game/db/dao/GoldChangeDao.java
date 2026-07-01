package com.game.db.dao;

import com.game.activity.struct.ActivityRankTopTen;
import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.GoldChange;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

/**
 * @author hewei@haowan123.com
 */
public class GoldChangeDao {

    public int insert(GoldChange goldChange) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.insert("goldChange.insert", goldChange);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int getUserTimeRecharge(long userId, int serverId, long beginTime, long endTime) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("serverId", serverId);
            map.put("begin", (int) (beginTime / 1000));
            map.put("end", (int) (endTime / 1000));
            Integer rows = session.selectOne("goldChange.selectRecharge", map);
            session.commit();
            if (rows == null) {
                return 0;
            }
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int getRoleRechargeGold(long roleId, int createServerId) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("roleId", roleId);
            map.put("serverId", createServerId);
            Integer rows = session.selectOne("goldChange.selectRoleRechargeGold", map);
            session.commit();
            if (rows == null) {
                return 0;
            }
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int getRoleTimeRecharge(long roleId, long beginTime, long endTime) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("roleId", roleId);
            map.put("begin", (int) (beginTime / 1000));
            map.put("end", (int) (endTime / 1000));
            Integer rows = session.selectOne("goldChange.selectRechargeByRoleId", map);
            session.commit();
            if (rows == null) {
                return 0;
            }
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public List<ActivityRankTopTen> getPlayerTimeRechargeTopTen(long beginTime, long endTime) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("begin", (int) (beginTime / 1000));
            map.put("end", (int) (endTime / 1000));
            List<ActivityRankTopTen> rows = session.selectList("goldChange.selectRechargeTopTen", map);
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public int getUserTimeConsumption(long userId, int serverId, long beginTime, long endTime) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", userId);
            map.put("serverId", serverId);
            map.put("begin", (int) (beginTime / 1000));
            map.put("end", (int) (endTime / 1000));
            Integer rows = session.selectOne("goldChange.selectConsumption", map);
            session.commit();
            if (rows == null) {
                return 0;
            }
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int getRoleTimeConsumption(long roleId, long beginTime, long endTime) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("roleId", roleId);
            map.put("begin", (int) (beginTime / 1000));
            map.put("end", (int) (endTime / 1000));
            Integer rows = session.selectOne("goldChange.selectConsumptionByRoleId", map);
            session.commit();
            if (rows == null) {
                return 0;
            }
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public List<ActivityRankTopTen> getPlayerTimeConsumptionTopTen(long beginTime, long endTime) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("begin", (int) (beginTime / 1000));
            map.put("end", (int) (endTime / 1000));
            List<ActivityRankTopTen> rows = session.selectList("goldChange.selectConsumptionTopTen", map);
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

}
