package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.guildcrossfud.struct.FudGroup;
import com.game.guildcrossfud.struct.FudRole;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @Desc TODO
 * @Date 2020/11/16 17:15
 * @Auth ZUncle
 */
public class FudDao {

    /**
     * 获取福地分组
     * @return
     */
    public List<FudGroup> selectFud() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<FudGroup> list = session.selectList("fud.selectFud");
            session.commit();
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    /**
     * 清空福地分组
     */
    public void clearFud() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            session.delete("fud.clearFud");
            session.commit();
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
    }

    /**
     * 更新
     *
     * @param bean
     * @return
     */
    public int insertOrUpdateFud(FudGroup bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int row = session.insert("fud.insertOrUpdateFud", bean);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    /**
     * 获取福地玩家积分
     * @return
     */
    public List<FudRole> selectFudRole() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<FudRole> list = session.selectList("fud.selectFudRole");
            session.commit();
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    /**
     * 清空福地分组
     */
    public void clearFudRole() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            session.delete("fud.clearFudRole");
            session.commit();
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
    }

    /**
     * 更新玩家数据
     *
     * @param bean
     * @return
     */
    public int insertOrUpdateFudRole(FudRole bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int row = session.insert("fud.insertOrUpdateFudRole", bean);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

}
